package com.example.travelmate.APIS;

import com.example.travelmate.Distance.Distance;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class DistanceApiHitter {

    public static Retrofit retrofit;
    public static String URL1 = "https://maps.googleapis.com/maps/api/distancematrix/";

    public static ApiInterface DistanceApiHitter() {
        retrofit = new Retrofit.Builder().baseUrl(URL1).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;
    }

    public interface ApiInterface {
        @GET("json?units=metric&origins=30.7046,76.7179&destinations=31.4491,76.7048&key=AIzaSyCOggg7f0D3iWZOQSLOKbo0BWrbQ9Y6ymw")
        Call<Distance> getDistance();
    }

}
