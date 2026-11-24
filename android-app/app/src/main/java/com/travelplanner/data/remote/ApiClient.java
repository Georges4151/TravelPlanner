package com.travelplanner.data.remote;

import com.travelplanner.data.remote.api.AMapApi;
import com.travelplanner.data.remote.api.AttractionApi;
import com.travelplanner.data.remote.api.ChecklistApi;
import com.travelplanner.data.remote.api.FavoriteApi;
import com.travelplanner.data.remote.api.HotelApi;
import com.travelplanner.data.remote.api.ReminderApi;
import com.travelplanner.data.remote.api.RestaurantApi;
import com.travelplanner.data.remote.api.TeamApi;
import com.travelplanner.data.remote.api.TransportApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiClient {
    // Android模拟器访问本地主机的地址是 10.0.2.2
    // 真机测试时，需要改成实际服务器IP地址，例如: http://192.168.1.100:8080/
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    
    private static Retrofit retrofit;
    private static Retrofit aMapRetrofit;
    private static OkHttpClient okHttpClient;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getAMapRetrofit() {
        if (aMapRetrofit == null) {
            aMapRetrofit = new Retrofit.Builder()
                    .baseUrl(AMapConfig.BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return aMapRetrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();
        }
        return okHttpClient;
    }

    public static AttractionApi getAttractionApi() {
        return getRetrofit().create(AttractionApi.class);
    }

    public static RestaurantApi getRestaurantApi() {
        return getRetrofit().create(RestaurantApi.class);
    }

    public static HotelApi getHotelApi() {
        return getRetrofit().create(HotelApi.class);
    }

    public static TransportApi getTransportApi() {
        return getRetrofit().create(TransportApi.class);
    }

    public static ChecklistApi getChecklistApi() {
        return getRetrofit().create(ChecklistApi.class);
    }

    public static ReminderApi getReminderApi() {
        return getRetrofit().create(ReminderApi.class);
    }

    public static AMapApi getAMapApi() {
        return getAMapRetrofit().create(AMapApi.class);
    }

    public static FavoriteApi getFavoriteApi() {
        return getRetrofit().create(FavoriteApi.class);
    }

    public static TeamApi getTeamApi() {
        return getRetrofit().create(TeamApi.class);
    }
}
