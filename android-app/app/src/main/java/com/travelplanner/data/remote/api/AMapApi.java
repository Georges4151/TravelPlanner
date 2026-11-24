package com.travelplanner.data.remote.api;

import com.travelplanner.data.remote.AMapConfig;
import com.travelplanner.model.amap.AMapGeocodeResponse;
import com.travelplanner.model.amap.AMapDirectionResponse;
import com.travelplanner.model.amap.AMapPoiSearchResponse;
import com.travelplanner.model.amap.AMapWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface AMapApi {

        @GET("geocode/geo")
        Call<AMapGeocodeResponse> geocode(
                        @Query("key") String key,
                        @Query("address") String address,
                        @Query("city") String city);

        @GET("direction/driving")
        Call<AMapDirectionResponse> drivingDirection(
                        @Query("key") String key,
                        @Query("origin") String origin,
                        @Query("destination") String destination,
                        @Query("extensions") String extensions,
                        @Query("strategy") Integer strategy);

        @GET("direction/walking")
        Call<AMapDirectionResponse> walkingDirection(
                        @Query("key") String key,
                        @Query("origin") String origin,
                        @Query("destination") String destination);

        @GET("direction/transit/integrated")
        Call<AMapDirectionResponse> transitDirection(
                        @Query("key") String key,
                        @Query("origin") String origin,
                        @Query("destination") String destination,
                        @Query("city") String city,
                        @Query("cityd") String cityDestination);

        @GET("direction/bicycling")
        Call<AMapDirectionResponse> bicyclingDirection(
                        @Query("key") String key,
                        @Query("origin") String origin,
                        @Query("destination") String destination);

        @GET("place/text")
        Call<AMapPoiSearchResponse> poiSearch(
                        @Query("key") String key,
                        @Query("keywords") String keywords,
                        @Query("location") String location,
                        @Query("city") String city,
                        @Query("types") String types,
                        @Query("offset") Integer offset,
                        @Query("page") Integer page);

        @GET("place/around")
        Call<AMapPoiSearchResponse> poiAroundSearch(
                        @Query("key") String key,
                        @Query("keywords") String keywords,
                        @Query("location") String location,
                        @Query("radius") Integer radius,
                        @Query("types") String types,
                        @Query("offset") Integer offset,
                        @Query("page") Integer page,
                        @Query("extensions") String extensions);

        @GET("weather/weatherInfo")
        Call<AMapWeatherResponse> weatherForecast(
                        @Query("key") String key,
                        @Query("city") String city,
                        @Query("extensions") String extensions);
}
