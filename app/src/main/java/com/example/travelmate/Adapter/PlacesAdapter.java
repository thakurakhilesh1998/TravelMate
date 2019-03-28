package com.example.travelmate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.travelmate.R;
import com.example.travelmate.TouristPlace_activity;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.Holder> implements View.OnClickListener {


    Context context;
    ArrayList<Integer> placesimages;

    public PlacesAdapter(Context context, ArrayList<Integer> placesimages) {

        this.placesimages=placesimages;
        this.context = context;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.places, viewGroup,false);


        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {


    holder.ivImage.setImageResource(placesimages.get(i));
    holder.ivImage.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return placesimages.size();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ivImage:
            click();
        }

    }

    private void click() {
        context.startActivity(new Intent(context, TouristPlace_activity.class));


    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        public Holder(@NonNull View itemView) {
            super(itemView);
            ivImage=itemView.findViewById(R.id.ivImage);
        }
    }
}
