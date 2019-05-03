

package com.example.iexpens.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.iexpens.R;
import com.example.iexpens.fragments.WalletFragment;
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
    private DatabaseReference databaseTransactions;
    private DatabaseReference databaseCash;
    private DatabaseReference databaseWallet;
    public static final String EXPENSE_CASH_ID = "cashid";
    public static final String EXPENSE_CASH_TITLE = "cashtitle";
    public static final String EXPENSE_CASH_AMOUNT = "cashamount";

    public static final String EXPENSE_BANK_ID = "bankid";
    public static final String EXPENSE_BANK_AMOUNT = "bankamount";
    public static final String EXPENSE_BANK_NO = "bankaccountno";
    public static final String EXPENSE_BANK_NAME = "bankaccountname";
    public static final String EXPENSE_BANK_BANKS = "bankaccounts";
    public static final String EXPENSE_BANK_TYPE = "bankaccounttype";



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

        Intent intent = getIntent();
        String cashtitle = intent.getStringExtra(CashWalletScreen.WALLET_CASH_TITLE);
        String cashId = intent.getStringExtra(CashWalletScreen.WALLET_CASH_ID);
        String cash = intent.getStringExtra(CashWalletScreen.WALLET_CASH_AMOUNT);

        String bankId = intent.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_ID);
        String acc_no = intent.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_NO);
        String bank_amt = intent.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_AMOUNT);

        Log.e(TAG, "bankid" + bankId);
        Log.e(TAG, "cashid" + cashId);

        String cash_Id = intent.getStringExtra(Category.CATEGORY_CASH_ID);
        String bank_Id = intent.getStringExtra(Category.CATEGORY_BANK_ID);

        //  databaseExpenses = FirebaseDatabase.getInstance().getReference(mUserId).child("expenses");

        if (cash_Id != null) {
            databaseExpenses = FirebaseDatabase.getInstance().getReference().child(mUserId).child("Expenses").child("WALLET").child(cash_Id);
            databaseTransactions = FirebaseDatabase.getInstance().getReference(mUserId).child("Wallet Transactions").child("WALLET").child(cash_Id);
            databaseCash = FirebaseDatabase.getInstance().getReference().child(mUserId).child("WALLET").child(cash_Id);
        } else {
            databaseExpenses = FirebaseDatabase.getInstance().getReference(mUserId).child("Expenses").child("Bank Accounts").child(bank_Id);
            databaseTransactions = FirebaseDatabase.getInstance().getReference(mUserId).child("Bank Transactions").child("Bank Accounts").child(bank_Id);
            databaseWallet = FirebaseDatabase.getInstance().getReference().child(mUserId).child("Bank Accounts").child(bank_Id);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        selectCategory = (TextView) findViewById(R.id.textView1);
        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategoryForExpense(v);
            }
        });
        // Receiving value into activity using intent.
        String TempHolder = getIntent().getStringExtra("ListViewClickedValue");
        // Setting up received value into EditText.
        selectCategory.setText(TempHolder);

        textPrice = (EditText) findViewById(R.id.txtPrice);
        textDate = findViewById(R.id.txtDate);
        datePicker_expense = (Button) findViewById(R.id.button_date);
        textDescription = (EditText) findViewById(R.id.txtDescription);
        buttonAdd = (Button) findViewById(R.id.button);


        datePicker_expense.setOnClickListener(new View.OnClickListener() {
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
//                addToCloudStorage();
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
        DatabaseReference drExpense = FirebaseDatabase.getInstance().getReference(mUserId).child("Expenses").child(expenseId);

        drExpense.removeValue();
        Toast.makeText(this, "Expense is deleted", Toast.LENGTH_LONG).show();
    }


    private boolean updateExpense(String id, String category, String price, String date, String description) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(mUserId).child("Expenses").child(id);
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

        Intent intent = getIntent();
        String cashtitle = intent.getStringExtra(Category.CATEGORY_CASH_TITLE);
        String cashId = intent.getStringExtra(Category.CATEGORY_CASH_ID);
        String cash = intent.getStringExtra(Category.CATEGORY_CASH_AMOUNT);
        String cashamt = "";

        String bankId = intent.getStringExtra(Category.CATEGORY_BANK_ID);
        String acc_no = intent.getStringExtra(Category.CATEGORY_BANK_NO);
        String bank_amt = intent.getStringExtra(Category.CATEGORY_BANK_AMOUNT);
        String accname = intent.getStringExtra(Category.CATEGORY_BANK_NAME);
        String bank = intent.getStringExtra(Category.CATEGORY_BANK_BANKS);
        String bank_type = intent.getStringExtra(Category.CATEGORY_BANK_TYPE);

        String bankamt = "";

        if (!TextUtils.isEmpty(price)) {

            if (cashId != null) {
                if (Integer.parseInt(cash) < Integer.parseInt(price)) {
                    Toast.makeText(this, "There is no sufficient amount in Wallet", Toast.LENGTH_LONG).show();
                } else {
                    int value = Integer.parseInt(cash) - Integer.parseInt(price);
                    Log.d("Print", "print1" + cashId + cashtitle + value);
                    cashamt = "" + value;
                    update_cashamount(cashId, cashtitle, cashamt);
                    update_cashtransaction(price);

                    String id = databaseExpenses.push().getKey();
                    Expense expense = new Expense(id, category, price, date, description);
                    Log.d("Print", "print4" + id + category + price + date);
                    databaseExpenses.child(id).setValue(expense);
                    Toast.makeText(this, "Expense added", Toast.LENGTH_LONG).show();
                }
            } else {
                if (Integer.parseInt(bank_amt) < Integer.parseInt(price)) {
                    Toast.makeText(this, "There is no sufficient amount in the account", Toast.LENGTH_LONG).show();
                } else {
                    int value = Integer.parseInt(bank_amt) - Integer.parseInt(price);
                    bankamt = "" + value;
                    update_bankamount(bankId, acc_no, accname, bankamt, bank, bank_type);
                    update_banktransaction(price);

                    String id = databaseExpenses.push().getKey();
                    Expense expense = new Expense(id, category, price, date, description);
                    Log.d("Print", "print4" + id + category + price + date);
                    databaseExpenses.child(id).setValue(expense);
                    Toast.makeText(this, "Expense added", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            Toast.makeText(this, "Category and Price are Mandatory!!", Toast.LENGTH_LONG).show();
        }
    }

    private void update_cashamount(String cashId, String title, String cashAmount) {
        CashWallet cashWallet = new CashWallet(cashId, title, cashAmount);
        Log.d("Print", "print2" + cashId + title);
        databaseCash.setValue(cashWallet);
    }

    private void update_cashtransaction(String price) {

        String trans_id = databaseTransactions.push().getKey();

        SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("dd-MM-yyyy' T 'HH:mm:sss");

        String current_date = ISO_8601_FORMAT.format(new Date());
        String trans_type = getString(R.string.trans_debit);
        String amt = price;

        Log.d("Print", "print3" + current_date + trans_type + trans_id + amt);

        Transactions transactions = new Transactions(trans_id, current_date, trans_type, amt);
        databaseTransactions.child(trans_id).setValue(transactions);
    }

    private void update_bankamount(String bankId, String acc_no, String acc_name, String acc_amount, String banks, String acc_type) {
        BankAccount bankAccount = new BankAccount(bankId, acc_no, acc_name, acc_amount, banks, acc_type);
        databaseWallet.setValue(bankAccount);
    }

    private void update_banktransaction(String price) {
        String trans_id = databaseTransactions.push().getKey();

        SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("dd-MM-yyyy' T 'HH:mm:sss");

        String current_date = ISO_8601_FORMAT.format(new Date());
        String trans_type = getString(R.string.trans_debit);
        String amt = price;

        Transactions transactions = new Transactions(trans_id, current_date, trans_type, amt);
        databaseTransactions.child(trans_id).setValue(transactions);
    }

    /**
     * Thr method selectCategoryForExpense is used to move to category page
     *
     * @param view
     */
    public void selectCategoryForExpense(View view) {
        Intent intent = new Intent(AddExpenseActivity.this, Category.class);

        Intent intent1 = getIntent();

        String cashid = intent1.getStringExtra(CashWalletScreen.WALLET_CASH_ID);
        String title = intent1.getStringExtra(CashWalletScreen.WALLET_CASH_TITLE);
        String camount = intent1.getStringExtra(CashWalletScreen.WALLET_CASH_AMOUNT);

        String bankid = intent1.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_ID);
        String accno = intent1.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_NO);
        String accname = intent1.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_NAME);
        String bamount = intent1.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_AMOUNT);
        String bank = intent1.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_BANKS);
        String bank_type = intent1.getStringExtra(AccountScreenActivity.ACCOUNT_BANK_TYPE);

        if (cashid != null) {
            intent.putExtra(EXPENSE_CASH_ID, cashid);
            intent.putExtra(EXPENSE_CASH_TITLE, title);
            intent.putExtra(EXPENSE_CASH_AMOUNT, camount);
        } else {
            intent.putExtra(EXPENSE_BANK_ID, bankid);
            intent.putExtra(EXPENSE_BANK_NO, accno);
            intent.putExtra(EXPENSE_BANK_NAME, accname);
            intent.putExtra(EXPENSE_BANK_AMOUNT, bamount);
            intent.putExtra(EXPENSE_BANK_BANKS, bank);
            intent.putExtra(EXPENSE_BANK_TYPE, bank_type);
        }
        startActivity(intent);
    }

}
