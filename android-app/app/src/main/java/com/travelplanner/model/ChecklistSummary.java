package com.travelplanner.model;

import java.util.List;

public class ChecklistSummary {
    private String category;
    private Integer totalItems;
    private Integer totalQuantity;
    private Integer packedItems;
    private Integer needPurchaseItems;
    private List<ChecklistItemDTO> items;

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Integer getPackedItems() { return packedItems; }
    public void setPackedItems(Integer packedItems) { this.packedItems = packedItems; }
    public Integer getNeedPurchaseItems() { return needPurchaseItems; }
    public void setNeedPurchaseItems(Integer needPurchaseItems) { this.needPurchaseItems = needPurchaseItems; }
    public List<ChecklistItemDTO> getItems() { return items; }
    public void setItems(List<ChecklistItemDTO> items) { this.items = items; }

    public static class ChecklistItemDTO {
        private Long id;
        private String itemName;
        private String category;
        private Integer quantity;
        private Boolean isPacked;
        private Boolean isPurchased;
        private String notes;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Boolean getIsPacked() { return isPacked; }
        public void setIsPacked(Boolean isPacked) { this.isPacked = isPacked; }
        public Boolean getIsPurchased() { return isPurchased; }
        public void setIsPurchased(Boolean isPurchased) { this.isPurchased = isPurchased; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
