package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Attraction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AttractionApi {
    @GET("api/attractions/{id}")
    Call<ApiResponse<Attraction>> getAttractionDetail(@Path("id") Long id);

    @GET("api/attractions/city/{city}")
    Call<ApiResponse<List<Attraction>>> getAttractionsByCity(@Path("city") String city);

    @GET("api/attractions/search")
    Call<ApiResponse<List<Attraction>>> searchAttractions(@Query("keyword") String keyword);

    @GET("api/attractions/{id}/crowd-forecast")
    Call<ApiResponse<List<CrowdForecast>>> getCrowdForecast(
            @Path("id") Long id,
            @Query("days") int days);

    @GET("api/attractions/nearby")
    Call<ApiResponse<List<Attraction>>> getNearbyAttractions(
            @Query("lat") double latitude,
            @Query("lng") double longitude,
            @Query("radius") int radius);

    class CrowdForecast {
        private String date;
        private String crowdLevel;
        private String crowdIndex;
        private String bestTime;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCrowdLevel() {
            return crowdLevel;
        }

        public void setCrowdLevel(String crowdLevel) {
            this.crowdLevel = crowdLevel;
        }

        public String getCrowdIndex() {
            return crowdIndex;
        }

        public void setCrowdIndex(String crowdIndex) {
            this.crowdIndex = crowdIndex;
        }

        public String getBestTime() {
            return bestTime;
        }

        public void setBestTime(String bestTime) {
            this.bestTime = bestTime;
        }
    }
}
