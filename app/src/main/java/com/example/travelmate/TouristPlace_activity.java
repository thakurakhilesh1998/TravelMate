package com.example.travelmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelmate.APIS.LocationKeyApi;
import com.example.travelmate.APIS.WeatherApi;
import com.example.travelmate.Adapter.PlacePhotosAdapter;
import com.example.travelmate.locationkey.LocationKey;
import com.example.travelmate.weather.Weather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TouristPlace_activity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvPhotos;
    ArrayList<Integer> photos;
    TextView tvPlaceName, tvAbout, tvviewMap, tvNearByPlaces;
    ImageView ivWeatherIcon;
    TextView tvTemp, tvHeadline, tvWindspeed;
    LinearLayout weather;
    String locationkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_place_activity);
        findIds();
        photos = new ArrayList<>();
        addPhoto();
        recyclerViewPhotos();
        // getLocationKey();
        getWeatherForecast();
        tvviewMap.setOnClickListener(this);
        weather.setOnClickListener(this);
        tvNearByPlaces.setOnClickListener(this);

    }

    private void getLocationKey() {


        Call<LocationKey> locationkey = LocationKeyApi.LocationKeyApi().getLocationKey();
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
        locationkey = response.body().getKey();
//        getWeatherForecast();
    }

    private void getWeatherForecast() {

        Call<Weather> weatherCall = WeatherApi.WeatherApi().getWeather();
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    putWeatgerData(response);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

            }
        });

    }

    private void putWeatgerData(Response<Weather> response) {
        tvHeadline.setText(response.body().getDailyForecasts().get(0).getDay().getIconPhrase());
        Double temp = response.body().getDailyForecasts().get(0).getRealFeelTemperature().getMaximum().getValue();
        tvTemp.setText(String.valueOf(temp));
        tvWindspeed.setText(response.body().getDailyForecasts().get(0).getAirAndPollen().get(0).getType());

    }


    private void findIds() {

        rvPhotos = findViewById(R.id.rvPhotos);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvAbout = findViewById(R.id.tvAbout);
        tvviewMap = findViewById(R.id.tvviewOnMap);
        tvNearByPlaces = findViewById(R.id.tvNearByPlaces);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        tvTemp = findViewById(R.id.tvTemp);
        tvWindspeed = findViewById(R.id.tvwinspeed);
        tvHeadline = findViewById(R.id.tvHeadline);
        weather = findViewById(R.id.weatherlayout);

    }

    private void addPhoto() {
        photos.add(R.drawable.dharamshala);

        photos.add(R.drawable.bilaspur);
    }

    private void recyclerViewPhotos() {

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(manager);
        PlacePhotosAdapter placePhotosAdapter = new PlacePhotosAdapter(this, photos);
        rvPhotos.setAdapter(placePhotosAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvviewOnMap:
                viewOnMap();
                break;
            case R.id.weatherlayout:
                onWeatherClick();
                break;
            case R.id.tvNearByPlaces:
                onNearByPlacesClicked();
                break;


        }
    }

    private void onNearByPlacesClicked() {
startActivity(new Intent(this,NearByPlacesActivity.class));

    }

    private void onWeatherClick() {


        startActivity(new Intent(getApplicationContext(), weatheractivity.class));
    }


    private void viewOnMap() {
        startActivity(new Intent(this, map_activity.class));
    }
}
