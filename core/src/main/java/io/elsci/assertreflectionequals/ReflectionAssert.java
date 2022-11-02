package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionAssert {
    private final Map<Class<?>, Set<String>> excludedFields = new HashMap<>();
    private boolean lenientOrder = false;

    public void assertReflectionEquals(Object expectedObject, Object actualObject) {
        // Put initial objects into initialObjects collection for proper building the 1st general part of error message
        List<Object> initialObjects = new ArrayList<>();
        initialObjects.add(expectedObject);
        initialObjects.add(actualObject);

        // Put initial objects into checkedPairs collection so to have with what to compare next child pair of objects
        IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs = new IdentityHashMap<>();
        checkedPairs.put(expectedObject, new IdentityHashSet<>(actualObject));
        checkedPairs.put(actualObject, new IdentityHashSet<>(expectedObject));
        assertReflectionEquals(expectedObject, actualObject, checkedPairs, new ArrayDeque<>(), initialObjects);
    }

    /**
     * Allows to provide the list of fields that shouldn't be compared
     * @param clazz class for which the fields are excluded
     * @param fieldNames fields to be excluded
     */
    public ReflectionAssert excludeFields(Class<?> clazz, String... fieldNames) {
        excludedFields.put(clazz, new HashSet<>(Arrays.asList(fieldNames)));
        return this;
    }

    /**
     * Allows to specify that while comparing the order of elements in an array should be ignored
     */
    public ReflectionAssert withLenientOrder() {
        lenientOrder = true;
        return this;
    }

    /**
     * @param expectedObject expected object to be checked
     * @param actualObject   actual object to be checked
     * @param initialObjects contains initial objects (expected and actual) for proper building the 1st general part of error message
     * @param fullPath       contains the path made of checking (checked) objects (field names) for proper building error messages
     * @param checkedPairs   contains pairs of objects (expected and actual) that have been compared to each other: it also
     *                       helps to prevent going into infinite loop in case of bidirectional relationships
     */
    private void assertReflectionEquals(Object expectedObject, Object actualObject,
                                        IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs,
                                        Deque<String> fullPath, List<Object> initialObjects) {
        if (expectedObject == actualObject) {
            return;
        }
        if (expectedObject == null || actualObject == null || expectedObject.getClass() != actualObject.getClass()) {
            ErrorMessageBuilder builder = new ErrorMessageBuilder(initialObjects, fullPath, new StringBuilder());
            builder.addDeepDiff(expectedObject, actualObject);
            throw new AssertionError(builder.build());
        } else {
            Field[] fields = getProperFields(expectedObject.getClass(), fullPath, initialObjects);
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().isPrimitive()) {
                    new ReflectionAssertEqualsPrimitives(field, fullPath).
                            assertEquals(expectedObject, actualObject, initialObjects);
                } else if (field.getType().isArray()) {
                    new ReflectionAssertEqualsArrays(field, lenientOrder, fullPath).
                            assertEquals(expectedObject, actualObject, initialObjects);
                } else {
                    assertReferencesEqual(expectedObject, actualObject, field, checkedPairs, fullPath, initialObjects);
                }
            }
        }
    }

    /**
     * Gets proper array of fields in case some fields were excluded
     * @param expectedClass class of comparing objects
     */
    private Field[] getProperFields(Class<?> expectedClass, Deque<String> fullPath, List<Object> initialObjects) {
        if (!excludedFields.containsKey(expectedClass))
            return expectedClass.getDeclaredFields();
        Set<String> excludedNames = excludedFields.get(expectedClass);
        ReflectionUtil.assertExcludedFields(expectedClass, excludedNames, fullPath, initialObjects);

        List<Field> fields = new LinkedList<>(Arrays.asList(expectedClass.getDeclaredFields()));
        fields.removeIf(field -> excludedNames.contains(field.getName()));
        return fields.toArray(new Field[0]);
    }

    /**
     * Validates child objects of parent objects whether it makes sense to compare them if we've already checked this pair
     * @param expectedObject expected object to be checked
     * @param actualObject   actual object to be checked
     * @param expectedField  object field of parent object
     * @param checkedPairs   contains pars of objects that have already been compared together: helps to prevent going into infinite loop in case of bidirectional relationships
     * @param fullPath       contains the path made from checking object: is necessary for proper building error messages
     * @param initialObjects contains initial objects (expected and actual) for proper building the 1st general part of error message
     */
    private void assertReferencesEqual(Object expectedObject, Object actualObject, Field expectedField,
                                       IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs,
                                       Deque<String> fullPath, List<Object> initialObjects) {

        Object expected = ReflectionUtil.get(expectedField, expectedObject);
        Object actual = ReflectionUtil.get(expectedField, actualObject);

        if (expected != null && actual != null) {
            Set<Object> rightSet = checkedPairs.computeIfAbsent(expected, k -> new IdentityHashSet<>());
            if (rightSet.contains(actual))
                return;
            rightSet.add(actual);
            checkedPairs.computeIfAbsent(actual, k -> new IdentityHashSet<>()).add(expected);
        }
        fullPath.push(expectedField.getName());
        assertReflectionEquals(expected, actual, checkedPairs, fullPath, initialObjects);
        fullPath.pop();
    }
}
