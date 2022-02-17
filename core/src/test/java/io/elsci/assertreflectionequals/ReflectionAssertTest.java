package io.elsci.assertreflectionequals;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReflectionAssertTest {
    @Test
    public void twoObjectsAreEqualIfAllValuesOfTheirPropertiesAreEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        ReflectionAssert.assertReflectionEquals(person, person2);
    }

    @Test
    public void objectIsEqualToItself() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        ReflectionAssert.assertReflectionEquals(person, person);
    }

    @Test
    public void objectsAreEqualIfBothAreNull() {
        Person person = null;
        Person person2 = null;
        ReflectionAssert.assertReflectionEquals(person, person2);
    }

    @Test
    public void possibleToCompareObjectsWithPrivateFields() {
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164);
        Animal animal2 = new Animal(150245871L, (short) 25, 50.1007d, 164);
        ReflectionAssert.assertReflectionEquals(animal, animal2);
    }

    @Test
    public void possibleToCompareObjectsWithProtectedFields() {
        Plant plant = new Plant(150245871L, (short) 3, 50);
        Plant plant2 = new Plant(150245871L, (short) 3, 50);
        ReflectionAssert.assertReflectionEquals(plant, plant2);
    }

    @Test
    public void objectsAreNotEqualIfExpectedObjectIsNull() {
        Person person = null;
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        AssertionError e = assertThrows(AssertionError.class,
                () -> ReflectionAssert.assertReflectionEquals(person, person2));
        assertEquals("Objects are not equal since one of them is null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = null;
        AssertionError e = assertThrows(AssertionError.class, () ->
                ReflectionAssert.assertReflectionEquals(person, person2));
        assertEquals("Objects are not equal since one of them is null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirClassesAreDifferent() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164);
        AssertionError e = assertThrows(AssertionError.class, () ->
                ReflectionAssert.assertReflectionEquals(person, animal));
        assertEquals("Objects are not equal since their classes are different", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfAllOfTheirPropertyValuesAreDifferent() {
        Person person = new Person(150245872L, (short) 30, 55.1007d, 170, (byte) 0b0111_1111, 100f);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        AssertionError e = assertThrows(AssertionError.class, () ->
                ReflectionAssert.assertReflectionEquals(person, person2));
        assertEquals("The id of expected object is 150245872, but actual id is 150245871\n" +
                "The age of expected object is 30, but actual age is 25\n" +
                "The weight of expected object is 55.1007, but actual weight is 50.1007\n" +
                "The height of expected object is 170, but actual height is 164\n" +
                "The shoeSize of expected object is 127, but actual shoeSize is 2\n" +
                "The waist of expected object is 100.0, but actual waist is 69.0\n", e.getMessage());
    }
}