package com.example.iexpens.Activity;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.iexpens.Fragments.Bills;
import com.example.iexpens.Fragments.HomeFragment;
import com.example.iexpens.Fragments.NotificationFragment;
import com.example.iexpens.Fragments.OverviewFragment;
import com.example.iexpens.Fragments.WalletFragment;
import com.example.iexpens.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment;
            Log.d(TAG,"inside bottom navigation listener" );
            Log.d(TAG,"Item" + item);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new NotificationFragment();
                    break;
                case R.id.navigation_overview:
                   selectedFragment = new OverviewFragment();
                 break;
                case R.id.navigation_wallet:
                    selectedFragment = new WalletFragment();
                break;
                default:
                    selectedFragment = new HomeFragment();
                    break;
            }
            Log.d(TAG,"selectedFragment "+ selectedFragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //To keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
        CalendarView expenseCalendar = findViewById(R.id.expenseCalendar);
        if(expenseCalendar!=null){
            expenseCalendar.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    printDate( year, month, dayOfMonth );
                }
            });
        }
    }

    private void printDate(int year, int month, int dayOfMonth) {
        Log.d("Year",Integer.toString(year));
        Log.d("month",Integer.toString(month));
        Log.d("day",Integer.toString(dayOfMonth));
    }

    public void setDatePickListener(View view) {
        CalendarView expenseCalendar = findViewById(R.id.expenseCalendar);
        /*if(expenseCalendar!=null){
            expenseCalendar.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    Log.d("print","Print");
                    printDate( year, month, dayOfMonth );
    }
            });
        }*/
    }
    public void addBill(View view) {
        Log.d("Add","Adding new bill");
        Fragment AddBills = new Bills();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,AddBills);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}