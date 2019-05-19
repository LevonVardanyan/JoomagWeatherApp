package com.joomag.test.datasource.remote;


import com.joomag.test.callback.RequestCallback;
import com.joomag.test.callback.RetrofitSimpleCallback;
import com.joomag.test.model.remote.Forecast;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;

import java.io.IOException;
import java.util.List;

public class RetrofitRemoteDataSource implements RemoteDataSource {

    private static RetrofitRemoteDataSource retrofitRemoteDataSource;
    private ApiService apiService;

    public static RetrofitRemoteDataSource getInstance() {
        if (retrofitRemoteDataSource == null) {
            synchronized (RetrofitRemoteDataSource.class) {
                if (retrofitRemoteDataSource == null) {
                    retrofitRemoteDataSource = new RetrofitRemoteDataSource();
                }
            }
        }
        return retrofitRemoteDataSource;
    }

    private RetrofitRemoteDataSource() {
        apiService = RetrofitRequestBuilder.getInstance().getApiService();
    }

    @Override
    public void search(String query, RequestCallback<List<SearchItem>> requestCallback) {
        apiService.search(RequestConstants.API_KEY, query).enqueue(new RetrofitSimpleCallback<>(requestCallback));
    }

    @Override
    public void requestWeather(String query, RequestCallback<Weather> requestCallback) {
        apiService.getCurrentWeather(RequestConstants.API_KEY, query).enqueue(new RetrofitSimpleCallback<>(requestCallback));
    }

    @Override
    public Weather requestWeather(String query) {
        try {
            return apiService.getCurrentWeather(RequestConstants.API_KEY, query).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void requestForecast(String query, int daysCount, RequestCallback<Forecast> requestCallback) {
        apiService.getForecast(RequestConstants.API_KEY, query, RequestConstants.FORECAST_DAYS_COUNT)
                .enqueue(new RetrofitSimpleCallback<>(requestCallback));
    }
}
