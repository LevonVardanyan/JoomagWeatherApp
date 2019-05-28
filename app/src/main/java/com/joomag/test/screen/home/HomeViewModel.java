package com.joomag.test.screen.home;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.joomag.test.R;
import com.joomag.test.callback.RequestCallback;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;
import com.joomag.test.repository.WeatherRepository;
import com.joomag.test.util.Utils;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private WeatherRepository weatherRepository;

    private MutableLiveData<List<SearchItem>> searchResultLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();
    private MutableLiveData<Boolean> itemAdded = new MutableLiveData<>();

    private String currentQuery = "";
    private String lastRequestedQuery = "";
    private Handler handler = new Handler();
    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (!currentQuery.equals(lastRequestedQuery)) {
                weatherRepository.search(currentQuery, new RequestCallback<List<SearchItem>>() {
                    @Override
                    public void onSuccess(List<SearchItem> response) {
                        searchResultLiveData.setValue(response);
                    }

                    @Override
                    public void onFail(String error) {

                    }
                });
                lastRequestedQuery = String.valueOf(currentQuery);
            }
        }
    };


    public HomeViewModel(@NonNull Application application, WeatherRepository weatherRepository) {
        super(application);
        this.weatherRepository = weatherRepository;
    }

    void search(String query) {
        handler.removeCallbacks(searchRunnable);
        currentQuery = query;
        handler.postDelayed(searchRunnable, 400);
    }

    LiveData<Integer> getSavedWeathersCountLiveData() {
        return weatherRepository.getSavedWeathersCountLiveData();
    }

    LiveData<List<SearchItem>> getSearchResultLiveData() {
        return searchResultLiveData;
    }

    LiveData<Boolean> getIsRefreshing() {
        return isRefreshing;
    }

    LiveData<List<Weather>> getSavedWeathers() {
        return weatherRepository.getSavedWeathers();
    }

    LiveData<Boolean> getItemAdded() {
        return itemAdded;
    }

    void requestWeatherAndSave(SearchItem searchItem) {
        if (!Utils.checkInternetConnection(getApplication().getApplicationContext())) {
            Toast.makeText(getApplication().getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        weatherRepository.requestWeatherBySearchItem(searchItem, new RequestCallback<Weather>() {
            @Override
            public void onSuccess(Weather response) {
                handler.postDelayed(() -> itemAdded.setValue(true), 500);
            }

            @Override
            public void onFail(String error) {
            }
        });
    }

    void refreshSavedWeathers() {
        weatherRepository.refreshSavedWeathers(new RequestCallback<List<Weather>>() {
            @Override
            public void onSuccess(List<Weather> response) {
                isRefreshing.setValue(false);
            }

            @Override
            public void onFail(String error) {
                isRefreshing.setValue(false);
            }
        });
    }

    void removeWeatherFromCache(Weather weather) {
        weatherRepository.removeSavedWeather(weather);
    }

    void swapItems(int fromId, int toId) {
        weatherRepository.swapItems(fromId, toId);
    }

    void removeSelectedItems(List<Integer> removingItemIds) {
        weatherRepository.removeItems(removingItemIds);

    }
}
