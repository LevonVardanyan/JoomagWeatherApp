package com.joomag.test.repository;

import androidx.lifecycle.LiveData;

import com.joomag.test.callback.RequestCallback;
import com.joomag.test.datasource.local.LocalDataSource;
import com.joomag.test.datasource.remote.RemoteDataSource;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;
import com.joomag.test.util.SimpleExecutor;

import java.util.List;

public class WeatherRepository {

    private static WeatherRepository weatherRepository;
    private RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;

    private SimpleExecutor simpleExecutor;

    static WeatherRepository getInstance(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        if (weatherRepository == null) {
            synchronized (WeatherRepository.class) {
                if (weatherRepository == null) {
                    weatherRepository = new WeatherRepository(remoteDataSource, localDataSource);
                }
            }
        }
        return weatherRepository;
    }

    private WeatherRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.simpleExecutor = SimpleExecutor.getInstance();
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
        remoteDataSource.requestWeather(searchItem.getName(), new RequestCallback<Weather>() {
            @Override
            public void onSuccess(Weather weather) {
                simpleExecutor.lunchOn(SimpleExecutor.LunchOn.DB, () -> {
                    weather.setId(weather.hashCode());
                    List<Weather> savedWeathers = localDataSource.getSavedWeathersSync();
                    boolean contains = weatherSaved(savedWeathers, weather);
                    if (!contains) {
                        int maxIndex = localDataSource.maxIndex() + 1;
                        weather.setOrdering(maxIndex);
                    }
                    localDataSource.insertWeather(weather);
                    simpleExecutor.lunchOn(SimpleExecutor.LunchOn.UI, () -> requestCallback.onSuccess(weather));
                });

            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private boolean weatherSaved(List<Weather> savedWeathers, Weather item) {
        for (Weather savedWeather : savedWeathers) {
            if (savedWeather.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }

    public void refreshSavedWeathers(RequestCallback requestCallback) {
        simpleExecutor.lunchOn(SimpleExecutor.LunchOn.NETWORK, () -> {
            List<Weather> weathers = localDataSource.getSavedWeathersSync();
            for (Weather weather : weathers) {
                Weather newWeather = remoteDataSource.requestWeather(weather.getLocation().getName());
                newWeather.setId(weather.getId());
                newWeather.setOrdering(weather.getOrdering());
                localDataSource.insertWeather(newWeather);
            }
            simpleExecutor.lunchOn(SimpleExecutor.LunchOn.UI, () -> {
                requestCallback.onSuccess(null);
            });
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
