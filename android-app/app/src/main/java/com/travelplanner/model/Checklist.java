package com.travelplanner.model;

public class Checklist {
    private Long id;
    private Long itineraryId;
    private String itemName;
    private String category;
    private Boolean isChecked;
    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getItineraryId() { return itineraryId; }
    public void setItineraryId(Long itineraryId) { this.itineraryId = itineraryId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Boolean getIsChecked() { return isChecked; }
    public void setIsChecked(Boolean isChecked) { this.isChecked = isChecked; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
