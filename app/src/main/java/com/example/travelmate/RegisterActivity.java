package com.example.travelmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travelmate.utility.util;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    EditText etEmail, etPassword;
    Button btnRegister;
    String Email, Password;
    TextView btnLogin;
    FirebaseAuth mAuth;
    public final int REQUEST_CODE = 11;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInButton btngooglesignin;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog, progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findIds();
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(this);
        btngooglesignin.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog1 = new ProgressDialog(this);
        googleSignIn();
    }

    private void googleSignIn() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.serverid))
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void findIds() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btngooglesignin = findViewById(R.id.btngooglesignin);
        btnLogin = findViewById(R.id.btnLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                onRegister(v);
                break;
            case R.id.btngooglesignin:
                onGoogleSignIn();
                break;
            case R.id.btnLogin:
                onLogin();
        }

    }

    private void onLogin() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void onGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE);
    }

    private void onRegister(View v) {
        progressDialog.setMessage(getResources().getString(R.string.registering));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.emailempty));
            etEmail.setFocusable(true);
            progressDialog.dismiss();
            return;
        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.passwordempty));
            etPassword.setFocusable(true);
            progressDialog.dismiss();
        } else {
            createWithFirebase(v);
        }
    }

    private void createWithFirebase(final View v) {
        if (!isEmailCorrect() || etEmail.getText().toString().length() < 10) {
            etEmail.setError(getString(R.string.emailerrormsg));
            etEmail.setFocusable(true);
            progressDialog.dismiss();
        } else if (!isPassCorrect() || etPassword.getText().toString().length() < 6) {
            etPassword.setError(getString(R.string.passerrormsg));
            etPassword.setFocusable(true);
            progressDialog.dismiss();
        } else {
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        OnCompleteregisterUser();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void OnCompleteregisterUser() {
        progressDialog.dismiss();
        startActivity(new Intent(getApplicationContext(), Register2Activity.class)
                .putExtra("email", etEmail.getText().toString())
                .putExtra("password", etPassword.getText().toString()));
        finish();
        onCLear();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        progressDialog1.dismiss();
        util.toast(getApplicationContext(), "Connection Failed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestcode", String.valueOf(resultCode));
        if (requestCode == REQUEST_CODE) {
            progressDialog1.setMessage("Wait.. Google Sign Up");
            progressDialog1.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

                Log.e("Exception", e.getMessage());
            }
        } else {
            progressDialog1.dismiss();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String useruid = user.getUid();
                            checkForUserInDatabase(useruid);
                        } else {
                            Log.e("error", "error");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog1.dismiss();
                util.toast(getApplicationContext(), e.getMessage());
            }
        });
    }

    private void checkForUserInDatabase(final String useruid) {
        FirebaseDatabase.getInstance().getReference().child("User Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    if (dataSnapshot.hasChild(useruid)) {
                        progressDialog1.dismiss();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        progressDialog1.dismiss();
                        startActivity(new Intent(getApplicationContext(), Register2Activity.class));
                        finish();
                    }
                } else {
                    progressDialog1.dismiss();
                    startActivity(new Intent(getApplicationContext(), Register2Activity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                progressDialog1.dismiss();
                util.toast(getApplicationContext(), databaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    public void onCLear() {
        etEmail.setText("");
        etPassword.setText("");
    }

}
