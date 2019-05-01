package com.example.iexpens.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.iexpens.R;
import com.example.iexpens.activity.AddExpenseActivity;
import com.example.iexpens.activity.Expense;
import com.example.iexpens.activity.ExpenseList;
import com.github.mikephil.charting.charts.BarChart;
import com.example.iexpens.activity.AccountList;
import com.example.iexpens.activity.BankAccount;
import com.example.iexpens.activity.CashList;
import com.example.iexpens.activity.CashWallet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


public class HomeFragment extends Fragment {

    PieChartView pieChartView;
    BarChart barChart;

    private ListView listViewAccounts_home;
    private ListView listViewCash_home;
    private DatabaseReference databaseAccounts;
    private DatabaseReference databaseWallet;
    private List<BankAccount> accountList;
    private List<CashWallet> cashList;
    private Activity activity;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseExpenses;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainview = inflater.inflate(R.layout.fragment_home, container, false);

        // Pie chart
        pieChartView = mainview.findViewById(R.id.chart);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_Id = user.getUid();
        databaseExpenses = FirebaseDatabase.getInstance().getReference(user_Id).child("expenses");

        List<SliceValue> pieData = new ArrayList<SliceValue>();
        pieData.add(new SliceValue(15, Color.BLUE).setLabel("Q1: $10"));
        pieData.add(new SliceValue(25, Color.GRAY).setLabel("Q2: $4"));
        pieData.add(new SliceValue(10, Color.GREEN).setLabel("Q3: $18"));
        pieData.add(new SliceValue(60, Color.LTGRAY).setLabel("Q4: $28"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("Expenses").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#080808"));
        pieChartView.setPieChartData(pieChartData);

        // Wallet and Accounts
        listViewAccounts_home = (ListView) mainview.findViewById(R.id.listViewAccounts_home);
        listViewCash_home = (ListView) mainview.findViewById(R.id.listViewCash_home);
        accountList = new ArrayList<>();
        cashList = new ArrayList<>();

        databaseAccounts = FirebaseDatabase.getInstance().getReference().child(user_Id).child("Bank Accounts");
        databaseWallet = FirebaseDatabase.getInstance().getReference().child(user_Id).child("WALLET");

        return mainview;
    }

    public void onStart() {
        super.onStart();



        databaseExpenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Float> expensesByCategory = new HashMap<>();

                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()){
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    String category = expense.getExpenseCategory();
                    Float amount = Float.parseFloat(expense.getPrice());

                    if(!expensesByCategory.containsKey(category))
                        expensesByCategory.put(category, amount);
                    else{
                        amount += expensesByCategory.get(category);
                        expensesByCategory.put(category, amount);
                    }
                }

                //expensesByCategory.forEach(cat, value);
                List<SliceValue> pieData = new ArrayList<>();
                pieData.add(new SliceValue(15, Color.BLUE).setLabel("Q1: $10"));
                pieData.add(new SliceValue(25, Color.GRAY).setLabel("Q2: $4"));
                pieData.add(new SliceValue(10, Color.GREEN).setLabel("Q3: $18"));
                pieData.add(new SliceValue(60, Color.LTGRAY).setLabel("Q4: $28"));
                PieChartData pieChartData = new PieChartData(pieData);
                pieChartView.setPieChartData(pieChartData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        databaseWallet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cashList.clear();
                for (DataSnapshot cashSnapshot : dataSnapshot.getChildren()) {
                    CashWallet cashWallet = cashSnapshot.getValue(CashWallet.class);
                    cashList.add(cashWallet);
                }
                CashList adapter1 = new CashList(activity, cashList);
                listViewCash_home.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseAccounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                accountList.clear();

                for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                    BankAccount bankAccount = accountSnapshot.getValue(BankAccount.class);

                    accountList.add(bankAccount);
                }
                AccountList adapter = new AccountList(activity, accountList);
                listViewAccounts_home.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    /**
     * The method onPrepareOptionsMenu is used to hide the menu option in fragment
     * @param menu
     */

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.item_menu1);
        if(item!=null)
            item.setVisible(false);
    }


}