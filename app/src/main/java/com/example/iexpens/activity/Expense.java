package com.example.iexpens.activity;

import android.app.DatePickerDialog;

public class Expense {
    String expenseId;
    String expenseCategory;
    String price;
    String date;
    String description;
    String imageAddress;

    public Expense() {
    }



    public Expense(String expenseId, String expenseCategory, String price, String date, String description,String imageAddress) {
        this.expenseId = expenseId;
        this.expenseCategory = expenseCategory;
        this.price = price;
        this.date = date;
        this.description = description;
        this.imageAddress=imageAddress;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
