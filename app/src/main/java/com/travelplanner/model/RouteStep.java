package com.travelplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteStep implements Parcelable {
    private String instruction;
    private String road;
    private Integer distance;
    private Integer duration;
    private String action;
    private String polyline;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public static final Parcelable.Creator<RouteStep> CREATOR = new Parcelable.Creator<RouteStep>() {
        public RouteStep createFromParcel(Parcel in) {
            RouteStep step = new RouteStep();
            step.instruction = in.readString();
            step.road = in.readString();
            step.distance = (Integer) in.readValue(Integer.class.getClassLoader());
            step.duration = (Integer) in.readValue(Integer.class.getClassLoader());
            step.action = in.readString();
            step.polyline = in.readString();
            return step;
        }

        public RouteStep[] newArray(int size) {
            return new RouteStep[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(instruction);
        dest.writeString(road);
        dest.writeValue(distance);
        dest.writeValue(duration);
        dest.writeString(action);
        dest.writeString(polyline);
    }
}
