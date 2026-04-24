package com.travelplanner.ui.restaurant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.repository.RestaurantRepository;
import com.travelplanner.data.repository.Resource;
import com.travelplanner.model.Restaurant;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    private final MutableLiveData<List<Restaurant>> restaurantList = new MutableLiveData<>();

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application);
    }

    public void loadRestaurantsByCity(String city) {
        repository.getRestaurantsByCity(city).observeForever(resource -> {
            if (resource.isSuccess()) {
                restaurantList.setValue(resource.getData());
            }
        });
    }

    public void loadRestaurantsByCuisine(String cuisineType) {
        repository.getRestaurantsByCuisine(cuisineType).observeForever(resource -> {
            if (resource.isSuccess()) {
                restaurantList.setValue(resource.getData());
            }
        });
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        return restaurantList;
    }
}
