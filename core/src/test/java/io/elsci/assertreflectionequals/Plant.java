package io.elsci.assertreflectionequals;

public class Plant {
    protected long id;
    protected short age;
    protected int height;
    protected float[] floatArray;
    protected double[] doubleArray;

    public Plant(long id, short age, int height, float[] floatArray, double[] doubleArray) {
        this.id = id;
        this.age = age;
        this.height = height;
        this.floatArray = floatArray;
        this.doubleArray = doubleArray;
    }
}
