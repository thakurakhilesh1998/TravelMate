package com.example.travelmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.travelmate.utility.util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail,etPassword;
    Button btnRegister;
    String Email,Password;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findIds();
        mAuth=FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(this);

    }

    private void findIds() {

        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnRegister=findViewById(R.id.btnRegister);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnRegister:
                onRegister();
                break;
        }

    }

    private void onRegister() {

        Email=etEmail.getText().toString().trim();
        Password=etPassword.getText().toString().trim();
        createWithFirebase(Email,Password);
    }

    private void createWithFirebase(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           OnCompleteregisterUser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                util.toast(getApplicationContext(),e.getMessage());
            }
        });
    }

    private void OnCompleteregisterUser() {

        startActivity(new Intent(getApplicationContext(),Register2Activity.class));
    }


}
