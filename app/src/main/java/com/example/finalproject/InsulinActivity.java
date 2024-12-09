package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InsulinActivity extends AppCompatActivity {

    private Spinner spinnerMealTime;
    private EditText etInsulin;
    private Button btnSaveInsulin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulin);

        // Initialize UI components
        spinnerMealTime = findViewById(R.id.spinner_meal_time);
        etInsulin = findViewById(R.id.et_insulin);
        btnSaveInsulin = findViewById(R.id.btn_save_insulin);

        dbHelper = new DatabaseHelper(this);

        btnSaveInsulin.setOnClickListener(v -> saveInsulinData());
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("current_user_id", -1);  // Default to -1 if no user is logged in
    }


    private void saveInsulinData() {
        // Get data from UI
        String mealTime = spinnerMealTime.getSelectedItem().toString();
        String insulinAmountStr = etInsulin.getText().toString();


        if (insulinAmountStr.isEmpty()) {
            etInsulin.setError("Please enter insulin amount");
            return;
        }

        float insulinAmount = Float.parseFloat(insulinAmountStr);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        int currentUserId = getCurrentUserId();
        if (currentUserId == -1) {
            Toast.makeText(this, "No user logged in. Cannot save data.", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.insertRecord(currentDate, mealTime, 0, insulinAmount, currentUserId);

        Toast.makeText(this, "Insulin data saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}

