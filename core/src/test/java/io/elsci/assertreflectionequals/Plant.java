package io.elsci.assertreflectionequals;

public class Plant {
    protected long id;
    protected short age;
    protected int height;
    protected boolean isFlowering;
    protected char letter;
    protected Float[] floatArray;
    protected double[] doubleArray;
    protected Character[] charArray;
    protected boolean[] booleanArray;


    public Plant(long id, short age, int height, boolean isFlowering, char letter, Float[] floatArray,
                 double[] doubleArray, Character[] charArray, boolean[] booleanArray) {
        this.id = id;
        this.age = age;
        this.height = height;
        this.isFlowering = isFlowering;
        this.letter = letter;
        this.floatArray = floatArray;
        this.doubleArray = doubleArray;
        this.charArray = charArray;
        this.booleanArray = booleanArray;
    }
}
