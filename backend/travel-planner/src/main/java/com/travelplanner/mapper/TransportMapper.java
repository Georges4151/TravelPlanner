package com.travelplanner.mapper;

import com.travelplanner.entity.Transport;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TransportMapper {
    @Select("SELECT * FROM transport WHERE departure LIKE CONCAT('%', #{city}, '%') OR arrival LIKE CONCAT('%', #{city}, '%') ORDER BY departure_time")
    List<Transport> findByCity(String city);

    @Select("SELECT * FROM transport WHERE type = #{type} ORDER BY departure_time")
    List<Transport> findByType(String type);

    @Select("SELECT * FROM transport WHERE id = #{id}")
    Transport findById(Long id);

    @Insert("INSERT INTO transport(type, departure, arrival, departure_time, arrival_time, duration, price, provider, create_time) VALUES(#{type}, #{departure}, #{arrival}, #{departureTime}, #{arrivalTime}, #{duration}, #{price}, #{provider}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Transport transport);
}
