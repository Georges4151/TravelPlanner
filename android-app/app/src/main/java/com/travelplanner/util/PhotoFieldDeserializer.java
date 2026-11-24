package com.travelplanner.util;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

public class PhotoFieldDeserializer implements JsonDeserializer<String> {
    private static final String TAG = "PhotoFieldDeserializer";

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Log.d(TAG, "开始反序列化，类型: " + typeOfT + ", 原始JSON: " + json);

        if (json.isJsonNull()) {
            Log.d(TAG, "JSON为null");
            return null;
        }

        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();
            if (primitive.isString()) {
                String result = primitive.getAsString();
                Log.d(TAG, "直接返回String: " + result);
                return result;
            }
            String result = String.valueOf(primitive.getAsString());
            Log.d(TAG, "返回基本类型: " + result);
            return result;
        }

        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            Log.d(TAG, "解析JsonObject，所有键: " + obj.keySet());

            String[] priorityKeys = { "url", "title", "value", "count", "data", "result", "text", "name" };
            for (String key : priorityKeys) {
                if (obj.has(key)) {
                    JsonElement val = obj.get(key);
                    Log.d(TAG, "找到优先键: " + key + ", 值: " + val);
                    String result = extractString(val);
                    if (result != null) {
                        Log.d(TAG, "从键 " + key + " 提取到值: " + result);
                        return result;
                    }
                }
            }

            for (String key : obj.keySet()) {
                JsonElement val = obj.get(key);
                String result = extractString(val);
                if (result != null) {
                    Log.d(TAG, "从键 " + key + " 提取到值: " + result);
                    return result;
                }
            }
        }

        if (json.isJsonArray()) {
            Log.d(TAG, "解析JsonArray，大小: " + json.getAsJsonArray().size());
            if (json.getAsJsonArray().size() > 0) {
                JsonElement first = json.getAsJsonArray().get(0);
                Log.d(TAG, "取第一个元素: " + first);
                String result = extractString(first);
                if (result != null) {
                    Log.d(TAG, "从数组提取到值: " + result);
                    return result;
                }
            }
        }

        Log.d(TAG, "无法提取值，返回null");
        return null;
    }

    private String extractString(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return primitive.getAsString();
            }
            return String.valueOf(primitive.getAsString());
        }

        if (element.isJsonObject()) {
            return deserialize(element, String.class, null);
        }

        if (element.isJsonArray()) {
            if (element.getAsJsonArray().size() > 0) {
                return extractString(element.getAsJsonArray().get(0));
            }
        }

        return null;
    }
}
