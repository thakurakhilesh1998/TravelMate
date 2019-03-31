package com.example.travelmate.APIS;

import com.example.travelmate.Direction.Direction;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class DirectionApiHitter {

    public static Retrofit retrofit;
    static String BASEURL = "https://maps.googleapis.com/maps/api/directions/";

    public static ApiInterface DirectionApiHitter() {
        retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;

    }

    public interface ApiInterface {
        @GET("json?")
        Call<Direction> getDirection(@Query(value = "origin", encoded = true) String origin, @Query(value = "destination", encoded = true) String destination, @Query(value = "mode", encoded = true) String mode, @Query(value = "key", encoded = true) String key);
    }


}
