package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChecklistTemplate {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private String items;
    private Boolean isSystem;
    private Boolean isDefault;
    private Integer usageCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
