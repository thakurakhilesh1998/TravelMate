package com.example.travelmate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelmate.HomeFragment.HomePageFragment;
import com.example.travelmate.HomeFragment.Setting;
import com.example.travelmate.utility.GpsEnabled;
import com.example.travelmate.utility.PrefLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final int REQUEST_CODE = 12;
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
    FusedLocationProviderClient fusedLocationProviderClient;
    PrefLocation prefLocation;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        findIds();
        subscribetocloudmessaging();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        temp = new ArrayList<>();
        filtered = new ArrayList<>();
        placename = new ArrayList<>();
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
        try {

            if (GpsEnabled.isEnabled(getApplicationContext())) {

                fetchCurrentLocation();
            } else {
                Toast.makeText(getApplicationContext(), "Please Enable GPS", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("msg", e.getMessage());
        }


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

    private void fetchCurrentLocation() {


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);

        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        Log.e("location", String.valueOf(location.getLatitude()));
                        prefLocation = new PrefLocation(getApplicationContext());
                        prefLocation.setLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                    } else {
                        Toast.makeText(getApplicationContext(), "Your location not accessible", Toast.LENGTH_LONG).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("msg e", e.getMessage());
                }
            });
        }


    }

    private void subscribetocloudmessaging() {
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            Log.e("message", "fcm");
                        }

                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchData() {


        final String uid = mUser.getUid();
        reference = database.getReference();
        reference.child("User Profile").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChildren()) {
                        String Email = dataSnapshot.child("Email").getValue().toString();
                        String Name = dataSnapshot.child("Name").getValue().toString();
                        String profile = dataSnapshot.child("Profile").getValue().toString();
                        Log.e("akhilesh", "akhilesh thakur");
                        showData(Email, Name, profile);
                    }
                } catch (Exception e) {
                    Log.e("msg", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE);
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        Log.e("location", String.valueOf(location.getLatitude()));
                        prefLocation = new PrefLocation(getApplicationContext());
                        prefLocation.setLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                    } else {
                        Toast.makeText(getApplicationContext(), "Your location not accessible", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Please allow location to use function of app", Snackbar.LENGTH_LONG).show();
        }
    }

    private void showData(String email, String name, String profile) {
        tvName.setText(name);
        tvEmail.setText(email);
        Glide.with(this).load(profile).into(ivprofile);
        HomePageFragment homePageFragment = new HomePageFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, homePageFragment);
        ft.commitAllowingStateLoss();
        progressDialog.dismiss();
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
            case R.id.home:
                HomePageFragment homePageFragment = new HomePageFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, homePageFragment);
                ft.addToBackStack("fm1");
                drawerLayout.closeDrawer(Gravity.START);
                ft.commitAllowingStateLoss();
                break;
            case R.id.mytrip:
                startActivity(new Intent(getApplicationContext(), viewmytripactivity.class));
                break;
            case R.id.settings:
                onSettingClicked();
                break;
            case R.id.logout:
                mAuth.signOut();
                finish();
        }
        return false;
    }

    private void onSettingClicked() {
        Setting setting = new Setting();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, setting);
        ft.addToBackStack("fm1");
        drawerLayout.closeDrawer(Gravity.START);
        ft.commitAllowingStateLoss();

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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            finish();
        }
    }


}
