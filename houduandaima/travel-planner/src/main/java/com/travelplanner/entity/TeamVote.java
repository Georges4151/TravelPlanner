package com.travelplanner.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeamVote {
    private Long id;
    private Long teamId;
    private String title;
    private String description;
    private Boolean multipleChoice;
    private LocalDateTime endTime;
    private Long creatorId;
    private String status;  // active/closed
    private LocalDateTime createTime;
    
    // 扩展字段
    private List<TeamVoteOption> options;
    private String creatorName;
    private Boolean hasVoted;  // 当前用户是否已投票
    private Long totalVotes;
}
