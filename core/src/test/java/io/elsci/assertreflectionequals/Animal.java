package io.elsci.assertreflectionequals;

public class Animal {
    private long id;
    private short age;
    private double weight;
    private int height;
    private byte[] byteArray;
    private short[] shortArray;

    public Animal(long id, short age, double weight, int height, byte[] byteArray, short[] shortArray) {
      this.id = id;
      this.age = age;
      this.weight = weight;
      this.height = height;
      this.byteArray = byteArray;
      this.shortArray = shortArray;
    }
}
