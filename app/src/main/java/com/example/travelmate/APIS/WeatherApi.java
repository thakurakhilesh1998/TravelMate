package com.example.travelmate.APIS;


import com.example.travelmate.weather.Weather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class WeatherApi {
    public static Retrofit retrofit;

    static String URL = "http://dataservice.accuweather.com/forecasts/";

    public static ApiInterface WeatherApi() {
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;

    }

    public interface ApiInterface {
        @GET("v1/daily/5day/{locationkey}?")
        Call<Weather> getWeather(@Path("locationkey") String locationkey, @Query(value = "apikey", encoded = true) String key, @Query(value = "details", encoded = true) String details, @Query(value = "metric", encoded = true) String metric);
    }
}
