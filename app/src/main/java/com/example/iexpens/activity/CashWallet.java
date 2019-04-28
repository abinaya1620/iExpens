package com.example.iexpens.activity;

public class CashWallet {
    //test
    String cashId, cashTitle, cash;

    public CashWallet() {
    }

    public CashWallet(String cashId, String cashTitle, String cash) {
        this.cashId = cashId;
        this.cashTitle = cashTitle;
        this.cash = cash;
    }

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }

    public String getCashTitle() {
        return cashTitle;
    }

    public void setCashTitle(String cashTitle) {
        this.cashTitle = cashTitle;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
