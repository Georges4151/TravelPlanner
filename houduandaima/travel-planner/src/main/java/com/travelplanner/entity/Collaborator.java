package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Collaborator {
    private Long id;
    private Long itineraryId;
    private Long userId;
    private String role;
    private LocalDateTime createTime;
}
