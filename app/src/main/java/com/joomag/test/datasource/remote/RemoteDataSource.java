package com.joomag.test.datasource.remote;


import com.joomag.test.callback.RequestCallback;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;

import java.util.List;

public interface RemoteDataSource {

    void search(String query, RequestCallback<List<SearchItem>> requestCallback);

    void requestWeather(String query, RequestCallback<Weather> requestCallback);

    Weather requestWeather(String query);

}
