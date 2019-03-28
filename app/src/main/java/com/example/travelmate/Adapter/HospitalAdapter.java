package com.example.travelmate.Adapter;

import android.content.Context;
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

import java.util.List;

public class HospitalAdapter  extends RecyclerView.Adapter<HospitalAdapter.Holder>
{
    Context context;
    List<Result> hospital;
    public HospitalAdapter(Context context, List<Result>hospital) {
        this.context = context;
        this.hospital = hospital;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearbyhospital, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.tvName.setText(hospital.get(i).getName());
        Log.e("name",hospital.get(0).getName());
        holder.tvRating.setText(String.valueOf(hospital.get(i).getRating()));
        holder.tvOpeing.setText(hospital.get(i).getVicinity());
    }

    @Override
    public int getItemCount() {
        return hospital.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvName, tvOpeing,tvRating;
        Button btnOnMap;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvOpeing = itemView.findViewById(R.id.tvOpeningHours);

            tvRating = itemView.findViewById(R.id.tvRating);
            btnOnMap = itemView.findViewById(R.id.btnOnMap);
        }
    }
}
