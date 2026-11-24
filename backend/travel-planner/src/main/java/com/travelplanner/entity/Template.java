package com.travelplanner.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Template {
    private Long id;
    private String title;
    private String destination;
    private Integer days;
    private String style;
    private String crowd;
    private String description;
    private String coverImage;
    private Long userId;
    private String authorName;
    private Integer usageCount;
    private BigDecimal budget;
    private Boolean isOfficial;
    private Boolean isHot;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
