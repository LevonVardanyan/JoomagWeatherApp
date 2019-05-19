package com.joomag.test.screen.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joomag.test.databinding.SearchItemBinding;
import com.joomag.test.di.FragmentScoped;
import com.joomag.test.model.remote.SearchItem;

import java.util.List;

import javax.inject.Inject;

@FragmentScoped
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    @Inject
    List<SearchItem> items;
    private OnItemClickListener onItemClickListener;

    @Inject
    SearchAdapter() {
    }

    void setItems(List<SearchItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchItemBinding binding = SearchItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(binding);
        binding.getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onSearchItemClick(items.get(searchViewHolder.getAdapterPosition()));
            }
        });
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        int adapterPos = holder.getAdapterPosition();
        SearchItem searchItem = items.get(adapterPos);
        holder.binding.name.setText(searchItem.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        SearchItemBinding binding;

        SearchViewHolder(SearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onSearchItemClick(SearchItem searchItem);
    }
}
