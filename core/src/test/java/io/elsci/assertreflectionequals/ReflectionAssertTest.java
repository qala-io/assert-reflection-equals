package io.elsci.assertreflectionequals;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReflectionAssertTest {
    @Test
    public void objectsAreEqualIfAllValuesOfTheirPropertiesAreEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{-5, -10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{-10, 897, -5, 7, 0}, new long[]{16, 34, 17, 149, 1});
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
                new byte[]{111, 112, 114}, new short[]{10, 30, 40});
        new ReflectionAssert().assertReflectionEquals(animal, animal2);
    }

    @Test
    public void possibleToCompareObjectsWithProtectedFields() {
        Plant plant = new Plant(150245871L, (short) 3, 50,
                new float[]{10.10f, 80.50f, 40.60f}, new double[]{100000d, 400000d, 90000d});
        Plant plant2 = new Plant(150245871L, (short) 3, 50,
                new float[]{80.50f, 40.60f, 10.10f}, new double[]{100000d, 400000d, 90000d});
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
                new int[]{-10, 0, 5, 897, 7}, new long[]{2, 17, 35, 150, 18});
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
                null, new long[]{16, 34, 17, 149, 1});
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
        assertEquals("The id of expected object is 150245872, but actual id is 150245871\n" +
                "The age of expected object is 30, but actual age is 25\n" +
                "The weight of expected object is 55.1007, but actual weight is 50.1007\n" +
                "The height of expected object is 170, but actual height is 164\n" +
                "The shoeSize of expected object is 127, but actual shoeSize is 2\n" +
                "The waist of expected object is 100.0, but actual waist is 69.0\n" +
                "The intArray of expected object is [5, 10, 897, 0, 7], but actual intArray is [0, 0, 0, 0, 0]\n" +
                "The longArray of expected object is [1, 16, 34, 149, 17], but actual longArray is [2, 17, 35, 150, 18]\n", e.getMessage());
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
                new int[]{10, 897, 5, 7, 0}, new long[]{16, 34, 17, 149, 1, 100});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("The longArray of expected object is [1, 16, 34], " +
                "but actual longArray is [16, 34, 17, 149, 1, 100]\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfOneOfArrayFieldIsEmpty() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{10, 897, 5, 7, 0}, new long[]{16, 34, 17, 149, 1, 100});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("The longArray of expected object is [], " +
                "but actual longArray is [16, 34, 17, 149, 1, 100]\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{5, 10, 897, 0, 7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{10, 897, 5, 7, 0}, null);
        AssertionError e = assertThrows(AssertionError.class, () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("The longArray of expected object is [16, 34, 17, 149, 1], but actual longArray is null\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfExpectedObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{-5, -10, -897, -14, -7}, null);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                new int[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("The longArray of expected object is null, but actual longArray is [16, 34, 17, 149, 1]\n", e.getMessage());
    }
}