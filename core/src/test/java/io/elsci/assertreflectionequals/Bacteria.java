package io.elsci.assertreflectionequals;

public class Bacteria {
    int id;
    Insect insect;
    float size;
    Integer[] intArray;

    public Bacteria(int id, float size, Integer[] intArray) {
        this.id = id;
        this.size = size;
        this.intArray = intArray;
    }

    public void setInsect(Insect insect) {
        this.insect = insect;
    }
}
