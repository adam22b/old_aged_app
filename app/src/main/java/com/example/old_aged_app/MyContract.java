package com.example.old_aged_app;

public final class MyContract {
    public static class Kroky{
    public static final String TABLE_NAME = "kroky";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KROK = "krok";
    public static final String COLUMN_DATUM = "datum";
    }
    public static class Kontakty{
        public static final String TABLE_NAME = "kontakty";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MENO = "meno";
        public static final String COLUMN_CISLO = "cislo";
    }
    public static class Lieky{
        public static final String TABLE_NAME = "lieky";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAZOV = "nazov";
        public static final String COLUMN_CAS = "cas";
        public static final String COLUMN_INFO = "info";
    }
}
