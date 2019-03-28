package com.example.travelmate.APIS;


import com.example.travelmate.weather.Weather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class WeatherApi {
    public static Retrofit retrofit;

    static String URL = "http://dataservice.accuweather.com/forecasts/";

    public static ApiInterface WeatherApi() {

        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;

    }

  public interface ApiInterface {


        @GET("v1/daily/5day/3136635?apikey=YU0QPtFwgBh7jzA4eGELHprxq28AJW5U&details=true&metric=true")
    public Call<Weather> getWeather();

    }
}
