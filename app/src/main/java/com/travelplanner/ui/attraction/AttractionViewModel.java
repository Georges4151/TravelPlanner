package com.travelplanner.ui.attraction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.repository.AttractionRepository;
import com.travelplanner.data.repository.Resource;
import com.travelplanner.model.Attraction;

import java.util.List;

public class AttractionViewModel extends AndroidViewModel {
    private final AttractionRepository repository;
    private final MutableLiveData<Attraction> attractionDetail = new MutableLiveData<>();
    private final MutableLiveData<List<Attraction>> attractionList = new MutableLiveData<>();

    public AttractionViewModel(@NonNull Application application) {
        super(application);
        repository = new AttractionRepository(application);
    }

    public void loadAttractionDetail(Long id) {
        repository.getAttractionDetail(id).observeForever(resource -> {
            if (resource.isSuccess()) {
                attractionDetail.setValue(resource.getData());
            }
        });
    }

    public void loadAttractionsByCity(String city) {
        repository.getAttractionsByCity(city).observeForever(resource -> {
            if (resource.isSuccess()) {
                attractionList.setValue(resource.getData());
            }
        });
    }

    public void searchAttractions(String keyword) {
        repository.searchAttractions(keyword).observeForever(resource -> {
            if (resource.isSuccess()) {
                attractionList.setValue(resource.getData());
            }
        });
    }

    public LiveData<Attraction> getAttractionDetail() {
        return attractionDetail;
    }

    public LiveData<List<Attraction>> getAttractionList() {
        return attractionList;
    }
}
