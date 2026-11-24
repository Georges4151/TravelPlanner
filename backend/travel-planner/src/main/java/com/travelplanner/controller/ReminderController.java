package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.entity.Reminder;
import com.travelplanner.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminder")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @GetMapping("/{id}")
    public ApiResponse<Reminder> getReminderDetail(@PathVariable Long id) {
        Reminder reminder = reminderService.findById(id);
        if (reminder == null) {
            return ApiResponse.error(404, "提醒不存在");
        }
        return ApiResponse.success(reminder);
    }

    @GetMapping("/itinerary/{itineraryId}")
    public ApiResponse<List<Reminder>> getRemindersByItinerary(@PathVariable Long itineraryId) {
        return ApiResponse.success(reminderService.findByItineraryId(itineraryId));
    }

    @GetMapping("/upcoming")
    public ApiResponse<List<Reminder>> getUpcomingReminders(@RequestParam(defaultValue = "24") int hours) {
        return ApiResponse.success(reminderService.findUpcoming(hours));
    }

    @PostMapping("")
    public ApiResponse<Reminder> createReminder(@RequestBody Reminder reminder) {
        try {
            Reminder created = reminderService.create(reminder);
            return ApiResponse.success(created);
        } catch (Exception e) {
            return ApiResponse.error(500, "创建提醒失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Reminder> updateReminder(@PathVariable Long id, @RequestBody Reminder reminder) {
        try {
            reminder.setId(id);
            Reminder updated = reminderService.update(reminder);
            return ApiResponse.success(updated);
        } catch (Exception e) {
            return ApiResponse.error(500, "更新提醒失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReminder(@PathVariable Long id) {
        try {
            reminderService.delete(id);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, "删除提醒失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateReminderStatus(@PathVariable Long id, @RequestParam int status) {
        try {
            reminderService.updateStatus(id, status);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(500, "更新状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch")
    public ApiResponse<List<Reminder>> createBatchReminders(@RequestBody List<Reminder> reminders) {
        try {
            List<Reminder> created = new java.util.ArrayList<>();
            for (Reminder reminder : reminders) {
                created.add(reminderService.create(reminder));
            }
            return ApiResponse.success(created);
        } catch (Exception e) {
            return ApiResponse.error(500, "批量创建提醒失败: " + e.getMessage());
        }
    }
}
