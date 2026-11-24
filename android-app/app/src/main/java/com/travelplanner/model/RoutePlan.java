package com.travelplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class RoutePlan implements Parcelable {
    private Long id;
    private String origin;
    private String destination;
    private String originLat;
    private String originLng;
    private String destLat;
    private String destLng;
    private String transportMode;
    private Integer distance;
    private Integer duration;
    private BigDecimal cost;
    private String route;
    private List<RouteStep> steps;
    private String trafficStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOriginLat() {
        return originLat;
    }

    public void setOriginLat(String originLat) {
        this.originLat = originLat;
    }

    public String getOriginLng() {
        return originLng;
    }

    public void setOriginLng(String originLng) {
        this.originLng = originLng;
    }

    public String getDestLat() {
        return destLat;
    }

    public void setDestLat(String destLat) {
        this.destLat = destLat;
    }

    public String getDestLng() {
        return destLng;
    }

    public void setDestLng(String destLng) {
        this.destLng = destLng;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public List<RouteStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RouteStep> steps) {
        this.steps = steps;
    }

    public String getTrafficStatus() {
        return trafficStatus;
    }

    public void setTrafficStatus(String trafficStatus) {
        this.trafficStatus = trafficStatus;
    }

    public static final Parcelable.Creator<RoutePlan> CREATOR = new Parcelable.Creator<RoutePlan>() {
        public RoutePlan createFromParcel(Parcel in) {
            RoutePlan plan = new RoutePlan();
            plan.id = (Long) in.readValue(Long.class.getClassLoader());
            plan.origin = in.readString();
            plan.destination = in.readString();
            plan.originLat = in.readString();
            plan.originLng = in.readString();
            plan.destLat = in.readString();
            plan.destLng = in.readString();
            plan.transportMode = in.readString();
            plan.distance = (Integer) in.readValue(Integer.class.getClassLoader());
            plan.duration = (Integer) in.readValue(Integer.class.getClassLoader());
            plan.cost = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
            plan.route = in.readString();
            plan.steps = in.createTypedArrayList(RouteStep.CREATOR);
            plan.trafficStatus = in.readString();
            return plan;
        }

        public RoutePlan[] newArray(int size) {
            return new RoutePlan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeString(origin);
        dest.writeString(destination);
        dest.writeString(originLat);
        dest.writeString(originLng);
        dest.writeString(destLat);
        dest.writeString(destLng);
        dest.writeString(transportMode);
        dest.writeValue(distance);
        dest.writeValue(duration);
        dest.writeValue(cost);
        dest.writeString(route);
        dest.writeTypedList(steps);
        dest.writeString(trafficStatus);
    }
}
