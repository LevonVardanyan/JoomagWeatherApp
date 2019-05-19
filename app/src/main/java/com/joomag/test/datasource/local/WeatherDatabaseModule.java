package com.joomag.test.datasource.local;

import android.app.Application;

import androidx.room.Room;

import dagger.Module;

@Module
public class WeatherDatabaseModule {

    private WeatherDataBase weatherDataBase;

    public WeatherDatabaseModule(Application application) {
        this.weatherDataBase = Room.databaseBuilder(application.getApplicationContext(),
                WeatherDataBase.class, "weather_db").build();
    }
}
