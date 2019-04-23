package com.example.travelmate;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.travelmate.Adapter.NearByAdapter;
import com.example.travelmate.utility.PrefLocation;

import java.util.ArrayList;

public class NearByPlaces1 extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<String> name;
    String geolocation;
    PrefLocation prefLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);
        prefLocation = new PrefLocation(getApplicationContext());
        findIds();
        geolocation = prefLocation.getLatitude() + "," + prefLocation.getLangitude();
        NearByAdapter myadapter = new NearByAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount(), name, geolocation);
        viewPager.setAdapter(myadapter);
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

    }
}