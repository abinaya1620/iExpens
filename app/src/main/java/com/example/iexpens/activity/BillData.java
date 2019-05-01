package com.example.iexpens.activity;

import java.io.Serializable;

public class BillData implements Serializable {

    String strBillName;
    String strAccountName;
    String strAmount;
    String strCategory;
    String strDueDate;
    String strReminder;
    String strNote;
    String strBillId;

    public String getStrBillPaid() {
        return strBillPaid;
    }

    public void setStrBillPaid(String strBillPaid) {
        this.strBillPaid = strBillPaid;
    }

    String strBillPaid;

    public String getStrBillName() {
        return strBillName;
    }

    public void setStrBillName(String strBillName) {
        this.strBillName = strBillName;
    }

    public String getStrAccountName() {
        return strAccountName;
    }

    public void setStrAccountName(String strAccountName) {
        this.strAccountName = strAccountName;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrDueDate() {
        return strDueDate;
    }

    public void setStrDueDate(String strDueDate) {
        this.strDueDate = strDueDate;
    }

    public String getStrReminder() {
        return strReminder;
    }

    public void setStrReminder(String strReminder) {
        this.strReminder = strReminder;
    }

    public String getStrNote() {
        return strNote;
    }

    public void setStrNote(String strNote) {
        this.strNote = strNote;
    }

    public String getStrBillId() { return strBillId;}

    public void setStrBillId(String strBillId) {this.strBillId = strBillId;}

    public BillData(){
    }

    public BillData(String strBillName,
                 String strAccountName,
                 String strAmount,
                 String strCategory,
                 String strDueDate,
                 String strReminder,
                 String strNote){
        setStrBillName(strBillName);
        setStrAccountName(strAccountName);
        setStrAmount(strAmount);
        setStrCategory(strCategory);
        setStrDueDate(strDueDate);
        setStrReminder(strReminder);
        setStrNote(strNote);
        setStrBillPaid("False");
    }

    public String toString(){
        String strReturn = "";
        String BillName = getStrBillName();
        String BillAmount = getStrAmount();
        String BillCategory = getStrCategory();
        strReturn += BillName + "\n";
        strReturn += "Amount : "+BillAmount;
        return strReturn;
    }
}
