package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItinerarySpot {
    private Long id;
    private Long itineraryDayId;
    private Long attractionId;
    private String attractionName;
    private Integer orderNum;
    private String arrivalTime;
    private String leaveTime;
    private Integer stayDuration;
    private String transportToNext;
    private String notes;
    private LocalDateTime createTime;
}
