package com.example.travelmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.example.travelmate.APIS.LocationKeyApi;
import com.example.travelmate.APIS.WeatherApi;
import com.example.travelmate.Adapter.WeatherForecastAdapter;
import com.example.travelmate.locationkey.LocationKey;
import com.example.travelmate.utility.weathericon;
import com.example.travelmate.weather.Weather;

import retrofit2.Call;
import retrofit2.Callback;

import com.example.travelmate.utility.*;

import retrofit2.Response;

public class weatheractivity extends AppCompatActivity {
    ImageView ivIcon;
    RecyclerView recyclerView;
    String geolocation;
    String KEY = "YU0QPtFwgBh7jzA4eGELHprxq28AJW5U";
    String details = "true";
    String locationkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatheractivity);
        findids();
        Intent intent = getIntent();
        Log.e("geolocation", geolocation);
        String lat = substringGeolocation.getLatitude(geolocation);
        String longitude = substringGeolocation.getLongitude(geolocation);
        getLocationKey(lat, longitude);
    }

    private void forecastWeather(Response<Weather> response) {


        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(this, response);
        recyclerView.setAdapter(adapter);
    }

    private void findids() {

        ivIcon = findViewById(R.id.ivIcon);
        recyclerView = findViewById(R.id.rvweatherforecast);
    }


    private void getLocationKey(String lat, String longitude) {

        String latlong = lat + "%2C" + longitude;

        Call<LocationKey> locationkey = LocationKeyApi.LocationKeyApi().getLocationKey(KEY, latlong, details);
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
        Log.e("locationkey", locationkey);

        getWeatherForecast(locationkey);
    }

    private void getWeatherForecast(String locationkey) {
        Call<Weather> forecast = WeatherApi.WeatherApi().getWeather(locationkey, KEY, details, details);
        forecast.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    getWeather(response);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

            }
        });
    }

    private void getWeather(Response<Weather> response) {
        int getIcon;
        int icon = response.body().getDailyForecasts().get(0).getDay().getIcon();
        getIcon = weathericon.WeatherIcon(icon);
        ivIcon.setImageResource(getIcon);
        forecastWeather(response);
    }
}
