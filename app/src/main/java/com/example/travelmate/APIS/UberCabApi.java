package com.example.travelmate.APIS;

import com.example.travelmate.UberCab.UberCab;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class UberCabApi {
    public static Retrofit retrofit;
    public static String URL = "https://sandbox-api.uber.com/v1/estimates/";

    public static ApiInterface UberCab() {
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;
    }


    public interface ApiInterface {
        @GET("price?")
        Call<UberCab> getCabDetails(@Query(value = "Content-Type", encoded = true) String content, @Query(value = "server_token", encoded = true) String server_token, @Query(value = "start_latitude", encoded = true) String startlat, @Query(value = "start_longitude", encoded = true) String startlang, @Query(value = "end_latitude", encoded = true) String endlat, @Query(value = "end_longitude", encoded = true) String endlong);
    }
}
