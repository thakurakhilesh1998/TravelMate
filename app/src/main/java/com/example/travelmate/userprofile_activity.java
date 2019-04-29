package com.example.travelmate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class userprofile_activity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivProfile;
    EditText tvEmail;
    EditText etPhone, etAge;
    TextView etName;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6;
    Button btnEditDetails, btnsaveDetails;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ArrayList<String> list;
    LinearLayout linearLayout;
    Toolbar toolbar;
    String interest1, interest2, interest3, interest4, interest5, interest6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile_activity);
        findIds();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        setSupportActionBar(toolbar);
        getTripNumber();
        list = new ArrayList<>();
        btnEditDetails.setVisibility(View.VISIBLE);
        btnEditDetails.setOnClickListener(this);
        btnsaveDetails.setOnClickListener(this);
        getDataFromFirebase();
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
                Log.e("dataage", data.getAge());
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
                case "Ocean":
                    cb2.setChecked(true);
                    break;
                case "Deserts":
                    cb3.setChecked(true);
                    break;
                case "Religous":
                    cb4.setChecked(true);
                    break;
                case "Forest":
                    cb5.setChecked(true);

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
        }

    }

    private void onSaveDetails(View v) {
        btnsaveDetails.setVisibility(View.GONE);
        String name = etName.getText().toString();
        ArrayList<String> list = new ArrayList<>();
        if (cb1.isChecked()) {
            interest1 = (String) cb1.getText();
            cb1.setChecked(true);
        }
        if (cb2.isChecked()) {
            interest2 = (String) cb2.getText();
            cb2.setChecked(true);
        }
        if (cb3.isChecked()) {
            interest3 = (String) cb3.getText();
            cb3.setChecked(true);
        }
        if (cb4.isChecked()) {
            interest4 = (String) cb4.getText();
            cb4.setChecked(true);
        }
        if (cb5.isChecked()) {
            interest5 = (String) cb5.getText();
            cb5.setChecked(true);
        }
        if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked() || cb4.isChecked() || cb5.isChecked()) {
            SaveData data = new SaveData(interest1, interest2, interest3, interest4, interest5, interest6);
            reference.child("User Profile").child(mUser.getUid()).child("Name").setValue(name);
            reference.child("User Profile").child(mUser.getUid()).child("interests").setValue(data);

            btnEditDetails.setVisibility(View.VISIBLE);
            etName.setEnabled(false);
            cb1.setEnabled(false);
            cb2.setEnabled(false);
            cb3.setEnabled(false);
            cb4.setEnabled(false);
            cb5.setEnabled(false);
            cb6.setEnabled(false);
        } else {

            btnsaveDetails.setVisibility(View.VISIBLE);
            Snackbar.make(v, "Please Select At least One Interest", Snackbar.LENGTH_LONG).show();
        }
    }

    private void onEditDetails() {
        etName.setEnabled(true);
        cb1.setEnabled(true);
        cb2.setEnabled(true);
        cb3.setEnabled(true);
        cb4.setEnabled(true);
        cb5.setEnabled(true);
        cb6.setEnabled(true);
        btnEditDetails.setVisibility(View.GONE);
        btnsaveDetails.setVisibility(View.VISIBLE);
    }
}
