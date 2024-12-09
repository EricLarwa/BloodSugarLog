package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton, registerButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.et_username);
        passwordInput = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);
        registerButton = findViewById(R.id.btn_register);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                boolean isValid = dbHelper.validateUser(username, password);
                if (isValid) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("current_username", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();

            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
    
}

