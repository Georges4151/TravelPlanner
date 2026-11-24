package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TemplateSpot {
    private Long id;
    private Long templateDayId;
    private String attractionName;
    private Integer orderNum;
    private String arrivalTime;
    private String leaveTime;
    private Integer stayDuration;
    private String transportToNext;
    private String notes;
    private LocalDateTime createTime;
}