package io.elsci.assertreflectionequals;

import org.junit.Test;
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
    public void objectIsEqualToItselfItsNull() {
        Person person = null;
        new ReflectionAssert().assertReflectionEquals(person, person);
    }

    @Test
    public void objectsAreEqualIfBothAreNull() {
        Person person = null;
        Person person2 = null;
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfBothAreNullAndBelongToDifferentClasses() {
        Person person = null;
        Animal animal = null;
        new ReflectionAssert().assertReflectionEquals(person, animal);
    }

    @Test
    public void objectsAreEqualIfProblemFieldWasExcluded() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 896, 0, 7}, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().
                excludeFields(Person.class, "intArray").
                assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfAllProblemFieldsWereExcluded() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 26, 50.1008d, 165, (byte) 0b11, 70f,
                (char) 77, true, new Integer[]{6, 111, 898, 1, 8}, new long[]{2, 17, 35, 150, 18});
        new ReflectionAssert().
                excludeFields(Person.class, "id", "age", "weight", "height",
                        "shoeSize", "clothingSize", "adult", "waist", "intArray", "longArray").
                assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreNotEqualIfExpectedObjectIsNull() {
        Person person = null;
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: null\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = null;
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfTheirClassesAreDifferent() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164, new Byte[]{112, 114, 111},
                new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, animal));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Animal<id=150245871, age=25, weight=50.1007, height=164, byteArray=[112, 114, 111], " +
                "shortArray=[40, 30, 10]>\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: object of type Person\n" +
                "actual:   object of type Animal", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfObjectsBelongToDifferentClassesAndExpectedObjectIsNull() {
        Person person = null;
        Animal animal = new Animal(150245871L, (short) 25, 50.1007d, 164, new Byte[]{112, 114, 111},
                new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, animal));
        assertEquals("\nExpected: null\n" +
                "Actual:   Animal<id=150245871, age=25, weight=50.1007, height=164, byteArray=[112, 114, 111], " +
                "shortArray=[40, 30, 10]>", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfObjectsBelongToDifferentClassesAndActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Animal animal = null;
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, animal));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, " +
                "waist=69.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfAllOfTheirPropertyValuesAreDifferent() {
        Person person = new Person(150245872L, (short) 30, 55.1007d, 170, (byte) 0b0111_1111, 100f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 76, false, new Integer[]{0, 0, 0, 0, 0}, new long[]{2, 17, 35, 150, 18});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245872, age=30, weight=55.1007, height=170, shoeSize=127, " +
                "waist=100.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=L, adult=false, intArray=[0, 0, 0, 0, 0], longArray=[2, 17, 35, 150, 18]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "id expected: 150245872\n" +
                "id actual:   150245871", e.getMessage());
    }

    @Test
    public void throwsIllegalArgumentExceptionIfSpecifiedExcludedFieldIsNotFieldOfSpecifiedObject() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ReflectionAssert().
                excludeFields(Person.class, "id", "test").
                assertReflectionEquals(person, person2));
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245872, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "'test' is not field of Person class", e.getMessage());
    }

    @Test
    public void throwsIllegalArgumentExceptionIfSpecifiedExcludedFieldIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ReflectionAssert().
                excludeFields(Person.class, "null").
                assertReflectionEquals(person, person2));
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245872, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "'null' is not field of Person class", e.getMessage());
    }

    @Test
    public void specifiedFieldIsNotExcludedIfSpecifiedClassIsNotMatched() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().excludeFields(Bacteria.class, "id").assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, " +
                "waist=69.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245872, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "id expected: 150245871\n" +
                "id actual:   150245872", e.getMessage());
    }

    @Test
    public void excludedFieldIsNotReasonOfError() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 26, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().excludeFields(Bacteria.class, "id").assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, " +
                "waist=69.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245872, age=26, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "id expected: 150245871\n" +
                "id actual:   150245872", e.getMessage());
    }

    @Test
    public void excludingSameClassTwice_overvwitesPreviousExclusion() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, false, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().excludeFields(Person.class, "adult").
                        excludeFields(Person.class, "id").assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, " +
                "waist=69.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245872, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, " +
                "adult=false, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "adult expected: true\n" +
                "adult actual:   false", e.getMessage());
    }

    @Test
    public void noErrorIfTheSameClassWasExcludedTwice() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, false, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().excludeFields(Person.class, "adult").
                        excludeFields(Person.class, "adult").assertReflectionEquals(person, person2);
    }

    @Test
    public void noErrorIfExcludedFieldsParameterContainsNullObjects() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().excludeFields(null, "null").assertReflectionEquals(person, person2);
    }

    @Test
    public void errorIsPresentIfSpecifiedExcludedFieldIsCorrectButSpecifiedExcludedClassIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245872L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().excludeFields(null, "id").assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245872, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "id expected: 150245871\n" +
                "id actual:   150245872", e.getMessage());
    }
}