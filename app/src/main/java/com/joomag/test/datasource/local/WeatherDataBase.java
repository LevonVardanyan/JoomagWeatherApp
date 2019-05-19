package com.joomag.test.datasource.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.joomag.test.model.remote.Weather;

import javax.inject.Singleton;

@Singleton
@Database(entities = {Weather.class},
        version = 1, exportSchema = false)
public abstract class WeatherDataBase extends RoomDatabase {

    public abstract WeatherDao getWeatherDao();

}
