package com.example.iexpens.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iexpens.fragments.WalletFragment;
import com.example.iexpens.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CashWalletScreen extends AppCompatActivity {

    private TextView cash_bal;
    private FloatingActionButton update_cash_amount;
    private Button button_cash_expense;
    private Button button_cash_trans;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseTransactions;
    private DatabaseReference databaseReference;

    public static final String USER_ID = "userid";
    public static final String BANK_AMOUNT = "bankamount";
    public static final String BANK_ID = "bankid";
    public static final String BANK_NO = "bankaccountno";
    public static final String BANK_NAME = "bankaccountname";
    public static final String BANK_BANKS = "bankaccounts";
    public static final String BANK_TYPE = "bankaccounttype";

    public static final String CASH_ID = "cashid";
    public static final String CASH_TITLE = "cashtitle";
    public static final String CASH_AMOUNT = "cashamount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_wallet_screen);

        update_cash_amount = (FloatingActionButton) findViewById(R.id.update_cash_amount);
        update_cash_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_cash_amount_onClick(v);
            }
        });

        button_cash_expense = (Button) findViewById(R.id.button_cash_expense);
        button_cash_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash_expense_onClick(v);
            }
        });

        button_cash_trans = (Button) findViewById(R.id.button_cash_trans);
        button_cash_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash_trans_onClick(v);
            }
        });

        cash_bal = (TextView) findViewById(R.id.cash_bal);

        Intent intent = getIntent();
        String amount = intent.getStringExtra(WalletFragment.CASH_AMOUNT);
        cash_bal.setText(amount + " Kr");
        String cashId = intent.getStringExtra(WalletFragment.CASH_ID);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_Id = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(user_Id).child("WALLET").child(cashId);
        databaseTransactions = FirebaseDatabase.getInstance().getReference(user_Id).child("Wallet Transactions").child("WALLET").child(cashId);

    }

    private void cash_trans_onClick(View v) {
/*
        Intent intent = new Intent(CashWalletScreen.this, AddExpenseActivity.class);

        String id = intent.getStringExtra(WalletFragment.CASH_ID);
        String title = intent.getStringExtra(WalletFragment.CASH_TITLE);
        String amount = intent.getStringExtra(WalletFragment.CASH_AMOUNT);

        intent.putExtra(id, WalletFragment.CASH_ID);
        intent.putExtra(title, WalletFragment.CASH_TITLE);
        intent.putExtra(amount, WalletFragment.CASH_TITLE);

        startActivity(intent);*/
    }

    private void cash_expense_onClick(View v) {

    }

    private void update_cash_amount_onClick(View v) {
        showUpdateDialog();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder dialogBuiler = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_amount, null);
        dialogBuiler.setView(dialogView);

        final EditText update_acc_amount = (EditText) dialogView.findViewById(R.id.update_acc_amount);
        final Button button_update_amount = (Button) dialogView.findViewById(R.id.button_update_amount);

        final AlertDialog alertDialog = dialogBuiler.create();
        alertDialog.show();

        button_update_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra(WalletFragment.CASH_ID);
                String title = intent.getStringExtra(WalletFragment.CASH_TITLE);
                String amount = intent.getStringExtra(WalletFragment.CASH_AMOUNT);

                String cashamount = update_acc_amount.getText().toString().trim();
                String cashamt = "";

                int value = Integer.parseInt(cashamount) + Integer.parseInt(amount);
                cashamt = "" + value;

                if (TextUtils.isEmpty(cashamount)) {
                    update_acc_amount.setError(getString(R.string.enter_amount_err));
                    return;
                }
                update_amount(id, title, cashamt);
                update_transaction(cashamount);
                alertDialog.dismiss();
                cash_bal.setText(cashamt + " Kr");
            }
        });
    }

    private boolean update_amount(String cashId, String title, String cashAmount) {
        CashWallet cashWallet = new CashWallet(cashId, title, cashAmount);
        databaseReference.setValue(cashWallet);

        Toast.makeText(this, getString(R.string.amount_added_msg_wallet), Toast.LENGTH_LONG).show();
        return true;
    }

    private void update_transaction(String cash_amt) {
        String trans_id = databaseTransactions.push().getKey();

        SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("dd-MM-yyyy' T 'HH:mm:sss");

        String current_date = ISO_8601_FORMAT.format(new Date());
        String trans_type = getString(R.string.trans_credit);
        String amt = cash_amt;

        Transactions transactions = new Transactions(trans_id, current_date, trans_type, amt);
        databaseTransactions.child(trans_id).setValue(transactions);

    }

    /**
     * Thr method selectCategoryForExpense is used to move to category page
     * @param view
     */
    public void selectCategoryForExpense(View view) {
        Intent intent = new Intent(CashWalletScreen.this, Category.class);
        startActivity(intent);
    }
}
