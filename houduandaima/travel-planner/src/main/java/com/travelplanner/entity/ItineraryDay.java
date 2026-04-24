package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItineraryDay {
    private Long id;
    private Long itineraryId;
    private Integer dayNumber;
    private String date;
    private String weather;
    private String tips;
    private LocalDateTime createTime;
}
