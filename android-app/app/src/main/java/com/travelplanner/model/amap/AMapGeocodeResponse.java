package com.travelplanner.model.amap;

import com.google.gson.annotations.SerializedName;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.util.List;

public class AMapGeocodeResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("infocode")
    private String infocode;

    @SerializedName("count")
    private String count;

    @SerializedName("geocodes")
    private List<Geocode> geocodes;

    public boolean isSuccess() {
        return "1".equals(status);
    }

    public String getInfo() {
        return info;
    }

    public String getInfocode() {
        return infocode;
    }

    public List<Geocode> getGeocodes() {
        return geocodes;
    }

    public static class Geocode {
        @SerializedName("formatted_address")
        private String formattedAddress;

        @SerializedName("country")
        private String country;

        @SerializedName("province")
        private String province;

        @SerializedName("city")
        private String city;

        @SerializedName("district")
        private JsonElement districtElement;

        @SerializedName("location")
        private String location;

        @SerializedName("level")
        private String level;

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public String getCountry() {
            return country;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            if (districtElement == null) {
                return null;
            }
            // 处理 district 可能是字符串或数组的情况
            if (districtElement.isJsonPrimitive()) {
                return districtElement.getAsString();
            } else if (districtElement.isJsonArray()) {
                JsonArray array = districtElement.getAsJsonArray();
                if (array.size() > 0) {
                    return array.get(0).getAsString();
                }
            }
            return null;
        }

        public String getLocation() {
            return location;
        }

        public double getLongitude() {
            if (location != null && location.contains(",")) {
                try {
                    return Double.parseDouble(location.split(",")[0]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
            return 0;
        }

        public double getLatitude() {
            if (location != null && location.contains(",")) {
                try {
                    return Double.parseDouble(location.split(",")[1]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
            return 0;
        }

        public String getLevel() {
            return level;
        }
    }
}
