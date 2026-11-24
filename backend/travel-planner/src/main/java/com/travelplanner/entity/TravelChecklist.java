package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TravelChecklist {
    private Long id;
    private Long itineraryId;
    private String itemName;
    private String category;
    private Integer quantity;
    private Boolean isPacked;
    private Boolean isPurchased;
    private String notes;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
