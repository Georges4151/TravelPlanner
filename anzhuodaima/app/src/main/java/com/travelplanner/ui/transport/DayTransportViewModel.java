package com.travelplanner.ui.transport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.travelplanner.data.local.dao.AttractionDao;
import com.travelplanner.data.local.dao.AttractionDayDao;
import com.travelplanner.data.local.dao.ItineraryDao;
import com.travelplanner.data.local.dao.ItineraryDayDao;
import com.travelplanner.data.local.dao.TransportPlanDao;
import com.travelplanner.data.local.entity.ItineraryDayEntity;
import com.travelplanner.data.local.entity.ItineraryEntity;
import com.travelplanner.data.local.entity.TransportPlanEntity;
import com.travelplanner.data.remote.api.AMapApi;
import com.travelplanner.model.TransportOptions;
import com.travelplanner.service.TransportPlanService;

import java.util.List;

public class DayTransportViewModel extends ViewModel {
    private final ItineraryDao itineraryDao;
    private final ItineraryDayDao itineraryDayDao;
    private final AttractionDayDao attractionDayDao;
    private final AttractionDao attractionDao;
    private final TransportPlanDao transportPlanDao;
    private final AMapApi aMapApi;

    private TransportPlanService transportPlanService;

    private final MutableLiveData<TransportOptions> transportOptions = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public DayTransportViewModel(ItineraryDao itineraryDao, ItineraryDayDao itineraryDayDao,
            AttractionDayDao attractionDayDao, AttractionDao attractionDao,
            TransportPlanDao transportPlanDao, AMapApi aMapApi) {
        this.itineraryDao = itineraryDao;
        this.itineraryDayDao = itineraryDayDao;
        this.attractionDayDao = attractionDayDao;
        this.attractionDao = attractionDao;
        this.transportPlanDao = transportPlanDao;
        this.aMapApi = aMapApi;

        this.transportPlanService = TransportPlanService.getInstance(aMapApi, itineraryDao, itineraryDayDao,
                attractionDayDao, attractionDao, transportPlanDao);
    }

    public LiveData<TransportOptions> getTransportOptions() {
        return transportOptions;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<ItineraryDayEntity> getDayById(Long dayId) {
        return itineraryDayDao.getById(dayId);
    }

    public LiveData<ItineraryEntity> getItineraryById(Long itineraryId) {
        MutableLiveData<ItineraryEntity> liveData = new MutableLiveData<>();
        liveData.setValue(itineraryDao.getById(itineraryId));
        return liveData;
    }

    public LiveData<List<TransportPlanEntity>> getTransportPlansByDay(Long dayId) {
        return transportPlanDao.getByDayId(dayId);
    }

    public void generateTransportPlans(Long dayId, String city) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        transportPlanService.generateDayTransportPlans(dayId, city, new TransportPlanService.TransportPlanCallback() {
            @Override
            public void onSuccess(TransportOptions options) {
                isLoading.postValue(false);
                transportOptions.postValue(options);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                errorMessage.postValue(error);
            }
        });
    }

    public void clearError() {
        errorMessage.setValue(null);
    }
}