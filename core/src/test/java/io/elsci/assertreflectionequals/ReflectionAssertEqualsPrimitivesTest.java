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
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=0, " +
                "waist=70.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=70.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "shoeSize expected: 0\n" +
                "shoeSize actual:   2", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfShortFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245875L, (short) 24, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("\nExpected: Animal<id=150245875, age=25, weight=50.1007, height=165, " +
                "byteArray=[112, 114, 111], shortArray=[40, 30, 10]>\n" +
                "Actual:   Animal<id=150245875, age=24, weight=50.1007, height=165, byteArray=[112, 114, 111], " +
                "shortArray=[40, 30, 10]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "age expected: 25\n" +
                "age actual:   24", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfIntegerFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245875L, (short) 25, 50.1007d, -165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("\nExpected: Animal<id=150245875, age=25, weight=50.1007, height=165, " +
                "byteArray=[112, 114, 111], shortArray=[40, 30, 10]>\n" +
                "Actual:   Animal<id=150245875, age=25, weight=50.1007, height=-165, byteArray=[112, 114, 111], " +
                "shortArray=[40, 30, 10]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "height expected: 165\n" +
                "height actual:   -165", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfLongFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245874L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("\nExpected: Animal<id=150245875, age=25, weight=50.1007, height=165, " +
                "byteArray=[112, 114, 111], shortArray=[40, 30, 10]>\n" +
                "Actual:   Animal<id=150245874, age=25, weight=50.1007, height=165, byteArray=[112, 114, 111], " +
                "shortArray=[40, 30, 10]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "id expected: 150245875\n" +
                "id actual:   150245874", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfFloatFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, " +
                "waist=70.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "waist expected: 70.0\n" +
                "waist actual:   69.0", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfDoubleFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245875L, (short) 25, 50.1006d, 165,
                new Byte[]{112, 114, 111}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("\nExpected: Animal<id=150245875, age=25, weight=50.1007, height=165, " +
                "byteArray=[112, 114, 111], shortArray=[40, 30, 10]>\n" +
                "Actual:   Animal<id=150245875, age=25, weight=50.1006, height=165, byteArray=[112, 114, 111], " +
                "shortArray=[40, 30, 10]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "weight expected: 50.1007\n" +
                "weight actual:   50.1006", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfCharFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 76, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, " +
                "waist=70.0, clothingSize=L, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=70.0, " +
                "clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "clothingSize expected: L\n" +
                "clothingSize actual:   M", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfBooleanFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 70f,
                (char) 77, false, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 17});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, " +
                "waist=70.0, clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=70.0, " +
                "clothingSize=M, adult=false, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "adult expected: true\n" +
                "adult actual:   false", e.getMessage());
    }
}