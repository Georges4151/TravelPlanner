package com.travelplanner.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StringOrListDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        }

        if (json.isJsonArray()) {
            List<String> result = new ArrayList<>();
            for (JsonElement element : json.getAsJsonArray()) {
                if (element.isJsonPrimitive()) {
                    String value = element.getAsString();
                    value = value.replace("`", "");
                    value = value.trim();
                    if (!value.isEmpty()) {
                        result.add(value);
                    }
                }
            }
            return result.isEmpty() ? "" : String.join(", ", result);
        } else if (json.isJsonPrimitive()) {
            String value = json.getAsString();
            value = value.replace("`", "");
            value = value.trim();
            return value;
        }

        return null;
    }
}
