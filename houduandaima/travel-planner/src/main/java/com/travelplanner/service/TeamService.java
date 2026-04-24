package com.travelplanner.service;

import com.travelplanner.dto.*;
import com.travelplanner.entity.*;
import com.travelplanner.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamMemberMapper teamMemberMapper;

    @Autowired
    private TeamTodoMapper teamTodoMapper;

    @Autowired
    private TeamVoteMapper teamVoteMapper;

    @Autowired
    private TeamVoteOptionMapper teamVoteOptionMapper;

    @Autowired
    private TeamVoteRecordMapper teamVoteRecordMapper;

    @Autowired
    private TeamMessageMapper teamMessageMapper;

    @Autowired
    private TeamActivityMapper teamActivityMapper;

    @Autowired
    private UserMapper userMapper;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 创建团队
    @Transactional
    public TeamResponse createTeam(TeamCreateRequest request) {
        Team team = new Team();
        team.setName(request.getName());
        team.setInviteCode(generateInviteCode());
        team.setCreatorId(request.getCreatorId());
        team.setItineraryId(request.getItineraryId());
        team.setDescription(request.getDescription());

        teamMapper.insert(team);

        // 创建者自动成为成员
        User creator = userMapper.findById(request.getCreatorId());
        TeamMember member = new TeamMember();
        member.setTeamId(team.getId());
        member.setUserId(request.getCreatorId());
        member.setRole("owner");
        member.setNickname(creator != null ? creator.getUsername() : "未知用户");
        member.setAvatar(creator != null ? creator.getAvatar() : null);
        teamMemberMapper.insert(member);

        // 记录活动
        recordActivity(team.getId(), request.getCreatorId(), "team_create", "team", team.getId(), 
                creator != null ? creator.getUsername() + " 创建了团队" : "创建了团队");

        return getTeamResponse(team.getId(), request.getCreatorId());
    }

    // 通过邀请码加入团队
    @Transactional
    public TeamResponse joinByInviteCode(String inviteCode, Long userId) {
        Team team = teamMapper.findByInviteCode(inviteCode);
        if (team == null) {
            throw new RuntimeException("邀请码无效");
        }

        TeamMember existing = teamMemberMapper.findByTeamAndUser(team.getId(), userId);
        if (existing != null) {
            // 已存在成员，直接返回团队信息
            return getTeamResponse(team.getId(), userId);
        }

        User user = userMapper.findById(userId);
        TeamMember member = new TeamMember();
        member.setTeamId(team.getId());
        member.setUserId(userId);
        member.setRole("viewer");  // 默认viewer权限
        member.setNickname(user != null ? user.getUsername() : "未知用户");
        member.setAvatar(user != null ? user.getAvatar() : null);
        teamMemberMapper.insert(member);

        // 记录活动
        recordActivity(team.getId(), userId, "join", "team", team.getId(), 
                user != null ? user.getUsername() + " 加入了团队" : "加入了团队");

        return getTeamResponse(team.getId(), userId);
    }

    // 根据邀请码查找团队（供测试用）
    public Team findTeamByInviteCode(String inviteCode) {
        return teamMapper.findByInviteCode(inviteCode);
    }

    // 获取团队详情
    public TeamResponse getTeamResponse(Long teamId, Long currentUserId) {
        Team team = teamMapper.findById(teamId);
        if (team == null) {
            throw new RuntimeException("团队不存在");
        }

        List<TeamMember> members = teamMemberMapper.findByTeamId(teamId);
        for (TeamMember m : members) {
            User user = userMapper.findById(m.getUserId());
            if (user != null) {
                m.setUsername(user.getUsername());
            }
        }

        TeamResponse response = new TeamResponse();
        response.setTeam(team);
        response.setMembers(members);
        response.setMemberCount(members.size());

        // 获取当前用户角色
        TeamMember currentMember = teamMemberMapper.findByTeamAndUser(teamId, currentUserId);
        response.setUserRole(currentMember != null ? currentMember.getRole() : null);

        return response;
    }

    // 获取用户所有团队
    public List<TeamResponse> getUserTeams(Long userId) {
        List<Team> teams = teamMapper.findByUserId(userId);
        return teams.stream()
                .map(t -> getTeamResponse(t.getId(), userId))
                .collect(Collectors.toList());
    }

    // 获取团队成员
    public List<TeamMember> getTeamMembers(Long teamId) {
        List<TeamMember> members = teamMemberMapper.findByTeamId(teamId);
        for (TeamMember m : members) {
            User user = userMapper.findById(m.getUserId());
            if (user != null) {
                m.setUsername(user.getUsername());
            }
        }
        return members;
    }

    // 更新成员角色
    @Transactional
    public void updateMemberRole(Long teamId, Long memberId, String newRole, Long operatorId) {
        TeamMember operator = teamMemberMapper.findByTeamAndUser(teamId, operatorId);
        if (operator == null || !"owner".equals(operator.getRole())) {
            throw new RuntimeException("只有团队所有者可以修改成员角色");
        }

        TeamMember member = teamMemberMapper.findByTeamAndUser(teamId, memberId);
        if (member == null) {
            throw new RuntimeException("成员不存在");
        }
        member.setRole(newRole);
        teamMemberMapper.update(member);

        User user = userMapper.findById(memberId);
        recordActivity(teamId, operatorId, "role_change", "member", memberId,
                user != null ? user.getUsername() + " 的角色被更新为 " + newRole : "更新了成员角色");
    }

    // 移除成员
    @Transactional
    public void removeMember(Long teamId, Long memberId, Long operatorId) {
        TeamMember operator = teamMemberMapper.findByTeamAndUser(teamId, operatorId);
        TeamMember member = teamMemberMapper.findByTeamAndUser(teamId, memberId);

        if (member == null) {
            throw new RuntimeException("成员不存在");
        }

        // 只有owner可以移除成员，或者成员自己可以退出
        boolean isOwner = operator != null && "owner".equals(operator.getRole());
        boolean isSelf = memberId.equals(operatorId);

        if (!isOwner && !isSelf) {
            throw new RuntimeException("无权移除此成员");
        }

        if ("owner".equals(member.getRole()) && !memberId.equals(operatorId)) {
            throw new RuntimeException("无法移除团队所有者");
        }

        User user = userMapper.findById(memberId);
        teamMemberMapper.deleteByTeamAndUser(teamId, memberId);

        recordActivity(teamId, operatorId, "leave", "member", memberId,
                user != null ? user.getUsername() + " 离开了团队" : "成员离开了团队");
    }

    // ==================== 待办事项 ====================

    public List<TeamTodo> getTeamTodos(Long teamId, Long currentUserId) {
        List<TeamTodo> todos = teamTodoMapper.findByTeamId(teamId);
        for (TeamTodo todo : todos) {
            User assignee = userMapper.findById(todo.getAssigneeId());
            User creator = userMapper.findById(todo.getCreatorId());
            todo.setAssigneeName(assignee != null ? assignee.getUsername() : null);
            todo.setCreatorName(creator != null ? creator.getUsername() : null);
        }
        return todos;
    }

    @Transactional
    public TeamTodo createTodo(TodoCreateRequest request, Long creatorId) {
        TeamTodo todo = new TeamTodo();
        todo.setTeamId(request.getTeamId());
        todo.setTitle(request.getTitle());
        todo.setContent(request.getContent());
        todo.setPriority(request.getPriority() != null ? request.getPriority() : "medium");
        todo.setAssigneeId(request.getAssigneeId());
        todo.setStatus("pending");
        todo.setCreatorId(creatorId);
        
        // 解析日期，支持 "yyyy-MM-dd" 和 "yyyy-MM-dd HH:mm:ss" 两种格式
        if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
            try {
                if (request.getDueDate().length() <= 10) {
                    // 格式: yyyy-MM-dd
                    todo.setDueDate(LocalDateTime.parse(request.getDueDate() + " 00:00:00", DATE_FORMAT));
                } else {
                    // 格式: yyyy-MM-dd HH:mm:ss
                    todo.setDueDate(LocalDateTime.parse(request.getDueDate(), DATE_FORMAT));
                }
            } catch (Exception e) {
                System.out.println("日期解析失败，使用默认值: " + e.getMessage());
            }
        }

        // 检查团队是否存在
        Team team = teamMapper.findById(request.getTeamId());
        if (team == null) {
            throw new RuntimeException("团队不存在，teamId: " + request.getTeamId());
        }

        teamTodoMapper.insert(todo);
        System.out.println("待办已插入数据库，id=" + todo.getId());

        User creator = userMapper.findById(creatorId);
        recordActivity(request.getTeamId(), creatorId, "todo_create", "todo", todo.getId(),
                creator != null ? creator.getUsername() + " 创建了待办：" + todo.getTitle() : "创建了待办事项");

        return todo;
    }

    @Transactional
    public void updateTodo(TeamTodo todo, Long userId) {
        teamTodoMapper.update(todo);

        User user = userMapper.findById(userId);
        recordActivity(todo.getTeamId(), userId, "todo_update", "todo", todo.getId(),
                user != null ? user.getUsername() + " 更新了待办：" + todo.getTitle() : "更新了待办事项");
    }

    @Transactional
    public void updateTodoStatus(Long todoId, String status, Long userId) {
        TeamTodo todo = teamTodoMapper.findById(todoId);
        if (todo == null) {
            throw new RuntimeException("待办不存在");
        }

        teamTodoMapper.updateStatus(todoId, status);

        User user = userMapper.findById(userId);
        recordActivity(todo.getTeamId(), userId, "todo_status", "todo", todoId,
                user != null ? user.getUsername() + " 将「" + todo.getTitle() + "」标记为" + status : "更新了待办状态");
    }

    @Transactional
    public void deleteTodo(Long todoId, Long userId) {
        TeamTodo todo = teamTodoMapper.findById(todoId);
        if (todo != null) {
            teamTodoMapper.deleteById(todoId);
            User user = userMapper.findById(userId);
            recordActivity(todo.getTeamId(), userId, "todo_delete", "todo", todoId,
                    user != null ? user.getUsername() + " 删除了待办：" + todo.getTitle() : "删除了待办事项");
        }
    }

    // ==================== 投票 ====================

    public List<TeamVote> getTeamVotes(Long teamId, Long currentUserId) {
        List<TeamVote> votes = teamVoteMapper.findByTeamId(teamId);
        for (TeamVote vote : votes) {
            List<TeamVoteOption> options = teamVoteOptionMapper.findByVoteId(vote.getId());
            for (TeamVoteOption opt : options) {
                List<Long> votedOptions = teamVoteRecordMapper.findByVoteAndUser(vote.getId(), currentUserId);
                opt.setHasVoted(votedOptions.contains(opt.getId()));
            }
            vote.setOptions(options);
            vote.setHasVoted(teamVoteRecordMapper.countByVoteAndUser(vote.getId(), currentUserId) > 0);
            vote.setTotalVotes(options.stream().mapToLong(TeamVoteOption::getVoteCount).sum());

            User creator = userMapper.findById(vote.getCreatorId());
            vote.setCreatorName(creator != null ? creator.getUsername() : null);
        }
        return votes;
    }

    @Transactional
    public TeamVote createVote(VoteCreateRequest request) {
        TeamVote vote = new TeamVote();
        vote.setTeamId(request.getTeamId());
        vote.setTitle(request.getTitle());
        vote.setDescription(request.getDescription());
        vote.setMultipleChoice(request.getMultipleChoice() != null ? request.getMultipleChoice() : false);
        vote.setCreatorId(request.getCreatorId());
        vote.setStatus("active");
        if (request.getEndTime() != null) {
            vote.setEndTime(LocalDateTime.parse(request.getEndTime(), DATE_FORMAT));
        }

        teamVoteMapper.insert(vote);

        // 插入选项
        List<TeamVoteOption> options = request.getOptions().stream()
                .map(text -> {
                    TeamVoteOption opt = new TeamVoteOption();
                    opt.setVoteId(vote.getId());
                    opt.setOptionText(text);
                    opt.setVoteCount(0);
                    return opt;
                })
                .collect(Collectors.toList());
        teamVoteOptionMapper.insertBatch(vote.getId(), options);

        User creator = userMapper.findById(request.getCreatorId());
        recordActivity(request.getTeamId(), request.getCreatorId(), "vote_create", "vote", vote.getId(),
                creator != null ? creator.getUsername() + " 创建了投票：" + vote.getTitle() : "创建了投票");

        return getTeamVotes(vote.getTeamId(), request.getCreatorId()).stream()
                .filter(v -> v.getId().equals(vote.getId()))
                .findFirst()
                .orElse(vote);
    }

    @Transactional
    public void castVote(VoteRequest request) {
        // 检查用户是否已经投过票
        int existingVotes = teamVoteRecordMapper.countByVoteAndUser(request.getVoteId(), request.getUserId());
        if (existingVotes > 0) {
            throw new RuntimeException("您已经投过票了，无法再次投票");
        }

        // 投票
        for (Long optionId : request.getOptionIds()) {
            teamVoteRecordMapper.insert(request.getVoteId(), optionId, request.getUserId());
            teamVoteOptionMapper.incrementVoteCount(optionId);
        }

        TeamVote vote = teamVoteMapper.findById(request.getVoteId());
        User user = userMapper.findById(request.getUserId());
        recordActivity(vote.getTeamId(), request.getUserId(), "vote_cast", "vote", vote.getId(),
                user != null ? user.getUsername() + " 参与了投票：" + vote.getTitle() : "参与了投票");
    }

    @Transactional
    public void closeVote(Long voteId, Long userId) {
        TeamVote vote = teamVoteMapper.findById(voteId);
        if (vote == null) {
            throw new RuntimeException("投票不存在");
        }

        teamVoteMapper.updateStatus(voteId, "closed");

        User user = userMapper.findById(userId);
        recordActivity(vote.getTeamId(), userId, "vote_close", "vote", voteId,
                user != null ? user.getUsername() + " 关闭了投票：" + vote.getTitle() : "关闭了投票");
    }

    // ==================== 消息 ====================

    public List<TeamMessage> getMessages(Long teamId, int offset, int limit) {
        List<TeamMessage> messages = teamMessageMapper.findByTeamId(teamId, offset, limit);
        for (TeamMessage msg : messages) {
            User user = userMapper.findById(msg.getUserId());
            if (user != null) {
                msg.setUsername(user.getUsername());
                msg.setNickname(user.getUsername());
                msg.setAvatar(user.getAvatar());
            }
        }
        return messages;
    }

    public List<TeamMessage> getNewMessages(Long teamId, Long lastMessageId) {
        TeamMessage lastMessage = teamMessageMapper.findLatestByTeamId(teamId);
        if (lastMessage == null || lastMessageId.equals(lastMessage.getId())) {
            return List.of();
        }
        return teamMessageMapper.findByTeamIdSince(teamId, lastMessage.getCreateTime());
    }

    @Transactional
    public TeamMessage sendMessage(MessageRequest request) {
        TeamMessage message = new TeamMessage();
        message.setTeamId(request.getTeamId());
        message.setUserId(request.getUserId());
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType() != null ? request.getMessageType() : "text");

        teamMessageMapper.insert(message);

        User user = userMapper.findById(request.getUserId());
        message.setUsername(user != null ? user.getUsername() : null);
        message.setNickname(user != null ? user.getUsername() : null);
        message.setAvatar(user != null ? user.getAvatar() : null);

        return message;
    }

    // ==================== 活动动态 ====================

    public List<TeamActivity> getActivities(Long teamId, int offset, int limit) {
        List<TeamActivity> activities = teamActivityMapper.findByTeamIdWithPage(teamId, offset, limit);
        for (TeamActivity activity : activities) {
            User user = userMapper.findById(activity.getUserId());
            if (user != null) {
                activity.setUsername(user.getUsername());
                activity.setNickname(user.getUsername());
            }
        }
        return activities;
    }

    // ==================== 辅助方法 ====================

    private String generateInviteCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void recordActivity(Long teamId, Long userId, String actionType, String targetType, Long targetId, String content) {
        TeamActivity activity = new TeamActivity();
        activity.setTeamId(teamId);
        activity.setUserId(userId);
        activity.setActionType(actionType);
        activity.setTargetType(targetType);
        activity.setTargetId(targetId);
        activity.setContent(content);
        teamActivityMapper.insert(activity);
    }

    // 删除团队
    @Transactional
    public void deleteTeam(Long teamId, Long userId) {
        Team team = teamMapper.findById(teamId);
        if (team == null) {
            throw new RuntimeException("团队不存在");
        }

        if (!team.getCreatorId().equals(userId)) {
            throw new RuntimeException("只有团队创建者可以删除团队");
        }

        // 删除所有相关数据
        teamTodoMapper.deleteByTeamId(teamId);
        List<TeamVote> votes = teamVoteMapper.findByTeamId(teamId);
        for (TeamVote vote : votes) {
            teamVoteOptionMapper.deleteByVoteId(vote.getId());
        }
        teamVoteMapper.deleteById(teamId);
        // 成员和活动会在级联删除时一起删除（如果设置了外键）

        teamMapper.deleteById(teamId);
    }
}
