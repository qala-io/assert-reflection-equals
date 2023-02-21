package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

class ErrorMessageBuilder {

    private final List<Object> initialObjects;

    private final LinkedList<String> pathToProblematicField;

    private final StringBuilder errorMessage;
    private static final HashSet<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(Boolean.class, Character.class,
            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class, String.class));

    public ErrorMessageBuilder(List<Object> initialObjects, Deque<String> pathToProblematicField, StringBuilder errorMessage) {
        this.initialObjects = initialObjects;
        this.pathToProblematicField = new LinkedList<>(pathToProblematicField);
        Collections.reverse(this.pathToProblematicField); // deque contained fields in the opposite order (from the deepest)
        this.errorMessage = errorMessage;
        addInitialGeneralPart();
    }

    public String build() {
        return errorMessage.toString();
    }

    /**
     * Builds part about mismatched specified field that must be excluded from specified class
     * <pre>
     * 'test' is not field of Person class
     * </pre>
     * @param fieldName name of specified excluded field
     * @param className name of specified class for which specified field must be excluded
     */
    public void addStringWithMismatchedField(String fieldName, String className) {
        errorMessage.append("\n\n'").append(fieldName).append("' is not field of ").append(className).append(" class");
    }

    /**
     * Builds part about different values in primitives. Example:
     * <pre>
     * --- Fields that differed ---
     * bacteria.insect.size expected: 1
     * bacteria.insect.size actual:   5</pre>
     */
    public ErrorMessageBuilder addShallowDiff(Object expected, Object actual) {
        errorMessage.append("\n\n").append("--- Fields that differed ---").append("\n");

        errorMessage.append(String.join(".", pathToProblematicField));
        errorMessage.append(" expected: ").append(expected).append("\n");
        errorMessage.append(String.join(".", pathToProblematicField));
        errorMessage.append(" actual:   ").append(actual);
        return this;
    }

    /**
     * Builds part about different values in array. Example:
     * <pre>
     * --- Fields that differed ---
     * bacteria.insect.id expected: [-5, 9, 17]
     * bacteria.insect.id actual:   [-10, 8, 4]</pre>
     *
     * <pre>
     * intArray[0] expected: -5
     * intArray[0] actual:   -10
     * </pre>
     * @param expected expected array
     * @param actual actual array
     * @param problemValues  contains mismatched values and their corresponding index in array
     */
    public ErrorMessageBuilder addArraysDiff(Object[] expected, Object[] actual, Map<Object, List<Object>> problemValues) {
        // Array may contain primitives or objects. We need to detect it before invoking 'addShallowDiff' method
        // since if it's array with objects, we need firstly build a general string with all array objects
        if (isBlank(expected) && isBlank(actual)) {
            this.addShallowDiff(Arrays.toString(expected), Arrays.toString(actual));
        } else if (isBlank(expected)) {
            if (WRAPPER_TYPES.contains(actual.getClass().getComponentType())) {
                this.addShallowDiff(Arrays.toString(expected), Arrays.toString(actual));
            } else {
                this.addShallowDiff(Arrays.toString(expected), toStringArrayWithObjects(actual));
            }
        } else if (isBlank(actual)) {
            if (WRAPPER_TYPES.contains(expected.getClass().getComponentType())) {
                this.addShallowDiff(Arrays.toString(expected), Arrays.toString(actual));
            } else {
                this.addShallowDiff(toStringArrayWithObjects(expected), Arrays.toString(actual));
            }
        } else {
            if (!WRAPPER_TYPES.contains(actual.getClass().getComponentType())) {
                this.addShallowDiff(toStringArrayWithObjects(expected), toStringArrayWithObjects(actual));
            } else {
                this.addShallowDiff(Arrays.toString(expected), Arrays.toString(actual));
            }
        }
        // When arrays with objects are NOT equal
        if (problemValues.size() > 0) {
            errorMessage.append("\n\n");
            for (Map.Entry<Object, List<Object>> entry : problemValues.entrySet()) {
                errorMessage.append(pathToProblematicField.getLast()).append("[").append(entry.getKey()).append("]")
                        .append(" expected: ");
                if (WRAPPER_TYPES.contains(expected.getClass().getComponentType())) {
                    errorMessage.append(entry.getValue().get(0));
                } else {
                    errorMessage.append(toStringRecursive(entry.getValue().get(0), new IdentityHashSet<>(), new StringBuilder()));
                    removeLastFieldSeparator(errorMessage);
                }

                errorMessage.append("\n").append(pathToProblematicField.getLast()).append("[").append(entry.getKey()).append("]")
                        .append(" actual:   ");
                if (WRAPPER_TYPES.contains(expected.getClass().getComponentType())) {
                    errorMessage.append(entry.getValue().get(1));
                } else {
                    errorMessage.append(toStringRecursive(entry.getValue().get(1), new IdentityHashSet<>(), new StringBuilder()));
                    removeLastFieldSeparator(errorMessage);
                }
            }
        }
        return this;
    }

    /**
     * Builds part about different child objects. Example:
     * <pre>
     * --- Objects that differed ---
     * expected: Insect{@literal <}id=5, bacteria=null, size=5.55>
     * actual:   Insect{@literal <}id=6, bacteria=null, size=6.55></pre>
     */
    public void addDeepDiff(Object expectedObject, Object actualObject) {
        // Only for different classes
        if (expectedObject != null && actualObject != null && expectedObject.getClass() != actualObject.getClass()) {
            errorMessage.append("\n\n").append("--- Objects that differed ---").append("\n");
            errorMessage.append("expected: object of type ").append(expectedObject.getClass().getSimpleName())
                    .append("\n").append("actual:   object of type ").append(actualObject.getClass().getSimpleName());
        }
        // Add "Objects that differed" part only if comparing objects are child objects
        // (it makes no sense to show this part for initial objects since it duplicates info reported in 1st part of error)
        if (initialObjects.get(0) != expectedObject || initialObjects.get(1) != actualObject) {
            errorMessage.append("\n\n").append("--- Objects that differed ---").append("\n");
            errorMessage.append("expected: ");
            errorMessage.append(toStringRecursive(expectedObject, new IdentityHashSet<>(), new StringBuilder()));
            removeLastFieldSeparator(errorMessage);

            errorMessage.append("\n").append("actual:   ");
            errorMessage.append(toStringRecursive(actualObject, new IdentityHashSet<>(), new StringBuilder()));
            removeLastFieldSeparator(errorMessage);
        }
    }

    /**
     * Performs logic for building first general part of error message
     */
    private void addInitialGeneralPart() {
        Object expectedInitial = initialObjects.get(0);
        Object actualInitial = initialObjects.get(1);

        errorMessage.append("\nExpected: ")
                .append(toStringRecursive(expectedInitial, new IdentityHashSet<>(), new StringBuilder()));
        removeLastFieldSeparator(errorMessage);

        errorMessage.append("\nActual:   ")
                .append(toStringRecursive(actualInitial, new IdentityHashSet<>(), new StringBuilder()));
        errorMessage.append(">").setLength(errorMessage.length() - 3);
    }

    /**
     * Example:
     * <pre>
     * Expected: Insect{@literal <}id=0, bacteria=Bacteria{@literal <}id=1, insect={@literal <}BEEN HERE, NOT GOING INSIDE AGAIN>, size=2.88>, size=1.55>
     * Actual:   Insect{@literal <}id=0, bacteria=Bacteria{@literal <}id=1, insect={@literal <}BEEN HERE, NOT GOING INSIDE AGAIN>, size=2.88>, size=1.55></pre>
     */
    private StringBuilder toStringRecursive(Object object, IdentityHashSet<Object> alreadyChecked, StringBuilder sb) {
        alreadyChecked.add(object);
        if (object == null) {
            sb.append((Object) null).append(", ");
            return sb;
        }
        sb.append(object.getClass().getSimpleName()).append("<");
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            sb.append(f.getName()).append("=");
            f.setAccessible(true);
            Object value = ReflectionUtil.get(f, object);
            if (value == null) { // if array is null
                sb.append((Object) null).append(", ");
            } else {
                if (f.getType().isPrimitive()) {
                    sb.append(value).append(", ");
                } else if (f.getType().isArray()) {
                    Object[] array = ReflectionUtil.getArrayWithValues(ReflectionUtil.get(f, object));
                    if (array.length == 0) {
                        sb.append("[]").append(", ");
                    } else if (WRAPPER_TYPES.contains(array.getClass().getComponentType())) {
                        String valueText = Arrays.deepToString(new Object[]{value});
                        valueText = valueText.substring(1, valueText.length() - 1); // remove an extra pair of square brackets
                        sb.append(valueText).append(", ");
                    } else {
                        // If array contains objects, we need to show the full inner part too
                        sb.append("[");
                        for (Object o : array) {
                            toStringRecursive(o, new IdentityHashSet<>(), sb);
                        }
                        removeLastFieldSeparator(sb);
                        sb.append("], ");
                    }
                } else {
                    if (alreadyChecked.contains(value))
                        sb.append("<BEEN HERE, NOT GOING INSIDE AGAIN>, ");
                    else
                        toStringRecursive(value, alreadyChecked, sb);
                }
            }
        }
        removeLastFieldSeparator(sb);
        sb.append(">, ");
        return sb;
    }

    /**
     * We end each field value with {@code >, }, so need to remove that last comma with the space.
     */
    private void removeLastFieldSeparator(StringBuilder sb) {
        sb.setLength(sb.length() - 2);
    }

    /**
     * Example:
     * <pre>[Person{@literal <}id=150245871, age=25, weight=50.1007, height=164>, Person{@literal <}id=150245871, age=25, weight=50.1007, height=164>]</pre>
     */
    private StringBuilder toStringArrayWithObjects(Object[] array) {
        StringBuilder arrayWithObjects = new StringBuilder();
        arrayWithObjects.append("[");
        for (Object o : array) {
            toStringRecursive(o, new IdentityHashSet<>(), arrayWithObjects);
        }
        removeLastFieldSeparator(arrayWithObjects);
        arrayWithObjects.append("]");
        return arrayWithObjects;
    }

    /**
     * Checks if array is null or empty
     */
    private static boolean isBlank(Object[] object) {
        return object == null || object.length == 0;
    }
}
