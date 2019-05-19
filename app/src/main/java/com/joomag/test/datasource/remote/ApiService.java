package com.joomag.test.datasource.remote;


import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * request api service
 */
public interface ApiService {


    @GET(RequestConstants.CURRENT)
    Call<Weather> getCurrentWeather(@Query("key") String apiKey, @Query("q") String query);

    @GET(RequestConstants.SEARCH)
    Call<List<SearchItem>> search(@Query("key") String apiKey, @Query("q") String query);

}
