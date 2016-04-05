package com.android.app.weatherapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.app.weatherapp.ApplicationClass;
import com.android.app.weatherapp.model.HourlyModel;
import com.android.app.weatherapp.model.List;
import com.android.app.weatherapp.model.Main;
import com.android.app.weatherapp.model.NowModel;
import com.android.app.weatherapp.database.CurrentWeatherFeed.CurrentFeedEntry;
import com.android.app.weatherapp.model.Weather;

import java.util.ArrayList;


/**
 * Created by nandi_000 on 01-04-2016.
 */
public class ReadWriteData {

    /**
     * database helper method to add hourly weather data
     * @param model - HourlyWeather object
     */
    public static void addHourlyWeatherData(HourlyModel model) {
        SQLiteDatabase db = ApplicationClass.getDbInstance().getWritableDatabase();

        // delete old data
        db.delete(CurrentFeedEntry.TABLE_NAME, null, null);

        java.util.List<List> list = model.getList();
        for (List item : list) {
            ContentValues contentValues = new ContentValues();
            Weather weather = item.getWeather().get(0);
            contentValues.put(CurrentFeedEntry.COLUMN_NAME_MAIN, weather.getMain());
            contentValues.put(CurrentFeedEntry.COLUMN_NAME_DESCRIPTION, weather.getDescription());
            contentValues.put(CurrentFeedEntry.COLUMN_NAME_TEMPERATURE, item.getMain().getTemp());
            contentValues.put(CurrentFeedEntry.COLUMN_NAME_ICON, weather.getIcon());
            contentValues.put(CurrentFeedEntry.COLUMN_NAME_TIME, item.getDt());
            long newRowId = db.insert(CurrentFeedEntry.TABLE_NAME, null, contentValues);
        }

    }

    /**
     * database helper method to read hourly weather data
     * @return list of hourly weather data
     */
    public static java.util.List<List> readHourlyWeatherData() {
        SQLiteDatabase db = ApplicationClass.getDbInstance().getReadableDatabase();

        java.util.List<List> list = new ArrayList<List>();
        String[] projection = {
                CurrentFeedEntry.COLUMN_NAME_MAIN, CurrentFeedEntry.COLUMN_NAME_DESCRIPTION,
                CurrentFeedEntry.COLUMN_NAME_TEMPERATURE, CurrentFeedEntry.COLUMN_NAME_ICON,
                CurrentFeedEntry.COLUMN_NAME_TIME
        };

        //String sortOrder = CurrentFeedEntry.COLUMN_NAME_TIME + " DESC";
        String whereClause = CurrentFeedEntry.COLUMN_NAME_TIME + ">?";
        String whereArgs[] = {String.valueOf(System.currentTimeMillis()/1000)};

        Cursor cursor = db.query(CurrentFeedEntry.TABLE_NAME, projection, whereClause, whereArgs, null, null, null);

        if(cursor != null) {
            //cursor.moveToFirst();
            while(cursor.moveToNext()) {
                List item = new List();
                item.setDt(cursor.getLong(cursor.getColumnIndex(CurrentFeedEntry.COLUMN_NAME_TIME)));
                java.util.List<Weather> weatherList = new ArrayList<Weather>();
                Weather weather = new Weather();
                weather.setMain(cursor.getString(cursor.getColumnIndex(CurrentFeedEntry.COLUMN_NAME_MAIN)));
                weather.setDescription(cursor.getString(cursor.getColumnIndex(CurrentFeedEntry.COLUMN_NAME_DESCRIPTION)));
                weather.setIcon(cursor.getString(cursor.getColumnIndex(CurrentFeedEntry.COLUMN_NAME_ICON)));
                weatherList.add(weather);
                item.setWeather(weatherList);
                Main main = new Main();
                main.setTemp(cursor.getLong(cursor.getColumnIndex(CurrentFeedEntry.COLUMN_NAME_TEMPERATURE)));
                item.setMain(main);
                list.add(item);
            }
        }

        return list;
    }
}
