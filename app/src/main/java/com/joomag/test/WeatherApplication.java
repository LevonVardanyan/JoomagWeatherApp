package com.joomag.test;

import android.app.Application;

import com.joomag.test.di.AppComponent;

public class WeatherApplication extends Application {

    AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();


    }
}
