package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.entity.Budget;
import com.travelplanner.entity.Expense;
import com.travelplanner.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/{itineraryId}")
    public ApiResponse<Budget> getBudget(@PathVariable Long itineraryId) {
        return ApiResponse.success(budgetService.getBudgetByItineraryId(itineraryId));
    }

    @PostMapping("/create")
    public ApiResponse<Void> createBudget(
            @RequestParam Long itineraryId,
            @RequestParam String totalBudget) {
        try {
            budgetService.createBudget(itineraryId, new BigDecimal(totalBudget));
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @PostMapping("/expense")
    public ApiResponse<Void> addExpense(
            @RequestParam Long itineraryId,
            @RequestParam Long userId,
            @RequestParam String category,
            @RequestParam String itemName,
            @RequestParam String amount) {
        try {
            budgetService.addExpense(itineraryId, userId, category, itemName, new BigDecimal(amount));
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @GetMapping("/expense/{itineraryId}")
    public ApiResponse<List<Expense>> getExpenses(@PathVariable Long itineraryId) {
        return ApiResponse.success(budgetService.getExpensesByItineraryId(itineraryId));
    }

    @GetMapping("/comparison/{itineraryId}")
    public ApiResponse<Map<String, BigDecimal>> getBudgetComparison(@PathVariable Long itineraryId) {
        return ApiResponse.success(budgetService.getBudgetComparison(itineraryId));
    }

    @PostMapping("/update")
    public ApiResponse<Void> updateBudget(
            @RequestParam Long itineraryId,
            @RequestParam String totalBudget,
            @RequestParam String transportBudget,
            @RequestParam String hotelBudget,
            @RequestParam String foodBudget,
            @RequestParam String ticketBudget,
            @RequestParam String otherBudget) {
        try {
            budgetService.updateBudget(
                    itineraryId,
                    new BigDecimal(totalBudget),
                    new BigDecimal(transportBudget),
                    new BigDecimal(hotelBudget),
                    new BigDecimal(foodBudget),
                    new BigDecimal(ticketBudget),
                    new BigDecimal(otherBudget)
            );
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }
}
