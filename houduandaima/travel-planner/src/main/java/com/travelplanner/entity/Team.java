package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Team {
    private Long id;
    private String name;
    private String inviteCode;
    private Long creatorId;
    private Long itineraryId;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
