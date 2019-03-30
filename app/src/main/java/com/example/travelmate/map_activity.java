package com.example.travelmate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.travelmate.utility.*;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class map_activity extends AppCompatActivity {
    String geolocation;
    Double latitude, longitude;
    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_activity);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getDataFromIntent();

        getCurrentLocation();
    }

    private void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                showOnMap(location.getLatitude(), location.getLongitude());
            }
        });
    }

    private void showOnMap(double latitude, double longitude) {


        if (mMap == null) {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    addMarker(mMap);
                }
            });
        }

    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        geolocation = intent.getStringExtra("geocoordinatesmap");
        latitude = Double.valueOf(substringGeolocation.getLatitude(geolocation));
        longitude = Double.valueOf(substringGeolocation.getLongitude(geolocation));
    }


    public void addMarker(GoogleMap mMap) {
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().title("Location On Map").position(latLng));

    }
}
