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

public class AccountList extends ArrayAdapter<BankAccount> {
    private Activity context;
    private List<BankAccount> accountList;

    public AccountList(Activity context, List<BankAccount> accountList) {
        super(context, R.layout.list_accounts, accountList);
        this.context = context;
        this.accountList = accountList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listviewItem = inflater.inflate(R.layout.list_accounts,null,true);

        TextView text_accno = (TextView) listviewItem.findViewById(R.id.text_accno);
        TextView text_accamount = (TextView) listviewItem.findViewById(R.id.text_accamount);
        TextView text_accbank = (TextView) listviewItem.findViewById(R.id.text_accbank);

        BankAccount bankAccount = accountList.get(position);

        text_accno.setText(bankAccount.getAcc_no());
        text_accamount.setText(bankAccount.getAcc_amount() + " Kr" );
        text_accbank.setText(bankAccount.getBanks());

        return listviewItem;
    }


}

