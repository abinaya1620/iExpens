

package com.example.iexpens.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.iexpens.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class AddExpenseActivity extends AppCompatActivity {

    private String[] category;
    private TextView selectCategory;
    private EditText textPrice;
    private EditText textDescription;
    private Button buttonAdd;
    private TextView textDate;
    private Button datePicker_expense;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    DatabaseReference databaseExpenses;
    ListView listViewExpenses;
    List<Expense> expenseList;
   private String imageAddr;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private ImageView cameraImage;
    private String pictureFilePath;
    private FirebaseStorage firebaseStorage;
    private ImageButton captureButton;
    private StorageReference uploadeRef;
    static String tempUrl;



    private FirebaseAuth mAuth;
    private String mUserId;



    private static final String TAG = "AddExpense";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        cameraImage = findViewById(R.id.cameraImage);
        captureButton = findViewById(R.id.ImageButtonCamera);
        captureButton.setOnClickListener(capture);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            captureButton.setEnabled(false);
        }

        firebaseStorage = FirebaseStorage.getInstance();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mUserId = user.getUid();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        databaseExpenses = FirebaseDatabase.getInstance().getReference(mUserId).child("expenses");


        selectCategory = (TextView) findViewById(R.id.textView1);
        // Receiving value into activity using intent.
        String TempHolder = getIntent().getStringExtra("ListViewClickedValue");
        // Setting up received value into EditText.
        selectCategory.setText(TempHolder);

        textPrice = (EditText) findViewById(R.id.txtPrice);
        textDate = findViewById(R.id.txtDate);
        textDescription = (EditText) findViewById(R.id.txtDescription);
        buttonAdd = (Button) findViewById(R.id.button);


        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(AddExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                textDate.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        });


        listViewExpenses = (ListView) findViewById(R.id.listViewExpense);
        expenseList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCloudStorage();
                addExpense();

            }
        });


        listViewExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Expense expense = expenseList.get(position);

                showUpdateDialog(expense.getExpenseId(), expense.getExpenseCategory());

                return false;
            }
        });


    }


    private View.OnClickListener capture = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                sendTakePictureIntent();
            }
        }
    };

    private void sendTakePictureIntent() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.iexpens.fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "IEXPENS" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()) {
                cameraImage.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }



    private void addToCloudStorage() {
        File f = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(f);
        final String cloudFilePath = picUri.getLastPathSegment();


        StorageReference storageRef = firebaseStorage.getReference(mUserId);
        uploadeRef = storageRef.child("iExpens").child(cloudFilePath);
        Log.d(TAG, "cloudfilepath :" + "" + cloudFilePath);

        uploadeRef.putFile(picUri).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Failed to upload picture to cloud storage");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                uploadeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        uri.toString();
                        Log.d(TAG, "onSuccess: uri= " + uri.toString());
                        Toast.makeText(AddExpenseActivity.this,
                                "Image has been uploaded to cloud storage",
                                Toast.LENGTH_SHORT).show();
                        tempUrl = uri.toString();
                    }
                });
            }
        });

    }





    @Override
    protected void onStart() {

        super.onStart();
        databaseExpenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseList.clear();

                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);


                    expenseList.add(expense);
                }
                ExpenseList adapter = new ExpenseList(AddExpenseActivity.this, expenseList);
                listViewExpenses.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String expenseId, final String expenseCategory) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.update_expense_dialog, null, true);
        dialogBuilder.setView(dialogView);
        final TextView textView1 = (TextView) dialogView.findViewById(R.id.textView1_update);
        final Spinner spinnerCategory1 = (Spinner) dialogView.findViewById(R.id.spinnerCategory1);
        final EditText editTextPrice = (EditText) dialogView.findViewById(R.id.editTextPrice);
        final EditText editTextDate = dialogView.findViewById(R.id.textDate_update);
        final EditText editTextDescription = (EditText) dialogView.findViewById(R.id.editTextDescription);
        final Button buttonUpdateExpense = (Button) dialogView.findViewById(R.id.buttonUpdateExpense);
        final Button buttonExpenseDelete = (Button) dialogView.findViewById(R.id.buttonExpenseDelete);


        category = getResources().getStringArray(R.array.category);


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerCategory1.setAdapter(adapter);
        spinnerCategory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), category[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        dialogBuilder.setTitle("Updating Expense " + expenseCategory);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = spinnerCategory1.getSelectedItem().toString();
                String price = editTextPrice.getText().toString();
                String date = editTextDate.getText().toString();
                String description = editTextDescription.getText().toString();

                if (TextUtils.isEmpty(price) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description)) {
                    editTextPrice.setError("Fields are Mandatory!!");
                    return;
                }
                updateExpense(expenseId, category, price, date, description);

                alertDialog.dismiss();

            }
        });
        buttonExpenseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteExpense(expenseId);
                alertDialog.dismiss();
            }
        });
    }

    private void deleteExpense(String expenseId) {
        DatabaseReference drExpense = FirebaseDatabase.getInstance().getReference(mUserId).child("expenses").child(expenseId);

        drExpense.removeValue();
        Toast.makeText(this, "Expense is deleted", Toast.LENGTH_LONG).show();
    }


    private boolean updateExpense(String id, String category, String price, String date, String description) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(mUserId).child("expenses").child(id);
        Expense expense = new Expense(id, category, price, date, description);

        databaseReference.setValue(expense);

        Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_LONG).show();

        return true;
    }

    private void addExpense() {
        String category = selectCategory.getText().toString();
        String price = textPrice.getText().toString();
        String date = textDate.getText().toString();
        String description = textDescription.getText().toString();


        if (!TextUtils.isEmpty(price)) {
            String id = databaseExpenses.push().getKey();



            Expense expense = new Expense(id, category, price, date, description,imageAddr);
            databaseExpenses.child(id).setValue(expense);
            Toast.makeText(this, "Expense added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Category and Price are Mandatory!!", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Thr method selectCategoryForExpense is used to move to category page
     *
     * @param view
     */
    public void selectCategoryForExpense(View view) {
        Intent intent = new Intent(AddExpenseActivity.this, Category.class);
        startActivity(intent);
    }


}
