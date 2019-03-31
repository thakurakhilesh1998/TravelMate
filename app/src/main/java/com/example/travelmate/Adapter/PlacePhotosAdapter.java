package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.travelmate.R;

import java.util.ArrayList;

public class PlacePhotosAdapter extends RecyclerView.Adapter<PlacePhotosAdapter.Holder> {
    Context context;
    ArrayList<String> photos;

    public PlacePhotosAdapter(Context context, ArrayList<String> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.placesphotos, viewGroup, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        Glide.with(context).load(photos.get(i)).into(holder.ivPlacesPhotos);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivPlacesPhotos;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ivPlacesPhotos = itemView.findViewById(R.id.ivPlacePhotos);
        }
    }
}
