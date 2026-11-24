package com.travelplanner.mapper;

import com.travelplanner.entity.Restaurant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RestaurantMapper {
    @Select("SELECT * FROM restaurant WHERE city = #{city} ORDER BY rating DESC")
    List<Restaurant> findByCity(String city);

    @Select("SELECT * FROM restaurant WHERE cuisine_type = #{cuisineType} ORDER BY rating DESC")
    List<Restaurant> findByCuisineType(String cuisineType);

    @Select("SELECT * FROM restaurant WHERE id = #{id}")
    Restaurant findById(Long id);

    @Insert("INSERT INTO restaurant(name, city, address, avg_price, rating, cuisine_type, image_url, business_hours, create_time) VALUES(#{name}, #{city}, #{address}, #{avgPrice}, #{rating}, #{cuisineType}, #{imageUrl}, #{businessHours}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Restaurant restaurant);
}
