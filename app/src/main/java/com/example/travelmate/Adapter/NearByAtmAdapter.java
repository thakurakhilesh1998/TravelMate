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
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travelmate.APIS.DistanceApiHitter;
import com.example.travelmate.Distance.Distance;
import com.example.travelmate.NearByAtm.Result;
import com.example.travelmate.R;
import com.example.travelmate.map_activity;
import com.example.travelmate.utility.constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByAtmAdapter extends RecyclerView.Adapter<NearByAtmAdapter.Holder> {

    Context context;
    List<Result> atm;
    String latlong;

    public NearByAtmAdapter(Context context, List<Result> atm, String latlong) {
        this.context = context;
        this.atm = atm;
        this.latlong = latlong;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearbyatm, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        String lat = String.valueOf(atm.get(i).getGeometry().getLocation().getLat());
        String lng = String.valueOf(atm.get(i).getGeometry().getLocation().getLng());
        getDistance(holder, i, lat, lng);

    }

    private void getDistance(final Holder holder, final int i, final String lat, final String lng) {


        Call<Distance> getDistance = DistanceApiHitter.DistanceApiHitter().getDistance(constants.units, latlong, lat + "," + lng, constants.KEY);
        getDistance.enqueue(new Callback<Distance>() {
            @Override
            public void onResponse(Call<Distance> call, Response<Distance> response) {


                holder.tvName.setText(atm.get(i).getName());
                Log.e("name", atm.get(0).getName());
                try {
                    holder.tvRating.setText(atm.get(i).getUserRatingsTotal());
                } catch (Exception e) {
                    holder.tvRating.setText("");
                }
                holder.tvAddress.setText(atm.get(i).getVicinity());


                try {
                    holder.tvDistance.setText(response.body().getRows().get(0).getElements().get(0).getDistance().getText());
                } catch (Exception e) {
                    Log.e("exception", e.getMessage());
                }

                try {
                    Glide.with(context).load(atm.get(i).getIcon()).into(holder.ivImage);
                } catch (Exception e) {
                    Log.e("exception", e.getMessage());
                }
                try {
                    holder.rbRating.setRating((float) atm.get(i).getRating());
                } catch (Exception e) {
                    holder.rbRating.setRating((float) 0.0);
                }

                try {
                    holder.tvRating1.setText(String.valueOf(atm.get(i).getRating()));
                } catch (Exception e) {
                    holder.tvRating1.setText("");
                }
                holder.btnOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, map_activity.class).putExtra("lat", lat).putExtra("lang", lng));
                    }
                });
            }

            @Override
            public void onFailure(Call<Distance> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return atm.size();


    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView tvName, tvRating1, tvDistance, tvRating, tvAddress;
        LinearLayout btnOnMap;
        ImageView ivImage;
        RatingBar rbRating;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRating1 = itemView.findViewById(R.id.tvratings1);
            btnOnMap = itemView.findViewById(R.id.btnOnMap);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            ivImage = itemView.findViewById(R.id.ivImage);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvRating = itemView.findViewById(R.id.tvratings);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }
    }
}
