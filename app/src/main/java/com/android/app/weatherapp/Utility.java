package com.android.app.weatherapp;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nandi_000 on 01-04-2016.
 * Provides utility methods to (i) convert kelvin to Fahrenheit
 * (ii) convert utc time to HH:mm format and (iii) convert utc time to MM/dd HH:mm
 **/
public final class Utility {

    // Method to convert from degrees Kelvin to degrees Fahrenheit
    public static String kelvinToFahrenheit(float degKelvin) {
        float degCelcius;
        degCelcius = degKelvin - 273.15f;
        return String.format("%.1f", (degCelcius * 9.0f / 5.0f + 32));
    }

    public static String millisToHM(long seconds) {
        Date date = new Date(seconds * 1000);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getDefault());
        System.out.println(date);
        System.out.print(TimeZone.getDefault());
        return formatter.format(date);
    }

    public static String millisToDDHM(long seconds, boolean isUTC) {
        Date date;
        if(isUTC) {
            date = new Date(seconds * 1000);
        } else {
            date = new Date(seconds);
        }

        DateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date);
    }

}
