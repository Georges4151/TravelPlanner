package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Reminder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReminderApi {
    @GET("api/reminder/{id}")
    Call<ApiResponse<Reminder>> getReminderDetail(@Path("id") Long id);

    @GET("api/reminder/itinerary/{itineraryId}")
    Call<ApiResponse<List<Reminder>>> getRemindersByItinerary(@Path("itineraryId") Long itineraryId);

    @GET("api/reminder/upcoming")
    Call<ApiResponse<List<Reminder>>> getUpcomingReminders(@Query("hours") int hours);

    @POST("api/reminder")
    Call<ApiResponse<Reminder>> createReminder(@Body Reminder reminder);

    @PUT("api/reminder/{id}")
    Call<ApiResponse<Reminder>> updateReminder(@Path("id") Long id, @Body Reminder reminder);

    @DELETE("api/reminder/{id}")
    Call<ApiResponse<Void>> deleteReminder(@Path("id") Long id);

    @PUT("api/reminder/{id}/status")
    Call<ApiResponse<Void>> updateReminderStatus(@Path("id") Long id, @Query("status") int status);

    @POST("api/reminder/batch")
    Call<ApiResponse<List<Reminder>>> createBatchReminders(@Body List<Reminder> reminders);
}
