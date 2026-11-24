package com.travelplanner.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TelDeserializer implements JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        List<String> result = new ArrayList<>();

        if (json.isJsonNull()) {
            return result;
        }

        if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                if (element.isJsonPrimitive()) {
                    result.add(element.getAsString());
                }
            }
        } else if (json.isJsonPrimitive()) {
            String tel = json.getAsString();
            if (tel != null && !tel.isEmpty()) {
                result.add(tel);
            }
        } else if (json.isJsonObject()) {
            result.add("未知格式");
        }

        return result;
    }
}
