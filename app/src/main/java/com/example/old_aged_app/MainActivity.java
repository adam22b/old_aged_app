package com.example.old_aged_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.old_aged_app.KrokyActivity.getMyDate;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Button krokyAkt;
    int counter;
    Button hra;
    Button kontakty;
    Button kroky;
    Button lieky;

    private static final int ALL_REQUEST = 103;
    SharedPreferences sharedPreferences;

    boolean activityRunning = false;
    private int TARGET_STEPS;
    final DBWorker dbWorker = new DBWorker(this);

    private Sensor stepCounterSensor;
    private Sensor stepDetectorSensor;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:com.example.old_aged_app"));
                context.startActivity(intent);
            }
        }

        DBWorker dbWorker = new DBWorker(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.CALL_PHONE}, ALL_REQUEST);
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 1000);

        sharedPreferences = getSharedPreferences("TARGET_STEPS", MODE_PRIVATE);
        TARGET_STEPS = sharedPreferences.getInt("goal", 5000);
        krokyAkt = findViewById(R.id.krokyAktualBtn);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);

        krokyAkt.setText("Prešiel si:\n" + counter + " / " + TARGET_STEPS + "");

        hra = findViewById(R.id.hraBtn);
        kontakty = findViewById(R.id.volanieBtn);
        kroky = findViewById(R.id.krokyCelkBtn);
        lieky = findViewById(R.id.liekyBtn);

        hra.setOnClickListener(v -> {
            try {
                Intent i = getApplicationContext().getPackageManager().getLaunchIntentForPackage("ee.dustland.android.dustlandsudoku");
                getApplicationContext().startActivity(i);
            } catch (Exception e) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=ee.dustland.android.dustlandsudoku")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=ee.dustland.android.dustlandsudoku")));
                }
            }
        });

        kroky.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KrokyActivity.class);
            startActivity(intent);
        });

        kontakty.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KontaktyActivity.class);
            startActivity(intent);
        });

        lieky.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LiekyActivity.class);
            startActivity(intent);
        });

        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat s1 = new SimpleDateFormat("dd-MM-yyyy");
        cal1.add(Calendar.DATE, 0);
        Kroky k = dbWorker.getKroky(s1.format(new Date(cal1.getTimeInMillis())));
        if (k.getID() < 1) {
            counter = 0;
        } else {
            counter = Integer.parseInt(k.getKroky());
        }
        if (TARGET_STEPS <= counter) {
            krokyAkt.setBackgroundColor(Color.parseColor("#78f400"));
        } else {
            krokyAkt.setBackgroundColor(Color.parseColor("#81D4FA"));
        }
        checkFirstRun();
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {

            new AlertDialog.Builder(this).setTitle("Aplikáciu je potrebné nastaviť").setMessage("Pre nastavenie treba podržat zelené tlačidlo \"Zmeniť\" ktoré sa objaví po kliknutí na jednu zo 4 položie: \"Prehľad krokov\", \"Lieky\", \"Volanie rodine\", \"Hra\" ").setNeutralButton("OK", null).show();


            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat s1 = new SimpleDateFormat("dd-MM-yyyy");
        cal1.add(Calendar.DATE, 0);

        TARGET_STEPS = sharedPreferences.getInt("goal", 5000);
        krokyAkt.setText("Prešiel si:\n" + counter + " / " + TARGET_STEPS + "");
        activityRunning = true;
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        Kroky k = dbWorker.getKroky(s1.format(new Date(cal1.getTimeInMillis())));
        dbWorker.updateKroky(k);

        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        if (TARGET_STEPS <= counter) {
            krokyAkt.setBackgroundColor(Color.parseColor("#78f400"));
        } else {
            krokyAkt.setBackgroundColor(Color.parseColor("#81D4FA"));
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat s1 = new SimpleDateFormat("dd-MM-yyyy");
        cal1.add(Calendar.DATE, 0);
        String datum = s1.format(new Date(cal1.getTimeInMillis()));

        if (activityRunning) {
            counter = (int) event.values[0];
            krokyAkt.setText("Prešiel si:\n" + counter + " / " + TARGET_STEPS + "");

        }
        if (dbWorker.getKroky(getMyDate("dd-MM-yyyy", 0)).getID() < 1) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            counter = 0;
            Kroky kroky = new Kroky(1, datum, String.valueOf(counter));
            dbWorker.addKroky(kroky);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            Kroky kroky = new Kroky(1, datum, String.valueOf(counter));
            dbWorker.updateKroky(kroky);
        }
        if (TARGET_STEPS <= counter) {
            krokyAkt.setBackgroundColor(Color.parseColor("#78f400"));
        } else {
            krokyAkt.setBackgroundColor(Color.parseColor("#81D4FA"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}