package com.example.travelmate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelmate.R;
import com.example.travelmate.atm_activity;

import java.util.ArrayList;

public class NearByImageAdapter extends RecyclerView.Adapter<NearByImageAdapter.Holder> {
    ArrayList<Integer> images;
    ArrayList<String> name;
    Context context;
    String geolocation;

    public NearByImageAdapter(Context context, ArrayList<Integer> images, ArrayList<String> name, String geolocation) {
        this.context = context;
        this.images = images;
        this.name = name;
        this.geolocation = geolocation;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.nearbyimage, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        holder.image.setImageResource(images.get(i));
    //    holder.tvName.setText(name.get(i));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }


    private void onItemClick(int i) {

        switch (i) {
            case 0:
                context.startActivity(new Intent(context, atm_activity.class).putExtra("geocoordinates2", geolocation).putExtra("type", "food"));
                break;
            case 1:
                context.startActivity(new Intent(context, atm_activity.class).putExtra("geocoordinates2", geolocation).putExtra("type", "atm"));
                break;
            case 2:
                context.startActivity(new Intent(context, atm_activity.class).putExtra("geocoordinates2", geolocation).putExtra("type", "hotels"));
                break;
            case 3:
                context.startActivity(new Intent(context, atm_activity.class).putExtra("geocoordinates2", geolocation).putExtra("type", "petrol-pump"));
                break;
            case 4:
                context.startActivity(new Intent(context, atm_activity.class).putExtra("geocoordinates2", geolocation).putExtra("type", "hospital"));

        }


    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvName;
        LinearLayout linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivimage1);
            tvName = itemView.findViewById(R.id.tvName);
            linearLayout = itemView.findViewById(R.id.linearLayout1);
        }
    }
}
