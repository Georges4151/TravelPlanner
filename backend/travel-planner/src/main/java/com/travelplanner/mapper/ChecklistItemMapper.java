package com.travelplanner.mapper;

import com.travelplanner.entity.ChecklistItem;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ChecklistItemMapper {
    
    @Select("SELECT * FROM checklist_item ORDER BY category, name")
    List<ChecklistItem> findAll();
    
    @Select("SELECT * FROM checklist_item WHERE category = #{category} ORDER BY name")
    List<ChecklistItem> findByCategory(@Param("category") String category);
    
    @Select("SELECT * FROM checklist_item WHERE id = #{id}")
    ChecklistItem findById(@Param("id") Long id);
    
    @Select("SELECT DISTINCT category FROM checklist_item ORDER BY category")
    List<String> findAllCategories();
    
    @Select("<script>" +
            "SELECT * FROM checklist_item WHERE " +
            "(season = 'all' OR season LIKE CONCAT('%', #{season}, '%')) " +
            "AND (destination_type = 'all' OR destination_type = #{destinationType} OR destination_type LIKE CONCAT('%', #{destinationType}, '%')) " +
            "ORDER BY is_essential DESC, category, name" +
            "</script>")
    List<ChecklistItem> findBySeasonAndDestinationType(@Param("season") String season, 
                                                        @Param("destinationType") String destinationType);
    
    @Insert("INSERT INTO checklist_item(name, category, sub_category, icon, is_essential, season, destination_type, min_days, max_days) " +
            "VALUES(#{name}, #{category}, #{subCategory}, #{icon}, #{isEssential}, #{season}, #{destinationType}, #{minDays}, #{maxDays})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChecklistItem item);
    
    @Update("UPDATE checklist_item SET name=#{name}, category=#{category}, sub_category=#{subCategory}, " +
            "icon=#{icon}, is_essential=#{isEssential}, season=#{season}, destination_type=#{destinationType}, " +
            "min_days=#{minDays}, max_days=#{maxDays} WHERE id=#{id}")
    int update(ChecklistItem item);
    
    @Delete("DELETE FROM checklist_item WHERE id = #{id}")
    int delete(@Param("id") Long id);
}
