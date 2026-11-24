package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Attraction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AttractionApi {
    @GET("api/attraction/{id}")
    Call<ApiResponse<Attraction>> getAttractionDetail(@Path("id") Long id);

    @GET("api/attraction/city/{city}")
    Call<ApiResponse<List<Attraction>>> getAttractionsByCity(@Path("city") String city);

    @GET("api/attraction/search")
    Call<ApiResponse<List<Attraction>>> searchAttractions(@Query("keyword") String keyword, @Query("city") String city);

    @GET("api/attraction/{id}/crowd")
    Call<ApiResponse<List<CrowdForecast>>> getCrowdForecast(
            @Path("id") Long id,
            @Query("days") int days);

    @GET("api/attraction/top")
    Call<ApiResponse<List<Attraction>>> getTopAttractions(@Query("limit") int limit);

    @GET("api/attraction/province/{province}")
    Call<ApiResponse<List<Attraction>>> getAttractionsByProvince(@Path("province") String province);

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
