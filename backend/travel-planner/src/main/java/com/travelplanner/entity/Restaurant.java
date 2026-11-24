package com.travelplanner.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Restaurant {
    private Long id;
    private String name;
    private String city;
    private String address;
    private BigDecimal avgPrice;
    private Double rating;
    private String cuisineType;
    private String imageUrl;
    private String businessHours;
    private LocalDateTime createTime;
}
