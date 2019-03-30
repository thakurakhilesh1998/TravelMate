package com.example.travelmate.APIS;

import com.example.travelmate.locationkey.LocationKey;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class LocationKeyApi {

    public static Retrofit retrofit;
    static String URl = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/";


    public static ApiInterface LocationKeyApi() {

        retrofit = new Retrofit.Builder().baseUrl(URl).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;

    }

 public interface ApiInterface {
        @GET("search?")
        Call<LocationKey> getLocationKey(@Query(value = "apikey",encoded = true)String key
        ,@Query(value = "q",encoded = true)String location,@Query(value = "details",encoded = true)String details);

//apikey=cqD52k5cnb9H0YdLoKcLLrZ13x9evzhj&q=30.7046%2C76.7179&details=true"
    }
}
