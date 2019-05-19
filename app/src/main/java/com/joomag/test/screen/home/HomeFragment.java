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
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joomag.test.R;
import com.joomag.test.WeatherViewModelFactory;
import com.joomag.test.callback.WeatherItemTouchCallback;
import com.joomag.test.databinding.HomeFragmentBinding;
import com.joomag.test.model.remote.SearchItem;
import com.joomag.test.model.remote.Weather;
import com.joomag.test.util.Constants;

import java.util.Objects;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener,
        SearchAdapter.OnItemClickListener, WeatherItemTouchCallback.WeatherItemTouchListener,
        WeathersAdapter.OnItemClickListener, View.OnClickListener {

    private HomeFragmentBinding binding;
    private HomeViewModel homeViewModel;

    private SearchAdapter searchAdapter;
    private WeathersAdapter weathersAdapter;

    private NavController navController;

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
        navController = Navigation.findNavController(getActivity(), R.id.main_nav_host);
        homeViewModel = obtainViewModel(this);
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

        binding.swipeRefresh.setOnRefreshListener(() -> {
            homeViewModel.refreshSavedWeathers();
        });
    }

    private void initSearchAdapter() {
        if (searchAdapter == null) {
            searchAdapter = new SearchAdapter();
        }
        searchAdapter.setOnItemClickListener(this);
        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.searchRecyclerView.setAdapter(searchAdapter);
        homeViewModel.getSearchResultLiveData().observe(getViewLifecycleOwner(), searchItems -> {
            binding.searchRecyclerView.setVisibility(View.VISIBLE);
            searchAdapter.setItems(searchItems);
        });

    }

    private void initWeathersAdapter() {
        if (weathersAdapter == null) {
            weathersAdapter = new WeathersAdapter();
        }
        weathersAdapter.setOnItemClickListener(this);
        binding.weathersRecyclerView.setHasFixedSize(true);
        binding.weathersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.weathersRecyclerView.setAdapter(weathersAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new WeatherItemTouchCallback(this));
        itemTouchHelper.attachToRecyclerView(binding.weathersRecyclerView);
        homeViewModel.getSavedWeathers().observe(getViewLifecycleOwner(), weathers -> {
            weathersAdapter.setItems(weathers);
        });
    }

    private void setupObservers() {
        homeViewModel.getSearchQueryEmptyLiveData().observe(getViewLifecycleOwner(), isEmptyQuery -> {
            binding.searchRecyclerView.setVisibility(isEmptyQuery ? View.GONE : View.VISIBLE);
            binding.swipeRefresh.setVisibility(isEmptyQuery ? View.VISIBLE : View.GONE);
        });
        homeViewModel.getMessageLiveData().observe(getViewLifecycleOwner(), message -> {
            binding.message.setText(message);
        });
        homeViewModel.getSavedWeathersCountLiveData().observe(getViewLifecycleOwner(), savedWeathersCount -> {
            homeViewModel.showMessage(savedWeathersCount == 0, getString(R.string.no_saved_locations));
        });
        homeViewModel.getIsRefreshing().observe(getViewLifecycleOwner(), isRefreshing -> {
            binding.swipeRefresh.setRefreshing(isRefreshing);
        });
        homeViewModel.getItemAdded().observe(getViewLifecycleOwner(), aBoolean ->
                binding.weathersRecyclerView.smoothScrollToPosition(0));
    }

    private void initActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);

        }
    }

    private HomeViewModel obtainViewModel(Fragment fragment) {
        WeatherViewModelFactory factory = WeatherViewModelFactory.getInstance(
                Objects.requireNonNull(fragment.getActivity()).getApplication());
        return ViewModelProviders.of(fragment, factory).get(HomeViewModel.class);
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
    public void onWeatherItemClick(Weather weather, View sharedView) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(sharedView, ViewCompat.getTransitionName(sharedView))
                .build();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_WEATHER_LOCATION_NAME, weather.getLocation().getName());
        bundle.putString(Constants.EXTRA_TRANSITION_NAME, ViewCompat.getTransitionName(sharedView));
        navController.navigate(R.id.action_homeFragment_to_weatherDetailsFragment, bundle, null, extras);
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
