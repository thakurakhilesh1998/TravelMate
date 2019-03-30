package com.example.travelmate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.travelmate.APIS.DistanceApiHitter;
import com.example.travelmate.Distance.Distance;
import com.example.travelmate.NearByAtm.Result;
import com.example.travelmate.R;
import com.example.travelmate.map_activity;
import com.example.travelmate.utility.*;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByAtmAdapter extends RecyclerView.Adapter<NearByAtmAdapter.Holder>  {

    Context context;
    List<Result> atm;
    String latlong;
//    String units="metric";
//    String key="AIzaSyCOggg7f0D3iWZOQSLOKbo0BWrbQ9Y6ymw";

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

        getDistance(holder,i,lat,lng);

    }

    private void getDistance(final Holder holder, final int i, String lat, String lng) {

        Call<Distance> getDistance= DistanceApiHitter.DistanceApiHitter().getDistance(constants.units,latlong,lat+","+lng,constants.KEY);
        getDistance.enqueue(new Callback<Distance>() {
            @Override
            public void onResponse(Call<Distance> call, Response<Distance> response) {
                holder.tvName.setText(atm.get(i).getName());
                Log.e("name", atm.get(0).getName());
                holder.tvRating.setText(String.valueOf(atm.get(i).getRating()));
                holder.tvOpeing.setText(atm.get(i).getVicinity());
                Double d = atm.get(i).getRating();
                holder.tvRating.setText(String.valueOf(d));
                holder.tvDistance.setText(response.body().getRows().get(0).getElements().get(0).getDistance().getText());
                holder.btnOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   context.startActivity(new Intent(context,map_activity.class));
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

        TextView tvName, tvOpeing, tvRating, tvDistance;
        Button btnOnMap;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvOpeing = itemView.findViewById(R.id.tvOpeningHours);
            tvRating = itemView.findViewById(R.id.tvRating);
            btnOnMap = itemView.findViewById(R.id.btnOnMap);
            tvDistance = itemView.findViewById(R.id.tvDistance);

        }
    }
}
