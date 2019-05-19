package com.joomag.test.callback;

public interface RequestCallback<T> {
    void onSuccess(T response);

    void onFail(String error);
}
