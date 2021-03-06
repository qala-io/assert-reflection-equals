package io.elsci.assertreflectionequals;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ReflectionAssertTest {
    @Test
    public void objectsAreEqualIfAllValuesOfTheirPropertiesAreEqual() {
        Person person = new Person(150245871L, (short) 25, Double.NaN, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, 897, 0}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, Double.NaN, 164, (byte) 0b10, 69f,
                (char) 77,true, new Integer[]{-5, -10, 897, 0}, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectIsEqualToItself() {
        Person person = new Person(150245871L, (short) 25, Double.NaN, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, 897, 0}, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().assertReflectionEquals(person, person);
    }

    @Test
    public void objectsAreEqualIfBothAreNull() {
        Person person = null;
        Person person2 = null;
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfAllFieldsWereExcluded() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 26, 50.1008d, 165, (byte) 0b11, 70f,
                (char) 77, true, new Integer[]{6, 111, 898, 1, 8}, new long[]{2, 17, 35, 150, 18});
        new ReflectionAssert().
                excludeFields("id", "age", "weight", "height", "shoeSize", "waist", "intArray", "longArray").
                assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreNotEqualIfExpectedObjectIsNull() {
        Person person = null;
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertTrue(e.getMessage().startsWith("Values were different for: Person.\n" +
                        "Expected: null\n" +
                        "Actual: io.elsci.assertreflectionequals.Person@"));
    }

    @Test
    public void objectsAreNotEqualIfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = null;
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertTrue(e.getMessage().startsWith("Values were different for: Person.\n" +
                "Expected: io.elsci.assertreflectionequals.Person@"));
    }

    @Test
    public void objectsAreNotEqualIfTheirClassesAreDifferent() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164, new Byte[]{112, 114, 111},
                new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, animal));
        assertEquals("Values were different for: Person.Animal.\n" +
                "Expected: class io.elsci.assertreflectionequals.Person\n" +
                "Actual: class io.elsci.assertreflectionequals.Animal\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfAllOfTheirPropertyValuesAreDifferent() {
        Person person = new Person(150245872L, (short) 30, 55.1007d, 170, (byte) 0b0111_1111, 100f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 76, false, new Integer[]{0, 0, 0, 0, 0}, new long[]{2, 17, 35, 150, 18});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Values were different for: Person.id\n" +
                "Expected: 150245872\n" +
                "Actual: 150245871\n" +
                "Values were different for: Person.age\n" +
                "Expected: 30\n" +
                "Actual: 25\n" +
                "Values were different for: Person.weight\n" +
                "Expected: 55.1007\n" +
                "Actual: 50.1007\n" +
                "Values were different for: Person.height\n" +
                "Expected: 170\n" +
                "Actual: 164\n" +
                "Values were different for: Person.shoeSize\n" +
                "Expected: 127\n" +
                "Actual: 2\n" +
                "Values were different for: Person.waist\n" +
                "Expected: 100.0\n" +
                "Actual: 69.0\n" +
                "Values were different for: Person.clothingSize\n" +
                "Expected: M\n" +
                "Actual: L\n" +
                "Values were different for: Person.adult\n" +
                "Expected: true\n" +
                "Actual: false\n" +
                "Values were different for: Person.intArray\n" +
                "Expected: [5, 10, 897, 0, 7]\n" +
                "Actual: [0, 0, 0, 0, 0]\n" +
                "Values were different for: Person.longArray\n" +
                "Expected: [1, 16, 34, 149, 17]\n" +
                "Actual: [2, 17, 35, 150, 18]\n", e.getMessage());
    }

    @Test
    public void throwsIllegalArgumentExceptionIfSpecifiedExcludedFieldIsNotFieldOfSpecifiedObject() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ReflectionAssert().
                excludeFields("id", "test").
                assertReflectionEquals(person, person2));
        assertEquals("test is not field of specified object", e.getMessage());
    }
}