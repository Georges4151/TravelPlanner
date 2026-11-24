package com.travelplanner.dto;

import lombok.Data;

@Data
public class WeatherInfo {
    private String city;
    private String date;
    private String weather;
    private String temperature;
    private String humidity;
    private String wind;
    private String tips;

    public WeatherInfo() {}

    public WeatherInfo(String city, String date, String weather, String temperature) {
        this.city = city;
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getText() {
        return weather;
    }
}
