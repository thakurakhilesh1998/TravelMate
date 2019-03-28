package com.example.travelmate.Adapter;

import android.content.Context;
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

    public NearByAdapter(FragmentManager fm, Context context, int count, ArrayList<String> name) {
        super(fm);
        this.context = context;
        this.count = count;
        this.name = name;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new Atmfragment();
            case 1:
                return new HospitalFragment();
            case 2:
                return new FoodFragment();
            case 3:
                return new HotelFragment();
            case 4:
                return new PetrolStationFragment();
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
