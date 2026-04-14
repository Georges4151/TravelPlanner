package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantApi {
    @GET("api/restaurants/{id}")
    Call<ApiResponse<Restaurant>> getRestaurantDetail(@Path("id") Long id);

    @GET("api/restaurants/city/{city}")
    Call<ApiResponse<List<Restaurant>>> getRestaurantsByCity(@Path("city") String city);

    @GET("api/restaurants/search")
    Call<ApiResponse<List<Restaurant>>> searchRestaurants(
            @Query("keyword") String keyword,
            @Query("cuisineType") String cuisineType,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice,
            @Query("rating") Double minRating);

    @GET("api/restaurants/nearby")
    Call<ApiResponse<List<Restaurant>>> getNearbyRestaurants(
            @Query("lat") double latitude,
            @Query("lng") double longitude,
            @Query("radius") int radius);

    @GET("api/restaurants/{id}/queue-status")
    Call<ApiResponse<QueueStatus>> getQueueStatus(@Path("id") Long id);

    class QueueStatus {
        private String status;
        private Integer waitTime;
        private Integer queueCount;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getWaitTime() {
            return waitTime;
        }

        public void setWaitTime(Integer waitTime) {
            this.waitTime = waitTime;
        }

        public Integer getQueueCount() {
            return queueCount;
        }

        public void setQueueCount(Integer queueCount) {
            this.queueCount = queueCount;
        }
    }
}
