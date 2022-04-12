package com.example.old_aged_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditKontaktyActivity extends AppCompatActivity {

    EditText meno;
    EditText cislo;
    Kontakty kontakt;
    Button saveKontaktBtn;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kontakty);

        meno = findViewById(R.id.meno);
        cislo = findViewById(R.id.cislo);
        saveKontaktBtn = findViewById(R.id.savekontakt);


        Intent intent = getIntent();
        id = intent.getIntExtra("id", 10);

        saveKontaktBtn.setOnLongClickListener(v -> {
            boolean possible = false;

            if(meno.getText().toString().matches("") || cislo.getText().toString().matches("") || !cislo.getText().toString().startsWith("09") ) {
//                final CharSequence[] items = {"Ok"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditKontaktyActivity.this);


                dialog.setTitle("Niečo je zle");
                dialog.setNeutralButton("Ok",null);
                dialog.setMessage("Meno a číslo sú povinné!\nČíslo musí byť v tvare:\n0905123123");
//                dialog.setItems(items, (dialog1, i) -> {
//                    if (i == 0) {
//                    }
//                });
                dialog.show();
            }else {
                possible = true;
                saveKontakt(v,possible);
            }
            return possible;
        });

    }

    public void saveKontakt(View view,boolean pos) {
        if(pos){
            kontakt = new Kontakty(id, meno.getText().toString(), cislo.getText().toString());
            DBWorker dbWorker = new DBWorker(this);
            dbWorker.addKontakt(kontakt);
            goBack(view);
        }
    }

    public void goBack(View view) {
        finish();
    }
}