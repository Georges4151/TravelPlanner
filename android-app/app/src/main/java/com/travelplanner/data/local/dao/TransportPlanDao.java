package com.travelplanner.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.TransportPlanEntity;

import java.util.List;

@Dao
public interface TransportPlanDao {
    @Insert
    void insert(TransportPlanEntity transportPlan);

    @Update
    void update(TransportPlanEntity transportPlan);

    @Delete
    void delete(TransportPlanEntity transportPlan);

    @Query("SELECT * FROM transport_plans WHERE planId = :planId")
    LiveData<TransportPlanEntity> getByPlanId(String planId);

    @Query("SELECT * FROM transport_plans WHERE itineraryId = :itineraryId")
    LiveData<List<TransportPlanEntity>> getByItineraryId(Long itineraryId);

    @Query("SELECT * FROM transport_plans WHERE dayId = :dayId ORDER BY createTime DESC")
    LiveData<List<TransportPlanEntity>> getByDayId(Long dayId);

    @Query("SELECT * FROM transport_plans WHERE dayId = :dayId ORDER BY createTime DESC")
    List<TransportPlanEntity> getByDayIdSync(Long dayId);

    @Query("SELECT * FROM transport_plans WHERE dayId = :dayId AND transportMode = :transportMode ORDER BY createTime DESC")
    LiveData<List<TransportPlanEntity>> getByDayIdAndTransportMode(Long dayId, String transportMode);

    @Query("DELETE FROM transport_plans WHERE dayId = :dayId")
    void deleteByDayId(Long dayId);

    @Query("DELETE FROM transport_plans WHERE itineraryId = :itineraryId")
    void deleteByItineraryId(Long itineraryId);

    @Query("DELETE FROM transport_plans WHERE planId = :planId")
    void deleteByPlanId(String planId);

    @Query("SELECT COUNT(*) FROM transport_plans WHERE dayId = :dayId")
    LiveData<Integer> getCountByDayId(Long dayId);
}