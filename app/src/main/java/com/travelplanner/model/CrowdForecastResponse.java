package com.travelplanner.model;

import java.util.List;

public class CrowdForecastResponse {
    private Long attractionId;
    private String attractionName;
    private String coordinates;
    private List<DailyForecast> forecasts;

    public Long getAttractionId() { return attractionId; }
    public void setAttractionId(Long attractionId) { this.attractionId = attractionId; }
    public String getAttractionName() { return attractionName; }
    public void setAttractionName(String attractionName) { this.attractionName = attractionName; }
    public String getCoordinates() { return coordinates; }
    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }
    public List<DailyForecast> getForecasts() { return forecasts; }
    public void setForecasts(List<DailyForecast> forecasts) { this.forecasts = forecasts; }

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

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public Boolean getIsHoliday() { return isHoliday; }
        public void setIsHoliday(Boolean isHoliday) { this.isHoliday = isHoliday; }
        public String getHolidayName() { return holidayName; }
        public void setHolidayName(String holidayName) { this.holidayName = holidayName; }
        public String getWeather() { return weather; }
        public void setWeather(String weather) { this.weather = weather; }
        public String getTrafficStatus() { return trafficStatus; }
        public void setTrafficStatus(String trafficStatus) { this.trafficStatus = trafficStatus; }
        public Double getBaseCrowd() { return baseCrowd; }
        public void setBaseCrowd(Double baseCrowd) { this.baseCrowd = baseCrowd; }
        public Double getWeightedCrowd() { return weightedCrowd; }
        public void setWeightedCrowd(Double weightedCrowd) { this.weightedCrowd = weightedCrowd; }
        public String getCrowdLevel() { return crowdLevel; }
        public void setCrowdLevel(String crowdLevel) { this.crowdLevel = crowdLevel; }
        public List<String> getPeakHours() { return peakHours; }
        public void setPeakHours(List<String> peakHours) { this.peakHours = peakHours; }
        public String getBestTime() { return bestTime; }
        public void setBestTime(String bestTime) { this.bestTime = bestTime; }
        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
        public Factors getFactors() { return factors; }
        public void setFactors(Factors factors) { this.factors = factors; }
    }

    public static class Factors {
        private Double holiday;
        private Double weekend;
        private Double weather;
        private Double hot;
        private Double traffic;

        public Double getHoliday() { return holiday; }
        public void setHoliday(Double holiday) { this.holiday = holiday; }
        public Double getWeekend() { return weekend; }
        public void setWeekend(Double weekend) { this.weekend = weekend; }
        public Double getWeather() { return weather; }
        public void setWeather(Double weather) { this.weather = weather; }
        public Double getHot() { return hot; }
        public void setHot(Double hot) { this.hot = hot; }
        public Double getTraffic() { return traffic; }
        public void setTraffic(Double traffic) { this.traffic = traffic; }
    }
}
