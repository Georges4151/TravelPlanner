package com.travelplanner.mapper;

import com.travelplanner.entity.Reminder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReminderMapper {
    @Select("SELECT * FROM reminder WHERE id = #{id}")
    Reminder findById(Long id);

    @Select("SELECT * FROM reminder WHERE itinerary_id = #{itineraryId} ORDER BY remind_time ASC")
    List<Reminder> findByItineraryId(Long itineraryId);

    @Select("SELECT * FROM reminder WHERE remind_time <= NOW() + INTERVAL #{hours} HOUR AND status = 0 ORDER BY remind_time ASC")
    List<Reminder> findUpcoming(int hours);

    @Insert("INSERT INTO reminder(itinerary_id, title, content, remind_time, type, status, create_time) VALUES(#{itineraryId}, #{title}, #{content}, #{remindTime}, #{type}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reminder reminder);

    @Update("UPDATE reminder SET title = #{title}, content = #{content}, remind_time = #{remindTime}, type = #{type}, status = #{status}, update_time = NOW() WHERE id = #{id}")
    int update(Reminder reminder);

    @Update("UPDATE reminder SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Delete("DELETE FROM reminder WHERE id = #{id}")
    int deleteById(Long id);

    @Delete("DELETE FROM reminder WHERE itinerary_id = #{itineraryId}")
    int deleteByItineraryId(Long itineraryId);
}
