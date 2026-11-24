package com.travelplanner.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.travelplanner.TravelPlannerApp;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    private static final Gson gson = new GsonBuilder().create();

    public static void post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json != null ? json : "", JSON);
        Request request = new Request.Builder()
                .url(TravelPlannerApp.BASE_URL + url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void post(String url, Object data, Callback callback) {
        String json = data != null ? toJson(data) : "{}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(TravelPlannerApp.BASE_URL + url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void put(String url, Callback callback) {
        RequestBody body = RequestBody.create("", JSON);
        Request request = new Request.Builder()
                .url(TravelPlannerApp.BASE_URL + url)
                .put(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void put(String url, Object data, Callback callback) {
        String json = data != null ? toJson(data) : "{}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(TravelPlannerApp.BASE_URL + url)
                .put(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(TravelPlannerApp.BASE_URL + url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void delete(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(TravelPlannerApp.BASE_URL + url)
                .delete()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postWithAuth(String url, String json, String token, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(TravelPlannerApp.BASE_URL + url)
                .post(body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postForm(String url, Map<String, String> params, Callback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = formBuilder.build();
        Request request = new Request.Builder()
                .url(TravelPlannerApp.BASE_URL + url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, java.lang.reflect.Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }
}
