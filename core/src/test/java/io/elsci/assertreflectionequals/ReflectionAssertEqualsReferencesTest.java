package io.elsci.assertreflectionequals;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReflectionAssertEqualsReferencesTest {

    @Test
    public void objectsAreEqualIfChildObjectsAreNull() {
        Bacteria bacteria = null;
        Bacteria bacteria2 = null;

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfChildObjectsAreTheSameObject() {
        Bacteria bacteria = new Bacteria(1, 1.15f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfChildObjectsWithDifferentValuesWereExcluded() {
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
    public void objectsAreEqualIfFieldsWithDifferentValuesForChildObjectsWereExcluded() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().excludeFields(Bacteria.class, "id", "size").assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfFieldsWithDifferentValuesForParentAndChildObjectsWereExcluded() {
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
    public void errorMessageIsCorrectIfExpectedChildObjectHasObjectOfItsParent() {
        Bacteria bacteria = new Bacteria(1, 1.88f);
        Bacteria bacteria2 = new Bacteria(1, 1.88f);

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        Insect insect3 = new Insect(1, 1.54f);

        bacteria.setInsect(insect3);
        bacteria2.setInsect(insect);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertTrue(e.getMessage().startsWith("Values were different for: Insect.Bacteria.Insect.id\n" +
                "Expected: 1\n" +
                "Actual: 0\n" +
                "Values were different for: Insect.Bacteria.Insect.Bacteria\n" +
                "Expected: null\n" +
                "Actual: io.elsci.assertreflectionequals.Bacteria@"));
        assertTrue(e.getMessage().contains("Values were different for: Insect.Bacteria.Insect.size\n" +
                "Expected: 1.54\n" +
                "Actual: 1.55"));
    }

    @Test
    public void objectsAreEqualIfParentObjectsAreObjectsOfItsChildObjects() {
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
    public void objectsAreEqualIfParentObjectsAreObjectsOfItsChildObjectsButExpectedHasLinkToActualAndViseVersa() {
        Bacteria bacteria = new Bacteria(1, 1.88f);
        Bacteria bacteria2 = new Bacteria(1, 1.88f);

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        bacteria.setInsect(insect2);
        bacteria2.setInsect(insect);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void errorMessageIsCorrectIfActualChildObjectHasObjectOfItsParent() {
        Bacteria bacteria = new Bacteria(1, 1.88f);
        Bacteria bacteria2 = new Bacteria(1, 1.88f);

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        Insect insect3 = new Insect(1, 1.54f);

        bacteria.setInsect(insect);
        bacteria2.setInsect(insect3);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertTrue(e.getMessage().startsWith("Values were different for: Insect.Bacteria.Insect.id\n" +
                "Expected: 0\n" +
                "Actual: 1\n" +
                "Values were different for: Insect.Bacteria.Insect.Bacteria\n" +
                "Expected: io.elsci.assertreflectionequals.Bacteria"));
        assertTrue(e.getMessage().contains("Actual: null\n" +
                "Values were different for: Insect.Bacteria.Insect.size\n" +
                "Expected: 1.55\n" +
                "Actual: 1.54\n"));
    }

    @Test
    public void objectsAreEqualIfChildObjectsAreEqualRecursively() {
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
    public void objectsAreNotEqualIfChildObjectOfExpectedObjectIsNull() {
        Bacteria bacteria = null;
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertTrue(e.getMessage().startsWith("Values were different for: Insect.Bacteria\n" +
                "Expected: null\n" +
                "Actual: io.elsci.assertreflectionequals.Bacteria@"));
    }

    @Test
    public void errorMessageIsBuiltCorrectlyIfChildObjectOfExpectedObjectIsNullAndFollowingFieldsAreNotEqual() {
        Bacteria bacteria = null;
        Bacteria bacteria2 = new Bacteria(2, 2.16f);

        Insect insect = new Insect(0, 40.61f);
        Insect insect2 = new Insect(0, 40.65f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertTrue(e.getMessage().startsWith("Values were different for: Insect.Bacteria\n" +
                "Expected: null\n" +
                "Actual: io.elsci.assertreflectionequals.Bacteria@"));
        assertTrue(e.getMessage().contains("Values were different for: Insect.size\n" +
                "Expected: 40.61\n" +
                "Actual: 40.65\n"));
    }

    @Test
    public void objectsAreNotEqualIfChildObjectOfActualObjectIsNull() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = null;

        Insect insect = new Insect(0,40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertTrue(e.getMessage().startsWith("Values were different for: Insect.Bacteria\n" +
                "Expected: io.elsci.assertreflectionequals.Bacteria@"));
        assertTrue(e.getMessage().contains("Actual: null"));
    }

    @Test
    public void errorMessageIsBuiltCorrectlyIfChildObjectOfActualObjectIsNullAndFollowingFieldsAreNotEqual() {
        Bacteria bacteria = new Bacteria(1, 1.15f);
        Bacteria bacteria2 = null;

        Insect insect = new Insect(0,40.61f);
        Insect insect2 = new Insect(0, 40.65f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertTrue(e.getMessage().startsWith("Values were different for: Insect.Bacteria\n" +
                "Expected: io.elsci.assertreflectionequals.Bacteria@"));
        assertTrue(e.getMessage().contains("Actual: null\n" +
                "Values were different for: Insect.size\n" +
                "Expected: 40.61\n" +
                "Actual: 40.65"));
    }

    @Test
    public void objectsAreNotEqualIfTheirChildObjectsAreNotEqual() {
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
    public void objectsAreNotEqualIfTheirChildObjectsAreEqualButOtherFieldsAreNot() {
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
    public void objectsAreNotEqualIfObjectFieldsOfChildObjectsAreEqualButOtherFieldsOfChildObjectsAreNot() {
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
    public void objectsAreNotEqualIfPrimitiveFieldsOfChildObjectsAreEqualButObjectFieldsOfChildObjectsAreNot() {
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
