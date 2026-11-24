package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TeamMember {
    private Long id;
    private Long teamId;
    private Long userId;
    private String role;  // owner/editor/viewer
    private String nickname;
    private String avatar;
    private LocalDateTime joinTime;
    
    // 扩展字段（不存储在数据库）
    private String username;
}
