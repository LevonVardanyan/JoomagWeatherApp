package com.joomag.test.datasource.local;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Transaction;

import com.joomag.test.callback.RequestCallback;
import com.joomag.test.model.remote.Weather;
import com.joomag.test.util.SimpleExecutor;

import java.util.List;


public class RoomLocalDataSource implements LocalDataSource {
    private static RoomLocalDataSource roomLocalDataSource;
    private WeatherDao weatherDao;
    private SimpleExecutor simpleExecutor;

    public static RoomLocalDataSource getInstance(Context context) {
        if (roomLocalDataSource == null) {
            synchronized (RoomLocalDataSource.class) {
                if (roomLocalDataSource == null) {
                    roomLocalDataSource = new RoomLocalDataSource(context);
                }
            }
        }
        return roomLocalDataSource;
    }

    private RoomLocalDataSource(Context context) {
        weatherDao = WeatherDataBase.getInstance(context).getWeatherDao();
        simpleExecutor = SimpleExecutor.getInstance();
    }


    @Override
    public LiveData<List<Weather>> getSavedWeathers() {
        return weatherDao.getSavedWeathers();
    }

    @Override
    public List<Weather> getSavedWeathersSync() {
        return weatherDao.getSavedWeathersSync();
    }

    @Override
    public LiveData<Integer> getSavedWeathersCountLiveData() {
        return weatherDao.getSavedWeathersCountLiveData();
    }

    @Override
    public void insertWeather(Weather weather) {
        simpleExecutor.lunchOn(SimpleExecutor.LunchOn.DB, () -> {
            weatherDao.insert(weather);
        });
    }

    @Override
    public void getSavedWeathersCount(RequestCallback<Integer> requestCallback) {
        simpleExecutor.lunchOn(SimpleExecutor.LunchOn.DB, () -> {
            Integer count = weatherDao.getSavedWeathersCount();
            simpleExecutor.lunchOn(SimpleExecutor.LunchOn.UI, () -> requestCallback.onSuccess(count));
        });
    }

    @Override
    public int maxIndex() {
        return weatherDao.maxIndex();
    }

    @Override
    public void remove(Weather weather) {
        simpleExecutor.lunchOn(SimpleExecutor.LunchOn.DB, () -> weatherDao.remove(weather));
    }

    @Override
    public void swapItems(int fromId, int toId) {
        simpleExecutor.lunchOn(SimpleExecutor.LunchOn.DB, () -> swap(fromId, toId));
    }

    @Override
    public void removeItems(List<Integer> removingItemIds) {
        simpleExecutor.lunchOn(SimpleExecutor.LunchOn.DB, () -> {
            removeList(removingItemIds);
        });
    }


    @Transaction
    private void removeList(List<Integer> removingItemIds) {
        for (int id : removingItemIds) {
            weatherDao.removeById(id);
        }
    }

    @Transaction
    private void swap(int fromId, int toId) {
        int fromOrdering = weatherDao.getOrdering(fromId);
        int toOrdering = weatherDao.getOrdering(toId);
        weatherDao.setOrdering(fromId, toOrdering);
        weatherDao.setOrdering(toId, fromOrdering);
    }
}
