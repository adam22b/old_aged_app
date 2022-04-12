package com.example.old_aged_app;

public class Kroky {

    private int ID;
    private String datum;
    private String kroky;

    public Kroky(int ID, String datum, String kroky) {
        this.ID = ID;
        this.datum = datum;
        this.kroky = kroky;
    }

    public int getID() {
        return ID;
    }

    public String getDatum() {
        return datum;
    }

    public String getKroky() {
        try {
            return kroky;
        } catch (Exception e) {
            System.out.println("Error : " + e);
            return "0";
        }
    }
}
