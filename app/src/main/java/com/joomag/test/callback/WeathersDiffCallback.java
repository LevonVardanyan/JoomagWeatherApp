package com.joomag.test.callback;

import androidx.recyclerview.widget.DiffUtil;

import com.joomag.test.model.remote.Weather;

import java.util.List;

public class WeathersDiffCallback extends DiffUtil.Callback {

    private List<Weather> oldList;
    private List<Weather> newList;

    public WeathersDiffCallback(List<Weather> oldList, List<Weather> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

}
