package com.travelplanner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.travelplanner.mapper")
public class TravelPlannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TravelPlannerApplication.class, args);
    }
}
