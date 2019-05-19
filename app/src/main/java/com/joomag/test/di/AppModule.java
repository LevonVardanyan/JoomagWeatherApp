package com.joomag.test.di;

import com.joomag.test.WeatherApplication;

import dagger.Provides;

public class AppModule {

    WeatherApplication application;

    public AppModule(WeatherApplication application) {
        this.application = application;
    }

    @Provides
    WeatherApplication getApplication() {
        return application;
    }


}
