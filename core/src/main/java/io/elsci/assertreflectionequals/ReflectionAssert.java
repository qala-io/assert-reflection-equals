package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;

public class ReflectionAssert {
    public static void assertReflectionEquals(Object expected, Object actual) {
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
            throwAssertionError("Objects are not equal since their classes are different");
        }

        Field[] expectedFields = expectedClass.getDeclaredFields();
        Field[] actualFields = actualClass.getDeclaredFields();

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

    private static void buildErrorMessage(Object expectedValue, String expectedName,
                                          Object actualValue, String actualName, StringBuilder errorMessage) {
        errorMessage.append("The ").append(expectedName).append(" of expected object is ").append(expectedValue).
                append(", but actual ").append(actualName).append(" is ").append(actualValue).append("\n");
    }

    private static void throwAssertionError(String message) {
        throw new AssertionError(message);
    }
}
