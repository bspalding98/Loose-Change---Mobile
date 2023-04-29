package com.boydspalding.loosechange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText value;
    private TextView weeklyAverageValue;
    private TextView projectedValue;
    private Button record;
    private ImageView settings;
    private ImageView pieChart;
    private CalendarView calendarView;
    private int position;

    public static List<String> masterDataList;
    private List<String> foodList;
    private List<String> fuelList;
    private List<String> billsList;
    private List<String> extrasList;

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoCompleteTextView;

    private String selectedDate;
    private int days;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseVariables();



        // Quick fix for no cate selection:
        position = 5;

        createDropBox();


        // Read data initially:
        readData();
        calculateDisplays();

        //TODO READ BELOW:
        //Calendar was broken, so had to just pre set it :/ Everytime
        // I restarted app it went up a month using built in java Calendar
        // listener is also off by a month as well.
        // Actually ruined everything, settings, so much here. Tell Java they suck.
        System.out.println(selectedDate);
        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            selectedDate = i2 + "/" + (i1+1) + "/" +i;
            System.out.println(selectedDate);
        });

        //Reset data CAUTION: Leave this commented unless you want to reset your data.
        // Did not have enough time to implement a button for it.
//        resetData();
//        storeData();
    }




    public void recordSavings(View view){
        if(value.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a value to record", Toast.LENGTH_SHORT).show();
        } else if(position == 5) {
            Toast.makeText(this, "Please select a Category", Toast.LENGTH_SHORT).show();
        } else{
            switch (position) {
                case 0 :
                    foodList.add(value.getText().toString());
                    break;
                case 1 :
                    fuelList.add(value.getText().toString());
                    break;
                case 2 :
                    billsList.add(value.getText().toString());
                    break;
                case 3 :
                    extrasList.add(value.getText().toString());
                    break;
            }
            System.out.println("Food list: " + Arrays.asList(foodList));
            System.out.println("Fuel List: " + Arrays.asList(fuelList));
            System.out.println("Bills List: " + Arrays.asList(billsList));
            System.out.println("Extras List: " + Arrays.asList(extrasList));

            storeData();
            DataController.writeListInPref(getApplicationContext(), masterDataList);
            calculateDisplays();
            value.setText("");
        }
    }

    public void readData(){
        masterDataList = DataController.readListFromPref(this);
        if(masterDataList == null) {
            masterDataList = new ArrayList<>();
        } else {
            foodList.add(masterDataList.get(1));
            fuelList.add(masterDataList.get(2));
            billsList.add(masterDataList.get(3));
            extrasList.add(masterDataList.get(4));
        }
        System.out.println("data retrieved: " + Arrays.asList(masterDataList));
    }

    public void storeData() {
        if(masterDataList.isEmpty()) {
            masterDataList.add("16/03/2022");
            masterDataList.add(listTotals(foodList));
            masterDataList.add(listTotals(fuelList));
            masterDataList.add(listTotals(billsList));
            masterDataList.add(listTotals(extrasList));
            masterDataList.add("no Date");
        } else {
            masterDataList.set(1, listTotals(foodList));
            masterDataList.set(2, listTotals(fuelList));
            masterDataList.set(3, listTotals(billsList));
            masterDataList.set(4, listTotals(extrasList));
            masterDataList.set(5, selectedDate);
        }
        System.out.println(selectedDate);
        System.out.println("data stored: " + Arrays.asList(masterDataList));
    }

    public void storeData(List<String> main) {
        masterDataList.add(main.get(0));
        System.out.println("data stored: " + Arrays.asList(masterDataList));
    }

    public String listTotals(List<String> list) {
        int total = 0;
        for (String d : list) {
            total += Integer.parseInt(d);
        }
        return String.valueOf(total);
    }


    private void calculateDisplays() {

        calculateDays();

        double total = Integer.parseInt(masterDataList.get(1)) + Integer.parseInt(masterDataList.get(2)) +
                Integer.parseInt(masterDataList.get(3)) + Integer.parseInt(masterDataList.get(4));
        double weeks = (double) days/7;
        System.out.println("w" + weeks);
        System.out.println("b" + total);
        total = total/weeks;
        System.out.println("a" + total);
        weeklyAverageValue.setText(String.format("$ %.02f", total));
        projectedValue.setText(String.format("$ %.02f", total*52));
    }

    private void calculateDays() {
        String[] firstDate = masterDataList.get(0).split("/");
        int firstDay = Integer.parseInt(firstDate[0]);
        int firstMonth = Integer.parseInt(firstDate[1]);
        System.out.println(firstDay);
        System.out.println(firstMonth);



        String[] secondDate = masterDataList.get(5).split("/");
        int secondDay = Integer.parseInt(secondDate[0]);
        int secondMonth = Integer.parseInt(secondDate[1]);
        System.out.println(secondDay);
        System.out.println(secondMonth);

        int monthDifference = secondMonth - firstMonth;
        if(monthDifference == 0) {
            days = secondDay - firstDay;
        } else {
            for(int i = 0; i < monthDifference; i++) {
                int month = (i+1) + firstMonth;
                if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    days += 31;
                } else if(month == 2) {
                    days += 28;
                } else {
                    days += 30;
                }
                days = secondDay - firstDay;
            }
        }
    }


    public void viewPieChart(View view) {
        Intent intent = new Intent(this, PieChart.class);
        startActivityForResult(intent, PieChart.PIE_CHART_REQUEST);
    }

    public void settingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, SettingsActivity.SETTINGS_REQUEST);
    }

    private void resetData() {
        masterDataList.clear();
        foodList.clear();
        fuelList.clear();
        billsList.clear();
        extrasList.clear();
    }

    private void createDropBox() {
        String[] items={"Food", "Fuel", "Bills Excess", "Extras"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, R.layout.items_list, items);
        autoCompleteTextView.setAdapter(itemAdapter);
        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            position = i;
            System.out.println(position);
        });
    }

    private void initialiseVariables() {
        // assigning variables:
        value = findViewById(R.id.value);
        weeklyAverageValue = findViewById(R.id.weeklyAverageValue);
        projectedValue = findViewById(R.id.projectedValue);
        record = findViewById(R.id.record);
        settings = findViewById(R.id.settings);
        pieChart = findViewById(R.id.pieChart);
        textInputLayout = findViewById(R.id.menu);
        autoCompleteTextView = findViewById(R.id.drop_items);
        calendarView = findViewById(R.id.calendarView);
        foodList = new ArrayList<>();
        fuelList = new ArrayList<>();
        billsList = new ArrayList<>();
        extrasList = new ArrayList<>();
    }
}