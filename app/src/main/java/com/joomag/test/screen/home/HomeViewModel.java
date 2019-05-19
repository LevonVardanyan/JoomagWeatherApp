package com.joomag.test.screen.home;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.joomag.test.R;
import com.joomag.test.callback.RequestCallback;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;
import com.joomag.test.repository.WeatherRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private WeatherRepository weatherRepository;

    private MutableLiveData<List<SearchItem>> searchResultLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> searchQueryEmptyLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();
    private MutableLiveData<Boolean> itemAdded = new MutableLiveData<>();

    private ObservableBoolean isProgressShowing = new ObservableBoolean();
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private ObservableBoolean isShowMessageView = new ObservableBoolean();

    private String currentQuery = "";
    private String lastRequestedQuery = "";
    private Handler handler = new Handler();
    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (!currentQuery.equals(lastRequestedQuery)) {
                isProgressShowing.set(true);
                isShowMessageView.set(false);
                weatherRepository.search(currentQuery, new RequestCallback<List<SearchItem>>() {
                    @Override
                    public void onSuccess(List<SearchItem> response) {
                        isProgressShowing.set(false);
                        showMessage(response.isEmpty(), getApplication().getApplicationContext().getString(R.string.no_results));
                        searchResultLiveData.setValue(response);
                    }

                    @Override
                    public void onFail(String error) {
                        isProgressShowing.set(false);


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
        if (!TextUtils.isEmpty(query)) {
            handler.removeCallbacks(searchRunnable);
            currentQuery = query;
            handler.postDelayed(searchRunnable, 400);
        } else {
            checkForEmptySavedWeathersCount();
        }
        searchQueryEmptyLiveData.setValue(TextUtils.isEmpty(query));
    }

    void showMessage(boolean show, String message) {
        isShowMessageView.set(show);
        messageLiveData.setValue(message);
    }

    LiveData<Integer> getSavedWeathersCountLiveData() {
        return weatherRepository.getSavedWeathersCountLiveData();
    }

    private void checkForEmptySavedWeathersCount() {
        weatherRepository.getSavedWeathersCount(new RequestCallback<Integer>() {
            @Override
            public void onSuccess(Integer response) {
                showMessage(response == 0, getApplication().getApplicationContext().getString(R.string.no_saved_locations));
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    ObservableBoolean getIsProgressShowing() {
        return isProgressShowing;
    }

    MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    ObservableBoolean getIsShowMessageView() {
        return isShowMessageView;
    }

    MutableLiveData<List<SearchItem>> getSearchResultLiveData() {
        return searchResultLiveData;
    }

    MutableLiveData<Boolean> getSearchQueryEmptyLiveData() {
        return searchQueryEmptyLiveData;
    }

    MutableLiveData<Boolean> getIsRefreshing() {
        return isRefreshing;
    }

    LiveData<List<Weather>> getSavedWeathers() {
        return weatherRepository.getSavedWeathers();
    }

    MutableLiveData<Boolean> getItemAdded() {
        return itemAdded;
    }

    void requestWeatherAndSave(SearchItem searchItem) {
        isProgressShowing.set(true);
        weatherRepository.requestWeatherBySearchItem(searchItem, new RequestCallback<Weather>() {
            @Override
            public void onSuccess(Weather response) {
                isProgressShowing.set(false);
                handler.postDelayed(() -> itemAdded.setValue(true), 100);
            }

            @Override
            public void onFail(String error) {
                isProgressShowing.set(false);
            }
        });
    }

    void refreshSavedWeathers() {
        weatherRepository.refreshSavedWeathers(new RequestCallback() {
            @Override
            public void onSuccess(Object response) {
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

    public void swapItems(int fromId, int toId) {
        weatherRepository.swapItems(fromId, toId);
    }

    public void removeSelectedItems(List<Integer> removingItemIds) {
        weatherRepository.removeItems(removingItemIds);

    }
}
