package io.elsci.assertreflectionequals;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class ReflectionAssert {
    private final Set<String> excludedFields = new HashSet<>();
    private boolean sortArray = false;

    public void assertReflectionEquals(Object expected, Object actual) {
        if(expected == null && actual == null) {
            return;
        }
        if(expected == null || actual == null) {
            throwAssertionError("Objects are not equal since one of them is null");
        }
        StringBuilder errorMessage = new StringBuilder();

        Class<?> expectedClass = expected.getClass();
        Class<?> actualClass = actual.getClass();
        if(expectedClass != actualClass) {
            throwAssertionError("Expected " + expectedClass + ", but actual " + actualClass);
        }

        Field[] fields = getProperFields(expectedClass.getDeclaredFields());
        for(Field field : fields) {
            field.setAccessible(true);
            if(field.getType().isPrimitive()) {
                assertPrimitivesEqual(expectedClass, field, expected, actual, errorMessage);
            } else if(field.getType().isArray()) {
                assertArrayEqual(expectedClass, field, expected, actual, errorMessage);
            } else {
                throw new UnsupportedOperationException("This operation is not supported yet");
            }
        }

        if(errorMessage.length() != 0) {
            throwAssertionError(errorMessage.toString());
        }
    }

    public ReflectionAssert excludeFields(String ... fieldNames) {
        excludedFields.addAll(Arrays.asList(fieldNames));
        return this;
    }

    public ReflectionAssert withLenientOrder() {
        sortArray = true;
        return this;
    }

    // Get proper array of fields in case some fields were excluded
    private Field[] getProperFields(Field[] objectFields) {
        if(excludedFields.isEmpty()) {
            return objectFields;
        }
        verifyExcludedFields(excludedFields, objectFields);
        ArrayList<Field> fields = new ArrayList<>();
        for(Field objectField : objectFields) {
            objectField.setAccessible(true);
            if(!excludedFields.contains(objectField.getName())) {
                fields.add(objectField);
            }
        }
        return fields.toArray(new Field[0]);
    }

    // Verify if excluded field name belongs to the specified object
    private void verifyExcludedFields(Set<String> excludedFields, Field[] objectFields) {
        ArrayList<String> fields = new ArrayList<>();
        for(Field objectField : objectFields) {
            objectField.setAccessible(true);
            fields.add(objectField.getName());
        }
        for(String name : excludedFields) {
            if(!fields.contains(name)) {
                throw new IllegalArgumentException(name + " is not field of specified object");
            }
        }
    }

    private void assertPrimitivesEqual(Class<?> clazz, Field field, Object expected, Object actual, StringBuilder errorMessage) {
        try {
            if (!field.get(expected).equals(field.get(actual))) {
                buildErrorMessage(clazz, field.get(expected), field.getName(), field.get(actual), field.getName(), errorMessage);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertArrayEqual(Class<?> clazz, Field field, Object expected, Object actual, StringBuilder errorMessage) {
        try {
            if (!compareArrays(field.get(expected), field.get(actual))) {
                buildErrorMessage(clazz, Arrays.toString(getArrayWithValues(field.get(expected))), field.getName(),
                        Arrays.toString(getArrayWithValues(field.get(actual))), field.getName(), errorMessage);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Compare arrays with primitives
    private boolean compareArrays(Object expected, Object actual) {
        if(expected == null && actual == null) {
            return true;
        }
        if(expected == null || actual == null) {
            return false;
        }
        Object[] expectedArray = getArrayWithValues(expected);
        Object[] actualArray = getArrayWithValues(actual);
        if(expectedArray.length != actualArray.length) {
            return false;
        }
        if (sortArray) {
            Arrays.sort(expectedArray);
            Arrays.sort(actualArray);
        }
        for(int i = 0; i < expectedArray.length; i++) {
            Object exp = expectedArray[i];
            Object act = actualArray[i];
            if (!(Objects.equals(exp, act))) {
                return false;
            }
        }
        return true;
    }

    // Get array with primitives
    private Object[] getArrayWithValues(Object o) {
        if(o == null) {
            return null;
        }
        Object[] elements = new Object[Array.getLength(o)];
        for(int i = 0; i < Array.getLength(elements); i++) {
            Object element = Array.get(o, i);
            elements[i] = element;
        }
        return elements;
    }

    private void buildErrorMessage(Class<?> clazz, Object expectedValue, String expectedName,
                                          Object actualValue, String actualName, StringBuilder errorMessage) {
        errorMessage.append("Expected: ").append(clazz.getSimpleName()).append(".").append(expectedName).
                append(" is ").append(expectedValue).append(", actual: ").append(clazz.getSimpleName()).
                append(".").append(actualName).append(" is ").append(actualValue).append("\n");
    }

    private void throwAssertionError(String message) {
        throw new AssertionError(message);
    }
}
