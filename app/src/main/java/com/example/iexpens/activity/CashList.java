package com.example.iexpens.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.iexpens.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CashList extends ArrayAdapter<CashWallet> {
    private Activity context;
    private List<CashWallet> cashList;

    public CashList(Activity context, List<CashWallet> cashList){
        super(context, R.layout.list_cash, cashList);
        this.context = context;
        this.cashList = cashList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listviewItem = inflater.inflate(R.layout.list_cash,null,true);

        TextView text_cash = (TextView) listviewItem.findViewById(R.id.text_cash);
        TextView text_cashamount = (TextView) listviewItem.findViewById(R.id.text_cashamount);

        CashWallet cashWallet = cashList.get(position);

        text_cash.setText(cashWallet.getCashTitle());
        text_cashamount.setText(cashWallet.getCash() + " Kr" );

        return listviewItem;
    }
}
