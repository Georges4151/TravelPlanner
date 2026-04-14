package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.RoutePlan;
import com.travelplanner.model.Transport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TransportApi {
    @GET("api/transport/search")
    Call<ApiResponse<List<Transport>>> searchTransport(
            @Query("departure") String departure,
            @Query("arrival") String arrival,
            @Query("date") String date,
            @Query("type") String type);

    @GET("api/transport/route-plan")
    Call<ApiResponse<RoutePlan>> getRoutePlan(
            @Query("originLat") double originLat,
            @Query("originLng") double originLng,
            @Query("destLat") double destLat,
            @Query("destLng") double destLng,
            @Query("mode") String mode);

    @GET("api/transport/multi-route")
    Call<ApiResponse<List<RoutePlan>>> getMultiModeRoutes(
            @Query("originLat") double originLat,
            @Query("originLng") double originLng,
            @Query("destLat") double destLat,
            @Query("destLng") double destLng);

    @GET("api/transport/real-time-traffic")
    Call<ApiResponse<TrafficInfo>> getRealTimeTraffic(
            @Query("originLat") double originLat,
            @Query("originLng") double originLng,
            @Query("destLat") double destLat,
            @Query("destLng") double destLng);

    class TrafficInfo {
        private String status;
        private Integer congestionLevel;
        private Integer estimatedTime;
        private String description;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getCongestionLevel() {
            return congestionLevel;
        }

        public void setCongestionLevel(Integer congestionLevel) {
            this.congestionLevel = congestionLevel;
        }

        public Integer getEstimatedTime() {
            return estimatedTime;
        }

        public void setEstimatedTime(Integer estimatedTime) {
            this.estimatedTime = estimatedTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
