package com.travelplanner.mapper;

import com.travelplanner.entity.TemplateDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TemplateDayMapper {
    List<TemplateDay> findByTemplateId(@Param("templateId") Long templateId);
    TemplateDay findById(@Param("id") Long id);
    void insert(TemplateDay templateDay);
}