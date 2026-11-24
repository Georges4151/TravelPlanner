package com.travelplanner.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private Long teamId;
    private Long userId;
    private String content;
    private String messageType;  // text/image/system
}
