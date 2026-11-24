package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Transport {
    private Long id;
    private String type;
    private String departure;
    private String arrival;
    private String departureTime;
    private String arrivalTime;
    private Integer duration;
    private String price;
    private String provider;
    private LocalDateTime createTime;
}
