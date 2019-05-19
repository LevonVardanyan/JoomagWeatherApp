package com.joomag.test.datasource.remote;

import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitRequestBuilder {


    private static RetrofitRequestBuilder instance;
    private ApiService apiService;
    private Retrofit retrofit;

    static RetrofitRequestBuilder getInstance() {
        if (instance == null) {
            instance = new RetrofitRequestBuilder();
        }
        return instance;
    }

    private RetrofitRequestBuilder() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder().baseUrl(RequestConstants.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();

        apiService = retrofit.create(ApiService.class);
    }


    @Nullable
    ApiService getApiService() {
        return apiService;
    }

    Retrofit getRetrofit() {
        return retrofit;
    }

}
