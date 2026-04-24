package com.travelplanner.mapper;

import com.travelplanner.entity.Attraction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AttractionMapper {
    @Select("SELECT * FROM attraction WHERE id = #{id}")
    Attraction findById(Long id);

    @Select("SELECT * FROM attraction WHERE id = #{id}")
    Attraction selectById(Long id);

    @Select("SELECT * FROM attraction WHERE city = #{city} ORDER BY rating DESC")
    List<Attraction> findByCity(String city);

    @Select("SELECT * FROM attraction WHERE city = #{keyword} OR pinyin LIKE CONCAT(#{keyword}, '%') OR pinyin LIKE CONCAT('%', #{keyword}, '%') OR name LIKE CONCAT('%', #{keyword}, '%') ORDER BY rating DESC")
    List<Attraction> searchByCityOrPinyin(String keyword);

    @Select("SELECT * FROM attraction WHERE province = #{province} ORDER BY rating DESC")
    List<Attraction> findByProvince(String province);

    @Select("SELECT * FROM attraction ORDER BY rating DESC LIMIT #{limit}")
    List<Attraction> findTopRated(Integer limit);

    @Select("SELECT * FROM attraction WHERE crowd_level = #{crowdLevel}")
    List<Attraction> findByCrowdLevel(String crowdLevel);

    @Insert("INSERT INTO attraction(name, city, province, description, address, open_time, ticket_price, play_duration, crowd_level, image_url, rating, create_time) VALUES(#{name}, #{city}, #{province}, #{description}, #{address}, #{openTime}, #{ticketPrice}, #{playDuration}, #{crowdLevel}, #{imageUrl}, #{rating}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Attraction attraction);

    @Select("SELECT COUNT(*) FROM attraction")
    int countAttractions();
}
