package com.example.iexpens.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.ListView;

import com.example.iexpens.activity.AccountList;
import com.example.iexpens.activity.BankAccount;
import com.example.iexpens.activity.AccountScreenActivity;

import com.example.iexpens.activity.CashList;
import com.example.iexpens.activity.CashWallet;
import com.example.iexpens.activity.CashWalletScreen;
import com.example.iexpens.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WalletFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */

public class WalletFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button button_add_account;
    private Button button_add_cash;

    private ListView listViewAccounts;
    private ListView listViewCash;
    private DatabaseReference databaseAccounts;
    private DatabaseReference databaseCash;
    private List<BankAccount> accountList;
    private List<CashWallet> cashList;
    private Activity activity;

    public static final String BANK_AMOUNT = "bankamount";
    public static final String BANK_ID = "bankid";
    public static final String BANK_NO = "bankaccountno";
    public static final String BANK_NAME = "bankaccountname";
    public static final String BANK_BANKS = "bankaccounts";
    public static final String BANK_TYPE = "bankaccounttype";

    public static final String CASH_ID = "cashid";
    public static final String CASH_TITLE = "cashtitle";
    public static final String CASH_AMOUNT = "cashamount";

    public WalletFragment() {
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
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        listViewAccounts = (ListView) view.findViewById(R.id.listViewAccounts);
        listViewCash = (ListView) view.findViewById(R.id.listViewCash);
        databaseAccounts = FirebaseDatabase.getInstance().getReference("Bank Accounts");
        databaseCash = FirebaseDatabase.getInstance().getReference("Cash");
        accountList = new ArrayList<>();
        cashList = new ArrayList<>();

        button_add_account = (Button) view.findViewById(R.id.button_add_account);
        button_add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_add_account_onClick(v);
            }
        });

        listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BankAccount bankAccount = accountList.get(position);
                Intent intent = new Intent(getActivity(), AccountScreenActivity.class);
                intent.putExtra(BANK_ID, bankAccount.getBankId());
                intent.putExtra(BANK_AMOUNT, bankAccount.getAcc_amount());
                intent.putExtra(BANK_NO, bankAccount.getAcc_no());
                intent.putExtra(BANK_NAME, bankAccount.getAcc_name());
                intent.putExtra(BANK_BANKS, bankAccount.getBanks());
                intent.putExtra(BANK_TYPE, bankAccount.getAcc_type());
                startActivity(intent);
            }
        });

        listViewCash.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CashWallet cashWallet = cashList.get(position);
                Intent intent = new Intent(getActivity(), CashWalletScreen.class);
                intent.putExtra(CASH_ID, cashWallet.getCashId());
                intent.putExtra(CASH_TITLE, cashWallet.getCashTitle());
                intent.putExtra(CASH_AMOUNT, cashWallet.getCash());
                startActivity(intent);
            }
        });
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void button_add_account_onClick(View view) {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new AddAccountFragment());
        fr.commit();
    }

    public void onStart() {
        super.onStart();

        databaseCash.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cashList.clear();
                for (DataSnapshot cashSnapshot : dataSnapshot.getChildren()) {
                    CashWallet cashWallet = cashSnapshot.getValue(CashWallet.class);
                    cashList.add(cashWallet);
                }
                CashList adapter1 = new CashList(activity, cashList);
                listViewCash.setAdapter(adapter1);
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
                listViewAccounts.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

     @Override
    public void onAttach(Activity activity){
        this.activity = activity;
        super.onAttach(activity);
    }
}
