package com.travelplanner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class TravelPlannerApp extends Application {
    private static TravelPlannerApp instance;
    private SharedPreferences sp;
    public static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private com.travelplanner.model.amap.AMapPoiSearchResponse.Poi currentHotel;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sp = getSharedPreferences("travel_planner", Context.MODE_PRIVATE);
    }

    public static TravelPlannerApp getInstance() {
        return instance;
    }

    public void saveUserId(Long userId) {
        sp.edit().putLong("user_id", userId).apply();
    }

    public Long getUserId() {
        return sp.getLong("user_id", 0);
    }

    public void saveUsername(String username) {
        sp.edit().putString("username", username).apply();
    }

    public String getUsername() {
        return sp.getString("username", "");
    }

    public void saveMemberLevel(Integer level) {
        sp.edit().putInt("member_level", level).apply();
    }

    public Integer getMemberLevel() {
        return sp.getInt("member_level", 0);
    }

    public boolean isLoggedIn() {
        return getUserId() != 0;
    }

    public void logout() {
        sp.edit().clear().apply();
    }

    public com.travelplanner.model.amap.AMapPoiSearchResponse.Poi getCurrentHotel() {
        return currentHotel;
    }

    public void setCurrentHotel(com.travelplanner.model.amap.AMapPoiSearchResponse.Poi hotel) {
        this.currentHotel = hotel;
    }
}
