package com.example.iexpens.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.iexpens.R;
import com.example.iexpens.activity.AddExpenseActivity;
import com.example.iexpens.activity.Expense;
import com.example.iexpens.activity.ExpenseList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;


public class
OverviewFragment extends Fragment {

    BarChart barChart;
    private List<ExpenseList> expenseList;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseExpenses;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainview = inflater.inflate(R.layout.fragment_overview, container, false);

        barChart = mainview.findViewById(R.id.barChart);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_Id = user.getUid();
        databaseExpenses = FirebaseDatabase.getInstance().getReference(user_Id).child("expenses");

        fillChart();

        return mainview;
    }

    private void fillChart() {
        databaseExpenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float jan=0, feb=0, mar=0, apr=0, may=0, jun=0, jul=0, aug=0, sep=0, oct=0, nov=0, dic=0;

                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()){
                    Expense expense = expenseSnapshot.getValue(Expense.class);

                    String splitedDate[] = expense.getDate().split("/");
                    int month = Integer.parseInt(splitedDate[1]);

                    switch (month){
                        case 1:
                            jan++;
                            break;
                        case 2:
                            feb++;
                            break;
                        case 3:
                            mar++;
                            break;
                        case 4:
                            apr++;
                            break;
                        case 5:
                            may++;
                            break;
                        case 6:
                            jun++;
                            break;
                        case 7:
                            jul++;
                            break;
                        case 8:
                            aug++;
                            break;
                        case 9:
                            sep++;
                            break;
                        case 10:
                            oct++;
                            break;
                        case 11:
                            nov++;
                            break;
                        case 12:
                            dic++;
                    }
                }

                ArrayList<String> labels = new ArrayList<>();
                labels.add("Jan");
                labels.add("Feb");
                labels.add("Mar");
                labels.add("Apr");
                labels.add("May");
                labels.add("Jun");
                labels.add("Jul");
                labels.add("Aug");
                labels.add("Sep");
                labels.add("Oct");
                labels.add("Nov");
                labels.add("Dec");

                ArrayList<BarEntry> entries = new ArrayList<>();
                entries.add(new BarEntry(jan, 0));
                entries.add(new BarEntry(feb, 1));
                entries.add(new BarEntry(mar, 2));
                entries.add(new BarEntry(apr, 3));
                entries.add(new BarEntry(may, 4));
                entries.add(new BarEntry(jun, 5));
                entries.add(new BarEntry(jul, 6));
                entries.add(new BarEntry(aug, 7));
                entries.add(new BarEntry(sep, 8));
                entries.add(new BarEntry(oct, 9));
                entries.add(new BarEntry(nov, 10));
                entries.add(new BarEntry(dic, 11));

                BarDataSet bardataset = new BarDataSet(entries, "Expenses by month");

                BarData data = new BarData(labels, bardataset);
                barChart.setData(data); // setting the data and list of lables into chart

                // barChart.setDescription(""); // the description

                bardataset.setColors(ColorTemplate.LIBERTY_COLORS);
                barChart.animateY(3000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Integer getColorForCategory(String theCategory) {
        HashMap<String, Integer> categoryColor = new HashMap<>();

        categoryColor.put("Child Care", Color.parseColor("#82b1ff"));
        categoryColor.put("Dining", Color.parseColor("#80d8ff"));
        categoryColor.put("Education", Color.parseColor("#84ffff"));
        categoryColor.put("Entertainment", Color.parseColor("#a7ffeb"));
        categoryColor.put("Gift", Color.parseColor("#b9f6ca"));
        categoryColor.put("Health & Fitness", Color.parseColor("#ccff90"));
        categoryColor.put("House Repair", Color.parseColor("#f4ff81"));
        categoryColor.put("Insurance", Color.parseColor("#ffff8d"));
        categoryColor.put("Loan", Color.parseColor("#ffe57f"));
        categoryColor.put("Medical", Color.parseColor("#ffd180"));
        categoryColor.put("Miscellaneous", Color.parseColor("#ff9e80"));
        categoryColor.put("Rent", Color.parseColor("#ff8a80"));
        categoryColor.put("Shopping", Color.parseColor("#ff80ab"));
        categoryColor.put("Tax", Color.parseColor("#ea80fc"));
        categoryColor.put("Transport", Color.parseColor("#b388ff"));
        categoryColor.put("Utilities", Color.parseColor("#8c9eff"));

        if(!categoryColor.containsKey(theCategory))
            return Color.BLACK;

        return categoryColor.get(theCategory);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.item_menu1);
        if(item!=null)
            item.setVisible(false);
    }

}