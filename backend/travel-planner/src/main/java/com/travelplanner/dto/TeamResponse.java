package com.travelplanner.dto;

import com.travelplanner.entity.Team;
import com.travelplanner.entity.TeamMember;
import lombok.Data;
import java.util.List;

@Data
public class TeamResponse {
    private Team team;
    private List<TeamMember> members;
    private String userRole;  // 当前用户在此团队中的角色
    private int memberCount;
}
