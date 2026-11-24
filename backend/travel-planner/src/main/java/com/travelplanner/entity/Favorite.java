package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Favorite {
    private Long id;
    private Long userId;
    private String poiId;
    private String type;
    private String name;
    private String address;
    private String imageUrl;
    private String rating;
    private String price;
    private String phone;
    private String location;
    private String poiJson;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
