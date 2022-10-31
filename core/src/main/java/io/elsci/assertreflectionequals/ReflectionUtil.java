package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class ReflectionUtil {
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
     * Validates if excluded field name belongs to the specified object
     * @param expectedClass class of objects
     * @param excludedNames set of fields that must be excluded from the checking
     */
    public static void assertExcludedFields(Class<?> expectedClass, Set<String> excludedNames) {
        List<String> fieldNames = new ArrayList<>();
        for (Field objectField : expectedClass.getDeclaredFields()) {
            fieldNames.add(objectField.getName());
        }
        for (String name : excludedNames) {
            if (!fieldNames.contains(name)) {
                throw new IllegalArgumentException(name + " is not field of " + expectedClass.getSimpleName() + " class");
            }
        }
    }
}
