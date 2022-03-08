package io.elsci.assertreflectionequals;

public class Person {
    long id;
    short age;
    double weight;
    int height;
    byte shoeSize;
    float waist;
    char clothingSize;
    boolean adult;
    Integer[] intArray;
    long[] longArray;

    public Person(long id, short age, double weight, int height, byte shoeSize, float waist, char clothingSize, boolean adult,
                  Integer[] intArray, long[] longArray) {
        this.id = id;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.shoeSize = shoeSize;
        this.waist = waist;
        this.clothingSize = clothingSize;
        this.adult = adult;
        this.intArray = intArray;
        this.longArray = longArray;
    }
}
