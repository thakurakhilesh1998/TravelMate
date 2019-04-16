package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.travelmate.APIS.NearbyApiHitter;
import com.example.travelmate.NearByAtm.NearByAtm;
import com.example.travelmate.NearByAtm.Result;
import com.example.travelmate.utility.constants;
import com.example.travelmate.utility.substringGeolocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class map_nearby_activity extends AppCompatActivity {
    public static final String RADIUS = "1000";
    GoogleMap mMap;
    String types = "atm";
    String geolocation;
    String lat, longitude;
    List<Result> atm1;
    List<Double> lat1, lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_nearby_activity);
        Intent intent = getIntent();
        geolocation = intent.getStringExtra("geo");
        Log.e("geo", geolocation);
        lat1 = new ArrayList<>();
        lang = new ArrayList<>();
        lat = substringGeolocation.getLatitude(geolocation);
        longitude = substringGeolocation.getLongitude(geolocation);
        showOnMap();

    }

    private void showOnMap() {

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

    private void addMarker(GoogleMap mMap) {

        final String latlong = lat + "," + longitude;

        Call<NearByAtm> getPlaces = NearbyApiHitter.NearbyApiHitter().getPlaces(constants.KEY, latlong, RADIUS, types);
        getPlaces.enqueue(new Callback<NearByAtm>() {
            @Override
            public void onResponse(Call<NearByAtm> call, Response<NearByAtm> response) {
                if (response.isSuccessful()) {
                    atm1 = response.body().getResults();
                    for (int i = 0; i < atm1.size(); i++) {
                        lat1.add(atm1.get(i).getGeometry().getLocation().getLat());
                        lang.add(atm1.get(i).getGeometry().getLocation().getLng());
                    }
                    addMarkerOnMap(lat1, lang);


                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<NearByAtm> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void addMarkerOnMap(List<Double> lat1, List<Double> lang) {

        Log.e("size", lat1.size() + "," + lang.size());
        for (int i = 0; i < lat1.size(); i++) {
            LatLng latLng = new LatLng(lat1.get(i), lang.get(i));
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat1.get(i), lang.get(i))).title("atm"));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

    }
}
