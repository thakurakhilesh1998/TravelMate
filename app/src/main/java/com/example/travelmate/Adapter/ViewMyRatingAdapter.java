package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.travelmate.R;

public class ViewMyRatingAdapter extends RecyclerView.Adapter<ViewMyRatingAdapter.Holder> {

    Context context;

    public ViewMyRatingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewrating, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvRatingName, tvUserReview;
        RatingBar rbUserRating;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvRatingName = itemView.findViewById(R.id.tvRatingName);
            tvUserReview = itemView.findViewById(R.id.tvUserReview);
            rbUserRating = itemView.findViewById(R.id.rbUserRating);
        }
    }
}
