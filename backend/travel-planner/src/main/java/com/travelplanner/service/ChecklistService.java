package com.travelplanner.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.travelplanner.dto.ChecklistGenerateRequest;
import com.travelplanner.dto.ChecklistSummary;
import com.travelplanner.entity.ChecklistItem;
import com.travelplanner.entity.ChecklistTemplate;
import com.travelplanner.entity.Itinerary;
import com.travelplanner.entity.TravelChecklist;
import com.travelplanner.mapper.ChecklistItemMapper;
import com.travelplanner.mapper.ChecklistTemplateMapper;
import com.travelplanner.mapper.ItineraryMapper;
import com.travelplanner.mapper.TravelChecklistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChecklistService {
    
    @Autowired
    private TravelChecklistMapper travelChecklistMapper;
    
    @Autowired
    private ChecklistItemMapper checklistItemMapper;
    
    @Autowired
    private ChecklistTemplateMapper checklistTemplateMapper;
    
    @Autowired
    private ItineraryMapper itineraryMapper;
    
    @Transactional
    public List<ChecklistSummary> generateChecklist(ChecklistGenerateRequest request) {
        Itinerary itinerary = itineraryMapper.findById(request.getItineraryId());
        if (itinerary == null) {
            throw new RuntimeException("行程不存在");
        }
        
        travelChecklistMapper.deleteByItineraryId(request.getItineraryId());
        
        List<TravelChecklist> items = new ArrayList<>();
        
        if (request.getTemplateId() != null) {
            ChecklistTemplate template = checklistTemplateMapper.findById(request.getTemplateId());
            if (template != null) {
                items = applyTemplate(request.getItineraryId(), template, itinerary.getDays());
                checklistTemplateMapper.incrementUsageCount(template.getId());
            }
        } else if (Boolean.TRUE.equals(request.getUseSmartRecommend())) {
            items = smartRecommend(request, itinerary);
        } else {
            items = generateDefaultChecklist(request.getItineraryId(), itinerary);
        }
        
        for (TravelChecklist item : items) {
            travelChecklistMapper.insert(item);
        }
        
        return getChecklistSummary(request.getItineraryId());
    }
    
    private List<TravelChecklist> applyTemplate(Long itineraryId, ChecklistTemplate template, Integer days) {
        List<Map<String, Object>> templateItems = JSON.parseObject(template.getItems(), 
                new TypeReference<List<Map<String, Object>>>(){});
        
        List<TravelChecklist> items = new ArrayList<>();
        for (Map<String, Object> item : templateItems) {
            TravelChecklist checklist = new TravelChecklist();
            checklist.setItineraryId(itineraryId);
            checklist.setItemName((String) item.get("name"));
            checklist.setCategory((String) item.get("category"));
            
            Integer quantity = ((Number) item.get("quantity")).intValue();
            if (quantity > 1 && days != null && days > 1) {
                quantity = Math.min(quantity, days + 1);
            }
            checklist.setQuantity(quantity);
            checklist.setIsPacked(false);
            checklist.setIsPurchased(false);
            items.add(checklist);
        }
        
        return items;
    }
    
    private List<TravelChecklist> smartRecommend(ChecklistGenerateRequest request, Itinerary itinerary) {
        String season = getSeason(request.getStartDate());
        String destinationType = getDestinationType(request.getDestination());
        
        List<ChecklistItem> recommendedItems = checklistItemMapper
                .findBySeasonAndDestinationType(season, destinationType);
        
        List<TravelChecklist> items = new ArrayList<>();
        for (ChecklistItem item : recommendedItems) {
            TravelChecklist checklist = new TravelChecklist();
            checklist.setItineraryId(request.getItineraryId());
            checklist.setItemName(item.getName());
            checklist.setCategory(item.getCategory());
            
            int quantity = calculateQuantity(item, itinerary.getDays());
            checklist.setQuantity(quantity);
            checklist.setIsPacked(false);
            checklist.setIsPurchased(false);
            items.add(checklist);
        }
        
        return items;
    }
    
    private List<TravelChecklist> generateDefaultChecklist(Long itineraryId, Itinerary itinerary) {
        List<ChecklistItem> allItems = checklistItemMapper.findAll();
        
        List<TravelChecklist> items = new ArrayList<>();
        for (ChecklistItem item : allItems) {
            if (Boolean.TRUE.equals(item.getIsEssential())) {
                TravelChecklist checklist = new TravelChecklist();
                checklist.setItineraryId(itineraryId);
                checklist.setItemName(item.getName());
                checklist.setCategory(item.getCategory());
                
                int quantity = calculateQuantity(item, itinerary.getDays());
                checklist.setQuantity(quantity);
                checklist.setIsPacked(false);
                checklist.setIsPurchased(false);
                items.add(checklist);
            }
        }
        
        return items;
    }
    
    private String getSeason(String startDate) {
        if (startDate == null || startDate.isEmpty()) {
            return "all";
        }
        
        try {
            LocalDate date = LocalDate.parse(startDate);
            Month month = date.getMonth();
            
            if (month == Month.MARCH || month == Month.APRIL || month == Month.MAY) {
                return "spring";
            } else if (month == Month.JUNE || month == Month.JULY || month == Month.AUGUST) {
                return "summer";
            } else if (month == Month.SEPTEMBER || month == Month.OCTOBER || month == Month.NOVEMBER) {
                return "autumn";
            } else {
                return "winter";
            }
        } catch (Exception e) {
            return "all";
        }
    }
    
    private String getDestinationType(String destination) {
        if (destination == null) {
            return "all";
        }
        
        destination = destination.toLowerCase();
        
        if (destination.contains("海") || destination.contains("岛") || destination.contains("滩")) {
            return "coastal";
        } else if (destination.contains("山") || destination.contains("峰") || destination.contains("岭")) {
            return "mountain";
        } else if (destination.contains("高原") || destination.contains("藏") || destination.contains("拉萨") || 
                   destination.contains("青海") || destination.contains("西藏")) {
            return "plateau";
        } else {
            return "city";
        }
    }
    
    private int calculateQuantity(ChecklistItem item, Integer days) {
        if (days == null || days <= 0) {
            days = 1;
        }
        
        if (item.getMinDays() == null) {
            return 1;
        }
        
        if (item.getMaxDays() == null || days <= item.getMinDays()) {
            return item.getMinDays();
        }
        
        if (days >= item.getMaxDays()) {
            return item.getMaxDays();
        }
        
        int quantity = item.getMinDays() + (days - item.getMinDays()) / 2;
        return Math.min(quantity, item.getMaxDays());
    }
    
    public List<ChecklistSummary> getChecklistSummary(Long itineraryId) {
        List<TravelChecklist> allItems = travelChecklistMapper.findByItineraryId(itineraryId);
        
        Map<String, List<TravelChecklist>> groupedItems = allItems.stream()
                .collect(Collectors.groupingBy(TravelChecklist::getCategory));
        
        List<ChecklistSummary> summaries = new ArrayList<>();
        for (Map.Entry<String, List<TravelChecklist>> entry : groupedItems.entrySet()) {
            ChecklistSummary summary = new ChecklistSummary();
            summary.setCategory(entry.getKey());
            summary.setTotalItems(entry.getValue().size());
            summary.setTotalQuantity(entry.getValue().stream().mapToInt(TravelChecklist::getQuantity).sum());
            summary.setPackedItems((int) entry.getValue().stream().filter(i -> Boolean.TRUE.equals(i.getIsPacked())).count());
            summary.setNeedPurchaseItems((int) entry.getValue().stream().filter(i -> Boolean.TRUE.equals(i.getIsPurchased())).count());
            
            List<ChecklistSummary.ChecklistItemDTO> itemDTOs = entry.getValue().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            summary.setItems(itemDTOs);
            
            summaries.add(summary);
        }
        
        summaries.sort(Comparator.comparing(ChecklistSummary::getCategory));
        
        return summaries;
    }
    
    private ChecklistSummary.ChecklistItemDTO convertToDTO(TravelChecklist item) {
        ChecklistSummary.ChecklistItemDTO dto = new ChecklistSummary.ChecklistItemDTO();
        dto.setId(item.getId());
        dto.setItemName(item.getItemName());
        dto.setCategory(item.getCategory());
        dto.setQuantity(item.getQuantity());
        dto.setIsPacked(item.getIsPacked());
        dto.setIsPurchased(item.getIsPurchased());
        dto.setNotes(item.getNotes());
        return dto;
    }
    
    @Transactional
    public void updatePackedStatus(Long itemId, Boolean isPacked) {
        travelChecklistMapper.updatePackedStatus(itemId, isPacked);
    }
    
    @Transactional
    public void updatePurchasedStatus(Long itemId, Boolean isPurchased) {
        travelChecklistMapper.updatePurchasedStatus(itemId, isPurchased);
    }
    
    @Transactional
    public void addItem(Long itineraryId, String itemName, String category, Integer quantity, String notes) {
        TravelChecklist item = new TravelChecklist();
        item.setItineraryId(itineraryId);
        item.setItemName(itemName);
        item.setCategory(category);
        item.setQuantity(quantity != null ? quantity : 1);
        item.setIsPacked(false);
        item.setIsPurchased(false);
        item.setNotes(notes);
        travelChecklistMapper.insert(item);
    }
    
    @Transactional
    public void deleteItem(Long itemId) {
        travelChecklistMapper.delete(itemId);
    }
    
    @Transactional
    public void updateItem(Long itemId, String itemName, String category, Integer quantity, String notes) {
        TravelChecklist item = travelChecklistMapper.findById(itemId);
        if (item != null) {
            item.setItemName(itemName);
            item.setCategory(category);
            item.setQuantity(quantity);
            item.setNotes(notes);
            travelChecklistMapper.update(item);
        }
    }
    
    public List<ChecklistTemplate> getAvailableTemplates(Long userId) {
        return checklistTemplateMapper.findAvailableTemplates(userId);
    }
    
    public List<ChecklistTemplate> getSystemTemplates() {
        return checklistTemplateMapper.findSystemTemplates();
    }
    
    public List<ChecklistTemplate> getUserTemplates(Long userId) {
        return checklistTemplateMapper.findByUserId(userId);
    }
    
    @Transactional
    public ChecklistTemplate createTemplate(Long userId, String name, String description, List<Map<String, Object>> items) {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setUserId(userId);
        template.setName(name);
        template.setDescription(description);
        template.setItems(JSON.toJSONString(items));
        template.setIsSystem(false);
        template.setIsDefault(false);
        template.setUsageCount(0);
        checklistTemplateMapper.insert(template);
        return template;
    }
    
    @Transactional
    public void deleteTemplate(Long templateId, Long userId) {
        ChecklistTemplate template = checklistTemplateMapper.findById(templateId);
        if (template != null && !Boolean.TRUE.equals(template.getIsSystem()) && 
            (userId.equals(template.getUserId()) || template.getUserId() == null)) {
            checklistTemplateMapper.delete(templateId);
        }
    }
    
    public Map<String, Object> getChecklistStats(Long itineraryId) {
        Map<String, Object> stats = new HashMap<>();
        int total = travelChecklistMapper.countByItineraryId(itineraryId);
        int packed = travelChecklistMapper.countPackedByItineraryId(itineraryId);
        
        stats.put("total", total);
        stats.put("packed", packed);
        stats.put("unpacked", total - packed);
        stats.put("progress", total > 0 ? (packed * 100.0 / total) : 0);
        
        return stats;
    }
}
