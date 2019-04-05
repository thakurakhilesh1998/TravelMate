package com.example.travelmate.Adapter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelmate.APIS.DistanceApiHitter;
import com.example.travelmate.Distance.Distance;
import com.example.travelmate.R;
import com.example.travelmate.TouristPlace_activity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.Holder> {


    Context context;
    ArrayList<String> placesimages, name, geolocation;
    ProgressDialog mmprogressDialog;

    static String KEY = "AIzaSyCOggg7f0D3iWZOQSLOKbo0BWrbQ9Y6ymw";
    FusedLocationProviderClient fusedLocationProviderClient;
    String currentLatlong;
    String units = "metric";


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
    public void onBindViewHolder(@NonNull final Holder holder, final int i) {
        mmprogressDialog = new ProgressDialog(context);
        mmprogressDialog.setMessage("Wait...Fetching...Your Places");
        mmprogressDialog.setCanceledOnTouchOutside(false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLatlong = location.getLatitude() + "," + location.getLongitude();
                getDistanceTime(currentLatlong, i, holder);
            }
        });

    }

    private void getDistanceTime(String currentLatlong, final int i, final Holder holder) {
        geolocation.set(i, addChar(geolocation.get(i), '.', 2));
        geolocation.set(i, addChar(geolocation.get(i), '.', 12));

        Call<Distance> getDistance = DistanceApiHitter.DistanceApiHitter().getDistance(units, currentLatlong, geolocation.get(i), KEY);
        getDistance.enqueue(new Callback<Distance>() {
            @Override
            public void onResponse(Call<Distance> call, Response<Distance> response) {

                if (true) {
                    holder.tvName.setText(name.get(i));
                    Glide.with(context).load(placesimages.get(i)).into(holder.ivImage);
//                    holder.tvDistance.setText("Around " + response.body().getRows().get(0).getElements().get(0).getDistance().getText() + " Away");
//                    holder.tvTime.setText(" " + response.body().getRows().get(0).getElements().get(0).getDuration().getText());
                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            click(i);
                        }

                    });
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Distance> call, Throwable t) {

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

    public String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvName, tvDistance, tvTime;
        LinearLayout linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvTime = itemView.findViewById(R.id.tvTime);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }
}
