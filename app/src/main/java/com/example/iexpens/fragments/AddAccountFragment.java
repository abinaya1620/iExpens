package com.example.iexpens.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iexpens.activity.BankAccount;
import com.example.iexpens.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class AddAccountFragment extends Fragment {

    private EditText ip_acc_no;
    private EditText ip_acc_name;
    private EditText ip_acc_amount;
    private Spinner spinner_banks;
    private Spinner spinner_acctype;
    private Button button_add_acc;
    private Button button_acc_clear;
    private Activity activity;


    DatabaseReference databaseAccounts;

    public AddAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_account, container, false);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("Bank Accounts");

        ip_acc_no = (EditText)view.findViewById(R.id.ip_acc_no);
        ip_acc_name = (EditText)view.findViewById(R.id.ip_acc_name);
        ip_acc_amount = (EditText)view.findViewById(R.id.ip_acc_amount);
        spinner_banks = (Spinner) view.findViewById(R.id.spinner_banks);
        spinner_acctype = (Spinner)view.findViewById(R.id.spinner_acctype);

        button_add_acc = (Button)view.findViewById(R.id.button_add_acc);
        button_add_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_add_acc_onClick(v);
            }
        });

        button_acc_clear = (Button)view.findViewById(R.id.button_acc_clear);
        button_acc_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_acc_clear_onClick(v);
            }
        });
        return view;
    }

    private void button_add_acc_onClick(View v) {

        String acc_no = ip_acc_no.getText().toString().trim();
        String acc_name = ip_acc_name.getText().toString().trim();
        String acc_amount = ip_acc_amount.getText().toString().trim();
        String bank = spinner_banks.getSelectedItem().toString();
        String acc_type = spinner_acctype.getSelectedItem().toString();

        if(TextUtils.isEmpty(acc_name)){
            Toast.makeText(getActivity(), getString(R.string.account_name), Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(acc_amount)){
            Toast.makeText(getActivity(), getString(R.string.account_amount), Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(acc_no)){
            Toast.makeText(getActivity(), getString(R.string.account_number), Toast.LENGTH_LONG).show();
            return;
        }


        if(!TextUtils.isEmpty(acc_no)){

            String id = databaseAccounts.push().getKey();
            BankAccount bankAccount = new BankAccount(id, acc_no, acc_name, acc_amount, bank, acc_type);
            databaseAccounts.child(id).setValue(bankAccount);
            Toast.makeText(getActivity(), getString(R.string.account_added), Toast.LENGTH_LONG).show();

            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.fragment_container, new WalletFragment());
            fr.commit();

        }else{
            Toast.makeText(getActivity(), "Account is not saved", Toast.LENGTH_LONG).show();
        }
    }

    private void button_acc_clear_onClick(View v) {
        ip_acc_no.setText("");
        ip_acc_name.setText("");
        ip_acc_amount.setText("");
    }


}
