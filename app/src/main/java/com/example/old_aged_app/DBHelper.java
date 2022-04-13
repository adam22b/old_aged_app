package com.example.old_aged_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "appDB";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_KROKY = " CREATE TABLE " +
                MyContract.Kroky.TABLE_NAME + " ( "
                + MyContract.Kroky.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + MyContract.Kroky.COLUMN_KROK + " TEXT , "
                + MyContract.Kroky.COLUMN_DATUM + " TEXT ) ";

        db.execSQL(SQL_CREATE_TABLE_KROKY);

        String SQL_CREATE_TABLE_KONTAKTY = " CREATE TABLE " +
                MyContract.Kontakty.TABLE_NAME + " ( "
                + MyContract.Kontakty.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + MyContract.Kontakty.COLUMN_MENO + " TEXT, "
                + MyContract.Kontakty.COLUMN_CISLO + " TEXT )";

        db.execSQL(SQL_CREATE_TABLE_KONTAKTY);

        String SQL_CREATE_TABLE_LIEKY = " CREATE TABLE " +
                MyContract.Lieky.TABLE_NAME + " ( "
                + MyContract.Lieky.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + MyContract.Lieky.COLUMN_NAZOV + " TEXT , "
                + MyContract.Lieky.COLUMN_CAS + " TEXT , "
                + MyContract.Lieky.COLUMN_INFO + " TEXT ) ";

        db.execSQL(SQL_CREATE_TABLE_LIEKY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVar, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + MyContract.Kroky.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MyContract.Kontakty.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MyContract.Lieky.TABLE_NAME);

        onCreate(db);
    }
}
