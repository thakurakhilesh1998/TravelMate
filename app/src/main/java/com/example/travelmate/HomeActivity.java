package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.travelmate.HomeFragment.*;
import com.bumptech.glide.Glide;
import com.example.travelmate.HomeFragment.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
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
    DrawerLayout drawerLayout;

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

        navigationView.setNavigationItemSelectedListener(this);

        navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDrawerOpened();
            }
        });

        fetchData();
        ivprofile.setOnClickListener(this);
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
                Log.e("profile", profile);
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
        View headerView = navigationView.getHeaderView(0);
        navigationIcon = findViewById(R.id.ivnavigationIcon);

        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        ivprofile = headerView.findViewById(R.id.ivProfile);
        drawerLayout = findViewById(R.id.drawerLayout);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.places:
                Places places = new Places();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, places);
                ft.commit();
                break;
            case R.id.weather:
                break;
            case R.id.checklist:

                break;
            case R.id.nearby:
                mytripFragment mytripFragment=new mytripFragment();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.frame,mytripFragment);
                ft1.commit();

        }
        return false;
    }


    private void isDrawerOpened() {

        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivProfile:
                onClickOnProfile();
        }

    }

    private void onClickOnProfile() {

        startActivity(new Intent(this, userprofile_activity.class));
    }
}
