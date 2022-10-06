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
     * @param fullPath       contains path to the property: necessary for proper displaying error messages
     * @param checkedPairs contains pars of objects that were already compared together
     * @param expectedObject object to be checked
     * @param actualObject   object to be checked
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
            if (fullPath.size() != 0) {
                return;
            }
        } else if (actualObject == null) {
            fullPath.push(expectedObject.getClass());
            BuildErrorMessage.build(fullPath, expectedObject.toString(), "", actualObject, errorMessage);
            fullPath.pop();
            if (fullPath.size() != 0) {
                return;
            }
        } else if (expectedObject.getClass() != actualObject.getClass()) {
            fullPath.push(expectedObject.getClass());
            fullPath.push(actualObject.getClass());
            BuildErrorMessage.build(fullPath, expectedObject.getClass(), "", actualObject.getClass(), errorMessage);
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
            fullPath.pop();
        }
        if (errorMessage.length() != 0 && fullPath.size() == 0) {
            throwAssertionError(errorMessage.toString());
        }
    }

    // Get proper array of fields in case some fields were excluded
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

    // Verify if excluded field name belongs to the specified object
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

    private void assertReferencesEqual(Deque<Class<?>> fullPath, Map<Object, Object> checkedPairs, Field expectedField,
                                       Object expectedObject, Object actualObject) {
        // This check is actual only for initial pair of objects
        if (checkedPairs.isEmpty()) {
            IdentityHashMap<Object, Object> value = new IdentityHashMap<>();
            value.put(actualObject, null);
            checkedPairs.put(expectedObject, value);
        }

        Object expected = ReflectionUtil.get(expectedField, expectedObject);
        Object actual = ReflectionUtil.get(expectedField, actualObject);

        // There's no point in iterating collection with checked pairs if at least one of the objects is null
        if (expected == null || actual == null) {
            assertReflectionEquals(fullPath, checkedPairs, expected, actual);
        } else {
            for (Map.Entry<Object, Object> entry : checkedPairs.entrySet()) {
                // In the following checks we will try to map our objects with objects(keys) that were already checked
                if (entry.getKey() == expected) {
                    IdentityHashMap<Object, Object> value = (IdentityHashMap<Object, Object>) entry.getValue();
                    if (!value.containsKey(actual)) {
                        value.put(actual, null);
                        entry.setValue(value);
                        assertReflectionEquals(fullPath, checkedPairs, expected, actual);
                    } else {
                        return;
                    }
                }
                if (entry.getKey() == actual) {
                    IdentityHashMap<Object, Object> value = (IdentityHashMap<Object, Object>) entry.getValue();
                    if (!value.containsKey(expected)) {
                        value.put(expected, null);
                        entry.setValue(value);
                        assertReflectionEquals(fullPath, checkedPairs, expected, actual);
                    } else {
                        return;
                    }
                }
            }
            assertReflectionEquals(fullPath, checkedPairs, expected, actual);
            if (fullPath.size() != 0) {
                return;
            }
            fullPath.pop();
        }
    }

    private void throwAssertionError(String message) {
        throw new AssertionError(message);
    }
}
