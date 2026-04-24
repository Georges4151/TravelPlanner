package com.travelplanner.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrafficInfo {
    private Double congestionIndex;
    private String status;
    private Double speed;
}