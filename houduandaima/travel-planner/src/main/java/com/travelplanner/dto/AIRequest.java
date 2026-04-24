package com.travelplanner.dto;

import lombok.Data;

@Data
public class AIRequest {
    private String prompt;
    private String context;
}
