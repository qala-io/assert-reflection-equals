package io.elsci.assertreflectionequals;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Deque;
import java.util.Objects;

class ReflectionAssertEqualsArrays {
    private final Deque<String> fullPath;
    private final Field field;
    private final boolean lenientOrder;

    public ReflectionAssertEqualsArrays(Deque<String> fullPath, Field field, boolean lenientOrder) {
        this.fullPath = fullPath;
        this.field = field;
        this.lenientOrder = lenientOrder;
    }

    public StringBuilder assertEquals(Object expectedObject, Object actualObject, StringBuilder errorMessage) {
        fullPath.push(field.getName());
        if (!compareArrays(ReflectionUtil.get(field, expectedObject), ReflectionUtil.get(field, actualObject))) {
            BuildErrorMessage.build(fullPath,
                    Arrays.toString(getArrayWithValues(ReflectionUtil.get(field, expectedObject))), field.getName(),
                    Arrays.toString(getArrayWithValues(ReflectionUtil.get(field, actualObject))), errorMessage);
        }
        fullPath.pop();
        return errorMessage;
    }

    // Compare arrays with primitives
    private boolean compareArrays(Object expectedObject, Object actualObject) {
        if (expectedObject == actualObject) {
            return true;
        }
        if (expectedObject == null || actualObject == null) {
            return false;
        }
        Object[] expectedArray = getArrayWithValues(expectedObject);
        Object[] actualArray = getArrayWithValues(actualObject);
        if (expectedArray.length != actualArray.length) {
            return false;
        }
        if (lenientOrder) {
            Arrays.sort(expectedArray);
            Arrays.sort(actualArray);
        }
        for (int i = 0; i < expectedArray.length; i++) {
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
        if (o == null) {
            return null;
        }
        Object[] elements = new Object[Array.getLength(o)];
        for (int i = 0; i < Array.getLength(elements); i++) {
            Object element = Array.get(o, i);
            elements[i] = element;
        }
        return elements;
    }
}
