package com.travelplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.ReminderEntity;

import java.util.List;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE id = :id")
    ReminderEntity getById(Long id);

    @Query("SELECT * FROM reminders WHERE itineraryId = :itineraryId ORDER BY remindTime ASC")
    List<ReminderEntity> getByItineraryId(Long itineraryId);

    @Query("SELECT * FROM reminders WHERE status = :status ORDER BY remindTime ASC")
    List<ReminderEntity> getByStatus(int status);

    @Query("SELECT * FROM reminders WHERE remindTime BETWEEN :startTime AND :endTime AND status = 0")
    List<ReminderEntity> getPendingReminders(long startTime, long endTime);

    @Query("SELECT * FROM reminders WHERE remindTime <= :currentTime AND status = 0")
    List<ReminderEntity> getTriggeredReminders(long currentTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ReminderEntity reminder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ReminderEntity> reminders);

    @Update
    void update(ReminderEntity reminder);

    @Delete
    void delete(ReminderEntity reminder);

    @Query("DELETE FROM reminders WHERE itineraryId = :itineraryId")
    void deleteByItineraryId(Long itineraryId);

    @Query("UPDATE reminders SET status = :status WHERE id = :id")
    void updateStatus(Long id, int status);
}
