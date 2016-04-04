package com.android.app.weatherapp.api;

import com.android.app.weatherapp.model.HourlyModel;
import com.android.app.weatherapp.model.NowModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by nandi_000 on 01-04-2016.
 * api call : /weather?lat=35&lon=139&appid=APP_KEY
 * api call : /forecast?lat=35&lon=139&appid=APP_KEY
 */
public interface ApiInterface {

    // current weather api
    @GET("weather")
    Call<NowModel> getCurrentWeather(@QueryMap Map<String, String> queryParams);

    // hourly weather api
    @GET("forecast")
    Call<HourlyModel> getHourlyWeather(@QueryMap Map<String, String> queryParams);
}
