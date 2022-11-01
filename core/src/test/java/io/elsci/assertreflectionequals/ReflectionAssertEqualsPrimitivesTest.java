package io.elsci.assertreflectionequals;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReflectionAssertEqualsPrimitivesTest {
    @Test
    public void objectsAreEqualIfPrimitiveFieldsWithDifferentValuesWereExcluded() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{10.10f, 40.60f, 20.00f}, new double[]{100000d, 400000d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'b',
                new Float[]{10.10f, 40.60f, 20.00f}, new double[]{100000d, 400000d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        new ReflectionAssert().excludeFields(Plant.class, "id", "letter").assertReflectionEquals(plant, plant2);
    }

    @Test
    public void objectsAreNotEqualIfByteFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b00, 70f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Values were different for: shoeSize\n" +
                "Expected: 0\n" +
                "Actual: 2\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfShortFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245875L, (short) 24, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("Values were different for: age\n" +
                "Expected: 25\n" +
                "Actual: 24\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfIntegerFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245875L, (short) 25, 50.1007d, -165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("Values were different for: height\n" +
                "Expected: 165\n" +
                "Actual: -165\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfLongFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245874L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("Values were different for: id\n" +
                "Expected: 150245875\n" +
                "Actual: 150245874\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfFloatFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Values were different for: waist\n" +
                "Expected: 70.0\n" +
                "Actual: 69.0\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfDoubleFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245875L, (short) 25, 50.1006d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("Values were different for: weight\n" +
                "Expected: 50.1007\n" +
                "Actual: 50.1006\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfCharFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 76, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Values were different for: clothingSize\n" +
                "Expected: L\n" +
                "Actual: M\n", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfBooleanFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, false, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("Values were different for: adult\n" +
                "Expected: true\n" +
                "Actual: false\n", e.getMessage());
    }
}