package Assertion;

import java.lang.reflect.Field;

public class Assertion {
    public static void assertReflectionEquals(Object expected, Object actual) {
        if (expected == null || actual == null) {
            return;
        }
        StringBuffer errorMessage = new StringBuffer();

        Class<?> expectedClass = expected.getClass();
        Class<?> actualClass = actual.getClass();

        Field[] expectedFields = expectedClass.getDeclaredFields();
        Field[] actualFields = actualClass.getDeclaredFields();

        for (int i = 0; i < expectedFields.length; i++) {

            expectedFields[i].setAccessible(true);
            actualFields[i].setAccessible(true);

            Class<?> expectedType = expectedFields[i].getType();
            Class<?> actualType = actualFields[i].getType();

            Number expectedValue = null;
            Number actualValue = null;

            try {
                expectedValue = (Number) expectedFields[i].get(expected);
                actualValue = (Number) actualFields[i].get(actual);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (getTypeOfValue(expectedType, expectedValue) != getTypeOfValue(actualType, actualValue)) {
                buildErrorMessage(expectedValue, expectedFields[i].getName(),
                        actualValue, actualFields[i].getName(), errorMessage);
            }
        }

        if (errorMessage.length() != 0) {
            throw new AssertionError(errorMessage);
        }

    }

    private static double getTypeOfValue(Class<?> clazz, Number value) {
        switch (clazz.getTypeName()) {
            case "byte":
                return value.byteValue();
            case "short":
                return value.shortValue();
            case "int":
                return value.intValue();
            case "long":
                return value.longValue();
            case "float":
                return value.floatValue();
            case "double":
                return value.doubleValue();
            default:
                return 0;
        }
    }

    private static void buildErrorMessage(Object expectedValue, String expectedName,
                                          Object actualValue, String actualName, StringBuffer errorMessage) {
        errorMessage.append("The ").append(expectedName).append(" of expected object is ").append(expectedValue).
                append(", but actual ").append(actualName).append(" is ").append(actualValue).append("\n");
    }
}
