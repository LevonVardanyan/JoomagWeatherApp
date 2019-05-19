package com.joomag.test.model.remote;

import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("status")
    private int status;
    @SerializedName("code")
    private int code;
    @SerializedName("detail")
    private String errorDetail;
    @SerializedName("source")
    private ErrorSourceParameter source;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public ErrorSourceParameter getSource() {
        return source;
    }

    public void setSource(ErrorSourceParameter source) {
        this.source = source;
    }

    public class ErrorSourceParameter {
        @SerializedName("parameter")
        private String parameter;

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }
    }

}
