package com.example.iexpens.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iexpens.Fragments.WalletFragment;
import com.example.iexpens.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountScreenActivity extends AppCompatActivity {

    private TextView acc_bal;
    private TextView acc_no;
    FloatingActionButton update_acc_amount;


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

        acc_bal = (TextView) findViewById(R.id.acc_bal);
        acc_no = (TextView) findViewById(R.id.acc_no);

        Intent intent = getIntent();

        String amount = intent.getStringExtra(WalletFragment.BANK_AMOUNT);
        acc_bal.setText(amount + " Kr");
        String accno = intent.getStringExtra(WalletFragment.BANK_NO);
        acc_no.setText(accno);
    }

    public void update_acc_amount_onClick(View view) {
        showUpdateDialog();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder dialogBuiler = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_acc_amount, null);
        dialogBuiler.setView(dialogView);

        final EditText update_acc_amount = (EditText) dialogView.findViewById(R.id.update_acc_amount);
        final Button button_update_amount = (Button) dialogView.findViewById(R.id.button_update_amount);
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
                //int accamount = Integer.parseInt(update_acc_amount.getText().toString().trim());
                String accamt = "";

                int value = Integer.parseInt(accamount) + Integer.parseInt(amount);
                accamt = "" + value;

                if (TextUtils.isEmpty(accamount)) {
                    update_acc_amount.setError("Enter Amount");
                    return;
                }

                update_amount(id, accno, accname, accamt, bank, bank_type);
            }
        });

        //dialogBuiler.setTitle("Updating Accout Amount " +bankId);

        AlertDialog alertDialog = dialogBuiler.create();
        alertDialog.show();
    }

    public boolean update_amount(String bankId, String acc_no, String acc_name, String acc_amount, String banks, String acc_type) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bank Accounts").child(bankId);

        BankAccount bankAccount = new BankAccount(bankId, acc_no, acc_name, acc_amount, banks, acc_type);
        databaseReference.setValue(bankAccount);

        Toast.makeText(this, "Amount Added Successfully", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AccountScreenActivity.this, MainActivity.class);
        startActivity(intent);
        return true;

    }


}
