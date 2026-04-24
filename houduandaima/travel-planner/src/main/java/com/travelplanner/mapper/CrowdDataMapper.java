package com.travelplanner.mapper;

import com.travelplanner.entity.CrowdData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CrowdDataMapper {
    @Select("SELECT * FROM crowd_data WHERE attraction_id = #{attractionId} AND date >= CURDATE() ORDER BY date, hour")
    List<CrowdData> findFutureByAttractionId(Long attractionId);

    @Select("SELECT * FROM crowd_data WHERE attraction_id = #{attractionId} AND date = #{date}")
    List<CrowdData> findByAttractionIdAndDate(@Param("attractionId") Long attractionId, @Param("date") String date);

    @Insert("INSERT INTO crowd_data(attraction_id, date, hour, crowd_level, update_time) VALUES(#{attractionId}, #{date}, #{hour}, #{crowdLevel}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CrowdData crowdData);
}
