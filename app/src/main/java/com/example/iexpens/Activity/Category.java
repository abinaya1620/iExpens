package com.example.iexpens.Activity;

import com.example.iexpens.Adapter.GridViewAdapater;
import com.example.iexpens.Adapter.ListViewAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;



import com.example.iexpens.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapater gridViewAdapater;
    private List<Product> categoryList;
    private int currentViewMode=0;

    static final int VIEW_MODE_LISTVIEW =0;
    static final int VIEW_MODE_GRIDVIEW =1;

    private static final String TAG = "Category";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"Inside on create of Category");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        stubList = findViewById(R.id.stub_list);
        stubGrid = findViewById(R.id.stub_grid);

        //Inflate ViewStub before get view
        stubList.inflate();
        stubGrid.inflate();

        listView= findViewById(R.id.mylist_view);
        gridView= findViewById(R.id.gridview);

        //get list of category
        getCategoryList();



        //get current view mode in shared references
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode",MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode",VIEW_MODE_LISTVIEW); //Default is viw listView

        //To save listitem

        //Register item click
        listView.setOnItemClickListener(onItemClick);
        gridView.setOnItemClickListener(onItemClick);

           switchView();
    }

    private void switchView()
    {
    if(VIEW_MODE_LISTVIEW == currentViewMode)
    {
        //Display ListView
        stubList.setVisibility(View.VISIBLE);
        //Hide GridView
        stubGrid.setVisibility(View.GONE);
    }
    else
    {
        //Gide ListView
        stubList.setVisibility(View.GONE);
        //Display GridView
        stubGrid.setVisibility(View.VISIBLE);
    }

   setAdapters();
    }

    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == currentViewMode)
        {
            listViewAdapter= new ListViewAdapter(this,R.layout.list_item,categoryList);
            listView.setAdapter(listViewAdapter);

        }else{
            gridViewAdapater = new GridViewAdapater(this,R.layout.grid_item,categoryList);
            gridView.setAdapter(gridViewAdapater);
        }
    }


    /**
     * The method getCategoryList is used to get the list of category
     * @return the categoryList
     */
    private List<Product> getCategoryList() {
         categoryList = new ArrayList<>();
        categoryList.add(new Product(R.drawable.childcare,"Child Care"));
        categoryList.add(new Product(R.drawable.dining,"Dining"));
        categoryList.add(new Product(R.drawable.education,"Education"));
        categoryList.add(new Product(R.drawable.entertainment,"Entertainment"));
        categoryList.add(new Product(R.drawable.gift,"Gift"));
        categoryList.add(new Product(R.drawable.fitness,"Health & Fitness"));
        categoryList.add(new Product(R.drawable.repair,"House Repair"));
        categoryList.add(new Product(R.drawable.insurance,"Insurance"));
        categoryList.add(new Product(R.drawable.loan,"Loan"));
        categoryList.add(new Product(R.drawable.medical,"Medical"));
        categoryList.add(new Product(R.drawable.misc,"Miscellaneous"));
        categoryList.add(new Product(R.drawable.rent,"Rent"));
        categoryList.add(new Product(R.drawable.shopping,"Shopping"));
        categoryList.add(new Product(R.drawable.tax,"Tax"));
        categoryList.add(new Product(R.drawable.transport,"Transport"));
        categoryList.add(new Product(R.drawable.utilities,"Utilities"));
        return  categoryList;
    }

AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       //Action after clicking item
        Toast.makeText(getApplicationContext(), categoryList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
};



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

