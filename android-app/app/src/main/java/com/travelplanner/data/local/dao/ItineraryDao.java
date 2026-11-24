package com.travelplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.ItineraryEntity;

import java.util.List;

@Dao
public interface ItineraryDao {
    @Query("SELECT * FROM itineraries WHERE id = :id")
    ItineraryEntity getById(Long id);

    @Query("SELECT * FROM itineraries WHERE id = :id")
    ItineraryEntity getByIdSync(Long id);

    @Query("SELECT * FROM itineraries ORDER BY createTime DESC")
    List<ItineraryEntity> getAll();

    @Query("SELECT * FROM itineraries WHERE status = :status ORDER BY createTime DESC")
    List<ItineraryEntity> getByStatus(int status);

    @Query("SELECT * FROM itineraries WHERE destination LIKE '%' || :keyword || '%' OR title LIKE '%' || :keyword || '%'")
    List<ItineraryEntity> search(String keyword);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ItineraryEntity itinerary);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ItineraryEntity> itineraries);

    @Update
    void update(ItineraryEntity itinerary);

    @Delete
    void delete(ItineraryEntity itinerary);

    @Query("DELETE FROM itineraries WHERE id = :id")
    void deleteById(Long id);

    @Query("UPDATE itineraries SET status = :status WHERE id = :id")
    void updateStatus(Long id, int status);
}
