package com.travelplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.HotelEntity;

import java.util.List;

@Dao
public interface HotelDao {
    @Query("SELECT * FROM hotels WHERE id = :id")
    HotelEntity getById(Long id);

    @Query("SELECT * FROM hotels WHERE city = :city ORDER BY rating DESC")
    List<HotelEntity> getByCity(String city);

    @Query("SELECT * FROM hotels WHERE city = :city AND price BETWEEN :minPrice AND :maxPrice ORDER BY rating DESC")
    List<HotelEntity> getByPriceRange(String city, double minPrice, double maxPrice);

    @Query("SELECT * FROM hotels WHERE city = :city ORDER BY distanceToCenter ASC LIMIT :limit")
    List<HotelEntity> getNearbyCenter(String city, int limit);

    @Query("SELECT * FROM hotels WHERE name LIKE '%' || :keyword || '%' OR city LIKE '%' || :keyword || '%'")
    List<HotelEntity> search(String keyword);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HotelEntity hotel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<HotelEntity> hotels);

    @Update
    void update(HotelEntity hotel);

    @Delete
    void delete(HotelEntity hotel);

    @Query("DELETE FROM hotels WHERE cacheTime < :expireTime")
    void deleteExpired(long expireTime);

    @Query("DELETE FROM hotels")
    void deleteAll();
}
