package com.example.iexpens.activity;

public class Transactions {
    String transId;
    String date_time, trans_type, amount;

    public Transactions() {
    }

    public Transactions(String transId, String date_time, String trans_type, String amount) {
        this.transId = transId;
        this.date_time = date_time;
        this.trans_type = trans_type;
        this.amount = amount;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
