package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelmate.R;

import java.util.ArrayList;

public class NavAdapter extends RecyclerView.Adapter<NavAdapter.Holder> {

    Context context;
    ArrayList<String> distance, time, direction;
    ArrayList<String> manuer;

    public NavAdapter(Context context, ArrayList<String> distance, ArrayList<String> time, ArrayList<String> direction, ArrayList<String> manuer) {
        this.context = context;
        this.distance = distance;
        this.time = time;
        this.direction = direction;
        this.manuer = manuer;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.mapnavlayout, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        if (direction.get(i) == null) {
            holder.tvInstruction.setText("--");
        }
        if (distance.get(i) == null) {
            holder.tvDistance.setText("--");
        }
        if (time.get(i) == null) {
            holder.tvTime.setText("--");
        }
        if (manuer.get(i) == null) {
            holder.tvMain.setText("--");
        }
        if (manuer.get(i) != null && manuer.get(i).contains("left")) {
            holder.ivImage.setImageResource(R.drawable.left);
        }
        if (manuer.get(i) != null && manuer.get(i).contains("right")) {
            holder.ivImage.setImageResource(R.drawable.right);
        }
        if (manuer.get(i) != null && manuer.get(i).contains("Head")) {
            holder.ivImage.setImageResource(R.drawable.up);
        }
        holder.tvInstruction.setText(direction.get(i).trim().replaceAll(" +", " "));
        holder.tvDistance.setText(distance.get(i));
        holder.tvTime.setText("(" + time.get(i) + ")");
        if(manuer.get(i)!=null) {
            holder.tvMain.setText(manuer.get(i).substring(0, 1).toUpperCase() + manuer.get(i).substring(1));
        }
    }

    @Override
    public int getItemCount() {
        return direction.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvInstruction, tvTime, tvDistance, tvMain;
        ImageView ivImage;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvInstruction = itemView.findViewById(R.id.tvInstrcution);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvMain = itemView.findViewById(R.id.tvMian);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
