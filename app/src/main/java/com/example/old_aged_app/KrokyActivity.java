package com.example.old_aged_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class KrokyActivity extends AppCompatActivity {

    private int TARGET_STEPS;
    private static final int MAX_X_VALUE = 7;
    private static final String SET_LABEL = "Kroky";
    private static final String[] DAYS = {getCalculatedDate("EEEE", -6),
            getCalculatedDate("EEEE", -5),
            getCalculatedDate("EEEE", -4),
            getCalculatedDate("EEEE", -3),
            getCalculatedDate("EEEE", -2),
            getCalculatedDate("EEEE", -1),
            getCalculatedDate("EEEE", 0)};
    private final DBWorker dbWorker = new DBWorker(this);
    private String[] STEPS;
    private Button optionBtn;
    private BarChart chart;
    private SharedPreferences sharedPreferences;
    public static KrokyActivity instance = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kroky);
        instance = this;

        STEPS = new String[]{((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -6)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -6)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -5)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -5)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -4)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -4)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -3)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -3)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -2)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -2)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -1)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -1)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", 0)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", 0)).getKroky())};

        optionBtn = findViewById(R.id.optionBtn);
        chart = findViewById(R.id.fragment_verticalbarchart_chart);

        sharedPreferences = getSharedPreferences("TARGET_STEPS", MODE_PRIVATE);
        TARGET_STEPS = sharedPreferences.getInt("goal", 5000);
        int maxCapacity = TARGET_STEPS;
        LimitLine ll = new LimitLine(maxCapacity, "Cieľ");
        chart.getAxisLeft().addLimitLine(ll);
        ll.setLineWidth(4f);
        ll.setTextSize(16f);
        ll.setLineColor(Color.parseColor("#000000"));

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);
        data.setValueFormatter(new MyValueFormatter());

        optionBtn.setOnLongClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EditKrokyActivity.class);
            startActivity(intent);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        STEPS = new String[]{((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -6)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -6)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -5)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -5)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -4)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -4)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -3)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -3)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -2)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -2)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", -1)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", -1)).getKroky()),
                ((dbWorker.getKroky(getMyDate("dd-MM-yyyy", 0)).getKroky() == null) ? String.valueOf(0) : dbWorker.getKroky(getMyDate("dd-MM-yyyy", 0)).getKroky())};
        optionBtn = findViewById(R.id.optionBtn);
        chart = findViewById(R.id.fragment_verticalbarchart_chart);

        sharedPreferences = getSharedPreferences("TARGET_STEPS", MODE_PRIVATE);
        TARGET_STEPS = sharedPreferences.getInt("goal", 5000);
        int maxCapacity = TARGET_STEPS;
        LimitLine ll = new LimitLine(maxCapacity, "Cieľ");
        chart.getAxisLeft().addLimitLine(ll);
        ll.setLineWidth(4f);
        ll.setTextSize(16f);
        ll.setLineColor(Color.parseColor("#000000"));

        BarData data = createChartData();
        data.setValueFormatter(new MyValueFormatter());
        configureChartAppearance();
        prepareChartData(data);
    }

    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawValueAboveBar(false);
        chart.setTouchEnabled(false);
        chart.setExtraBottomOffset(20);
        chart.setExtraTopOffset(5);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#000000"));
        xAxis.setTextSize(20);
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisLineColor(Color.parseColor("#FF0000"));
        xAxis.setGridColor(Color.parseColor("#FF0000"));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawGridLinesBehindData(false);
        xAxis.getYOffset();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setTextColor(Color.parseColor("#BADA55"));
        axisLeft.setAxisMinimum(0f);
        axisLeft.setDrawGridLines(false);
        axisLeft.setTextSize(0);
        axisLeft.setDrawLabels(false);
        axisLeft.setDrawAxisLine(false);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setTextColor(Color.parseColor("#FFFFFF"));
        axisRight.setAxisMinimum(0f);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawGridLinesBehindData(false);
        axisRight.setDrawAxisLine(false);
        axisRight.setTextSize(0);
        axisRight.setDrawLabels(false);
    }

    private BarData createChartData() {
        YAxis axisLeft = chart.getAxisLeft();
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            int y = Integer.parseInt(STEPS[i]);
            values.add(new BarEntry(i, y));
            if (TARGET_STEPS < y) {
                axisLeft.setAxisMaximum(y + y/10);
            } else {
                axisLeft.setAxisMaximum(TARGET_STEPS + TARGET_STEPS/10);
            }
        }

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);
        set1.setValueFormatter(new MyValueFormatter());
        set1.setBarBorderWidth(1);
        set1.setValueTextColor(Color.parseColor("#FF0000"));
        set1.setColor(Color.parseColor("#BADA55"));
        set1.setValueTextColor(Color.parseColor("#000000"));

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextColor(Color.parseColor("#000000"));
        data.setBarWidth(1f);
        data.setValueFormatter(new MyValueFormatter());

        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(16);
        data.setValueFormatter(new MyValueFormatter());
        chart.setData(data);
        chart.invalidate();
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);

        switch (s.format(new Date(cal.getTimeInMillis()))) {
            case "pondelok":
            case "Monday":
                return "Po";
            case "utorok":
            case "Tuesday":
                return "Ut";
            case "streda":
            case "Wednesday":
                return "St";
            case "štvrtok":
            case "Thursday":
                return "Št";
            case "piatok":
            case "Friday":
                return "Pi";
            case "sobota":
            case "Saturday":
                return "So";
            case "nedeľa":
            case "Sunday":
                return "Ne";
            default:
                return "ERR";
        }
    }

    public static String getMyDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DATE, days);
        try {
            return s.format(new Date(cal.getTimeInMillis()));
        } catch (Exception e) {
            System.out.println("error " + e);
            return String.valueOf(0);
        }
    }

    public void goBack(View view) {
        finish();
    }
}