
package com.joomag.test.model.remote;

import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_weathers_table")
public class Weather {

    @PrimaryKey
    private int id;
    @Embedded
    private Current current;
    @Embedded
    private Location location;

    private int ordering;
    private String uniqueQuery;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueQuery() {
        return uniqueQuery;
    }

    public void setUniqueQuery(String uniqueQuery) {
        this.uniqueQuery = uniqueQuery;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof Weather) {
            Weather weather = (Weather) obj;
            return weather.getLocation().equals(location) && weather.getCurrent().equals(current);
        }
        return super.equals(obj);
    }
}
