package io.elsci.assertreflectionequals;

public class Insect {
    int id;
    Bacteria bacteria;
    float size;

    public Insect(int id, float size) {
        this.id = id;
        this.size = size;
    }

    public void setBacteria(Bacteria bacteria) {
        this.bacteria = bacteria;
    }
}
