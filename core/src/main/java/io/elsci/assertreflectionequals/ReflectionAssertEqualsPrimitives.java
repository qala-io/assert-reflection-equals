package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.Deque;

class ReflectionAssertEqualsPrimitives {
    private final Deque<String> fullPath;
    private final Field field;

    public ReflectionAssertEqualsPrimitives(Deque<String> fullPath, Field field) {
        this.fullPath = fullPath;
        this.field = field;
    }

    public StringBuilder assertEquals(Object expectedObject, Object actualObject, StringBuilder errorMessage) {
        fullPath.push(field.getName());
        if (!ReflectionUtil.get(field, expectedObject).equals(ReflectionUtil.get(field, actualObject))) {
            BuildErrorMessage.build(fullPath, ReflectionUtil.get(field, expectedObject), field.getName(),
                    ReflectionUtil.get(field, actualObject), errorMessage);
        }
        fullPath.pop();
        return errorMessage;
    }
}
