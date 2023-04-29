package com.boydspalding.loosechange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    public static final int SETTINGS_REQUEST = 3524;

    List<String> main;
    EditText day;
    EditText month;
    EditText year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        main = MainActivity.masterDataList;
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
    }

    public void goBack(View view) {
        Toast.makeText(this, "Sorry still under construction", Toast.LENGTH_SHORT).show();
        finish();
    }
}