package com.example.travelmate.Adapter;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelmate.APIS.DistanceApiHitter;
import com.example.travelmate.Distance.Distance;
import com.example.travelmate.R;
import com.example.travelmate.TouristPlace_activity;
import com.example.travelmate.utility.PrefLocation;
import com.example.travelmate.utility.SaveRating;
import com.example.travelmate.utility.constants;
import com.example.travelmate.utility.util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.Holder> {


    Context context;
    ArrayList<String> placesimages, name, geolocation, geolocation1;
    ProgressDialog mmprogressDialog;
    FusedLocationProviderClient fusedLocationProviderClient;
    String currentLatlong;
    String units = "metric";
    PrefLocation prefLocation;



    public PlacesAdapter(Context context, ArrayList<String> placesimages, ArrayList<String> name, ArrayList<String> geolocation, ArrayList<String> geolocation1) {
        this.name = name;
        this.placesimages = placesimages;
        this.context = context;
        this.geolocation = geolocation;
        this.geolocation1 = geolocation1;
    }

    public String removeChar(String str, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.deleteCharAt(position);
        return sb.toString();
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
        prefLocation = new PrefLocation(context);
        currentLatlong = prefLocation.getLatitude() + "," + prefLocation.getLangitude();
        getDistanceTime(currentLatlong, i, holder);
    }

    private void getDistanceTime(String currentLatlong, final int i, final Holder holder) {
       final ArrayList<Float> ratings = new ArrayList<>();
        final long[] childnumber = new long[1];
        FirebaseDatabase.getInstance().getReference().child(geolocation.get(i)).child("Rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    childnumber[0] = dataSnapshot.getChildrenCount();
                    ArrayList<String> geo1 = new ArrayList<>();
                    geo1 = geolocation1;
                    geo1.set(i, removeChar(geolocation1.get(i), 2));
                    geo1.set(i, removeChar(geolocation1.get(i), 11));
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference().child(geo1.get(i)).child("Rating").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChildren()) {
                                    SaveRating saveRating = dataSnapshot.getValue(SaveRating.class);
                                    ratings.add(saveRating.getRating());
                                    sum(ratings);
                                    Log.e("ratings float", String.valueOf(roundOneDecimals(sum(ratings) / childnumber[0])));
                                    holder.totalRating.setText(String.valueOf(childnumber[0]));
                                    holder.rating.setText(String.valueOf(roundOneDecimals(sum(ratings) / childnumber[0])));
                                    holder.rbRating.setRating(roundOneDecimals(sum(ratings) / childnumber[0]));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
        geolocation.set(i, addChar(geolocation.get(i), '.', 2));
        geolocation.set(i, addChar(geolocation.get(i), '.', 12));
        Call<Distance> getDistance = DistanceApiHitter.DistanceApiHitter().getDistance(units, currentLatlong, geolocation.get(i), constants.KEY);
        getDistance.enqueue(new Callback<Distance>() {
            @Override
            public void onResponse(Call<Distance> call, Response<Distance> response) {
                if (true) {
                    try {
                        holder.tvName.setText(name.get(i));
                        Glide.with(context).load(placesimages.get(i)).into(holder.ivImage);
                        holder.tvDistance.setText(response.body().getRows().get(0).getElements().get(0).getDistance().getText());
                        holder.tvTime.setText(" " + response.body().getRows().get(0).getElements().get(0).getDuration().getText());
                    } catch (Exception e) {
                        util.toast(context, e.getMessage());
                    }
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
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    private void click(int i) {

        Intent intent = new Intent(context, TouristPlace_activity.class).putExtra("geocordinates", geolocation1.get(i));
        context.startActivity(intent);
    }

    public String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

    public float sum(ArrayList<Float> ratings) {
        float sum = 0;
        for (int i = 0; i < ratings.size(); i++)
            sum += ratings.get(i);
        return sum;

    }

    float roundOneDecimals(float d) {
        DecimalFormat twoDForm = new DecimalFormat("#.#");

        return Float.valueOf(twoDForm.format(d));
    }


    public class Holder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvName, tvDistance, tvTime;
        LinearLayout linearLayout;
        TextView rating, totalRating;
        RatingBar rbRating;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvTime = itemView.findViewById(R.id.tvTime);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            rating = itemView.findViewById(R.id.ratings);
            totalRating = itemView.findViewById(R.id.totalrating);
            rbRating = itemView.findViewById(R.id.rbRatings);
        }
    }
}
