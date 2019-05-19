package com.joomag.test.callback;

import android.util.Log;


import com.joomag.test.datasource.remote.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitSimpleCallback<T> implements Callback<T> {
    private RequestCallback<T> requestCallback;

    public RetrofitSimpleCallback(RequestCallback<T> requestCallback) {
        this.requestCallback = requestCallback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (requestCallback != null) {
            if (response.isSuccessful()) {
                requestCallback.onSuccess(response.body());
            } else {
                Log.e("Failed request", response.raw().request().url().toString());
                requestCallback.onFail(ErrorUtils.parseError(response).getError());
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (requestCallback != null) {
            requestCallback.onFail(t.getMessage());
        }
    }
}
