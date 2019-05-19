package com.joomag.test.screen.weatherdetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.joomag.test.repository.WeatherRepository;

public class WeatherDetailsViewModel extends AndroidViewModel {
    private WeatherRepository weatherRepository;
    public WeatherDetailsViewModel(@NonNull Application application, WeatherRepository weatherRepository) {
        super(application);
        this.weatherRepository = weatherRepository;
    }
}
