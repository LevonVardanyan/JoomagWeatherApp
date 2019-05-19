package com.joomag.test.datasource.remote;


import com.joomag.test.callback.RequestCallback;
import com.joomag.test.callback.RetrofitSimpleCallback;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RetrofitRemoteDataSource implements RemoteDataSource {

    private ApiService apiService;

    @Inject
    RetrofitRemoteDataSource(ApiService apiService) {
        this.apiService = apiService;
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

}
