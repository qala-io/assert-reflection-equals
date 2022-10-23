package io.elsci.assertreflectionequals;

public class Bacteria {
    int id;
//    Insect insect;
    Bacteria bacteria;
    float size;

    public Bacteria(int id, float size) {
        this.id = id;
        this.size = size;
    }

    public void setBacteria(Bacteria bacteria) {
        this.bacteria = bacteria;
    }
}
