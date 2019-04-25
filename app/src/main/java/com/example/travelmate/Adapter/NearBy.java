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
import com.example.travelmate.APIS.PlacePhoto;
import com.example.travelmate.Distance.Distance;
import com.example.travelmate.NearByAtm.Result;
import com.example.travelmate.R;
import com.example.travelmate.map_activity;
import com.example.travelmate.utility.constants;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearBy extends RecyclerView.Adapter<NearBy.Holder> {
    Context context;
    String photoref;
    List<Result> atm1;
    String latlong;


    public NearBy(Context context, List<Result> atm1, String latlong) {

        this.context = context;
        this.atm1 = atm1;
        this.latlong = latlong;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.nearbyplaces, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        final String lat = String.valueOf(atm1.get(i).getGeometry().getLocation().getLat());
        final String lng = String.valueOf(atm1.get(i).getGeometry().getLocation().getLng());

        getDistance(holder, i, lat, lng);
    }

    private void getDistance(final Holder holder, final int i, final String lat, final String lng) {


        Call<Distance> getDistance = DistanceApiHitter.DistanceApiHitter().getDistance(constants.units, latlong, lat + "," + lng, constants.KEY);
        getDistance.enqueue(new Callback<Distance>() {
            @Override
            public void onResponse(Call<Distance> call, Response<Distance> response) {
                holder.tvPlaceName.setText(atm1.get(i).getName());
                try {
                    holder.tvRatings.setText(String.valueOf(atm1.get(i).getRating()));
                } catch (Exception e) {
                    holder.tvRatings.setText("");
                }
                holder.tvPlaceAddress.setText(atm1.get(i).getVicinity());
                holder.tvDistance.setText(response.body().getRows().get(0).getElements().get(0).getDistance().getText());
                try {
                    photoref = atm1.get(i).getPhotos().get(i).getPhotoReference();
                } catch (Exception e) {
                    photoref = "";
                    Log.e("msg", e.getMessage());
                }
                Call<ResponseBody> getphoto = PlacePhoto.PlacePhoto().getPhoto("200dp", "200dp", photoref, constants.KEY);
                getphoto.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("url", String.valueOf(response.body()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, map_activity.class).putExtra("lat", lat).putExtra("lang", lng));
                    }
                });

                Glide.with(context).load(atm1.get(0).getIcon()).into(holder.ivImage);
                holder.tvTotal.setText(String.valueOf("(" + atm1.get(i).getUserRatingsTotal() + ")"));
                try {
                    Float a = (float) atm1.get(i).getRating();
                    holder.rbRating.setRating(a);
                } catch (Exception e) {
                    holder.rbRating.setRating((float) 0.0);
                }
                //   holder.rbRating.setRating();

            }


            @Override
            public void onFailure(Call<Distance> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return atm1.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvPlaceName, tvPlaceAddress, tvRatings, tvDistance;
        LinearLayout linearLayout;
        RatingBar rbRating;
        TextView tvTotal;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvPlaceName = itemView.findViewById(R.id.tvPlaceName);
            tvPlaceAddress = itemView.findViewById(R.id.tvPlaceAddress);
            tvRatings = itemView.findViewById(R.id.tvratings);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            //   linearLayout = itemView.findViewById(R.id.linearLayout);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            linearLayout = itemView.findViewById(R.id.linearlayout2);
        }
    }
}
