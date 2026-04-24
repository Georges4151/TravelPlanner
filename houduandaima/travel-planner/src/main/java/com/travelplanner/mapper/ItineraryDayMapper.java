package com.travelplanner.mapper;

import com.travelplanner.entity.ItineraryDay;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItineraryDayMapper {
    @Select("SELECT * FROM itinerary_day WHERE itinerary_id = #{itineraryId} ORDER BY day_number")
    List<ItineraryDay> findByItineraryId(Long itineraryId);

    @Select("SELECT * FROM itinerary_day WHERE itinerary_id = #{itineraryId} ORDER BY day_number")
    List<ItineraryDay> selectByItineraryId(Long itineraryId);

    @Select("SELECT * FROM itinerary_day WHERE id = #{id}")
    ItineraryDay findById(Long id);

    @Insert("INSERT INTO itinerary_day(itinerary_id, day_number, date, weather, tips, create_time) VALUES(#{itineraryId}, #{dayNumber}, #{date}, #{weather}, #{tips}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ItineraryDay itineraryDay);

    @Update("UPDATE itinerary_day SET date = #{date}, weather = #{weather}, tips = #{tips} WHERE id = #{id}")
    int update(ItineraryDay itineraryDay);

    @Delete("DELETE FROM itinerary_day WHERE itinerary_id = #{itineraryId}")
    int deleteByItineraryId(Long itineraryId);
}
