package com.example.travelmate.utility;

public class substringGeolocation {


    public static String getLatitude(String geolocation) {
        String lat2 = geolocation.substring(0, 9);

        return lat2;
    }

    public static String getLongitude(String geolocation) {
        String lon2 = geolocation.substring(10, 19);
        return lon2;
    }
}
