package com.travelplanner.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.local.AppDatabase;
import com.travelplanner.data.local.entity.ChecklistItemEntity;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.ChecklistApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Checklist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChecklistRepository {
    private final ChecklistApi checklistApi;
    private final AppDatabase database;
    private final ExecutorService executor;

    public ChecklistRepository(Context context) {
        this.checklistApi = ApiClient.getChecklistApi();
        this.database = AppDatabase.getInstance(context);
        this.executor = Executors.newFixedThreadPool(4);
    }

    public LiveData<Resource<List<Checklist>>> getChecklistByItinerary(Long itineraryId) {
        MutableLiveData<Resource<List<Checklist>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        executor.execute(() -> {
            List<ChecklistItemEntity> localItems = database.checklistDao().getByItineraryId(itineraryId);
            if (!localItems.isEmpty()) {
                List<Checklist> items = new ArrayList<>();
                for (ChecklistItemEntity entity : localItems) {
                    items.add(entityToModel(entity));
                }
                result.postValue(Resource.success(items));
            }
        });

        checklistApi.getChecklistByItinerary(itineraryId).enqueue(new Callback<ApiResponse<List<Checklist>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Checklist>>> call,
                    Response<ApiResponse<List<Checklist>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Checklist> items = response.body().getData();
                    result.setValue(Resource.success(items));
                    executor.execute(() -> {
                        database.checklistDao().deleteByItineraryId(itineraryId);
                        for (Checklist item : items) {
                            database.checklistDao().insert(modelToEntity(item));
                        }
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Checklist>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Checklist>> addChecklistItem(Checklist item) {
        MutableLiveData<Resource<Checklist>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        checklistApi.addChecklistItem(item).enqueue(new Callback<ApiResponse<Checklist>>() {
            @Override
            public void onResponse(Call<ApiResponse<Checklist>> call, Response<ApiResponse<Checklist>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Checklist savedItem = response.body().getData();
                    result.setValue(Resource.success(savedItem));
                    executor.execute(() -> {
                        database.checklistDao().insert(modelToEntity(savedItem));
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Checklist>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Checklist>> updateChecklistItem(Long id, Checklist item) {
        MutableLiveData<Resource<Checklist>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        checklistApi.updateChecklistItem(id, item).enqueue(new Callback<ApiResponse<Checklist>>() {
            @Override
            public void onResponse(Call<ApiResponse<Checklist>> call, Response<ApiResponse<Checklist>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Checklist updatedItem = response.body().getData();
                    result.setValue(Resource.success(updatedItem));
                    executor.execute(() -> {
                        database.checklistDao().update(modelToEntity(updatedItem));
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Checklist>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Void>> deleteChecklistItem(Long id) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        checklistApi.deleteChecklistItem(id).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(null));
                    executor.execute(() -> {
                        ChecklistItemEntity entity = new ChecklistItemEntity();
                        entity.setId(id);
                        database.checklistDao().delete(entity);
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Checklist>> toggleCheckItem(Long id) {
        MutableLiveData<Resource<Checklist>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        checklistApi.toggleCheckItem(id).enqueue(new Callback<ApiResponse<Checklist>>() {
            @Override
            public void onResponse(Call<ApiResponse<Checklist>> call, Response<ApiResponse<Checklist>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Checklist updatedItem = response.body().getData();
                    result.setValue(Resource.success(updatedItem));
                    executor.execute(() -> {
                        database.checklistDao().update(modelToEntity(updatedItem));
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Checklist>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    private ChecklistItemEntity modelToEntity(Checklist model) {
        ChecklistItemEntity entity = new ChecklistItemEntity();
        entity.setId(model.getId());
        entity.setItineraryId(model.getItineraryId());
        entity.setItemName(model.getItemName());
        entity.setCategory(model.getCategory());
        entity.setIsChecked(model.getIsChecked());
        entity.setNotes(model.getNotes());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        return entity;
    }

    private Checklist entityToModel(ChecklistItemEntity entity) {
        Checklist model = new Checklist();
        model.setId(entity.getId());
        model.setItineraryId(entity.getItineraryId());
        model.setItemName(entity.getItemName());
        model.setCategory(entity.getCategory());
        model.setIsChecked(entity.getIsChecked());
        model.setNotes(entity.getNotes());
        return model;
    }
}
