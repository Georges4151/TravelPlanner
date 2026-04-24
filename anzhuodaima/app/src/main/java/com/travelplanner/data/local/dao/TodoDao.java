package com.travelplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.travelplanner.data.local.entity.TodoEntity;

import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM team_todos WHERE id = :id")
    TodoEntity getById(Long id);

    @Query("SELECT * FROM team_todos WHERE teamId = :teamId ORDER BY createTime DESC")
    List<TodoEntity> getByTeamId(Long teamId);

    @Query("SELECT * FROM team_todos WHERE teamId = :teamId AND assigneeId = :assigneeId ORDER BY createTime DESC")
    List<TodoEntity> getByTeamIdAndAssignee(Long teamId, Long assigneeId);

    @Query("SELECT * FROM team_todos WHERE teamId = :teamId AND status = :status ORDER BY createTime DESC")
    List<TodoEntity> getByTeamIdAndStatus(Long teamId, String status);

    @Query("SELECT * FROM team_todos WHERE teamId = :teamId ORDER BY " +
            "CASE priority WHEN 'high' THEN 1 WHEN 'medium' THEN 2 WHEN 'low' THEN 3 ELSE 4 END, " +
            "createTime DESC")
    List<TodoEntity> getByTeamIdSortedByPriority(Long teamId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TodoEntity todo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TodoEntity> todos);

    @Update
    void update(TodoEntity todo);

    @Delete
    void delete(TodoEntity todo);

    @Query("DELETE FROM team_todos WHERE id = :id")
    void deleteById(Long id);

    @Query("DELETE FROM team_todos WHERE teamId = :teamId")
    void deleteByTeamId(Long teamId);

    @Query("UPDATE team_todos SET status = :status, updateTime = :updateTime WHERE id = :id")
    void updateStatus(Long id, String status, Long updateTime);

    @Query("UPDATE team_todos SET assigneeId = :assigneeId, assigneeName = :assigneeName, updateTime = :updateTime WHERE id = :id")
    void updateAssignee(Long id, Long assigneeId, String assigneeName, Long updateTime);

    @Query("UPDATE team_todos SET priority = :priority, updateTime = :updateTime WHERE id = :id")
    void updatePriority(Long id, String priority, Long updateTime);

    @Query("UPDATE team_todos SET dueDate = :dueDate, updateTime = :updateTime WHERE id = :id")
    void updateDueDate(Long id, String dueDate, Long updateTime);
}
