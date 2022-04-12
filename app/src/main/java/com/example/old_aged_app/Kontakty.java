package com.example.old_aged_app;

public class Kontakty {

    private int ID;
    private String meno;
    private String cislo;

    public Kontakty(int ID, String meno, String cislo) {
        this.ID = ID;
        this.meno = meno;
        this.cislo = cislo;
    }

    public String getMeno() {
        return meno;
    }

    public String getCislo() {
        return cislo;
    }
}
