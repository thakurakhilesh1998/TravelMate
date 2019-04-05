package com.example.travelmate.HomeFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelmate.Adapter.TripAdapter;
import com.example.travelmate.R;
import com.example.travelmate.utility.savetripdata;
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

public class mytripFragment extends Fragment {

    RecyclerView rvMyTrips;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ArrayList<String> list;
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
        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        list=new ArrayList<>();
        getDataFromFirebase();


    }

    private void getDataFromFirebase() {

        databaseReference = database.getReference();
        databaseReference.child("User Profile").child(mUser.getUid()).child("MyTrip").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                rvMyTrips.setLayoutManager(manager);
                Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                Iterator myVeryOwnIterator = td.keySet().iterator();

                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    list.add(key);

                }
                Log.e("size", String.valueOf(list.size()));
                Log.e("first",list.get(0));

                TripAdapter tripAdapter = new TripAdapter(getContext(),list);
                rvMyTrips.setAdapter(tripAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
