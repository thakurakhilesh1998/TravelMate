package com.example.travelmate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.travelmate.Adapter.NearByAdapter;
import com.example.travelmate.utility.PrefLocation;

import java.util.ArrayList;

public class NearByPlaces1 extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<String> name;
    String geolocation;
    PrefLocation prefLocation;
    Toolbar titletoolbar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        prefLocation = new PrefLocation(getApplicationContext());
        findIds();
        setSupportActionBar(titletoolbar);
        geolocation = prefLocation.getLatitude() + "," + prefLocation.getLangitude();
        Log.e("geolocation123", geolocation);
        NearByAdapter myadapter = new NearByAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount(), name, geolocation);
        viewPager.setAdapter(myadapter);
        progressDialog.dismiss();
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(10);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });


    }


    private void findIds() {
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        titletoolbar = findViewById(R.id.titletoolbar1);

    }
}