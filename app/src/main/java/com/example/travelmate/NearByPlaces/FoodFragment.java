package com.example.travelmate.NearByPlaces;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FoodFragment extends Fragment {
    public static final String KEY = "AIzaSyDubiCUnOFUDUqIZMGjy8NKav32P6ioDUg";

    public static final String RADIUS = "1000";
    List<Result> food;
    RecyclerView rvFood;
    String types = "cafe";
    String lat, longitude;

    public FoodFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFood = view.findViewById(R.id.rvFood);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvFood.setLayoutManager(manager);

        String geolocation = getArguments().getString("geolocation");
        if (!(geolocation.length() < 19)) {
            lat = substringGeolocation.getLatitude(geolocation);
            longitude = substringGeolocation.getLongitude(geolocation);
            getDataFromApi(lat, longitude);
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.location_not_accessible), Toast.LENGTH_LONG).show();
        }
    }


    private void getDataFromApi(String lat, String longitude) {

        final String latlong = lat + longitude;
        Call<NearByAtm> getPlaces = NearbyApiHitter.NearbyApiHitter().getPlaces(constants.KEY, latlong, RADIUS, types);
        getPlaces.enqueue(new Callback<NearByAtm>() {
            @Override
            public void onResponse(Call<NearByAtm> call, Response<NearByAtm> response) {
                if (response.isSuccessful()) {
                    food = response.body().getResults();
                    NearByAtmAdapter adapter = new NearByAtmAdapter(getContext(), food, latlong);
                    rvFood.setAdapter(adapter);

                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<NearByAtm> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
