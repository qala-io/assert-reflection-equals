package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionAssert {
    private final Set<String> excludedFields = new HashSet<>();
    private boolean lenientOrder = false;

    public void assertReflectionEquals(Object expectedObject, Object actualObject) {
        assertReflectionEquals(new ArrayDeque<>(), expectedObject, actualObject);
    }

    public ReflectionAssert excludeFields(String ... fieldNames) {
        excludedFields.addAll(Arrays.asList(fieldNames));
        return this;
    }

    public ReflectionAssert withLenientOrder() {
        lenientOrder = true;
        return this;
    }

    private void assertReflectionEquals(Deque<Class<?>> listOfClasses, Object expectedObject, Object actualObject) {
        StringBuilder errorMessage = new StringBuilder();

        if (expectedObject == null && actualObject == null) {
            return;
        } else if (expectedObject == null) {
            listOfClasses.push(actualObject.getClass());
            BuildErrorMessage.build(listOfClasses, expectedObject, "", actualObject.getClass(),
                    errorMessage);
        } else if (actualObject == null) {
            listOfClasses.push(expectedObject.getClass());
            BuildErrorMessage.build(listOfClasses, expectedObject.getClass(), "", actualObject,
                    errorMessage);
        } else if (expectedObject.getClass() != actualObject.getClass()) {
            listOfClasses.push(expectedObject.getClass());
            listOfClasses.push(actualObject.getClass());
            BuildErrorMessage.build(listOfClasses, expectedObject.getClass(), "", actualObject.getClass(),
                    errorMessage);
        } else {
            Class<?> expectedClass = expectedObject.getClass();
            listOfClasses.push(expectedClass);

            Field[] fields = getProperFields(expectedClass.getDeclaredFields());
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().isPrimitive()) {
                    errorMessage = new ReflectionAssertEqualsPrimitives(listOfClasses, field, expectedObject,
                            actualObject, errorMessage).compare();
                } else if (field.getType().isArray()) {
                    errorMessage = new ReflectionAssertEqualsArrays(listOfClasses, lenientOrder, field, expectedObject,
                            actualObject, errorMessage).compare();
                } else {
                    assertReferencesEqual(listOfClasses, field, expectedObject, actualObject);
                }
            }
        }
        if (errorMessage.length() != 0) {
            throwAssertionError(errorMessage.toString());
        }
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

    private void assertReferencesEqual(Deque<Class<?>> listOfClasses, Field expectedField, Object expected, Object actual) {
        try {
            assertReflectionEquals(listOfClasses, expectedField.get(expected), expectedField.get(actual));
            listOfClasses.pop();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void throwAssertionError(String message) {
        throw new AssertionError(message);
    }
}
