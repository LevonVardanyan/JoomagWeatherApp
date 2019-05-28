package com.joomag.test.screen.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.joomag.test.callback.WeathersDiffCallback;
import com.joomag.test.databinding.WeatherCardBinding;
import com.joomag.test.di.FragmentScoped;
import com.joomag.test.model.remote.Weather;
import com.joomag.test.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@FragmentScoped
public class WeathersAdapter extends RecyclerView.Adapter<WeathersAdapter.WeatherViewHolder> {
    @Inject
    List<Weather> items;

    @Inject
    List<Integer> selectedItemIds;

    private boolean selectionMode;

    @Inject
    WeathersAdapter() {
    }

    void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
        if (!selectionMode) {
            selectedItemIds.clear();
            notifyDataSetChanged();
        }
    }

    boolean isSelectionMode() {
        return selectionMode;
    }

    void saveState(Bundle bundle) {
        bundle.putIntegerArrayList(HomeFragment.EXTRA_SELECTION_IDS, new ArrayList<>(selectedItemIds));
    }

    void setSelectedItemIds(List<Integer> selectedItemIds) {
        this.selectedItemIds = selectedItemIds;
    }

    void setItems(List<Weather> items) {
        WeathersDiffCallback weathersDiffCallback = new WeathersDiffCallback(this.items, items);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(weathersDiffCallback);

        this.items.clear();
        this.items.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WeatherCardBinding binding = WeatherCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(binding);
        binding.root.setOnClickListener(v -> {
            if (selectionMode) {
                int pos = weatherViewHolder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Weather weather = items.get(weatherViewHolder.getAdapterPosition());
                    if (selectedItemIds.contains(weather.getId())) {
                        selectedItemIds.remove(Integer.valueOf(weather.getId()));
                    } else {
                        selectedItemIds.add(weather.getId());
                    }
                    notifyItemChanged(weatherViewHolder.getAdapterPosition());
                }
            }
        });
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        int adapterPos = holder.getAdapterPosition();
        if (adapterPos != RecyclerView.NO_POSITION) {
            Weather weather = items.get(adapterPos);
            ViewCompat.setTransitionName(holder.binding.root, Constants.TRANSITION_NAME_PREFIX + adapterPos);
            Glide.with(holder.binding.getRoot().getContext()).load("https:" + weather.getCurrent()
                    .getCondition().getIcon()).into(holder.binding.icon);
            holder.binding.setWeather(weather);
            if (selectedItemIds != null) {
                holder.binding.setItemMaskVisible(selectedItemIds.contains(weather.getId()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    Weather getItem(int pos) {
        return items.get(pos);
    }

    List<Integer> getSelectedItemIdCopies() {
        return new ArrayList<>(selectedItemIds);
    }

    boolean isNothingSelected() {
        return selectedItemIds.isEmpty();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        WeatherCardBinding binding;

        WeatherViewHolder(WeatherCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setHapticFeedbackEnabled(true);

        }
    }

}
