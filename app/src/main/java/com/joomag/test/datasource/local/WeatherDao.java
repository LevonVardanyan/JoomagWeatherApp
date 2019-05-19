package com.joomag.test.datasource.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.joomag.test.model.remote.Weather;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM saved_weathers_table ORDER BY ordering DESC")
    LiveData<List<Weather>> getSavedWeathers();

    @Query("SELECT * FROM saved_weathers_table ORDER BY ordering DESC")
    List<Weather> getSavedWeathersSync();

    @Query("SELECT COUNT(*) FROM saved_weathers_table")
    LiveData<Integer> getSavedWeathersCountLiveData();

    @Query("SELECT COUNT(*) FROM saved_weathers_table")
    Integer getSavedWeathersCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weather weather);


    @Query("SELECT MAX(ordering) FROM saved_weathers_table")
    int maxIndex();

    @Delete
    void remove(Weather weather);

    @Query("SELECT ordering FROM saved_weathers_table WHERE id = :weatherId")
    int getOrdering(int weatherId);

    @Query("UPDATE saved_weathers_table SET ordering = :ordering WHERE id = :weatherId")
    void setOrdering(int weatherId, int ordering);

    @Query("DELETE  FROM saved_weathers_table WHERE id =:id")
    void removeById(int id);
}
