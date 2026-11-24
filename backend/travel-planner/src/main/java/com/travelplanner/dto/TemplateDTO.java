package com.travelplanner.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TemplateDTO {
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
    private Integer viewCount;
    private String highlights;
    private BigDecimal budget;
    private Boolean isOfficial;
    private Boolean isHot;
}