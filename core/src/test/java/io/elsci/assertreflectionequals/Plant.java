package io.elsci.assertreflectionequals;

public class Plant {
    protected long id;
    protected short age;
    protected int height;
    protected boolean isFlowering;
    protected char letter;
    protected float[] floatArray;
    protected double[] doubleArray;

    public Plant(long id, short age, int height, boolean isFlowering, char letter, float[] floatArray, double[] doubleArray) {
        this.id = id;
        this.age = age;
        this.height = height;
        this.isFlowering = isFlowering;
        this.letter = letter;
        this.floatArray = floatArray;
        this.doubleArray = doubleArray;
    }
}
