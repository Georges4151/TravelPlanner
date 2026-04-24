package com.travelplanner.service;

import com.travelplanner.model.CityCostConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CostCalculator {
    private static final Map<String, CityCostConfig> CITY_CONFIGS = new HashMap<>();

    static {
        CITY_CONFIGS.put("北京", new CityCostConfig(0.8, 1.2, 1.3));
        CITY_CONFIGS.put("上海", new CityCostConfig(0.8, 1.2, 1.3));
        CITY_CONFIGS.put("广州", new CityCostConfig(0.7, 1.1, 1.2));
        CITY_CONFIGS.put("深圳", new CityCostConfig(0.7, 1.1, 1.2));
        CITY_CONFIGS.put("杭州", new CityCostConfig(0.65, 1.0, 1.1));
        CITY_CONFIGS.put("成都", new CityCostConfig(0.6, 0.9, 1.0));
        CITY_CONFIGS.put("武汉", new CityCostConfig(0.6, 0.9, 1.0));
        CITY_CONFIGS.put("西安", new CityCostConfig(0.6, 0.9, 1.0));
    }

    public static BigDecimal calculateDrivingCost(double distance, String city) {
        CityCostConfig config = CITY_CONFIGS.getOrDefault(city, new CityCostConfig(0.6, 1.0, 1.0));
        double fuelCost = distance * config.getFuelPricePerKm();
        double parkingCost = config.getParkingCostPerHour() * 2;
        double tollCost = distance * 0.5 * config.getTollRate();
        return BigDecimal.valueOf(fuelCost + parkingCost + tollCost).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateTransitCost(double distance, String city, int transferCount) {
        CityCostConfig config = CITY_CONFIGS.getOrDefault(city, new CityCostConfig(0.6, 1.0, 1.0));
        double baseCost = config.getTransitBaseCost();
        double distanceCost = distance * config.getTransitDistanceRate();
        double transferCost = transferCount * config.getTransferCost();
        return BigDecimal.valueOf(baseCost + distanceCost + transferCost).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateTaxiCost(double distance, long duration, String city) {
        CityCostConfig config = CITY_CONFIGS.getOrDefault(city, new CityCostConfig(0.6, 1.0, 1.0));
        double baseCost = config.getTaxiBaseCost();
        double distanceCost = distance * config.getTaxiDistanceRate();
        double timeCost = (duration / 60.0) * config.getTaxiTimeRate();
        return BigDecimal.valueOf(baseCost + distanceCost + timeCost).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateWalkingCost() {
        return BigDecimal.ZERO;
    }

    public static BigDecimal calculateCyclingCost(double duration, String city) {
        CityCostConfig config = CITY_CONFIGS.getOrDefault(city, new CityCostConfig(0.6, 1.0, 1.0));
        double rides = Math.ceil(duration / 900.0);
        return BigDecimal.valueOf(rides * config.getCyclingCostPerRide()).setScale(2, RoundingMode.HALF_UP);
    }

    public static CityCostConfig getCityConfig(String city) {
        return CITY_CONFIGS.getOrDefault(city, new CityCostConfig(0.6, 1.0, 1.0));
    }

    public static void addCityConfig(String city, CityCostConfig config) {
        CITY_CONFIGS.put(city, config);
    }
}