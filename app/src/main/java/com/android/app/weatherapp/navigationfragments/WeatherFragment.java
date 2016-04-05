package com.android.app.weatherapp.navigationfragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.weatherapp.ApiCall;
import com.android.app.weatherapp.Constants;
import com.android.app.weatherapp.HourlyDataAdapter;
import com.android.app.weatherapp.MainActivity;
import com.android.app.weatherapp.R;
import com.android.app.weatherapp.Utility;
import com.android.app.weatherapp.api.ApiInterface;
import com.android.app.weatherapp.database.ReadWriteData;
import com.android.app.weatherapp.model.HourlyModel;
import com.android.app.weatherapp.model.Main;
import com.android.app.weatherapp.model.NowModel;
import com.android.app.weatherapp.model.Sys;
import com.android.app.weatherapp.model.Weather;
import com.android.app.weatherapp.model.Wind;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    NowModel apiResponse;
    HourlyModel hourlyApiResponse;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    View view;
    SharedPreferences prefs;
    boolean hasNowApiHistoryData, hasHourlyApiHistoryData;
    static final long TIME_DIFF_CHECK = 10 * 60 * 1000; // 10 mins time check
    ApiCall apiCall;
    boolean isLandscapeMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // to handle refresh action
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weather, container, false);
        prefs = getActivity().getSharedPreferences(MainActivity.PREFSNAME, Context.MODE_PRIVATE);
        hasNowApiHistoryData = prefs.getBoolean(getString(R.string.now_api_response), false);
        hasHourlyApiHistoryData = prefs.getBoolean(getString(R.string.hourly_api_response), false);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            isLandscapeMode = true;
        }
        else{
            isLandscapeMode = false;
        }

        apiCall = new ApiCall(getActivity());

        if(hasNowApiHistoryData && hasHourlyApiHistoryData) {
            if (checkTime() && apiCall.canMakeApiCall()) { // time check and internet check and location check
                api.makeApiCall();
            } else { // show data from history
                NowModel response = readFromPref();
                updateView(view, response);

                HourlyModel model = readFromDb();
                updateView(view, model);
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // on resume
        if (!hasNowApiHistoryData || !hasHourlyApiHistoryData) { // if no history
            if (!apiCall.isOnline()) {   // internet check
                apiCall.showToast("Please enable data connection"); // show toast
                return;
            }

            if (!apiCall.isLocationEnabled()) {  // location check
                apiCall.showAlert();    // show alert
                return;
            }

            api.makeApiCall();
        }
    }

    /*final Handler handler = new Handler(); // call the api for every 10 min if active, cancel on pause
    Timer timer = new Timer();
    TimerTask doAsynchronousTask = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    try {
                        if(apiCall.canMakeApiCall()) {
                            api.makeApiCall();
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }
    };*/

    // Handle refresh action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!apiCall.isLocationEnabled()) {  // location check
                    apiCall.showAlert();    // show alert
                }

                if (!apiCall.isOnline()) {   // internet check
                    apiCall.showToast("Please enable data connection and try again"); // show toast
                }

                api.makeApiCall();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkTime() { // check if 10 mins of time has elapsed since last successful api call
        return (System.currentTimeMillis() - prefs.getLong(getString(R.string.last_api_call), 0))
                > TIME_DIFF_CHECK;
    }

    /*
     * read from shared preferences and create a NowModel object
     */
    private NowModel readFromPref() {
        NowModel model = new NowModel();
        Weather weather = new Weather();
        Main main = new Main();
        Sys sys = new Sys();
        Wind wind = new Wind();

        weather.setMain(prefs.getString(getString(R.string.current_main), ""));
        weather.setDescription(prefs.getString(getString(R.string.current_description), ""));
        weather.setIcon(prefs.getString(getString(R.string.current_icon), ""));
        main.setTemp(prefs.getFloat(getString(R.string.current_temp), 0));
        main.setHumidity(prefs.getFloat(getString(R.string.current_humidity), 0));
        main.setPressure(prefs.getFloat(getString(R.string.current_pressure), 0));
        main.setTempMax(prefs.getFloat(getString(R.string.current_temp_max), 0));
        main.setTempMin(prefs.getFloat(getString(R.string.current_temp_min), 0));
        java.util.List<Weather> weatherList = new ArrayList<Weather>();
        weatherList.add(weather);
        model.setMain(main);
        model.setWeather(weatherList);
        model.setName(prefs.getString(getString(R.string.current_city), ""));
        sys.setSunrise(prefs.getLong(getString(R.string.current_sun_rise), 0));
        sys.setSunset(prefs.getLong(getString(R.string.current_sun_set), 0));
        wind.setSpeed(prefs.getFloat(getString(R.string.current_wind_speed), 0));
        model.setWind(wind);
        model.setSys(sys);
        return model;
    }

    ApiCall.MakeApiCall api = new ApiCall.MakeApiCall() {
        @Override
        public void makeApiCall() {
            registerLocationListener();
        }
    };

    /*
     * update view with current weather data
     */
    private void updateView(View view, NowModel apiResponse) {
        Weather weather = apiResponse.getWeather().get(0);
        Main main = apiResponse.getMain();
        Sys sys = apiResponse.getSys();

        TextView textView = (TextView) view.findViewById(R.id.current_city);
        textView.setText(apiResponse.getName());

        TextView mainWeatherTextView = (TextView) view.findViewById(R.id.current_main);
        mainWeatherTextView.setText(weather.getMain() + "\n" + weather.getDescription());

        /*TextView descTextView = (TextView) view.findViewById(R.id.current_desc);
        descTextView.setText(weather.getDescription());*/

        ImageView weatherImg = (ImageView) view.findViewById(R.id.weather_img_view);
        setWeatherIcon(weatherImg, weather.getIcon());

        TextView tempTextView = (TextView) view.findViewById(R.id.current_temp);
        tempTextView.setText(Utility.kelvinToFahrenheit(main.getTemp()) + " F");

        TextView humidityTextView = (TextView) view.findViewById(R.id.humidity_text);
        humidityTextView.setText(main.getHumidity() + "%");

        TextView windSpeedTextView = (TextView) view.findViewById(R.id.wind_text);
        windSpeedTextView.setText(apiResponse.getWind().getSpeed() + " mps");

        TextView pressureTextView = (TextView) view.findViewById(R.id.pressure_text);
        pressureTextView.setText(main.getPressure() + " hPa");

        TextView tempMaxTextView = (TextView) view.findViewById(R.id.temp_max_text);
        tempMaxTextView.setText(Utility.kelvinToFahrenheit(main.getTempMax()) + " F ");

        TextView tempMinTextView = (TextView) view.findViewById(R.id.temp_min_text);
        tempMinTextView.setText(Utility.kelvinToFahrenheit(main.getTempMin()) + " F");

        TextView sunRiseTextView = (TextView) view.findViewById(R.id.sun_rise_text);
        sunRiseTextView.setText(Utility.millisToHM(sys.getSunrise()));

        TextView sunSetTextView = (TextView) view.findViewById(R.id.sun_set_text);
        sunSetTextView.setText(Utility.millisToHM(sys.getSunset()));
    }

    /*
     * load imageview with custom weather icon based on weather icon id
     */
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

    public void registerLocationListener() {
        // register location client
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);

        // changes related to marshmallow user permissions
        if(getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        fetchCurrentWeather(location);
        fetchHourlyWeather(location);

        if (mGoogleApiClient.isConnected()) { // disconnect location listening after api request
            mGoogleApiClient.disconnect();
        }
    }

    /*
     * make an api request to open weather hourly api using retrofit
     */
    private void fetchHourlyWeather(Location location) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WEATHER_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("lat", "" + location.getLatitude());
        queryParams.put("lon", "" + location.getLongitude());
        queryParams.put("appid", Constants.API_KEY);
        Call<HourlyModel> hourlyresponse = api.getHourlyWeather(queryParams);

        hourlyresponse.enqueue(new Callback<HourlyModel>() {
            @Override
            public void onResponse(Call<HourlyModel> call, Response<HourlyModel> response) {
                if (getActivity() != null) {
                    if (response.isSuccessful() && response != null) {

                        if (!hasHourlyApiHistoryData) {
                            prefs.edit().putBoolean(getString(R.string.hourly_api_response), true).commit();
                        }

                        hourlyApiResponse = response.body();
                        addToDb(hourlyApiResponse);
                        updateView(view, hourlyApiResponse);
                    } else {
                        Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<HourlyModel> call, Throwable t) {
                System.out.print("Failure");
            }
        });
    }

    private void addToDb(HourlyModel apiResponse) { // add to database
        ReadWriteData.addHourlyWeatherData(apiResponse);
    }

    private HourlyModel readFromDb() { // read from database
        HourlyModel model = new HourlyModel();
        model.setList(ReadWriteData.readHourlyWeatherData());
        return model;
    }

    /*
     * update view with hourly weather data
     */
    private void updateView(View view, HourlyModel apiResponse) {
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        if(isLandscapeMode) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
        }

        HourlyDataAdapter adapter = new HourlyDataAdapter(apiResponse.getList());
        mRecyclerView.setAdapter(adapter);
    }

    /*
     * make an api request to open weather current weather api using retrofit
     */
    private void fetchCurrentWeather(Location location) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WEATHER_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("lat", "" + location.getLatitude());
        queryParams.put("lon", "" + location.getLongitude());
        queryParams.put("appid", Constants.API_KEY);
        Call<NowModel> response = api.getCurrentWeather(queryParams);

        response.enqueue(new Callback<NowModel>() {
            @Override
            public void onResponse(Call<NowModel> call, Response<NowModel> response) {
                if(getActivity() != null) {
                    if (response.isSuccessful() && response != null) {
                        ((TextView) view.findViewById(R.id.last_update_text)).setText
                                ("Last Update : " + Utility.millisToDDHM(System.currentTimeMillis(), false));
                        prefs.edit().putLong(getString(R.string.last_api_call), System.currentTimeMillis()).commit();
                        if (!hasNowApiHistoryData) {
                            prefs.edit().putBoolean(getString(R.string.now_api_response), true).commit();
                        }
                        apiResponse = response.body();
                        addToSharedPref(apiResponse);
                        updateView(view, apiResponse);
                    }
                }

            }

            @Override
            public void onFailure(Call<NowModel> call, Throwable t) {
                System.out.print("Failure");
            }
        });
    }

    private void addToSharedPref(NowModel nowModel) {
        SharedPreferences.Editor editor = prefs.edit();
        Weather weather = nowModel.getWeather().get(0);
        Main main = nowModel.getMain();
        editor.putString(getString(R.string.current_city), nowModel.getName());
        editor.putString(getString(R.string.current_main), weather.getMain());
        editor.putString(getString(R.string.current_description), weather.getDescription());
        editor.putString(getString(R.string.current_icon), weather.getIcon());
        editor.putFloat(getString(R.string.current_temp), main.getTemp());
        editor.putFloat(getString(R.string.current_humidity), main.getHumidity());
        editor.putFloat(getString(R.string.current_wind_speed), nowModel.getWind().getSpeed());
        editor.putFloat(getString(R.string.current_pressure), main.getPressure());
        editor.putFloat(getString(R.string.current_temp_max), main.getTempMax());
        editor.putFloat(getString(R.string.current_temp_min), main.getTempMin());
        editor.putLong(getString(R.string.current_sun_rise), nowModel.getSys().getSunrise());
        editor.putLong(getString(R.string.current_sun_set), nowModel.getSys().getSunset());
        editor.commit();
    }
}
