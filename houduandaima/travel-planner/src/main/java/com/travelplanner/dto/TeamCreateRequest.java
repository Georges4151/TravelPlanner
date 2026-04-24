package com.travelplanner.dto;

import lombok.Data;

@Data
public class TeamCreateRequest {
    private String name;
    private Long creatorId;
    private Long itineraryId;
    private String description;
}
