package com.travelplanner.model;

import java.io.Serializable;

public class City implements Serializable {
    private String province;
    private String name;
    private String pinyin;

    public City(String province, String name, String pinyin) {
        this.province = province;
        this.name = name;
        this.pinyin = pinyin;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
