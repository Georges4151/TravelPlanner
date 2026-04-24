package com.travelplanner.model;

import java.math.BigDecimal;
import java.util.List;

public class Itinerary {
    private Long id;
    private Long userId;
    private String title;
    private String destination;
    private Integer days;
    private Integer peopleCount;
    private BigDecimal budget;
    private String travelStyle;
    private String status;
    private String startDate;
    private String endDate;
    private String source;
    private Long templateId;
    private String coverImage;
    private List<DayPlan> dayPlans;
    private BudgetInfo budgetInfo;
    private String generateStatus;
    private String generateMessage;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }
    public Integer getPeopleCount() { return peopleCount; }
    public void setPeopleCount(Integer peopleCount) { this.peopleCount = peopleCount; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public String getTravelStyle() { return travelStyle; }
    public void setTravelStyle(String travelStyle) { this.travelStyle = travelStyle; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public List<DayPlan> getDayPlans() { return dayPlans; }
    public void setDayPlans(List<DayPlan> dayPlans) { this.dayPlans = dayPlans; }
    public BudgetInfo getBudgetInfo() { return budgetInfo; }
    public void setBudgetInfo(BudgetInfo budgetInfo) { this.budgetInfo = budgetInfo; }
    public String getGenerateStatus() { return generateStatus; }
    public void setGenerateStatus(String generateStatus) { this.generateStatus = generateStatus; }
    public String getGenerateMessage() { return generateMessage; }
    public void setGenerateMessage(String generateMessage) { this.generateMessage = generateMessage; }

    public static class DayPlan {
        private Integer dayNumber;
        private String date;
        private String weather;
        private String tips;
        private Double estimatedDailyBudget;
        private MealRecommendations mealRecommendations;
        private List<SpotPlan> spots;

        public Integer getDayNumber() { return dayNumber; }
        public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getWeather() { return weather; }
        public void setWeather(String weather) { this.weather = weather; }
        public String getTips() { return tips; }
        public void setTips(String tips) { this.tips = tips; }
        public Double getEstimatedDailyBudget() { return estimatedDailyBudget; }
        public void setEstimatedDailyBudget(Double estimatedDailyBudget) { this.estimatedDailyBudget = estimatedDailyBudget; }
        public MealRecommendations getMealRecommendations() { return mealRecommendations; }
        public void setMealRecommendations(MealRecommendations mealRecommendations) { this.mealRecommendations = mealRecommendations; }
        public List<SpotPlan> getSpots() { return spots; }
        public void setSpots(List<SpotPlan> spots) { this.spots = spots; }
    }

    public static class MealRecommendations {
        private MealInfo breakfast;
        private MealInfo lunch;
        private MealInfo dinner;

        public MealInfo getBreakfast() { return breakfast; }
        public void setBreakfast(MealInfo breakfast) { this.breakfast = breakfast; }
        public MealInfo getLunch() { return lunch; }
        public void setLunch(MealInfo lunch) { this.lunch = lunch; }
        public MealInfo getDinner() { return dinner; }
        public void setDinner(MealInfo dinner) { this.dinner = dinner; }
    }

    public static class MealInfo {
        private String name;
        private Double price;
        private String distance;
        private String notes;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public String getDistance() { return distance; }
        public void setDistance(String distance) { this.distance = distance; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class SpotPlan {
        private Long attractionId;
        private String attractionName;
        private String arrivalTime;
        private String leaveTime;
        private Integer stayDuration;
        private BigDecimal ticketPrice;
        private Double estimatedCost;
        private String crowdLevel;
        private String bestVisitTime;
        private String transportMode;
        private String distanceToNext;
        private String transportToNext;
        private String trafficTips;
        private String notes;

        public Long getAttractionId() { return attractionId; }
        public void setAttractionId(Long attractionId) { this.attractionId = attractionId; }
        public String getAttractionName() { return attractionName; }
        public void setAttractionName(String attractionName) { this.attractionName = attractionName; }
        public String getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
        public String getLeaveTime() { return leaveTime; }
        public void setLeaveTime(String leaveTime) { this.leaveTime = leaveTime; }
        public Integer getStayDuration() { return stayDuration; }
        public void setStayDuration(Integer stayDuration) { this.stayDuration = stayDuration; }
        public BigDecimal getTicketPrice() { return ticketPrice; }
        public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
        public Double getEstimatedCost() { return estimatedCost; }
        public void setEstimatedCost(Double estimatedCost) { this.estimatedCost = estimatedCost; }
        public String getCrowdLevel() { return crowdLevel; }
        public void setCrowdLevel(String crowdLevel) { this.crowdLevel = crowdLevel; }
        public String getBestVisitTime() { return bestVisitTime; }
        public void setBestVisitTime(String bestVisitTime) { this.bestVisitTime = bestVisitTime; }
        public String getTransportMode() { return transportMode; }
        public void setTransportMode(String transportMode) { this.transportMode = transportMode; }
        public String getDistanceToNext() { return distanceToNext; }
        public void setDistanceToNext(String distanceToNext) { this.distanceToNext = distanceToNext; }
        public String getTransportToNext() { return transportToNext; }
        public void setTransportToNext(String transportToNext) { this.transportToNext = transportToNext; }
        public String getTrafficTips() { return trafficTips; }
        public void setTrafficTips(String trafficTips) { this.trafficTips = trafficTips; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
