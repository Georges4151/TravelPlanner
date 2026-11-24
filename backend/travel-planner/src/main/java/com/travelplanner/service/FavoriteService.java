package com.travelplanner.service;

import com.travelplanner.entity.Favorite;
import com.travelplanner.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    public boolean isFavorite(Long userId, String poiId, String type) {
        return favoriteMapper.existsByUserIdAndPoiIdAndType(userId, poiId, type);
    }

    public Favorite addFavorite(Long userId, String poiId, String type, String name, String address,
                                String imageUrl, String rating, String price, String phone,
                                String location, String poiJson) {
        // 检查是否已收藏
        if (favoriteMapper.existsByUserIdAndPoiIdAndType(userId, poiId, type)) {
            return favoriteMapper.findByUserIdAndPoiIdAndType(userId, poiId, type);
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setPoiId(poiId);
        favorite.setType(type);
        favorite.setName(name);
        favorite.setAddress(address);
        favorite.setImageUrl(imageUrl);
        favorite.setRating(rating);
        favorite.setPrice(price);
        favorite.setPhone(phone);
        favorite.setLocation(location);
        favorite.setPoiJson(poiJson);

        favoriteMapper.insert(favorite);
        return favorite;
    }

    public boolean removeFavorite(Long userId, String poiId, String type) {
        return favoriteMapper.deleteByUserIdAndPoiIdAndType(userId, poiId, type) > 0;
    }

    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteMapper.findByUserId(userId);
    }

    public List<Favorite> getUserFavoritesByType(Long userId, String type) {
        return favoriteMapper.findByUserIdAndType(userId, type);
    }

    public int getFavoriteCount(Long userId) {
        return favoriteMapper.countByUserId(userId);
    }
}
