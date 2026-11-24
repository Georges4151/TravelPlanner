package com.travelplanner.data.remote.api;

import com.travelplanner.model.ApiResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface TeamApi {

    // ==================== 团队管理 ====================

    @POST("api/team/create")
    Call<ApiResponse<Map<String, Object>>> createTeam(@Body Map<String, Object> teamData);

    @GET("api/team/{teamId}")
    Call<ApiResponse<Map<String, Object>>> getTeam(
            @Path("teamId") Long teamId,
            @Query("userId") Long userId
    );

    @GET("api/team/user/{userId}")
    Call<ApiResponse<List<Map<String, Object>>>> getUserTeams(@Path("userId") Long userId);

    @POST("api/team/join")
    Call<ApiResponse<Map<String, Object>>> joinByInviteCode(@Body Map<String, Object> joinData);

    @GET("api/team/{teamId}/members")
    Call<ApiResponse<List<Map<String, Object>>>> getTeamMembers(@Path("teamId") Long teamId);

    @DELETE("api/team/{teamId}/member/{memberId}")
    Call<ApiResponse<Void>> removeMember(
            @Path("teamId") Long teamId,
            @Path("memberId") Long memberId,
            @Query("operatorId") Long operatorId
    );

    @DELETE("api/team/{teamId}")
    Call<ApiResponse<Void>> deleteTeam(
            @Path("teamId") Long teamId,
            @Query("userId") Long userId
    );

    // ==================== 待办事项 ====================

    @GET("api/team/{teamId}/todos")
    Call<ApiResponse<List<Map<String, Object>>>> getTeamTodos(
            @Path("teamId") Long teamId,
            @Query("userId") Long userId
    );

    @POST("api/team/todo/create")
    Call<ApiResponse<Map<String, Object>>> createTodo(
            @Body Map<String, Object> todoData
    );

    @PUT("api/team/todo/{todoId}/status")
    Call<ApiResponse<Void>> updateTodoStatus(
            @Path("todoId") Long todoId,
            @Query("status") String status,
            @Query("userId") Long userId
    );

    @DELETE("api/team/todo/{todoId}")
    Call<ApiResponse<Void>> deleteTodo(
            @Path("todoId") Long todoId,
            @Query("userId") Long userId
    );

    // ==================== 投票 ====================

    @GET("api/team/{teamId}/votes")
    Call<ApiResponse<List<Map<String, Object>>>> getTeamVotes(
            @Path("teamId") Long teamId,
            @Query("userId") Long userId
    );

    @POST("api/team/vote/create")
    Call<ApiResponse<Map<String, Object>>> createVote(
            @Body Map<String, Object> voteData
    );

    @POST("api/team/vote/cast")
    Call<ApiResponse<Void>> castVote(
            @Body Map<String, Object> voteData
    );

    @PUT("api/team/vote/{voteId}/close")
    Call<ApiResponse<Void>> closeVote(
            @Path("voteId") Long voteId,
            @Query("userId") Long userId
    );

    // ==================== 消息 ====================

    @GET("api/team/{teamId}/messages")
    Call<ApiResponse<List<Map<String, Object>>>> getMessages(
            @Path("teamId") Long teamId,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @POST("api/team/message/send")
    Call<ApiResponse<Map<String, Object>>> sendMessage(
            @Body Map<String, Object> messageData
    );

    // ==================== 活动动态 ====================

    @GET("api/team/{teamId}/activities")
    Call<ApiResponse<List<Map<String, Object>>>> getActivities(
            @Path("teamId") Long teamId,
            @Query("offset") int offset,
            @Query("limit") int limit
    );
}
