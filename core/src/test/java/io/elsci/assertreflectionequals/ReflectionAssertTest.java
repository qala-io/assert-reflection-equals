package io.elsci.assertreflectionequals;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReflectionAssertTest {
    @Test
    public void twoObjectsAreEqualIfAllValuesOfTheirPropertiesAreEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectIsEqualToItself() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
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
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164);
        Animal animal2 = new Animal(150245871L, (short) 25, 50.1007d, 164);
        new ReflectionAssert().assertReflectionEquals(animal, animal2);
    }

    @Test
    public void possibleToCompareObjectsWithProtectedFields() {
        Plant plant = new Plant(150245871L, (short) 3, 50);
        Plant plant2 = new Plant(150245871L, (short) 3, 50);
        new ReflectionAssert().assertReflectionEquals(plant, plant2);
    }

    @Test
    public void objectsAreNotEqualIfExpectedObjectIsNull() {
        Person person = null;
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Objects are not equal since one of them is null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = null;
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Objects are not equal since one of them is null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirClassesAreDifferent() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, animal));
        assertEquals("Expected class io.elsci.assertreflectionequals.Person, " +
                "but actual class io.elsci.assertreflectionequals.Animal", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfAllOfTheirPropertyValuesAreDifferent() {
        Person person = new Person(150245872L, (short) 30, 55.1007d, 170, (byte) 0b0111_1111, 100f);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("The id of expected object is 150245872, but actual id is 150245871\n" +
                "The age of expected object is 30, but actual age is 25\n" +
                "The weight of expected object is 55.1007, but actual weight is 50.1007\n" +
                "The height of expected object is 170, but actual height is 164\n" +
                "The shoeSize of expected object is 127, but actual shoeSize is 2\n" +
                "The waist of expected object is 100.0, but actual waist is 69.0\n", e.getMessage());
    }

    @Test
    public void objectsAreEqualIfFieldsWithDifferentValuesWereExcluded() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f);
        new ReflectionAssert().excludeFields("id", "waist").assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfAllFieldsWereExcluded() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = new Person(150245872L, (short) 26, 50.1008d, 165, (byte) 0b11, 70f);
        new ReflectionAssert().
                excludeFields("id", "age", "weight", "height", "shoeSize", "waist").
                assertReflectionEquals(person, person2);
    }

    @Test
    public void throwsIllegalArgumentExceptionIfSpecifiedFieldIsNotPropertyOfSpecifiedObject() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ReflectionAssert().
                excludeFields("id", "test").
                assertReflectionEquals(person, person2));
        assertEquals("test field is not field of specified object",
                "test field is not field of specified object");
    }
}
