package com.travelplanner.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface TeamVoteRecordMapper {
    
    @Insert("INSERT INTO team_vote_record (vote_id, option_id, user_id) VALUES (#{voteId}, #{optionId}, #{userId})")
    void insert(@Param("voteId") Long voteId, @Param("optionId") Long optionId, @Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM team_vote_record WHERE vote_id = #{voteId} AND user_id = #{userId}")
    int countByVoteAndUser(@Param("voteId") Long voteId, @Param("userId") Long userId);
    
    @Select("SELECT option_id FROM team_vote_record WHERE vote_id = #{voteId} AND user_id = #{userId}")
    java.util.List<Long> findByVoteAndUser(@Param("voteId") Long voteId, @Param("userId") Long userId);
    
    @Delete("DELETE FROM team_vote_record WHERE vote_id = #{voteId} AND user_id = #{userId}")
    void deleteByVoteAndUser(@Param("voteId") Long voteId, @Param("userId") Long userId);
}
