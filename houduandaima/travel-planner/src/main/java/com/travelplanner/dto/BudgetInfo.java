package com.travelplanner.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetInfo {
    private BigDecimal totalBudget;
    private BigDecimal transportBudget;
    private BigDecimal hotelBudget;
    private BigDecimal foodBudget;
    private BigDecimal ticketBudget;
    private BigDecimal otherBudget;
    private BigDecimal estimatedTotal;
}
