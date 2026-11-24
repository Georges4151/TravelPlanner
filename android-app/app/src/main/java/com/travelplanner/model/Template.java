package com.travelplanner.model;

import java.math.BigDecimal;

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
    private Integer viewCount;
    private String highlights;
    private BigDecimal budget;
    private Boolean isOfficial;
    private Boolean isHot;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    public String getCrowd() { return crowd; }
    public void setCrowd(String crowd) { this.crowd = crowd; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public Integer getViewCount() { return viewCount != null ? viewCount : 0; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public String getHighlights() { return highlights != null ? highlights : "精选景点、合理安排行程"; }
    public void setHighlights(String highlights) { this.highlights = highlights; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public Boolean getIsOfficial() { return isOfficial; }
    public void setIsOfficial(Boolean isOfficial) { this.isOfficial = isOfficial; }
    public Boolean getIsHot() { return isHot; }
    public void setIsHot(Boolean isHot) { this.isHot = isHot; }
}
