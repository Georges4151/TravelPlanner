package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HotelApi {
    @GET("api/hotels/{id}")
    Call<ApiResponse<Hotel>> getHotelDetail(@Path("id") Long id);

    @GET("api/hotels/city/{city}")
    Call<ApiResponse<List<Hotel>>> getHotelsByCity(@Path("city") String city);

    @GET("api/hotels/search")
    Call<ApiResponse<List<Hotel>>> searchHotels(
            @Query("keyword") String keyword,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice,
            @Query("rating") Double minRating,
            @Query("roomType") String roomType);

    @GET("api/hotels/nearby")
    Call<ApiResponse<List<Hotel>>> getNearbyHotels(
            @Query("lat") double latitude,
            @Query("lng") double longitude,
            @Query("radius") int radius);

    @GET("api/hotels/{id}/rooms")
    Call<ApiResponse<List<RoomInfo>>> getAvailableRooms(
            @Path("id") Long id,
            @Query("checkIn") String checkInDate,
            @Query("checkOut") String checkOutDate);

    class RoomInfo {
        private String roomType;
        private Double price;
        private Integer availableCount;
        private String facilities;

        public String getRoomType() {
            return roomType;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getAvailableCount() {
            return availableCount;
        }

        public void setAvailableCount(Integer availableCount) {
            this.availableCount = availableCount;
        }

        public String getFacilities() {
            return facilities;
        }

        public void setFacilities(String facilities) {
            this.facilities = facilities;
        }
    }
}
