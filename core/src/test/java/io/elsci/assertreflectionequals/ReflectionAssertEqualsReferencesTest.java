package io.elsci.assertreflectionequals;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReflectionAssertEqualsReferencesTest {
    @Test public void blah() {
        Insect i1 = new Insect(1, 1);
        Insect i2 = new Insect(1, 1);
        Bacteria b1 = new Bacteria(4, 4);
        Bacteria b2 = new Bacteria(4, 4);
        b1.setInsect(i1);
        b2.setInsect(i2);
        i1.setBacteria(b1);
        assertThrows(AssertionError.class, ()->new ReflectionAssert().assertReflectionEquals(b1, b2));
    }

    @Test
    public void objectsAreEqualIfInternalObjectsAreNull() {
        Bacteria bacteria = null;
        Bacteria bacteria2 = null;

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfInternalObjectsAreTheSameObject() {
        Bacteria bacteria = new Bacteria(1, 1.15f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfInternalObjectsWithDifferentValuesWereExcluded() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().excludeFields(Insect.class, "bacteria").assertReflectionEquals(insect, insect2);
    }

    @Test
    public void possibleToSpecifyTheSameClassWithTheSameFieldsTwice() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().excludeFields(Insect.class, "bacteria").
                excludeFields(Insect.class, "bacteria").assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfFieldsWithDifferentValuesForInternalObjectsWereExcluded() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().excludeFields(Bacteria.class, "id", "size").assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfFieldsWithDifferentValuesForExternalAndInternalObjectsWereExcluded() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(3, 50.60f);
        Insect insect2 = new Insect(4, 50.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().excludeFields(Insect.class, "id").
                excludeFields(Bacteria.class, "id", "size").assertReflectionEquals(insect, insect2);
    }

    @Test
    public void noInfiniteLoopIfObjectAndItsInternalObjectHaveBidirectionalRelationships() {
        Bacteria bacteria = new Bacteria(1, 1.88f);
        Bacteria bacteria2 = new Bacteria(1, 1.88f);

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        bacteria.setInsect(insect);
        bacteria2.setInsect(insect2);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfInternalObjectsAreEqualRecursively() {
        Bacteria bacteria = new Bacteria(1, 1.88f);
        Bacteria bacteria2 = new Bacteria(1, 1.88f);

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        Insect insect3 = new Insect(2, 5.55f);
        Insect insect4 = new Insect(2, 5.55f);
        bacteria.setInsect(insect3);
        bacteria2.setInsect(insect4);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreNotEqualIfInternalObjectOfExpectedObjectIsNull() {
        Bacteria bacteria = null;
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertTrue(e.getMessage().startsWith("Values were different for: Insect.Bacteria.\n" +
                "Expected: null\n" +
                "Actual: io.elsci.assertreflectionequals.Bacteria@"));
    }

    @Test
    public void objectsAreNotEqualIfInternalObjectOfActualObjectIsNull() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = null;

        Insect insect = new Insect(0,40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertTrue(e.getMessage().startsWith("Values were different for: Insect.Bacteria.\n" +
                "Expected: io.elsci.assertreflectionequals.Bacteria@"));
    }

    @Test
    public void objectsAreNotEqualIfTheirInternalObjectsAreNotEqual() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.Bacteria.id\n" +
                "Expected: 1\n" +
                "Actual: 2\n" +
                "Values were different for: Insect.Bacteria.size\n" +
                "Expected: 1.15\n" +
                "Actual: 2.16\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirInternalObjectsAreEqualButOtherFieldsAreNot() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = new Bacteria(1, 1.15f);

        Insect insect = new Insect(1, 50.50f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.id\n" +
                "Expected: 1\n" +
                "Actual: 0\n" +
                "Values were different for: Insect.size\n" +
                "Expected: 50.5\n" +
                "Actual: 40.6\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfObjectFieldsOfInternalObjectsAreEqualButOtherFieldsOfInternalObjectsAreNot() {
        Bacteria bacteria = new Bacteria(1, 2.88f);
        Bacteria bacteria2 = new Bacteria(2, 1.88f);

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        bacteria.setInsect(insect);
        bacteria2.setInsect(insect2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.Bacteria.id\n" +
                "Expected: 1\n" +
                "Actual: 2\n" +
                "Values were different for: Insect.Bacteria.size\n" +
                "Expected: 2.88\n" +
                "Actual: 1.88\n", e.getMessage());
    }

    @Test
    public void testik() {
        Bacteria bacteria = new Bacteria(1, 2.88f);
        Bacteria bacteria2 = new Bacteria(2, 1.88f);

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.54f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        bacteria.setInsect(insect);
        bacteria2.setInsect(insect2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.Bacteria.id\n" +
                "Expected: 1\n" +
                "Actual: 2\n" +
                "Values were different for: Insect.Bacteria.size\n" +
                "Expected: 2.88\n" +
                "Actual: 1.88\n" +
                "Values were different for: Insect.size\n" +
                "Expected: 1.55\n" +
                "Actual: 1.54\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfPrimitiveFieldsOfInternalObjectsAreEqualButObjectFieldsOfInternalObjectsAreNot() {
        Bacteria bacteria = new Bacteria(1, 2.88f);
        Bacteria bacteria2 = new Bacteria(1, 2.88f);

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        Insect insect3 = new Insect(5, 5.55f);
        Insect insect4 = new Insect(6, 6.55f);
        bacteria.setInsect(insect3);
        bacteria2.setInsect(insect4);

        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.Bacteria.Insect.id\n" +
                "Expected: 5\n" +
                "Actual: 6\n" +
                "Values were different for: Insect.Bacteria.Insect.size\n" +
                "Expected: 5.55\n" +
                "Actual: 6.55\n", e.getMessage());
    }
}
