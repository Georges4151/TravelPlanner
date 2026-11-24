package com.travelplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.AttractionEntity;

import java.util.List;

@Dao
public interface AttractionDao {
    @Query("SELECT * FROM attractions WHERE id = :id")
    AttractionEntity getById(Long id);

    @Query("SELECT * FROM attractions WHERE city = :city ORDER BY rating DESC")
    List<AttractionEntity> getByCity(String city);

    @Query("SELECT * FROM attractions WHERE city = :city AND crowdLevel = :crowdLevel")
    List<AttractionEntity> getByCityAndCrowdLevel(String city, String crowdLevel);

    @Query("SELECT * FROM attractions WHERE name LIKE '%' || :keyword || '%' OR city LIKE '%' || :keyword || '%'")
    List<AttractionEntity> search(String keyword);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AttractionEntity attraction);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AttractionEntity> attractions);

    @Update
    void update(AttractionEntity attraction);

    @Delete
    void delete(AttractionEntity attraction);

    @Query("DELETE FROM attractions WHERE cacheTime < :expireTime")
    void deleteExpired(long expireTime);

    @Query("DELETE FROM attractions")
    void deleteAll();
}
