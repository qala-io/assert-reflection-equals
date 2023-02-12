package io.elsci.assertreflectionequals;

import java.util.*;

public class ReflectionAssert {
    private final Map<Class<?>, Set<String>> excludedFields = new HashMap<>();
    private boolean lenientOrder = false;

    public void assertReflectionEquals(Object expectedObject, Object actualObject) {
        // Put initial objects into initialObjects collection for proper building the 1st general part of error message
        List<Object> initialObjects = new ArrayList<>();
        initialObjects.add(expectedObject);
        initialObjects.add(actualObject);

        // Put initial objects into checkedPairs collection so to have with what to compare next child pair of objects
        IdentityHashMap<Object, IdentityHashSet<Object>> checkedPairs = new IdentityHashMap<>();
        checkedPairs.put(expectedObject, new IdentityHashSet<>(actualObject));
        checkedPairs.put(actualObject, new IdentityHashSet<>(expectedObject));
        new ReflectionAssertEqualsObjects().assertReflectionEquals(expectedObject, actualObject,
                lenientOrder, excludedFields, checkedPairs, new ArrayDeque<>(), initialObjects);
    }

    /**
     * Allows to provide the list of fields that shouldn't be compared
     * @param clazz class for which the fields are excluded
     * @param fieldNames fields to be excluded
     */
    public ReflectionAssert excludeFields(Class<?> clazz, String... fieldNames) {
        excludedFields.put(clazz, new HashSet<>(Arrays.asList(fieldNames)));
        return this;
    }

    /**
     * Allows to specify that while comparing the order of elements in an array should be ignored
     */
    public ReflectionAssert withLenientOrder() {
        lenientOrder = true;
        return this;
    }
}
