package com.example.old_aged_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LiekyActivity extends AppCompatActivity {

    ListView listView;
    SimpleCursorAdapter cursorAdapter;
    final DBWorker dbWorker = new DBWorker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lieky);
        Button optBtn = findViewById(R.id.optionBtn);
        listView = findViewById(R.id.list_view_lieky);

        addCursorAdapter();

        optBtn.setOnLongClickListener(v -> {
            goToEditLieky(v);
            return true;
        });
    }

    public void goToEditLieky(View view) {
        Intent intent = new Intent(getApplicationContext(), EditLiekyActivity.class);
        startActivity(intent);
    }

    public void goBack(View view) {
        finish();
    }

    private void addCursorAdapter() {
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_lieky_layout, dbWorker.getCursorLieky(),
                new String[]{MyContract.Lieky.COLUMN_NAZOV, MyContract.Lieky.COLUMN_CAS, MyContract.Lieky.COLUMN_INFO},
                new int[]{R.id.textView, R.id.textView2, R.id.textView3}, 0);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            final CharSequence[] items = {"Ano, chcem zmazat pripomienku", "Nie, ponechat pripomienku"};

            AlertDialog.Builder dialog = new AlertDialog.Builder(LiekyActivity.this);

            dialog.setTitle("Chcete zmazat pripomienku ?");
            dialog.setItems(items, (dialog1, i) -> {
                if (i == 0) {
                    Cursor c = ((SimpleCursorAdapter) listView.getAdapter()).getCursor();
                    c.moveToPosition(position);
                    long id1 = c.getLong(c.getColumnIndex(MyContract.Lieky.COLUMN_ID));
                    String req = c.getString(c.getColumnIndex(MyContract.Lieky.COLUMN_CAS));
                    Log.e("TEST REQCODE", req);

                    req = req.replace(" : ", "");
                    Log.e("TEST REQCODE FINAL", req);

                    delAlarm(Integer.parseInt(req));

                    dbWorker.delLieky(id1);
                    addCursorAdapter();
                }
            });
            dialog.show();
            addCursorAdapter();
            return true;
        });
    }

    public void delAlarm(int req) {
        Intent intent = new Intent(LiekyActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(LiekyActivity.this, req, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addCursorAdapter();
    }
}