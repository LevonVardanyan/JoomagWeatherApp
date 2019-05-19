
package com.joomag.test.model.remote;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Location {


    private String country;

    private Double lat;

    private String localtime;
    @SerializedName("localtime_epoch")
    private Long localtimeEpoch;

    private Double lon;

    private String name;

    private String region;
    @SerializedName("tz_id")
    private String tzId;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public Long getLocaltimeEpoch() {
        return localtimeEpoch;
    }

    public void setLocaltimeEpoch(Long localtimeEpoch) {
        this.localtimeEpoch = localtimeEpoch;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTzId() {
        return tzId;
    }

    public void setTzId(String tzId) {
        this.tzId = tzId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof Location) {
            Location location = (Location) obj;
            return location.getCountry().equals(country) && location.getTzId().equals(tzId) &&
                    location.getLat().equals(lat) && location.getLon().equals(lon) &&
                    location.getRegion().equals(region) && location.getName().equals(name);
        }
        return super.equals(obj);
    }
}
