package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionAssert {
    private final Map<Class<?>, Set<String>> excludedFields = new HashMap<>();
    private boolean lenientOrder = false;

    public void assertReflectionEquals(Object expectedObject, Object actualObject) {
        assertReflectionEquals(new ArrayDeque<>(), new HashSet<>(), expectedObject, actualObject);
    }

    public ReflectionAssert excludeFields(Class<?> clazz, String ... fieldNames) {
        excludedFields.put(clazz, new HashSet<>(Arrays.asList(fieldNames)));
        return this;
    }

    public ReflectionAssert withLenientOrder() {
        lenientOrder = true;
        return this;
    }

    private void assertReflectionEquals(Deque<Class<?>> fullPath, Set<Object> objects, Object expectedObject, Object actualObject) {
        StringBuilder errorMessage = new StringBuilder();

        if (expectedObject == actualObject) {
            return;
        }
        if (expectedObject == null) {
            fullPath.push(actualObject.getClass());
            BuildErrorMessage.build(fullPath, expectedObject, "", actualObject.toString(), errorMessage);
        } else if (actualObject == null) {
            fullPath.push(expectedObject.getClass());
            BuildErrorMessage.build(fullPath, expectedObject.toString(), "", actualObject, errorMessage);
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
                    objects.add(expectedObject);
                    objects.add(actualObject);
                    assertReferencesEqual(fullPath, objects, field, expectedObject, actualObject);
                }
            }
        }
        if (errorMessage.length() != 0) {
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

    private void assertReferencesEqual(Deque<Class<?>> fullPath, Set<Object> objects, Field expectedField,
                                       Object expectedObject, Object actualObject) {
        if (objects.contains(ReflectionUtil.get(expectedField, expectedObject)) ||
                objects.contains(ReflectionUtil.get(expectedField, actualObject))) {
            return;
        }
        assertReflectionEquals(fullPath, objects,
                ReflectionUtil.get(expectedField, expectedObject), ReflectionUtil.get(expectedField, actualObject));
        if (ReflectionUtil.get(expectedField, expectedObject) == null) {
            return;
        }
        fullPath.pop();
    }

    private void throwAssertionError(String message) {
        throw new AssertionError(message);
    }
}
