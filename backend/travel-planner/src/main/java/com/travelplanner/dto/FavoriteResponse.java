package com.travelplanner.dto;

import com.travelplanner.entity.Favorite;
import lombok.Data;

@Data
public class FavoriteResponse {
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
    private String createTime;

    public static FavoriteResponse fromEntity(Favorite favorite) {
        FavoriteResponse response = new FavoriteResponse();
        response.setId(favorite.getId());
        response.setUserId(favorite.getUserId());
        response.setPoiId(favorite.getPoiId());
        response.setType(favorite.getType());
        response.setName(favorite.getName());
        response.setAddress(favorite.getAddress());
        response.setImageUrl(favorite.getImageUrl());
        response.setRating(favorite.getRating());
        response.setPrice(favorite.getPrice());
        response.setPhone(favorite.getPhone());
        response.setLocation(favorite.getLocation());
        response.setPoiJson(favorite.getPoiJson());
        if (favorite.getCreateTime() != null) {
            response.setCreateTime(favorite.getCreateTime().toString());
        }
        return response;
    }
}
