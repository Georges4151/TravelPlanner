package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Reminder {
    private Long id;
    private Long itineraryId;
    private String title;
    private String content;
    private LocalDateTime remindTime;
    private String type;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
