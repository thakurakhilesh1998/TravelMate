package com.example.travelmate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travelmate.FirebaseData.getDataFirebase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView navigationIcon, ivnotification, ivprofile;
    TextView tvName, tvEmail;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> list, list1;
    ArrayList<String> temp, filtered, placename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findIds();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        temp = new ArrayList<>();
        filtered = new ArrayList<>();
        placename = new ArrayList<>();
        fetchData();

        setSupportActionBar(toolbar);


    }

    private void fetchData() {


        final String uid = mUser.getUid();
        reference = database.getReference();
        reference.child("User Profile").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Email = dataSnapshot.child("Email").getValue().toString();
                String Name = dataSnapshot.child("Name").getValue().toString();
                String profile = dataSnapshot.child("Profile").getValue().toString();
                Log.e("profile",profile);
                showData(Email, Name, profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(String email, String name, String profile) {
        tvName.setText(name);
        tvEmail.setText(email);
        Glide.with(this).load(profile).into(ivprofile);
        Places places = new Places();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, places);
        ft.commit();




    }

    private void findIds() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);
        navigationIcon = findViewById(R.id.ivnavigationIcon);
        ivnotification = findViewById(R.id.ivnotification);
        View headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        ivprofile = headerView.findViewById(R.id.ivProfile);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void onLogOut() {


        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                onLogOut();
        }
        return super.onOptionsItemSelected(item);
    }


}
