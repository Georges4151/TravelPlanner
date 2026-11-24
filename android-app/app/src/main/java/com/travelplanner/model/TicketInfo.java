package com.travelplanner.model;

import java.math.BigDecimal;

public class TicketInfo {
    private String tripNumber;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String seatType;
    private BigDecimal price;
    private String transportMode;
    private String origin;
    private String destination;
    private String departureDate;
    private int dayNumber;  // 所属天数，用于排序
    private String mode;    // 出行方式代码
    private BigDecimal totalCost;  // 总花费（含票价+其他费用）

    // 步行/骑行相关字段
    private String distance;         // 距离（米或公里）
    private String caloriesBurned;  // 消耗卡路里
    private String healthTip;        // 健康提示
    private String landmarks;       // 途经地标
    private String bikeStations;     // 共享单车停放点
    private String safetyTip;         // 安全提示
    private String instruction;      // 具体行走/骑行指示
    private String terrain;          // 路况描述

    public String getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(String tripNumber) {
        this.tripNumber = tripNumber;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    // 步行/骑行字段 Getter/Setter
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(String caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getHealthTip() {
        return healthTip;
    }

    public void setHealthTip(String healthTip) {
        this.healthTip = healthTip;
    }

    public String getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(String landmarks) {
        this.landmarks = landmarks;
    }

    public String getBikeStations() {
        return bikeStations;
    }

    public void setBikeStations(String bikeStations) {
        this.bikeStations = bikeStations;
    }

    public String getSafetyTip() {
        return safetyTip;
    }

    public void setSafetyTip(String safetyTip) {
        this.safetyTip = safetyTip;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }
}
