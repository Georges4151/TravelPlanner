package com.travelplanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class City implements Serializable {
    private String province;
    private String name;
    private String pinyin;
    private List<String> districts;
    private boolean expanded;

    public City(String province, String name, String pinyin) {
        this.province = province;
        this.name = name;
        this.pinyin = pinyin;
        this.districts = new ArrayList<>();
        this.expanded = false;
    }

    public City(String province, String name, String pinyin, List<String> districts) {
        this.province = province;
        this.name = name;
        this.pinyin = pinyin;
        this.districts = districts != null ? districts : new ArrayList<>();
        this.expanded = false;
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

    public List<String> getDistricts() {
        return districts;
    }

    public void setDistricts(List<String> districts) {
        this.districts = districts;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
