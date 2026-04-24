package com.travelplanner.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChecklistGenerateRequest {
    private Long itineraryId;
    private String destination;
    private Integer days;
    private String startDate;
    private String travelStyle;
    private Long templateId;
    private Boolean useSmartRecommend;
}
