package com.travelplanner.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.local.AppDatabase;
import com.travelplanner.data.local.entity.RestaurantEntity;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.RestaurantApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private final RestaurantApi restaurantApi;
    private final AppDatabase database;
    private final ExecutorService executor;
    private static final long CACHE_EXPIRE_TIME = 12 * 60 * 60 * 1000;

    public RestaurantRepository(Context context) {
        this.restaurantApi = ApiClient.getRestaurantApi();
        this.database = AppDatabase.getInstance(context);
        this.executor = Executors.newFixedThreadPool(4);
    }

    public LiveData<Resource<Restaurant>> getRestaurantDetail(Long id) {
        MutableLiveData<Resource<Restaurant>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        restaurantApi.getRestaurantDetail(id).enqueue(new Callback<ApiResponse<Restaurant>>() {
            @Override
            public void onResponse(Call<ApiResponse<Restaurant>> call, Response<ApiResponse<Restaurant>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Restaurant>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Restaurant>>> getRestaurantsByCity(String city) {
        MutableLiveData<Resource<List<Restaurant>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        restaurantApi.getRestaurantsByCity(city).enqueue(new Callback<ApiResponse<List<Restaurant>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurant>>> call,
                    Response<ApiResponse<List<Restaurant>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurant>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Restaurant>>> searchRestaurants(String keyword, String cuisineType,
            Double minPrice, Double maxPrice, Double minRating) {
        MutableLiveData<Resource<List<Restaurant>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        restaurantApi.searchRestaurants(keyword, cuisineType, minPrice, maxPrice, minRating)
                .enqueue(new Callback<ApiResponse<List<Restaurant>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Restaurant>>> call,
                            Response<ApiResponse<List<Restaurant>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Restaurant>>> call, Throwable t) {
                        result.setValue(Resource.error(t.getMessage()));
                    }
                });

        return result;
    }

    public LiveData<Resource<RestaurantApi.QueueStatus>> getQueueStatus(Long id) {
        MutableLiveData<Resource<RestaurantApi.QueueStatus>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        restaurantApi.getQueueStatus(id).enqueue(new Callback<ApiResponse<RestaurantApi.QueueStatus>>() {
            @Override
            public void onResponse(Call<ApiResponse<RestaurantApi.QueueStatus>> call,
                    Response<ApiResponse<RestaurantApi.QueueStatus>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RestaurantApi.QueueStatus>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }
}
