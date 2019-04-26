package com.example.iexpens.activity;

public class Expense {
    String expenseId;
    String expenseCategory;
    String price;
    String date;
    String description;

    public Expense() {
    }

    public Expense(String expenseId, String expenseCategory, String price, String date, String description) {
        this.expenseId = expenseId;
        this.expenseCategory = expenseCategory;
        this.price = price;
        this.date = date;
        this.description = description;
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
}
