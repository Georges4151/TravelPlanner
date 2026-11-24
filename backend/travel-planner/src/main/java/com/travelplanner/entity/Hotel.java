package com.travelplanner.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Hotel {
    private Long id;
    private String name;
    private String city;
    private String address;
    private BigDecimal price;
    private String roomType;
    private Double rating;
    private String imageUrl;
    private String facilities;
    private LocalDateTime createTime;
}
