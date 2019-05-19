package com.joomag.test;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.joomag.test.repository.WeatherRepository;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WeatherViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final WeatherRepository weatherRepository;
    private Application application;


    @Inject
    WeatherViewModelFactory(Application application,
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
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return super.create(modelClass);
    }
}
