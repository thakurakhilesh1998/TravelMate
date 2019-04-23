package com.example.travelmate.utility;

import android.content.Context;
import android.location.LocationManager;

public class GpsEnabled {


    public static boolean isEnabled(Context context) {

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;

    }
}
