package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TeamMessage {
    private Long id;
    private Long teamId;
    private Long userId;
    private String content;
    private String messageType;  // text/image/system
    private LocalDateTime createTime;
    
    // 扩展字段
    private String username;
    private String nickname;
    private String avatar;
}
