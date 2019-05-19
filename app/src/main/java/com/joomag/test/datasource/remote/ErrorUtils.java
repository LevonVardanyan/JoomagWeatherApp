package com.joomag.test.datasource.remote;


import com.joomag.test.model.remote.ErrorsList;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static ErrorsList parseError(Response<?> response) {
        Converter<ResponseBody, ErrorsList> converter =
                RetrofitRequestBuilder.getInstance().getRetrofit()
                        .responseBodyConverter(ErrorsList.class, new Annotation[0]);

        ErrorsList error = null;

        try {
            if (response != null) {
                error = converter.convert(response.errorBody());
            }
        } catch (IOException e) {
            return new ErrorsList();
        }

        return error;
    }
}