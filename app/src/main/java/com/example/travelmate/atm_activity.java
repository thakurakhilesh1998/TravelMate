package com.example.travelmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class atm_activity extends AppCompatActivity {
    public static final String RADIUS = "1000";
    List<Result> atm1;
    RecyclerView rvAtm;
    String types;
    TextView tvTitle;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    String geolocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_activity);
        progressDialog = new ProgressDialog(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvAtm = findViewById(R.id.rvAtm);
        tvTitle = findViewById(R.id.tvTitle);
        Intent intent = getIntent();
        progressDialog.setMessage("Wait fetching places...");
        progressDialog.setCanceledOnTouchOutside(false);
        types = intent.getStringExtra("type");
        setTitles(types);
        progressDialog = new ProgressDialog(getApplicationContext());
        onBackButton();
        geolocation = intent.getStringExtra("geocoordinates2");
        String lat = substringGeolocation.getLatitude(geolocation);
        String longitude = substringGeolocation.getLongitude(geolocation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvAtm.setLayoutManager(manager);
        getDataFromApi(lat, longitude);

    }

    private void setTitles(String types) {
        switch (types) {
            case "cafe":
                tvTitle.setText("Restaurant");
                break;
            case "atm":
                tvTitle.setText("Atm");
                break;
            case "lodging":
                tvTitle.setText("Hotels");
                break;
            case "parking":
                tvTitle.setText("Parking");
                break;
            case "hospital":
                tvTitle.setText("Hospital");
                break;
            case "shopping_mall":
                tvTitle.setText("Shops");
                break;
            case "bus_station":
                tvTitle.setText("Bus Stop");
        }

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


    private void onClickMap() {

        startActivity(new Intent(getApplicationContext(), map_nearby_activity.class).putExtra("geo", geolocation).putExtra("type", types));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mapview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.viewonMap:
                onClickMap();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onBackButton() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
    }

}
