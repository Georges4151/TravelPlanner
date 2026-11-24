package com.travelplanner.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Budget {
    private Long id;
    private Long itineraryId;
    private BigDecimal totalBudget;
    private BigDecimal transportBudget;
    private BigDecimal hotelBudget;
    private BigDecimal foodBudget;
    private BigDecimal ticketBudget;
    private BigDecimal otherBudget;
    private BigDecimal actualTransport;
    private BigDecimal actualHotel;
    private BigDecimal actualFood;
    private BigDecimal actualTicket;
    private BigDecimal actualOther;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
