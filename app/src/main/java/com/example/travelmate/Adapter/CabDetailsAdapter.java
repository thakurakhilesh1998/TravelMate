package com.example.travelmate.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travelmate.BookCab;
import com.example.travelmate.Book_Cab2;
import com.example.travelmate.R;
import com.neno0o.ubersdk.Activites.Authentication;
import com.neno0o.ubersdk.Endpoints.Models.Prices.Prices;
import com.neno0o.ubersdk.Endpoints.Models.Products.Products;

public class CabDetailsAdapter extends RecyclerView.Adapter<CabDetailsAdapter.Holder> {

    private static final int UBER_AUTHENTICATION = 12;
    Context context;
    Products response;
    BookCab bookCab;
    Prices prices;

    public CabDetailsAdapter(BookCab bookCab, Context context, Products response, Prices prices) {
        this.context = context;
        this.response = response;
        this.bookCab = bookCab;
        this.prices = prices;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cabdata, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        holder.servicename.setText(response.getProducts().get(i).getDisplayName());
        Glide.with(context).load(response.getProducts().get(i).getImage()).into(holder.vehicle);
        holder.btnConfirm.setText(response.getProducts().get(i).getDisplayName());
        holder.tvCapacity.setText(String.valueOf("Capacity: "+response.getProducts().get(i).getCapacity()));
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(bookCab);
                dialog.setContentView(R.layout.cab);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                ImageView ivCabImage = dialog.findViewById(R.id.ivCanImage);
                TextView tvCabName = dialog.findViewById(R.id.tvCabName);
                final TextView tvPrice = dialog.findViewById(R.id.tvPrice);
                Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
                Glide.with(context).load(response.getProducts().get(i).getImage()).into(ivCabImage);
                tvCabName.setText(response.getProducts().get(i).getDisplayName());
                tvPrice.setText(prices.getPrices().get(i).getEstimate());
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,Book_Cab2.class));
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.getProducts().size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvNumbers, servicename, tvCapacity;
        Button btnConfirm;
        ImageView vehicle;
        LinearLayout linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            vehicle = itemView.findViewById(R.id.vehicle);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            servicename = itemView.findViewById(R.id.servicename);
        }
    }
}
