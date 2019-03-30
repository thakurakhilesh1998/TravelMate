package com.example.travelmate.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.travelmate.NearByPlaces.Atmfragment;
import com.example.travelmate.NearByPlaces.FoodFragment;
import com.example.travelmate.NearByPlaces.HospitalFragment;
import com.example.travelmate.NearByPlaces.HotelFragment;
import com.example.travelmate.NearByPlaces.PetrolStationFragment;


import java.util.ArrayList;


public class NearByAdapter extends FragmentPagerAdapter {

    Context context;
    int count;
    ArrayList<String> name;
    String geolocation;
    Bundle bundle = new Bundle();


    public NearByAdapter(FragmentManager fm, Context context, int count, ArrayList<String> name, String geolocation) {
        super(fm);
        this.context = context;
        this.count = count;
        this.name = name;
        this.geolocation = geolocation;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:

                bundle.putString("geolocation", geolocation);
                Atmfragment atmfragment = new Atmfragment();
                atmfragment.setArguments(bundle);
                return atmfragment;
            case 1:
                bundle.putString("geolocation", geolocation);
                HospitalFragment hospitalfragment = new HospitalFragment();
                hospitalfragment.setArguments(bundle);
                return hospitalfragment;
            case 2:
                bundle.putString("geolocation", geolocation);
                FoodFragment foodfragment = new FoodFragment();
                foodfragment.setArguments(bundle);
                return foodfragment;
            case 3:
                bundle.putString("geolocation", geolocation);
                HotelFragment hotelfragment = new HotelFragment();
                hotelfragment.setArguments(bundle);
                return hotelfragment;
            case 4:
                bundle.putString("geolocation", geolocation);
                PetrolStationFragment petrolfragment = new PetrolStationFragment();
                petrolfragment.setArguments(bundle);
                return petrolfragment;
        }
        return null;

    }

    @Override
    public int getCount() {

        return count;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }
}
