package com.example.old_aged_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditKrokyActivity extends AppCompatActivity {

    TextView ciel;
    Button save;
    int test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kroky);

        ciel = findViewById(R.id.ciel);
        save = findViewById(R.id.saveciel);


        save.setOnLongClickListener(v -> {
            boolean possible = false;

            if(ciel.getText().toString().matches("")) {
                final CharSequence[] items = {"Ok"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditKrokyActivity.this);

                dialog.setTitle("Niečo je zle\nCieľový počet krokov je povinný!");
                dialog.setItems(items, (dialog1, i) -> {
                    if (i == 0) {
                    }
                });
                dialog.show();
            }else {
                possible = true;
                Intent intent = new Intent(EditKrokyActivity.this, MainActivity.class);
                saveCiel(v,possible);
                KrokyActivity.instance.finish();
                startActivity(intent);
            }
            return possible;
        });

//        save.setOnClickListener(v -> {
//            Intent intent = new Intent(EditKrokyActivity.this, MainActivity.class);
//            saveCiel(v);
//            KrokyActivity.instance.finish();
//            startActivity(intent);
//        });



    }

    public void saveCiel(View view,boolean pos) {
        if(pos){
            SharedPreferences sharedPreferences = getSharedPreferences("TARGET_STEPS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("goal", Integer.parseInt(ciel.getText().toString()));
            editor.commit();
            editor.apply();
        }
    }

    public void goBack(View view) {
        finish();
    }
}