package com.travelplanner.mapper;

import com.travelplanner.entity.Itinerary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItineraryMapper {
    @Select("SELECT * FROM itinerary WHERE id = #{id}")
    Itinerary findById(Long id);

    @Select("SELECT * FROM itinerary WHERE id = #{id}")
    Itinerary selectById(Long id);

    @Select("SELECT * FROM itinerary WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Itinerary> findByUserId(Long userId);

    @Select("SELECT * FROM itinerary WHERE user_id = #{userId} AND status = #{status} ORDER BY create_time DESC")
    List<Itinerary> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    @Select("SELECT * FROM itinerary WHERE destination LIKE CONCAT('%', #{destination}, '%') ORDER BY create_time DESC")
    List<Itinerary> findByDestination(String destination);

    @Insert("INSERT INTO itinerary(user_id, title, destination, days, people_count, budget, travel_style, status, start_date, end_date, source, template_id, generate_status, generate_message, create_time) VALUES(#{userId}, #{title}, #{destination}, #{days}, #{peopleCount}, #{budget}, #{travelStyle}, #{status}, #{startDate}, #{endDate}, #{source}, #{templateId}, #{generateStatus}, #{generateMessage}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Itinerary itinerary);

    @Update("UPDATE itinerary SET title = #{title}, destination = #{destination}, days = #{days}, people_count = #{peopleCount}, budget = #{budget}, travel_style = #{travelStyle}, status = #{status}, start_date = #{startDate}, end_date = #{endDate}, generate_status = #{generateStatus}, generate_message = #{generateMessage}, update_time = NOW() WHERE id = #{id}")
    int update(Itinerary itinerary);

    @Update("UPDATE itinerary SET title = #{title}, destination = #{destination}, days = #{days}, people_count = #{peopleCount}, budget = #{budget}, travel_style = #{travelStyle}, status = #{status}, start_date = #{startDate}, end_date = #{endDate}, update_time = NOW() WHERE id = #{id}")
    int updateById(Itinerary itinerary);

    @Delete("DELETE FROM itinerary WHERE id = #{id}")
    int deleteById(Long id);
}
