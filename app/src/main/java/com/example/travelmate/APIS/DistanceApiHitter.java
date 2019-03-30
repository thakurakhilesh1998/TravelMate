package com.example.travelmate.APIS;

import com.example.travelmate.Distance.Distance;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class DistanceApiHitter {

    public static Retrofit retrofit;
    public static String URL1 = "https://maps.googleapis.com/maps/api/distancematrix/";

    public static ApiInterface DistanceApiHitter() {
        retrofit = new Retrofit.Builder().baseUrl(URL1).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;
    }

    public interface ApiInterface {
        @GET("json?")
        Call<Distance> getDistance(@Query(value = "units", encoded = true) String units, @Query(value = "origins", encoded = true) String origins, @Query(value = "destinations", encoded = true) String destinations, @Query(value = "key", encoded = true) String key);
    }

}
