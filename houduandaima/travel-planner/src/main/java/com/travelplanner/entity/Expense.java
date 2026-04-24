package com.travelplanner.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Expense {
    private Long id;
    private Long itineraryId;
    private Long userId;
    private String category;
    private String itemName;
    private BigDecimal amount;
    private String payMethod;
    private String notes;
    private LocalDateTime expenseTime;
    private LocalDateTime createTime;
}
