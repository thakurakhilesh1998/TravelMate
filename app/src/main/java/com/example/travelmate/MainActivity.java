package com.example.travelmate;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    EditText etEmail, etPass;
    Button btnLogin;
    final int REQUEST_CODE = 12;
    String Email, Password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    TextView btnRegister, tvforgotPass;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInButton googlesigninbutton;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog2;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        findIds();
        googleSignInOption();
        progressDialog = new ProgressDialog(this);
        progressDialog2 = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        googlesigninbutton.setOnClickListener(this);
        tvforgotPass.setOnClickListener(this);
    }

    private void googleSignInOption() {
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
        etPass = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        googlesigninbutton = findViewById(R.id.googlesigninButton);
        tvforgotPass = findViewById(R.id.tvforgotPass);
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
            case R.id.googlesigninButton:
                onGoogleSignClicked();
                break;
            case R.id.tvforgotPass:
                onForgotPassword();
        }

    }

    private void onForgotPassword() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.resetpass);
        final EditText etEmail = dialog.findViewById(R.id.etEmail);
        Button btnResetPass = dialog.findViewById(R.id.btnResetPass);
        final ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        final TextView error = dialog.findViewById(R.id.error);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()) {

                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(etEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Password Reset Link Sent to your Email", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            error.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

    }

    private void onGoogleSignClicked() {
        progressDialog2.setMessage("wait...");
        progressDialog2.setCanceledOnTouchOutside(false);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE);
        progressDialog2.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
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
                        }
                    }
                });


    }

    private void checkForUserInDatabase(final String useruid) {

        FirebaseDatabase.getInstance().getReference().child("User Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("msg", String.valueOf(dataSnapshot.hasChild(useruid)));
                if (dataSnapshot.hasChildren()) {
                    if (dataSnapshot.hasChild(useruid)) {

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), Register2Activity.class));
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), Register2Activity.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
