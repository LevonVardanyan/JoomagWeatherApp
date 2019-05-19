package com.joomag.test.datasource.local;


import androidx.lifecycle.LiveData;

import com.joomag.test.callback.RequestCallback;
import com.joomag.test.model.remote.Weather;

import java.util.List;

public interface LocalDataSource {

    LiveData<List<Weather>> getSavedWeathers();

    List<Weather> getSavedWeathersSync();

    LiveData<Integer> getSavedWeathersCountLiveData();

    void insertWeather(Weather weather);
    void getSavedWeathersCount(RequestCallback<Integer> requestCallback);

    int maxIndex();

    void remove(Weather weather);

    void swapItems(int fromId, int toId);

    void removeItems(List<Integer> removingItemIds);
}
