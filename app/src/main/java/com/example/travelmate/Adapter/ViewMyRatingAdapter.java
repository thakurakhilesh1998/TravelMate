package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travelmate.R;
import com.example.travelmate.utility.SaveRating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMyRatingAdapter extends RecyclerView.Adapter<ViewMyRatingAdapter.Holder> {

    Context context;

    DataSnapshot dataSnapshot;
    ArrayList<String> size;
    String geolocation1;

    public ViewMyRatingAdapter(Context context, ArrayList<String> size, DataSnapshot dataSnapshot, String geolocation1) {
        this.context = context;
        this.size = size;
        this.dataSnapshot = dataSnapshot;
        this.geolocation1 = geolocation1;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewrating, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        Log.e("size", String.valueOf(size.size()));

        FirebaseDatabase.getInstance().getReference().child(geolocation1).child("Rating").child(size.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    SaveRating rating = dataSnapshot.getValue(SaveRating.class);
                    holder.tvRatingName.setText(rating.getReviewtitle());
                    holder.tvUserReview.setText(rating.getReview());
                    holder.rbUserRating.setRating(rating.getRating());
                    int i = rating.getDisplayName().indexOf(' ');
                    String word = rating.getDisplayName().substring(0,i);
                    holder.name.setText(word);
                    Glide.with(context).load(rating.getPhotoUrl()).into(holder.profile);
                    holder.rating.setText(String.valueOf(rating.getRating()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return size.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvRatingName, tvUserReview,name,rating;
        ImageView profile;
        RatingBar rbUserRating;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            profile=itemView.findViewById(R.id.profile);
            tvRatingName = itemView.findViewById(R.id.tvRatingName);
            tvUserReview = itemView.findViewById(R.id.tvUserReview);
            rbUserRating = itemView.findViewById(R.id.rbUserRating);
            rating=itemView.findViewById(R.id.rating);
        }
    }
}
