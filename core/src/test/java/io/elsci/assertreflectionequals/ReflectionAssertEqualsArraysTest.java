package io.elsci.assertreflectionequals;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReflectionAssertEqualsArraysTest {
    @Test
    public void objectsAreEqualIfBothArrayFieldsAreNull() {
        Person person = new Person(150245871L, (short) 25, Double.POSITIVE_INFINITY, 164, (byte) 0b10,
                Float.POSITIVE_INFINITY, (char) 77, true,null, new long[]{1, 16, 34, 149, 17});
        Person person2 = new Person(150245871L, (short) 25, Double.POSITIVE_INFINITY, 164, (byte) 0b10,
                Float.POSITIVE_INFINITY, (char) 77, true,null, new long[]{1, 16, 34, 149, 17});
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfBothArrayFieldsAreEmpty() {
        Person person = new Person(150245871L, (short) 25, Double.NEGATIVE_INFINITY, 164, (byte) 0b10,
                Float.NEGATIVE_INFINITY, (char) 77, true, new Integer[]{}, new long[]{});
        Person person2 = new Person(150245871L, (short) 25, Double.NEGATIVE_INFINITY, 164, (byte) 0b10,
                Float.NEGATIVE_INFINITY, (char) 77, true, new Integer[]{}, new long[]{});
        new ReflectionAssert().assertReflectionEquals(person, person2);
    }

    @Test
    public void objectsAreEqualIfArrayFieldsWithDifferentValuesWereExcluded() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{10.10f, 40.60f, 20.00f}, new double[]{100000d, 400000d, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{-10.10f, 00.00f, 2250.10f}, new double[]{100000d, Double.POSITIVE_INFINITY, 90000d},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        new ReflectionAssert().excludeFields(Plant.class, "floatArray", "doubleArray").assertReflectionEquals(plant, plant2);
    }

    @Test
    public void arraysAreEqualEvenIfOrderOfElementsDiffers_ifLinientOrderOn() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{10.10f, Float.NaN, 40.60f, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 0.0f},
                new double[]{0.0d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, -100000d, Double.NEGATIVE_INFINITY, 90000d, Double.NaN},
                new Character[]{'b', 'a', 'c'}, new boolean[]{false, true, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, 40.60f, 10.10f, Float.NaN, Float.POSITIVE_INFINITY, 0.0f},
                new double[]{Double.POSITIVE_INFINITY, -100000d, 0.0d, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 90000d, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        new ReflectionAssert().withLenientOrder().assertReflectionEquals(plant, plant2);
    }

    @Test
    public void objectsAreEqualIfElementsInArrayAreLocatedInNaturalOrderAndArraysWereSortedAnyway() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{Double.NEGATIVE_INFINITY, -100000d, 0.0d, 90000d, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{false, true, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{Double.NEGATIVE_INFINITY, -100000d, 0.0d, 90000d, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{false, true, true});
        new ReflectionAssert().withLenientOrder().assertReflectionEquals(plant, plant2);
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfExpectedObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, null);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=null>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: null\n" +
                "longArray actual:   [16, 34, 17, 149, 1]", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, null);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[16, 34, 17, 149, 1]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[5, 10, 897, 0, 7], longArray=null>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: [16, 34, 17, 149, 1]\n" +
                "longArray actual:   null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfExpectedObjectIsEmpty() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{16, 34, 17, 149, 1, 100});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[5, 10, 897, 0, 7], longArray=[16, 34, 17, 149, 1, 100]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: []\n" +
                "longArray actual:   [16, 34, 17, 149, 1, 100]", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfActualObjectIsEmpty() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[16, 34, 17, 149, 1]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[5, 10, 897, 0, 7], longArray=[]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: [16, 34, 17, 149, 1]\n" +
                "longArray actual:   []", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfCountOfValuesInArraysIsDifferent() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 1, 100});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[5, 10, 897, 0, 7], longArray=[1, 16, 34, 149, 1, 100]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: [1, 16, 34]\n" +
                "longArray actual:   [1, 16, 34, 149, 1, 100]", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfElementsInArrayAreNotLocatedInNaturalOrderAndArraysWereNotSorted() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-10, -897, -7, -5, -14}, new long[]{34, 149, 34, 1, 17});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[-10, -897, -7, -5, -14], longArray=[34, 149, 34, 1, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "intArray expected: [-5, -10, -897, -14, -7]\n" +
                "intArray actual:   [-10, -897, -7, -5, -14]\n" +
                "\n" +
                "intArray[0] expected: -5\n" +
                "intArray[0] actual:   -10", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfByteArrayFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{-112, 115, 0}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{112, 115, 0}, new short[]{40, 30, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("\nExpected: Animal<id=150245875, age=25, weight=50.1007, height=165, byteArray=[-112, 115, 0], " +
                "shortArray=[40, 30, 10]>\n" +
                "Actual:   Animal<id=150245875, age=25, weight=50.1007, height=165, byteArray=[112, 115, 0], " +
                "shortArray=[40, 30, 10]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "byteArray expected: [-112, 115, 0]\n" +
                "byteArray actual:   [112, 115, 0]\n" +
                "\n" +
                "byteArray[0] expected: -112\n" +
                "byteArray[0] actual:   112", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfShortArrayFieldsAreNotEqual() {
        Animal animal = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{-112, -115, -20}, new short[]{40, 30, 10});
        Animal animal2 = new Animal(150245875L, (short) 25, 50.1007d, 165,
                new Byte[]{-112, -115, -20}, new short[]{10, 10, 10});
        AssertionError e = assertThrows(AssertionError.class,
                () -> new ReflectionAssert().assertReflectionEquals(animal, animal2));
        assertEquals("\nExpected: Animal<id=150245875, age=25, weight=50.1007, height=165, " +
                "byteArray=[-112, -115, -20], shortArray=[40, 30, 10]>\n" +
                "Actual:   Animal<id=150245875, age=25, weight=50.1007, height=165, byteArray=[-112, -115, -20], " +
                "shortArray=[10, 10, 10]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "shortArray expected: [40, 30, 10]\n" +
                "shortArray actual:   [10, 10, 10]\n" +
                "\n" +
                "shortArray[0] expected: 40\n" +
                "shortArray[0] actual:   10", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfIntegerArrayFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-10, -897, -7, -5, -14}, new long[]{16, 34, 17, 149, 1});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[-10, -897, -7, -5, -14], longArray=[16, 34, 17, 149, 1]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "intArray expected: [-5, -10, -897, -14, -7]\n" +
                "intArray actual:   [-10, -897, -7, -5, -14]\n" +
                "\n" +
                "intArray[0] expected: -5\n" +
                "intArray[0] actual:   -10", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfLongArrayFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 14, 0}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 14, 0}, new long[]{34, 149, 34, 1, 17});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\nExpected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 14, 0], longArray=[16, 34, 17, 149, 1]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[5, 10, 897, 14, 0], longArray=[34, 149, 34, 1, 17]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: [16, 34, 17, 149, 1]\n" +
                "longArray actual:   [34, 149, 34, 1, 17]\n" +
                "\n" +
                "longArray[0] expected: 16\n" +
                "longArray[0] actual:   34", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfFloatArrayFieldsAreNotEqual() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{Double.NEGATIVE_INFINITY, -100000d, 0.0d, 90000d, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.POSITIVE_INFINITY, 0.0f, Float.NEGATIVE_INFINITY, -10.10f, 40.60f, Float.NaN},
                new double[]{Double.NEGATIVE_INFINITY, -100000d, 0.0d, 90000d, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(plant, plant2));
        assertEquals("\nExpected: Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, " +
                "floatArray=[-Infinity, -10.1, 0.0, 40.6, Infinity, NaN], doubleArray=[-Infinity, -100000.0, 0.0, " +
                "90000.0, Infinity, NaN], charArray=[a, b, c], booleanArray=[true, false, true]>\n" +
                "Actual:   Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, floatArray=[Infinity, " +
                "0.0, -Infinity, -10.1, 40.6, NaN], doubleArray=[-Infinity, -100000.0, 0.0, 90000.0, Infinity, NaN], " +
                "charArray=[a, b, c], booleanArray=[true, false, true]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "floatArray expected: [-Infinity, -10.1, 0.0, 40.6, Infinity, NaN]\n" +
                "floatArray actual:   [Infinity, 0.0, -Infinity, -10.1, 40.6, NaN]\n" +
                "\n" +
                "floatArray[0] expected: -Infinity\n" +
                "floatArray[0] actual:   Infinity", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfDoubleArrayFieldsAreNotEqual() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{-100000d, 0.0d, 90000d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{Double.NEGATIVE_INFINITY, -100000d, 0.0d, 90000d, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(plant, plant2));
        assertEquals("\nExpected: Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, " +
                "floatArray=[-Infinity, -10.1, 0.0, 40.6, Infinity, NaN], doubleArray=[-100000.0, 0.0, 90000.0, " +
                "-Infinity, Infinity, NaN], charArray=[a, b, c], booleanArray=[true, false, true]>\n" +
                "Actual:   Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, floatArray=[-Infinity, " +
                "-10.1, 0.0, 40.6, Infinity, NaN], doubleArray=[-Infinity, -100000.0, 0.0, 90000.0, Infinity, NaN], " +
                "charArray=[a, b, c], booleanArray=[true, false, true]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "doubleArray expected: [-100000.0, 0.0, 90000.0, -Infinity, Infinity, NaN]\n" +
                "doubleArray actual:   [-Infinity, -100000.0, 0.0, 90000.0, Infinity, NaN]\n" +
                "\n" +
                "doubleArray[0] expected: -100000.0\n" +
                "doubleArray[0] actual:   -Infinity", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfCharArrayFieldsAreNotEqual() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{-100000d, 0.0d, 90000d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{-100000d, 0.0d, 90000d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'d', 'e', 'f'}, new boolean[]{true, false, true});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(plant, plant2));
        assertEquals("\nExpected: Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, " +
                "floatArray=[-Infinity, -10.1, 0.0, 40.6, Infinity, NaN], doubleArray=[-100000.0, 0.0, 90000.0, " +
                "-Infinity, Infinity, NaN], charArray=[a, b, c], booleanArray=[true, false, true]>\n" +
                "Actual:   Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, floatArray=[-Infinity, " +
                "-10.1, 0.0, 40.6, Infinity, NaN], doubleArray=[-100000.0, 0.0, 90000.0, -Infinity, Infinity, NaN], " +
                "charArray=[d, e, f], booleanArray=[true, false, true]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "charArray expected: [a, b, c]\n" +
                "charArray actual:   [d, e, f]\n" +
                "\n" +
                "charArray[0] expected: a\n" +
                "charArray[0] actual:   d", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfBooleanArrayFieldsAreNotEqual() {
        Plant plant = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{-100000d, 0.0d, 90000d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, false, true});
        Plant plant2 = new Plant(150245871L, (short) 3, 50, true, 'a',
                new Float[]{Float.NEGATIVE_INFINITY, -10.10f, 0.0f, 40.60f, Float.POSITIVE_INFINITY, Float.NaN},
                new double[]{-100000d, 0.0d, 90000d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN},
                new Character[]{'a', 'b', 'c'}, new boolean[]{true, true, true});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(plant, plant2));
        assertEquals("\nExpected: Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, " +
                "floatArray=[-Infinity, -10.1, 0.0, 40.6, Infinity, NaN], doubleArray=[-100000.0, 0.0, 90000.0, " +
                "-Infinity, Infinity, NaN], charArray=[a, b, c], booleanArray=[true, false, true]>\n" +
                "Actual:   Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, floatArray=[-Infinity, " +
                "-10.1, 0.0, 40.6, Infinity, NaN], doubleArray=[-100000.0, 0.0, 90000.0, -Infinity, Infinity, NaN], " +
                "charArray=[a, b, c], booleanArray=[true, true, true]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "booleanArray expected: [true, false, true]\n" +
                "booleanArray actual:   [true, true, true]\n" +
                "\n" +
                "booleanArray[1] expected: false\n" +
                "booleanArray[1] actual:   true", e.getMessage());
    }
}
