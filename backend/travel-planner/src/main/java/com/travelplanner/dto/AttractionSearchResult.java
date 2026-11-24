package com.travelplanner.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttractionSearchResult {
    private String id;
    private String name;
    private String city;
    private String province;
    private String address;
    private String location;
    private String tel;
    private String type;
    private Double distance;
    private String businessArea;
    private String typecode;
}