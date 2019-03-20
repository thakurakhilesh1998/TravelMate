package com.example.travelmate;

import android.content.Context;
import android.widget.Toast;

public class util {
    static void toast(Context context, String toprint) {
        Toast.makeText(context, toprint, Toast.LENGTH_SHORT).show();
    }
}
