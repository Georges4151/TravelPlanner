package com.travelplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.RestaurantEntity;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Query("SELECT * FROM restaurants WHERE id = :id")
    RestaurantEntity getById(Long id);

    @Query("SELECT * FROM restaurants WHERE city = :city ORDER BY rating DESC")
    List<RestaurantEntity> getByCity(String city);

    @Query("SELECT * FROM restaurants WHERE city = :city AND cuisineType = :cuisineType ORDER BY rating DESC")
    List<RestaurantEntity> getByCityAndCuisine(String city, String cuisineType);

    @Query("SELECT * FROM restaurants WHERE city = :city ORDER BY distance ASC LIMIT :limit")
    List<RestaurantEntity> getNearby(String city, int limit);

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :keyword || '%' OR city LIKE '%' || :keyword || '%'")
    List<RestaurantEntity> search(String keyword);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RestaurantEntity restaurant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RestaurantEntity> restaurants);

    @Update
    void update(RestaurantEntity restaurant);

    @Delete
    void delete(RestaurantEntity restaurant);

    @Query("DELETE FROM restaurants WHERE cacheTime < :expireTime")
    void deleteExpired(long expireTime);

    @Query("DELETE FROM restaurants")
    void deleteAll();
}
