package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Checklist {
    private Long id;
    private Long itineraryId;
    private String itemName;
    private String category;
    private Boolean isChecked;
    private String notes;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
