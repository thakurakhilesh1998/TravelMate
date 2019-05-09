package com.example.travelmate.NearByPlaces;


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
import android.widget.Toast;

import com.example.travelmate.APIS.NearbyApiHitter;
import com.example.travelmate.Adapter.NearByAtmAdapter;
import com.example.travelmate.NearByAtm.NearByAtm;
import com.example.travelmate.NearByAtm.Result;
import com.example.travelmate.R;
import com.example.travelmate.utility.constants;
import com.example.travelmate.utility.substringGeolocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusStaionFragment extends Fragment {
    public static final String KEY = "AIzaSyCOggg7f0D3iWZOQSLOKbo0BWrbQ9Y6ymw";
    public static final String RADIUS = "1000";
    List<Result> atm1;
    RecyclerView rvAtm;
    String types = "bus_station";

    public BusStaionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_staion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAtm = view.findViewById(R.id.rvAtm);
        String geolocation = getArguments().getString("geolocation");
        String lat = substringGeolocation.getLatitude(geolocation);
        String longitude = substringGeolocation.getLongitude(geolocation);
        Log.e("bdhsgfgvf", geolocation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvAtm.setLayoutManager(manager);
        getDataFromApi(lat, longitude);

    }

    private void getDataFromApi(String lat, String longitude) {
        final String latlong = lat + longitude;
        Call<NearByAtm> getPlaces = NearbyApiHitter.NearbyApiHitter().getPlaces(constants.KEY, latlong, RADIUS, types);
        getPlaces.enqueue(new Callback<NearByAtm>() {
            @Override
            public void onResponse(Call<NearByAtm> call, Response<NearByAtm> response) {
                if (response.isSuccessful()) {
                    atm1 = response.body().getResults();
                    NearByAtmAdapter adapter = new NearByAtmAdapter(getContext(), atm1, latlong);
                    rvAtm.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearByAtm> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}
