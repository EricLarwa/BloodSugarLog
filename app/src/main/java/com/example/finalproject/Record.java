package com.example.finalproject;

public class Record {
    private String date;
    private String mealTime;
    private int sugarLevel;
    private float insulinAmount;

    public Record(String date, String mealTime, int sugarLevel, float insulinAmount) {
        this.date = date;
        this.mealTime = mealTime;
        this.sugarLevel = sugarLevel;
        this.insulinAmount = insulinAmount;
    }

    public String getDate() {
        return date;
    }

    public String getMealTime() {
        return mealTime;
    }

    public int getSugarLevel() {
        return sugarLevel;
    }

    public float getInsulinAmount() {
        return insulinAmount;
    }
}

