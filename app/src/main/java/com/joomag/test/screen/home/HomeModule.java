package com.joomag.test.screen.home;

import com.joomag.test.di.FragmentScoped;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
abstract class HomeModule {

    @Provides
    @FragmentScoped
    static List<SearchItem> provideSearchItemList() {
        return new ArrayList<>();
    }

    @Provides
    @FragmentScoped
    static List<Weather> provideWeatherList() {
        return new ArrayList<>();
    }

    @Provides
    @FragmentScoped
    static List<Integer> providesIntegerList() {
        return new ArrayList<>();
    }


}
