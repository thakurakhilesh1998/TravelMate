package com.example.travelmate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.travelmate.Adapter.PlacesAdapter;
import com.example.travelmate.FirebaseData.getDataFirebase;
import com.example.travelmate.utility.PrefLocation;
import com.example.travelmate.utility.util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class nearbyplaces_activity extends AppCompatActivity {

    private static final int REQUEST_CODE = 12;
    RecyclerView rvPlaces;
    ArrayList<Integer> placesimages;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    Toolbar toolbar;
    ArrayList<String> list, list1;
    ArrayList<String> temp, filtered, placename, name, geolocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    ProgressDialog mprogressDialog;
    View v;
    PrefLocation prefLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearbyplaces_activity);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        onBackButton();
        prefLocation = new PrefLocation(this);
        rvPlaces = findViewById(R.id.rvPlaces);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPlaces.setLayoutManager(manager);
        mprogressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        placesimages = new ArrayList<>();
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        temp = new ArrayList<>();
        Log.e("temp size", String.valueOf(temp.size()));
        filtered = new ArrayList<>();
        placename = new ArrayList<>();
        name = new ArrayList<String>();
        geolocation = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mprogressDialog.setMessage("Wait...Fetching...Your Places");
        mprogressDialog.setCanceledOnTouchOutside(false);
        mprogressDialog.show();
        fetchInterests(mUser.getUid());
    }

    private void fetchInterests(String uid) {
        reference.child("User Profile").child(uid).child("interests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                    Iterator myVeryOwnIterator = td.keySet().iterator();
                    while (myVeryOwnIterator.hasNext()) {
                        String key = (String) myVeryOwnIterator.next();
                        list.add((String) td.get(key));
                    }
                    fetchPlaces(list);
                } catch (Exception e) {
                    mprogressDialog.dismiss();
                    util.toast(getApplicationContext(), e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchPlaces(final ArrayList<String> interest1) {

        try {
            call1(interest1.get(0), interest1);
        } catch (Exception e) {

            mprogressDialog.dismiss();
            Log.e("exception", "exception1");
        }

    }

    private void call1(String list, final ArrayList<String> interest1) {

        reference.child("Places").child(list).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                Iterator myVeryOwnIterator = td.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    temp.add((String) td.get(key));
                }
                if (1 < interest1.size()) {
                    try {
                        call2(temp, interest1);
                    } catch (Exception e) {
                        mprogressDialog.dismiss();
                        Log.e("exception", "exception2");
                    }
                } else {
                    getData(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getData(ArrayList<String> temp) {


        for (int i = 0; i < temp.size(); i++) {
            Log.e("data", temp.get(i));
        }

        findCurrentLocation(temp);
        mprogressDialog.dismiss();
    }

    private void findCurrentLocation(final ArrayList<String> temp) {

        filtered = getDataFirebase.isInside(Double.parseDouble(prefLocation.getLatitude()), Double.parseDouble(prefLocation.getLangitude()), temp);
        getFromFirebase(filtered);
    }

    private void getFromFirebase(final ArrayList<String> filtered) {


        for (int i = 0; i < filtered.size(); i++) {

            final int i1 = i;
            reference.child(filtered.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    geolocation.add(dataSnapshot.getKey());
                    placename.add(dataSnapshot.child("Imageaddress").getValue().toString());
                    name.add(dataSnapshot.child("Placename").getValue().toString());

                    if (i1 == filtered.size() - 1) {
                        Log.e("firebase", "firebase");
                        getPlaceName(placename, name, geolocation);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void getPlaceName(ArrayList<String> placename1, ArrayList<String> name, ArrayList<String> geolocation1) {


        PlacesAdapter placesAdapter = new PlacesAdapter(getApplicationContext(), placename1, name, geolocation1, geolocation1);
        rvPlaces.setAdapter(placesAdapter);
        mprogressDialog.dismiss();
    }

    private void call2(final ArrayList<String> temp, final ArrayList<String> interest1) {
        reference.child("Places").child(interest1.get(1)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                Iterator myVeryOwnIterator = td.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    temp.add((String) td.get(key));

                }
                if (2 < interest1.size()) {
                    try {
                        call3(temp, interest1);
                    } catch (Exception e) {
                        Log.e("exception 3", "exception 3");
                    }
                } else {
                    getData(temp);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void call3(final ArrayList<String> temp, final ArrayList<String> interest1) {
        reference.child("Places").child(interest1.get(2)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();

                Iterator myVeryOwnIterator = td.keySet().iterator();

                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    temp.add((String) td.get(key));
                }
                if (3 < interest1.size()) {
                    try {
                        call4(temp, interest1);
                    } catch (Exception e) {
                        Log.e("exception4", "exception4");
                    }

                } else {
                    getData(temp);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void call4(final ArrayList<String> temp, final ArrayList<String> interest1) {

        reference.child("Places").child(interest1.get(3)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                Iterator myVeryOwnIterator = td.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    temp.add((String) td.get(key));
                }
                if (4 < interest1.size()) {
                    try {
                        call5(temp, interest1);
                    } catch (Exception e) {
                        Log.e("exception 4", "exception 4");
                    }
                } else {
                    getData(temp);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void call5(final ArrayList<String> temp, final ArrayList<String> interest1) {

        reference.child("Places").child(interest1.get(4)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                Iterator myVeryOwnIterator = td.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    temp.add((String) td.get(key));
                }
                getData(temp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

//
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                            && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    }
//                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
                    filtered = getDataFirebase.isInside(Double.parseDouble(prefLocation.getLatitude()), Double.parseDouble(prefLocation.getLangitude()), temp);
                    Log.e("size", String.valueOf(filtered.size()));
                    getFromFirebase(filtered);
//                            }
//                        }
//                    });

//                } else {
//                    Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_SHORT);
//                }
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
        }
    }

    private void onBackButton() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
    }

}
