package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.WeatherInfo;
import com.travelplanner.dto.CrowdInfo;
import com.travelplanner.dto.HolidayInfo;
import com.travelplanner.dto.DistanceInfo;
import com.travelplanner.dto.AttractionSearchResult;
import com.travelplanner.dto.CrowdForecastResponse;
import com.travelplanner.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/external")
public class ExternalApiController {

    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping("/weather")
    public ApiResponse<WeatherInfo> getWeather(
            @RequestParam String city,
            @RequestParam(required = false) String date) {
        try {
            WeatherInfo weather = externalApiService.getWeather(city, date);
            return ApiResponse.success(weather);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取天气信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/weather/forecast")
    public ApiResponse<List<WeatherInfo>> getWeatherForecast(
            @RequestParam String city,
            @RequestParam(defaultValue = "3") int days) {
        try {
            List<WeatherInfo> forecast = externalApiService.getWeatherForecast(city, days);
            return ApiResponse.success(forecast);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取天气预报失败: " + e.getMessage());
        }
    }

    @GetMapping("/crowd/{attractionId}")
    public ApiResponse<CrowdInfo> getCrowdInfo(@PathVariable Long attractionId) {
        try {
            CrowdInfo crowdInfo = externalApiService.getCrowdInfo(attractionId);
            return ApiResponse.success(crowdInfo);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取客流信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/crowd/city/{city}")
    public ApiResponse<List<CrowdInfo>> getCityCrowdInfo(@PathVariable String city) {
        try {
            List<CrowdInfo> crowdList = externalApiService.getCityCrowdInfo(city);
            return ApiResponse.success(crowdList);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取城市客流信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/holiday")
    public ApiResponse<HolidayInfo> getHolidayInfo(@RequestParam String date) {
        try {
            HolidayInfo holidayInfo = externalApiService.getHolidayInfo(date);
            return ApiResponse.success(holidayInfo);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取节假日信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/distance")
    public ApiResponse<DistanceInfo> getDistance(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(defaultValue = "driving") String mode) {
        try {
            DistanceInfo distanceInfo = externalApiService.getDistance(origin, destination, mode);
            return ApiResponse.success(distanceInfo);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取距离信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/attraction/search")
    public ApiResponse<List<AttractionSearchResult>> searchAttractions(
            @RequestParam String city,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type) {
        try {
            List<AttractionSearchResult> results = externalApiService.searchAttractions(city, keyword, type);
            return ApiResponse.success(results);
        } catch (Exception e) {
            return ApiResponse.error(500, "搜索景点失败: " + e.getMessage());
        }
    }

    @GetMapping("/crowd/forecast/{attractionId}")
    public ApiResponse<CrowdForecastResponse> getCrowdForecast(@PathVariable Long attractionId) {
        try {
            CrowdForecastResponse forecast = externalApiService.getCrowdForecast(attractionId);
            return ApiResponse.success(forecast);// 第107行：用ApiResponse包装后返回JSON
        } catch (Exception e) {
            return ApiResponse.error(500, "获取客流预测失败: " + e.getMessage());
        }
    }

    @GetMapping("/crowd/forecast/name")
    public ApiResponse<CrowdForecastResponse> getCrowdForecastByName(
            @RequestParam String attractionName,
            @RequestParam(required = false) String city) {
        try {
            CrowdForecastResponse forecast = externalApiService.getCrowdForecastByName(attractionName, city);
            if (forecast == null) {
                return ApiResponse.error(404, "未找到该景点: " + attractionName);
            }
            return ApiResponse.success(forecast);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取客流预测失败: " + e.getMessage());
        }
    }
}
