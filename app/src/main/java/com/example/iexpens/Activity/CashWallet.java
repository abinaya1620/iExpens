package com.example.iexpens.Activity;

public class CashWallet {
    //test
    String cashId;

    String cash;

    public CashWallet() {
    }

    public CashWallet(String cashId, String cash) {
        this.cashId = cashId;
        this.cash = cash;
    }

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
