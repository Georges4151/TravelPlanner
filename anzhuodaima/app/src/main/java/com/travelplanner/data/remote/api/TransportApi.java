package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.RoutePlan;
import com.travelplanner.model.Transport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TransportApi {
    @GET("api/info/transport")
    Call<ApiResponse<List<Transport>>> getTransports(
            @Query("city") String city,
            @Query("type") String type);

    @GET("api/info/transport/search")
    Call<ApiResponse<List<Transport>>> searchTransport(
            @Query("departure") String departure,
            @Query("arrival") String arrival);
}
