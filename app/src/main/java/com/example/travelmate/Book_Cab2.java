package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.neno0o.ubersdk.Activites.Authentication;
import com.neno0o.ubersdk.Endpoints.Models.Requests.UberRequestBody;
import com.neno0o.ubersdk.Uber;

public class Book_Cab2 extends AppCompatActivity {
    private static final int UBER_AUTHENTICATION = 12;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__cab2);
        Uber.getInstance().init(getResources().getString(R.string.client_id),
                getResources().getString(R.string.client_secret),
                getResources().getString(R.string.server_token),
                getResources().getString(R.string.redirect_uri));
        Intent intent = new Intent(getApplicationContext(), Authentication.class);
        startActivityForResult(intent, UBER_AUTHENTICATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == UBER_AUTHENTICATION) {
            Log.e("data uber", data.getDataString());
            requestUberCab();
        }
    }

    private void requestUberCab() {




    }
}
