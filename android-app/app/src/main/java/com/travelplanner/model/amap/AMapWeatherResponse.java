package com.travelplanner.model.amap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AMapWeatherResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("count")
    private String count;

    @SerializedName("info")
    private String info;

    @SerializedName("infocode")
    private String infocode;

    @SerializedName("forecasts")
    private List<Forecast> forecasts;

    public boolean isSuccess() {
        return "1".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public static class Forecast {
        @SerializedName("city")
        private String city;

        @SerializedName("adcode")
        private String adcode;

        @SerializedName("province")
        private String province;

        @SerializedName("reporttime")
        private String reporttime;

        @SerializedName("casts")
        private List<Cast> casts;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }

        public List<Cast> getCasts() {
            return casts;
        }

        public void setCasts(List<Cast> casts) {
            this.casts = casts;
        }
    }

    public static class Cast {
        @SerializedName("date")
        private String date;

        @SerializedName("week")
        private String week;

        @SerializedName("dayweather")
        private String dayWeather;

        @SerializedName("nightweather")
        private String nightWeather;

        @SerializedName("daytemp")
        private String dayTemp;

        @SerializedName("nighttemp")
        private String nightTemp;

        @SerializedName("daywind")
        private String dayWind;

        @SerializedName("nightwind")
        private String nightWind;

        @SerializedName("daypower")
        private String dayPower;

        @SerializedName("nightpower")
        private String nightPower;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getDayWeather() {
            return dayWeather;
        }

        public void setDayWeather(String dayWeather) {
            this.dayWeather = dayWeather;
        }

        public String getNightWeather() {
            return nightWeather;
        }

        public void setNightWeather(String nightWeather) {
            this.nightWeather = nightWeather;
        }

        public String getDayTemp() {
            return dayTemp;
        }

        public void setDayTemp(String dayTemp) {
            this.dayTemp = dayTemp;
        }

        public String getNightTemp() {
            return nightTemp;
        }

        public void setNightTemp(String nightTemp) {
            this.nightTemp = nightTemp;
        }

        public String getDayWind() {
            return dayWind;
        }

        public void setDayWind(String dayWind) {
            this.dayWind = dayWind;
        }

        public String getNightWind() {
            return nightWind;
        }

        public void setNightWind(String nightWind) {
            this.nightWind = nightWind;
        }

        public String getDayPower() {
            return dayPower;
        }

        public void setDayPower(String dayPower) {
            this.dayPower = dayPower;
        }

        public String getNightPower() {
            return nightPower;
        }

        public void setNightPower(String nightPower) {
            this.nightPower = nightPower;
        }
    }
}
