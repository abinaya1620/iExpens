package com.example.iexpens.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.iexpens.R;
import com.example.iexpens.activity.BillData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BillDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BillDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String BillName;
    private String BillAccount;
    private String BillAmount;
    private String BillCategory;
    private String BillDueDate;
    private String BillNote;
    private String BillId;
    private FirebaseAuth mAuth;

    public BillDetailsFragment() {
    }

    public BillDetailsFragment(BillData bill, String billKey) {
        setBillName(bill.getStrBillName());
        setBillAccount(bill.getStrAccountName());
        setBillAmount(bill.getStrAmount());
        setBillCategory(bill.getStrCategory());
        setBillDueDate(bill.getStrDueDate());
        setBillNote(bill.getStrNote());
        setBillId(billKey);
    }

    public String getBillName() {
        return BillName;
    }

    public void setBillName(String billName) {
        BillName = billName;
    }

    public String getBillAccount() {
        return BillAccount;
    }

    public void setBillAccount(String billAccount) {
        BillAccount = billAccount;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public String getBillCategory() {
        return BillCategory;
    }

    public void setBillCategory(String billCategory) {
        BillCategory = billCategory;
    }

    public String getBillDueDate() {
        return BillDueDate;
    }

    public void setBillDueDate(String billDueDate) {
        BillDueDate = billDueDate;
    }

    public String getBillNote() {
        return BillNote;
    }

    public void setBillNote(String billNote) {
        BillNote = billNote;
    }

    public String getBillId() {
        return BillId;
    }

    public void setBillId(String billId) {
        BillId = billId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillDetailsFragment newInstance(String param1, String param2) {
        BillDetailsFragment fragment = new BillDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_details, container, false);
        final View BillsView = inflater.inflate(R.layout.fragment_bills, container, false);
        ((TextView)view.findViewById(R.id.BillDetails_Name)).setText(getBillName());
        ((TextView)view.findViewById(R.id.BillDetails_Account)).setText(getBillAccount());
        ((TextView)view.findViewById(R.id.BillDetails_Amount)).setText(getBillAmount());
        ((TextView)view.findViewById(R.id.BillDetails_Category)).setText(getBillCategory());
        ((TextView)view.findViewById(R.id.BillDetails_DueDate)).setText(getBillDueDate());
        ((TextView)view.findViewById(R.id.BillDetails_Note)).setText(getBillNote());
        Button deleteButton = (Button) view.findViewById(R.id.BillDetails_DeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBill(getBillId());
            }
        });
        Button cancelButton = (Button) view.findViewById(R.id.BillDetails_CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBill(BillsView);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void cancelBill(View view) {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new NotificationFragment());
        fr.commit();
    }
    public void deleteBill(String BillID) {
        //Delete Code here
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_Id = user.getUid();
        DatabaseReference currentBill = FirebaseDatabase.getInstance().getReference().child(user_Id).child("bills").child(BillID);
        currentBill.removeValue();
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new NotificationFragment());
        fr.commit();
    }
}
