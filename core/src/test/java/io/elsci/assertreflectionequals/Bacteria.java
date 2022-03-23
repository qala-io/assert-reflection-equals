package io.elsci.assertreflectionequals;

public class Bacteria {
    int id;
    Insect insect;
    float size;

    public Bacteria(int id, float size) {
        this.id = id;
        this.size = size;
    }

    public void setInsect(Insect insect) {
        this.insect = insect;
    }
}
