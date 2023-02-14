package io.elsci.assertreflectionequals;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

class ReflectionUtil {

    /**
     * Gets the field value of provided object
     */
    public static Object get(Field field, Object obj) {
        Object object;
        try {
            object = field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    /**
     * Retrieves array from array object
     */
    public static Object[] getArrayWithValues(Object o) {
        if (o == null) {
            return null;
        }
        // We'll not be able to build 'elements' array if initial array object is empty
        if (Array.getLength(o) == 0) {
            return (Object[]) Array.newInstance(o.getClass(), Array.getLength(o));
        }
        Object[] elements = (Object[]) Array.newInstance(Array.get(o, 0).getClass(), Array.getLength(o));
        for (int i = 0; i < Array.getLength(elements); i++) {
            Object element = Array.get(o, i);
            elements[i] = element;
        }
        return elements;
    }

    /**
     * Validates objects (initial, child, objects from array) whether it makes sense to compare them if we've already checked this pair
     * @param expectedObject expected object to be checked
     * @param actualObject   actual object to be checked
     * @param fieldName  field name of parent object
     * @param checkedPairs   contains pars of objects that have already been compared together: helps to prevent going into infinite loop in case of bidirectional relationships
     * @param lenientOrder   indicates if order of elements in an array should be ignored or not (required for further invoking {@link ReflectionAssertEqualsObjects#assertReflectionEquals(Object, Object, boolean, Map, IdentityHashMap, Deque, List)} method)
     * @param excludedFields set of fields that must be excluded from the checking (required for further invoking {@link ReflectionAssertEqualsObjects#assertReflectionEquals(Object, Object, boolean, Map, IdentityHashMap, Deque, List)} method)
     * @param fullPath       contains the path made from checking object: is necessary for proper building error messages
     * @param initialObjects contains initial objects (expected and actual) for proper building the 1st general part of error message (required for further invoking {@link ReflectionAssertEqualsObjects#assertReflectionEquals(Object, Object, boolean, Map, IdentityHashMap, Deque, List)} method)
     */
    public static void assertReferencesEqual(Object expectedObject, Object actualObject,
                                             IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs,
                                             Deque<String> fullPath, String fieldName, boolean lenientOrder,
                                             Map<Class<?>, Set<String>> excludedFields, List<Object> initialObjects) {

        if (expectedObject != null && actualObject != null) {
            Set<Object> rightSet = checkedPairs.computeIfAbsent(expectedObject, k -> new IdentityHashSet<>());
            if (rightSet.contains(actualObject))
                return;
            rightSet.add(actualObject);
            checkedPairs.computeIfAbsent(actualObject, k -> new IdentityHashSet<>()).add(expectedObject);
        }
        fullPath.push(fieldName);
        new ReflectionAssertEqualsObjects().assertReflectionEquals(expectedObject, actualObject, lenientOrder,
                excludedFields, checkedPairs, fullPath, initialObjects);
        fullPath.pop();
    }
}