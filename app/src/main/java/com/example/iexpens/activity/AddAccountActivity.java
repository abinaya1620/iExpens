package com.example.iexpens.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iexpens.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAccountActivity extends AppCompatActivity {

    private EditText ip_acc_no;
    private EditText ip_acc_name;
    private EditText ip_acc_amount;
    private TextView spinner_banks;
    private Spinner spinner_acctype;
    private Button button_add_acc;
    private Button button_acc_clear;
    private Activity activity;
    private FirebaseAuth mAuth;
    DatabaseReference databaseAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        databaseAccounts = FirebaseDatabase.getInstance().getReference().child(userId).child("Bank Accounts");

        ip_acc_no = (EditText)findViewById(R.id.ip_acc_no);
        ip_acc_name = (EditText)findViewById(R.id.ip_acc_name);
        ip_acc_amount = (EditText)findViewById(R.id.ip_acc_amount);
        spinner_banks = (TextView)findViewById(R.id.spinner_banks);
        spinner_acctype = (Spinner)findViewById(R.id.spinner_acctype);
        String[] arr = getResources().getStringArray(R.array.Acct_type);
        ArrayAdapter adp = new ArrayAdapter(this,R.layout.spinner_layout,arr);
        adp.setDropDownViewResource(R.layout.dropdown);
        spinner_acctype.setAdapter(adp);
        spinner_banks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBankForCreate(v);
            }
        });


        String TempHolder = getIntent().getStringExtra("ListViewClickedValue");
        spinner_banks.setText(TempHolder);

        button_add_acc = (Button)findViewById(R.id.button_add_acc);
        button_add_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_add_acc_onClick(v);
            }
        });

        button_acc_clear = (Button)findViewById(R.id.button_acc_clear);
        button_acc_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_acc_clear_onClick(v);
            }
        });

    }

    private void button_add_acc_onClick(View v) {

        String acc_no = ip_acc_no.getText().toString().trim();
        String acc_name = ip_acc_name.getText().toString().trim();
        String acc_amount = ip_acc_amount.getText().toString().trim();
        String bank = spinner_banks.getText().toString().trim();
        String acc_type = spinner_acctype.getSelectedItem().toString();

        if(TextUtils.isEmpty(acc_name)){
            Toast.makeText(this, getString(R.string.account_name), Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(acc_amount)){
            Toast.makeText(this, getString(R.string.account_amount), Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(acc_no)){
            Toast.makeText(this, getString(R.string.account_number), Toast.LENGTH_LONG).show();
            return;
        }


        if(!TextUtils.isEmpty(acc_no)){
            String id = databaseAccounts.push().getKey();
            BankAccount bankAccount = new BankAccount(id, acc_no, acc_name, acc_amount, bank, acc_type);
            databaseAccounts.child(id).setValue(bankAccount);
            Toast.makeText(this, getString(R.string.account_added), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(AddAccountActivity.this, MainActivity.class);
            startActivity(intent);

        }else{
            Toast.makeText(this, "Account is not saved", Toast.LENGTH_LONG).show();
        }
    }

    private void button_acc_clear_onClick(View v) {
        ip_acc_no.setText("");
        ip_acc_name.setText("");
        ip_acc_amount.setText("");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.item_menu1);
        if(item!=null)
            item.setVisible(false);
        return false;
    }


    public void selectBankForCreate(View view) {
        Intent intent = new Intent(AddAccountActivity.this, BankCategory.class);
        startActivity(intent);
    }

}
