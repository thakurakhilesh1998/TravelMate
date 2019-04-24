package com.example.travelmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelmate.APIS.LocationKeyApi;
import com.example.travelmate.APIS.WeatherApi;
import com.example.travelmate.Adapter.NearByImageAdapter;
import com.example.travelmate.Adapter.PlacePhotosAdapter;
import com.example.travelmate.locationkey.LocationKey;
import com.example.travelmate.utility.constants;
import com.example.travelmate.utility.weathericon;
import com.example.travelmate.weather.Weather;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cz.intik.overflowindicator.OverflowPagerIndicator;
import cz.intik.overflowindicator.SimpleSnapHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TouristPlace_activity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvPhotos;
    ArrayList<String> photos;
    TextView tvPlaceName, tvAbout, tvviewMap, tvRainProbablity;
    ImageView ivWeatherIcon;
    TextView tvTemp, tvHeadline, tvWindspeed;
    LinearLayout weather;
    String locationkey;
    String geolocation;
    String geolocation1;
    FirebaseDatabase database;
    Toolbar toolbar;
    RecyclerView rvnearby;
    DatabaseReference mRef;
    OverflowPagerIndicator overflow;
    String details = "true";
    ArrayList<Integer> images;
    ArrayList<String> name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_place_activity);
        setSupportActionBar(toolbar);
        images = new ArrayList<>();
        name = new ArrayList<>();
        setName();
        setImageList();
        findIds();
        photos = new ArrayList<>();
        Intent intent = getIntent();
        geolocation = intent.getStringExtra("geocordinates");
        Log.e("geo", geolocation);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        //  findlatlong(geolocation);
        getDataFromFirebase();
        // getWeatherForecast(locationkey);
        tvviewMap.setOnClickListener(this);
        weather.setOnClickListener(this);
        setNearByrecyclerView();
    }

    private void setName() {
        name.add("Food");
        name.add("Atm");
        name.add("Hotels");
        name.add("Petrol Station");
        name.add("Hospital");
    }

    private void setImageList() {
        images.add(R.drawable.food);
        images.add(R.drawable.atm);
        images.add(R.drawable.hotel);
        images.add(R.drawable.petrol);
        images.add(R.drawable.hospital);
    }

    private void setNearByrecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvnearby.setLayoutManager(manager);
        NearByImageAdapter nearByImageAdapter = new NearByImageAdapter(getApplicationContext(), images, name, geolocation);
        rvnearby.setAdapter(nearByImageAdapter);
    }

    private void getDataFromFirebase() {
        geolocation1 = geolocation;
        geolocation1 = geolocation1.substring(0, 2) + geolocation1.substring(3);
        geolocation1 = geolocation1.substring(0, 11) + geolocation1.substring(12);
        mRef.child(geolocation1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getFirebaseData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getFirebaseData(DataSnapshot dataSnapshot) {


        tvPlaceName.setText(dataSnapshot.child("Placename").getValue().toString());
        tvAbout.setText(dataSnapshot.child("About").getValue().toString());
        photos.add(dataSnapshot.child("Photos").child("Photo1").getValue().toString());
        photos.add(dataSnapshot.child("Photos").child("Photo2").getValue().toString());
        photos.add(dataSnapshot.child("Photos").child("Photo3").getValue().toString());
        recyclerViewPhotos(photos);

    }

    private void findlatlong(String geolocation) {


        Double latitide = Double.valueOf(this.geolocation.substring(0, 9));
        Double longitude = Double.valueOf(this.geolocation.substring(10, 19));

        String latlong = latitide + "%2C" + longitude;

        try {
            getLocationKey(latlong);
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }
    }

    private void getLocationKey(String latlong) {


        Call<LocationKey> locationkey = LocationKeyApi.LocationKeyApi().getLocationKey(constants.WeatherKEY, latlong, details);
        locationkey.enqueue(new Callback<LocationKey>() {
            @Override
            public void onResponse(Call<LocationKey> call, Response<LocationKey> response) {


                getKey(response);
            }

            @Override
            public void onFailure(Call<LocationKey> call, Throwable t) {

            }
        });
    }

    private void getKey(Response<LocationKey> response) {
        if (response != null) {
            locationkey = response.body().getKey();
            Log.e("locationkey", locationkey);
            getWeatherForecast(locationkey);
        } else {
            Log.e("error1", "error");
        }

    }

    private void getWeatherForecast(String locationkey) {

        Call<Weather> weatherCall = WeatherApi.WeatherApi().getWeather(locationkey, constants.WeatherKEY, details, details);
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                putWeatgerData(response);

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

            }
        });

    }

    private void putWeatgerData(Response<Weather> response) {

        tvHeadline.setText(String.valueOf(response.body().getDailyForecasts().get(0).getRealFeelTemperature().getMinimum().getValue()) + "°C");
        Double temp = response.body().getDailyForecasts().get(0).getRealFeelTemperature().getMaximum().getValue();
        tvTemp.setText(String.valueOf(temp) + "°C");
        tvWindspeed.setText(response.body().getDailyForecasts().get(0).getDay().getIconPhrase());
        tvRainProbablity.setText(String.valueOf("Rain Probablity:" + response.body().getDailyForecasts().get(0).getDay().getRainProbability()) + "%");
        int icon = response.body().getDailyForecasts().get(0).getDay().getIcon();
        ivWeatherIcon.setImageResource(weathericon.WeatherIcon(icon));
    }


    private void findIds() {
        toolbar = findViewById(R.id.toolbar);
        rvPhotos = findViewById(R.id.rvPhotos);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvAbout = findViewById(R.id.tvAbout);
        tvviewMap = findViewById(R.id.tvviewOnMap);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        tvTemp = findViewById(R.id.tvTemp);
        tvWindspeed = findViewById(R.id.tvwinspeed);
        tvHeadline = findViewById(R.id.tvHeadline);
        weather = findViewById(R.id.weatherlayout);
        overflow = findViewById(R.id.view_pager_indicator);
        tvRainProbablity = findViewById(R.id.tvRainProbablity);
        rvnearby = findViewById(R.id.rvnearby);

    }


    private void recyclerViewPhotos(ArrayList<String> photos1) {

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(manager);
        PlacePhotosAdapter placePhotosAdapter = new PlacePhotosAdapter(this, photos1, overflow);
        rvPhotos.setAdapter(placePhotosAdapter);
        overflow.attachToRecyclerView(rvPhotos);
        SimpleSnapHelper pagerSnapHelper = new SimpleSnapHelper(overflow);
        pagerSnapHelper.attachToRecyclerView(rvPhotos);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvviewOnMap:
                viewOnMap();
                break;
            case R.id.weatherlayout:
                onWeatherClick();
        }
    }


    private void onWeatherClick() {


        startActivity(new Intent(this, weatheractivity.class).putExtra("geocoordinates1", geolocation));
    }


    private void viewOnMap() {
        startActivity(new Intent(this, map_2_activity.class).putExtra("geocoordinatesmap", geolocation));
    }

    public String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }
}
