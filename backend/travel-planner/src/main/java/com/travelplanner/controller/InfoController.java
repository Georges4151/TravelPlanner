package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.entity.Hotel;
import com.travelplanner.entity.Restaurant;
import com.travelplanner.entity.Transport;
import com.travelplanner.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    @Autowired
    private InfoService infoService;

    @GetMapping("/hotel/{city}")
    public ApiResponse<List<Hotel>> getHotels(@PathVariable String city) {
        List<Hotel> hotels = infoService.getHotelsByCity(city);
        if (hotels == null || hotels.isEmpty()) {
            hotels = infoService.getMockHotels(city);
        }
        return ApiResponse.success(hotels);
    }

    @GetMapping("/restaurant/{city}")
    public ApiResponse<List<Restaurant>> getRestaurants(@PathVariable String city) {
        List<Restaurant> restaurants = infoService.getRestaurantsByCity(city);
        if (restaurants == null || restaurants.isEmpty()) {
            restaurants = infoService.getMockRestaurants(city);
        }
        return ApiResponse.success(restaurants);
    }

    @GetMapping("/restaurant/cuisine/{cuisineType}")
    public ApiResponse<List<Restaurant>> getRestaurantsByCuisine(@PathVariable String cuisineType) {
        return ApiResponse.success(infoService.getRestaurantsByCuisine(cuisineType));
    }

    @GetMapping("/transport")
    public ApiResponse<List<Transport>> getTransports(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type) {
        if (city != null) {
            return ApiResponse.success(infoService.getTransportsByCity(city));
        } else if (type != null) {
            return ApiResponse.success(infoService.getTransportsByType(type));
        }
        return ApiResponse.success(List.of());
    }

    @GetMapping("/transport/search")
    public ApiResponse<List<Transport>> searchTransports(
            @RequestParam String departure,
            @RequestParam String arrival) {
        List<Transport> transports = infoService.getMockTransports(departure, arrival);
        return ApiResponse.success(transports);
    }
}
