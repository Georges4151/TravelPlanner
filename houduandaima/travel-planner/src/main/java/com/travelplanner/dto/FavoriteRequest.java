package com.travelplanner.dto;

import lombok.Data;

@Data
public class FavoriteRequest {
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
}
