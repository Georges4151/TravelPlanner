package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TeamTodo {
    private Long id;
    private Long teamId;
    private String title;
    private String content;
    private String priority;  // high/medium/low
    private Long assigneeId;
    private String status;  // pending/in_progress/done
    private Long creatorId;
    private LocalDateTime dueDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 扩展字段
    private String assigneeName;
    private String creatorName;
}
