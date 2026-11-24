package com.travelplanner.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Attraction {
    private Long id;
    private String name;
    private String pinyin;
    private String city;
    private String province;
    private String description;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String openTime;
    private BigDecimal ticketPrice;
    private String bookingMethod;
    private Integer playDuration;
    private String crowdLevel;
    private String trafficInfo;
    private String facilities;
    private String imageUrl;
    private Double rating;
    private Integer commentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
