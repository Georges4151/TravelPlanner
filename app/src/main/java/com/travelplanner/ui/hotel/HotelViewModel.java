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
    private final MutableLiveData<Hotel> hotelDetail = new MutableLiveData<>();
    private final MutableLiveData<List<Hotel>> hotelList = new MutableLiveData<>();

    public HotelViewModel(@NonNull Application application) {
        super(application);
        repository = new HotelRepository(application);
    }

    public void loadHotelDetail(Long id) {
        repository.getHotelDetail(id).observeForever(resource -> {
            if (resource.isSuccess()) {
                hotelDetail.setValue(resource.getData());
            }
        });
    }

    public void loadHotelsByCity(String city) {
        repository.getHotelsByCity(city).observeForever(resource -> {
            if (resource.isSuccess()) {
                hotelList.setValue(resource.getData());
            }
        });
    }

    public void searchHotels(String keyword, Double minPrice,
            Double maxPrice, Double minRating, String roomType) {
        repository.searchHotels(keyword, minPrice, maxPrice, minRating, roomType)
                .observeForever(resource -> {
                    if (resource.isSuccess()) {
                        hotelList.setValue(resource.getData());
                    }
                });
    }

    public LiveData<Hotel> getHotelDetail() {
        return hotelDetail;
    }

    public LiveData<List<Hotel>> getHotelList() {
        return hotelList;
    }
}
