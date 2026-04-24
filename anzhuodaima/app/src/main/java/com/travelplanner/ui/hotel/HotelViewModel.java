package com.travelplanner.ui.hotel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.repository.HotelRepository;
import com.travelplanner.data.repository.Resource;
import com.travelplanner.model.Hotel;

import java.util.List;

public class HotelViewModel extends AndroidViewModel {
    private final HotelRepository repository;
    private final MutableLiveData<List<Hotel>> hotelList = new MutableLiveData<>();

    public HotelViewModel(@NonNull Application application) {
        super(application);
        repository = new HotelRepository(application);
    }

    public void loadHotelsByCity(String city) {
        repository.getHotelsByCity(city).observeForever(resource -> {
            if (resource.isSuccess()) {
                hotelList.setValue(resource.getData());
            }
        });
    }

    public LiveData<List<Hotel>> getHotelList() {
        return hotelList;
    }
}
