package com.travelplanner.mapper;

import com.travelplanner.entity.TeamMember;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamMemberMapper {
    
    @Insert("INSERT INTO team_member (team_id, user_id, role, nickname, avatar) " +
            "VALUES (#{teamId}, #{userId}, #{role}, #{nickname}, #{avatar})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TeamMember member);
    
    @Select("SELECT * FROM team_member WHERE team_id = #{teamId}")
    List<TeamMember> findByTeamId(@Param("teamId") Long teamId);
    
    @Select("SELECT * FROM team_member WHERE team_id = #{teamId} AND user_id = #{userId}")
    TeamMember findByTeamAndUser(@Param("teamId") Long teamId, @Param("userId") Long userId);
    
    @Select("SELECT * FROM team_member WHERE user_id = #{userId}")
    List<TeamMember> findByUserId(@Param("userId") Long userId);
    
    @Update("UPDATE team_member SET role = #{role}, nickname = #{nickname} WHERE id = #{id}")
    void update(TeamMember member);
    
    @Delete("DELETE FROM team_member WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
    
    @Delete("DELETE FROM team_member WHERE team_id = #{teamId} AND user_id = #{userId}")
    void deleteByTeamAndUser(@Param("teamId") Long teamId, @Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM team_member WHERE team_id = #{teamId}")
    int countByTeamId(@Param("teamId") Long teamId);
}
