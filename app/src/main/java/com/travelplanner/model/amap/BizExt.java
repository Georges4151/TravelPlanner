package com.travelplanner.model.amap;

import com.google.gson.annotations.JsonAdapter;
import com.travelplanner.util.PhotoFieldDeserializer;

public class BizExt {
    @JsonAdapter(PhotoFieldDeserializer.class)
    private String cost;
    @JsonAdapter(PhotoFieldDeserializer.class)
    private String opentime2;
    @JsonAdapter(PhotoFieldDeserializer.class)
    private String rating;
    @JsonAdapter(PhotoFieldDeserializer.class)
    private String open_time;
    private String meal_ordering;
    @JsonAdapter(PhotoFieldDeserializer.class)
    private String room_type;
    @JsonAdapter(PhotoFieldDeserializer.class)
    private String room_price;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOpentime2() {
        return opentime2;
    }

    public void setOpentime2(String opentime2) {
        this.opentime2 = opentime2;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getMeal_ordering() {
        return meal_ordering;
    }

    public void setMeal_ordering(String meal_ordering) {
        this.meal_ordering = meal_ordering;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getRoom_price() {
        return room_price;
    }

    public void setRoom_price(String room_price) {
        this.room_price = room_price;
    }
}
