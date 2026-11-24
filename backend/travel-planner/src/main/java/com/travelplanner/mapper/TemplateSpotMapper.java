package com.travelplanner.mapper;

import com.travelplanner.entity.TemplateSpot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TemplateSpotMapper {
    List<TemplateSpot> findByTemplateDayId(@Param("templateDayId") Long templateDayId);
    void insert(TemplateSpot templateSpot);
}