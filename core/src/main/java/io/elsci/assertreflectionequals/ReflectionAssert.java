package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionAssert {
    private final Set<String> excludedFields = new HashSet<>();

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
            try {
                if(!field.get(expected).equals(field.get(actual))) {
                    buildErrorMessage(field.get(expected), field.getName(), field.get(actual), field.getName(), errorMessage);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
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

    private void buildErrorMessage(Object expectedValue, String expectedName,
                                          Object actualValue, String actualName, StringBuilder errorMessage) {
        errorMessage.append("The ").append(expectedName).append(" of expected object is ").append(expectedValue).
                append(", but actual ").append(actualName).append(" is ").append(actualValue).append("\n");
    }

    private void throwAssertionError(String message) {
        throw new AssertionError(message);
    }
}
