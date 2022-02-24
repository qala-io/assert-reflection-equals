package io.elsci.assertreflectionequals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReflectionAssertTest {
    @Test
    public void objectsAreEqualIfAllValuesOfTheirPropertiesAreEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{-5, -10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{-5, -10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectIsEqualToItself() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().assertReflectionEquals(person, person);
    }

    @Test
    public void objectsAreEqualIfBothAreNull() {
        Person person = null;
        Person person2 = null;
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void possibleToCompareObjectsWithPrivateFields() {
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164,
                new byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245871L, (short) 25, 50.1007d, 164,
                new byte[]{112, 114, 111}, new short[]{40, 30, 10});
        new ReflectionAssert().assertReflectionEquals(animal, animal2);
    }

    @Test
    public void possibleToCompareObjectsWithProtectedFields() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new float[]{10.10f, 80.50f, 40.60f}, new double[]{100000d, 400000d, 90000d});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new float[]{10.10f, 80.50f, 40.60f}, new double[]{100000d, 400000d, 90000d});
        new ReflectionAssert().assertReflectionEquals(plant, plant2);
    }

    @Test
    public void objectsAreEqualIfArrayFieldsAreEmpty() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{}, new long[]{});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{}, new long[]{});
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfFieldsWithDifferentValuesWereExcluded() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                new int[]{5, -10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                new int[]{5, -10, 897, 0, 7}, new long[]{2, 17, 35, 150, 18});
        new ReflectionAssert().excludeFields("id", "longArray").assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfAllFieldsWereExcluded() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 26, 50.1008d, 165, (byte) 0b11, 70f,
                new int[]{6, 111, 898, 1, 8}, new long[]{2, 17, 35, 150, 18});
        new ReflectionAssert().
                excludeFields("id", "age", "weight", "height", "shoeSize", "waist", "intArray", "longArray").
                assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfBothArrayFieldsAreNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                null, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                null, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreNotEqualIfExpectedObjectIsNull() {
        Person person = null;
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Objects are not equal since one of them is null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = null;
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Objects are not equal since one of them is null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirClassesAreDifferent() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164, new byte[]{112, 114, 111},
                new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, animal));
        assertEquals("Expected class io.elsci.assertreflectionequals.Person, " +
                "but actual class io.elsci.assertreflectionequals.Animal", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfAllOfTheirPropertyValuesAreDifferent() {
        Person person = new Person(150245872L, (short) 30, 55.1007d, 170, (byte) 0b0111_1111, 100f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{0, 0, 0, 0, 0}, new long[]{2, 17, 35, 150, 18});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Expected: Person.id is 150245872, actual: Person.id is 150245871\n" +
                "Expected: Person.age is 30, actual: Person.age is 25\n" +
                "Expected: Person.weight is 55.1007, actual: Person.weight is 50.1007\n" +
                "Expected: Person.height is 170, actual: Person.height is 164\n" +
                "Expected: Person.shoeSize is 127, actual: Person.shoeSize is 2\n" +
                "Expected: Person.waist is 100.0, actual: Person.waist is 69.0\n" +
                "Expected: Person.intArray is [5, 10, 897, 0, 7], actual: Person.intArray is [0, 0, 0, 0, 0]\n" +
                "Expected: Person.longArray is [1, 16, 34, 149, 17], actual: Person.longArray is [2, 17, 35, 150, 18]\n", e.getMessage());
    }

    @Test
    public void throwsIllegalArgumentExceptionIfSpecifiedFieldIsNotPropertyOfSpecifiedObject() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ReflectionAssert().
                excludeFields("id", "test").
                assertReflectionEquals(person, person2));
        assertEquals("test field is not field of specified object",
                "test field is not field of specified object");
    }

    @Test
    public void objectsAreNotEqualIfCountOfValuesInArrayIsDifferent() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 1, 100});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Expected: Person.longArray is [1, 16, 34], " +
                "actual: Person.longArray is [1, 16, 34, 149, 1, 100]\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfOneOfArrayFieldIsEmpty() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{16, 34, 17, 149, 1, 100});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Expected: Person.longArray is [], " +
                "actual: Person.longArray is [16, 34, 17, 149, 1, 100]\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, null);
        AssertionError e = assertThrows(AssertionError.class, () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Expected: Person.longArray is [16, 34, 17, 149, 1], " +
                "actual: Person.longArray is null\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfExpectedObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{-5, -10, -897, -14, -7}, null);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Expected: Person.longArray is null, " +
                "actual: Person.longArray is [16, 34, 17, 149, 1]\n", e.getMessage());
    }

    @Test
    public void throwUnsupportedOperationExceptionIfComparingFieldsAreCollection() {
        Set<String> set = new HashSet<>();
        set.add("cocci");
        set.add("bacilli");
        set.add("spirilla");
        Bacteria bacteria = new Bacteria(set);
        Bacteria bacteria2 = new Bacteria(set);
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, () ->
                new ReflectionAssert().assertReflectionEquals(bacteria, bacteria2));
        assertEquals("This operation is not supported yet", e.getMessage());
    }
}