package com.example.iexpens.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iexpens.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;
    private Button btnSignUp;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mCashKeyReference;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();



        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        mDialog = new ProgressDialog(this);

        etEmail = findViewById(R.id.email_reg);
        etPassword = findViewById(R.id.password_reg);

        btnSignIn = findViewById(R.id.btnsignin_reg);
        btnSignUp = findViewById(R.id.btnsignup_reg);

        // for complete login
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        // for sign up page
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                boolean emailAndPasswordNotEmpty = true;

                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Required Field..");
                    emailAndPasswordNotEmpty = false;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Required Field..");
                    emailAndPasswordNotEmpty = false;
                }



                if(emailAndPasswordNotEmpty){

                    mDialog.setMessage("Processing..");
                    mDialog.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    mDialog.dismiss();
                                    if(task.isSuccessful()){
                                        // wallet creation

                                       // FirebaseUser user = mAuth.getCurrentUser();
                                       // String userId = user.getUid();


                                      //  mCashKeyReference = mDatabase.getReference().child(userId).child("cash2");


                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Please, try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
