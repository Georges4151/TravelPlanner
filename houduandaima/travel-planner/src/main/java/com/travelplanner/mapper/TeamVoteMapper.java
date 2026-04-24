package com.travelplanner.mapper;

import com.travelplanner.entity.TeamVote;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamVoteMapper {
    
    @Insert("INSERT INTO team_vote (team_id, title, description, multiple_choice, end_time, creator_id, status) " +
            "VALUES (#{teamId}, #{title}, #{description}, #{multipleChoice}, #{endTime}, #{creatorId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TeamVote vote);
    
    @Select("SELECT * FROM team_vote WHERE team_id = #{teamId} ORDER BY create_time DESC")
    List<TeamVote> findByTeamId(@Param("teamId") Long teamId);
    
    @Select("SELECT * FROM team_vote WHERE id = #{id}")
    TeamVote findById(@Param("id") Long id);
    
    @Update("UPDATE team_vote SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Delete("DELETE FROM team_vote WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
}
