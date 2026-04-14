package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Checklist;
import com.travelplanner.model.ChecklistTemplate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChecklistApi {
    @GET("api/checklists/itinerary/{itineraryId}")
    Call<ApiResponse<List<Checklist>>> getChecklistByItinerary(@Path("itineraryId") Long itineraryId);

    @POST("api/checklists")
    Call<ApiResponse<Checklist>> addChecklistItem(@Body Checklist item);

    @PUT("api/checklists/{id}")
    Call<ApiResponse<Checklist>> updateChecklistItem(@Path("id") Long id, @Body Checklist item);

    @DELETE("api/checklists/{id}")
    Call<ApiResponse<Void>> deleteChecklistItem(@Path("id") Long id);

    @PUT("api/checklists/{id}/toggle")
    Call<ApiResponse<Checklist>> toggleCheckItem(@Path("id") Long id);

    @GET("api/checklists/templates")
    Call<ApiResponse<List<ChecklistTemplate>>> getTemplates();

    @GET("api/checklists/templates/{destination}")
    Call<ApiResponse<List<ChecklistTemplate>>> getTemplatesByDestination(@Path("destination") String destination);

    @POST("api/checklists/generate")
    Call<ApiResponse<List<Checklist>>> generateSmartChecklist(
            @Query("itineraryId") Long itineraryId,
            @Query("destination") String destination,
            @Query("season") String season,
            @Query("tripType") String tripType);

    @POST("api/checklists/batch")
    Call<ApiResponse<List<Checklist>>> addBatchItems(@Body List<Checklist> items);
}
