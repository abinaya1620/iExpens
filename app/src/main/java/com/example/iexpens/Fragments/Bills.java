package com.example.iexpens.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.iexpens.Activity.BillData;
import com.example.iexpens.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Bills.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Bills#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bills extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Bills() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bills.
     */
    // TODO: Rename and change types and number of parameters
    public static Bills newInstance(String param1, String param2) {
        Bills fragment = new Bills();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View BillsView = inflater.inflate(R.layout.fragment_bills, container, false);
        Button addButton = (Button) BillsView.findViewById(R.id.billAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBill(BillsView);
            }
        });
        Button canceldButton = (Button) BillsView.findViewById(R.id.billCancel);
        canceldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBill(BillsView);
            }
        });

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        final Calendar myCalendar = Calendar.getInstance();
        final EditText edittext= (EditText) BillsView.findViewById(R.id.billDueDate);
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear+1)
                                +"-"+String.valueOf(dayOfMonth);
                        edittext.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

        EditText eText = (EditText) BillsView.findViewById(R.id.billDueDate);
        eText.setHint("Add Due Date");
        return BillsView;
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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

    private void updateLabel(EditText edittext, Calendar myCalendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.d("text value",myCalendar.getTime().toString());
        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    public void saveBill(View view) {
        Log.d("Saving","Saving Bills");
        EditText billName= view.findViewById(R.id.billName);
        if(billName==null)
            Log.d("Bill Name","It is null" + view.getClass().getName());
        Spinner billAccount = view.findViewById(R.id.billAccount);
        EditText billAmount= view.findViewById(R.id.billAmount);
        Spinner billCategory = view.findViewById(R.id.billCategory);
        EditText billDueDate= view.findViewById(R.id.billDueDate);
        Spinner billReminder = view.findViewById(R.id.billReminder);
        Switch billAutoPay = view.findViewById(R.id.billAutoPay);
        EditText billNotes= view.findViewById(R.id.billNote);

        if(TextUtils.isEmpty(billName.getText())){
            Toast.makeText(getActivity(), getString(R.string.BillNameNotAvailable), Toast.LENGTH_LONG).show();
            return;
        }else if(TextUtils.isEmpty(billAmount.getText())){
            Toast.makeText(getActivity(), getString(R.string.BillAmountNotAvailable), Toast.LENGTH_LONG).show();
            return;
        }else if(TextUtils.isEmpty(billDueDate.getText())){
            Toast.makeText(getActivity(), getString(R.string.BillDueDateNotAvailable), Toast.LENGTH_LONG).show();
            return;
        }
        String billNameValue = billName.getText().toString();
        String billAccountValue = billAccount.getSelectedItem().toString();
        String billAmountValue = billAmount.getText().toString();
        String billCategoryValue = billCategory.getSelectedItem().toString();
        String billDueDateValue = billDueDate.getText().toString();
        String billReminderValue = billReminder.getSelectedItem().toString();
        String billAutoPayValue =  Boolean.toString(billAutoPay.isChecked());
        String billNotesValue = "";
        if(!TextUtils.isEmpty(billNotes.getText())) {
            billNotesValue = billNotes.getText().toString();
        }

        if(TextUtils.isEmpty(billNameValue)){
            Toast.makeText(getActivity(), getString(R.string.account_name), Toast.LENGTH_LONG).show();
            return;
        }

        saveBillToDatabase(billNameValue,
                billAccountValue,
                billAmountValue,
                billCategoryValue,
                billDueDateValue,
                billReminderValue,
                billAutoPayValue,
                billNotesValue);
    }

    private void saveBillToDatabase(String billNameValue,
                                    String billAccountValue,
                                    String billAmountValue,
                                    String billCategoryValue,
                                    String billDueDateValue,
                                    String billReminderValue,
                                    String billAutoPayValue,
                                    String billNotesValue) {
        String userid = "user1";
        BillData Bill = new BillData(billNameValue,
                billAccountValue,
                billAmountValue,
                billCategoryValue,
                billDueDateValue,
                billReminderValue,
                billAutoPayValue,
                billNotesValue);
        DatabaseReference firebaseDb = FirebaseDatabase.getInstance().getReference("Bill_"+userid);
        String id = firebaseDb.push().getKey();
        firebaseDb.child(id).setValue(Bill);
        Toast.makeText(getActivity(), getString(R.string.BillSavedSuccesfully), Toast.LENGTH_LONG).show();
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new NotificationFragment());
        fr.commit();
    }

    public void cancelBill(View view) {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new NotificationFragment());
        fr.commit();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }
}
