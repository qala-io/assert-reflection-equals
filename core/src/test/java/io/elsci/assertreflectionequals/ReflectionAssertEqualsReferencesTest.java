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
        Bacteria bacteria = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfChildObjectsWithDifferentValuesWereExcluded() {
        Bacteria bacteria = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(2, 2.16f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().excludeFields(Insect.class, "bacteria").assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfFieldsWithDifferentValuesForChildObjectsWereExcluded() {
        Bacteria bacteria = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(2, 2.16f, new Integer[]{6, 11, 898, 1, 8});

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().excludeFields(Bacteria.class, "id", "size", "intArray")
                .assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfFieldsWithDifferentValuesForParentAndChildObjectsWereExcluded() {
        Bacteria bacteria = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(2, 2.16f, new Integer[]{6, 11, 898, 1, 8});

        Insect insect = new Insect(3, 50.60f);
        Insect insect2 = new Insect(4, 50.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        new ReflectionAssert().excludeFields(Insect.class, "id").
                excludeFields(Bacteria.class, "id", "size", "intArray").assertReflectionEquals(insect, insect2);
    }

    @Test
    public void errorMessageIsCorrectIfExpectedChildObjectHasObjectOfItsParent() {
        Bacteria bacteria = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        Insect insect3 = new Insect(1, 1.54f);

        bacteria.setInsect(insect3);
        bacteria2.setInsect(insect);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=Bacteria<id=1, insect=Insect<id=1, bacteria=null, " +
                "size=1.54>, size=1.88, intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=1, insect=Insect<id=0, bacteria=Bacteria<id=1, " +
                "insect=Insect<id=1, bacteria=null, size=1.54>, size=1.88, intArray=[5, 10, 897, 0, 7]>, size=1.55>, " +
                "size=1.88, intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "bacteria.insect.id expected: 1\n" +
                "bacteria.insect.id actual:   0\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Insect<id=1, bacteria=null, size=1.54>\n" +
                "actual:   Insect<id=0, bacteria=Bacteria<id=1, insect=Insect<id=1, bacteria=null, size=1.54>, size=1.88," +
                " intArray=[5, 10, 897, 0, 7]>, size=1.55>", e.getMessage());
    }

    @Test
    public void objectsAreEqualIfParentObjectsAreObjectsOfItsChildObjects() {
        Bacteria bacteria = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});

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
        Bacteria bacteria = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});

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
        Bacteria bacteria = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        Insect insect3 = new Insect(1, 1.54f);

        bacteria.setInsect(insect);
        bacteria2.setInsect(insect3);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=Bacteria<id=1, insect=<BEEN HERE, NOT GOING INSIDE AGAIN>, " +
                "size=1.88, intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=1, insect=Insect<id=1, bacteria=null, size=1.54>, size=1.88, " +
                "intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "bacteria.insect.id expected: 0\n" +
                "bacteria.insect.id actual:   1\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Insect<id=0, bacteria=Bacteria<id=1, insect=<BEEN HERE, NOT GOING INSIDE AGAIN>, size=1.88, " +
                "intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "actual:   Insect<id=1, bacteria=null, size=1.54>", e.getMessage());
    }

    @Test
    public void objectsAreEqualIfChildObjectsAreEqualRecursively() {
        Bacteria bacteria = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(1, 1.88f, new Integer[]{5, 10, 897, 0, 7});

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
    public void objectsAreNotEqualIfChildObjectOfExpectedObjectIsNullAndFollowingFieldsAreEqual() {
        Bacteria bacteria = new Bacteria(2, 2.16f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(null);
        insect2.setBacteria(bacteria);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=null, size=40.6>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=2, insect=null, size=2.16, intArray=[5, 10, 897, 0, 7]>, " +
                "size=40.6>\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: null\n" +
                "actual:   Bacteria<id=2, insect=null, size=2.16, intArray=[5, 10, 897, 0, 7]>", e.getMessage());
    }

    @Test
    public void errorMessageIsBuiltCorrectlyIfChildObjectOfExpectedObjectIsNullAndFollowingFieldsAreNotEqual() {
        Bacteria bacteria = new Bacteria(2, 2.16f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(0, 40.61f);
        Insect insect2 = new Insect(0, 40.65f);
        insect.setBacteria(null);
        insect2.setBacteria(bacteria);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=null, size=40.61>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=2, insect=null, size=2.16, intArray=[5, 10, 897, 0, 7]>, " +
                "size=40.65>\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: null\n" +
                "actual:   Bacteria<id=2, insect=null, size=2.16, intArray=[5, 10, 897, 0, 7]>", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfChildObjectOfActualObjectIsNullAndFollowingFieldsAreEqual() {
        Bacteria bacteria = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(0,40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(null);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=Bacteria<id=1, insect=null, size=1.15, " +
                "intArray=[5, 10, 897, 0, 7]>, size=40.6>\n" +
                "Actual:   Insect<id=0, bacteria=null, size=40.6>\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Bacteria<id=1, insect=null, size=1.15, intArray=[5, 10, 897, 0, 7]>\n" +
                "actual:   null", e.getMessage());
    }

    @Test
    public void errorMessageIsBuiltCorrectlyIfChildObjectOfActualObjectIsNullAndFollowingFieldsAreNotEqual() {
        Bacteria bacteria = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(0,40.61f);
        Insect insect2 = new Insect(0, 40.65f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(null);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=Bacteria<id=1, insect=null, size=1.15, " +
                "intArray=[5, 10, 897, 0, 7]>, size=40.61>\n" +
                "Actual:   Insect<id=0, bacteria=null, size=40.65>\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Bacteria<id=1, insect=null, size=1.15, intArray=[5, 10, 897, 0, 7]>\n" +
                "actual:   null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirChildObjectsAreNotEqual() {
        Bacteria bacteria = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(2, 2.16f, new Integer[]{6, 11, 898, 1, 8});

        Insect insect = new Insect(0, 40.60f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=Bacteria<id=1, insect=null, size=1.15, " +
                "intArray=[5, 10, 897, 0, 7]>, size=40.6>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=2, insect=null, size=2.16, " +
                "intArray=[6, 11, 898, 1, 8]>, size=40.6>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "bacteria.id expected: 1\n" +
                "bacteria.id actual:   2\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Bacteria<id=1, insect=null, size=1.15, intArray=[5, 10, 897, 0, 7]>\n" +
                "actual:   Bacteria<id=2, insect=null, size=2.16, intArray=[6, 11, 898, 1, 8]>", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirChildObjectsAreEqualButOtherFieldsAreNot() {
        Bacteria bacteria = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(1, 1.15f, new Integer[]{5, 10, 897, 0, 7});

        Insect insect = new Insect(1, 50.50f);
        Insect insect2 = new Insect(0, 40.60f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=1, bacteria=Bacteria<id=1, insect=null, size=1.15, intArray=[5, 10, 897, 0, 7]>, " +
                "size=50.5>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=1, insect=null, size=1.15, intArray=[5, 10, 897, 0, 7]>, " +
                "size=40.6>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "id expected: 1\n" +
                "id actual:   0", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfObjectFieldsOfChildObjectsAreEqualButOtherFieldsOfChildObjectsAreNot() {
        Bacteria bacteria = new Bacteria(1, 2.88f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(2, 1.88f, new Integer[]{6, 11, 898, 1, 8});

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        bacteria.setInsect(insect);
        bacteria2.setInsect(insect2);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=Bacteria<id=1, insect=<BEEN HERE, NOT GOING INSIDE AGAIN>, " +
                "size=2.88, intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=2, insect=<BEEN HERE, NOT GOING INSIDE AGAIN>, size=1.88, " +
                "intArray=[6, 11, 898, 1, 8]>, size=1.55>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "bacteria.id expected: 1\n" +
                "bacteria.id actual:   2\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Bacteria<id=1, insect=Insect<id=0, bacteria=<BEEN HERE, NOT GOING INSIDE AGAIN>, size=1.55>, " +
                "size=2.88, intArray=[5, 10, 897, 0, 7]>\n" +
                "actual:   Bacteria<id=2, insect=Insect<id=0, bacteria=<BEEN HERE, NOT GOING INSIDE AGAIN>, size=1.55>, " +
                "size=1.88, intArray=[6, 11, 898, 1, 8]>", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfPrimitiveFieldsOfChildObjectsAreEqualButObjectFieldsOfChildObjectsAreNot() {
        Bacteria bacteria = new Bacteria(1, 2.88f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(1, 2.88f, new Integer[]{5, 10, 897, 0, 7});

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
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=Bacteria<id=1, insect=Insect<id=5, bacteria=null, size=5.55>, " +
                "size=2.88, intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=1, insect=Insect<id=6, bacteria=null, size=6.55>, " +
                "size=2.88, intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "bacteria.insect.id expected: 5\n" +
                "bacteria.insect.id actual:   6\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Insect<id=5, bacteria=null, size=5.55>\n" +
                "actual:   Insect<id=6, bacteria=null, size=6.55>", e.getMessage());
    }

    @Test
    public void errorMessageIsCorrectIfArraysOfChildObjectAreNotEqual() {
        Bacteria bacteria = new Bacteria(1, 2.88f, new Integer[]{5, 10, 897, 0, 7});
        Bacteria bacteria2 = new Bacteria(1, 2.88f, new Integer[]{5, 10, 898, 0, 7});

        Insect insect = new Insect(0, 1.55f);
        Insect insect2 = new Insect(0, 1.55f);
        insect.setBacteria(bacteria);
        insect2.setBacteria(bacteria2);

        Insect insect3 = new Insect(5, 5.55f);
        Insect insect4 = new Insect(5, 5.55f);
        bacteria.setInsect(insect3);
        bacteria2.setInsect(insect4);

        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("\n" +
                "Expected: Insect<id=0, bacteria=Bacteria<id=1, insect=Insect<id=5, bacteria=null, size=5.55>, size=2.88, " +
                "intArray=[5, 10, 897, 0, 7]>, size=1.55>\n" +
                "Actual:   Insect<id=0, bacteria=Bacteria<id=1, insect=Insect<id=5, bacteria=null, size=5.55>, size=2.88, " +
                "intArray=[5, 10, 898, 0, 7]>, size=1.55>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "bacteria.intArray expected: [5, 10, 897, 0, 7]\n" +
                "bacteria.intArray actual:   [5, 10, 898, 0, 7]\n" +
                "\n" +
                "intArray[2] expected: 897\n" +
                "intArray[2] actual:   898\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Bacteria<id=1, insect=Insect<id=5, bacteria=null, size=5.55>, size=2.88, " +
                "intArray=[5, 10, 897, 0, 7]>\n" +
                "actual:   Bacteria<id=1, insect=Insect<id=5, bacteria=null, size=5.55>, size=2.88, " +
                "intArray=[5, 10, 898, 0, 7]>", e.getMessage());
    }
}
