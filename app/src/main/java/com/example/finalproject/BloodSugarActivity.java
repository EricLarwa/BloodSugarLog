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

public class BloodSugarActivity extends AppCompatActivity {

    private Spinner spinnerMealTime;
    private EditText etBloodSugar;
    private Button btnSaveBloodSugar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar);

        // Initialize UI components
        spinnerMealTime = findViewById(R.id.spinner_meal_time);
        etBloodSugar = findViewById(R.id.et_blood_sugar);
        btnSaveBloodSugar = findViewById(R.id.btn_save_blood_sugar);

        dbHelper = new DatabaseHelper(this);

        btnSaveBloodSugar.setOnClickListener(v -> saveBloodSugarData());
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("current_user_id", -1);
    }

    private void saveBloodSugarData() {
        // Get data from UI
        String mealTime = spinnerMealTime.getSelectedItem().toString();
        String bloodSugarStr = etBloodSugar.getText().toString();

        // Validate input
        if (bloodSugarStr.isEmpty()) {
            etBloodSugar.setError("Please enter blood sugar level");
            return;
        }

        int bloodSugar = Integer.parseInt(bloodSugarStr);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        int currentUserId = getCurrentUserId();
        if (currentUserId == -1) {
            Toast.makeText(this, "No user logged in. Cannot save data.", Toast.LENGTH_SHORT).show();
            return;
        }
        dbHelper.insertRecord(currentDate, mealTime, bloodSugar, 0, currentUserId);

        // Show success message and finish
        Toast.makeText(this, "Blood sugar data saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}

