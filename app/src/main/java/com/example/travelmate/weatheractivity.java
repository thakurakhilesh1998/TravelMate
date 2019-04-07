package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelmate.APIS.LocationKeyApi;
import com.example.travelmate.APIS.WeatherApi;
import com.example.travelmate.Adapter.WeatherForecastAdapter;
import com.example.travelmate.locationkey.LocationKey;
import com.example.travelmate.utility.constants;
import com.example.travelmate.utility.substringGeolocation;
import com.example.travelmate.utility.weathericon;
import com.example.travelmate.weather.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class weatheractivity extends AppCompatActivity {
    TextView tvName, tvTemp, tvStatus, tvRain, tvTempMinMax;
    ImageView ivIcon;
    RecyclerView recyclerView;
    String geolocation;
    String details = "true";
    String locationkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatheractivity);
        findids();
        Intent intent = getIntent();
        geolocation = intent.getStringExtra("geocoordinates1");
//        Log.e("geolocation", geolocation);
        String lat = substringGeolocation.getLatitude(geolocation);
        String longitude = substringGeolocation.getLongitude(geolocation);
        getLocationKey(lat, longitude);
    }

    private void forecastWeather(Response<Weather> response, String cityName) {

        String min = response.body().getDailyForecasts().get(0).getTemperature().getMinimum().getValue().toString() + "°c";
        String max = response.body().getDailyForecasts().get(0).getTemperature().getMaximum().getValue().toString() + "°c";


        tvName.setText(cityName);
        tvTemp.setText(response.body().getDailyForecasts().get(0).getRealFeelTemperature().getMaximum().getValue().toString() + "°C");
        tvStatus.setText(response.body().getDailyForecasts().get(0).getDay().getIconPhrase());
        tvRain.setText(response.body().getDailyForecasts().get(0).getDay().getWind().getSpeed().getValue().toString() + response.body().getDailyForecasts().get(0).getDay().getWind().getSpeed().getUnit().toString());
        tvTempMinMax.setText(min + "-" + max);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(this, response);
        recyclerView.setAdapter(adapter);
    }

    private void findids() {

        tvName = findViewById(R.id.tvName);
        tvTemp = findViewById(R.id.tvTemp);
        tvStatus = findViewById(R.id.tvStatus);
        tvRain = findViewById(R.id.tvRain);
        tvTempMinMax = findViewById(R.id.tvTempMinMax);
        ivIcon = findViewById(R.id.ivIcon);
        recyclerView = findViewById(R.id.rvweatherforecast);
    }


    private void getLocationKey(String lat, String longitude) {

        String latlong = lat + "%2C" + longitude;

        Call<LocationKey> locationkey = LocationKeyApi.LocationKeyApi().getLocationKey(constants.WeatherKEY, latlong, details);
        locationkey.enqueue(new Callback<LocationKey>() {
            @Override
            public void onResponse(Call<LocationKey> call, Response<LocationKey> response) {

                String cityName = response.body().getEnglishName();
                getKey(response, cityName);
            }

            @Override
            public void onFailure(Call<LocationKey> call, Throwable t) {

            }
        });


    }

    private void getKey(Response<LocationKey> response, String cityName) {


        locationkey = response.body().getKey();
        Log.e("locationkey", locationkey);

        getWeatherForecast(locationkey, cityName);
    }

    private void getWeatherForecast(String locationkey, final String cityName) {
        Call<Weather> forecast = WeatherApi.WeatherApi().getWeather(locationkey, constants.WeatherKEY, details, details);
        forecast.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    getWeather(response, cityName);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

            }
        });
    }

    private void getWeather(Response<Weather> response, String cityName) {
        int getIcon;
        int icon = response.body().getDailyForecasts().get(0).getDay().getIcon();
        getIcon = weathericon.WeatherIcon(icon);
        ivIcon.setImageResource(getIcon);
        forecastWeather(response, cityName);
    }
}
