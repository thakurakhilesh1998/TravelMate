package com.example.travelmate;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelmate.Adapter.PlacesAdapter;

import java.util.ArrayList;


public class Places extends Fragment {

    RecyclerView rvPlaces;
    ArrayList<Integer> placesimages;

    public Places() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        placesimages=new ArrayList<>();

        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlaces=view.findViewById(R.id.rvPlaces);
        RecyclerView.LayoutManager manager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rvPlaces.setLayoutManager(manager);
        placesimages.add(R.drawable.bilaspur);
        placesimages.add(R.drawable.dharamshala);

        placesimages.add(R.drawable.bilaspur);
        placesimages.add(R.drawable.dharamshala);
            placesimages.add(R.drawable.bilaspur);
        placesimages.add(R.drawable.dharamshala);

        PlacesAdapter placesAdapter=new PlacesAdapter(getContext(),placesimages);
        rvPlaces.setAdapter(placesAdapter);


    }


}
