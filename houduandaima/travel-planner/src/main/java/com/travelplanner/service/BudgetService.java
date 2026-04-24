package com.travelplanner.service;

import com.travelplanner.entity.Budget;
import com.travelplanner.entity.Expense;
import com.travelplanner.mapper.BudgetMapper;
import com.travelplanner.mapper.ExpenseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BudgetService {

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private ExpenseMapper expenseMapper;

    public Budget getBudgetByItineraryId(Long itineraryId) {
        Budget budget = budgetMapper.findByItineraryId(itineraryId);
        if (budget == null) {
            System.out.println("Budget not found for itineraryId: " + itineraryId);
        } else {
            System.out.println("Budget found: totalBudget=" + budget.getTotalBudget() +
                    ", transportBudget=" + budget.getTransportBudget() +
                    ", hotelBudget=" + budget.getHotelBudget());
        }
        return budget;
    }

    public void createBudget(Long itineraryId, BigDecimal totalBudget) {
        Budget budget = new Budget();
        budget.setItineraryId(itineraryId);
        budget.setTotalBudget(totalBudget);

        BigDecimal transportBudget = totalBudget.multiply(new BigDecimal("0.3"));
        BigDecimal hotelBudget = totalBudget.multiply(new BigDecimal("0.35"));
        BigDecimal foodBudget = totalBudget.multiply(new BigDecimal("0.2"));
        BigDecimal ticketBudget = totalBudget.multiply(new BigDecimal("0.1"));
        BigDecimal otherBudget = totalBudget.multiply(new BigDecimal("0.05"));

        budget.setTransportBudget(transportBudget);
        budget.setHotelBudget(hotelBudget);
        budget.setFoodBudget(foodBudget);
        budget.setTicketBudget(ticketBudget);
        budget.setOtherBudget(otherBudget);

        budgetMapper.insert(budget);
    }

    public void addExpense(Long itineraryId, Long userId, String category, String itemName, BigDecimal amount) {
        Expense expense = new Expense();
        expense.setItineraryId(itineraryId);
        expense.setUserId(userId);
        expense.setCategory(category);
        expense.setItemName(itemName);
        expense.setAmount(amount);
        expense.setExpenseTime(java.time.LocalDateTime.now());
        expenseMapper.insert(expense);
    }

    public List<Expense> getExpensesByItineraryId(Long itineraryId) {
        return expenseMapper.findByItineraryId(itineraryId);
    }

    public BigDecimal getTotalExpenses(Long itineraryId) {
        BigDecimal total = expenseMapper.sumAmountByItineraryId(itineraryId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Map<String, BigDecimal> getBudgetComparison(Long itineraryId) {
        Budget budget = budgetMapper.findByItineraryId(itineraryId);
        List<Expense> expenses = expenseMapper.findByItineraryId(itineraryId);

        Map<String, BigDecimal> comparison = new HashMap<>();

        if (budget != null) {
            comparison.put("totalBudget", budget.getTotalBudget());
            comparison.put("transportBudget", budget.getTransportBudget());
            comparison.put("hotelBudget", budget.getHotelBudget());
            comparison.put("foodBudget", budget.getFoodBudget());
            comparison.put("ticketBudget", budget.getTicketBudget());
            comparison.put("otherBudget", budget.getOtherBudget());
        }

        BigDecimal transportActual = BigDecimal.ZERO;
        BigDecimal hotelActual = BigDecimal.ZERO;
        BigDecimal foodActual = BigDecimal.ZERO;
        BigDecimal ticketActual = BigDecimal.ZERO;
        BigDecimal otherActual = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            switch (expense.getCategory()) {
                case "transport": transportActual = transportActual.add(expense.getAmount()); break;
                case "hotel": hotelActual = hotelActual.add(expense.getAmount()); break;
                case "food": foodActual = foodActual.add(expense.getAmount()); break;
                case "ticket": ticketActual = ticketActual.add(expense.getAmount()); break;
                default: otherActual = otherActual.add(expense.getAmount()); break;
            }
        }

        comparison.put("transportActual", transportActual);
        comparison.put("hotelActual", hotelActual);
        comparison.put("foodActual", foodActual);
        comparison.put("ticketActual", ticketActual);
        comparison.put("otherActual", otherActual);
        comparison.put("totalActual", transportActual.add(hotelActual).add(foodActual).add(ticketActual).add(otherActual));

        return comparison;
    }

    public void updateBudget(Long itineraryId, BigDecimal totalBudget, BigDecimal transportBudget,
                             BigDecimal hotelBudget, BigDecimal foodBudget, BigDecimal ticketBudget,
                             BigDecimal otherBudget) {
        Budget budget = budgetMapper.findByItineraryId(itineraryId);
        if (budget == null) {
            throw new RuntimeException("预算不存在");
        }
        BigDecimal newTotalBudget = transportBudget.add(hotelBudget).add(foodBudget).add(ticketBudget).add(otherBudget);
        budget.setTotalBudget(newTotalBudget);
        budget.setTransportBudget(transportBudget);
        budget.setHotelBudget(hotelBudget);
        budget.setFoodBudget(foodBudget);
        budget.setTicketBudget(ticketBudget);
        budget.setOtherBudget(otherBudget);
        budgetMapper.update(budget);
    }
}
