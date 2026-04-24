package com.travelplanner.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.TransportApi;
import com.travelplanner.model.ApiResponse;
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

    public LiveData<Resource<List<Transport>>> searchTransport(String departure, String arrival) {
        MutableLiveData<Resource<List<Transport>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        transportApi.searchTransport(departure, arrival)
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

    public LiveData<Resource<List<Transport>>> getTransports(String city, String type) {
        MutableLiveData<Resource<List<Transport>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        transportApi.getTransports(city, type)
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
}
