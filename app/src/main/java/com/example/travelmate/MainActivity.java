package com.example.travelmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText etEmail, etPass;
    Button btnLogin, btnRegister;
    String Email, Password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        progressDialog = new ProgressDialog(this);
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
                onLogin(v);
                break;
            case R.id.btnRegister:
                onRegister();
                break;
        }

    }

    private void onRegister() {

        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

    }

    private void onLogin(View v) {
        progressDialog.setMessage(getString(R.string.loggingprogress));

        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.emailempty));
            etEmail.setFocusable(true);
            return;
        }

        if (etPass.getText().toString().isEmpty()) {
            etPass.setError(getString(R.string.passwordempty));
            etPass.setFocusable(true);
            return;
        }


        checkLoginDetails(v);
    }

    private void checkLoginDetails(final View v) {


        if (!isEmailCorrect() || etEmail.getText().toString().length() < 8) {
            etEmail.setError(getString(R.string.emailerrormsg));
            etEmail.setFocusable(true);
        } else if (!isPassCorrect() || etPass.getText().toString().length() < 6) {
            etPass.setError(getString(R.string.passerrormsg));
            etPass.setFocusable(true);
        } else {
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        onLoginSuccessFull();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    util.toast(getApplicationContext(), e.getMessage());
                }
            });
        }
    }

    private boolean isPassCorrect() {
        Pattern pattern;
        Matcher matcher;
        String password_matcher = getString(R.string.regesxpassword);
        pattern = Pattern.compile(password_matcher);
        matcher = pattern.matcher(etPass.getText().toString());
        return matcher.matches();
    }

    private boolean isEmailCorrect() {


        if (Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            return true;
        } else {
            return false;
        }


    }

    private void onLoginSuccessFull() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        progressDialog.dismiss();
    }
}
