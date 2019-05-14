package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.neno0o.ubersdk.Endpoints.Models.Requests.UberStatus;
import com.neno0o.ubersdk.Sandbox.Models.SandboxRequestBody;
import com.neno0o.ubersdk.Uber;

public class Book_Cab2 extends AppCompatActivity {
    private static final int UBER_AUTHENTICATION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__cab2);
    }

}
