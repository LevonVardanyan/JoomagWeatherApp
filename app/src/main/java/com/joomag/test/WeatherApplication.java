package com.joomag.test;

import android.app.Application;

import com.joomag.test.di.AppComponent;
import com.joomag.test.di.DaggerAppComponent;

public class WeatherApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().application(this).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
