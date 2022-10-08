package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Map;

public class ReflectionAssertEqualsReferences {

    private Deque<Class<?>> fullPath;
    private Map<Object, Object> checkedPairs;
    private Field expectedField;
    private Object expectedObject;
    private Object actualObject;

    public ReflectionAssertEqualsReferences(Deque<Class<?>> fullPath, Map<Object, Object> checkedPairs,
                                            Field expectedField, Object expectedObject, Object actualObject,
                                            StringBuilder errorMessage) {
        this.fullPath = fullPath;
        this.checkedPairs = checkedPairs;
        this.expectedField = expectedField;
        this.expectedObject = expectedObject;
        this.actualObject = actualObject;
    }

//    public StringBuilder assertReferencesEqual() {
//        // This check is actual only for case when we check the first pair of internal objects
//        if (checkedPairs.isEmpty()) {
//            IdentityHashMap<Object, Object> value = new IdentityHashMap<>();
//            value.put(actualObject, null);
//            checkedPairs.put(expectedObject, value);
//        }
//
//        Object expected = ReflectionUtil.get(expectedField, expectedObject);
//        Object actual = ReflectionUtil.get(expectedField, actualObject);
//
//        return errorMessage;
//    }
}