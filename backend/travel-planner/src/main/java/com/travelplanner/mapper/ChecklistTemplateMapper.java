package com.travelplanner.mapper;

import com.travelplanner.entity.ChecklistTemplate;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ChecklistTemplateMapper {
    
    @Select("SELECT * FROM checklist_template WHERE is_system = true ORDER BY name")
    List<ChecklistTemplate> findSystemTemplates();
    
    @Select("SELECT * FROM checklist_template WHERE user_id = #{userId} ORDER BY is_default DESC, name")
    List<ChecklistTemplate> findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM checklist_template WHERE id = #{id}")
    ChecklistTemplate findById(@Param("id") Long id);
    
    @Select("SELECT * FROM checklist_template WHERE user_id = #{userId} AND is_default = true LIMIT 1")
    ChecklistTemplate findDefaultByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM checklist_template WHERE is_system = true OR user_id = #{userId} ORDER BY is_system DESC, name")
    List<ChecklistTemplate> findAvailableTemplates(@Param("userId") Long userId);
    
    @Insert("INSERT INTO checklist_template(user_id, name, description, items, is_system, is_default) " +
            "VALUES(#{userId}, #{name}, #{description}, #{items}, #{isSystem}, #{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChecklistTemplate template);
    
    @Update("UPDATE checklist_template SET name=#{name}, description=#{description}, " +
            "items=#{items}, is_default=#{isDefault} WHERE id=#{id}")
    int update(ChecklistTemplate template);
    
    @Update("UPDATE checklist_template SET usage_count = usage_count + 1 WHERE id = #{id}")
    int incrementUsageCount(@Param("id") Long id);
    
    @Update("UPDATE checklist_template SET is_default = false WHERE user_id = #{userId}")
    int clearDefaultFlag(@Param("userId") Long userId);
    
    @Delete("DELETE FROM checklist_template WHERE id = #{id} AND is_system = false")
    int delete(@Param("id") Long id);
}
