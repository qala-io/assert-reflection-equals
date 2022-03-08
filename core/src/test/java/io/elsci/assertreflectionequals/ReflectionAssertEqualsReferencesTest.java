package io.elsci.assertreflectionequals;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReflectionAssertEqualsReferencesTest {
    @Test
    public void objectsAreEqualIfInternalObjectsAreNull() {
        Plant plant = null;
        Plant plant2 = null;
        Insect insect = new Insect(0, plant, 40.60f);
        Insect insect2 = new Insect(0, plant2, 40.60f);
        new ReflectionAssert().assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreEqualIfInternalObjectFieldsWithDifferentValuesWereExcluded() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{-10.10f, 0.0f, 40.60f}, new double[]{-100000d, 0.0d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245872L, (short) 4, 51, false, 'b',
                new Float[]{-10.10f, 0.0f, 40.60f}, new double[]{-100000d, 0.0d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Insect insect = new Insect(0, plant, 40.60f);
        Insect insect2 = new Insect(0, plant2, 40.60f);
        new ReflectionAssert().excludeFields("plant").assertReflectionEquals(insect, insect2);
    }

    @Test
    public void objectsAreNotEqualIfInternalObjectOfExpectedObjectIsNull() {
        Plant plant = null;
        Plant plant2 = new Plant(150245872L, (short) 4, 51, false, 'b',
                new Float[]{-10.10f, 0.0f, 40.60f}, new double[]{-100000d, 0.0d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Insect insect = new Insect(0, plant, 40.60f);
        Insect insect2 = new Insect(0, plant2, 40.60f);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.Plant.\n" +
                "Expected: null\n" +
                "Actual: class io.elsci.assertreflectionequals.Plant\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfInternalObjectOfActualObjectIsNull() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{-10.10f, 0.0f, 40.60f}, new double[]{-100000d, 0.0d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = null;
        Insect insect = new Insect(0, plant, 40.60f);
        Insect insect2 = new Insect(0, plant2, 40.60f);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.Plant.\n" +
                "Expected: class io.elsci.assertreflectionequals.Plant\n" +
                "Actual: null\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirInternalObjectsAreNotEqual() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{-10.10f, 0.0f, 40.60f}, new double[]{-100000d, 0.0d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245872L, (short) 4, 51, false, 'a',
                new Float[]{-10.10f, 0.0f, 40.60f}, new double[]{-100000d, 0.0d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Insect insect = new Insect(0, plant, 40.60f);
        Insect insect2 = new Insect(0, plant2, 40.60f);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.Plant.id\n" +
                "Expected: 150245871\n" +
                "Actual: 150245872\n" +
                "Values were different for: Insect.Plant.age\n" +
                "Expected: 3\n" +
                "Actual: 4\n" +
                "Values were different for: Insect.Plant.height\n" +
                "Expected: 50\n" +
                "Actual: 51\n" +
                "Values were different for: Insect.Plant.isFlowering\n" +
                "Expected: true\n" +
                "Actual: false\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirInternalObjectsAreEqualButOtherFieldsAreNot() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{-10.10f, 0.0f, 40.60f}, new double[]{-100000d, 0.0d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{-10.10f, 0.0f, 40.60f}, new double[]{-100000d, 0.0d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Insect insect = new Insect(1, plant, 50.60f);
        Insect insect2 = new Insect(2, plant2, 40.60f);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(insect, insect2));
        assertEquals("Values were different for: Insect.id\n" +
                "Expected: 1\n" +
                "Actual: 2\n" +
                "Values were different for: Insect.size\n" +
                "Expected: 50.6\n" +
                "Actual: 40.6\n", e.getMessage());
    }
}
