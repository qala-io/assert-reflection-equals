package io.elsci.assertreflectionequals;

public class Insect {
    int id;
    Bacteria bacteria, bacteria1;
    float size;

    public Insect(int id, float size) {
        this.id = id;
        this.size = size;
    }

    public void setBacteria(Bacteria bacteria) {
        this.bacteria = bacteria;
    }
    public void setBacteria1(Bacteria bacteria1) {
        this.bacteria1 = bacteria1;
    }
}
