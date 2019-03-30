package com.example.travelmate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travelmate.R;
import com.example.travelmate.TouristPlace_activity;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.Holder> {


    Context context;
    ArrayList<String> placesimages, name, geolocation;

    public PlacesAdapter(Context context, ArrayList<String> placesimages, ArrayList<String> name, ArrayList<String> geolocation) {
        this.name = name;

        this.placesimages = placesimages;
        this.context = context;
        this.geolocation = geolocation;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.places, viewGroup, false);


        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {

        holder.tvName.setText(name.get(i));
        Glide.with(context).load(placesimages.get(i)).into(holder.ivImage);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }


    private void click(int i) {

        Intent intent = new Intent(context, TouristPlace_activity.class);
        intent.putExtra("geocordinates", geolocation.get(i));
        context.startActivity(intent);



    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvName;
        LinearLayout linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }
}
