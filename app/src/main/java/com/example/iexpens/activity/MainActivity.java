package com.example.iexpens.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.iexpens.fragments.BillReminder;
import com.example.iexpens.fragments.Bills;
import com.example.iexpens.fragments.CashReminder;
import com.example.iexpens.fragments.NotificationFragment;
import com.example.iexpens.fragments.HomeFragment;
import com.example.iexpens.fragments.OverviewFragment;
import com.example.iexpens.fragments.WalletFragment;
import com.example.iexpens.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment;
            Log.d(TAG, "inside bottom navigation listener");
            Log.d(TAG, "Item" + item);

            FirebaseUser user = mAuth.getCurrentUser();
            String userId = user.getUid();

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
            Log.d(TAG, "selectedFragment " + selectedFragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        /*if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Welcome.class));
            finish();
        }*/

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
        if (expenseCalendar != null) {
            expenseCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    printDate(year, month, dayOfMonth);
                }
            });
        }
        Log.i("Main Activity invoked","Main Activity invoked");
        createPeriodicCheckforCash();
    }

    public void createPeriodicCheckforCash(){
        Intent intent = new Intent(MainActivity.this, CashReminder.class);
        Calendar calendar = Calendar.getInstance();
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this.getApplicationContext(), 234324243, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.HOUR, 8);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        long reminderTime = calendar.getTimeInMillis();
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
    }

    private void printDate(int year, int month, int dayOfMonth) {
        Log.d("Year", Integer.toString(year));
        Log.d("month", Integer.toString(month));
        Log.d("day", Integer.toString(dayOfMonth));
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

    /*public void addBill(View view) {
        Log.d("Add", "Adding new bill - MainActivity");
        Fragment AddBills = new Bills();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, AddBills);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(this, Welcome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001){
            String val = data.getExtras().getString("ListViewClickedValue");
            Log.d("val MainActivity",val);
            TextView categoryChooser = findViewById(R.id.CategoryChooser);
            categoryChooser.setText(val);
        }
    }
}