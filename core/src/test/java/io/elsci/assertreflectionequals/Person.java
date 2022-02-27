package io.elsci.assertreflectionequals;

public class Person {
    long id;
    short age;
    double weight;
    int height;
    byte shoeSize;
    float waist;
    Integer[] intArray;
    long[] longArray;

    public Person(long id, short age, double weight, int height, byte shoeSize, float waist, Integer[] intArray, long[] longArray) {
        this.id = id;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.shoeSize = shoeSize;
        this.waist = waist;
        this.intArray = intArray;
        this.longArray = longArray;
    }
}
