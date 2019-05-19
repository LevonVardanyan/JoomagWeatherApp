package com.joomag.test;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.joomag.test.repository.RepositoryProvider;
import com.joomag.test.repository.WeatherRepository;

import java.lang.reflect.InvocationTargetException;

public class WeatherViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile WeatherViewModelFactory weatherViewModelFactory;

    private final WeatherRepository weatherRepository;
    private Application application;

    public static WeatherViewModelFactory getInstance(Application application) {

        if (weatherViewModelFactory == null) {
            synchronized (WeatherViewModelFactory.class) {
                if (weatherViewModelFactory == null) {
                    weatherViewModelFactory = new WeatherViewModelFactory(
                            application,
                            RepositoryProvider.provideWeatherRepository(application.getApplicationContext()));
                }
            }
        }
        return weatherViewModelFactory;
    }

    private WeatherViewModelFactory(Application application,
                                    WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(Application.class, WeatherRepository.class)
                        .newInstance(application, weatherRepository);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return super.create(modelClass);
    }
}
