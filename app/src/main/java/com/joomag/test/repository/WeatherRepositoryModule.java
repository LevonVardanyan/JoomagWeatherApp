package com.joomag.test.repository;

import android.app.Application;

import androidx.room.Room;

import com.joomag.test.datasource.local.LocalDataSource;
import com.joomag.test.datasource.local.RoomLocalDataSource;
import com.joomag.test.datasource.local.WeatherDao;
import com.joomag.test.datasource.local.WeatherDataBase;
import com.joomag.test.datasource.remote.ApiService;
import com.joomag.test.datasource.remote.RemoteDataSource;
import com.joomag.test.datasource.remote.RequestConstants;
import com.joomag.test.datasource.remote.RetrofitRemoteDataSource;
import com.joomag.test.util.SimpleExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class WeatherRepositoryModule {

    @Provides
    @Singleton
    static WeatherRepository provideWeatherRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource,
                                                      SimpleExecutor simpleExecutor) {
        return new WeatherRepository(remoteDataSource, localDataSource, simpleExecutor);
    }

    @Provides
    @Singleton
    static WeatherDataBase provideWeatherDataBase(Application application) {
        return Room.databaseBuilder(application.getApplicationContext(),
                WeatherDataBase.class, "weather_db").build();
    }

    @Provides
    @Singleton
    static LocalDataSource provideRoomDataSource(RoomLocalDataSource roomLocalDataSource) {
        return roomLocalDataSource;
    }

    @Provides
    static WeatherDao provideWeatherDao(WeatherDataBase weatherDataBase) {
        return weatherDataBase.getWeatherDao();
    }

    @Provides
    @Singleton
    static RemoteDataSource provideRetrofitDataSource(RetrofitRemoteDataSource retrofitRemoteDataSource) {
        return retrofitRemoteDataSource;
    }

    @Provides
    @Singleton
    static ApiService provideApiService() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestConstants.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(ApiService.class);
    }
}
