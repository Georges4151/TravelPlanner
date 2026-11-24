package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Checklist;
import com.travelplanner.model.ChecklistTemplate;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChecklistApi {
    @GET("api/checklist/{itineraryId}")
    Call<ApiResponse<List<Checklist>>> getChecklistByItinerary(@Path("itineraryId") Long itineraryId);

    @GET("api/checklist/stats/{itineraryId}")
    Call<ApiResponse<Map<String, Object>>> getChecklistStats(@Path("itineraryId") Long itineraryId);

    @POST("api/checklist/generate")
    Call<ApiResponse<List<Checklist>>> generateSmartChecklist(@Body Object request);

    @PUT("api/checklist/item/{itemId}/pack")
    Call<ApiResponse<Void>> updatePackedStatus(@Path("itemId") Long itemId, @Query("isPacked") Boolean isPacked);

    @PUT("api/checklist/item/{itemId}/purchase")
    Call<ApiResponse<Void>> updatePurchasedStatus(@Path("itemId") Long itemId, @Query("isPurchased") Boolean isPurchased);

    @POST("api/checklist/item")
    Call<ApiResponse<Void>> addItem(
            @Query("itineraryId") Long itineraryId,
            @Query("itemName") String itemName,
            @Query("category") String category,
            @Query("quantity") Integer quantity,
            @Query("notes") String notes);

    @DELETE("api/checklist/item/{itemId}")
    Call<ApiResponse<Void>> deleteItem(@Path("itemId") Long itemId);

    @PUT("api/checklist/item/{itemId}")
    Call<ApiResponse<Void>> updateItem(
            @Path("itemId") Long itemId,
            @Query("itemName") String itemName,
            @Query("category") String category,
            @Query("quantity") Integer quantity,
            @Query("notes") String notes);

    @GET("api/checklist/templates/system")
    Call<ApiResponse<List<ChecklistTemplate>>> getSystemTemplates();

    @GET("api/checklist/templates/user/{userId}")
    Call<ApiResponse<List<ChecklistTemplate>>> getUserTemplates(@Path("userId") Long userId);

    @POST("api/checklist/template")
    Call<ApiResponse<ChecklistTemplate>> createTemplate(
            @Query("userId") Long userId,
            @Query("name") String name,
            @Query("description") String description,
            @Body List<Map<String, Object>> items);

    @DELETE("api/checklist/template/{templateId}")
    Call<ApiResponse<Void>> deleteTemplate(@Path("templateId") Long templateId, @Query("userId") Long userId);
}
