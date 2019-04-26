package com.example.iexpens.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.iexpens.R;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class ExpenseList extends ArrayAdapter {
    private Activity context;
    private List<Expense> expenseList;

    public ExpenseList(Activity context, List<Expense> expenseList){
        super(context, R.layout.expense_list_layout,expenseList);
        this.context = context;
        this.expenseList = expenseList;
    }
    @NotNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.expense_list_layout, null, true);

        TextView textViewCaterogy = (TextView) listViewItem.findViewById(R.id.textViewCaterogy);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewPrice);

        Expense expense = expenseList.get(position);
        textViewCaterogy.setText(expense.getExpenseCategory());
        textViewPrice.setText(expense.getPrice());

        return listViewItem;
    }
}
