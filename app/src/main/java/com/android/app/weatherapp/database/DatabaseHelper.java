package com.android.app.weatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.app.weatherapp.database.CurrentWeatherFeed.CurrentFeedEntry;

/**
 * Created by nandi_000 on 01-04-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Weather.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + CurrentFeedEntry.TABLE_NAME + " (" +
            CurrentFeedEntry._ID + " INTEGER PRIMARY KEY," +
            CurrentFeedEntry.COLUMN_NAME_MAIN + TEXT_TYPE + COMMA_SEP +
            CurrentFeedEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            CurrentFeedEntry.COLUMN_NAME_TEMPERATURE + " INTEGER " + COMMA_SEP +
            CurrentFeedEntry.COLUMN_NAME_ICON + TEXT_TYPE + COMMA_SEP +
            CurrentFeedEntry.COLUMN_NAME_TIME + " INTEGER )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CurrentFeedEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
