package com.travelplanner.dto;

import lombok.Data;
import java.util.List;

@Data
public class VoteRequest {
    private Long voteId;
    private Long userId;
    private List<Long> optionIds;  // 用户选择的选项ID列表
}
