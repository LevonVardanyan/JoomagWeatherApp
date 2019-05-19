package com.joomag.test.screen.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.joomag.test.R;
import com.joomag.test.WeatherApplication;
import com.joomag.test.callback.WeatherItemTouchCallback;
import com.joomag.test.databinding.HomeFragmentBinding;
import com.joomag.test.di.AppComponent;
import com.joomag.test.di.FragmentScoped;
import com.joomag.test.model.remote.SearchItem;

import javax.inject.Inject;

@FragmentScoped
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener,
        SearchAdapter.OnItemClickListener, WeatherItemTouchCallback.WeatherItemTouchListener, View.OnClickListener {

    private HomeFragmentBinding binding;
    private HomeViewModel homeViewModel;
    @Inject
    SearchAdapter searchAdapter;
    @Inject
    WeathersAdapter weathersAdapter;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        binding.setClicks(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) {
            return;
        }
        HomeComponent homeComponent = DaggerHomeComponent.builder().application(getActivity().getApplication())
                .appComponent(((WeatherApplication) getActivity().getApplication()).getAppComponent()).build();
        AppComponent appComponent = ((WeatherApplication) getActivity().getApplication()).getAppComponent();
        homeComponent.inject(this);
        homeViewModel = ViewModelProviders.of(this, appComponent.getWeatherViewModelFactory()).get(HomeViewModel.class);

        binding.setIsSearching(homeViewModel.getIsProgressShowing());
        binding.setShowMessageView(homeViewModel.getIsShowMessageView());
        setHasOptionsMenu(true);
        initActionBar();

        init();
        initSearchAdapter();
        initWeathersAdapter();
        setupObservers();
    }


    private void init() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        binding.search.setIconified(false);
        binding.search.setActivated(true);
        binding.search.onActionViewExpanded();
        binding.search.clearFocus();
        binding.search.setOnQueryTextListener(this);
        binding.search.setMaxWidth(screenWidth - 2 * getResources().getDimensionPixelSize(R.dimen.search_view_margin)
                - getResources().getDimensionPixelSize(R.dimen.remove_mode_btn_size));

        binding.swipeRefresh.setOnRefreshListener(() -> homeViewModel.refreshSavedWeathers());
    }

    private void initSearchAdapter() {
        searchAdapter.setOnItemClickListener(this);
        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.searchRecyclerView.setAdapter(searchAdapter);
        homeViewModel.getSearchResultLiveData().observe(getViewLifecycleOwner(), searchItems -> {
            binding.searchRecyclerView.setVisibility(View.VISIBLE);
            searchAdapter.setItems(searchItems);
        });

    }

    private void initWeathersAdapter() {
        binding.weathersRecyclerView.setAdapter(weathersAdapter);
        binding.weathersRecyclerView.setHasFixedSize(true);
        binding.weathersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new WeatherItemTouchCallback(this));
        itemTouchHelper.attachToRecyclerView(binding.weathersRecyclerView);
        homeViewModel.getSavedWeathers().observe(getViewLifecycleOwner(), weathers -> weathersAdapter.setItems(weathers));
    }

    private void setupObservers() {
        homeViewModel.getSearchQueryEmptyLiveData().observe(getViewLifecycleOwner(), isEmptyQuery -> {
            binding.searchRecyclerView.setVisibility(isEmptyQuery ? View.GONE : View.VISIBLE);
            binding.swipeRefresh.setVisibility(isEmptyQuery ? View.VISIBLE : View.GONE);
        });
        homeViewModel.getMessageLiveData().observe(getViewLifecycleOwner(), message -> binding.message.setText(message));
        homeViewModel.getSavedWeathersCountLiveData().observe(getViewLifecycleOwner(), savedWeathersCount ->
                homeViewModel.showMessage(savedWeathersCount == 0, getString(R.string.no_saved_locations)));
        homeViewModel.getIsRefreshing().observe(getViewLifecycleOwner(), isRefreshing ->
                binding.swipeRefresh.setRefreshing(isRefreshing));
        homeViewModel.getItemAdded().observe(getViewLifecycleOwner(), itemAdded -> {
            if (itemAdded) {
                binding.weathersRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void initActionBar() {
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(binding.toolbar);
            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        homeViewModel.search(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        homeViewModel.search(newText);
        return true;
    }

    @Override
    public void onSearchItemClick(SearchItem searchItem) {
        binding.search.setQuery("", true);
        binding.search.clearFocus();
        homeViewModel.requestWeatherAndSave(searchItem);
    }

    @Override
    public void onSwipe(int pos) {
        homeViewModel.removeWeatherFromCache(weathersAdapter.getItem(pos));
    }

    @Override
    public void onMove(int from, int to) {
        homeViewModel.swapItems(weathersAdapter.getItem(from).getId(), weathersAdapter.getItem(to).getId());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.delete_mode_btn:
                binding.setSelectionMode(true);
                weathersAdapter.setSelectionMode(true);
                Toast.makeText(getActivity(), R.string.select_items_for_remove, Toast.LENGTH_LONG).show();
                break;
            case R.id.done_remove:
                if (weathersAdapter.isNothingSelected()) {
                    Toast.makeText(getActivity(), R.string.select_items_for_remove, Toast.LENGTH_LONG).show();
                    return;
                }
                homeViewModel.removeSelectedItems(weathersAdapter.getSelectedItemIdCopies());
                binding.setSelectionMode(false);
                weathersAdapter.setSelectionMode(false);
                break;
            case R.id.cancel_remove:
                weathersAdapter.setSelectionMode(false);
                binding.setSelectionMode(false);
                break;
        }
    }
}
