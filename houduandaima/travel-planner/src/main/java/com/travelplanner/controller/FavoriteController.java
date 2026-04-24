package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.FavoriteRequest;
import com.travelplanner.dto.FavoriteResponse;
import com.travelplanner.entity.Favorite;
import com.travelplanner.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/check")
    public ApiResponse<Boolean> checkFavorite(@RequestParam Long userId, @RequestParam String poiId, @RequestParam String type) {
        try {
            boolean isFavorite = favoriteService.isFavorite(userId, poiId, type);
            return ApiResponse.success(isFavorite);
        } catch (Exception e) {
            return ApiResponse.error("检查收藏状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ApiResponse<FavoriteResponse> addFavorite(@RequestBody FavoriteRequest request) {
        try {
            if (request.getUserId() == null) {
                return ApiResponse.error(400, "用户ID不能为空");
            }
            Favorite favorite = favoriteService.addFavorite(
                    request.getUserId(),
                    request.getPoiId(),
                    request.getType(),
                    request.getName(),
                    request.getAddress(),
                    request.getImageUrl(),
                    request.getRating(),
                    request.getPrice(),
                    request.getPhone(),
                    request.getLocation(),
                    request.getPoiJson()
            );
            return ApiResponse.success(FavoriteResponse.fromEntity(favorite));
        } catch (Exception e) {
            return ApiResponse.error("添加收藏失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ApiResponse<Void> removeFavorite(@RequestParam Long userId, @RequestParam String poiId, @RequestParam String type) {
        try {
            boolean success = favoriteService.removeFavorite(userId, poiId, type);
            if (success) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error(404, "收藏不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error("取消收藏失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<FavoriteResponse>> getUserFavorites(@RequestParam Long userId) {
        try {
            List<Favorite> favorites = favoriteService.getUserFavorites(userId);
            List<FavoriteResponse> responses = favorites.stream()
                    .map(FavoriteResponse::fromEntity)
                    .collect(Collectors.toList());
            return ApiResponse.success(responses);
        } catch (Exception e) {
            return ApiResponse.error("获取收藏列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/{type}")
    public ApiResponse<List<FavoriteResponse>> getUserFavoritesByType(@RequestParam Long userId, @PathVariable String type) {
        try {
            List<Favorite> favorites = favoriteService.getUserFavoritesByType(userId, type);
            List<FavoriteResponse> responses = favorites.stream()
                    .map(FavoriteResponse::fromEntity)
                    .collect(Collectors.toList());
            return ApiResponse.success(responses);
        } catch (Exception e) {
            return ApiResponse.error("获取收藏列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/count")
    public ApiResponse<Integer> getFavoriteCount(@RequestParam Long userId) {
        try {
            int count = favoriteService.getFavoriteCount(userId);
            return ApiResponse.success(count);
        } catch (Exception e) {
            return ApiResponse.error("获取收藏数量失败: " + e.getMessage());
        }
    }
}
