package com.example.travelmate.APIS;

import com.example.travelmate.NearByAtm.NearByAtm;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NearbyApiHitter {

    public static Retrofit retrofit;

   public static String URL1 = "https://maps.googleapis.com/maps/api/place/";

    public static ApiInterface NearbyApiHitter() {
        retrofit = new Retrofit.Builder().baseUrl(URL1).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;

    }

public interface ApiInterface {
        @GET("nearbysearch/json?")
        Call<NearByAtm> getPlaces(@Query(value = "key", encoded = true) String key, @Query(value = "location", encoded = true) String latlang, @Query(value = "radius", encoded = true) String radius, @Query(value = "types", encoded = true) String types);
    }

}
