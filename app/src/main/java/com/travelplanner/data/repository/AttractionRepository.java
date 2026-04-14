package com.travelplanner.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.local.AppDatabase;
import com.travelplanner.data.local.entity.AttractionEntity;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.AttractionApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Attraction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttractionRepository {
    private final AttractionApi attractionApi;
    private final AppDatabase database;
    private final ExecutorService executor;
    private static final long CACHE_EXPIRE_TIME = 24 * 60 * 60 * 1000;

    public AttractionRepository(Context context) {
        this.attractionApi = ApiClient.getAttractionApi();
        this.database = AppDatabase.getInstance(context);
        this.executor = Executors.newFixedThreadPool(4);
    }

    public LiveData<Resource<Attraction>> getAttractionDetail(Long id) {
        MutableLiveData<Resource<Attraction>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        executor.execute(() -> {
            AttractionEntity cached = database.attractionDao().getById(id);
            if (cached != null && !isCacheExpired(cached.getCacheTime())) {
                result.postValue(Resource.success(entityToModel(cached)));
            }
        });

        attractionApi.getAttractionDetail(id).enqueue(new Callback<ApiResponse<Attraction>>() {
            @Override
            public void onResponse(Call<ApiResponse<Attraction>> call, Response<ApiResponse<Attraction>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Attraction attraction = response.body().getData();
                    result.setValue(Resource.success(attraction));
                    executor.execute(() -> {
                        database.attractionDao().insert(modelToEntity(attraction));
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Attraction>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Attraction>>> getAttractionsByCity(String city) {
        MutableLiveData<Resource<List<Attraction>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        executor.execute(() -> {
            List<AttractionEntity> cached = database.attractionDao().getByCity(city);
            if (!cached.isEmpty() && !isCacheExpired(cached.get(0).getCacheTime())) {
                List<Attraction> attractions = new ArrayList<>();
                for (AttractionEntity entity : cached) {
                    attractions.add(entityToModel(entity));
                }
                result.postValue(Resource.success(attractions));
            }
        });

        attractionApi.getAttractionsByCity(city).enqueue(new Callback<ApiResponse<List<Attraction>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Attraction>>> call,
                    Response<ApiResponse<List<Attraction>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Attraction> attractions = response.body().getData();
                    result.setValue(Resource.success(attractions));
                    executor.execute(() -> {
                        List<AttractionEntity> entities = new ArrayList<>();
                        for (Attraction attraction : attractions) {
                            entities.add(modelToEntity(attraction));
                        }
                        database.attractionDao().insertAll(entities);
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Attraction>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Attraction>>> searchAttractions(String keyword) {
        MutableLiveData<Resource<List<Attraction>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        attractionApi.searchAttractions(keyword).enqueue(new Callback<ApiResponse<List<Attraction>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Attraction>>> call,
                    Response<ApiResponse<List<Attraction>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Attraction>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    private boolean isCacheExpired(Long cacheTime) {
        return System.currentTimeMillis() - cacheTime > CACHE_EXPIRE_TIME;
    }

    private AttractionEntity modelToEntity(Attraction model) {
        AttractionEntity entity = new AttractionEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setCity(model.getCity());
        entity.setProvince(model.getProvince());
        entity.setDescription(model.getDescription());
        entity.setAddress(model.getAddress());
        entity.setOpenTime(model.getOpenTime());
        entity.setTicketPrice(model.getTicketPrice());
        entity.setPlayDuration(model.getPlayDuration());
        entity.setCrowdLevel(model.getCrowdLevel());
        entity.setCrowdIndex(model.getCrowdIndex());
        entity.setBestVisitTime(model.getBestVisitTime());
        entity.setImageUrl(model.getImageUrl());
        entity.setRating(model.getRating());
        entity.setCommentCount(model.getCommentCount());
        entity.setLatitude(model.getLatitude());
        entity.setLongitude(model.getLongitude());
        entity.setReservationUrl(model.getReservationUrl());
        entity.setPhone(model.getPhone());
        entity.setFacilities(model.getFacilities());
        entity.setTips(model.getTips());
        entity.setCacheTime(System.currentTimeMillis());
        return entity;
    }

    private Attraction entityToModel(AttractionEntity entity) {
        Attraction model = new Attraction();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setCity(entity.getCity());
        model.setProvince(entity.getProvince());
        model.setDescription(entity.getDescription());
        model.setAddress(entity.getAddress());
        model.setOpenTime(entity.getOpenTime());
        model.setTicketPrice(entity.getTicketPrice());
        model.setPlayDuration(entity.getPlayDuration());
        model.setCrowdLevel(entity.getCrowdLevel());
        model.setCrowdIndex(entity.getCrowdIndex());
        model.setBestVisitTime(entity.getBestVisitTime());
        model.setImageUrl(entity.getImageUrl());
        model.setRating(entity.getRating());
        model.setCommentCount(entity.getCommentCount());
        model.setLatitude(entity.getLatitude());
        model.setLongitude(entity.getLongitude());
        model.setReservationUrl(entity.getReservationUrl());
        model.setPhone(entity.getPhone());
        model.setFacilities(entity.getFacilities());
        model.setTips(entity.getTips());
        return model;
    }
}
