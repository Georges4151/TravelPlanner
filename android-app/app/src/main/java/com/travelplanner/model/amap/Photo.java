package com.travelplanner.model.amap;

import com.google.gson.annotations.JsonAdapter;
import com.travelplanner.util.PhotoFieldDeserializer;

public class Photo {
    @JsonAdapter(PhotoFieldDeserializer.class)
    private String title;
    @JsonAdapter(PhotoFieldDeserializer.class)
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
