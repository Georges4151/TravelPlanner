package com.travelplanner.mapper;

import com.travelplanner.entity.Hotel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HotelMapper {
    @Select("SELECT * FROM hotel WHERE city = #{city} ORDER BY rating DESC")
    List<Hotel> findByCity(String city);

    @Select("SELECT * FROM hotel WHERE id = #{id}")
    Hotel findById(Long id);

    @Insert("INSERT INTO hotel(name, city, address, price, room_type, rating, image_url, facilities, create_time) VALUES(#{name}, #{city}, #{address}, #{price}, #{roomType}, #{rating}, #{imageUrl}, #{facilities}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Hotel hotel);
}
