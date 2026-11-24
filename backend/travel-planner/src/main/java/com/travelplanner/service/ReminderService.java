package com.travelplanner.service;

import com.travelplanner.entity.Reminder;
import com.travelplanner.mapper.ReminderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private ReminderMapper reminderMapper;

    public Reminder findById(Long id) {
        return reminderMapper.findById(id);
    }

    public List<Reminder> findByItineraryId(Long itineraryId) {
        return reminderMapper.findByItineraryId(itineraryId);
    }

    public List<Reminder> findUpcoming(int hours) {
        return reminderMapper.findUpcoming(hours);
    }

    public Reminder create(Reminder reminder) {
        reminder.setStatus(0);
        reminderMapper.insert(reminder);
        return reminder;
    }

    public Reminder update(Reminder reminder) {
        reminderMapper.update(reminder);
        return reminderMapper.findById(reminder.getId());
    }

    public void updateStatus(Long id, Integer status) {
        reminderMapper.updateStatus(id, status);
    }

    public void delete(Long id) {
        reminderMapper.deleteById(id);
    }
}
