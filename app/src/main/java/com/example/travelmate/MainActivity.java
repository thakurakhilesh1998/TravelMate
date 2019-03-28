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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText etEmail, etPass;
    Button btnLogin, btnRegister;
    String Email, Password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
        findIds();

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void findIds() {

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
                onLogin();
                break;
            case R.id.btnRegister:
                onRegister();
                break;
        }

    }

    private void onRegister() {

        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

    }

    private void onLogin() {

        Email = etEmail.getText().toString().trim();
        Password = etPass.getText().toString().trim();
        checkLoginDetails(Email, Password);
    }

    private void checkLoginDetails(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onLoginSuccessFull();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                util.toast(getApplicationContext(), e.getMessage());
            }
        });
    }

    private void onLoginSuccessFull() {


        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
}
