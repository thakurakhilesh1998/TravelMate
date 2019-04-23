package com.example.travelmate.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefLocation {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

  public PrefLocation(Context context) {
        sharedPreferences = context.getSharedPreferences("Shared Preference",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLocation(String latitude, String langitude) {
        editor.putString("lat", latitude);
        editor.putString("lang", langitude);
        editor.commit();
  }

    public String getLatitude() {
        return sharedPreferences.getString("lat", "0");

    }

    public String getLangitude() {
        return sharedPreferences.getString("lang", "0");
    }
}
