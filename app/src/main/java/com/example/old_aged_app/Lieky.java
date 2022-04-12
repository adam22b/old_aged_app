package com.example.old_aged_app;

public class Lieky {
    private int ID;
    private String nazov;
    private String cas;
    private String info;

    public Lieky(int ID, String nazov, String cas, String info) {
        this.ID = ID;
        this.nazov = nazov;
        this.cas = cas;
        this.info = info;
    }

    public String getNazov() {
        return nazov;
    }

    public String getCas() {
        return cas;
    }

    public String getInfo() {
        return info;
    }
}
