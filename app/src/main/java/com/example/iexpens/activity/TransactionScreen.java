package com.example.iexpens.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.iexpens.R;
import com.example.iexpens.fragments.WalletFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionScreen extends AppCompatActivity {
    private TextView text;
    private FirebaseAuth mAuth;
    private ListView listViewTransactions;
    private DatabaseReference databaseTransaction;
    private List<Transactions> transactionsList;
    private ListView listViewTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_screen);

        text = (TextView) findViewById(R.id.text);
        Intent intent = getIntent();
        String cashtitle = intent.getStringExtra(CashWalletScreen.WALLET_CASH_TITLE);
        String cashId = intent.getStringExtra(CashWalletScreen.WALLET_CASH_ID);


        String bankId = intent.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_ID);
        String acc_no = intent.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_NO);

        listViewTransaction = (ListView) findViewById(R.id.listViewTransaction);
        transactionsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_Id = user.getUid();

        if (cashId != null) {
            text.setText(cashtitle);
            databaseTransaction = FirebaseDatabase.getInstance().getReference().child(user_Id).child("Wallet Transactions").child("WALLET").child(cashId);
        }else{
            text.setText(acc_no);
            databaseTransaction = FirebaseDatabase.getInstance().getReference().child(user_Id).child("Bank Transactions").child("Bank Accounts").child(bankId);
        }
    }

    public void onStart() {
        super.onStart();

        databaseTransaction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transactionsList.clear();
                for (DataSnapshot cashSnapshot : dataSnapshot.getChildren()) {
                    Transactions transactions = cashSnapshot.getValue(Transactions.class);
                    transactionsList.add(transactions);
                }
                TransactionList adapter1 = new TransactionList(TransactionScreen.this, transactionsList);
                listViewTransaction.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
