package com.example.iexpens.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iexpens.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCashActivity extends AppCompatActivity {
    private EditText cname;
    private EditText camount;
    Button button_cashsave;
    DatabaseReference databaseCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash);

        databaseCash = FirebaseDatabase.getInstance().getReference("Cash");

        cname = (EditText)findViewById(R.id.cname);
        camount = (EditText)findViewById(R.id.camount);
        button_cashsave = (Button)findViewById(R.id.button_cashsave);
        button_cashsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_cashsave_onClick(v);
            }
        });
    }

    private void button_cashsave_onClick(View v) {

        String c_name = cname.getText().toString().trim();
        String c_amount = camount.getText().toString().trim();


            String id = databaseCash.push().getKey();
            CashWallet cashWallet = new CashWallet(id, c_name, c_amount);
            databaseCash.child(id).setValue(cashWallet);
            Toast.makeText(AddCashActivity.this, "Cash Saved", Toast.LENGTH_LONG).show();

    }



}
