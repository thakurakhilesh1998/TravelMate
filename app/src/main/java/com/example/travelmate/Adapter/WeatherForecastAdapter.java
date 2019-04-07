package com.example.travelmate.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelmate.R;
import com.example.travelmate.weather.Weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


        if (i % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EA5A42"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#3A5276"));

        }

        String minTemp = String.valueOf(response.body().getDailyForecasts().get(i + 1).getRealFeelTemperature().getMinimum().getValue());
        String maxTemp = String.valueOf(response.body().getDailyForecasts().get(i + 1).getRealFeelTemperature().getMaximum().getValue());
        holder.tvTemp.setText(minTemp.substring(0, 2) + "°-" + maxTemp.substring(0, 2) + "°");
        String status = response.body().getDailyForecasts().get(i).getDay().getIconPhrase();
        holder.tvStatus.setText(status);

        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = inFormat.parse(response.body().getDailyForecasts().get(i).getDate().substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String goal = outFormat.format(date);
        holder.tvDayName.setText(goal);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivWeatherIcon;
        TextView tvDayName, tvStatus, tvTemp;


        public Holder(@NonNull View itemView) {
            super(itemView);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
            tvDayName = itemView.findViewById(R.id.tvDayName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTemp = itemView.findViewById(R.id.tvTemp);


        }
    }
}
