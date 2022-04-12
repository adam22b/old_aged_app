package com.example.old_aged_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditLiekyActivity extends AppCompatActivity {

    TimePickerDialog picker;
    final Calendar cldr = Calendar.getInstance();
    int hour = cldr.get(Calendar.HOUR_OF_DAY);
    int minutes = cldr.get(Calendar.MINUTE);
    EditText nazov;
    EditText cas;
    EditText info;
    Button save;
    int id;
    Lieky liek;
    int reqCode;
    int h, m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lieky);

        nazov = findViewById(R.id.nazovLieku);
        cas = findViewById(R.id.cas);
        info = findViewById(R.id.info);
        save = findViewById(R.id.saveliek);

        cas.setOnClickListener(v -> {
            picker = new TimePickerDialog(EditLiekyActivity.this, R.style.TimePickerTheme,
                    (view, hourOfDay, minute) -> {

                        if (minute > -1 && minute < 10) {
                            cas.setText(hourOfDay + " : 0" + minute);
                            reqCode = Integer.parseInt((hourOfDay + "0" + minute));
                            h = hourOfDay;
                            m = minute;
                        } else {
                            cas.setText(hourOfDay + " : " + minute);
                            reqCode = Integer.parseInt((hourOfDay + "" + minute));
                            h = hourOfDay;
                            m = minute;
                        }

                    }, hour, minutes, true);
            picker.show();
        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 10);

        save.setOnLongClickListener(v -> {
            boolean possible = false;

            if(nazov.getText().toString().matches("") || cas.getText().toString().matches("")) {
//                final CharSequence[] items = {"Ok"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditLiekyActivity.this);

                dialog.setTitle("Niečo je zle");
                dialog.setNeutralButton("Ok",null);
                dialog.setMessage("Názov lieku a čas\npripomenutia sú povinné!");
//                dialog.setItems(items, (dialog1, i) -> {
//                    if (i == 0) {
//                    }
//                });
                dialog.show();
            }else {
                possible = true;
                saveLiek(v,possible);
            }
            return possible;
        });

    }

    public void saveLiek(View view,boolean pos) {

        if(pos){
            Log.e("TEST", String.valueOf(pos));
            liek = new Lieky(id, nazov.getText().toString(), cas.getText().toString(), info.getText().toString());
            DBWorker dbWorker = new DBWorker(this);
            dbWorker.addLieky(liek);

            setAlarm(view, h, m, reqCode);
            goBack(view);
        }
    }

    public void setAlarm(View view, int alarmH, int alarmM, int reqCOde) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date date = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(date);
        cal_alarm.setTime(date);
        cal_alarm.set(Calendar.HOUR_OF_DAY, alarmH);
        cal_alarm.set(Calendar.MINUTE, alarmM);
        cal_alarm.set(Calendar.SECOND, 0);
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }
        Intent intent = new Intent(EditLiekyActivity.this, Receiver.class);
        intent.putExtra("nameMed", nazov.getText().toString());
        intent.putExtra("infoMed", info.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EditLiekyActivity.this, reqCOde, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void goBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}