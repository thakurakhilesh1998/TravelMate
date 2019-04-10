package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button btnRegister;
    String Email, Password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findIds();
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(this);

    }

    private void findIds() {


        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                onRegister(v);
                break;
        }

    }

    private void onRegister(View v) {


        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.emailempty));
            etEmail.setFocusable(true);
            return;
        }
        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.passwordempty));
            etPassword.setFocusable(true);
        }
        createWithFirebase(v);
    }

    private void createWithFirebase(final View v) {

        if (!isEmailCorrect() || etEmail.getText().toString().length() < 10) {
            etEmail.setError(getString(R.string.emailerrormsg));
            etEmail.setFocusable(true);
        } else if (!isPassCorrect() || etPassword.getText().toString().length() < 6) {
            etPassword.setError(getString(R.string.passerrormsg));
            etPassword.setFocusable(true);
        } else {
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    OnCompleteregisterUser();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void OnCompleteregisterUser() {

        startActivity(new Intent(getApplicationContext(), Register2Activity.class));
    }


    private boolean isEmailCorrect() {


        if (Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            return true;
        } else {
            return false;
        }


    }

    private boolean isPassCorrect() {
        Pattern pattern;
        Matcher matcher;
        String password_matcher = getString(R.string.regesxpassword);
        pattern = Pattern.compile(password_matcher);
        matcher = pattern.matcher(etPassword.getText().toString());
        return matcher.matches();
    }


}
