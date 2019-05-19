
package com.joomag.test.model.remote;

import com.google.gson.annotations.SerializedName;

public class ForecastDay {
    private Astro astro;

    private String date;
    @SerializedName("date_epoch")
    private Long dateEpoch;

    private Day day;

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getDateEpoch() {
        return dateEpoch;
    }

    public void setDateEpoch(Long dateEpoch) {
        this.dateEpoch = dateEpoch;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

}
