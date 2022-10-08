package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionAssert {
    private final Map<Class<?>, Set<String>> excludedFields = new HashMap<>();
    private boolean lenientOrder = false;
    private StringBuilder errorMessage = new StringBuilder();

    public void assertReflectionEquals(Object expectedObject, Object actualObject) {
        assertReflectionEquals(new ArrayDeque<>(), new IdentityHashMap<>(), expectedObject, actualObject);
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
     * @param checkedPairs contains pairs of objects (expected and actual) that were compared to each other: it also
     *                     helps to prevent going into infinite loop in case of bidirectional relationships
     * @param expectedObject expected object to be checked
     * @param actualObject   actual object to be checked
     */
    private void assertReflectionEquals(Deque<Class<?>> fullPath, Map<Object, Object> checkedPairs,
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
            fullPath.push(expectedObject.getClass());
            fullPath.push(actualObject.getClass());
            BuildErrorMessage.build(fullPath, expectedObject.getClass(), "", actualObject.getClass(), errorMessage);
            fullPath.clear();
        } else {
            Class<?> expectedClass = expectedObject.getClass();
            fullPath.push(expectedClass);

            Field[] fields = getProperFields(expectedClass, expectedClass.getDeclaredFields());
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
        if (errorMessage.length() != 0 && fullPath.size() == 0) {
            throwAssertionError(errorMessage.toString());
        }
    }

    /**
     * Gets proper array of fields in case some fields were excluded
     * @param expectedClass class of objects
     * @param objectFields fields of comparing objects
     */
    private Field[] getProperFields(Class<?> expectedClass, Field[] objectFields) {
        if (excludedFields.isEmpty() || !excludedFields.containsKey(expectedClass)) {
            return objectFields;
        }
        Set<String> excludedNames = excludedFields.get(expectedClass);
        verifyExcludedFields(objectFields, excludedNames, expectedClass);
        ArrayList<Field> fields = new ArrayList<>();
        for (Field objectField : objectFields) {
            objectField.setAccessible(true);
            if (!excludedNames.contains(objectField.getName())) {
                fields.add(objectField);
            }
        }
        return fields.toArray(new Field[0]);
    }

    /**
     * Validates if excluded field name belongs to the specified object
     * @param objectFields fields of comparing objects
     * @param excludedNames set of fields that must be excluded from the checking
     */
    private void verifyExcludedFields(Field[] objectFields, Set<String> excludedNames, Class<?> expectedClass) {
        ArrayList<String> fieldNames = new ArrayList<>();
        for (Field objectField : objectFields) {
            objectField.setAccessible(true);
            fieldNames.add(objectField.getName());
        }
        for (String name : excludedNames) {
            if (!fieldNames.contains(name)) {
                throw new IllegalArgumentException(name + " is not field of " + expectedClass.getSimpleName() + " class");
            }
        }
    }

    /**
     * Validates child objects of parent objects whether it makes sense to compare them if we've already checked this pair
     * @param fullPath contains the path made from checking object: is necessary for proper building error messages
     * @param checkedPairs contains pars of objects that have already been compared together: helps to prevent going into infinite loop in case of bidirectional relationships
     * @param expectedField object field of parent object
     * @param expectedObject expected object to be checked
     * @param actualObject actual object to be checked
     */
    private void assertReferencesEqual(Deque<Class<?>> fullPath, Map<Object, Object> checkedPairs, Field expectedField,
                                       Object expectedObject, Object actualObject) {
        if (checkedPairs.isEmpty()) {
            IdentityHashMap<Object, Object> value = new IdentityHashMap<>();
            value.put(actualObject, null);
            checkedPairs.put(expectedObject, value);
        }

        Object expected = ReflectionUtil.get(expectedField, expectedObject);
        Object actual = ReflectionUtil.get(expectedField, actualObject);

        if (expected != null && actual != null) {
            for (Map.Entry<Object, Object> entry : checkedPairs.entrySet()) {
                // In the following checks we will try to map our objects with objects (keys) that were already checked
                IdentityHashMap<Object, Object> value = (IdentityHashMap<Object, Object>) entry.getValue();
                if (entry.getKey() == expected) {
                    if (!value.containsKey(actual)) {
                        value.put(actual, null);
                        entry.setValue(value);
                        assertReflectionEquals(fullPath, checkedPairs, expected, actual);
                    }
                    return;
                }
                if (entry.getKey() == actual) {
                    if (!value.containsKey(expected)) {
                        value.put(expected, null);
                        entry.setValue(value);
                        assertReflectionEquals(fullPath, checkedPairs, expected, actual);
                    }
                    return;
                }
            }
        }
        assertReflectionEquals(fullPath, checkedPairs, expected, actual);
    }

    private void throwAssertionError(String message) {
        throw new AssertionError(message);
    }
}
