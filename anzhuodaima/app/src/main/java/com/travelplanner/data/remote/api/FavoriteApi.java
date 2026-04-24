package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Favorite;
import com.travelplanner.model.FavoriteRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteApi {
    @GET("api/favorite/check")
    Call<ApiResponse<Boolean>> checkFavorite(
            @Query("userId") Long userId,
            @Query("poiId") String poiId,
            @Query("type") String type);

    @POST("api/favorite/add")
    Call<ApiResponse<Favorite>> addFavorite(@Body FavoriteRequest request);

    @DELETE("api/favorite/remove")
    Call<ApiResponse<Void>> removeFavorite(
            @Query("userId") Long userId,
            @Query("poiId") String poiId,
            @Query("type") String type);

    @GET("api/favorite/list")
    Call<ApiResponse<List<Favorite>>> getUserFavorites(@Query("userId") Long userId);

    @GET("api/favorite/list/{type}")
    Call<ApiResponse<List<Favorite>>> getUserFavoritesByType(
            @Path("type") String typeParam,
            @Query("userId") Long userId);

    @GET("api/favorite/count")
    Call<ApiResponse<Integer>> getFavoriteCount(@Query("userId") Long userId);
}
