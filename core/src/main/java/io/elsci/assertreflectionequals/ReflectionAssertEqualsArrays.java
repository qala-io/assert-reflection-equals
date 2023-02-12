package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

class ReflectionAssertEqualsArrays {
    private final Map<Object, List<Object>> problemValues = new HashMap<>();
    private static final HashSet<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(Boolean.class, Character.class,
            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class, String.class));

    public void assertEquals(Object expectedObject, Object actualObject, Field field, boolean lenientOrder,
                             Map<Class<?>, Set<String>> excludedFields,
                             IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs,
                             Deque<String> fullPath, List<Object> initialObjects) {
        if (!compareArrays(ReflectionUtil.get(field, expectedObject), ReflectionUtil.get(field, actualObject), field,
                lenientOrder, excludedFields, checkedPairs, fullPath, initialObjects)) {
            fullPath.push(field.getName());
            Object[] expectedArray = ReflectionUtil.getArrayWithValues(ReflectionUtil.get(field, expectedObject));
            Object[] actualArray = ReflectionUtil.getArrayWithValues(ReflectionUtil.get(field, actualObject));

            ErrorMessageBuilder builder = new ErrorMessageBuilder(initialObjects, fullPath, new StringBuilder());
            builder.addArraysDiff(expectedArray, actualArray, problemValues)
                    .addDeepDiff(expectedObject, actualObject);
            throw new AssertionError(builder.build());
        }
    }

    /**
     * @param expectedObject expected array
     * @param actualObject   actual array
     * @param lenientOrder   is used only for array fields that belong to initial or child objects (not to objects that contain inside arrays)
     */
    public boolean compareArrays(Object expectedObject, Object actualObject, Field field, boolean lenientOrder,
                                 Map<Class<?>, Set<String>> excludedFields,
                                 IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs, Deque<String> fullPath,
                                 List<Object> initialObjects) {
        if (expectedObject == actualObject) {
            return true;
        }
        if (expectedObject == null || actualObject == null) {
            return false;
        }
        Object[] expectedArray = ReflectionUtil.getArrayWithValues(expectedObject);
        Object[] actualArray = ReflectionUtil.getArrayWithValues(actualObject);
        if (expectedArray.length == 0 && actualArray.length == 0) {
            return true;
        }
        if (expectedArray.length != actualArray.length) {
            return false;
        }
        if (lenientOrder) {
            Arrays.sort(expectedArray);
            Arrays.sort(actualArray);
        }
        for (int i = 0; i < expectedArray.length; i++) {
            Object expected = expectedArray[i];
            Object actual = actualArray[i];

            // Check if array contains objects or primitives
            if (!WRAPPER_TYPES.contains(expected.getClass())) {
                ReflectionUtil.assertReferencesEqual(expected, actual, checkedPairs, fullPath,
                        field.getName() + "[" + i + "]", lenientOrder, excludedFields, initialObjects);
            } else if (!(Objects.equals(expected, actual))) {
                List<Object> values = new ArrayList<>();
                values.add(expected);
                values.add(actual);
                problemValues.put(i, values);
                break;
            }
        }
        return problemValues.isEmpty();
    }
}