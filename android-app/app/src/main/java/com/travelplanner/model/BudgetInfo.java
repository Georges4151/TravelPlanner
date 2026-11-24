package com.travelplanner.model;

import java.math.BigDecimal;

public class BudgetInfo {
    private BigDecimal totalBudget;
    private BigDecimal transportBudget;
    private BigDecimal hotelBudget;
    private BigDecimal foodBudget;
    private BigDecimal ticketBudget;
    private BigDecimal otherBudget;
    private BigDecimal estimatedTotal;

    public BudgetInfo() {
    }

    public BigDecimal getTotalBudget() { return totalBudget; }
    public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }
    public BigDecimal getTransportBudget() { return transportBudget; }
    public void setTransportBudget(BigDecimal transportBudget) { this.transportBudget = transportBudget; }
    public BigDecimal getHotelBudget() { return hotelBudget; }
    public void setHotelBudget(BigDecimal hotelBudget) { this.hotelBudget = hotelBudget; }
    public BigDecimal getFoodBudget() { return foodBudget; }
    public void setFoodBudget(BigDecimal foodBudget) { this.foodBudget = foodBudget; }
    public BigDecimal getTicketBudget() { return ticketBudget; }
    public void setTicketBudget(BigDecimal ticketBudget) { this.ticketBudget = ticketBudget; }
    public BigDecimal getOtherBudget() { return otherBudget; }
    public void setOtherBudget(BigDecimal otherBudget) { this.otherBudget = otherBudget; }
    public BigDecimal getEstimatedTotal() { return estimatedTotal; }
    public void setEstimatedTotal(BigDecimal estimatedTotal) { this.estimatedTotal = estimatedTotal; }
}
