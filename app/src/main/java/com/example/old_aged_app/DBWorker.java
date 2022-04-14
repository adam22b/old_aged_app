package com.example.old_aged_app;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBWorker extends DBHelper {
    public DBWorker(Context context) {
        super(context);
    }

    public Cursor getCursorKontakty() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + MyContract.Kontakty.TABLE_NAME, null);
        c.moveToFirst();
        db.close();
        return c;
    }

    public void addKontakt(Kontakty kontakty) {
        ContentValues values = new ContentValues();
        values.put(MyContract.Kontakty.COLUMN_MENO, kontakty.getMeno());
        values.put(MyContract.Kontakty.COLUMN_CISLO, kontakty.getCislo());

        SQLiteDatabase db = getWritableDatabase();

        long newRowId = db.insert(
                MyContract.Kontakty.TABLE_NAME,
                null,
                values
        );
        db.close();
    }

    public void delKontakty(long ID) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(
                MyContract.Kontakty.TABLE_NAME,
                MyContract.Kontakty.COLUMN_ID + "= ?",
                new String[]{"" + ID});
        db.close();
    }

    private ArrayList<String> kontaktyId = new ArrayList<>();
    public ArrayList<String> getKontaktId() {
        return kontaktyId;
    }


    public ArrayList<String> getKontaktCislo() {
        ArrayList<String> zoznam = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + MyContract.Kontakty.TABLE_NAME,
                null);
        if (c.moveToFirst()) {
            do {
                kontaktyId.add(c.getString(c.
                        getColumnIndex(MyContract.Kontakty.COLUMN_ID)));
                zoznam.add(c.getString(c.
                        getColumnIndex(MyContract.Kontakty.COLUMN_CISLO)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return zoznam;
    }

    public void addKroky(Kroky kroky) {
        ContentValues values = new ContentValues();
        values.put(MyContract.Kroky.COLUMN_KROK, kroky.getKroky());
        values.put(MyContract.Kroky.COLUMN_DATUM, kroky.getDatum());

        SQLiteDatabase db = getWritableDatabase();

        long newRowId = db.insert(
                MyContract.Kroky.TABLE_NAME,
                null,
                values
        );
        db.close();
    }

    public void updateKroky(Kroky kroky) {
        ContentValues values = new ContentValues();
        values.put(MyContract.Kroky.COLUMN_KROK, kroky.getKroky());
        values.put(MyContract.Kroky.COLUMN_DATUM, kroky.getDatum());

        SQLiteDatabase db = getWritableDatabase();
        db.update(
                MyContract.Kroky.TABLE_NAME,
                values,
                MyContract.Kroky.COLUMN_DATUM + "= ?",
                new String[]{"" + kroky.getDatum()}
        );
        db.close();
    }

    public Kroky getKroky(String date) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String[] projection = {MyContract.Kroky.COLUMN_ID, MyContract.Kroky.COLUMN_DATUM, MyContract.Kroky.COLUMN_KROK};
            String selection = MyContract.Kroky.COLUMN_DATUM + "=? ";
            String[] selectionArgs = {"" + date};

            Cursor c = db.query(
                    MyContract.Kroky.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            if (c.getCount() > 0 && c != null && c.moveToFirst()) {
                c.moveToFirst();

                Kroky s = new Kroky(
                        c.getInt(c.getColumnIndex(MyContract.Kroky.COLUMN_ID)),
                        date,
                        c.getString(c.getColumnIndex(MyContract.Kroky.COLUMN_KROK)));
                c.close();
                db.close();
                return s;
            } else {
                return new Kroky(-1, date, "0");
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
            return null;
        }
    }

    public Cursor getCursorLieky() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + MyContract.Lieky.TABLE_NAME, null);
        c.moveToFirst();
        db.close();
        return c;
    }

    public void addLieky(Lieky lieky) {
        ContentValues values = new ContentValues();
        values.put(MyContract.Lieky.COLUMN_NAZOV, lieky.getNazov());
        values.put(MyContract.Lieky.COLUMN_CAS, lieky.getCas());
        values.put(MyContract.Lieky.COLUMN_INFO, lieky.getInfo());

        SQLiteDatabase db = DBWorker.super.getWritableDatabase();

        long newRowId = db.insert(
                MyContract.Lieky.TABLE_NAME,
                null,
                values
        );
        db.close();
    }

    public void delLieky(long ID) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(
                MyContract.Lieky.TABLE_NAME,
                MyContract.Lieky.COLUMN_ID + "= ?",
                new String[]{"" + ID});
        db.close();
    }
}



//    private ArrayList<String> kontaktyId = new ArrayList<>();
//    public ArrayList<String> getKontaktyId() {
//        return kontaktyId;
//    }











