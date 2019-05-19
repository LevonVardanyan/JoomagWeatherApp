package com.joomag.test.di;

import com.joomag.test.WeatherApplication;
import com.joomag.test.datasource.local.RoomDataSourceModule;
import com.joomag.test.datasource.local.WeatherDatabaseModule;
import com.joomag.test.datasource.remote.RetrofitDataSourceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, WeatherDatabaseModule.class, RetrofitDataSourceModule.class,
        RoomDataSourceModule.class})
public interface AppComponent {

    WeatherApplication provideApplication();
}
