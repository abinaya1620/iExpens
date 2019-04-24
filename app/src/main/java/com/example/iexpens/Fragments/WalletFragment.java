package com.example.iexpens.Fragments;

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
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;

import com.example.iexpens.Activity.AccountList;
import com.example.iexpens.Activity.BankAccount;
import com.example.iexpens.Activity.AccountScreenActivity;

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
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class WalletFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button button_add_account;

    private ListView listViewAccounts;
    private DatabaseReference databaseAccounts;
    private DatabaseReference databaseCash;
    private List<BankAccount> accountList;
    private ArrayAdapter<AccountList> adapter;
    private TextView op_cash;
    private TextView text_cash;

    public static final String BANK_AMOUNT = "bankamount";
    public static final String BANK_ID = "bankid";
    public static final String BANK_NO = "bankaccountno";
    public static final String BANK_NAME = "bankaccountname";
    public static final String BANK_BANKS = "bankaccounts";
    public static final String BANK_TYPE = "bankaccounttype";

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(String param1, String param2) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        listViewAccounts = (ListView)view.findViewById(R.id.listViewAccounts);
        databaseAccounts = FirebaseDatabase.getInstance().getReference("Bank Accounts");
        databaseCash = FirebaseDatabase.getInstance().getReference("Cash");
        accountList = new ArrayList<>();
      /*  op_cash = (TextView)view.findViewById(R.id.op_cash);
        text_cash = (TextView)view.findViewById(R.id.text_cash);
        text_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_cash_onClick(v);
            }
        });*/

        button_add_account = (Button)view.findViewById(R.id.button_add_account);
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
                intent.putExtra(BANK_ID,bankAccount.getBankId());
                intent.putExtra(BANK_AMOUNT,bankAccount.getAcc_amount());
                intent.putExtra(BANK_NO,bankAccount.getAcc_no());
                intent.putExtra(BANK_NAME,bankAccount.getAcc_name());
                intent.putExtra(BANK_BANKS,bankAccount.getBanks());
                intent.putExtra(BANK_TYPE,bankAccount.getAcc_type());

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

        //Intent intent = new Intent(getActivity(), AddAccountActivity.class);
        //startActivity(intent);
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new AddAccountFragment());
        fr.commit();
    }

   /* public void text_cash_onClick(View view){
        Intent intent = new Intent(getActivity(), CashWalletScreen.class);
        startActivity(intent);
    }*/

    public void onStart(){
        super.onStart();

        databaseAccounts.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                accountList.clear();

                for(DataSnapshot accountSnapshot: dataSnapshot.getChildren()){
                    BankAccount bankAccount = accountSnapshot.getValue(BankAccount.class);

                    accountList.add(bankAccount);
                }
              AccountList adapter = new AccountList(getActivity(), accountList);
              listViewAccounts.setAdapter(adapter);

            }


                @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       // FragmentTransaction fr = getFragmentManager().beginTransaction();
        //fr.replace(R.id.fragment_container,new AddAccountFragment());
        //fr.commit();
    }
}
