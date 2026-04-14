package com.travelplanner.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.TransportApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.RoutePlan;
import com.travelplanner.model.Transport;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportRepository {
    private final TransportApi transportApi;

    public TransportRepository() {
        this.transportApi = ApiClient.getTransportApi();
    }

    public LiveData<Resource<List<Transport>>> searchTransport(String departure, String arrival,
            String date, String type) {
        MutableLiveData<Resource<List<Transport>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        transportApi.searchTransport(departure, arrival, date, type)
                .enqueue(new Callback<ApiResponse<List<Transport>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Transport>>> call,
                            Response<ApiResponse<List<Transport>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Transport>>> call, Throwable t) {
                        result.setValue(Resource.error(t.getMessage()));
                    }
                });

        return result;
    }

    public LiveData<Resource<RoutePlan>> getRoutePlan(double originLat, double originLng,
            double destLat, double destLng, String mode) {
        MutableLiveData<Resource<RoutePlan>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        transportApi.getRoutePlan(originLat, originLng, destLat, destLng, mode)
                .enqueue(new Callback<ApiResponse<RoutePlan>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<RoutePlan>> call,
                            Response<ApiResponse<RoutePlan>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<RoutePlan>> call, Throwable t) {
                        result.setValue(Resource.error(t.getMessage()));
                    }
                });

        return result;
    }

    public LiveData<Resource<List<RoutePlan>>> getMultiModeRoutes(double originLat, double originLng,
            double destLat, double destLng) {
        MutableLiveData<Resource<List<RoutePlan>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        transportApi.getMultiModeRoutes(originLat, originLng, destLat, destLng)
                .enqueue(new Callback<ApiResponse<List<RoutePlan>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<RoutePlan>>> call,
                            Response<ApiResponse<List<RoutePlan>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<RoutePlan>>> call, Throwable t) {
                        result.setValue(Resource.error(t.getMessage()));
                    }
                });

        return result;
    }

    public LiveData<Resource<TransportApi.TrafficInfo>> getRealTimeTraffic(double originLat,
            double originLng, double destLat, double destLng) {
        MutableLiveData<Resource<TransportApi.TrafficInfo>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        transportApi.getRealTimeTraffic(originLat, originLng, destLat, destLng)
                .enqueue(new Callback<ApiResponse<TransportApi.TrafficInfo>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<TransportApi.TrafficInfo>> call,
                            Response<ApiResponse<TransportApi.TrafficInfo>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<TransportApi.TrafficInfo>> call, Throwable t) {
                        result.setValue(Resource.error(t.getMessage()));
                    }
                });

        return result;
    }
}
