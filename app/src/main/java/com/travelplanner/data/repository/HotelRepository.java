package com.travelplanner.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.local.AppDatabase;
import com.travelplanner.data.local.entity.HotelEntity;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.HotelApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Hotel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelRepository {
    private final HotelApi hotelApi;
    private final AppDatabase database;
    private final ExecutorService executor;
    private static final long CACHE_EXPIRE_TIME = 24 * 60 * 60 * 1000;

    public HotelRepository(Context context) {
        this.hotelApi = ApiClient.getHotelApi();
        this.database = AppDatabase.getInstance(context);
        this.executor = Executors.newFixedThreadPool(4);
    }

    public LiveData<Resource<Hotel>> getHotelDetail(Long id) {
        MutableLiveData<Resource<Hotel>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        hotelApi.getHotelDetail(id).enqueue(new Callback<ApiResponse<Hotel>>() {
            @Override
            public void onResponse(Call<ApiResponse<Hotel>> call, Response<ApiResponse<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Hotel>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Hotel>>> getHotelsByCity(String city) {
        MutableLiveData<Resource<List<Hotel>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        hotelApi.getHotelsByCity(city).enqueue(new Callback<ApiResponse<List<Hotel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Hotel>>> call, Response<ApiResponse<List<Hotel>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Hotel>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Hotel>>> searchHotels(String keyword, Double minPrice,
            Double maxPrice, Double minRating, String roomType) {
        MutableLiveData<Resource<List<Hotel>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        hotelApi.searchHotels(keyword, minPrice, maxPrice, minRating, roomType)
                .enqueue(new Callback<ApiResponse<List<Hotel>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Hotel>>> call,
                            Response<ApiResponse<List<Hotel>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Hotel>>> call, Throwable t) {
                        result.setValue(Resource.error(t.getMessage()));
                    }
                });

        return result;
    }
}
