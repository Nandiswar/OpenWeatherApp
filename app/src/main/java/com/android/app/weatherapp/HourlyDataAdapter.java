package com.android.app.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.app.weatherapp.model.List;
import com.android.app.weatherapp.model.Main;
import com.android.app.weatherapp.model.Weather;

/**
 * Created by nandi_000 on 02-04-2016.
 * adapter for the recycler view used to display the hourly weather data
 */
public class HourlyDataAdapter extends RecyclerView.Adapter<HourlyDataAdapter.ViewHolder> {

    java.util.List<List> dataList;

    public HourlyDataAdapter(java.util.List<List> list) {
        dataList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mainWeatherView;
        public TextView descriptionView;
        public TextView temperatureView;
        public TextView timeView;
        public ImageView weatherIconView;

        public ViewHolder(View itemView) {
            super(itemView);
            mainWeatherView = (TextView) itemView.findViewById(R.id.row_main);
            descriptionView = (TextView) itemView.findViewById(R.id.row_desc);
            temperatureView = (TextView) itemView.findViewById(R.id.row_temp);
            timeView = (TextView) itemView.findViewById(R.id.row_time);
            weatherIconView = (ImageView) itemView.findViewById(R.id.row_weather_icon);
        }
    }

    @Override
    public HourlyDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_navigation_hourly_each_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HourlyDataAdapter.ViewHolder holder, int position) {
        List data = dataList.get(position);
        Main main = data.getMain();
        Weather weather = data.getWeather().get(0);

        holder.mainWeatherView.setText(weather.getMain());
        holder.descriptionView.setText(weather.getDescription());
        holder.temperatureView.setText(Utility.kelvinToFahrenheit(main.getTemp()) + " F");
        holder.timeView.setText(Utility.millisToDDHM(data.getDt(), true));
        setWeatherIcon(holder.weatherIconView, weather.getIcon());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void setWeatherIcon(ImageView weatherImg, String icon) {
        switch (icon) {
            case "01d":
                weatherImg.setImageResource(R.drawable.ic_clear);
                break;
            case "01n":
                weatherImg.setImageResource(R.drawable.ic_clear_night);
                break;
            case "02d":
            case "03d":
                weatherImg.setImageResource(R.drawable.ic_clouds_day);
                break;
            case "02n":
            case "03n":
                weatherImg.setImageResource(R.drawable.ic_clouds_night);
                break;
            case "04d":
            case "04n":
                weatherImg.setImageResource(R.drawable.ic_clouds_many);
                break;
            case "10d":
                weatherImg.setImageResource(R.drawable.ic_light_rain_day);
                break;
            case "10n":
                weatherImg.setImageResource(R.drawable.ic_light_rain_night);
                break;
            default:
                weatherImg.setImageResource(R.drawable.ic_default);
                break;
        }
    }
}
