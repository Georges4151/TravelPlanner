package com.travelplanner.model;

public class Expense {
    private Long id;
    private Long itineraryId;
    private Long userId;
    private String category;
    private String itemName;
    private java.math.BigDecimal amount;
    private String payMethod;
    private String notes;
    private String expenseTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getItineraryId() { return itineraryId; }
    public void setItineraryId(Long itineraryId) { this.itineraryId = itineraryId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public java.math.BigDecimal getAmount() { return amount; }
    public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
    public String getPayMethod() { return payMethod; }
    public void setPayMethod(String payMethod) { this.payMethod = payMethod; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getExpenseTime() { return expenseTime; }
    public void setExpenseTime(String expenseTime) { this.expenseTime = expenseTime; }
}
