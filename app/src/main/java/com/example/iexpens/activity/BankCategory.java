package com.example.iexpens.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.iexpens.R;
import com.example.iexpens.adapter.BankGridViewAdapter;
import com.example.iexpens.adapter.BankListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class BankCategory extends AppCompatActivity {

    private ViewStub stub_bankgrid;
    private ViewStub stub_banklist;
    private ListView listView;
    private GridView gridView;
    private BankListViewAdapter bankListViewAdapter;
    private BankGridViewAdapter bankGridViewAdapter;
    private List<Banks> banksList;
    private int currentViewMode=0;

    static final int VIEW_MODE_LISTVIEW =0;
    static final int VIEW_MODE_GRIDVIEW =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_category);

        stub_bankgrid = findViewById(R.id.stub_bankgrid);
        stub_banklist= findViewById(R.id.stub_banklist);

        //Inflate ViewStub before get view
        stub_bankgrid.inflate();
        stub_banklist.inflate();

        listView= findViewById(R.id.mylist_view);
        gridView= findViewById(R.id.gridview);

        //get list of banks
        getBankList();

        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode",MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode",VIEW_MODE_LISTVIEW); //Default is viw listView

        //To save listitem

        //Register item click
        listView.setOnItemClickListener(onItemClick);
        gridView.setOnItemClickListener(onItemClick);

        switchView();

    }

    private List<Banks> getBankList() {
        banksList = new ArrayList<>();
        banksList.add(new Banks(R.drawable.seb,"SEB"));
        banksList.add(new Banks(R.drawable.nordea,"Nordea"));
        banksList.add(new Banks(R.drawable.dnb,"DNB"));
        banksList.add(new Banks(R.drawable.nordnet,"Nordnet"));
        banksList.add(new Banks(R.drawable.swedbank,"Swedbank"));
        return  banksList;
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Action after clicking item
            // Getting listview click value into String variable.
            String TempListViewClickedValue =banksList.get(position).getTitle();
           Intent intent = new Intent(BankCategory.this, AddAccountActivity.class);
            intent.putExtra("ListViewClickedValue", TempListViewClickedValue);
            startActivity(intent);
        }
    };

    private void switchView() {
        if(VIEW_MODE_LISTVIEW == currentViewMode)
        {
            //Display ListView
            stub_banklist.setVisibility(View.VISIBLE);
            //Hide GridView
            stub_bankgrid.setVisibility(View.GONE);
        }
        else
        {
            //Gide ListView
            stub_banklist.setVisibility(View.GONE);
            //Display GridView
            stub_bankgrid.setVisibility(View.VISIBLE);
        }

        setAdapters();
    }

    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == currentViewMode)
        {
            bankListViewAdapter= new BankListViewAdapter(this,R.layout.banklist_item,banksList);
            listView.setAdapter(bankListViewAdapter);

        }else{
            bankGridViewAdapter = new BankGridViewAdapter(this,R.layout.bankgrid_item,banksList);
            gridView.setAdapter(bankGridViewAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_menu1:
                if(VIEW_MODE_LISTVIEW == currentViewMode){
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                }
                else {
                    currentViewMode= VIEW_MODE_LISTVIEW;
                }

                //switchView
                switchView();
                //save view mode in shared reference

                SharedPreferences sharedPreferences = getSharedPreferences("ViewMode",MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putInt("currentViewMode",currentViewMode);
                editor.commit();

                break;
        }
        return true;
    }
}
