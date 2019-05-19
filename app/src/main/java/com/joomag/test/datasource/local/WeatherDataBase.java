package com.joomag.test.datasource.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.joomag.test.model.remote.Weather;

@Database(entities = {Weather.class},
        version = 1, exportSchema = false)
public abstract class WeatherDataBase extends RoomDatabase {

    private static WeatherDataBase weatherDataBase;

    abstract WeatherDao getWeatherDao();

    public static WeatherDataBase getInstance(Context context) {
        if (weatherDataBase == null) {
            synchronized (WeatherDataBase.class) {
                if (weatherDataBase == null) {
                    weatherDataBase = Room.databaseBuilder(context,
                            WeatherDataBase.class, "weather_db").build();
                }
            }
        }
        return weatherDataBase;
    }
}
