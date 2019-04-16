package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelmate.R;
import com.example.travelmate.UberCab.UberCab;

import retrofit2.Response;

public class CabDetailsAdapter extends RecyclerView.Adapter<CabDetailsAdapter.Holder> {

    Context context;
    Response<UberCab> response;

    public CabDetailsAdapter(Context context, Response<UberCab> response) {
        this.context = context;
        this.response = response;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cabdata, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        if (i == 0) {
            holder.tvHeading.setText("Most Common");
            holder.tvDistance.setText(String.valueOf(response.body().getPrices().get(2).getDistance()));
        }
        if (i == 1) {
            holder.tvHeading.setText("Economy");
        } else {
            holder.tvHeading.setText("More");
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {


        TextView tvHeading, tvTime, tvDistance, tvNumbers;
        Button btnConfirm;
        LinearLayout linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNumbers = itemView.findViewById(R.id.tvNumbers);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
