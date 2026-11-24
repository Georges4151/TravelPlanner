package com.travelplanner.mapper;

import com.travelplanner.entity.TeamVoteOption;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamVoteOptionMapper {
    
    @Insert("INSERT INTO team_vote_option (vote_id, option_text) VALUES (#{voteId}, #{optionText})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TeamVoteOption option);
    
    @Insert("<script>" +
            "INSERT INTO team_vote_option (vote_id, option_text) VALUES " +
            "<foreach collection='options' item='opt' separator=','>" +
            "(#{voteId}, #{opt.optionText})" +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("voteId") Long voteId, @Param("options") List<TeamVoteOption> options);
    
    @Select("SELECT * FROM team_vote_option WHERE vote_id = #{voteId}")
    List<TeamVoteOption> findByVoteId(@Param("voteId") Long voteId);
    
    @Update("UPDATE team_vote_option SET vote_count = vote_count + 1 WHERE id = #{id}")
    void incrementVoteCount(@Param("id") Long id);
    
    @Delete("DELETE FROM team_vote_option WHERE vote_id = #{voteId}")
    void deleteByVoteId(@Param("voteId") Long voteId);
}
