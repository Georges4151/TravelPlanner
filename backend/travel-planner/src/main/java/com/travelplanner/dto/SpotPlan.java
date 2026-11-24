package com.travelplanner.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SpotPlan {
    private Long attractionId;
    private String attractionName;
    private String name;
    private String type;
    private String visitTime;
    private String arrivalTime;
    private String leaveTime;
    private Integer stayDuration;
    private Integer duration;
    private String transportMode;
    private String transportToNext;
    private BigDecimal ticketPrice;
    private String crowdLevel;
    private String bestVisitTime;
    private String notes;
}
