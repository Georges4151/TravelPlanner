package com.travelplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.FavoriteEntity;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE id = :id")
    FavoriteEntity getById(Long id);

    @Query("SELECT * FROM favorites WHERE poiId = :poiId AND type = :type")
    FavoriteEntity getByPoiId(String poiId, String type);

    @Query("SELECT * FROM favorites ORDER BY createdAt DESC")
    List<FavoriteEntity> getAll();

    @Query("SELECT * FROM favorites WHERE type = :type ORDER BY createdAt DESC")
    List<FavoriteEntity> getByType(String type);

    @Query("SELECT * FROM favorites WHERE name LIKE '%' || :keyword || '%' OR address LIKE '%' || :keyword || '%'")
    List<FavoriteEntity> search(String keyword);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FavoriteEntity favorite);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FavoriteEntity> favorites);

    @Update
    void update(FavoriteEntity favorite);

    @Delete
    void delete(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE poiId = :poiId AND type = :type")
    void deleteByPoiId(String poiId, String type);

    @Query("DELETE FROM favorites")
    void deleteAll();

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE poiId = :poiId AND type = :type)")
    boolean isFavorite(String poiId, String type);
}
