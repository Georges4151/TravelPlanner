package com.travelplanner.mapper;

import com.travelplanner.entity.TeamActivity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamActivityMapper {
    
    @Insert("INSERT INTO team_activity (team_id, user_id, action_type, target_type, target_id, content) " +
            "VALUES (#{teamId}, #{userId}, #{actionType}, #{targetType}, #{targetId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TeamActivity activity);
    
    @Select("SELECT * FROM team_activity WHERE team_id = #{teamId} ORDER BY create_time DESC LIMIT #{limit}")
    List<TeamActivity> findByTeamId(@Param("teamId") Long teamId, @Param("limit") int limit);
    
    @Select("SELECT * FROM team_activity WHERE team_id = #{teamId} ORDER BY create_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<TeamActivity> findByTeamIdWithPage(@Param("teamId") Long teamId, @Param("offset") int offset, @Param("limit") int limit);
}
