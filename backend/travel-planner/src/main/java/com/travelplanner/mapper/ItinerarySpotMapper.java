package com.travelplanner.mapper;

import com.travelplanner.entity.ItinerarySpot;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItinerarySpotMapper {
    @Select("SELECT * FROM itinerary_spot WHERE itinerary_day_id = #{itineraryDayId} ORDER BY order_num")
    List<ItinerarySpot> findByItineraryDayId(Long itineraryDayId);

    @Select("SELECT * FROM itinerary_spot WHERE itinerary_day_id = #{itineraryDayId} ORDER BY order_num")
    List<ItinerarySpot> selectByItineraryDayId(Long itineraryDayId);

    @Insert("INSERT INTO itinerary_spot(itinerary_day_id, attraction_id, attraction_name, order_num, arrival_time, leave_time, stay_duration, transport_to_next, notes, create_time) VALUES(#{itineraryDayId}, #{attractionId}, #{attractionName}, #{orderNum}, #{arrivalTime}, #{leaveTime}, #{stayDuration}, #{transportToNext}, #{notes}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ItinerarySpot itinerarySpot);

    @Update("UPDATE itinerary_spot SET order_num = #{orderNum}, arrival_time = #{arrivalTime}, leave_time = #{leaveTime}, stay_duration = #{stayDuration}, transport_to_next = #{transportToNext}, notes = #{notes} WHERE id = #{id}")
    int update(ItinerarySpot itinerarySpot);

    @Update("UPDATE itinerary_spot SET order_num = #{orderNum}, arrival_time = #{arrivalTime}, leave_time = #{leaveTime}, stay_duration = #{stayDuration}, transport_to_next = #{transportToNext}, notes = #{notes} WHERE id = #{id}")
    int updateById(ItinerarySpot itinerarySpot);

    @Delete("DELETE FROM itinerary_spot WHERE id = #{id}")
    int deleteById(Long id);

    @Delete("DELETE FROM itinerary_spot WHERE itinerary_day_id = #{itineraryDayId}")
    int deleteByItineraryDayId(Long itineraryDayId);
}
