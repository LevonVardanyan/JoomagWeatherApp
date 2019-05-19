package com.joomag.test.repository;

import androidx.lifecycle.LiveData;

import com.joomag.test.callback.RequestCallback;
import com.joomag.test.datasource.local.LocalDataSource;
import com.joomag.test.datasource.remote.RemoteDataSource;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;
import com.joomag.test.util.SimpleExecutor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WeatherRepository {

    private RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;

    private SimpleExecutor simpleExecutor;

    @Inject
    WeatherRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource, SimpleExecutor simpleExecutor) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.simpleExecutor = simpleExecutor;
    }

    public void search(String query, RequestCallback<List<SearchItem>> callbackWithResponse) {
        remoteDataSource.search(query, callbackWithResponse);

    }

    public LiveData<List<Weather>> getSavedWeathers() {
        return localDataSource.getSavedWeathers();
    }

    public LiveData<Integer> getSavedWeathersCountLiveData() {
        return localDataSource.getSavedWeathersCountLiveData();
    }

    public void getSavedWeathersCount(RequestCallback<Integer> requestCallback) {
        localDataSource.getSavedWeathersCount(requestCallback);

    }

    public void requestWeatherBySearchItem(SearchItem searchItem, RequestCallback<Weather> requestCallback) {
        remoteDataSource.requestWeather(searchItem.getUrl(), new RequestCallback<Weather>() {
            @Override
            public void onSuccess(Weather weather) {
                simpleExecutor.lunchOn(SimpleExecutor.LunchOn.DB, () -> {
                    weather.setId(searchItem.getId().intValue());
                    int maxIndex = localDataSource.maxIndex() + 1;
                    weather.setOrdering(maxIndex);
                    localDataSource.insertWeather(weather);
                    simpleExecutor.lunchOn(SimpleExecutor.LunchOn.UI, () -> requestCallback.onSuccess(weather));
                });
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    public void refreshSavedWeathers(RequestCallback<List<Weather>> requestCallback) {
        simpleExecutor.lunchOn(SimpleExecutor.LunchOn.NETWORK, () -> {
            List<Weather> weathers = localDataSource.getSavedWeathersSync();
            for (Weather weather : weathers) {
                Weather newWeather = remoteDataSource.requestWeather(weather.getLocation().getName());
                if (newWeather != null) {
                    newWeather.setId(weather.getId());
                    newWeather.setOrdering(weather.getOrdering());
                    localDataSource.insertWeather(newWeather);
                }
            }
            simpleExecutor.lunchOn(SimpleExecutor.LunchOn.UI, () -> requestCallback.onSuccess(weathers));
        });
    }

    public void removeSavedWeather(Weather weather) {
        localDataSource.remove(weather);
    }

    public void swapItems(int fromId, int toId) {
        localDataSource.swapItems(fromId, toId);
    }

    public void removeItems(List<Integer> removingItemIds) {
        localDataSource.removeItems(removingItemIds);
    }
}
