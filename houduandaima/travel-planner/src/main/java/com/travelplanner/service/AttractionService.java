package com.travelplanner.service;

import com.travelplanner.dto.AttractionDTO;
import com.travelplanner.entity.Attraction;
import com.travelplanner.entity.CrowdData;
import com.travelplanner.mapper.AttractionMapper;
import com.travelplanner.mapper.CrowdDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    @Autowired
    private AttractionMapper attractionMapper;

    @Autowired
    private CrowdDataMapper crowdDataMapper;

    public List<AttractionDTO> getAttractionsByCity(String city) {
        List<Attraction> attractions = attractionMapper.searchByCityOrPinyin(city);
        return attractions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<AttractionDTO> searchAttractions(String keyword) {
        List<Attraction> attractions = attractionMapper.searchByCityOrPinyin(keyword);
        return attractions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<AttractionDTO> getTopAttractions(Integer limit) {
        List<Attraction> attractions = attractionMapper.findTopRated(limit != null ? limit : 10);
        return attractions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<AttractionDTO> getAttractionsByProvince(String province) {
        List<Attraction> attractions = attractionMapper.findByProvince(province);
        return attractions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AttractionDTO getAttractionById(Long id) {
        Attraction attraction = attractionMapper.findById(id);
        return attraction != null ? convertToDTO(attraction) : null;
    }

    public List<String> getCrowdForecast(Long attractionId, Integer days) {
        List<CrowdData> crowdData = crowdDataMapper.findFutureByAttractionId(attractionId);
        return crowdData.stream()
                .map(c -> c.getDate() + " " + c.getHour() + ": " + c.getCrowdLevel())
                .limit(days > 0 ? days : 3)
                .collect(Collectors.toList());
    }

    private AttractionDTO convertToDTO(Attraction attraction) {
        AttractionDTO dto = new AttractionDTO();
        dto.setId(attraction.getId());
        dto.setName(attraction.getName());
        dto.setCity(attraction.getCity());
        dto.setProvince(attraction.getProvince());
        dto.setDescription(attraction.getDescription());
        dto.setAddress(attraction.getAddress());
        dto.setOpenTime(attraction.getOpenTime());
        dto.setTicketPrice(attraction.getTicketPrice());
        dto.setPlayDuration(attraction.getPlayDuration());
        dto.setCrowdLevel(attraction.getCrowdLevel());
        dto.setImageUrl(attraction.getImageUrl());
        dto.setRating(attraction.getRating());
        dto.setCommentCount(attraction.getCommentCount());
        return dto;
    }
}
