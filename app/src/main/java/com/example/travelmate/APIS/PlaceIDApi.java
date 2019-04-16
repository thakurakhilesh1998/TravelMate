package com.example.travelmate.APIS;

import com.example.travelmate.PlaceID.PlaceID;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class PlaceIDApi {
    public static Retrofit retrofit;
    public static String URL = "https://maps.googleapis.com/maps/api/place/details/";

    public static ApiInterface PlaceIDApi()

    {
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;
    }


   public interface ApiInterface {
        @GET("json?")
        Call<PlaceID> getLatlang(@Query(value = "placeid", encoded = true) String placeid, @Query(value = "key", encoded = true) String key);
    }
}
