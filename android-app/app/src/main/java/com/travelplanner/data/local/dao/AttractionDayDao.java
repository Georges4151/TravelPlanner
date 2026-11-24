package com.travelplanner.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.AttractionDayEntity;

import java.util.List;

@Dao
public interface AttractionDayDao {
    @Insert
    void insert(AttractionDayEntity attractionDay);

    @Update
    void update(AttractionDayEntity attractionDay);

    @Delete
    void delete(AttractionDayEntity attractionDay);

    @Query("SELECT * FROM attraction_days WHERE id = :id")
    LiveData<AttractionDayEntity> getById(Long id);

    @Query("SELECT * FROM attraction_days WHERE dayId = :dayId ORDER BY visitOrder ASC")
    LiveData<List<AttractionDayEntity>> getByDayId(Long dayId);

    @Query("SELECT * FROM attraction_days WHERE dayId = :dayId ORDER BY visitOrder ASC")
    List<AttractionDayEntity> getByDayIdSync(Long dayId);

    @Query("DELETE FROM attraction_days WHERE dayId = :dayId")
    void deleteByDayId(Long dayId);

    @Query("SELECT COUNT(*) FROM attraction_days WHERE dayId = :dayId")
    LiveData<Integer> getCountByDayId(Long dayId);
}