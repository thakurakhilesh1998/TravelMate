package com.example.travelmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.travelmate.utility.util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register2Activity extends AppCompatActivity implements View.OnClickListener {

    int PICK_IMAGE = 1;
    ImageView ivProfile;
    EditText etName, etPhone, etAge;
    Button btnRegister;
    RadioGroup radioGroup;
    RadioButton rbMale, rbFemale;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6;
    String Name, Phone, Age, Gender;
    String interest1, interest2, interest3, interest4, interest5, interest6;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    Uri Filepath;

    SaveData saveData, saveData1;
    ProgressDialog progressDialog, progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        findIds();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        btnRegister.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
    }

    private void findIds() {

        ivProfile = findViewById(R.id.ivProfile);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAge = findViewById(R.id.etAge);
        btnRegister = findViewById(R.id.btnRegister);
        radioGroup = findViewById(R.id.radioGroup);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Registering");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnRegister:
                onRegister(v);
                break;
            case R.id.ivProfile:
                chooseImage();

        }

    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    private void onRegister(View v) {
        progressDialog1.show();
        findInterests();
        if (emptycheck(v)) ;
        {
            Name = etName.getText().toString().trim();
            Phone = etPhone.getText().toString().trim();
            Age = etAge.getText().toString().trim();
            Gender = findGender();

            saveDataOnFirebase();
        }


    }

    private boolean emptycheck(View v) {

        if (etName.getText().toString().isEmpty()) {
            etName.setError("Name field can not empty");
            etName.setFocusable(true);

        } else if (etAge.getText().toString().isEmpty()) {
            etAge.setError("Name field can not empty");
            etAge.setFocusable(true);


        } else if (etPhone.getText().toString().isEmpty()) {
            etPhone.setError("Phone can not be empty");
            etPhone.setFocusable(true);
        } else if (!cb1.isChecked() || !cb2.isChecked() || !cb3.isChecked() || !cb4.isChecked() || !cb5.isChecked()) {
            Snackbar.make(v, "Check At Least One interest", BaseTransientBottomBar.LENGTH_LONG).show();
        } else if (findGender() == null) {
            Snackbar.make(v, "Select your gender", BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }

    private void saveDataOnFirebase() {
        final String Email = mUser.getEmail();
        final String uid = mUser.getUid();
        mStorageRef.child(uid).child(Email).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageurl = String.valueOf(uri);
                saveDataInFirebase(Email, uid, imageurl);
            }
        });


        progressDialog1.dismiss();
        //   startActivity(new Intent(getApplicationContext(), HomeActivity.class));

    }

    private void saveDataInFirebase(String email, String uid, String imageurl) {

        saveData = new SaveData(Name, Phone, email, Gender, imageurl);
        saveData1 = new SaveData(interest1, interest2, interest3, interest4, interest5, interest6);


        myRef = database.getReference("User Profile");
        myRef.child(uid).setValue(saveData);
        myRef.child(uid).child("interests").setValue(saveData1);

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void findInterests() {

        if (cb1.isChecked()) {
            interest1 = cb1.getText().toString();

        }
        if (cb2.isChecked()) {
            interest2 = cb2.getText().toString();

        }
        if (cb3.isChecked()) {


            interest3 = cb3.getText().toString();
        }
        if (cb4.isChecked()) {

            interest4 = cb4.getText().toString();

        }
        if (cb5.isChecked()) {
            interest5 = cb5.getText().toString();
        }
        if (cb6.isChecked()) {
            interest6 = cb6.getText().toString();

        }


    }

    private String findGender() {
        String gen = "";
        int id = radioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rbMale:
                gen = (String) rbMale.getText();
                break;
            case R.id.rbFemale:
                gen = (String) rbFemale.getText();
        }
        return gen;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Filepath = data.getData();
            Log.e("image url", String.valueOf(Filepath));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Filepath);
                ivProfile.setImageBitmap(bitmap);
                saveImage();
            } catch (Exception e) {
                util.toast(getApplicationContext(), e.getMessage());
            }
        }

    }

    private void saveImage() {
        progressDialog.show();
        String uid = mUser.getUid();
        String email = mUser.getEmail();
        StorageReference profileRef = mStorageRef.child(uid).child(email);
        profileRef.putFile(Filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                util.toast(getApplicationContext(), e.getMessage());
            }
        });


    }

    public boolean isNameCorrect() {
        Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(etName.getText().toString());
        return matcher.matches();


    }

}
