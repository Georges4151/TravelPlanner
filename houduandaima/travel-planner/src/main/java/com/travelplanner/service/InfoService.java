package com.travelplanner.service;

import com.travelplanner.entity.Hotel;
import com.travelplanner.entity.Restaurant;
import com.travelplanner.entity.Transport;
import com.travelplanner.mapper.HotelMapper;
import com.travelplanner.mapper.RestaurantMapper;
import com.travelplanner.mapper.TransportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InfoService {

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private TransportMapper transportMapper;

    public List<Hotel> getHotelsByCity(String city) {
        return hotelMapper.findByCity(city);
    }

    public List<Restaurant> getRestaurantsByCity(String city) {
        return restaurantMapper.findByCity(city);
    }

    public List<Restaurant> getRestaurantsByCuisine(String cuisineType) {
        return restaurantMapper.findByCuisineType(cuisineType);
    }

    public List<Transport> getTransportsByCity(String city) {
        return transportMapper.findByCity(city);
    }

    public List<Transport> getTransportsByType(String type) {
        return transportMapper.findByType(type);
    }

    public List<Hotel> getMockHotels(String city) {
        Hotel h1 = new Hotel();
        h1.setId(1L);
        h1.setName("西湖畔精品酒店");
        h1.setCity(city);
        h1.setAddress("西湖区龙井路1号");
        h1.setPrice(new BigDecimal("688"));
        h1.setRoomType("豪华双人房");
        h1.setRating(4.8);
        h1.setFacilities("免费WiFi,停车场,健身房");

        Hotel h2 = new Hotel();
        h2.setId(2L);
        h2.setName("灵隐寺周边民宿");
        h2.setCity(city);
        h2.setAddress("西湖区灵隐路18号");
        h2.setPrice(new BigDecimal("388"));
        h2.setRoomType("舒适大床房");
        h2.setRating(4.6);
        h2.setFacilities("免费WiFi,早餐");

        return List.of(h1, h2);
    }

    public List<Restaurant> getMockRestaurants(String city) {
        Restaurant r1 = new Restaurant();
        r1.setId(1L);
        r1.setName("外婆家");
        r1.setCity(city);
        r1.setAddress("西湖区湖滨路28号");
        r1.setAvgPrice(new BigDecimal("80"));
        r1.setRating(4.5);
        r1.setCuisineType("浙菜");
        r1.setBusinessHours("10:30-21:00");

        Restaurant r2 = new Restaurant();
        r2.setId(2L);
        r2.setName("知味观");
        r2.setCity(city);
        r2.setAddress("西湖区孤山路28号");
        r2.setAvgPrice(new BigDecimal("120"));
        r2.setRating(4.7);
        r2.setCuisineType("浙菜");
        r2.setBusinessHours("08:00-20:00");

        return List.of(r1, r2);
    }

    public List<Transport> getMockTransports(String departure, String arrival) {
        Transport t1 = new Transport();
        t1.setId(1L);
        t1.setType("highspeed");
        t1.setDeparture(departure);
        t1.setArrival(arrival);
        t1.setDepartureTime("08:30");
        t1.setArrivalTime("10:45");
        t1.setDuration(135);
        t1.setPrice("553");
        t1.setProvider("高铁");

        Transport t2 = new Transport();
        t2.setId(2L);
        t2.setType("flight");
        t2.setDeparture(departure);
        t2.setArrival(arrival);
        t2.setDepartureTime("09:00");
        t2.setArrivalTime("11:30");
        t2.setDuration(150);
        t2.setPrice("780");
        t2.setProvider("东方航空");

        return List.of(t1, t2);
    }
}
