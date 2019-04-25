package com.example.travelmate.HomeFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelmate.Adapter.TripAdapter;
import com.example.travelmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class mytripFragment extends Fragment {

    RecyclerView rvMyTrips;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ArrayList<String> list;
    ArrayList<String> listfinal;
    TextView tvnotrip;
    LinearLayout linearLayout;

    public mytripFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mytrip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMyTrips = view.findViewById(R.id.rvMytrips);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        listfinal = new ArrayList<>();
        linearLayout = view.findViewById(R.id.linearLayout);
        tvnotrip = view.findViewById(R.id.tvnotrip);
        getDataFromFirebase();


    }

    private void getDataFromFirebase() {

        databaseReference = database.getReference();
        databaseReference.child("User Profile").child(mUser.getUid()).child("MyTrip").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                    Iterator myVeryOwnIterator = td.keySet().iterator();
                    while (myVeryOwnIterator.hasNext()) {
                        String key = (String) myVeryOwnIterator.next();
                        list.add(key);
                    }
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).compareTo(formattedDate) > 0) {
                            listfinal.add(list.get(i));
                        }
                    }
                    if (listfinal.size() != 0) {
                        try {
                            rvMyTrips = new RecyclerView(getContext());
                        } catch (Exception e) {
                        }
                        linearLayout.addView(rvMyTrips);
                        rvMyTrips.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvMyTrips.setLayoutManager(manager);
                        TripAdapter tripAdapter = new TripAdapter(getContext(), listfinal);
                        rvMyTrips.setAdapter(tripAdapter);
                        rvMyTrips.setVisibility(View.VISIBLE);
                    } else {
                        tvnotrip = new TextView(getContext());
                        tvnotrip.setText("no tripfound");
                        tvnotrip.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        linearLayout.addView(tvnotrip);
                    }

                } else {
                    tvnotrip = new TextView(getContext());
                    tvnotrip.setText("no tripfound");
                    tvnotrip.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    linearLayout.addView(tvnotrip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
