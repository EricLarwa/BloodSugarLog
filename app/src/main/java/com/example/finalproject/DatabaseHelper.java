package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HealthLog.db";
    public static final int DATABASE_VERSION = 3;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Users (UserId INTEGER PRIMARY KEY, Username TEXT UNIQUE, Password TEXT, Age INTEGER, Gender TEXT, Birthday TEXT, DiabetesType TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Records (RecordId INTEGER PRIMARY KEY, Date TEXT, MealTime TEXT, SugarLevel INTEGER, InsulinAmount REAL, UserId INTEGER)");

    }

    public long registerUser(String username, String password, int age, String gender, String birthday, String diabetesType) {
        if (isUsernameTaken(username)) {
            return -1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("Password", password);
        values.put("Age", age);
        values.put("Gender", gender);
        values.put("Birthday", birthday);
        values.put("DiabetesType", diabetesType);

        Log.d("DatabaseHelper", "Saving to SharedPreferences: " + username);

        long UserId = db.insert("Users", null, values);

        SharedPreferences sharedPreferences = this.context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username + "_username", username);
        editor.putString(username + "_age", String.valueOf(age));
        editor.putString(username + "_gender", gender);
        editor.putString(username + "_birthday", birthday);
        editor.putString(username + "_type", diabetesType);
        editor.putInt("current_user_id", (int) UserId);
        editor.apply();

        Log.d("DatabaseHelper", "UserId: " + UserId);

        return UserId;
    }


    private boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users", new String[]{"Username"}, "Username = ?", new String[]{username}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Users WHERE Username = ? AND Password = ?",
                new String[]{username, password}
        );
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Records");
        onCreate(db);
    }

    public void insertRecord(String date, String mealTime, int sugarLevel, float insulinAmount, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Date", date);
        values.put("MealTime", mealTime);
        values.put("SugarLevel", sugarLevel);
        values.put("InsulinAmount", insulinAmount);
        values.put("UserId", userId);
        db.insert("Records", null, values);
        db.close();
    }

    public List<Record> getRecordsForUser(int UserId) {
        List<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Records WHERE UserId = ?", new String[]{String.valueOf(UserId)});

        if (cursor.moveToFirst()) {
            do {
                records.add(new Record(
                        cursor.getString(cursor.getColumnIndexOrThrow("Date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MealTime")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("SugarLevel")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("InsulinAmount"))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }
}
