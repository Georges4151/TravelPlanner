package com.travelplanner.dto;

import lombok.Data;
import java.util.List;

@Data
public class VoteCreateRequest {
    private Long teamId;
    private String title;
    private String description;
    private Boolean multipleChoice;
    private String endTime;
    private Long creatorId;
    private List<String> options;
}
