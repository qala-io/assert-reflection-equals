package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.Deque;

class ReflectionAssertEqualsPrimitives {
    private final Deque<Class<?>> fullPath;
    private final Field field;

    public ReflectionAssertEqualsPrimitives(Deque<Class<?>> fullPath, Field field) {
        this.fullPath = fullPath;
        this.field = field;
    }

    public StringBuilder assertEquals(Object expectedObject, Object actualObject, StringBuilder errorMessage) {
        if (!ReflectionUtil.get(field, expectedObject).equals(ReflectionUtil.get(field, actualObject))) {
            return BuildErrorMessage.build(fullPath, ReflectionUtil.get(field, expectedObject), field.getName(),
                    ReflectionUtil.get(field, actualObject), errorMessage);
        }
        return errorMessage;
    }
}
