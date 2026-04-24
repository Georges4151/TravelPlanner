package com.travelplanner.model;

import java.util.List;

public class OffpeakRecommendationResponse {
    private OriginalDates originalDates;
    private List<OffpeakRecommendation> offpeakRecommendations;

    public OriginalDates getOriginalDates() {
        return originalDates;
    }

    public void setOriginalDates(OriginalDates originalDates) {
        this.originalDates = originalDates;
    }

    public List<OffpeakRecommendation> getOffpeakRecommendations() {
        return offpeakRecommendations;
    }

    public void setOffpeakRecommendations(List<OffpeakRecommendation> offpeakRecommendations) {
        this.offpeakRecommendations = offpeakRecommendations;
    }

    public static class OriginalDates {
        private String startDate;
        private String endDate;
        private Boolean isHoliday;
        private String holidayName;
        private Boolean isWorkday;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public Boolean getIsHoliday() {
            return isHoliday;
        }

        public void setIsHoliday(Boolean isHoliday) {
            this.isHoliday = isHoliday;
        }

        public String getHolidayName() {
            return holidayName;
        }

        public void setHolidayName(String holidayName) {
            this.holidayName = holidayName;
        }

        public Boolean getIsWorkday() {
            return isWorkday;
        }

        public void setIsWorkday(Boolean isWorkday) {
            this.isWorkday = isWorkday;
        }
    }

    public static class OffpeakRecommendation {
        private String startDate;
        private String endDate;
        private String reason;
        private List<AlternativeSpot> alternativeSpots;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public List<AlternativeSpot> getAlternativeSpots() {
            return alternativeSpots;
        }

        public void setAlternativeSpots(List<AlternativeSpot> alternativeSpots) {
            this.alternativeSpots = alternativeSpots;
        }
    }

    public static class AlternativeSpot {
        private String original;
        private String alternative;
        private String reason;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getAlternative() {
            return alternative;
        }

        public void setAlternative(String alternative) {
            this.alternative = alternative;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
