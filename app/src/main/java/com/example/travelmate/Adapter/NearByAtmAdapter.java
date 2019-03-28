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

import com.example.travelmate.NearByAtm.Result;
import com.example.travelmate.R;
import com.example.travelmate.map_activity;

import java.util.List;

public class NearByAtmAdapter extends RecyclerView.Adapter<NearByAtmAdapter.Holder> implements View.OnClickListener {

    Context context;
    List<Result> atm;

    public NearByAtmAdapter(Context context, List<Result> atm) {
        this.context = context;
        this.atm = atm;


    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.nearbyatm, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        holder.tvName.setText(atm.get(i).getName());
        Log.e("name", atm.get(0).getName());
        holder.tvRating.setText(String.valueOf(atm.get(i).getRating()));
        holder.tvOpeing.setText(atm.get(i).getVicinity());
        Double d = atm.get(i).getRating();
        holder.tvRating.setText(String.valueOf(d));
        holder.btnOnMap.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return atm.size();


    }

    @Override
    public void onClick(View v) {
        context.startActivity(new Intent(context, map_activity.class));
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
