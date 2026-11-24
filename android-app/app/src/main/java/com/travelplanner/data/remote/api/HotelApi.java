package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HotelApi {
    @GET("api/info/hotel/{city}")
    Call<ApiResponse<List<Hotel>>> getHotelsByCity(@Path("city") String city);
}
