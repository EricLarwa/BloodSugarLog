package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseHelper dbHelper;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.tbl_weekly_data);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        Button addEntryButton = findViewById(R.id.btn_add_entry);
        addEntryButton.setOnClickListener(v -> showPopupMenu());

        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "No user logged in. Redirecting to login screen.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        populateTable();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            Intent intent = new Intent(this, ProfileDisplayActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.btn_add_entry));
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_blood_sugar) {
                startActivity(new Intent(this, BloodSugarActivity.class));
            } else if (item.getItemId() == R.id.menu_insulin) {
                startActivity(new Intent(this, InsulinActivity.class));
            }
            return true;
        });
        popupMenu.show();
    }

    private void populateTable() {
        tableLayout.removeViews(1, Math.max(0, tableLayout.getChildCount() - 1));

        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);

        if (currentUserId != -1) {
            List<Record> records = dbHelper.getRecordsForUser(currentUserId);
            for (Record record : records) {
                TableRow row = new TableRow(this);

                TextView dateTextView = new TextView(this);
                dateTextView.setText(record.getDate());
                dateTextView.setPadding(8, 8, 8, 8);

                TextView mealTimeTextView = new TextView(this);
                mealTimeTextView.setText(record.getMealTime());
                mealTimeTextView.setPadding(8, 8, 8, 8);

                TextView sugarLevelTextView = new TextView(this);
                sugarLevelTextView.setText(String.valueOf(record.getSugarLevel()));
                sugarLevelTextView.setPadding(8, 8, 8, 8);

                TextView insulinAmountTextView = new TextView(this);
                insulinAmountTextView.setText(String.valueOf(record.getInsulinAmount()));
                insulinAmountTextView.setPadding(8, 8, 8, 8);

                row.addView(dateTextView);
                row.addView(mealTimeTextView);
                row.addView(sugarLevelTextView);
                row.addView(insulinAmountTextView);

                tableLayout.addView(row);
            }
        } else {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTable();
    }
}
