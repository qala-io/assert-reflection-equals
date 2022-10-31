package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionAssert {
    private final Map<Class<?>, Set<String>> excludedFields = new HashMap<>();
    private boolean lenientOrder = false;
    private StringBuilder errorMessage = new StringBuilder();

    public void assertReflectionEquals(Object expectedObject, Object actualObject) {
        // Put initial objects into checkedPairs collection so to have with what to compare next pair of object
        IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs = new IdentityHashMap<>();
        checkedPairs.put(expectedObject, new IdentityHashSet<>(actualObject));
        checkedPairs.put(actualObject, new IdentityHashSet<>(expectedObject));
        assertReflectionEquals(new ArrayDeque<>(), checkedPairs, expectedObject, actualObject);
    }

    public ReflectionAssert excludeFields(Class<?> clazz, String... fieldNames) {
        excludedFields.put(clazz, new HashSet<>(Arrays.asList(fieldNames)));
        return this;
    }

    public ReflectionAssert withLenientOrder() {
        lenientOrder = true;
        return this;
    }

    /**
     * @param fullPath contains the path made of checking (checked) objects for proper building error messages
     * @param checkedPairs contains pairs of objects (expected and actual) that have been compared to each other: it also
     *                     helps to prevent going into infinite loop in case of bidirectional relationships
     * @param expectedObject expected object to be checked
     * @param actualObject   actual object to be checked
     */
    private void assertReflectionEquals(Deque<Class<?>> fullPath, IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs,
                                        Object expectedObject, Object actualObject) {
        if (expectedObject == actualObject) {
            return;
        }
        if (expectedObject == null) {
            fullPath.push(actualObject.getClass());
            BuildErrorMessage.build(fullPath, expectedObject, "", actualObject.toString(), errorMessage);
            fullPath.pop();
        } else if (actualObject == null) {
            fullPath.push(expectedObject.getClass());
            BuildErrorMessage.build(fullPath, expectedObject.toString(), "", actualObject, errorMessage);
            fullPath.pop();
        } else if (expectedObject.getClass() != actualObject.getClass()) {
            BuildErrorMessage.build(expectedObject, actualObject, errorMessage);
        } else {
            Class<?> expectedClass = expectedObject.getClass();
            fullPath.push(expectedClass);

            Field[] fields = getProperFields(expectedClass);
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().isPrimitive()) {
                    errorMessage = new ReflectionAssertEqualsPrimitives(fullPath, field).
                            assertEquals(expectedObject, actualObject, errorMessage);
                } else if (field.getType().isArray()) {
                    errorMessage = new ReflectionAssertEqualsArrays(fullPath, field, lenientOrder).
                            assertEquals(expectedObject, actualObject, errorMessage);
                } else {
                    assertReferencesEqual(fullPath, checkedPairs, field, expectedObject, actualObject);
                }
            }
            // We need to clear path for proper building error messages and for timely throwing error each time
            // when checking objects (no matter parent or child) was completed
            fullPath.pop();
        }
        if (fullPath.isEmpty() && errorMessage.length() != 0) {
            throwAssertionError(errorMessage.toString());
        }
    }

    /**
     * Gets proper array of fields in case some fields were excluded
     * @param expectedClass class of objects
     */
    private Field[] getProperFields(Class<?> expectedClass) {
        if (!excludedFields.containsKey(expectedClass)) {
            return expectedClass.getDeclaredFields();
        }
        Set<String> excludedNames = excludedFields.get(expectedClass);
        ReflectionUtil.assertExcludedFields(expectedClass, excludedNames);

        List<Field> fields = new LinkedList<>(Arrays.asList(expectedClass.getDeclaredFields()));
        fields.removeIf(field -> excludedNames.contains(field.getName()));
        return fields.toArray(new Field[0]);
    }

    /**
     * Validates child objects of parent objects whether it makes sense to compare them if we've already checked this pair
     * @param fullPath contains the path made from checking object: is necessary for proper building error messages
     * @param checkedPairs contains pars of objects that have already been compared together: helps to prevent going into infinite loop in case of bidirectional relationships
     * @param expectedField object field of parent object
     * @param expectedObject expected object to be checked
     * @param actualObject actual object to be checked
     */
    private void assertReferencesEqual(Deque<Class<?>> fullPath, IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs,
                                       Field expectedField, Object expectedObject, Object actualObject) {

        Object expected = ReflectionUtil.get(expectedField, expectedObject);
        Object actual = ReflectionUtil.get(expectedField, actualObject);

        if (expected != null && actual != null) {
            Set<Object> rightSet = checkedPairs.computeIfAbsent(expected, k -> new IdentityHashSet<>());
            if (rightSet.contains(actual))
                return;
            rightSet.add(actual);
            checkedPairs.computeIfAbsent(actual, k -> new IdentityHashSet<>()).add(expected);
        }
        assertReflectionEquals(fullPath, checkedPairs, expected, actual);
    }

    private void throwAssertionError(String message) {
        throw new AssertionError(message);
    }
}
