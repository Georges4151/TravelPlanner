package com.travelplanner.dto;

import lombok.Data;

@Data
public class CrowdInfo {
    private Long attractionId;
    private String attractionName;
    private String currentLevel;
    private Integer currentIndex;
    private String forecast;
    private String bestVisitTime;
    private String avoidTime;

    public CrowdInfo() {}

    public CrowdInfo(Long attractionId, String attractionName, String currentLevel) {
        this.attractionId = attractionId;
        this.attractionName = attractionName;
        this.currentLevel = currentLevel;
    }
}
