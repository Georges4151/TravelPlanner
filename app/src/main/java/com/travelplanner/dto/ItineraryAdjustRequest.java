package com.travelplanner.dto;

import java.util.List;
import java.util.Map;

public class ItineraryAdjustRequest {
    private List<Long> removeSpots;
    private List<String> removeSpotNames;
    private List<AttractionToAdd> addSpots;
    private Map<String, Map<String, SpotAdjustment>> adjustments;

    public List<Long> getRemoveSpots() { return removeSpots; }
    public void setRemoveSpots(List<Long> removeSpots) { this.removeSpots = removeSpots; }
    public List<String> getRemoveSpotNames() { return removeSpotNames; }
    public void setRemoveSpotNames(List<String> removeSpotNames) { this.removeSpotNames = removeSpotNames; }
    public List<AttractionToAdd> getAddSpots() { return addSpots; }
    public void setAddSpots(List<AttractionToAdd> addSpots) { this.addSpots = addSpots; }
    public Map<String, Map<String, SpotAdjustment>> getAdjustments() { return adjustments; }
    public void setAdjustments(Map<String, Map<String, SpotAdjustment>> adjustments) { this.adjustments = adjustments; }

    public static class AttractionToAdd {
        private Long attractionId;
        private String attractionName;
        private Integer dayNumber;
        private Integer orderNum;
        private String arrivalTime;
        private String leaveTime;
        private Integer stayDuration;

        public Long getAttractionId() { return attractionId; }
        public void setAttractionId(Long attractionId) { this.attractionId = attractionId; }
        public String getAttractionName() { return attractionName; }
        public void setAttractionName(String attractionName) { this.attractionName = attractionName; }
        public Integer getDayNumber() { return dayNumber; }
        public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
        public Integer getOrderNum() { return orderNum; }
        public void setOrderNum(Integer orderNum) { this.orderNum = orderNum; }
        public String getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
        public String getLeaveTime() { return leaveTime; }
        public void setLeaveTime(String leaveTime) { this.leaveTime = leaveTime; }
        public Integer getStayDuration() { return stayDuration; }
        public void setStayDuration(Integer stayDuration) { this.stayDuration = stayDuration; }
    }

    public static class SpotAdjustment {
        private String arrivalTime;
        private String leaveTime;
        private Integer stayDuration;
        private Integer orderNum;

        public String getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
        public String getLeaveTime() { return leaveTime; }
        public void setLeaveTime(String leaveTime) { this.leaveTime = leaveTime; }
        public Integer getStayDuration() { return stayDuration; }
        public void setStayDuration(Integer stayDuration) { this.stayDuration = stayDuration; }
        public Integer getOrderNum() { return orderNum; }
        public void setOrderNum(Integer orderNum) { this.orderNum = orderNum; }
    }
}
