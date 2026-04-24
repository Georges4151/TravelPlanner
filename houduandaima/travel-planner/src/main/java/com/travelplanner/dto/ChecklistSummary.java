package com.travelplanner.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChecklistSummary {
    private String category;
    private Integer totalItems;
    private Integer totalQuantity;
    private Integer packedItems;
    private Integer needPurchaseItems;
    private List<ChecklistItemDTO> items;
    
    @Data
    public static class ChecklistItemDTO {
        private Long id;
        private String itemName;
        private String category;
        private Integer quantity;
        private Boolean isPacked;
        private Boolean isPurchased;
        private String notes;
    }
}
