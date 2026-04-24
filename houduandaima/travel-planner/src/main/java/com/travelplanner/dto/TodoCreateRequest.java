package com.travelplanner.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TodoCreateRequest {
    private Long teamId;
    private Long creatorId;
    private String title;
    private String content;
    private String priority;
    private Long assigneeId;
    private String dueDate;
}
