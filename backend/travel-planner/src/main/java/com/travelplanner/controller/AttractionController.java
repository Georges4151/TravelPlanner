package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.AttractionDTO;
import com.travelplanner.dto.AttractionSearchResult;
import com.travelplanner.service.AttractionService;
import com.travelplanner.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attraction")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping(value = "/city/{city}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<List<AttractionDTO>> getAttractionsByCity(@PathVariable String city) {
        return ApiResponse.success(attractionService.getAttractionsByCity(city));
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<List<AttractionSearchResult>> searchAttractions(
            @RequestParam String keyword,
            @RequestParam(required = false) String city) {
        System.out.println("搜索景点 - 关键词: " + keyword + ", 城市: " + city);
        List<AttractionSearchResult> results = externalApiService.searchAttractions(city, keyword, null);
        System.out.println("搜索结果数量: " + results.size());
        return ApiResponse.success(results);
    }

    @GetMapping(value = "/top", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<List<AttractionDTO>> getTopAttractions(@RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.success(attractionService.getTopAttractions(limit));
    }

    @GetMapping(value = "/province/{province}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<List<AttractionDTO>> getAttractionsByProvince(@PathVariable String province) {
        return ApiResponse.success(attractionService.getAttractionsByProvince(province));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<AttractionDTO> getAttractionById(@PathVariable Long id) {
        return ApiResponse.success(attractionService.getAttractionById(id));
    }

    @GetMapping(value = "/{id}/crowd", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<List<String>> getCrowdForecast(
            @PathVariable Long id,
            @RequestParam(defaultValue = "3") Integer days) {
        return ApiResponse.success(attractionService.getCrowdForecast(id, days));
    }
}
