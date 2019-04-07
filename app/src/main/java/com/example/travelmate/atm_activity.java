package com.example.travelmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmate.APIS.NearbyApiHitter;
import com.example.travelmate.Adapter.NearBy;
import com.example.travelmate.NearByAtm.NearByAtm;
import com.example.travelmate.NearByAtm.Result;
import com.example.travelmate.utility.constants;
import com.example.travelmate.utility.substringGeolocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class atm_activity extends AppCompatActivity implements View.OnClickListener {
    public static final String RADIUS = "1000";
    List<Result> atm1;
    RecyclerView rvAtm;
    String types = "atm";
    ProgressDialog progressDialog;
    Toolbar toolbar;
    TextView tvMapView;
    String geolocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_activity);
        toolbar=findViewById(R.id.toolbar);
        tvMapView=findViewById(R.id.tvMapView);
        setSupportActionBar(toolbar);
        rvAtm = findViewById(R.id.rvAtm);
        Intent intent = getIntent();
        progressDialog = new ProgressDialog(getApplicationContext());
        tvMapView.setOnClickListener(this);
        geolocation = intent.getStringExtra("geocoordinates2");
        Log.e("geolocation", geolocation);
        String lat = substringGeolocation.getLatitude(geolocation);
        String longitude = substringGeolocation.getLongitude(geolocation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvAtm.setLayoutManager(manager);

        getDataFromApi(lat, longitude);

    }

    private void getDataFromApi(String lat, String longitude) {

        final String latlong = lat + "," + longitude;
        Call<NearByAtm> getPlaces = NearbyApiHitter.NearbyApiHitter().getPlaces(constants.KEY, latlong, RADIUS, types);
        getPlaces.enqueue(new Callback<NearByAtm>() {
            @Override
            public void onResponse(Call<NearByAtm> call, Response<NearByAtm> response) {
                if (response.isSuccessful()) {
                    atm1 = response.body().getResults();
                    NearBy adapter = new NearBy(getApplicationContext(), atm1, latlong);
                    rvAtm.setAdapter(adapter);
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<NearByAtm> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
switch (v.getId())
{
    case R.id.tvMapView:
        onClickMap();

}
    }

    private void onClickMap() {

        startActivity(new Intent(getApplicationContext(),map_nearby_activity.class).putExtra("geo",geolocation));
    }
}
