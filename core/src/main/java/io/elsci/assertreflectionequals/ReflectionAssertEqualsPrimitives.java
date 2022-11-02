package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.List;

class ReflectionAssertEqualsPrimitives {

    private final Field field;
    private final Deque<String> fullPath;

    public ReflectionAssertEqualsPrimitives(Field field, Deque<String> fullPath) {
        this.field = field;
        this.fullPath = fullPath;
    }

    public void assertEquals(Object expectedObject, Object actualObject, List<Object> initialObjects) {
        if (!ReflectionUtil.get(field, expectedObject).equals(ReflectionUtil.get(field, actualObject))) {
            fullPath.push(field.getName());
            ErrorMessageBuilder builder = new ErrorMessageBuilder(initialObjects, fullPath, new StringBuilder());
            builder.addShallowDiff(
                            ReflectionUtil.get(field, expectedObject),
                            ReflectionUtil.get(field, actualObject))
                    .addDeepDiff(expectedObject, actualObject);
            throw new AssertionError(builder.build());
        }
    }
}
