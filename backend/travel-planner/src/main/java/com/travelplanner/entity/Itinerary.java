package com.travelplanner.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Itinerary {
    private Long id;
    private Long userId;
    private String title;
    private String destination;
    private Integer days;
    private Integer peopleCount;
    private BigDecimal budget;
    private String travelStyle;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String source;
    private Long templateId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String generateStatus;
    private String generateMessage;
}
