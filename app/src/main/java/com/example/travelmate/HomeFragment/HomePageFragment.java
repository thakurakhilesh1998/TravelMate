package com.example.travelmate.HomeFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.travelmate.BookCab;
import com.example.travelmate.NearByPlaces1;
import com.example.travelmate.R;
import com.example.travelmate.activity_weather1;
import com.example.travelmate.mytrip_activity;
import com.example.travelmate.nearbyplaces_activity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {

    LinearLayout nearby, weather, nearbyattrac, createtrip, bookcab;

    public HomePageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findId(view);
        nearby.setOnClickListener(this);
        weather.setOnClickListener(this);
        nearbyattrac.setOnClickListener(this);
        createtrip.setOnClickListener(this);
        bookcab.setOnClickListener(this);
    }

    private void findId(View view) {
        weather = view.findViewById(R.id.weather);
        nearby = view.findViewById(R.id.nearby);
        nearbyattrac = view.findViewById(R.id.nearbyattrac);
        createtrip = view.findViewById(R.id.createtrip);
        bookcab = view.findViewById(R.id.bookcab);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nearby:
                onNearBy();
                break;
            case R.id.weather:
                onWeather();
                break;
            case R.id.nearbyattrac:
                onNearByAttarc();
                break;
            case R.id.createtrip:
                onCreateTrip();
                break;
            case R.id.bookcab:
                onBookCab();
        }
    }

    private void onBookCab() {


    startActivity(new Intent(getContext(), BookCab.class));}

    private void onCreateTrip() {

        startActivity(new Intent(getContext(), mytrip_activity.class));

    }

    private void onNearByAttarc() {

        startActivity(new Intent(getContext(), NearByPlaces1.class));
    }

    private void onWeather() {

        startActivity(new Intent(getContext(), activity_weather1.class));
    }

    private void onNearBy() {

        Intent intent = new Intent(getContext(), nearbyplaces_activity.class);
        startActivity(intent);
    }
}
