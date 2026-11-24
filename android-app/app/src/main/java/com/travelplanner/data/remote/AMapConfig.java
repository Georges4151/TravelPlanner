package com.travelplanner.data.remote;

public class AMapConfig {
    public static final String BASE_URL = "https://restapi.amap.com/v3/";
    
    // 请在本地填写自己的高德 Web 服务 Key，避免把真实 Key 提交到仓库
    public static final String API_KEY = "your-amap-key";
    
    public static String getApiKey() {
        return API_KEY;
    }
}
