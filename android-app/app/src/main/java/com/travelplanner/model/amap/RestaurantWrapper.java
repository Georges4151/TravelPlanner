package com.travelplanner.model.amap;

public class RestaurantWrapper {
    private AMapPoiSearchResponse.Poi poi;
    private String rawJson;

    public RestaurantWrapper(AMapPoiSearchResponse.Poi poi, String rawJson) {
        this.poi = poi;
        this.rawJson = rawJson;
    }

    public AMapPoiSearchResponse.Poi getPoi() {
        return poi;
    }

    public String getRawJson() {
        return rawJson;
    }
}
