package com.travelplanner.ui.reminder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.repository.ReminderRepository;
import com.travelplanner.data.repository.Resource;
import com.travelplanner.model.Reminder;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {
    private final ReminderRepository repository;
    private final MutableLiveData<List<Reminder>> reminderList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        repository = new ReminderRepository(application);
    }

    public void loadReminders(Long itineraryId) {
        repository.getRemindersByItinerary(itineraryId).observeForever(resource -> {
            if (resource.isSuccess()) {
                reminderList.setValue(resource.getData());
            }
        });
    }

    public void createReminder(Reminder reminder) {
        repository.createReminder(reminder).observeForever(resource -> {
            operationSuccess.setValue(resource.isSuccess());
        });
    }

    public void updateReminder(Long id, Reminder reminder) {
        repository.updateReminder(id, reminder).observeForever(resource -> {
            operationSuccess.setValue(resource.isSuccess());
        });
    }

    public void deleteReminder(Long id) {
        repository.deleteReminder(id).observeForever(resource -> {
            operationSuccess.setValue(resource.isSuccess());
        });
    }

    public LiveData<List<Reminder>> getReminderList() {
        return reminderList;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }
}
