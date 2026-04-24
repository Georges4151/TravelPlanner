package com.travelplanner.entity;

import lombok.Data;

@Data
public class TeamVoteOption {
    private Long id;
    private Long voteId;
    private String optionText;
    private Integer voteCount;
    
    // 扩展字段
    private Boolean hasVoted;  // 当前用户是否选择了此选项
}
