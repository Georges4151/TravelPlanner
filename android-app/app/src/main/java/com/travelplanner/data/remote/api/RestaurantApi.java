package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestaurantApi {
    @GET("api/info/restaurant/{city}")
    Call<ApiResponse<List<Restaurant>>> getRestaurantsByCity(@Path("city") String city);

    @GET("api/info/restaurant/cuisine/{cuisineType}")
    Call<ApiResponse<List<Restaurant>>> getRestaurantsByCuisine(@Path("cuisineType") String cuisineType);
}
