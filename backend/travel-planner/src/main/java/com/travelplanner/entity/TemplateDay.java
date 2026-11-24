package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TemplateDay {
    private Long id;
    private Long templateId;
    private Integer dayNumber;
    private String date;
    private String weather;
    private String tips;
    private LocalDateTime createTime;
}