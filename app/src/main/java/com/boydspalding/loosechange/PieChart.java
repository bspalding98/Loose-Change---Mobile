package com.boydspalding.loosechange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends AppCompatActivity {

    public static final int PIE_CHART_REQUEST = 1234;

    AnyChartView anyChartView;
    List<String> main;
    String[] categories = {"Food", "Fuel", "Bills Excess", "Extras"};
    int[] savings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        main = MainActivity.masterDataList;
        savings = new int[]{Integer.parseInt(main.get(1)), Integer.parseInt(main.get(2)),
                Integer.parseInt(main.get(3)), Integer.parseInt(main.get(4))};
        anyChartView = findViewById(R.id.pieChart);
        setupPieChart();

    }

    public void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for(int i = 0; i < categories.length; i++) {
            dataEntries.add(new ValueDataEntry(categories[i], savings[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

    public void goBack() {
        finish();
    }
}