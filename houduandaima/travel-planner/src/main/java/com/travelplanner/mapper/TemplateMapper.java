package com.travelplanner.mapper;

import com.travelplanner.entity.Template;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TemplateMapper {
    @Select("SELECT * FROM template ORDER BY usage_count DESC LIMIT #{limit}")
    List<Template> findHotTemplates(Integer limit);

    @Select("SELECT * FROM template WHERE destination = #{destination} ORDER BY usage_count DESC")
    List<Template> findByDestination(String destination);

    @Select("SELECT * FROM template WHERE is_official = 1 ORDER BY usage_count DESC")
    List<Template> findOfficialTemplates();

    @Select("SELECT * FROM template WHERE style = #{style} ORDER BY usage_count DESC")
    List<Template> findByStyle(String style);

    @Select("SELECT * FROM template WHERE id = #{id}")
    Template findById(Long id);

    @Insert("INSERT INTO template(title, destination, days, style, crowd, description, cover_image, user_id, author_name, usage_count, budget, is_official, is_hot, create_time) VALUES(#{title}, #{destination}, #{days}, #{style}, #{crowd}, #{description}, #{coverImage}, #{userId}, #{authorName}, #{usageCount}, #{budget}, #{isOfficial}, #{isHot}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Template template);

    @Update("UPDATE template SET usage_count = usage_count + 1 WHERE id = #{id}")
    int incrementUsageCount(Long id);

    @Select("SELECT COUNT(*) FROM template")
    int countTemplates();
}
