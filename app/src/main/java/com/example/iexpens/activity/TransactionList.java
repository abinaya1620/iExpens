package com.example.iexpens.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.iexpens.R;

import java.util.List;

public class TransactionList extends ArrayAdapter<Transactions> {

    private Activity context;
    private List<Transactions> transactionsList;

    public TransactionList(Activity context, List<Transactions> transactionsList) {
        super(context, R.layout.activity_transaction_list, transactionsList);
        this.context = context;
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listviewItem = inflater.inflate(R.layout.activity_transaction_list, null, true);

        TextView text_date_time = (TextView) listviewItem.findViewById(R.id.text_date_time);
        TextView text_amt = (TextView) listviewItem.findViewById(R.id.text_amt);
        TextView text_trans_typ = (TextView) listviewItem.findViewById(R.id.text_trans_typ);

        Transactions transactions = transactionsList.get(position);

        text_date_time.setText(transactions.getDate_time());
        text_amt.setText(transactions.getAmount() + " Kr");
        text_trans_typ.setText(transactions.getTrans_type());

        return listviewItem;
    }

}
