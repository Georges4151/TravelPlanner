package com.travelplanner.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.local.AppDatabase;
import com.travelplanner.data.local.entity.ReminderEntity;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.ReminderApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Reminder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderRepository {
    private final ReminderApi reminderApi;
    private final AppDatabase database;
    private final ExecutorService executor;

    public ReminderRepository(Context context) {
        this.reminderApi = ApiClient.getReminderApi();
        this.database = AppDatabase.getInstance(context);
        this.executor = Executors.newFixedThreadPool(4);
    }

    public LiveData<Resource<List<Reminder>>> getRemindersByItinerary(Long itineraryId) {
        MutableLiveData<Resource<List<Reminder>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        executor.execute(() -> {
            List<ReminderEntity> localItems = database.reminderDao().getByItineraryId(itineraryId);
            if (!localItems.isEmpty()) {
                List<Reminder> items = new ArrayList<>();
                for (ReminderEntity entity : localItems) {
                    items.add(entityToModel(entity));
                }
                result.postValue(Resource.success(items));
            }
        });

        reminderApi.getRemindersByItinerary(itineraryId).enqueue(new Callback<ApiResponse<List<Reminder>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Reminder>>> call,
                    Response<ApiResponse<List<Reminder>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Reminder> items = response.body().getData();
                    result.setValue(Resource.success(items));
                    executor.execute(() -> {
                        database.reminderDao().deleteByItineraryId(itineraryId);
                        for (Reminder item : items) {
                            database.reminderDao().insert(modelToEntity(item));
                        }
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Reminder>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Reminder>> createReminder(Reminder reminder) {
        MutableLiveData<Resource<Reminder>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        reminderApi.createReminder(reminder).enqueue(new Callback<ApiResponse<Reminder>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reminder>> call, Response<ApiResponse<Reminder>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Reminder savedReminder = response.body().getData();
                    result.setValue(Resource.success(savedReminder));
                    executor.execute(() -> {
                        database.reminderDao().insert(modelToEntity(savedReminder));
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Reminder>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Reminder>> updateReminder(Long id, Reminder reminder) {
        MutableLiveData<Resource<Reminder>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        reminderApi.updateReminder(id, reminder).enqueue(new Callback<ApiResponse<Reminder>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reminder>> call, Response<ApiResponse<Reminder>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Reminder updatedReminder = response.body().getData();
                    result.setValue(Resource.success(updatedReminder));
                    executor.execute(() -> {
                        database.reminderDao().update(modelToEntity(updatedReminder));
                    });
                } else {
                    result.setValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Reminder>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Void>> deleteReminder(Long id) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        reminderApi.deleteReminder(id).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(null));
                    executor.execute(() -> {
                        ReminderEntity entity = new ReminderEntity();
                        entity.setId(id);
                        database.reminderDao().delete(entity);
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

    public List<Reminder> getTriggeredReminders(long currentTime) {
        List<ReminderEntity> entities = database.reminderDao().getTriggeredReminders(currentTime);
        List<Reminder> reminders = new ArrayList<>();
        for (ReminderEntity entity : entities) {
            reminders.add(entityToModel(entity));
        }
        return reminders;
    }

    public void updateReminderStatus(Long id, int status) {
        executor.execute(() -> {
            database.reminderDao().updateStatus(id, status);
        });
    }

    private ReminderEntity modelToEntity(Reminder model) {
        ReminderEntity entity = new ReminderEntity();
        entity.setId(model.getId());
        entity.setItineraryId(model.getItineraryId());
        entity.setTitle(model.getTitle());
        entity.setContent(model.getContent());
        entity.setRemindTime(model.getRemindTime());
        entity.setType(model.getType());
        entity.setStatus(model.getStatus());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        return entity;
    }

    private Reminder entityToModel(ReminderEntity entity) {
        Reminder model = new Reminder();
        model.setId(entity.getId());
        model.setItineraryId(entity.getItineraryId());
        model.setTitle(entity.getTitle());
        model.setContent(entity.getContent());
        model.setRemindTime(entity.getRemindTime());
        model.setType(entity.getType());
        model.setStatus(entity.getStatus());
        return model;
    }
}
