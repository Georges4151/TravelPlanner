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
public class ItineraryAdjustRequest {
    private List<Long> removeSpots;
    private List<String> removeSpotNames;
    private List<AttractionToAdd> addSpots;
    private Map<String, Map<String, SpotAdjustment>> adjustments;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttractionToAdd {
        private Long attractionId;
        private String attractionName;
        private Integer dayNumber;
        private Integer orderNum;
        private String arrivalTime;
        private String leaveTime;
        private Integer stayDuration;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpotAdjustment {
        private String arrivalTime;
        private String leaveTime;
        private Integer stayDuration;
        private Integer orderNum;
    }
}