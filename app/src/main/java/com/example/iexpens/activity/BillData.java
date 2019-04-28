package com.example.iexpens.activity;

public class BillData {

    String strBillName;
    String strAccountName;
    String strAmount;
    String strCategory;
    String strDueDate;
    String strReminder;
    String strAutoPay;
    String strNote;

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

    public String getStrAutoPay() {
        return strAutoPay;
    }

    public void setStrAutoPay(String strAutoPay) {
        this.strAutoPay = strAutoPay;
    }

    public String getStrNote() {
        return strNote;
    }

    public void setStrNote(String strNote) {
        this.strNote = strNote;
    }

    public BillData(){
    }

    public BillData(String strBillName,
                 String strAccountName,
                 String strAmount,
                 String strCategory,
                 String strDueDate,
                 String strReminder,
                 String strAutoPay,
                 String strNote){
        setStrBillName(strBillName);
        setStrAccountName(strAccountName);
        setStrAmount(strAmount);
        setStrCategory(strCategory);
        setStrDueDate(strDueDate);
        setStrReminder(strReminder);
        setStrAutoPay(strAutoPay);
        setStrNote(strNote);
        setStrBillPaid("False");
    }

    public String toString(){
        String strReturn = "";
        strReturn += getStrBillName() + "¤";
        strReturn += getStrAccountName() + "¤";
        strReturn += getStrAmount() + "¤";
        strReturn += getStrCategory() + "¤";
        strReturn += getStrDueDate() + "¤";
        strReturn += getStrReminder() + "¤";
        strReturn += getStrAutoPay() + "¤";
        strReturn += getStrBillPaid() + "¤";
        strReturn += getStrNote();
        return strReturn;
    }
}
