package io.elsci.assertreflectionequals;

import java.util.Deque;
import java.util.Iterator;

public class BuildErrorMessage {
    public static StringBuilder build(Deque<Class<?>> listOfClasses, Object expectedValue, String expectedName,
                                   Object actualValue, StringBuilder errorMessage) {
        errorMessage.append("Values were different for: ");
        Iterator<Class<?>> iterator = listOfClasses.descendingIterator();
        while(iterator.hasNext()) {
            errorMessage.append(iterator.next().getSimpleName()).append(".");
        }
        errorMessage.append(expectedName).append("\n").
                append("Expected: ").append(expectedValue).append("\n").
                append("Actual: ").append(actualValue).append("\n");
        return errorMessage;
    }
}
