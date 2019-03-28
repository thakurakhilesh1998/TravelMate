package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelmate.R;
import com.example.travelmate.weather.Weather;

import retrofit2.Response;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.Holder> {

    Context context;
    Response<Weather> response;

    public WeatherForecastAdapter(Context context, Response<Weather> response) {
        this.context = context;
        this.response = response;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.weatherforecast, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        String minTemp = String.valueOf(response.body().getDailyForecasts().get(i).getRealFeelTemperature().getMinimum().getValue());
        Log.e("minTemp", minTemp);
        String maxTemp = String.valueOf(response.body().getDailyForecasts().get(i).getRealFeelTemperature().getMaximum().getValue());
        holder.minTemp.setText(minTemp);
        holder.maxTemp.setText(maxTemp);
        holder.date.setText(response.body().getDailyForecasts().get(i).getDate().substring(0, 10));




    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView date, day, maxTemp, minTemp;
        ImageView ivIcon;


        public Holder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            day = itemView.findViewById(R.id.day);
            maxTemp = itemView.findViewById(R.id.maxTemp);
            minTemp = itemView.findViewById(R.id.minTemp);
        }
    }
}
