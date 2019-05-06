package com.example.travelmate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.travelmate.APIS.DirectionApiHitter;
import com.example.travelmate.APIS.PlaceIDApi;
import com.example.travelmate.APIS.UberCabApi;
import com.example.travelmate.Adapter.CabDetailsAdapter;
import com.example.travelmate.Direction.Direction;
import com.example.travelmate.Direction.Leg;
import com.example.travelmate.Direction.Route;
import com.example.travelmate.Direction.Step;
import com.example.travelmate.PlaceID.PlaceID;
import com.example.travelmate.UberCab.UberCab;
import com.example.travelmate.utility.Decodepoly;
import com.example.travelmate.utility.constants;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookCab extends AppCompatActivity {
    Double lat1, lang1, lat2, lang2;
    String content_type = "%20application/json";
    LinearLayout select;
    RecyclerView rvCabDeatils;
    GoogleMap mMap;
    Toolbar toolbarcab;
    List<com.example.travelmate.Direction.Route> Route;
    ArrayList<LatLng> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cab);
        select = findViewById(R.id.select);
        list = new ArrayList<>();
        toolbarcab = findViewById(R.id.toolbarcab);
        setSupportActionBar(toolbarcab);
        select.setVisibility(View.VISIBLE);
        onBackButton();
        Places.initialize(getApplicationContext(), constants.KEY);
        autocomplete1();
        autocomplete12();
    }
    private void onBackButton() {
        toolbarcab.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        toolbarcab.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
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
        select.setVisibility(View.GONE);
        rvCabDeatils = findViewById(R.id.rvCabDetails1);
        rvCabDeatils.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCabDeatils.setLayoutManager(manager);
        CabDetailsAdapter cabDetailsAdapter = new CabDetailsAdapter(this, response);
        rvCabDeatils.setAdapter(cabDetailsAdapter);
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
                showOnMap();

            }

            @Override
            public void onFailure(Call<PlaceID> call, Throwable t) {

            }
        });
    }

    private void showOnMap() {

        Call<Direction> getDirection = DirectionApiHitter.DirectionApiHitter().getDirection(lat1 + "," + lang1, lat2 + "," + lang2, "driving", constants.KEY);

        getDirection.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                drawOnMap(response);
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {

            }
        });

    }

    private void drawOnMap(final Response<Direction> response) {

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng source = new LatLng(lat1, lang1);
                LatLng destination = new LatLng(lat2, lang2);
                mMap.addMarker(new MarkerOptions().position(source).title("Source"));
                mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));

                drawPolyLIneOnMap(response);
            }
        });

    }

    private void drawPolyLIneOnMap(Response<Direction> response) {

        Route = response.body().getRoutes();
        list = getDirectionPolylines(Route);
        PolylineOptions options = new PolylineOptions().width(8).color(Color.BLUE).geodesic(true);
        options.addAll(list);
        Polyline polyline = mMap.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(list.get(1).latitude, list.get(1).longitude))
                .zoom(10)
                .build();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        getCabData();

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

    private ArrayList<LatLng> getDirectionPolylines(List<Route> route) {

        ArrayList<LatLng> directionList = new ArrayList<LatLng>();
        for (Route rout : route) {
            List<Leg> legs = rout.getLegs();
            for (Leg leg1 : legs) {
                List<Step> steps = leg1.getSteps();
                for (Step step : steps) {
                    com.example.travelmate.Direction.Polyline polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = Decodepoly.decodePoly(points);
                    for (LatLng direction : singlePolyline) {
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }


}
