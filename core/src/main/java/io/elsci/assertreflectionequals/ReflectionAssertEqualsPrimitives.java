package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.Deque;

public class ReflectionAssertEqualsPrimitives {
    private final Deque<Class<?>> listOfClasses;
    private final Field field;
    private final Object expectedObject;
    private final Object actualObject;
    private final StringBuilder errorMessage;

    public ReflectionAssertEqualsPrimitives(Deque<Class<?>> listOfClasses, Field field, Object expectedObject,
                                            Object actualObject, StringBuilder errorMessage) {
        this.listOfClasses = listOfClasses;
        this.field = field;
        this.expectedObject = expectedObject;
        this.actualObject = actualObject;
        this.errorMessage = errorMessage;
    }

    public StringBuilder compare() {
        try {
            if (!field.get(expectedObject).equals(field.get(actualObject))) {
                return BuildErrorMessage.build(listOfClasses, field.get(expectedObject), field.getName(),
                        field.get(actualObject), errorMessage);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return errorMessage;
    }
}
