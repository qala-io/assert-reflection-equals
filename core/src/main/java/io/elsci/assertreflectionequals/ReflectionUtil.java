package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;

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
}
