package com.example.iexpens.Activity;

public class BankAccount {
    String bankId;

    String acc_no, acc_name, acc_amount, banks, acc_type;

    public BankAccount(){

    }


    public BankAccount(String bankId, String acc_no, String acc_name, String acc_amount, String banks, String acc_type) {
        this.bankId = bankId;
        this.acc_no = acc_no;
        this.acc_name = acc_name;
        this.acc_amount = acc_amount;
        this.banks = banks;
        this.acc_type = acc_type;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String acc_no) {
        this.acc_no = acc_no;
    }

    public String getAcc_name() {
        return acc_name;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }

    public String getAcc_amount() {
        return acc_amount;
    }

    public void setAcc_amount(String acc_amount) {
        this.acc_amount = acc_amount;
    }

    public String getBanks() {
        return banks;
    }

    public void setBanks(String banks) {
        this.banks = banks;
    }

    public String getAcc_type() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }
}
