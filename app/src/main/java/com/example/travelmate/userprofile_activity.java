package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.travelmate.utility.UpdatedUserProfileData;
import com.example.travelmate.utility.UserProfileData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class userprofile_activity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivProfile;
    EditText tvEmail;
    EditText etPhone, etAge;
    EditText etName;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6;
    Button btnEditDetails, btnsaveDetails;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ArrayList<String> list, list2;
    LinearLayout linearLayout;
    Toolbar toolbar;
    ArrayList<String> int1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile_activity);
        findIds();
        int1 = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setBackButton();
        getTripNumber();
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        btnEditDetails.setVisibility(View.VISIBLE);
        btnEditDetails.setOnClickListener(this);
        btnsaveDetails.setOnClickListener(this);
        getDataFromFirebase();
    }

    private void setBackButton() {

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

    }

    private void getTripNumber() {

        reference.child("User Profile").child(mUser.getUid()).child("MyTrip").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  tvMyTrips.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getDataFromFirebase() {
        reference.child("User Profile").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileData data = dataSnapshot.getValue(UserProfileData.class);
                etName.setText(data.getName());
                tvEmail.setText(data.getEmail());
                etPhone.setText(data.getPhone());
                etAge.setText(data.getAge());
                Glide.with(getApplicationContext()).load(data.getProfile()).into(ivProfile);
                Iterator myVeryOwnIterator = data.getInterests().values().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String value = (String) myVeryOwnIterator.next();
                    list.add(value);
                }
                setCheckbox(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setCheckbox(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            switch (list.get(i)) {
                case "Mountains":
                    cb1.setChecked(true);
                    break;
                case "Deserts":
                    cb2.setChecked(true);
                    break;
                case "Ocean":
                    cb3.setChecked(true);
                    break;
                case "Religous":
                    cb4.setChecked(true);
                    break;
                case "Forest":
                    cb5.setChecked(true);
                    break;
                default:


            }
        }
    }

    private void findIds() {
        toolbar = findViewById(R.id.toolbar);
        ivProfile = findViewById(R.id.ivProfile);
        etName = findViewById(R.id.etName);
        tvEmail = findViewById(R.id.tvEmail);
        etPhone = findViewById(R.id.etPhone);
        etAge = findViewById(R.id.etAge);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);
        linearLayout = findViewById(R.id.linearLayout);
        btnEditDetails = findViewById(R.id.btnEditDetails);
        btnsaveDetails = findViewById(R.id.btnSaveDetails);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnEditDetails:
                onEditDetails();
                break;
            case R.id.btnSaveDetails:
                onSaveDetails(v);
                break;
            case R.id.logo:
                onLogogClick();
                break;
        }
    }

    private void onLogogClick() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void onSaveDetails(View v) {
        String interest1 = null;
        String interest2 = null;
        String interest3 = null;
        String interest4 = null;
        String interest5 = null;
        String interest6 = null;

        if (updateUserProfile() == true) {
            etName.setEnabled(false);
            etAge.setEnabled(false);
            etPhone.setEnabled(false);
            if (cb1.isChecked()) {
                interest1 = (String) cb1.getText();
            } else if (cb1.isChecked() == false) {
                cb1.setChecked(false);
            }

            if (cb2.isChecked()) {
                interest2 = (String) cb2.getText();
            }
            if (cb3.isChecked()) {
                interest3 = (String) cb3.getText();
            }
            if (cb4.isChecked()) {
                interest4 = (String) cb4.getText();
            }
            if (cb5.isChecked()) {
                interest5 = (String) cb5.getText();
            }
            if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked() || cb4.isChecked() || cb5.isChecked() || cb6.isChecked()) {
                SaveData data = new SaveData(interest1, interest2, interest3, interest4, interest5, interest6);
                reference.child("User Profile").child(mUser.getUid()).child("Phone").setValue(etPhone.getText().toString().trim());
                reference.child("User Profile").child(mUser.getUid()).child("Age").setValue(etAge.getText().toString().trim());
                reference.child("User Profile").child(mUser.getUid()).child("Name").setValue(etName.getText().toString().trim());
                reference.child("User Profile").child(mUser.getUid()).child("interests").setValue(data);
                getUpdateData();
                btnEditDetails.setVisibility(View.VISIBLE);
                etName.setEnabled(false);
                cb1.setEnabled(false);
                cb2.setEnabled(false);
                cb3.setEnabled(false);
                cb4.setEnabled(false);
                cb5.setEnabled(false);
                cb6.setEnabled(false);

            } else {
                Snackbar.make(v, "Please Select At least One Interest", Snackbar.LENGTH_LONG).show();
            }

        }
    }

    private void getUpdateData() {
        reference.child("User Profile").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    btnsaveDetails.setVisibility(View.GONE);
                    btnEditDetails.setVisibility(View.VISIBLE);
                    UpdatedUserProfileData data1 = dataSnapshot.getValue(UpdatedUserProfileData.class);
                    etName.setText(data1.getName());
                    etPhone.setText(data1.getPhone());
                    etAge.setText(data1.getAge());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private boolean updateUserProfile() {


        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Name field can not be empty");
            etName.setFocusable(true);
        } else if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Phone can not be empty");
            etPhone.setFocusable(true);
        } else if (etAge.getText().toString().trim().isEmpty()) {
            etAge.setError("Age can not be empty");
            etAge.setFocusable(true);
        } else if (!isNameCorrect()) {
            etName.setError("name format is not correct");
            etName.setFocusable(true);
        } else if (!isPhoneCorrect()) {
            etPhone.setError("phone pattern is not correct");
            etPhone.setFocusable(true);
        } else if (etAge.getText().toString().substring(0, 1).equals("0")) {
            etAge.setError("age format is not correct");
            etAge.setFocusable(true);
        } else {
            return true;
        }

        return false;

    }

    private void onEditDetails() {
        etName.setEnabled(true);
        etAge.setEnabled(true);
        etPhone.setEnabled(true);
        cb1.setEnabled(true);
        cb2.setEnabled(true);
        cb3.setEnabled(true);
        cb4.setEnabled(true);
        cb5.setEnabled(true);
        cb6.setEnabled(true);
        btnEditDetails.setVisibility(View.GONE);
        btnsaveDetails.setVisibility(View.VISIBLE);
    }


    public boolean isNameCorrect() {
        Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(etName.getText().toString());
        return matcher.matches();
    }

    public boolean isPhoneCorrect() {
        Boolean check = Patterns.PHONE.matcher(etPhone.getText().toString()).matches();
        return check;
    }


}
