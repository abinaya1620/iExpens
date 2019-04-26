package com.example.iexpens.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.example.iexpens.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    private Spinner spinner;
    private String[] category;

    EditText textPrice;
    EditText textDate;
    EditText textDescription;
    Button buttonAdd;

    DatabaseReference databaseExpenses;

    ListView listViewExpenses;

    List<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        databaseExpenses = FirebaseDatabase.getInstance().getReference("expenses");
        category = getResources().getStringArray(R.array.category);

        spinner = (Spinner) findViewById(R.id.spinnerCategory);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), category[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textPrice = (EditText) findViewById(R.id.txtPrice);
        textDate = (EditText) findViewById(R.id.txtDate);
        textDescription = (EditText) findViewById(R.id.txtDescription);
        buttonAdd = (Button) findViewById(R.id.button);
        listViewExpenses = (ListView) findViewById(R.id.listViewExpense);
        expenseList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        listViewExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Expense expense = expenseList.get(position);

                showUpdateDialog(expense.getExpenseId(),expense.getExpenseCategory());
                return false;
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

                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()){
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

    private void showUpdateDialog(final String expenseId, final String expenseCategory){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = getLayoutInflater();

        final View dialogView = layoutInflater.inflate(R.layout.update_expense_dialog, null);

        dialogBuilder.setView(dialogView);

        final TextView textView1 = (TextView) dialogView.findViewById(R.id.textView1);
        final Spinner spinnerCategory1 = (Spinner) dialogView.findViewById(R.id.spinnerCategory1);
        final EditText editTextPrice = (EditText) dialogView.findViewById(R.id.editTextPrice);
        final EditText editTextDate = (EditText) dialogView.findViewById(R.id.editTextDate);
        final EditText editTextDescription = (EditText) dialogView.findViewById(R.id.editTextDescription);
        final Button buttonUpdateExpense = (Button) dialogView.findViewById(R.id.buttonUpdateExpense);

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

                if(TextUtils.isEmpty(price)||TextUtils.isEmpty(category)||TextUtils.isEmpty(date)||TextUtils.isEmpty(description)){
                    editTextPrice.setError("Please fill the details");
                    return;
                }
                updateExpense(expenseId, category, price, date, description);

                alertDialog.dismiss();

            }
        });
    }

    private boolean updateExpense(String id, String category, String price, String date, String description){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("expenses").child(id);
        Expense expense = new Expense(id, category, price, date, description);

        databaseReference.setValue(expense);

        Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_LONG).show();

        return true;
    }

    private void addExpense(){
        String category = spinner.getSelectedItem().toString();
        String price = textPrice.getText().toString();
        String date = textDate.getText().toString();
        String description = textDescription.getText().toString();

        if(!TextUtils.isEmpty(price)){
            String id = databaseExpenses.push().getKey();
            Expense expense = new Expense(id, category, price, date, description );
            databaseExpenses.child(id).setValue(expense);
            Toast.makeText(this, "Expense added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Please select the Expense category and enter the price",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Thr method selectCategoryForExpense is used to move to category page
     * @param view

    public void selectCategoryForExpense(View view) {
        Intent intent = new Intent(AddExpenseActivity.this, Category.class);
        startActivity(intent);
    }
     */
}
