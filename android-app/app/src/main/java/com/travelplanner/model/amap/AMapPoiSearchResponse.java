package com.travelplanner.model.amap;

import com.google.gson.annotations.JsonAdapter;
import com.travelplanner.util.TelDeserializer;

import java.util.List;

public class AMapPoiSearchResponse {
    private String status;
    private String info;
    private String infocode;
    private String count;
    private List<Poi> pois;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Poi> getPois() {
        return pois;
    }

    public void setPois(List<Poi> pois) {
        this.pois = pois;
    }

    public boolean isSuccess() {
        return "1".equals(status);
    }

    public static class Poi {
        private String id;
        private String name;
        private String type;
        private String typecode;
        @JsonAdapter(com.travelplanner.util.StringOrListDeserializer.class)
        private String address;
        private String location;
        @JsonAdapter(TelDeserializer.class)
        private List<String> tel;
        private String distance;
        @JsonAdapter(com.travelplanner.util.StringOrListDeserializer.class)
        private String biz_type;
        @JsonAdapter(com.travelplanner.util.StringOrListDeserializer.class)
        private String business_area;
        private String rating;
        private String cost;
        private BizExt biz_ext;
        private List<Photo> photos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypecode() {
            return typecode;
        }

        public void setTypecode(String typecode) {
            this.typecode = typecode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<String> getTel() {
            return tel;
        }

        public void setTel(List<String> tel) {
            this.tel = tel;
        }

        public String getFirstTel() {
            if (tel != null && !tel.isEmpty()) {
                return tel.get(0);
            }
            return null;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getBiz_type() {
            return biz_type;
        }

        public void setBiz_type(String biz_type) {
            this.biz_type = biz_type;
        }

        public String getBusiness_area() {
            return business_area;
        }

        public void setBusiness_area(String business_area) {
            this.business_area = business_area;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public BizExt getBiz_ext() {
            return biz_ext;
        }

        public void setBiz_ext(BizExt biz_ext) {
            this.biz_ext = biz_ext;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }
    }
}
