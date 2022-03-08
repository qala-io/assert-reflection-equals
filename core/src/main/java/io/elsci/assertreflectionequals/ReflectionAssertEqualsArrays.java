package io.elsci.assertreflectionequals;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Deque;
import java.util.Objects;

public class ReflectionAssertEqualsArrays {
    private final Deque<Class<?>> listOfClasses;
    private final Field field;
    private final Object expectedObject;
    private final Object actualObject;
    private final StringBuilder errorMessage;
    private final boolean lenientOrder;

    public ReflectionAssertEqualsArrays(Deque<Class<?>> listOfClasses, boolean lenientOrder, Field field,
                                        Object expectedObject, Object actualObject, StringBuilder errorMessage) {
        this.listOfClasses = listOfClasses;
        this.field = field;
        this.expectedObject = expectedObject;
        this.actualObject = actualObject;
        this.errorMessage = errorMessage;
        this.lenientOrder = lenientOrder;
    }

    public StringBuilder compare() {
        try {
            if (!compareArrays(field.get(expectedObject), field.get(actualObject))) {
                return BuildErrorMessage.build(listOfClasses,
                        Arrays.toString(getArrayWithValues(field.get(expectedObject))), field.getName(),
                        Arrays.toString(getArrayWithValues(field.get(actualObject))), errorMessage);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return errorMessage;
    }

    // Compare arrays with primitives
    private boolean compareArrays(Object expectedObject, Object actualObject) {
        if(expectedObject == null && actualObject == null) {
            return true;
        }
        if(expectedObject == null || actualObject == null) {
            return false;
        }
        Object[] expectedArray = getArrayWithValues(expectedObject);
        Object[] actualArray = getArrayWithValues(actualObject);
        if(expectedArray.length != actualArray.length) {
            return false;
        }
        if (lenientOrder) {
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
}
