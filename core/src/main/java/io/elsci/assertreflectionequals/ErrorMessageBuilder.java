package io.elsci.assertreflectionequals;

import java.lang.reflect.Field;
import java.util.*;

class ErrorMessageBuilder {

    private final List<Object> initialObjects;

    private final LinkedList<String> pathToProblematicField;

    private final StringBuilder errorMessage;

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
     * bacteria.insect.id expected: 5
     * bacteria.insect.id actual:   6</pre>
     *
     * <pre>
     * intArray[0] expected: -5
     * intArray[0] actual:   -10
     * </pre>
     * @param expected expected array
     * @param actual actual array
     * @param problemValues  contains mismatched values and corresponding index for array
     */
    public ErrorMessageBuilder addArraysDiff(Object[] expected, Object[] actual, Map<Object, List<Object>> problemValues) {
        this.addShallowDiff(Arrays.toString(expected), Arrays.toString(actual));

        // For arrays only. We want to show which indices were different and values they had.
        if (problemValues.size() > 0) {
            errorMessage.append("\n\n");
            for (Map.Entry<Object, List<Object>> entry : problemValues.entrySet()) {
                errorMessage.append(pathToProblematicField.getLast()).append("[").append(entry.getKey()).append("]").
                        append(" expected: ").append(entry.getValue().get(0)).append("\n");
                errorMessage.append(pathToProblematicField.getLast()).append("[").append(entry.getKey()).append("]").
                        append(" actual:   ").append(entry.getValue().get(1));
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
            toStringRecursive(expectedObject, new IdentityHashSet<>());
            removeLastFieldSeparator(errorMessage);

            errorMessage.append("\n").append("actual:   ");
            toStringRecursive(actualObject, new IdentityHashSet<>());
            removeLastFieldSeparator(errorMessage);
        }
    }

    /**
     * Example:
     * <pre>
     * Expected: Insect{@literal <}id=0, bacteria=Bacteria{@literal <}id=1, insect={@literal <}BEEN HERE, NOT GOING INSIDE AGAIN>, size=2.88>, size=1.55>
     * Actual:   Insect{@literal <}id=0, bacteria=Bacteria{@literal <}id=1, insect={@literal <}BEEN HERE, NOT GOING INSIDE AGAIN>, size=2.88>, size=1.55></pre>
     */
    private void toStringRecursive(Object object, IdentityHashSet<Object> alreadyChecked) {
        alreadyChecked.add(object);
        if (object == null) {
            errorMessage.append((Object) null).append(", ");
            return;
        }
        errorMessage.append(object.getClass().getSimpleName()).append("<");
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            errorMessage.append(f.getName()).append("=");
            f.setAccessible(true);
            Object value = ReflectionUtil.get(f, object);
            if (f.getType().isPrimitive()) {
                errorMessage.append(value).append(", ");
            } else if (f.getType().isArray()) {
                String valueText = Arrays.deepToString(new Object[]{value});
                valueText = valueText.substring(1, valueText.length() - 1); // remove an extra pair of square brackets
                errorMessage.append(valueText).append(", ");
            } else {
                if (alreadyChecked.contains(value))
                    errorMessage.append("<BEEN HERE, NOT GOING INSIDE AGAIN>, ");
                else
                    toStringRecursive(value, alreadyChecked);
            }
        }
        removeLastFieldSeparator(errorMessage);
        errorMessage.append(">, ");
    }

    /**
     * Performs logic for building first general part of error message
     */
    private void addInitialGeneralPart() {
        Object expectedInitial = initialObjects.get(0);
        Object actualInitial = initialObjects.get(1);

        errorMessage.append("\nExpected: ");
        toStringRecursive(expectedInitial, new IdentityHashSet<>());
        removeLastFieldSeparator(errorMessage);

        errorMessage.append("\nActual:   ");
        toStringRecursive(actualInitial, new IdentityHashSet<>());
        errorMessage.append(">").setLength(errorMessage.length() - 3);
    }

    /**
     * We end each field value with {@code >, }, so need to remove that last comma with the space.
     */
    private static void removeLastFieldSeparator(StringBuilder sb) {
        sb.setLength(sb.length() - 2);
    }

}
