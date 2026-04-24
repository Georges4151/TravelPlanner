package com.travelplanner.ui.checklist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.repository.ChecklistRepository;
import com.travelplanner.data.repository.Resource;
import com.travelplanner.model.Checklist;

import java.util.List;

public class ChecklistViewModel extends AndroidViewModel {
    private final ChecklistRepository repository;
    private final MutableLiveData<List<Checklist>> checklistItems = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();

    public ChecklistViewModel(@NonNull Application application) {
        super(application);
        repository = new ChecklistRepository(application);
    }

    public void loadChecklist(Long itineraryId) {
        repository.getChecklistByItinerary(itineraryId).observeForever(resource -> {
            if (resource.isSuccess()) {
                checklistItems.setValue(resource.getData());
            }
        });
    }

    public void addItem(Long itineraryId, String itemName, String category,
            Integer quantity, String notes) {
        repository.addItem(itineraryId, itemName, category, quantity, notes)
                .observeForever(resource -> {
                    operationSuccess.setValue(resource.isSuccess());
                });
    }

    public void updateItem(Long itemId, String itemName, String category,
            Integer quantity, String notes) {
        repository.updateItem(itemId, itemName, category, quantity, notes)
                .observeForever(resource -> {
                    operationSuccess.setValue(resource.isSuccess());
                });
    }

    public void deleteItem(Long id) {
        repository.deleteItem(id).observeForever(resource -> {
            operationSuccess.setValue(resource.isSuccess());
        });
    }

    public void togglePackedStatus(Long itemId, Boolean isPacked) {
        repository.updatePackedStatus(itemId, isPacked)
                .observeForever(resource -> {
                    operationSuccess.setValue(resource.isSuccess());
                });
    }

    public void togglePurchasedStatus(Long itemId, Boolean isPurchased) {
        repository.updatePurchasedStatus(itemId, isPurchased)
                .observeForever(resource -> {
                    operationSuccess.setValue(resource.isSuccess());
                });
    }

    public LiveData<List<Checklist>> getChecklistItems() {
        return checklistItems;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }
}
