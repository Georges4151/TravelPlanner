package com.travelplanner.mapper;

import com.travelplanner.entity.TeamTodo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamTodoMapper {
    
    @Insert("INSERT INTO team_todo (team_id, title, content, priority, assignee_id, status, creator_id, due_date) " +
            "VALUES (#{teamId}, #{title}, #{content}, #{priority}, #{assigneeId}, #{status}, #{creatorId}, #{dueDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TeamTodo todo);
    
    @Select("SELECT * FROM team_todo WHERE team_id = #{teamId} ORDER BY " +
            "CASE priority WHEN 'high' THEN 1 WHEN 'medium' THEN 2 ELSE 3 END, create_time DESC")
    List<TeamTodo> findByTeamId(@Param("teamId") Long teamId);
    
    @Select("SELECT * FROM team_todo WHERE id = #{id}")
    TeamTodo findById(@Param("id") Long id);
    
    @Select("SELECT * FROM team_todo WHERE assignee_id = #{userId}")
    List<TeamTodo> findByAssigneeId(@Param("userId") Long userId);
    
    @Update("UPDATE team_todo SET title = #{title}, content = #{content}, priority = #{priority}, " +
            "assignee_id = #{assigneeId}, status = #{status}, due_date = #{dueDate} WHERE id = #{id}")
    void update(TeamTodo todo);
    
    @Update("UPDATE team_todo SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Delete("DELETE FROM team_todo WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
    
    @Delete("DELETE FROM team_todo WHERE team_id = #{teamId}")
    void deleteByTeamId(@Param("teamId") Long teamId);
}
