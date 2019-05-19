package com.joomag.test.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.joomag.test.datasource.local.RoomLocalDataSource;
import com.joomag.test.datasource.remote.RetrofitRemoteDataSource;

public class RepositoryProvider {
    public static WeatherRepository provideWeatherRepository(@NonNull Context context) {
        return WeatherRepository.getInstance(RetrofitRemoteDataSource.getInstance(), RoomLocalDataSource.getInstance(context));
    }


}
