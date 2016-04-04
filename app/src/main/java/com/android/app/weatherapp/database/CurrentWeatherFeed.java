package com.android.app.weatherapp.database;

import android.provider.BaseColumns;

/**
 * Created by nandi_000 on 01-04-2016.
 */
public final class CurrentWeatherFeed {

    public CurrentWeatherFeed() {

    }

    public static abstract class CurrentFeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "currentWeather";
        public static final String COLUMN_NAME_MAIN = "main";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TEMPERATURE = "temperature";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_ICON = "icon";
    }
}
