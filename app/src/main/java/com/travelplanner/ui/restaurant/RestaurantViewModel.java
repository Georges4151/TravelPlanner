package com.travelplanner.ui.restaurant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.remote.api.RestaurantApi;
import com.travelplanner.data.repository.RestaurantRepository;
import com.travelplanner.data.repository.Resource;
import com.travelplanner.model.Restaurant;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    private final MutableLiveData<Restaurant> restaurantDetail = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> restaurantList = new MutableLiveData<>();
    private final MutableLiveData<RestaurantApi.QueueStatus> queueStatus = new MutableLiveData<>();

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application);
    }

    public void loadRestaurantDetail(Long id) {
        repository.getRestaurantDetail(id).observeForever(resource -> {
            if (resource.isSuccess()) {
                restaurantDetail.setValue(resource.getData());
            }
        });
    }

    public void loadRestaurantsByCity(String city) {
        repository.getRestaurantsByCity(city).observeForever(resource -> {
            if (resource.isSuccess()) {
                restaurantList.setValue(resource.getData());
            }
        });
    }

    public void searchRestaurants(String keyword, String cuisineType,
            Double minPrice, Double maxPrice, Double minRating) {
        repository.searchRestaurants(keyword, cuisineType, minPrice, maxPrice, minRating)
                .observeForever(resource -> {
                    if (resource.isSuccess()) {
                        restaurantList.setValue(resource.getData());
                    }
                });
    }

    public void loadQueueStatus(Long id) {
        repository.getQueueStatus(id).observeForever(resource -> {
            if (resource.isSuccess()) {
                queueStatus.setValue(resource.getData());
            }
        });
    }

    public LiveData<Restaurant> getRestaurantDetail() {
        return restaurantDetail;
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        return restaurantList;
    }

    public LiveData<RestaurantApi.QueueStatus> getQueueStatus() {
        return queueStatus;
    }
}
