package com.example.old_aged_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class KontaktyActivity extends AppCompatActivity {

    ListView listView;
    SimpleCursorAdapter cursorAdapter;
    DBWorker dbWorker = new DBWorker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontakty);
        Button optBtn = findViewById(R.id.optionBtn);
        listView = findViewById(R.id.list_view_kontakty);

        addCursorAdapter();

        optBtn.setOnLongClickListener(v -> {
            goToEditKontakty(v);
            return true;
        });
    }

    public void goToEditKontakty(View view) {
        Intent intent = new Intent(getApplicationContext(), EditKontaktyActivity.class);
        startActivity(intent);
    }

    public void goBack(View view) {
        finish();
    }

    private void addCursorAdapter() {
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_kontakty_layout, dbWorker.getCursorKontakty(),
                new String[]{MyContract.Kontakty.COLUMN_MENO, MyContract.Kontakty.COLUMN_CISLO},
                new int[]{R.id.textView,R.id.textView2}, 0);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor c = ((SimpleCursorAdapter) listView.getAdapter()).getCursor();
            c.moveToPosition(position);

            ArrayList cislo = dbWorker.getKontaktCislo();

            CustomDialog cdd = new CustomDialog(KontaktyActivity.this, (String) cislo.get(position));

            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

            cdd.getWindow().setLayout(width, height);
            cdd.show();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            final CharSequence[] items = {"Ano, chcem zmazat kontat", "Nie, ponechat kontakt"};

            AlertDialog.Builder dialog = new AlertDialog.Builder(KontaktyActivity.this);

            dialog.setTitle("Chcete zmazat kontakt ?");
            dialog.setItems(items, (dialog1, i) -> {
                if (i == 0) {
                    Cursor c = ((SimpleCursorAdapter) listView.getAdapter()).getCursor();
                    c.moveToPosition(position);
                    long id1 = c.getLong(c.getColumnIndex(MyContract.Kontakty.COLUMN_ID));
                    dbWorker.delKontakty(id1);
                    addCursorAdapter();
                }
            });
            dialog.show();
            addCursorAdapter();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addCursorAdapter();
    }
}

