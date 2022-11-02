package io.elsci.assertreflectionequals;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

class ReflectionAssertEqualsArrays {
    private final Field field;
    private final boolean lenientOrder;
    private final Deque<String> fullPath;
    private final Map<Object, List<Object>> problemValues = new HashMap<>();

    public ReflectionAssertEqualsArrays(Field field, boolean lenientOrder, Deque<String> fullPath) {
        this.field = field;
        this.lenientOrder = lenientOrder;
        this.fullPath = fullPath;
    }

    public void assertEquals(Object expectedObject, Object actualObject, List<Object> initialObjects) {
        if (!compareArrays(ReflectionUtil.get(field, expectedObject), ReflectionUtil.get(field, actualObject))) {
            fullPath.push(field.getName());
            Object[] expectedArray = getArrayWithValues(ReflectionUtil.get(field, expectedObject));
            Object[] actualArray = getArrayWithValues(ReflectionUtil.get(field, actualObject));

            ErrorMessageBuilder builder = new ErrorMessageBuilder(initialObjects, fullPath, new StringBuilder());
            builder.addArraysDiff(expectedArray, actualArray, problemValues)
                    .addDeepDiff(expectedObject, actualObject);
            throw new AssertionError(builder.build());
        }
    }

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
                List<Object> values = new ArrayList<>();
                values.add(exp);
                values.add(act);
                problemValues.put(i, values);
                break;
            }
        }
        return problemValues.isEmpty();
    }

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
