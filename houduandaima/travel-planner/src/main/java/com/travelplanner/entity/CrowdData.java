package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CrowdData {
    private Long id;
    private Long attractionId;
    private String date;
    private String hour;
    private String crowdLevel;
    private LocalDateTime updateTime;
}
