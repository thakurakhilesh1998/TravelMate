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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        tvnotrip = view.findViewById(R.id.tvnotrip);
        rvMyTrips = view.findViewById(R.id.rvMytrips);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        listfinal = new ArrayList<>();
        linearLayout = view.findViewById(R.id.linearLayout);
        tvnotrip = view.findViewById(R.id.tvnotrip);
        try {
            getDataFromFirebase();
        } catch (Exception e) {
            Log.e("msg", e.getMessage());
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getDataFromFirebase() {

        databaseReference = database.getReference();
        databaseReference.child("User Profile").child(mUser.getUid()).child("MyTrip").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
            }
        });


    }

    private void getData(DataSnapshot dataSnapshot) {

        if (dataSnapshot.hasChildren()) {
            getData2(dataSnapshot);
        } else {

            tvnotrip.setVisibility(View.VISIBLE);
        }
    }

    private void getData2(DataSnapshot dataSnapshot) {

        Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
        Iterator myVeryOwnIterator = td.keySet().iterator();
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            list.add(key);
        }
        comapreDate();
        if (listfinal.size() != 0) {
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvMyTrips.setLayoutManager(manager);
        //    TripAdapter tripAdapter = new TripAdapter(viewmytripactivity.this, getContext(), listfinal);
          //  rvMyTrips.setAdapter(tripAdapter);
            rvMyTrips.setVisibility(View.VISIBLE);
        } else if (list.size() == 0) {

            tvnotrip.setVisibility(View.VISIBLE);
        } else {
            tvnotrip.setVisibility(View.VISIBLE);
        }

    }
    private ArrayList<String> comapreDate() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).compareTo(getCurrentDate()) > 0) {
                listfinal.add(list.get(i));
            }
        }
        return listfinal;
    }

    private String getCurrentDate() {


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
