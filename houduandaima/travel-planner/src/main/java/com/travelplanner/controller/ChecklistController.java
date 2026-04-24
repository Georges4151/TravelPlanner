package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.ChecklistGenerateRequest;
import com.travelplanner.dto.ChecklistSummary;
import com.travelplanner.entity.ChecklistTemplate;
import com.travelplanner.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checklist")
@CrossOrigin(origins = "*")
public class ChecklistController {
    
    @Autowired
    private ChecklistService checklistService;
    
    @PostMapping("/generate")
    public ApiResponse<List<ChecklistSummary>> generateChecklist(@RequestBody ChecklistGenerateRequest request) {
        try {
            List<ChecklistSummary> summary = checklistService.generateChecklist(request);
            return ApiResponse.success(summary);
        } catch (Exception e) {
            return ApiResponse.error(500, "生成清单失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/{itineraryId}")
    public ApiResponse<List<ChecklistSummary>> getChecklist(@PathVariable Long itineraryId) {
        try {
            List<ChecklistSummary> summary = checklistService.getChecklistSummary(itineraryId);
            return ApiResponse.success(summary);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取清单失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/stats/{itineraryId}")
    public ApiResponse<Map<String, Object>> getChecklistStats(@PathVariable Long itineraryId) {
        try {
            Map<String, Object> stats = checklistService.getChecklistStats(itineraryId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取统计失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/item/{itemId}/pack")
    public ApiResponse<Void> updatePackedStatus(@PathVariable Long itemId, @RequestParam Boolean isPacked) {
        try {
            checklistService.updatePackedStatus(itemId, isPacked);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, "更新打包状态失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/item/{itemId}/purchase")
    public ApiResponse<Void> updatePurchasedStatus(@PathVariable Long itemId, @RequestParam Boolean isPurchased) {
        try {
            checklistService.updatePurchasedStatus(itemId, isPurchased);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, "更新购买状态失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/item")
    public ApiResponse<Void> addItem(@RequestParam Long itineraryId, 
                                     @RequestParam String itemName,
                                     @RequestParam String category,
                                     @RequestParam(required = false, defaultValue = "1") Integer quantity,
                                     @RequestParam(required = false) String notes) {
        try {
            checklistService.addItem(itineraryId, itemName, category, quantity, notes);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, "添加物品失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/item/{itemId}")
    public ApiResponse<Void> deleteItem(@PathVariable Long itemId) {
        try {
            checklistService.deleteItem(itemId);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, "删除物品失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/item/{itemId}")
    public ApiResponse<Void> updateItem(@PathVariable Long itemId,
                                        @RequestParam String itemName,
                                        @RequestParam String category,
                                        @RequestParam Integer quantity,
                                        @RequestParam(required = false) String notes) {
        try {
            checklistService.updateItem(itemId, itemName, category, quantity, notes);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, "更新物品失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/templates")
    public ApiResponse<List<ChecklistTemplate>> getAvailableTemplates(@RequestParam Long userId) {
        try {
            List<ChecklistTemplate> templates = checklistService.getAvailableTemplates(userId);
            return ApiResponse.success(templates);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取模板失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/templates/system")
    public ApiResponse<List<ChecklistTemplate>> getSystemTemplates() {
        try {
            List<ChecklistTemplate> templates = checklistService.getSystemTemplates();
            return ApiResponse.success(templates);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取系统模板失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/templates/user/{userId}")
    public ApiResponse<List<ChecklistTemplate>> getUserTemplates(@PathVariable Long userId) {
        try {
            List<ChecklistTemplate> templates = checklistService.getUserTemplates(userId);
            return ApiResponse.success(templates);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取用户模板失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/template")
    public ApiResponse<ChecklistTemplate> createTemplate(@RequestParam Long userId,
                                                          @RequestParam String name,
                                                          @RequestParam(required = false) String description,
                                                          @RequestBody List<Map<String, Object>> items) {
        try {
            ChecklistTemplate template = checklistService.createTemplate(userId, name, description, items);
            return ApiResponse.success(template);
        } catch (Exception e) {
            return ApiResponse.error(500, "创建模板失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/template/{templateId}")
    public ApiResponse<Void> deleteTemplate(@PathVariable Long templateId, @RequestParam Long userId) {
        try {
            checklistService.deleteTemplate(templateId, userId);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, "删除模板失败: " + e.getMessage());
        }
    }
}
