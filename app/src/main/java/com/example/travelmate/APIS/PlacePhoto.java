package com.example.travelmate.APIS;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class PlacePhoto {
    public static Retrofit retrofit;
    public static String URL = "https://maps.googleapis.com/maps/api/place/";


    public static ApiInterface PlacePhoto() {
        retrofit = new Retrofit.Builder().baseUrl(URL).build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;

    }

    public interface ApiInterface {


        @GET("photo?")
        Call<PlacePhotosClass> getPhoto(@Query(value = "maxwidth", encoded = true) String maxwidth, @Query(value = "maxheight", encoded = true) String maxheight, @Query(value = "photoreference", encoded = true) String photorefrence, @Query(value = "key", encoded = true) String key);
    }
}
