package com.joomag.test.model.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorsList {
    @SerializedName("errors")
    private List<Error> errors;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public String getError() {
        StringBuilder stringBuilder = new StringBuilder();
        if (errors != null) {
            for (Error error : errors) {
                stringBuilder.append(error.getErrorDetail());
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
