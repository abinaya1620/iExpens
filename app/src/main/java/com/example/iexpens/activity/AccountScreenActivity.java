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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AccountScreenActivity extends AppCompatActivity {

    private TextView acc_bal;
    private TextView acc_no;
    FloatingActionButton update_acc_amount;
    Button button_del_acc;
    DatabaseReference databaseTransactions;
    Button button_add_accexp;
    Button button_acc_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_screen);

        update_acc_amount = (FloatingActionButton) findViewById(R.id.update_acc_amount);
        update_acc_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_acc_amount_onClick(v);
            }
        });

        button_del_acc = (Button)findViewById(R.id.button_del_acc);
        button_del_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del_acc_onClick(v);
            }
        });

        button_add_accexp = (Button)findViewById(R.id.button_add_accexp);
        button_add_accexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_accexp_onClick(v);
            }
        });

        button_acc_trans = (Button)findViewById(R.id.button_acc_trans);
        button_acc_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_trans_onClick(v);
            }
        });

        acc_bal = (TextView) findViewById(R.id.acc_bal);
        acc_no = (TextView) findViewById(R.id.acc_no);

        Intent intent = getIntent();

        String amount = intent.getStringExtra(WalletFragment.BANK_AMOUNT);
        acc_bal.setText(amount + " Kr");
        String accno = intent.getStringExtra(WalletFragment.BANK_NO);
        acc_no.setText(accno);

    }


    private void add_accexp_onClick(View v) {
/*        Intent intent = new Intent(AccountScreenActivity.this, AddExpenseActivity.class);

        String id = intent.getStringExtra(WalletFragment.BANK_ID);
        String accno = intent.getStringExtra(WalletFragment.BANK_NO);
        String accname = intent.getStringExtra(WalletFragment.BANK_NAME);
        String amount = intent.getStringExtra(WalletFragment.BANK_AMOUNT);
        String bank = intent.getStringExtra(WalletFragment.BANK_BANKS);
        String bank_type = intent.getStringExtra(WalletFragment.BANK_TYPE);

        intent.putExtra(id, WalletFragment.BANK_ID);
        intent.putExtra(accno, WalletFragment.BANK_NO);
        intent.putExtra(accname, WalletFragment.BANK_NAME);
        intent.putExtra(amount, WalletFragment.BANK_AMOUNT);
        intent.putExtra(bank, WalletFragment.BANK_BANKS);
        intent.putExtra(bank_type, WalletFragment.BANK_TYPE);

        startActivity(intent);*/
    }


    private void acc_trans_onClick(View v) {
    }

    private void del_acc_onClick(View v) {
        Intent intent = getIntent();
        String id = intent.getStringExtra(WalletFragment.BANK_ID);
        deleteAccount(id);
    }

    public void update_acc_amount_onClick(View view) {
        showUpdateDialog();
    }

    public boolean deleteAccount(String bankId) {

        DatabaseReference drAccount = FirebaseDatabase.getInstance().getReference("Bank Accounts").child(bankId);
        DatabaseReference drtransactions = FirebaseDatabase.getInstance().getReference("Bank Transactions").child(bankId);

        drAccount.removeValue();
        drtransactions.removeValue();

        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AccountScreenActivity.this, MainActivity.class);
        startActivity(intent);
        return true;
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
                String id = intent.getStringExtra(WalletFragment.BANK_ID);
                String accno = intent.getStringExtra(WalletFragment.BANK_NO);
                String accname = intent.getStringExtra(WalletFragment.BANK_NAME);
                String amount = intent.getStringExtra(WalletFragment.BANK_AMOUNT);
                String bank = intent.getStringExtra(WalletFragment.BANK_BANKS);
                String bank_type = intent.getStringExtra(WalletFragment.BANK_TYPE);

                String accamount = update_acc_amount.getText().toString().trim();
                String accamt = "";

                int value = Integer.parseInt(accamount) + Integer.parseInt(amount);
                accamt = "" + value;

                if (TextUtils.isEmpty(accamount)) {
                    update_acc_amount.setError("Enter Amount");
                    return;
                }
                update_amount(id, accno, accname, accamt, bank, bank_type);
                update_transaction(id, accamount);
                alertDialog.dismiss();
                acc_bal.setText(accamt + " Kr");

            }
        });
    }

    public boolean update_amount(String bankId, String acc_no, String acc_name, String acc_amount, String banks, String acc_type) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bank Accounts").child(bankId);

        BankAccount bankAccount = new BankAccount(bankId, acc_no, acc_name, acc_amount, banks, acc_type);
        databaseReference.setValue(bankAccount);

        Toast.makeText(this, "Amount Added Successfully into the account " + acc_no, Toast.LENGTH_LONG).show();
        return true;
    }

    private void update_transaction(String bankId, String acc_amt) {
        databaseTransactions = FirebaseDatabase.getInstance().getReference("Bank Transactions").child(bankId);
        String trans_id = databaseTransactions.push().getKey();

        SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("dd-MM-yyyy' T 'HH:mm:sss");

        String current_date = ISO_8601_FORMAT.format(new Date());
        String trans_type = "Credit";
        String amt = acc_amt;

        Log.d("Print", trans_id);
        Log.d("Print", current_date);
        Log.d("Print", trans_type);
        Log.d("Print", amt);

        Transactions transactions = new Transactions(trans_id, current_date, trans_type, amt);
        databaseTransactions.child(trans_id).setValue(transactions);

    }

}
