package com.travelplanner.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AttractionDTO {
    private Long id;
    private String name;
    private String city;
    private String province;
    private String description;
    private String address;
    private String openTime;
    private BigDecimal ticketPrice;
    private Integer playDuration;
    private String crowdLevel;
    private String crowdIndex;
    private String bestVisitTime;
    private String imageUrl;
    private Double rating;
    private Integer commentCount;
}
