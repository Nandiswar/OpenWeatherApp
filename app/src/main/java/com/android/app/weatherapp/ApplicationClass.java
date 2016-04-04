package com.android.app.weatherapp;

import android.app.Application;
import android.content.Context;

import com.android.app.weatherapp.database.DatabaseHelper;

/**
 * Created by nandi_000 on 01-04-2016.
 */
public class ApplicationClass extends Application {

    private static DatabaseHelper databaseHelper;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    public static synchronized DatabaseHelper getDbInstance() { // single instance of database
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

}
