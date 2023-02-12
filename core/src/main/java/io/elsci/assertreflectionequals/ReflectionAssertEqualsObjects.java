package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionAssertEqualsObjects {

    /** Is used for comparing initial, child objects or objects from arrays
     * @param expectedObject expected object to be checked
     * @param actualObject   actual object to be checked
     * @param lenientOrder   indicates if order of elements in an array should be ignored or not
     * @param excludedFields provides the list of fields that shouldn't be compared
     * @param checkedPairs   contains pairs of objects (expected and actual) that have been compared to each other: it also helps to prevent going into infinite loop in case of bidirectional relationships
     * @param initialObjects contains initial objects (expected and actual) for proper building the 1st general part of error message
     * @param fullPath       contains the path made of checking (checked) objects (field names) for proper building error messages
     */
    public void assertReflectionEquals(Object expectedObject, Object actualObject, boolean lenientOrder,
                                       Map<Class<?>, Set<String>> excludedFields,
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
            Field[] fields = getProperFields(expectedObject.getClass(), fullPath, excludedFields, initialObjects);
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().isPrimitive()) {
                    new ReflectionAssertEqualsPrimitives(field, fullPath).
                            assertEquals(expectedObject, actualObject, initialObjects);
                } else if (field.getType().isArray()) {
                    new ReflectionAssertEqualsArrays()
                            .assertEquals(expectedObject, actualObject, field, lenientOrder, excludedFields,
                                    checkedPairs, fullPath, initialObjects);
                } else {
                    ReflectionUtil.assertReferencesEqual(
                            ReflectionUtil.get(field, expectedObject), ReflectionUtil.get(field, actualObject),
                            checkedPairs, fullPath, field.getName(), lenientOrder, excludedFields, initialObjects);
                }
            }
        }
    }

    /**
     * Gets proper array of fields in case some fields were excluded
     * @param expectedClass class of comparing objects
     */
    private static Field[] getProperFields(Class<?> expectedClass, Deque<String> fullPath,
                                           Map<Class<?>, Set<String>> excludedFields, List<Object> initialObjects) {
        if (!excludedFields.containsKey(expectedClass))
            return expectedClass.getDeclaredFields();
        Set<String> excludedNames = excludedFields.get(expectedClass);
        assertExcludedFields(expectedClass, excludedNames, fullPath, initialObjects);

        List<Field> fields = new LinkedList<>(Arrays.asList(expectedClass.getDeclaredFields()));
        fields.removeIf(field -> excludedNames.contains(field.getName()));
        return fields.toArray(new Field[0]);
    }

    /**
     * Validates if excluded field name belongs to the specified object
     * @param expectedClass class of comparing objects
     * @param excludedNames set of fields that must be excluded from the checking
     */
    private static void assertExcludedFields(Class<?> expectedClass, Set<String> excludedNames,
                                             Deque<String> fullPath, List<Object> initialObjects) {
        List<String> fieldNames = new ArrayList<>();
        for (Field objectField : expectedClass.getDeclaredFields()) {
            fieldNames.add(objectField.getName());
        }
        for (String name : excludedNames) {
            if (!fieldNames.contains(name)) {
                ErrorMessageBuilder builder = new ErrorMessageBuilder(initialObjects, fullPath, new StringBuilder());
                builder.addStringWithMismatchedField(name, expectedClass.getSimpleName());
                throw new IllegalArgumentException(builder.build());
            }
        }
    }
}
