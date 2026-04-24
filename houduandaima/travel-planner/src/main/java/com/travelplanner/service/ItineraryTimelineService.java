package com.travelplanner.service;

import com.travelplanner.dto.BudgetInfo;
import com.travelplanner.dto.DayPlan;
import com.travelplanner.dto.SpotPlan;
import com.travelplanner.entity.Attraction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItineraryTimelineService {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    // 生成每日时间轴
    public List<DayPlan> generateDailyTimeline(List<Attraction> attractions, int days, String travelStyle, java.time.LocalDate startDate) {
        List<DayPlan> dayPlans = new ArrayList<>();
        int spotsPerDay = getSpotsPerDay(travelStyle);
        int totalSpots = Math.min(attractions.size(), spotsPerDay * days);

        int spotIndex = 0;
        for (int day = 1; day <= days && spotIndex < totalSpots; day++) {
            DayPlan dayPlan = new DayPlan();
            dayPlan.setDayNumber(day);
            dayPlan.setDate(startDate.plusDays(day - 1).toString());
            dayPlan.setWeather("晴");
            dayPlan.setTips(generateDayTips(day, travelStyle));

            List<SpotPlan> spots = new ArrayList<>();
            LocalTime currentTime = getStartHour(travelStyle);

            int spotsToday = Math.min(spotsPerDay, totalSpots - spotIndex);
            for (int i = 0; i < spotsToday && spotIndex < attractions.size(); i++) {
                Attraction attraction = attractions.get(spotIndex);
                spotIndex++;

                SpotPlan spot = new SpotPlan();
                spot.setName(attraction.getName());
                spot.setType(attraction.getDescription());
                spot.setVisitTime(currentTime.format(TIME_FMT));

                int duration = attraction.getPlayDuration() != null ? attraction.getPlayDuration() : 90;
                spot.setDuration(duration);

                LocalTime leaveTime = currentTime.plusMinutes(duration);
                spot.setArrivalTime(currentTime.format(TIME_FMT));
                spot.setLeaveTime(leaveTime.format(TIME_FMT));
                spot.setStayDuration(duration);

                if (i < spotsToday - 1) {
                    spot.setTransportToNext("公交/打车（约20分钟）");
                    currentTime = leaveTime.plusMinutes(20);
                } else {
                    spot.setTransportToNext("返回酒店");
                    currentTime = leaveTime;
                }

                spot.setTicketPrice(attraction.getTicketPrice() != null ? attraction.getTicketPrice() : BigDecimal.ZERO);
                spot.setCrowdLevel(attraction.getCrowdLevel());
                spot.setNotes(generateSpotNotes(attraction, travelStyle));

                spots.add(spot);
            }

            dayPlan.setSpots(spots);
            dayPlans.add(dayPlan);
        }

        return dayPlans;
    }

    // 计算每日总览
    public DailySummary calculateDailySummary(DayPlan dayPlan) {
        DailySummary summary = new DailySummary();

        int totalDistance = 0;
        int totalDuration = 0;
        BigDecimal totalTicketCost = BigDecimal.ZERO;

        for (SpotPlan spot : dayPlan.getSpots()) {
            totalDuration += spot.getDuration() != null ? spot.getDuration() : 0;
            if (spot.getTicketPrice() != null) {
                totalTicketCost = totalTicketCost.add(spot.getTicketPrice());
            }
            totalDistance += 2;
        }

        summary.setTotalDistance(totalDistance);
        summary.setTotalDuration(totalDuration);
        summary.setTotalTicketCost(totalTicketCost);
        summary.setSpotCount(dayPlan.getSpots().size());

        return summary;
    }

    // 计算总预算
    public BudgetInfo calculateDetailedBudget(List<DayPlan> dayPlans, int peopleCount, String travelStyle) {
        BudgetInfo budgetInfo = new BudgetInfo();

        BigDecimal totalTicketCost = BigDecimal.ZERO;
        int totalDays = dayPlans.size();

        for (DayPlan day : dayPlans) {
            for (SpotPlan spot : day.getSpots()) {
                if (spot.getTicketPrice() != null) {
                    totalTicketCost = totalTicketCost.add(spot.getTicketPrice());
                }
            }
        }

        BigDecimal ticketTotal = totalTicketCost.multiply(new BigDecimal(peopleCount));
        BigDecimal transportPerPersonPerDay = new BigDecimal("20");
        BigDecimal transportTotal = transportPerPersonPerDay.multiply(new BigDecimal(totalDays)).multiply(new BigDecimal(peopleCount));
        BigDecimal foodPerPersonPerDay = new BigDecimal("80");
        BigDecimal foodTotal = foodPerPersonPerDay.multiply(new BigDecimal(totalDays)).multiply(new BigDecimal(peopleCount));
        int rooms = (peopleCount + 1) / 2;
        BigDecimal hotelPerDay = new BigDecimal("200").multiply(new BigDecimal(rooms));
        BigDecimal hotelTotal = hotelPerDay.multiply(new BigDecimal(totalDays));
        BigDecimal otherPerPersonPerDay = new BigDecimal("30");
        BigDecimal otherTotal = otherPerPersonPerDay.multiply(new BigDecimal(totalDays)).multiply(new BigDecimal(peopleCount));

        BigDecimal grandTotal = ticketTotal.add(transportTotal).add(foodTotal).add(hotelTotal).add(otherTotal);

        budgetInfo.setTotalBudget(grandTotal);
        budgetInfo.setTransportBudget(transportTotal);
        budgetInfo.setHotelBudget(hotelTotal);
        budgetInfo.setFoodBudget(foodTotal);
        budgetInfo.setTicketBudget(ticketTotal);
        budgetInfo.setOtherBudget(otherTotal);
        budgetInfo.setEstimatedTotal(grandTotal.multiply(new BigDecimal("0.95")));

        return budgetInfo;
    }

    private int getSpotsPerDay(String travelStyle) {
        switch (travelStyle) {
            case "暴走": return 5;
            case "休闲": return 2;
            case "亲子": return 2;
            case "错峰优先": return 3;
            default: return 3;
        }
    }

    private LocalTime getStartHour(String travelStyle) {
        switch (travelStyle) {
            case "暴走": return LocalTime.of(7, 0);
            case "错峰优先": return LocalTime.of(7, 30);
            case "亲子": return LocalTime.of(9, 30);
            case "休闲": return LocalTime.of(9, 0);
            default: return LocalTime.of(8, 0);
        }
    }

    private String generateDayTips(int dayNumber, String travelStyle) {
        switch (travelStyle) {
            case "错峰优先":
                return "建议早起出发，避开人流高峰时段，享受更好的游览体验";
            case "亲子":
                return "注意儿童体力，适时休息，备好零食和水";
            case "暴走":
                return "行程较紧凑，注意体力分配，穿舒适的鞋子";
            case "休闲":
                return "放慢脚步，享受旅途，不必赶景点";
            default:
                return "祝您旅途愉快，注意安全";
        }
    }

    private String generateSpotNotes(Attraction attraction, String travelStyle) {
        StringBuilder notes = new StringBuilder();

        if ("错峰优先".equals(travelStyle)) {
            notes.append("错峰出行推荐");
            if ("拥挤".equals(attraction.getCrowdLevel())) {
                notes.append("，建议避开节假日");
            }
        } else if ("亲子".equals(travelStyle)) {
            notes.append("适合亲子游");
        } else {
            notes.append("热门景点");
        }

        if (attraction.getRating() != null && attraction.getRating() >= 4.5) {
            notes.append("，高评分景点");
        }

        return notes.toString();
    }

    public static class DailySummary {
        private int totalDistance;
        private int totalDuration;
        private BigDecimal totalTicketCost;
        private int spotCount;

        public int getTotalDistance() { return totalDistance; }
        public void setTotalDistance(int totalDistance) { this.totalDistance = totalDistance; }
        public int getTotalDuration() { return totalDuration; }
        public void setTotalDuration(int totalDuration) { this.totalDuration = totalDuration; }
        public BigDecimal getTotalTicketCost() { return totalTicketCost; }
        public void setTotalTicketCost(BigDecimal totalTicketCost) { this.totalTicketCost = totalTicketCost; }
        public int getSpotCount() { return spotCount; }
        public void setSpotCount(int spotCount) { this.spotCount = spotCount; }
    }
}
