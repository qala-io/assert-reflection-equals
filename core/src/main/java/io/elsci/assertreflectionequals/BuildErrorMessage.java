package io.elsci.assertreflectionequals;

import java.util.Deque;
import java.util.Iterator;

class BuildErrorMessage {
    public static StringBuilder build(Deque<Class<?>> fullPath, Object expectedValue, String expectedName,
                                   Object actualValue, StringBuilder errorMessage) {
        errorMessage.append("Values were different for: ");
        Iterator<Class<?>> iterator = fullPath.descendingIterator();
        while (iterator.hasNext()) {
            errorMessage.append(iterator.next().getSimpleName()).append(".");
        }
        if (expectedName.isEmpty()) {
            errorMessage.setLength(errorMessage.length() - 1);
        }
        errorMessage.append(expectedName).append("\n").
                append("Expected: ").append(expectedValue).append("\n").
                append("Actual: ").append(actualValue).append("\n");
        return errorMessage;
    }

    /**
     * Is used for building error when objects don't belong to the same class
     * @param expectedValue expected object
     * @param actualValue actual object
     */
    public static StringBuilder build(Object expectedValue, Object actualValue, StringBuilder errorMessage) {
        return errorMessage.
                append("Expected and actual objects are not objects of the same class: ").append("\n").
                append("Expected: ").append(expectedValue.getClass()).append("\n").
                append("Actual: ").append(actualValue.getClass());
    }
}
