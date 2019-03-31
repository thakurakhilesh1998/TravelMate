package com.example.travelmate;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.travelmate.APIS.DirectionApiHitter;
import com.example.travelmate.Direction.Direction;
import com.example.travelmate.Direction.Leg;
import com.example.travelmate.Direction.Route;
import com.example.travelmate.Direction.Step;
import com.example.travelmate.utility.constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class map_2_activity extends AppCompatActivity implements View.OnClickListener {
    TextView tvDistance, tvTime;
    GoogleMap mMap;
    FusedLocationProviderClient mClient;
    Double latitude, langitude;

    List<com.example.travelmate.Direction.Route> Route;
    ArrayList<LatLng> list;
    String destLatlang;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mode = "driving";
        setContentView(R.layout.activity_map_2_activity);
        mClient = LocationServices.getFusedLocationProviderClient(this);
        Route = new ArrayList<>();
        list = new ArrayList<>();
        tvDistance = findViewById(R.id.tvDistance);
        tvTime = findViewById(R.id.tvTime);

        Intent intent = getIntent();
        destLatlang = intent.getStringExtra("geocoordinatesmap");
        Log.e("destLatlang", destLatlang);

       onChangeMode();


    }

    private void getCurrentLocation(final String mode) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latitude = location.getLatitude();
                langitude = location.getLongitude();
                getDataFromApi(latitude, langitude, mode);
            }
        });

    }

    private void getDataFromApi(Double latitude, Double langitude, final String mode) {

        String originlatlang = latitude + "," + langitude;
        final LatLng latLng1 = new LatLng(latitude, langitude);
        Log.e("mode12", mode);
        final LatLng destLatlang1 = new LatLng(Double.valueOf(destLatlang.substring(0, 9)), Double.valueOf(destLatlang.substring(10, 19)));
        Call<Direction> getDirection = DirectionApiHitter.DirectionApiHitter().getDirection(originlatlang, destLatlang, mode, constants.KEY);
        getDirection.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {

                Route = response.body().getRoutes();
                initMap(Route, latLng1, destLatlang1, mode);


            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {

            }
        });

    }

    private void initMap(final List<com.example.travelmate.Direction.Route> route, final LatLng latLng1, final LatLng destLatlang1, String mode) {

        if (mMap == null) {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;

                    mMap.addMarker(new MarkerOptions().position(latLng1).title("Current"));
                    mMap.addMarker(new MarkerOptions().position(destLatlang1).title("destination"));
                    tvDistance.setText(route.get(0).getLegs().get(0).getDistance().getText());
                    tvTime.setText(route.get(0).getLegs().get(0).getDuration().getText());
                    Log.e("travelmode", route.get(0).getLegs().get(0).getSteps().get(0).getTravelMode());
                    list = getDirectionPolylines(route);
                    drawRouteOnMap(mMap, list);

                }


            });
        }


    }

    private void drawRouteOnMap(GoogleMap mMap, ArrayList<LatLng> list) {

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        options.addAll(list);
        Polyline polyline = mMap.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(list.get(1).latitude, list.get(1).longitude))
                .zoom(17)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



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
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline) {
                        directionList.add(direction);
                    }
                }
            }


        }


        return directionList;
    }

    private List<LatLng> decodePoly(String points) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = points.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = points.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = points.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;


    }

    @Override
    public void onClick(View v) {
String mode;
        switch (v.getId()) {
            case R.id.cbDriving:
                mode = "driving";
                getCurrentLocation(mode);
                dialog.dismiss();
                break;
            case R.id.cbWalking:
                mode = "walking";
                getCurrentLocation(mode);
                dialog.dismiss();
                break;
            case R.id.cbTransit:
                mode = "transit";
                getCurrentLocation(mode);
                dialog.dismiss();
                break;
        }

    }


    private void onChangeMode() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.mode);
        RadioButton cbDriving = dialog.findViewById(R.id.cbDriving);
        RadioButton cbWalking = dialog.findViewById(R.id.cbWalking);
        RadioButton cbTransit = dialog.findViewById(R.id.cbTransit);
        cbDriving.setOnClickListener(this);
        cbWalking.setOnClickListener(this);
        cbTransit.setOnClickListener(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

}
