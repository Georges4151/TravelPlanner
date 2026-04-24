package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChecklistItem {
    private Long id;
    private String name;
    private String category;
    private String subCategory;
    private String icon;
    private Boolean isEssential;
    private String season;
    private String destinationType;
    private Integer minDays;
    private Integer maxDays;
    private LocalDateTime createTime;
}
