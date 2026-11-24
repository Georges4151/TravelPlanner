package com.travelplanner.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.ItineraryDayEntity;

import java.util.List;

@Dao
public interface ItineraryDayDao {
    @Insert
    long insert(ItineraryDayEntity itineraryDay);

    @Update
    void update(ItineraryDayEntity itineraryDay);

    @Delete
    void delete(ItineraryDayEntity itineraryDay);

    @Query("SELECT * FROM itinerary_days WHERE id = :id")
    LiveData<ItineraryDayEntity> getById(Long id);

    @Query("SELECT * FROM itinerary_days WHERE id = :id")
    ItineraryDayEntity getByIdSync(Long id);

    @Query("SELECT * FROM itinerary_days WHERE itineraryId = :itineraryId ORDER BY dayNumber ASC")
    LiveData<List<ItineraryDayEntity>> getByItineraryId(Long itineraryId);

    @Query("SELECT * FROM itinerary_days WHERE itineraryId = :itineraryId ORDER BY dayNumber ASC")
    List<ItineraryDayEntity> getByItineraryIdSync(Long itineraryId);

    @Query("SELECT * FROM itinerary_days WHERE itineraryId = :itineraryId AND dayNumber = :dayNumber")
    LiveData<ItineraryDayEntity> getByItineraryIdAndDayNumber(Long itineraryId, Integer dayNumber);

    @Query("DELETE FROM itinerary_days WHERE itineraryId = :itineraryId")
    void deleteByItineraryId(Long itineraryId);

    @Query("SELECT COUNT(*) FROM itinerary_days WHERE itineraryId = :itineraryId")
    LiveData<Integer> getCountByItineraryId(Long itineraryId);
}