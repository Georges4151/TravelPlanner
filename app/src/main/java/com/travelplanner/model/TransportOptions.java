package com.travelplanner.model;

import java.util.List;

public class TransportOptions {
    private String fromLocation;
    private String toLocation;
    private List<RoutePlan> drivingPlans;
    private List<RoutePlan> transitPlans;
    private List<RoutePlan> walkingPlans;
    private List<RoutePlan> cyclingPlans;

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public List<RoutePlan> getDrivingPlans() {
        return drivingPlans;
    }

    public void setDrivingPlans(List<RoutePlan> drivingPlans) {
        this.drivingPlans = drivingPlans;
    }

    public List<RoutePlan> getTransitPlans() {
        return transitPlans;
    }

    public void setTransitPlans(List<RoutePlan> transitPlans) {
        this.transitPlans = transitPlans;
    }

    public List<RoutePlan> getWalkingPlans() {
        return walkingPlans;
    }

    public void setWalkingPlans(List<RoutePlan> walkingPlans) {
        this.walkingPlans = walkingPlans;
    }

    public List<RoutePlan> getCyclingPlans() {
        return cyclingPlans;
    }

    public void setCyclingPlans(List<RoutePlan> cyclingPlans) {
        this.cyclingPlans = cyclingPlans;
    }
}