package com.example.travelmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    FirebaseFirestore db;
    placeData data1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findIds();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        list1 = new ArrayList<>();
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
                showData(Email, Name, profile, uid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showData(String email, String name, String profile, String uid) {
        tvName.setText(name);
        tvEmail.setText(email);
        Glide.with(this).load(profile).into(ivprofile);

        fetchInterests(uid);
    }

    private void fetchInterests(String uid) {


        reference.child("User Profile").child(uid).child("interests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();

                Iterator myVeryOwnIterator = td.keySet().iterator();

                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    list.add((String) td.get(key));

                }

                fetchPlaces(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchPlaces(final ArrayList<String> interest1) {


        int i = 0;
        for (i = 0; i < interest1.size(); i++) {

            switch (i) {
                case 0:

                    call1(interest1.get(0));
                    break;
                case 1:
                    call2(interest1.get(1));
                    break;

                case 2:
                    call3(interest1.get(2));
                    break;
                case 3:
                    call4(interest1.get(3));
                    break;
                case 4:
                    call5(interest1.get(4));
                    break;
                case 5:
                    call6(interest1.get(5));

            }

        }

    }

    private void call1(final String s) {


    }

    private void call2(String s) {

        Log.e("string", s);
        reference.child("Places").child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

           for(DataSnapshot data:dataSnapshot.getChildren())
           {
              data1=dataSnapshot.getChildren().iterator().next().getValue(placeData.class);
           }
           data1.getPlacename();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void call3(String s) {

        Log.e("string", s);
    }

    private void call4(String s) {

        Log.e("string", s);
    }

    private void call5(String s) {
        Log.e("string", s);
    }

    private void call6(String s) {

        Log.e("string", s);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                onLogOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLogOut() {


        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
