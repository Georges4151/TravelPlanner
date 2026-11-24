package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TeamActivity {
    private Long id;
    private Long teamId;
    private Long userId;
    private String actionType;
    private String targetType;
    private Long targetId;
    private String content;
    private LocalDateTime createTime;
    
    // 扩展字段
    private String username;
    private String nickname;
}
