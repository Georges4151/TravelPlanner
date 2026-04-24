package com.travelplanner.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrowdForecastResponse {
    private Long attractionId;
    private String attractionName;
    private String coordinates;
    private List<DailyForecast> forecasts;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyForecast {
        private String date;
        private String dayOfWeek;
        private Boolean isHoliday;
        private String holidayName;
        private String weather;
        private String trafficStatus;
        private Double baseCrowd;
        private Double weightedCrowd;
        private String crowdLevel;
        private List<String> peakHours;
        private String bestTime;
        private String recommendation;
        private Factors factors;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Factors {
        private Double holiday;
        private Double weekend;
        private Double weather;
        private Double hot;
        private Double traffic;
    }
}