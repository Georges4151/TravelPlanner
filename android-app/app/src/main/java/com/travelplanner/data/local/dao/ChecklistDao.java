package com.travelplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.ChecklistItemEntity;

import java.util.List;

@Dao
public interface ChecklistDao {
    @Query("SELECT * FROM checklist_items WHERE id = :id")
    ChecklistItemEntity getById(Long id);

    @Query("SELECT * FROM checklist_items WHERE itineraryId = :itineraryId ORDER BY sortOrder ASC")
    List<ChecklistItemEntity> getByItineraryId(Long itineraryId);

    @Query("SELECT * FROM checklist_items WHERE itineraryId = :itineraryId AND category = :category ORDER BY sortOrder ASC")
    List<ChecklistItemEntity> getByCategory(Long itineraryId, String category);

    @Query("SELECT DISTINCT category FROM checklist_items WHERE itineraryId = :itineraryId")
    List<String> getCategories(Long itineraryId);

    @Query("SELECT COUNT(*) FROM checklist_items WHERE itineraryId = :itineraryId")
    int getCountByItineraryId(Long itineraryId);

    @Query("SELECT COUNT(*) FROM checklist_items WHERE itineraryId = :itineraryId AND isChecked = 1")
    int getCheckedCount(Long itineraryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ChecklistItemEntity item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ChecklistItemEntity> items);

    @Update
    void update(ChecklistItemEntity item);

    @Delete
    void delete(ChecklistItemEntity item);

    @Query("DELETE FROM checklist_items WHERE itineraryId = :itineraryId")
    void deleteByItineraryId(Long itineraryId);

    @Query("UPDATE checklist_items SET isChecked = :checked WHERE id = :id")
    void updateChecked(Long id, boolean checked);
}
