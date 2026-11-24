package com.travelplanner.mapper;

import com.travelplanner.entity.Team;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TeamMapper {
    
    @Insert("INSERT INTO team (name, invite_code, creator_id, itinerary_id, description) " +
            "VALUES (#{name}, #{inviteCode}, #{creatorId}, #{itineraryId}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Team team);
    
    @Select("SELECT * FROM team WHERE id = #{id}")
    Team findById(@Param("id") Long id);
    
    @Select("SELECT * FROM team WHERE invite_code = #{inviteCode}")
    Team findByInviteCode(@Param("inviteCode") String inviteCode);
    
    @Select("SELECT * FROM team WHERE creator_id = #{userId} OR id IN " +
            "(SELECT team_id FROM team_member WHERE user_id = #{userId})")
    java.util.List<Team> findByUserId(@Param("userId") Long userId);
    
    @Update("UPDATE team SET name = #{name}, description = #{description} WHERE id = #{id}")
    void update(Team team);
    
    @Delete("DELETE FROM team WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
    
    @Select("SELECT COUNT(*) FROM team_member WHERE team_id = #{teamId}")
    int countMembers(@Param("teamId") Long teamId);
}
