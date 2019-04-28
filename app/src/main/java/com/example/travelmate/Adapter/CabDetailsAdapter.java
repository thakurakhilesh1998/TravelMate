package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

        switch (response.body().getPrices().get(i).getDisplayName()) {
            case "UberXl":
                holder.vehicle.setImageResource(R.drawable.uberxl);
                break;
            case "POOL":
                holder.vehicle.setImageResource(R.drawable.ubergo);
                break;
            case "UberGo":
                holder.vehicle.setImageResource(R.drawable.ubergo);
            case "MOTO":
                holder.vehicle.setImageResource(R.drawable.ubermoto);
                break;
            case "UberAuto":
                holder.vehicle.setImageResource(R.drawable.uberauto);
                break;
            case "HIRE":
                holder.vehicle.setImageResource(R.drawable.uberhire);
                break;
            case "HIRE GO":
                holder.vehicle.setImageResource(R.drawable.uberhire);
                break;
            case "Intercity":
                holder.vehicle.setImageResource(R.drawable.uberx);
                break;
            default:
                holder.vehicle.setImageResource(R.drawable.uberx);
        }
        holder.tvNumbers.setText(response.body().getPrices().get(i).getEstimate());
        Double distance = response.body().getPrices().get(i).getDistance() * 1.60934;
        String distance1 = distance.toString().substring(0, 3);
        holder.tvDistance.setText(distance1 + " km");
        int duration = response.body().getPrices().get(i).getDuration() / 60;
        holder.tvTime.setText(String.valueOf(duration + " min"));
        holder.servicename.setText(response.body().getPrices().get(i).getDisplayName());
        holder.btnConfirm.setText("Confirm "+response.body().getPrices().get(i).getDisplayName());

    Log.e("data",response.body().getPrices().get(i).getProductId());
        Log.e("data", String.valueOf(response.body().getPrices().get(i).getSurgeMultiplier()));

    }

    @Override
    public int getItemCount() {
        return response.body().getPrices().size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvTime, tvDistance, tvNumbers, servicename;
        Button btnConfirm;
        ImageView vehicle;
        LinearLayout linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            vehicle = itemView.findViewById(R.id.vehicle);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNumbers = itemView.findViewById(R.id.tvNumbers);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            servicename = itemView.findViewById(R.id.servicename);
        }
    }
}
