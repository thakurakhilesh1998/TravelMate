package com.example.travelmate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.travelmate.APIS.PlaceIDApi;
import com.example.travelmate.APIS.UberCabApi;
import com.example.travelmate.Adapter.CabDetailsAdapter;
import com.example.travelmate.PlaceID.PlaceID;
import com.example.travelmate.UberCab.UberCab;
import com.example.travelmate.utility.constants;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookCab extends AppCompatActivity {
    Double lat1, lang1, lat2, lang2;
    String content_type = "%20application/json";
    RecyclerView rvCabDeatils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cab);
        rvCabDeatils = findViewById(R.id.rvCabDetails);
        Places.initialize(getApplicationContext(), constants.KEY);
        autocomplete1();
        autocomplete12();


    }

    private void getCabData() {
        Log.e("latlang", String.valueOf(lat1 + "," + lang1 + "," + lat2 + "," + lang2));
        final Call<UberCab> getData = UberCabApi.UberCab().getCabDetails(content_type, constants.Server_Token, String.valueOf(lat1), String.valueOf(lang1), String.valueOf(lat2), String.valueOf(lang2));
        getData.enqueue(new Callback<UberCab>() {
            @Override
            public void onResponse(Call<UberCab> call, Response<UberCab> response) {
                getData1(response);
            }

            @Override
            public void onFailure(Call<UberCab> call, Throwable t) {

            }
        });

    }

    private void getData1(Response<UberCab> response) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCabDeatils.setLayoutManager(layoutManager);
        CabDetailsAdapter cabDetailsAdapter = new CabDetailsAdapter(this, response);
    }


    private void autocomplete12() {

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Enter Your Desination");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                getLatitudeandlongitude1(place);

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    private void getLatitudeandlongitude1(Place place) {


        Call<PlaceID> getLocation = PlaceIDApi.PlaceIDApi().getLatlang(place.getId(), constants.KEY);
        getLocation.enqueue(new Callback<PlaceID>() {
            @Override
            public void onResponse(Call<PlaceID> call, Response<PlaceID> response) {
                lat2 = response.body().getResult().getGeometry().getLocation().getLat();
                lang2 = response.body().getResult().getGeometry().getLocation().getLng();
                getCabData();
            }

            @Override
            public void onFailure(Call<PlaceID> call, Throwable t) {

            }
        });
    }

    void getLatitudeandlongitude(Place place) {

        Call<PlaceID> getLocation = PlaceIDApi.PlaceIDApi().getLatlang(place.getId(), constants.KEY);
        getLocation.enqueue(new Callback<PlaceID>() {
            @Override
            public void onResponse(Call<PlaceID> call, Response<PlaceID> response) {
                lat1 = response.body().getResult().getGeometry().getLocation().getLat();
                lang1 = response.body().getResult().getGeometry().getLocation().getLng();


            }

            @Override
            public void onFailure(Call<PlaceID> call, Throwable t) {

            }
        });
    }

    public void autocomplete1() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment1);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Enter Your Source");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.e("placename", place.getName());
                Log.e("placeid", place.getId());
                Log.e("get", String.valueOf(place.getLatLng()));
                getLatitudeandlongitude(place);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

    }


}
