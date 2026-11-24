package com.travelplanner.dto;

import lombok.Data;
import java.util.List;

@Data
public class DayPlan {
    private Integer dayNumber;
    private String date;
    private String weather;
    private String tips;
    private List<SpotPlan> spots;
    private Double totalDistance;
    private Integer totalDuration;
}
