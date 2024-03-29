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
    public void arraysAreEqualEvenIfOrderOfElementsDiffers_ifLenientOrderOn() {
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
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=null>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>\n" +
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
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
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
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
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
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[16, 34, 17, 149, 1]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[5, 10, 897, 0, 7], longArray=[]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: [16, 34, 17, 149, 1]\n" +
                "longArray actual:   []", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfActualObjectIsEmptyAndArrayFieldOfExpectedObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, null);
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=null>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[5, 10, 897, 0, 7], longArray=[]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: null\n" +
                "longArray actual:   []", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldOfActualObjectIsNullAndArrayFieldOfExpectedObjectIsEmpty() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, null);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
                " clothingSize=M, adult=true, intArray=[5, 10, 897, 0, 7], longArray=[]>\n" +
                "Actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M," +
                " adult=true, intArray=[5, 10, 897, 0, 7], longArray=null>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "longArray expected: []\n" +
                "longArray actual:   null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfCountOfValuesInArraysIsDifferent() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{5, 10, 897, 0, 7}, new long[]{1, 16, 34, 149, 1, 100});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(person, person2));
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
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
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
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
        assertEquals("\n" +
                "Expected: Animal<id=150245875, age=25, weight=50.1007, height=165, byteArray=[-112, 115, 0], " +
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
        assertEquals("\n" +
                "Expected: Animal<id=150245875, age=25, weight=50.1007, height=165, " +
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
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
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
        assertEquals("\n" +
                "Expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0," +
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
        assertEquals("\n" +
                "Expected: Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, " +
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
        assertEquals("\n" +
                "Expected: Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, " +
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
        assertEquals("\n" +
                "Expected: Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, " +
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
        assertEquals("\n" +
                "Expected: Plant<id=150245871, age=3, height=50, isFlowering=true, letter=a, " +
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

    @Test
    public void objectsAreNotEqualIfObjectArrayOfExpectedObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, null);
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, new Object[]{person, person2});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<types=[Cantharellus friesii, Omphalotus olearius], livingBeings=null>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Omphalotus olearius], livingBeings=[" +
                "Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>, Person<id=150245871, age=25, " +
                "weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "livingBeings expected: null\n" +
                "livingBeings actual:   [Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>, " +
                "Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, " +
                "adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfObjectArrayOfActualObjectIsNull() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, new Object[]{person, person2});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, null);
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<types=[Cantharellus friesii, Omphalotus olearius], livingBeings=[Person<id=150245871, " +
                "age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>, Person<id=150245871, age=25, " +
                "weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Omphalotus olearius], livingBeings=null>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "livingBeings expected: [Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>, " +
                "Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, " +
                "adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]\n" +
                "livingBeings actual:   null", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfObjectArrayOfExpectedObjectIsEmpty() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, new Object[]{});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, new Object[]{person, person2});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<types=[Cantharellus friesii, Omphalotus olearius], livingBeings=[]>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Omphalotus olearius], livingBeings=[" +
                "Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, " +
                "adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>, Person<id=150245871, " +
                "age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "livingBeings expected: []\n" +
                "livingBeings actual:   [Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>, " +
                "Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, " +
                "adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfObjectArrayOfActualObjectIsEmpty() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, new Object[]{person, person2});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, new Object[]{});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<" +
                "types=[Cantharellus friesii, Omphalotus olearius], livingBeings=[Person<id=150245871, " +
                "age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>, Person<id=150245871, age=25, " +
                "weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Omphalotus olearius], livingBeings=[]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "livingBeings expected: [Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>, " +
                "Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, " +
                "adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]\n" +
                "livingBeings actual:   []", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfStringArrayFieldsAreNotEqual() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{person});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Omphalotus olearius"}, new Object[]{person});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Person<id=150245871, " +
                "age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Omphalotus olearius], livingBeings=[Person<id=150245871, " +
                "age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "types expected: [Cantharellus friesii, Cantharellus lateritius]\n" +
                "types actual:   [Cantharellus friesii, Omphalotus olearius]\n" +
                "\n" +
                "types[1] expected: Cantharellus lateritius\n" +
                "types[1] actual:   Omphalotus olearius", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldsWithObjectsAreNotEqualBecauseOfDifferentPrimitiveValues() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 26, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});

        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{person});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{person2});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Person<id=150245871, " +
                "age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Person<id=150245871, " +
                "age=26, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "livingBeings[0].age expected: 25\n" +
                "livingBeings[0].age actual:   26\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>\n" +
                "actual:   Person<id=150245871, age=26, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>", e.getMessage());
    }


    @Test
    public void objectsAreNotEqualIfArrayFieldsWithObjectsAreNotEqualBecauseOfDifferentPrimitiveValuesInArrays() {
        Person person = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -14, -7}, new long[]{16, 34, 17, 149, 1});
        Person person2 = new Person(150245871L, (short) 25, 50.1007d, 164, (byte) 0b10, 69f,
                (char) 77, true, new Integer[]{-5, -10, -897, -15, -8}, new long[]{16, 34, 17, 149, 1});

        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{person});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{person2});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Person<id=150245871, " +
                "age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Person<id=150245871, " +
                "age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, clothingSize=M, adult=true, " +
                "intArray=[-5, -10, -897, -15, -8], longArray=[16, 34, 17, 149, 1]>]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "livingBeings[0].intArray expected: [-5, -10, -897, -14, -7]\n" +
                "livingBeings[0].intArray actual:   [-5, -10, -897, -15, -8]\n" +
                "\n" +
                "intArray[3] expected: -14\n" +
                "intArray[3] actual:   -15\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -14, -7], longArray=[16, 34, 17, 149, 1]>\n" +
                "actual:   Person<id=150245871, age=25, weight=50.1007, height=164, shoeSize=2, waist=69.0, " +
                "clothingSize=M, adult=true, intArray=[-5, -10, -897, -15, -8], longArray=[16, 34, 17, 149, 1]>", e.getMessage());
    }

    @Test
    public void objectsAreNotEqualIfArrayFieldsWithObjectsAreNotEqualBecauseOfDifferentObjectsInArrays() {
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});
        Fungi fungi3 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});
        Fungi fungi4 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});

        fungi3.setLivingBeings(new Object[]{fungi2, fungi});
        fungi4.setLivingBeings(new Object[]{fungi, fungi2});

        fungi.setLivingBeings(new Object[]{fungi3, fungi4});
        fungi2.setLivingBeings(new Object[]{fungi4, fungi3});

        new ReflectionAssert().assertReflectionEquals(fungi, fungi2);
    }

    @Test
    public void errorMessageIsCorrectIfObjectFromArrayHasItsOwnProblemObject() {
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});

        Fungi fungi3 = new Fungi(new String[]{"th", ""}, new Object[]{});
        Fungi fungi4 = new Fungi(new String[]{"th", ""}, new Object[]{});

        Fungi fung5 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});
        Fungi fungi6 = new Fungi(new String[]{"Cantharellus friesii", "Ca"}, new Object[]{});

        fungi3.setLivingBeings(new Object[]{fung5});
        fungi4.setLivingBeings(new Object[]{fungi6});

        fungi.setLivingBeings(new Object[]{fungi3});
        fungi2.setLivingBeings(new Object[]{fungi4});
        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Fungi<types=[th, ], " +
                "livingBeings=[Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[]>]>]>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Fungi<types=[th, ], " +
                "livingBeings=[Fungi<types=[Cantharellus friesii, Ca], livingBeings=[]>]>]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "livingBeings[0].livingBeings[0].types expected: [Cantharellus friesii, Cantharellus lateritius]\n" +
                "livingBeings[0].livingBeings[0].types actual:   [Cantharellus friesii, Ca]\n" +
                "\n" +
                "types[1] expected: Cantharellus lateritius\n" +
                "types[1] actual:   Ca\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[]>\n" +
                "actual:   Fungi<types=[Cantharellus friesii, Ca], livingBeings=[]>", e.getMessage());
    }

    @Test
    public void errorMessageIsCorrectIfObjectFromArrayHasItsOwnArrayWithProblemObject() {
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});

        Fungi fungi3 = new Fungi(new String[]{"th", ""}, new Object[]{});
        Fungi fungi4 = new Fungi(new String[]{"th", ""}, new Object[]{});

        Fungi fung5 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});
        Fungi fungi6 = new Fungi(new String[]{"Cantharellus friesii", "Ca"}, new Object[]{});

        fungi3.setLivingBeings(new Object[]{fung5});
        fungi4.setLivingBeings(new Object[]{fungi6});

        fungi.setLivingBeings(new Object[]{fungi3});
        fungi2.setLivingBeings(new Object[]{fungi4});

        AssertionError e = assertThrows(AssertionError.class, () ->
                new ReflectionAssert().assertReflectionEquals(fungi, fungi2));
        assertEquals("\n" +
                "Expected: Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Fungi<types=[th, ], " +
                "livingBeings=[Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[]>]>]>\n" +
                "Actual:   Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[Fungi<types=[th, ], " +
                "livingBeings=[Fungi<types=[Cantharellus friesii, Ca], livingBeings=[]>]>]>\n" +
                "\n" +
                "--- Fields that differed ---\n" +
                "livingBeings[0].livingBeings[0].types expected: [Cantharellus friesii, Cantharellus lateritius]\n" +
                "livingBeings[0].livingBeings[0].types actual:   [Cantharellus friesii, Ca]\n" +
                "\n" +
                "types[1] expected: Cantharellus lateritius\n" +
                "types[1] actual:   Ca\n" +
                "\n" +
                "--- Objects that differed ---\n" +
                "expected: Fungi<types=[Cantharellus friesii, Cantharellus lateritius], livingBeings=[]>\n" +
                "actual:   Fungi<types=[Cantharellus friesii, Ca], livingBeings=[]>", e.getMessage());
    }

    @Test
    public void excludedFieldOptionTakesIntoAccountWhileComparingObjectsFromArray() {
        Fungi fungi = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus lateritius"}, new Object[]{});
        Fungi fungi2 = new Fungi(new String[]{"Cantharellus friesii", "Cantharellus"}, new Object[]{});

        Fungi fungi3 = new Fungi(new String[]{"t", ""}, new Object[]{});
        Fungi fungi4 = new Fungi(new String[]{"th", ""}, new Object[]{});

        Fungi fung5 = new Fungi(new String[]{"Cantharellus friesii"}, new Object[]{});
        Fungi fungi6 = new Fungi(new String[]{"Cantharellus friesii", "Ca"}, new Object[]{});

        fungi3.setLivingBeings(new Object[]{fung5});
        fungi4.setLivingBeings(new Object[]{fungi6});

        fungi.setLivingBeings(new Object[]{fungi3});
        fungi2.setLivingBeings(new Object[]{fungi4});

        new ReflectionAssert().excludeFields(Fungi.class, "types").assertReflectionEquals(fungi, fungi2);
    }
}
