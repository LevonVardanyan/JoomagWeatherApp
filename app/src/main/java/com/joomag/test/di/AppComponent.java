package com.joomag.test.di;

import android.app.Application;

import com.joomag.test.WeatherViewModelFactory;
import com.joomag.test.repository.WeatherRepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {WeatherRepositoryModule.class})
public interface AppComponent {

    WeatherViewModelFactory getWeatherViewModelFactory();

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
