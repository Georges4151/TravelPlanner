package com.travelplanner.mapper;

import com.travelplanner.entity.TeamMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamMessageMapper {
    
    @Insert("INSERT INTO team_message (team_id, user_id, content, message_type) " +
            "VALUES (#{teamId}, #{userId}, #{content}, #{messageType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TeamMessage message);
    
    @Select("SELECT * FROM team_message WHERE team_id = #{teamId} ORDER BY create_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<TeamMessage> findByTeamId(@Param("teamId") Long teamId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT * FROM team_message WHERE team_id = #{teamId} AND create_time > #{since} ORDER BY create_time ASC")
    List<TeamMessage> findByTeamIdSince(@Param("teamId") Long teamId, @Param("since") java.time.LocalDateTime since);
    
    @Select("SELECT * FROM team_message WHERE team_id = #{teamId} ORDER BY create_time DESC LIMIT 1")
    TeamMessage findLatestByTeamId(@Param("teamId") Long teamId);
}
