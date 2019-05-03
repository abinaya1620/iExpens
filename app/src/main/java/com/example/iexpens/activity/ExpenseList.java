package com.example.iexpens.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.iexpens.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseList extends ArrayAdapter {
    private Activity context;
    private List<Expense> expenseList;
    private static final String TAG = "ExpenseList";
    private FirebaseAuth mAuth;
    private String mUserId;



    public ExpenseList(Activity context, List<Expense> expenseList){
        super(context, R.layout.expense_list_layout,expenseList);
        this.context = context;
        this.expenseList = expenseList;
    }


    @NotNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.expense_list_layout, null, true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mUserId = user.getUid();



        TextView textViewCaterogy = (TextView) listViewItem.findViewById(R.id.textViewCaterogy);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewPrice);
        final ImageView imageView = listViewItem.findViewById(R.id.camera_image);

        final Expense expense = expenseList.get(position);
        textViewCaterogy.setText(expense.getExpenseCategory());
        textViewPrice.setText(expense.getPrice());




     listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d (TAG,"ImageAddress" +"" +expense.getImageAddress());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(expense.getImageAddress());
                Log.d(TAG,"StorageRef "+ ""+"....." +"" +storageRef);



            }



     });



        return listViewItem;
    }






}
