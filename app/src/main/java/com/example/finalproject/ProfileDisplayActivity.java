package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileDisplayActivity extends AppCompatActivity {

    private TextView tvUsername, tvAge, tvGender, tvBirthday, tvDiabetesType;
    private Button SignOut;
    private SharedPreferences sharedPreferences;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);


        tvUsername = findViewById(R.id.tv_username);
        tvAge = findViewById(R.id.tv_age);
        tvGender = findViewById(R.id.tv_gender);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvDiabetesType = findViewById(R.id.tv_diabetes_type);
        SignOut = findViewById(R.id.sign_out);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);


        username = getIntent().getStringExtra("current_username");
        if (username == null) {
            username = sharedPreferences.getString("current_username", null);
        }

        if (username != null) {
            displayUserProfile(username);
        } else {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
        }

        Button btnMainMenu = findViewById(R.id.btn_main_menu);
        btnMainMenu.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileDisplayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        SignOut.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileDisplayActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void displayUserProfile(String username) {
        String age = sharedPreferences.getString(username + "_age", "N/A");
        String birthday = sharedPreferences.getString(username + "_birthday", "N/A");
        String gender = sharedPreferences.getString(username + "_gender", "N/A");
        String diabetesType = sharedPreferences.getString(username + "_type", "N/A");

        // Log for debugging
        Log.d("ProfileDisplayActivity", "Age: " + age);
        Log.d("ProfileDisplayActivity", "Birthday: " + birthday);
        Log.d("ProfileDisplayActivity", "Gender: " + gender);
        Log.d("ProfileDisplayActivity", "Diabetes Type: " + diabetesType);

        tvUsername.setText("Username: " + username);
        tvAge.setText("Age: " + age);
        tvBirthday.setText("Birthday: " + birthday);
        tvGender.setText("Gender: " + gender);
        tvDiabetesType.setText("Diabetes Type: " + diabetesType);
    }
}
