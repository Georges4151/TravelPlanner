package com.travelplanner.dto;

import lombok.Data;

@Data
public class DistanceInfo {
    private String origin;
    private String destination;
    private Double distance;
    private Integer duration;
    private String route;
    private String transportMode;
}
