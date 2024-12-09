package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this);

        EditText usernameInput = findViewById(R.id.et_username);
        EditText passwordInput = findViewById(R.id.et_password);
        EditText ageInput = findViewById(R.id.et_age);
        EditText birthdayInput = findViewById(R.id.et_birthday);
        RadioGroup genderGroup = findViewById(R.id.rg_gender);
        RadioGroup diabetesTypeGroup = findViewById(R.id.rg_diabetes_type);

        Button registerButton = findViewById(R.id.btn_register);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        registerButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String birthday = birthdayInput.getText().toString().trim();
            int age;

            try {
                age = Integer.parseInt(ageInput.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            int selectedDiabetesTypeId = diabetesTypeGroup.getCheckedRadioButtonId();

            if (username.isEmpty() || password.isEmpty() || birthday.isEmpty() || selectedGenderId == -1 || selectedDiabetesTypeId == -1) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                String gender = ((RadioButton) findViewById(selectedGenderId)).getText().toString();
                String diabetesType = ((RadioButton) findViewById(selectedDiabetesTypeId)).getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("current_username", username);
                editor.putString(username + "_age", String.valueOf(age));
                editor.putString(username + "_gender", gender);
                editor.putString(username + "_birthday", birthday);
                editor.putString(username + "_type", diabetesType);
                editor.apply();

                Log.d("sharedPreferences", "saved username: " + username);
                Log.d("Registration", "Saved to sharedPreferences...");
            }

            String gender = ((RadioButton) findViewById(selectedGenderId)).getText().toString();
            String diabetesType = ((RadioButton) findViewById(selectedDiabetesTypeId)).getText().toString();
            if (dbHelper.validateUser(username, password)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
            long userId = dbHelper.registerUser(username, password, age, gender, birthday, diabetesType);
            if (userId == -1) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("current_username", username);
                startActivity(intent);
                finish();
            }

        });

    }
}
