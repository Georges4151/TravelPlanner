package com.travelplanner.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OffpeakRecommendationResponse {
    private OriginalDates originalDates;
    private List<OffpeakRecommendation> offpeakRecommendations;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OriginalDates {
        private String startDate;
        private String endDate;
        private Boolean isHoliday;
        private String holidayName;
        private Boolean isWorkday;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OffpeakRecommendation {
        private String startDate;
        private String endDate;
        private String reason;
        private String crowdLevel;
        private List<AlternativeSpot> alternativeSpots;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlternativeSpot {
        private String original;
        private String alternative;
        private String reason;
    }
}