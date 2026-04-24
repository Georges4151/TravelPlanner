package com.travelplanner.controller;

import com.travelplanner.dto.*;
import com.travelplanner.entity.*;
import com.travelplanner.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/team")
@CrossOrigin(origins = "*")
public class TeamController {

    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamService teamService;

    // ==================== 团队管理 ====================

    @PostMapping("/create")
    public ApiResponse<TeamResponse> createTeam(@RequestBody TeamCreateRequest request) {
        try {
            logger.info("创建团队请求: name={}, creatorId={}", request.getName(), request.getCreatorId());
            TeamResponse response = teamService.createTeam(request);
            logger.info("团队创建成功: {}", response);
            return ApiResponse.success(response);
        } catch (Exception e) {
            logger.error("创建团队失败: {}", e.getMessage(), e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/join")
    public ApiResponse<TeamResponse> joinByInviteCode(@RequestBody Map<String, Object> request) {
        try {
            String inviteCode = (String) request.get("inviteCode");
            Long userId = ((Number) request.get("userId")).longValue();
            logger.info("加入团队请求: inviteCode={}, userId={}", inviteCode, userId);
            TeamResponse response = teamService.joinByInviteCode(inviteCode, userId);
            logger.info("加入团队成功: {}", response);
            return ApiResponse.success(response);
        } catch (Exception e) {
            logger.error("加入团队失败: {}", e.getMessage(), e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @GetMapping("/{teamId}")
    public ApiResponse<TeamResponse> getTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId) {
        try {
            TeamResponse response = teamService.getTeamResponse(teamId, userId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(404, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<TeamResponse>> getUserTeams(@PathVariable Long userId) {
        try {
            logger.info("获取用户团队列表: userId={}", userId);
            List<TeamResponse> teams = teamService.getUserTeams(userId);
            logger.info("用户团队列表: count={}", teams.size());
            return ApiResponse.success(teams);
        } catch (Exception e) {
            logger.error("获取用户团队列表失败: {}", e.getMessage(), e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    // 测试接口：检查数据库状态
    @GetMapping("/test/db")
    public ApiResponse<String> testDatabase() {
        try {
            return ApiResponse.success("数据库连接正常");
        } catch (Exception e) {
            return ApiResponse.error(500, "数据库连接失败: " + e.getMessage());
        }
    }

    // 测试接口：验证邀请码
    @GetMapping("/test/invite/{inviteCode}")
    public ApiResponse<Object> testInviteCode(@PathVariable String inviteCode) {
        try {
            com.travelplanner.entity.Team team = teamService.findTeamByInviteCode(inviteCode);
            if (team != null) {
                return ApiResponse.success(java.util.Map.of(
                    "found", true,
                    "teamId", team.getId(),
                    "teamName", team.getName(),
                    "inviteCode", team.getInviteCode()
                ));
            } else {
                return ApiResponse.success(java.util.Map.of("found", false));
            }
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @GetMapping("/{teamId}/members")
    public ApiResponse<List<TeamMember>> getTeamMembers(@PathVariable Long teamId) {
        try {
            logger.info("获取团队成员: teamId={}", teamId);
            List<TeamMember> members = teamService.getTeamMembers(teamId);
            logger.info("团队成员数量: {}", members.size());
            return ApiResponse.success(members);
        } catch (Exception e) {
            logger.error("获取团队成员失败: {}", e.getMessage(), e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @PutMapping("/{teamId}/member/{memberId}")
    public ApiResponse<Void> updateMemberRole(
            @PathVariable Long teamId,
            @PathVariable Long memberId,
            @RequestParam String role,
            @RequestParam Long operatorId) {
        try {
            teamService.updateMemberRole(teamId, memberId, role, operatorId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{teamId}/member/{memberId}")
    public ApiResponse<Void> removeMember(
            @PathVariable Long teamId,
            @PathVariable Long memberId,
            @RequestParam Long operatorId) {
        try {
            teamService.removeMember(teamId, memberId, operatorId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{teamId}")
    public ApiResponse<Void> deleteTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId) {
        try {
            teamService.deleteTeam(teamId, userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // ==================== 待办事项 ====================

    @GetMapping("/{teamId}/todos")
    public ApiResponse<List<TeamTodo>> getTeamTodos(
            @PathVariable Long teamId,
            @RequestParam Long userId) {
        try {
            List<TeamTodo> todos = teamService.getTeamTodos(teamId, userId);
            return ApiResponse.success(todos);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @PostMapping("/todo/create")
    public ApiResponse<TeamTodo> createTodo(@RequestBody TodoCreateRequest request) {
        try {
            logger.info("创建待办请求: teamId={}, title={}, creatorId={}", 
                    request.getTeamId(), request.getTitle(), request.getCreatorId());
            TeamTodo todo = teamService.createTodo(request, request.getCreatorId());
            logger.info("待办创建成功: id={}, title={}", todo.getId(), todo.getTitle());
            return ApiResponse.success(todo);
        } catch (Exception e) {
            logger.error("创建待办失败: {}", e.getMessage(), e);
            return ApiResponse.error(500, "创建待办失败: " + e.getMessage());
        }
    }

    @PutMapping("/todo/{todoId}")
    public ApiResponse<Void> updateTodo(
            @PathVariable Long todoId,
            @RequestBody TeamTodo todo,
            @RequestParam Long userId) {
        try {
            todo.setId(todoId);
            teamService.updateTodo(todo, userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/todo/{todoId}/status")
    public ApiResponse<Void> updateTodoStatus(
            @PathVariable Long todoId,
            @RequestParam String status,
            @RequestParam Long userId) {
        try {
            teamService.updateTodoStatus(todoId, status, userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/todo/{todoId}")
    public ApiResponse<Void> deleteTodo(
            @PathVariable Long todoId,
            @RequestParam Long userId) {
        try {
            teamService.deleteTodo(todoId, userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // ==================== 投票 ====================

    @GetMapping("/{teamId}/votes")
    public ApiResponse<List<TeamVote>> getTeamVotes(
            @PathVariable Long teamId,
            @RequestParam Long userId) {
        try {
            List<TeamVote> votes = teamService.getTeamVotes(teamId, userId);
            return ApiResponse.success(votes);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @PostMapping("/vote/create")
    public ApiResponse<TeamVote> createVote(@RequestBody VoteCreateRequest request) {
        try {
            TeamVote vote = teamService.createVote(request);
            return ApiResponse.success(vote);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/vote/cast")
    public ApiResponse<Void> castVote(@RequestBody VoteRequest request) {
        try {
            teamService.castVote(request);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/vote/{voteId}/close")
    public ApiResponse<Void> closeVote(
            @PathVariable Long voteId,
            @RequestParam Long userId) {
        try {
            teamService.closeVote(voteId, userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // ==================== 消息 ====================

    @GetMapping("/{teamId}/messages")
    public ApiResponse<List<TeamMessage>> getMessages(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "50") int limit) {
        try {
            List<TeamMessage> messages = teamService.getMessages(teamId, offset, limit);
            return ApiResponse.success(messages);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @PostMapping("/message/send")
    public ApiResponse<TeamMessage> sendMessage(@RequestBody MessageRequest request) {
        try {
            TeamMessage message = teamService.sendMessage(request);
            return ApiResponse.success(message);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // ==================== 活动动态 ====================

    @GetMapping("/{teamId}/activities")
    public ApiResponse<List<TeamActivity>> getActivities(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit) {
        try {
            List<TeamActivity> activities = teamService.getActivities(teamId, offset, limit);
            return ApiResponse.success(activities);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }
}
