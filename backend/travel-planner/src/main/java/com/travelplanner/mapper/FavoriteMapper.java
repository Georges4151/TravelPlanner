package com.travelplanner.mapper;

import com.travelplanner.entity.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    @Select("SELECT * FROM favorite WHERE id = #{id}")
    Favorite findById(Long id);

    @Select("SELECT * FROM favorite WHERE user_id = #{userId} AND poi_id = #{poiId} AND type = #{type}")
    Favorite findByUserIdAndPoiIdAndType(@Param("userId") Long userId, @Param("poiId") String poiId, @Param("type") String type);

    @Select("SELECT * FROM favorite WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Favorite> findByUserId(Long userId);

    @Select("SELECT * FROM favorite WHERE user_id = #{userId} AND type = #{type} ORDER BY create_time DESC")
    List<Favorite> findByUserIdAndType(@Param("userId") Long userId, @Param("type") String type);

    @Select("SELECT EXISTS(SELECT 1 FROM favorite WHERE user_id = #{userId} AND poi_id = #{poiId} AND type = #{type})")
    boolean existsByUserIdAndPoiIdAndType(@Param("userId") Long userId, @Param("poiId") String poiId, @Param("type") String type);

    @Insert("INSERT INTO favorite(user_id, poi_id, type, name, address, image_url, rating, price, phone, location, poi_json, create_time) " +
            "VALUES(#{userId}, #{poiId}, #{type}, #{name}, #{address}, #{imageUrl}, #{rating}, #{price}, #{phone}, #{location}, #{poiJson}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Favorite favorite);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND poi_id = #{poiId} AND type = #{type}")
    int deleteByUserIdAndPoiIdAndType(@Param("userId") Long userId, @Param("poiId") String poiId, @Param("type") String type);

    @Delete("DELETE FROM favorite WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(*) FROM favorite WHERE user_id = #{userId}")
    int countByUserId(Long userId);
}
