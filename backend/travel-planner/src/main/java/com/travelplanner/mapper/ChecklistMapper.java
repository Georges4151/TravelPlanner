package com.travelplanner.mapper;

import com.travelplanner.entity.Checklist;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChecklistMapper {
    @Select("SELECT * FROM checklist WHERE itinerary_id = #{itineraryId} ORDER BY category, create_time")
    List<Checklist> findByItineraryId(Long itineraryId);

    @Insert("INSERT INTO checklist(itinerary_id, item_name, category, is_checked, notes, create_time) VALUES(#{itineraryId}, #{itemName}, #{category}, #{isChecked}, #{notes}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Checklist checklist);

    @Update("UPDATE checklist SET is_checked = #{isChecked} WHERE id = #{id}")
    int updateChecked(@Param("id") Long id, @Param("isChecked") Boolean isChecked);

    @Delete("DELETE FROM checklist WHERE id = #{id}")
    int deleteById(Long id);

    @Delete("DELETE FROM checklist WHERE itinerary_id = #{itineraryId}")
    int deleteByItineraryId(Long itineraryId);
}
