package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class ReflectionAssert {
    private HashSet<String> excludedFields;

    public void assertReflectionEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            throwAssertionError("Objects are not equal since one of them is null");
        }
        StringBuilder errorMessage = new StringBuilder();

        Class<?> expectedClass = expected.getClass();
        Class<?> actualClass = actual.getClass();
        if(expectedClass != actualClass) {
            throwAssertionError("Expected " + expectedClass + ", but actual " + actualClass);
        }

        Field[] expectedFields = getProperFields(expectedClass.getDeclaredFields());
        Field[] actualFields = getProperFields(actualClass.getDeclaredFields());
        for (int i = 0; i < expectedFields.length; i++) {
            expectedFields[i].setAccessible(true);
            actualFields[i].setAccessible(true);
            try {
                if (!expectedFields[i].get(expected).equals(actualFields[i].get(actual))) {
                    buildErrorMessage(expectedFields[i].get(expected), expectedFields[i].getName(),
                            actualFields[i].get(actual), actualFields[i].getName(), errorMessage);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        if (errorMessage.length() != 0) {
            throwAssertionError(errorMessage.toString());
        }
    }

    public ReflectionAssert excludeFields(String ... fieldNames) {
        excludedFields = new HashSet<>(Arrays.asList(fieldNames));
        return this;
    }

    // Get proper array of fields in case some fields were excluded
    private Field[] getProperFields(Field[] objectFields) {
        if (excludedFields == null) {
            return objectFields;
        }
        verifyExcludedFields(excludedFields, objectFields);
        ArrayList<Field> fields = new ArrayList<>();
        for (Field objectField : objectFields) {
            objectField.setAccessible(true);
            if (!excludedFields.contains(objectField.getName())) {
                fields.add(objectField);
            }
        }
        return fields.toArray(new Field[0]);
    }

    // Verify if excluded field name belongs to the specified object
    private void verifyExcludedFields(HashSet<String> excludedFields, Field[] objectFields) {
        ArrayList<String> fields = new ArrayList<>();
        for (Field objectField : objectFields) {
            objectField.setAccessible(true);
            fields.add(objectField.getName());
        }
        Iterator<String> iterator = excludedFields.iterator();
        if (iterator.hasNext()) {
            String name = iterator.next();
            if(!fields.contains(name)) {
                throw new IllegalArgumentException(name + " field is not property of specified object");
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
