package com.example.travelmate;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.travelmate.APIS.DirectionApiHitter;
import com.example.travelmate.Adapter.NavAdapter;
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

public class map_activity extends AppCompatActivity implements View.OnClickListener {
    TextView tvDistance, tvTime, tvDestination, tvCurrent;
    Toolbar toolbar;
    GoogleMap mMap;
    FusedLocationProviderClient mClient;
    Double latitude, langitude;
    ImageView ivcurrent, ivdirection;
    List<com.example.travelmate.Direction.Route> Route;
    ArrayList<LatLng> list;
    Dialog dialog;
    RecyclerView rvDirection;
    ArrayList<String> direction, time, distance, manuer;
    int backpresscount = 0;
    RelativeLayout relativeLayout;
    String lat, lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mode = "driving";
        setContentView(R.layout.activity_map_2_activity);
        mClient = LocationServices.getFusedLocationProviderClient(this);
        findIds();
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lang = intent.getStringExtra("lang");
        ivdirection.setOnClickListener(this);
        ivcurrent.setOnClickListener(this);
        relativeLayout.setVisibility(View.VISIBLE);
        onChangeMode();

    }

    private void findIds() {
        tvCurrent = findViewById(R.id.tvCurrent);
        toolbar = findViewById(R.id.toolbar);
        tvDistance = findViewById(R.id.tvDistance);
        tvTime = findViewById(R.id.tvTime);
        Route = new ArrayList<>();
        list = new ArrayList<>();
        ivcurrent = findViewById(R.id.ivcurrent);
        ivdirection = findViewById(R.id.ivDirection);
        rvDirection = findViewById(R.id.rvDirection);
        tvDestination = findViewById(R.id.tvDest);
        direction = new ArrayList<>();
        time = new ArrayList<>();
        distance = new ArrayList<>();
        manuer = new ArrayList<>();
        relativeLayout = findViewById(R.id.realativelayout);


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
        final LatLng destLatlang1 = new LatLng(Double.valueOf(lat), Double.valueOf(lang));
        Call<Direction> getDirection = DirectionApiHitter.DirectionApiHitter().getDirection(originlatlang, lat + "," + lang, mode, constants.KEY);
        getDirection.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {

                Route = response.body().getRoutes();
                initMap(Route, latLng1, destLatlang1, mode);
                tvCurrent.setText(response.body().getRoutes().get(0).getLegs().get(0).getStartAddress());
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {

                Log.e("error", t.getMessage());
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
                    tvDestination.setText(route.get(0).getLegs().get(0).getEndAddress());
                    tvTime.setText("(" + route.get(0).getLegs().get(0).getDuration().getText() + ")");
                    list = getDirectionPolylines(route);
                    drawRouteOnMap(mMap, list);


                    for (Route rout : route) {
                        List<Leg> legs = rout.getLegs();
                        for (Leg leg1 : legs) {
                            List<Step> steps = leg1.getSteps();
                            for (Step step : steps) {
                                String list = step.getHtmlInstructions();
                                distance.add(step.getDistance().getText());
                                time.add(step.getDuration().getText());
                                direction.add(android.text.Html.fromHtml(list).toString());
                                manuer.add(step.getManeuver());
                            }
                        }
                    }
                }


            });
        }


    }

    private void drawRouteOnMap(GoogleMap mMap, ArrayList<LatLng> list) {

        PolylineOptions options = new PolylineOptions().width(8).color(Color.BLUE).geodesic(true);
        options.addAll(list);
        Polyline polyline = mMap.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(list.get(1).latitude, list.get(1).longitude))
                .zoom(17)
                .build();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
            case R.id.ivcurrent:
                onClickOnCurrent();
                break;
            case R.id.ivDirection:
                onClickOnNav();
                break;
        }
    }

    private void onClickOnNav() {
        relativeLayout.setVisibility(View.GONE);
        rvDirection.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvDirection.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvDirection.getContext(),
                manager.getOrientation());
        rvDirection.addItemDecoration(dividerItemDecoration);
        NavAdapter adapter = new NavAdapter(this, distance, time, direction, manuer);
        rvDirection.setAdapter(adapter);
    }

    private void onClickOnCurrent() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latitude = location.getLatitude();
                langitude = location.getLongitude();
                addMarker(latitude, langitude);
            }
        });


    }

    private void addMarker(Double latitude, Double langitude) {
        LatLng latLng = new LatLng(latitude, langitude);
        mMap.addMarker(new MarkerOptions().title("Current Location").position(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private void onChangeMode() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.mode);
        RadioButton cbDriving = dialog.findViewById(R.id.cbDriving);
        RadioButton cbWalking = dialog.findViewById(R.id.cbWalking);
        cbDriving.setOnClickListener(this);
        cbWalking.setOnClickListener(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    @Override
    public void onBackPressed() {

        backpresscount++;
        rvDirection.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
        if (backpresscount == 2) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Do You Want To enter Home Page?");
            alertDialogBuilder.setTitle("Exit Screen");
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    backpresscount = 0;
                }
            });
            AlertDialog alert = alertDialogBuilder.create();

            alert.setTitle("AlertDialogExample");
            alert.show();
        }
        if (backpresscount == 3) {
            super.onBackPressed();
            return;
        }


    }

    public String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

}
