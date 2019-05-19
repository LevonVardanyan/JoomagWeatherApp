package com.joomag.test.screen.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.joomag.test.R;
import com.joomag.test.callback.WeathersDiffCallback;
import com.joomag.test.databinding.WeatherCardBinding;
import com.joomag.test.model.remote.Weather;
import com.joomag.test.util.Constants;
import com.joomag.test.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class WeathersAdapter extends RecyclerView.Adapter<WeathersAdapter.WeatherViewHolder> {
    private List<Weather> items;

    private List<Integer> selectedItemIds;

    private boolean selectionMode;

    private OnItemClickListener onItemClickListener;

    WeathersAdapter() {
        items = new ArrayList<>();
        selectedItemIds = new ArrayList<>();
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
        if (!selectionMode) {
            selectedItemIds.clear();
        }
        notifyDataSetChanged();
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
            if (!selectionMode) {
                if (onItemClickListener != null) {
                    onItemClickListener.onWeatherItemClick(items.get(weatherViewHolder.getAdapterPosition()), v);
                }
            } else {
                Weather weather = items.get(weatherViewHolder.getAdapterPosition());
                if (selectedItemIds.contains(weather.getId())) {
                    selectedItemIds.remove(weather.getId());
                } else {
                    selectedItemIds.add(weather.getId());
                }
                notifyItemChanged(weatherViewHolder.getAdapterPosition());
            }
        });
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        int adapterPos = holder.getAdapterPosition();
        Weather weather = items.get(adapterPos);
        Context context = holder.binding.getRoot().getContext();
        ViewCompat.setTransitionName(holder.binding.root, Constants.TRANSITION_NAME_PREFIX + adapterPos);
        Glide.with(context).load("https:" + weather.getCurrent().getCondition().getIcon()).into(holder.binding.icon);
        holder.binding.name.setText(weather.getLocation().getName());
        holder.binding.updateDate.setText(context.getString(R.string.updated) + " " + Utils.getHourPreviewWithNames(weather.getCurrent().getLastUpdated()));
        holder.binding.description.setText(weather.getCurrent().getCondition().getText());
        holder.binding.currentTemp.setText(context.getString(R.string.current_temp_preview, weather.getCurrent().getTempC().intValue()));
        holder.binding.windInfo.setText(context.getString(R.string.wind, weather.getCurrent().getWindMph(), weather.getCurrent().getWindDir()));

        holder.binding.itemSelectedMask.setVisibility(selectedItemIds.contains(weather.getId()) ? View.VISIBLE : View.GONE);
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

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        WeatherCardBinding binding;

        WeatherViewHolder(WeatherCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setHapticFeedbackEnabled(true);

        }
    }

    interface OnItemClickListener {
        void onWeatherItemClick(Weather weather, View sharedViews);
    }
}
