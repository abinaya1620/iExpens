package com.example.iexpens.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import com.example.iexpens.R;
import com.example.iexpens.activity.BillData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String selectedDate="";
    ArrayAdapter adapter;
    ArrayList listItems = new ArrayList();
    ArrayList listKeys = new ArrayList();
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String mUserId;

    private OnFragmentInteractionListener mListener;
    FloatingActionButton floatingActionButton;
    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    private static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserId = mUser.getUid();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        CalendarView expenseCalendar = view.findViewById(R.id.expenseCalendar);
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        setSelectedDate(String.valueOf(yy) +"-"+String.valueOf(mm+1)+"-"+String.valueOf(dd));
        expenseCalendar.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("print","Print");
                selectedDate = year+"-"+(month+1)+"-"+dayOfMonth;
                showItemsByDate( year, month+1, dayOfMonth );
            }
        });
        floatingActionButton = view.findViewById(R.id.floatingActionButton4);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Add","Adding new bill notification Fragment");
                Fragment AddBills = new com.example.iexpens.fragments.Bills();
                Log.d("Sent due date",selectedDate);
                ((Bills) AddBills).setStrIntialDuedate(selectedDate);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,AddBills);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void showItemsByDate(int year, int month, int dayOfMonth) {
        Log.d("Year",Integer.toString(year));
        Log.d("month",Integer.toString(month));
        Log.d("day",Integer.toString(dayOfMonth));
        String querydate = year+"-"+month+"-"+dayOfMonth;
        setSelectedDate(querydate);
        ListView itemList = getView().findViewById(R.id.billListView);
        ArrayList<String> items = new ArrayList<String>();
        //ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        adapter = new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1,items);
        String userid = "user1";
        //DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Bill_"+userid);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(mUserId).child("bills");
        Query query = dbRef.orderByChild("strDueDate").equalTo(querydate);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                adapter.clear();
                listKeys.clear();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    String billName = (String) next.child("strBillName").getValue();
                    String amount = (String) next.child("strAmount").getValue();
                    BillData bill = next.getValue(BillData.class);
                    String key = next.getKey();
                    listKeys.add(key);
                    //adapter.add("Bill: "+billName +" - Amount: " + amount);
                    adapter.add(bill);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        itemList.setAdapter(adapter);
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
    /*public void addBill(View view) {
        Log.d("Add","Adding new bill notification Fragment");
        Fragment AddBills = new com.example.iexpens.fragments.Bills();
        Log.d("Sent due date",selectedDate);
        ((Bills) AddBills).setStrIntialDuedate(selectedDate);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,AddBills);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    public void onStart() {
        super.onStart();
        String userid = "user1";
        //DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Bill_"+userid);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(mUserId).child("bills");
        Date date = new Date(); // your date
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        ListView itemList = getView().findViewById(R.id.billListView);
        ArrayList<String> items = new ArrayList<String>();
        adapter = new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1,items);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String querydate = year+"-"+month+"-"+day;
        Log.d("query date", querydate);
        Query query = dbRef.orderByChild("strDueDate").equalTo(querydate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                adapter.clear();
                listKeys.clear();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    String billName = (String) next.child("strBillName").getValue();
                    String amount = (String) next.child("strAmount").getValue();
                    BillData bill = next.getValue(BillData.class);
                    String key = next.getKey();
                    listKeys.add(key);
                    //adapter.add("Bill: "+billName +" - Amount: " + amount);
                    adapter.add(bill);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        itemList.setAdapter(adapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.item_menu1);
        if(item!=null)
            item.setVisible(false);
    }
}
