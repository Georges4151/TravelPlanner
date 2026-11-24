package com.travelplanner.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ItineraryResponse {
    private Long id;
    private String title;
    private String destination;
    private Integer days;
    private Integer peopleCount;
    private BigDecimal budget;
    private String travelStyle;
    private String status;
    private List<DayPlan> dayPlans;
    private BudgetInfo budgetInfo;
}
