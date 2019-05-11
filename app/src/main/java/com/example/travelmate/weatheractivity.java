package com.example.travelmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
    Toolbar toolbar;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatheractivity);
        findids();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait..Loading..Weather Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Snackbar.make(findViewById(android.R.id.content), "message", Snackbar.LENGTH_SHORT).show();
        setBackButton();
        Intent intent = getIntent();
        geolocation = intent.getStringExtra("geocoordinates1");
        String lat = substringGeolocation.getLatitude(geolocation);
        String longitude = substringGeolocation.getLongitude(geolocation);
        try {
            getLocationKey(lat, longitude);
        } catch (Exception e) {
            progressDialog.dismiss();
            Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    private void setBackButton() {

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
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
        progressDialog.dismiss();
    }

    private void findids() {

        tvName = findViewById(R.id.tvName);
        tvTemp = findViewById(R.id.tvTemp);
        tvStatus = findViewById(R.id.tvStatus);
        tvRain = findViewById(R.id.tvRain);
        tvTempMinMax = findViewById(R.id.tvTempMinMax);
        ivIcon = findViewById(R.id.ivIcon);
        recyclerView = findViewById(R.id.rvweatherforecast);
        toolbar = findViewById(R.id.weathertoolbar);
    }


    private void getLocationKey(String lat, String longitude) {

        String latlong = lat + "%2C" + longitude;

        Call<LocationKey> locationkey = LocationKeyApi.LocationKeyApi().getLocationKey(constants.WeatherKEY, latlong, details);
        locationkey.enqueue(new Callback<LocationKey>() {
            @Override
            public void onResponse(Call<LocationKey> call, Response<LocationKey> response) {
                if (response.isSuccessful()) {
                    try {
                        String cityName = response.body().getEnglishName();
                        getKey(response, cityName);
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), e.getMessage(), BaseTransientBottomBar.LENGTH_SHORT);
                    }
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LocationKey> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), t.getMessage(), BaseTransientBottomBar.LENGTH_SHORT);
            }
        });


    }

    private void getKey(Response<LocationKey> response, String cityName) {
        locationkey = response.body().getKey();
        try {
            getWeatherForecast(locationkey, cityName);
        } catch (Exception e) {
            progressDialog.dismiss();
            Snackbar.make(findViewById(android.R.id.content), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
        }
    }

    private void getWeatherForecast(String locationkey, final String cityName) {
        Call<Weather> forecast = WeatherApi.WeatherApi().getWeather(locationkey, constants.WeatherKEY, details, details);
        forecast.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    getWeather(response, cityName);
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                progressDialog.dismiss();
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
