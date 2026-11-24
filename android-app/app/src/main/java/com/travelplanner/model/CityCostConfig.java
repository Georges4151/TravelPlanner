package com.travelplanner.model;

public class CityCostConfig {
    private Double fuelPricePerKm;
    private Double parkingCostPerHour;
    private Double tollRate;
    private Double transitBaseCost;
    private Double transitDistanceRate;
    private Double transferCost;
    private Double taxiBaseCost;
    private Double taxiDistanceRate;
    private Double taxiTimeRate;
    private Double cyclingCostPerRide;

    public CityCostConfig() {
        this(0.6, 1.0, 1.0);
    }

    public CityCostConfig(Double fuelPricePerKm, Double parkingCostPerHour, Double tollRate) {
        this.fuelPricePerKm = fuelPricePerKm;
        this.parkingCostPerHour = parkingCostPerHour;
        this.tollRate = tollRate;
        this.transitBaseCost = 2.0;
        this.transitDistanceRate = 0.05;
        this.transferCost = 0.5;
        this.taxiBaseCost = 10.0;
        this.taxiDistanceRate = 2.0;
        this.taxiTimeRate = 0.4;
        this.cyclingCostPerRide = 1.5;
    }

    public Double getFuelPricePerKm() {
        return fuelPricePerKm;
    }

    public void setFuelPricePerKm(Double fuelPricePerKm) {
        this.fuelPricePerKm = fuelPricePerKm;
    }

    public Double getParkingCostPerHour() {
        return parkingCostPerHour;
    }

    public void setParkingCostPerHour(Double parkingCostPerHour) {
        this.parkingCostPerHour = parkingCostPerHour;
    }

    public Double getTollRate() {
        return tollRate;
    }

    public void setTollRate(Double tollRate) {
        this.tollRate = tollRate;
    }

    public Double getTransitBaseCost() {
        return transitBaseCost;
    }

    public void setTransitBaseCost(Double transitBaseCost) {
        this.transitBaseCost = transitBaseCost;
    }

    public Double getTransitDistanceRate() {
        return transitDistanceRate;
    }

    public void setTransitDistanceRate(Double transitDistanceRate) {
        this.transitDistanceRate = transitDistanceRate;
    }

    public Double getTransferCost() {
        return transferCost;
    }

    public void setTransferCost(Double transferCost) {
        this.transferCost = transferCost;
    }

    public Double getTaxiBaseCost() {
        return taxiBaseCost;
    }

    public void setTaxiBaseCost(Double taxiBaseCost) {
        this.taxiBaseCost = taxiBaseCost;
    }

    public Double getTaxiDistanceRate() {
        return taxiDistanceRate;
    }

    public void setTaxiDistanceRate(Double taxiDistanceRate) {
        this.taxiDistanceRate = taxiDistanceRate;
    }

    public Double getTaxiTimeRate() {
        return taxiTimeRate;
    }

    public void setTaxiTimeRate(Double taxiTimeRate) {
        this.taxiTimeRate = taxiTimeRate;
    }

    public Double getCyclingCostPerRide() {
        return cyclingCostPerRide;
    }

    public void setCyclingCostPerRide(Double cyclingCostPerRide) {
        this.cyclingCostPerRide = cyclingCostPerRide;
    }
}