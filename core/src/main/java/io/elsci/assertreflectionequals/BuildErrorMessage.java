package io.elsci.assertreflectionequals;

import java.util.Deque;
import java.util.Iterator;

class BuildErrorMessage {
    public static void build(Deque<String> fullPath, Object expectedValue, String expectedName,
                             Object actualValue, StringBuilder errorMessage) {
        if (!fullPath.isEmpty()) {
            errorMessage.append("Values were different for: ");
            Iterator<String> iterator = fullPath.descendingIterator();
            while (iterator.hasNext()) {
                errorMessage.append(iterator.next()).append(".");
            }
            errorMessage.setLength(errorMessage.length() - 1);
        }
        errorMessage.append("\n").append("Expected: ").append(expectedValue).append("\n").
                append("Actual: ").append(actualValue).append("\n");
    }

    /**
     * Is used for building error when objects don't belong to the same class
     *
     * @param expectedValue expected object
     * @param actualValue   actual object
     */
    public static void build(Object expectedValue, Object actualValue, StringBuilder errorMessage) {
        errorMessage.
                append("Expected and actual objects are not objects of the same class: ").append("\n").
                append("Expected: ").append(expectedValue.getClass()).append("\n").
                append("Actual: ").append(actualValue.getClass());
    }
}
