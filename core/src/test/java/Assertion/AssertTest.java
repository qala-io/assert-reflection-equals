package Assertion;

import org.junit.Test;
import org.junit.Assert;

public class AssertTest {

    @Test
    public void twoObjectsAreEqualIfAllValuesOfTheirPropertiesAreTheSame() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Assertion.assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfExpectedObjectIsNull() {
        Person person = null;
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Assertion.assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        Person person2 = null;
        Assertion.assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfBothAreNull() {
        Person person = null;
        Person person2 = null;
        Assertion.assertReflectionEquals(person, person2);
    }

    @Test
    public void twoObjectsAreNotEqualIfAllOfTheirPropertyValuesAreDifferent() {
        Person person = new Person(150245872L, (short) 30, 55.1007d, 170, (byte) 0b0111_1111, 100f);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f);
        try {
            Assertion.assertReflectionEquals(person, person2);
            Assert.fail("Expected AssertionError");
        } catch (AssertionError e) {
            Assert.assertEquals("The id of expected object is 150245872, but actual id is 150245871\n" +
                    "The age of expected object is 30, but actual age is 25\n" +
                    "The weight of expected object is 55.1007, but actual weight is 50.1007\n" +
                    "The height of expected object is 170, but actual height is 164\n" +
                    "The shoeSize of expected object is 127, but actual shoeSize is 2\n" +
                    "The waist of expected object is 100.0, but actual waist is 69.0\n", e.getMessage());
        }
    }
}