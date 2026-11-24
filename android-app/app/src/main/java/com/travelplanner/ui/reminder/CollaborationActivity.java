package com.travelplanner.ui.reminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.travelplanner.TravelPlannerApp;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.travelplanner.R;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.TeamApi;
import com.travelplanner.model.ApiResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollaborationActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private View viewMembers, viewTodos, viewVotes, viewChat, viewActivityLog;
    private RecyclerView rvMembers, rvTodos, rvVotes, rvMessages, rvActivityLog;
    private Button btnAddMember, btnAddTodo, btnAddVote;
    private Button btnShareLink, btnShareQR;
    private EditText etMessage;
    private Button btnSend;
    private Button btnCreateTeam, btnJoinTeam;
    private EditText etInviteCode;
    private LinearLayout layoutTeamButtons;

    private MemberAdapter memberAdapter;
    private TodoAdapter todoAdapter;
    private VoteAdapter voteAdapter;
    private ChatAdapter chatAdapter;
    private ActivityLogAdapter activityLogAdapter;

    private List<MemberItem> memberList = new ArrayList<>();
    private List<TodoItem> todoList = new ArrayList<>();
    private List<VoteItem> voteList = new ArrayList<>();
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private List<ActivityLogItem> activityLogs = new ArrayList<>();

    private String shareUrl;
    private String currentUser = "我";
    private Long currentUserId = 1L; // 默认用户ID，实际应从登录状态获取
    private Long currentTeamId = null;
    private String currentTeamInviteCode = null;
    private String userRole = "owner";
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable refreshRunnable;
    private TeamApi teamApi;

    public static void start(Context context) {
        Intent intent = new Intent(context, CollaborationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        teamApi = ApiClient.getTeamApi();
        
        // 从TravelPlannerApp获取当前用户ID（与登录保持一致）
        currentUserId = TravelPlannerApp.getInstance().getUserId();
        currentUser = TravelPlannerApp.getInstance().getUsername();
        
        // 检查是否有传入的teamId
        currentTeamId = getIntent().getLongExtra("teamId", -1L);
        if (currentTeamId == -1L) {
            currentTeamId = null;
        }
        
        // 从savedInstanceState恢复teamId（Activity重建时）
        if (savedInstanceState != null) {
            Long savedTeamId = savedInstanceState.getLong("currentTeamId", -1L);
            if (savedTeamId != -1L) {
                currentTeamId = savedTeamId;
            }
            String savedInviteCode = savedInstanceState.getString("currentTeamInviteCode");
            if (savedInviteCode != null) {
                currentTeamInviteCode = savedInviteCode;
            }
        }

        setupToolbar();
        initViews();
        setupTabs();
        setupRecyclerViews();
        setupListeners();
        
        // 根据是否有teamId决定显示创建/加入还是直接加载数据
        if (currentTeamId != null) {
            loadTeamData();
        } else {
            showTeamSelectionDialog();
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存团队信息，以便Activity重建时恢复
        if (currentTeamId != null) {
            outState.putLong("currentTeamId", currentTeamId);
        }
        if (currentTeamInviteCode != null) {
            outState.putString("currentTeamInviteCode", currentTeamInviteCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 刷新用户信息（可能用户在外部切换了账号）
        Long newUserId = TravelPlannerApp.getInstance().getUserId();
        String newUsername = TravelPlannerApp.getInstance().getUsername();
        
        if (!newUserId.equals(currentUserId) || !newUsername.equals(currentUser)) {
            currentUserId = newUserId;
            currentUser = newUsername;
            
            // 如果当前已有团队，重新加载
            if (currentTeamId != null) {
                loadTeamData();
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("团队协作");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewMembers = findViewById(R.id.viewMembers);
        viewTodos = findViewById(R.id.viewTodos);
        viewVotes = findViewById(R.id.viewVotes);
        viewChat = findViewById(R.id.viewChat);
        viewActivityLog = findViewById(R.id.viewActivityLog);
        rvMembers = findViewById(R.id.rvMembers);
        rvTodos = findViewById(R.id.rvTodos);
        rvVotes = findViewById(R.id.rvVotes);
        rvMessages = findViewById(R.id.rvMessages);
        rvActivityLog = findViewById(R.id.rvActivityLog);
        btnAddMember = findViewById(R.id.btnAddMember);
        btnAddTodo = findViewById(R.id.btnAddTodo);
        btnAddVote = findViewById(R.id.btnAddVote);
        btnShareLink = findViewById(R.id.btnShareLink);
        btnShareQR = findViewById(R.id.btnShareQR);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnCreateTeam = findViewById(R.id.btnCreateTeam);
        btnJoinTeam = findViewById(R.id.btnJoinTeam);
        etInviteCode = findViewById(R.id.etInviteCode);
        layoutTeamButtons = findViewById(R.id.layoutTeamButtons);
    }

    private void showTeamSelectionDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_team_selection, null);
        
        EditText etTeamName = dialogView.findViewById(R.id.etTeamName);
        EditText etJoinCode = dialogView.findViewById(R.id.etJoinCode);
        Button btnCreate = dialogView.findViewById(R.id.btnCreateTeam);
        Button btnJoin = dialogView.findViewById(R.id.btnJoinTeam);
        Button btnUseExisting = dialogView.findViewById(R.id.btnUseExisting);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("团队协作")
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnCreate.setOnClickListener(v -> {
            String teamName = etTeamName.getText().toString().trim();
            if (teamName.isEmpty()) {
                Toast.makeText(this, "请输入团队名称", Toast.LENGTH_SHORT).show();
                return;
            }
            createTeam(teamName);
            dialog.dismiss();
        });

        btnJoin.setOnClickListener(v -> {
            String inviteCode = etJoinCode.getText().toString().trim();
            if (inviteCode.isEmpty()) {
                Toast.makeText(this, "请输入邀请码", Toast.LENGTH_SHORT).show();
                return;
            }
            joinTeam(inviteCode);
            dialog.dismiss();
        });

        btnUseExisting.setOnClickListener(v -> {
            // 加载用户已有的团队列表
            loadUserTeams();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void createTeam(String teamName) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", teamName);
        params.put("creatorId", currentUserId);
        params.put("itineraryId", null);
        params.put("description", "");

        teamApi.createTeam(params).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(CollaborationActivity.this, "团队创建成功", Toast.LENGTH_SHORT).show();
                    Map<String, Object> data = response.body().getData();
                    if (data != null && data.containsKey("team")) {
                        Map<String, Object> team = (Map<String, Object>) data.get("team");
                        currentTeamId = ((Number) team.get("id")).longValue();
                        currentTeamInviteCode = (String) team.get("inviteCode");
                        if (data.containsKey("userRole")) {
                            userRole = (String) data.get("userRole");
                        }
                        loadTeamData();
                    }
                } else {
                    Toast.makeText(CollaborationActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                Toast.makeText(CollaborationActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // 离线模式：使用模拟数据
                loadSampleData();
                currentTeamId = 1L;
                currentTeamInviteCode = "DEMO123";
            }
        });
    }

    private void joinTeam(String inviteCode) {
        // 确保邀请码没有多余空格
        String trimmedCode = inviteCode.trim().toUpperCase();
        
        Map<String, Object> params = new HashMap<>();
        params.put("inviteCode", trimmedCode);
        params.put("userId", currentUserId);

        teamApi.joinByInviteCode(params).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(CollaborationActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                    handleTeamData(response.body().getData());
                } else if (response.body() != null && "您已经是团队成员".equals(response.body().getMessage())) {
                    // 已是成员，尝试获取团队信息
                    Toast.makeText(CollaborationActivity.this, "您已是团队成员，正在进入...", Toast.LENGTH_SHORT).show();
                    loadUserTeamsAndSelect(trimmedCode);
                } else {
                    // 显示服务器返回的错误消息
                    String errorMsg = "加入失败";
                    if (response.body() != null && response.body().getMessage() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Toast.makeText(CollaborationActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                Toast.makeText(CollaborationActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // 离线模式
                loadSampleData();
                currentTeamId = 1L;
                currentTeamInviteCode = trimmedCode;
            }
        });
    }

    // 加载用户团队列表并选择指定邀请码的团队
    private void loadUserTeamsAndSelect(String targetInviteCode) {
        teamApi.getUserTeams(currentUserId).enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Map<String, Object>>>> call, Response<ApiResponse<List<Map<String, Object>>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Map<String, Object>> teams = response.body().getData();
                    if (teams != null) {
                        for (Map<String, Object> teamData : teams) {
                            Map<String, Object> team = (Map<String, Object>) teamData.get("team");
                            if (team != null && targetInviteCode.equals(team.get("inviteCode"))) {
                                currentTeamId = ((Number) team.get("id")).longValue();
                                currentTeamInviteCode = (String) team.get("inviteCode");
                                userRole = (String) teamData.get("userRole");
                                loadTeamData();
                                return;
                            }
                        }
                    }
                    Toast.makeText(CollaborationActivity.this, "未找到对应团队", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CollaborationActivity.this, "获取团队列表失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Map<String, Object>>>> call, Throwable t) {
                Toast.makeText(CollaborationActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 统一处理团队数据的方法
    private void handleTeamData(Map<String, Object> data) {
        if (data != null && data.containsKey("team")) {
            Map<String, Object> team = (Map<String, Object>) data.get("team");
            currentTeamId = ((Number) team.get("id")).longValue();
            currentTeamInviteCode = (String) team.get("inviteCode");
            if (data.containsKey("userRole")) {
                userRole = (String) data.get("userRole");
            }
            loadTeamData();
        }
    }

    private void loadUserTeams() {
        teamApi.getUserTeams(currentUserId).enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Map<String, Object>>>> call, Response<ApiResponse<List<Map<String, Object>>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Map<String, Object>> teams = response.body().getData();
                    if (teams != null && !teams.isEmpty()) {
                        showTeamListDialog(teams);
                    } else {
                        Toast.makeText(CollaborationActivity.this, "您还没有加入任何团队", Toast.LENGTH_SHORT).show();
                        showTeamSelectionDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Map<String, Object>>>> call, Throwable t) {
                Toast.makeText(CollaborationActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTeamListDialog(List<Map<String, Object>> teams) {
        String[] teamNames = new String[teams.size()];
        for (int i = 0; i < teams.size(); i++) {
            Map<String, Object> team = teams.get(i);
            Map<String, Object> teamInfo = (Map<String, Object>) team.get("team");
            teamNames[i] = teamInfo != null ? (String) teamInfo.get("name") : "团队 " + (i + 1);
        }

        new AlertDialog.Builder(this)
                .setTitle("选择团队")
                .setItems(teamNames, (dialog, which) -> {
                    Map<String, Object> selected = teams.get(which);
                    Map<String, Object> team = (Map<String, Object>) selected.get("team");
                    currentTeamId = ((Number) team.get("id")).longValue();
                    currentTeamInviteCode = (String) team.get("inviteCode");
                    userRole = (String) selected.get("userRole");
                    loadTeamData();
                })
                .setNegativeButton("取消", (dialog, which) -> showTeamSelectionDialog())
                .show();
    }

    private void loadTeamData() {
        if (currentTeamId == null) {
            loadSampleData();
            return;
        }

        // 显示分享按钮
        btnShareLink.setVisibility(View.VISIBLE);
        btnShareQR.setVisibility(View.VISIBLE);
        if (currentTeamInviteCode != null) {
            shareUrl = "https://travelplanner.com/share/" + currentTeamInviteCode;
        } else {
            generateShareUrl();
        }

        // 加载团队成员
        loadMembers();
        // 加载待办事项
        loadTodos();
        // 加载投票
        loadVotes();
        // 加载消息
        loadMessages();
        // 加载活动日志
        loadActivities();

        // 启动定时刷新
        startAutoRefresh();
    }

    private void loadMembers() {
        teamApi.getTeamMembers(currentTeamId).enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Map<String, Object>>>> call, Response<ApiResponse<List<Map<String, Object>>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Map<String, Object>> members = response.body().getData();
                    if (members != null) {
                        memberList.clear();
                        for (Map<String, Object> m : members) {
                            String name = m.get("nickname") != null ? (String) m.get("nickname") : "未知";
                            String role = (String) m.get("role");
                            String displayRole = "owner".equals(role) ? "创建者" : "editor".equals(role) ? "编辑者" : "查看者";
                            boolean isOwner = "owner".equals(role);
                            memberList.add(new MemberItem(name, displayRole, isOwner, getAvatarColor(name)));
                        }
                        memberAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Map<String, Object>>>> call, Throwable t) {
                // 使用本地数据
            }
        });
    }

    private void loadTodos() {
        Log.d("Collaboration", "加载待办: teamId=" + currentTeamId);
        teamApi.getTeamTodos(currentTeamId, currentUserId).enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Map<String, Object>>>> call, Response<ApiResponse<List<Map<String, Object>>>> response) {
                Log.d("Collaboration", "加载待办响应: code=" + response.code() + ", success=" + (response.body() != null ? response.body().isSuccess() : "null"));
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Map<String, Object>> todos = response.body().getData();
                    if (todos != null) {
                        Log.d("Collaboration", "获取到 " + todos.size() + " 条待办");
                        todoList.clear();
                        for (Map<String, Object> t : todos) {
                            String title = (String) t.get("title");
                            String assignee = t.get("assigneeName") != null ? (String) t.get("assigneeName") : "未分配";
                            String deadline = t.get("dueDate") != null ? ((String) t.get("dueDate")).substring(0, 10) : "无";
                            boolean completed = "done".equals(t.get("status"));
                            String priority = (String) t.get("priority");
                            Long todoId = ((Number) t.get("id")).longValue();
                            todoList.add(new TodoItem(title, assignee, deadline, completed, priority, todoId));
                        }
                        todoAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("Collaboration", "加载待办失败: code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Map<String, Object>>>> call, Throwable t) {
                Log.e("Collaboration", "加载待办网络错误: " + t.getMessage());
                // 网络错误：清空本地列表，强制显示空状态
                todoList.clear();
                todoAdapter.notifyDataSetChanged();
                Toast.makeText(CollaborationActivity.this, "网络错误，无法加载待办", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadVotes() {
        teamApi.getTeamVotes(currentTeamId, currentUserId).enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Map<String, Object>>>> call, Response<ApiResponse<List<Map<String, Object>>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Map<String, Object>> votes = response.body().getData();
                    if (votes != null) {
                        voteList.clear();
                        for (Map<String, Object> v : votes) {
                            String title = (String) v.get("title");
                            List<Map<String, Object>> optionsData = (List<Map<String, Object>>) v.get("options");
                            List<String> options = new ArrayList<>();
                            List<Integer> voteCounts = new ArrayList<>();
                            List<Long> optionIds = new ArrayList<>();
                            
                            if (optionsData != null) {
                                for (Map<String, Object> opt : optionsData) {
                                    options.add((String) opt.get("optionText"));
                                    voteCounts.add(opt.get("voteCount") != null ? ((Number) opt.get("voteCount")).intValue() : 0);
                                    optionIds.add(((Number) opt.get("id")).longValue());
                                }
                            }
                            
                            Long voteId = ((Number) v.get("id")).longValue();
                            boolean hasVoted = v.get("hasVoted") != null && (Boolean) v.get("hasVoted");
                            voteList.add(new VoteItem(title, options, voteCounts, optionIds, voteId, hasVoted));
                        }
                        voteAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Map<String, Object>>>> call, Throwable t) {
                // 使用本地数据
            }
        });
    }

    private void loadMessages() {
        teamApi.getMessages(currentTeamId, 0, 50).enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Map<String, Object>>>> call, Response<ApiResponse<List<Map<String, Object>>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Map<String, Object>> messages = response.body().getData();
                    if (messages != null) {
                        chatMessages.clear();
                        for (Map<String, Object> m : messages) {
                            String sender = m.get("nickname") != null ? (String) m.get("nickname") : "未知";
                            String content = (String) m.get("content");
                            Long userId = m.get("userId") != null ? ((Number) m.get("userId")).longValue() : 0;
                            boolean isMe = userId.equals(currentUserId);
                            chatMessages.add(new ChatMessage(sender, content, isMe, getAvatarColor(sender)));
                        }
                        // 反转列表，确保最新消息在底部（按时间正序排列）
                        Collections.reverse(chatMessages);
                        chatAdapter.notifyDataSetChanged();
                        // 滚动到最新消息（底部）
                        rvMessages.post(() -> {
                            if (!chatMessages.isEmpty()) {
                                rvMessages.scrollToPosition(chatMessages.size() - 1);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Map<String, Object>>>> call, Throwable t) {
                // 使用本地数据
            }
        });
    }

    private void loadActivities() {
        teamApi.getActivities(currentTeamId, 0, 20).enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Map<String, Object>>>> call, Response<ApiResponse<List<Map<String, Object>>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Map<String, Object>> activities = response.body().getData();
                    if (activities != null) {
                        activityLogs.clear();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        for (Map<String, Object> a : activities) {
                            String userName = a.get("nickname") != null ? (String) a.get("nickname") : "未知";
                            String content = (String) a.get("content");
                            String createTime = a.get("createTime") != null ? ((String) a.get("createTime")).substring(11, 16) : "";
                            activityLogs.add(new ActivityLogItem(userName, content, createTime, getAvatarColor(userName)));
                        }
                        activityLogAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Map<String, Object>>>> call, Throwable t) {
                // 使用本地数据
            }
        });
    }

    private void startAutoRefresh() {
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentTeamId != null) {
                    // 刷新消息
                    loadMessages();
                    // 刷新活动
                    loadActivities();
                }
                // 每5秒刷新一次
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(refreshRunnable, 5000);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("成员"));
        tabLayout.addTab(tabLayout.newTab().setText("待办"));
        tabLayout.addTab(tabLayout.newTab().setText("投票"));
        tabLayout.addTab(tabLayout.newTab().setText("聊天"));
        tabLayout.addTab(tabLayout.newTab().setText("动态"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideAllViews();
                switch (tab.getPosition()) {
                    case 0:
                        viewMembers.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        viewTodos.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        viewVotes.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        viewChat.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        viewActivityLog.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void hideAllViews() {
        viewMembers.setVisibility(View.GONE);
        viewTodos.setVisibility(View.GONE);
        viewVotes.setVisibility(View.GONE);
        viewChat.setVisibility(View.GONE);
        viewActivityLog.setVisibility(View.GONE);
    }

    private void setupRecyclerViews() {
        memberAdapter = new MemberAdapter(memberList);
        rvMembers.setLayoutManager(new LinearLayoutManager(this));
        rvMembers.setAdapter(memberAdapter);

        todoAdapter = new TodoAdapter(todoList);
        rvTodos.setLayoutManager(new LinearLayoutManager(this));
        rvTodos.setAdapter(todoAdapter);

        voteAdapter = new VoteAdapter(voteList);
        rvVotes.setLayoutManager(new LinearLayoutManager(this));
        rvVotes.setAdapter(voteAdapter);

        // 聊天消息：微信风格排列
        chatAdapter = new ChatAdapter(chatMessages, currentUser);
        LinearLayoutManager chatLayoutManager = new LinearLayoutManager(this);
        chatLayoutManager.setStackFromEnd(false);  // 正常从上到下排列
        rvMessages.setLayoutManager(chatLayoutManager);
        rvMessages.setAdapter(chatAdapter);

        activityLogAdapter = new ActivityLogAdapter(activityLogs);
        rvActivityLog.setLayoutManager(new LinearLayoutManager(this));
        rvActivityLog.setAdapter(activityLogAdapter);
    }

    private void setupListeners() {
        btnAddMember.setOnClickListener(v -> showAddMemberDialog());
        btnAddTodo.setOnClickListener(v -> showAddTodoDialog());
        btnAddVote.setOnClickListener(v -> showAddVoteDialog());
        btnShareLink.setOnClickListener(v -> copyShareLinkToClipboard());
        btnShareQR.setOnClickListener(v -> showQRCodeDialog());
        btnSend.setOnClickListener(v -> sendMessageToServer());
    }

    private void loadSampleData() {
        memberList.add(new MemberItem("张三", "创建者", true, getAvatarColor("张三")));
        memberList.add(new MemberItem("李四", "编辑者", false, getAvatarColor("李四")));
        memberList.add(new MemberItem("王五", "查看者", false, getAvatarColor("王五")));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        
        todoList.add(new TodoItem("预订酒店", "张三", sdf.format(cal.getTime()), false, "high", 1L));
        todoList.add(new TodoItem("购买景点门票", "李四", sdf.format(cal.getTime()), false, "medium", 2L));
        todoList.add(new TodoItem("准备行李清单", "王五", sdf.format(cal.getTime()), true, "low", 3L));
        
        voteList.add(new VoteItem("今晚吃什么?",
                Arrays.asList("火锅", "烧烤", "日料"),
                Arrays.asList(5, 3, 2),
                Arrays.asList(1L, 2L, 3L),
                1L, false));
        voteList.add(new VoteItem("明天去哪玩?",
                Arrays.asList("故宫", "长城", "颐和园"),
                Arrays.asList(4, 4, 2),
                Arrays.asList(4L, 5L, 6L),
                2L, false));

        chatMessages.add(new ChatMessage("李四", "大家明天几点出发？", false, getAvatarColor("李四")));
        chatMessages.add(new ChatMessage("王五", "我觉得9点比较合适", false, getAvatarColor("王五")));
        chatMessages.add(new ChatMessage("张三", "好的，那就9点集合", false, getAvatarColor("张三")));

        SimpleDateFormat logTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        activityLogs.add(new ActivityLogItem("张三", "创建了行程", logTime.format(new Date()), getAvatarColor("张三")));
        activityLogs.add(new ActivityLogItem("李四", "加入了协作", logTime.format(new Date()), getAvatarColor("李四")));
        activityLogs.add(new ActivityLogItem("王五", "加入了协作", logTime.format(new Date()), getAvatarColor("王五")));

        memberAdapter.notifyDataSetChanged();
        todoAdapter.notifyDataSetChanged();
        voteAdapter.notifyDataSetChanged();
        chatAdapter.notifyDataSetChanged();
        activityLogAdapter.notifyDataSetChanged();

        btnShareLink.setVisibility(View.VISIBLE);
        btnShareQR.setVisibility(View.VISIBLE);
        generateShareUrl();
    }

    private int getAvatarColor(String name) {
        int[][] colors = {
            {0xFF3498DB, 0xFF2980B9},
            {0xFF2ECC71, 0xFF27AE60},
            {0xFFE74C3C, 0xFFC0392B},
            {0xFFF39C12, 0xFFF1C40F},
            {0xFF9B59B6, 0xFF8E44AD},
            {0xFF1ABC9C, 0xFF16A085}
        };
        int index = Math.abs(name.hashCode()) % colors.length;
        return colors[index][0];
    }

    private void showAddMemberDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_member, null);
        EditText etCode = dialogView.findViewById(R.id.etName);
        TextView tvHint = dialogView.findViewById(R.id.tvName);
        
        etCode.setVisibility(View.GONE);
        tvHint.setText("分享邀请码给好友，好友加入后即成为团队成员");
        
        String myCode = currentTeamInviteCode != null ? currentTeamInviteCode : "DEMO123";
        tvHint.setText("您的邀请码: " + myCode);

        new AlertDialog.Builder(this)
                .setTitle("添加成员")
                .setView(dialogView)
                .setPositiveButton("复制邀请码", (dialog, which) -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Invite Code", myCode);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "邀请码已复制", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("关闭", null)
                .show();
    }

    private void showAddTodoDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_todo, null);
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etDeadline = dialogView.findViewById(R.id.etDeadline);
        Spinner spinnerAssignee = dialogView.findViewById(R.id.spinnerAssignee);
        RadioGroup rgPriority = dialogView.findViewById(R.id.rgPriority);
        MaterialButton btnCancel = dialogView.findViewById(R.id.btnCancel);
        MaterialButton btnAdd = dialogView.findViewById(R.id.btnAdd);

        // 强制设置按钮文字颜色，防止Material主题覆盖
        btnCancel.setTextColor(android.graphics.Color.parseColor("#212121"));
        btnAdd.setTextColor(android.graphics.Color.WHITE);

        List<String> names = new ArrayList<>();
        names.add("未分配");
        for (MemberItem member : memberList) {
            names.add(member.name);
        }
        ArrayAdapter<String> assigneeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        assigneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignee.setAdapter(assigneeAdapter);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDeadline.setText(sdf.format(new Date()));

        etDeadline.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                cal.set(year, month, dayOfMonth);
                etDeadline.setText(sdf.format(cal.getTime()));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String assigneeName = (String) spinnerAssignee.getSelectedItem();
            String deadline = etDeadline.getText().toString();
            String priority = "medium";
            
            int selectedId = rgPriority.getCheckedRadioButtonId();
            if (selectedId == R.id.rbLow) priority = "low";
            else if (selectedId == R.id.rbHigh) priority = "high";
            else priority = "medium";

            if (title.isEmpty()) {
                Toast.makeText(this, "请输入待办事项", Toast.LENGTH_SHORT).show();
                return;
            }
            
            createTodoOnServer(title, assigneeName, deadline, priority);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void createTodoOnServer(String title, String assigneeName, String deadline, String priority) {
        Log.d("Collaboration", "创建待办: teamId=" + currentTeamId + ", title=" + title);
        
        Map<String, Object> params = new HashMap<>();
        params.put("teamId", currentTeamId);
        params.put("title", title);
        params.put("content", "");
        params.put("priority", priority);
        params.put("dueDate", deadline);
        params.put("creatorId", currentUserId);
        
        // 找到 assigneeId
        Long assigneeId = null;
        if (!"未分配".equals(assigneeName)) {
            for (MemberItem m : memberList) {
                if (m.name.equals(assigneeName)) {
                    assigneeId = currentUserId; // 简化处理
                    break;
                }
            }
        }
        params.put("assigneeId", assigneeId);

        teamApi.createTodo(params).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                Log.d("Collaboration", "响应码: " + response.code() + ", body: " + response.body());
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(CollaborationActivity.this, "待办已保存到服务器", Toast.LENGTH_SHORT).show();
                    loadTodos();
                } else {
                    String errorMsg = "创建待办失败，请检查网络连接";
                    if (response.body() != null && response.body().getMessage() != null) {
                        errorMsg = "创建待办失败: " + response.body().getMessage();
                    } else {
                        errorMsg = "创建待办失败，响应码: " + response.code();
                    }
                    Log.e("Collaboration", "创建待办失败: " + errorMsg);
                    Toast.makeText(CollaborationActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                Log.e("Collaboration", "网络错误: " + t.getMessage(), t);
                Toast.makeText(CollaborationActivity.this, "网络错误，请检查网络连接后重试: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAddVoteDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_vote, null);
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etOptions = dialogView.findViewById(R.id.etOptions);

        new AlertDialog.Builder(this)
                .setTitle("创建投票")
                .setView(dialogView)
                .setPositiveButton("创建", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String optionsStr = etOptions.getText().toString().trim();
                    if (!title.isEmpty() && !optionsStr.isEmpty()) {
                        createVoteOnServer(title, optionsStr);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void createVoteOnServer(String title, String optionsStr) {
        String[] options = optionsStr.split(",");
        List<String> optionList = Arrays.asList(options);
        List<String> trimmedOptions = new ArrayList<>();
        for (String opt : options) {
            trimmedOptions.add(opt.trim());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("teamId", currentTeamId);
        params.put("title", title);
        params.put("description", "");
        params.put("multipleChoice", false);
        params.put("creatorId", currentUserId);
        params.put("options", trimmedOptions);

        teamApi.createVote(params).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(CollaborationActivity.this, "投票已创建", Toast.LENGTH_SHORT).show();
                    loadVotes();
                } else {
                    // 离线模式
                    List<Integer> voteCounts = new ArrayList<>();
                    List<Long> optionIds = new ArrayList<>();
                    for (int i = 0; i < trimmedOptions.size(); i++) {
                        voteCounts.add(0);
                        optionIds.add(System.currentTimeMillis() + i);
                    }
                    voteList.add(0, new VoteItem(title, trimmedOptions, voteCounts, optionIds, System.currentTimeMillis(), false));
                    voteAdapter.notifyItemInserted(0);
                    
                    SimpleDateFormat logTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    activityLogs.add(0, new ActivityLogItem(currentUser, "创建了投票：" + title, 
                        logTime.format(new Date()), getAvatarColor(currentUser)));
                    activityLogAdapter.notifyItemInserted(0);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                List<Integer> voteCounts = new ArrayList<>();
                List<Long> optionIds = new ArrayList<>();
                for (int i = 0; i < trimmedOptions.size(); i++) {
                    voteCounts.add(0);
                    optionIds.add(System.currentTimeMillis() + i);
                }
                voteList.add(0, new VoteItem(title, trimmedOptions, voteCounts, optionIds, System.currentTimeMillis(), false));
                voteAdapter.notifyItemInserted(0);
                
                SimpleDateFormat logTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                activityLogs.add(0, new ActivityLogItem(currentUser, "创建了投票：" + title, 
                    logTime.format(new Date()), getAvatarColor(currentUser)));
                activityLogAdapter.notifyItemInserted(0);
            }
        });
    }

    private void sendMessageToServer() {
        String message = etMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("teamId", currentTeamId);
            params.put("userId", currentUserId);
            params.put("content", message);
            params.put("messageType", "text");

            teamApi.sendMessage(params).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
                @Override
                public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                    etMessage.setText("");
                    loadMessages();
                }

                @Override
                public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                    // 离线模式
                    chatMessages.add(new ChatMessage(currentUser, message, true, getAvatarColor(currentUser)));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    rvMessages.scrollToPosition(chatMessages.size() - 1);
                    etMessage.setText("");
                    
                    SimpleDateFormat logTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    activityLogs.add(0, new ActivityLogItem(currentUser, "发送消息: " + message, 
                        logTime.format(new Date()), getAvatarColor(currentUser)));
                    activityLogAdapter.notifyItemInserted(0);
                }
            });
        }
    }

    // ========== 数据模型 ==========

    static class MemberItem {
        String name;
        String role;
        boolean isOwner;
        int avatarColor;

        MemberItem(String name, String role, boolean isOwner, int avatarColor) {
            this.name = name;
            this.role = role;
            this.isOwner = isOwner;
            this.avatarColor = avatarColor;
        }
    }

    static class TodoItem {
        String title;
        String assignee;
        String deadline;
        boolean completed;
        String priority;
        Long todoId;

        TodoItem(String title, String assignee, String deadline, boolean completed, String priority, Long todoId) {
            this.title = title;
            this.assignee = assignee;
            this.deadline = deadline;
            this.completed = completed;
            this.priority = priority;
            this.todoId = todoId;
        }
    }

    static class VoteItem {
        String title;
        List<String> options;
        List<Integer> votes;
        List<Long> optionIds;
        Long voteId;
        boolean hasVoted;

        VoteItem(String title, List<String> options, List<Integer> votes, List<Long> optionIds, Long voteId, boolean hasVoted) {
            this.title = title;
            this.options = options;
            this.votes = votes;
            this.optionIds = optionIds;
            this.voteId = voteId;
            this.hasVoted = hasVoted;
        }
    }

    static class ChatMessage {
        String sender;
        String message;
        boolean isMe;
        int avatarColor;

        ChatMessage(String sender, String message, boolean isMe, int avatarColor) {
            this.sender = sender;
            this.message = message;
            this.isMe = isMe;
            this.avatarColor = avatarColor;
        }
    }

    static class ActivityLogItem {
        String userName;
        String action;
        String time;
        int avatarColor;

        ActivityLogItem(String userName, String action, String time, int avatarColor) {
            this.userName = userName;
            this.action = action;
            this.time = time;
            this.avatarColor = avatarColor;
        }
    }

    // ========== Adapter 类 ==========

    class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
        private List<MemberItem> items;

        MemberAdapter(List<MemberItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_member, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MemberItem item = items.get(position);
            holder.tvName.setText(item.name);
            holder.tvRole.setText(item.role);
            holder.tvAvatar.setText(item.name.substring(0, 1));
            
            GradientDrawable bg = (GradientDrawable) holder.tvAvatar.getBackground();
            bg.setColor(item.avatarColor);
            
            if (item.isOwner) {
                holder.tvRole.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvRole, tvAvatar;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvRole = itemView.findViewById(R.id.tvRole);
                tvAvatar = itemView.findViewById(R.id.tvAvatar);
            }
        }
    }

    class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
        private List<TodoItem> items;

        TodoAdapter(List<TodoItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_todo, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TodoItem item = items.get(position);
            holder.tvTitle.setText(item.title);
            holder.tvAssignee.setText("负责人: " + item.assignee);
            holder.tvDeadline.setText("截止: " + item.deadline);
            holder.cbCompleted.setChecked(item.completed);
            
            String priorityText;
            int priorityColor;
            switch (item.priority) {
                case "high":
                    priorityText = "高";
                    priorityColor = Color.parseColor("#F44336");
                    break;
                case "low":
                    priorityText = "低";
                    priorityColor = Color.parseColor("#4CAF50");
                    break;
                default:
                    priorityText = "中";
                    priorityColor = Color.parseColor("#FF9800");
            }
            holder.tvPriority.setText(priorityText);
            GradientDrawable bg = (GradientDrawable) holder.tvPriority.getBackground();
            bg.setColor(priorityColor);
            
            if (item.completed) {
                holder.tvStatus.setText("已完成");
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                holder.tvTitle.setTextColor(Color.parseColor("#999999"));
            } else {
                holder.tvStatus.setText("进行中");
                holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));
                holder.tvTitle.setTextColor(Color.parseColor("#333333"));
            }
            
            holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String newStatus = isChecked ? "done" : "pending";
                item.completed = isChecked;
                notifyItemChanged(position);
                
                // 更新到服务器
                if (currentTeamId != null && item.todoId != null) {
                    teamApi.updateTodoStatus(item.todoId, newStatus, currentUserId).enqueue(new Callback<ApiResponse<Void>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {}
                        @Override
                        public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {}
                    });
                }
                
                SimpleDateFormat logTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String action = isChecked ? "完成了待办：" + item.title : "重新打开了待办：" + item.title;
                activityLogs.add(0, new ActivityLogItem(currentUser, action, 
                    logTime.format(new Date()), getAvatarColor(currentUser)));
                activityLogAdapter.notifyItemInserted(0);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvAssignee, tvDeadline, tvPriority, tvStatus;
            CheckBox cbCompleted;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvAssignee = itemView.findViewById(R.id.tvAssignee);
                tvDeadline = itemView.findViewById(R.id.tvDeadline);
                tvPriority = itemView.findViewById(R.id.tvPriority);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                cbCompleted = itemView.findViewById(R.id.cbCompleted);
            }
        }
    }

    class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {
        private List<VoteItem> items;

        VoteAdapter(List<VoteItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_vote, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            VoteItem item = items.get(position);
            holder.tvTitle.setText(item.title);
            holder.llOptions.removeAllViews();

            int totalVotes = 0;
            for (int vote : item.votes) {
                totalVotes += vote;
            }

            for (int i = 0; i < item.options.size(); i++) {
                View optionView = LayoutInflater.from(CollaborationActivity.this)
                        .inflate(R.layout.item_vote_option, holder.llOptions, false);
                TextView tvOption = optionView.findViewById(R.id.tvOption);
                ProgressBar pbProgress = optionView.findViewById(R.id.pbProgress);
                TextView tvVotes = optionView.findViewById(R.id.tvVotes);

                tvOption.setText(item.options.get(i));
                int votes = item.votes.get(i);
                tvVotes.setText(votes + " 票");
                pbProgress.setMax(totalVotes > 0 ? totalVotes : 1);
                pbProgress.setProgress(votes);

                final int index = i;
                optionView.setOnClickListener(v -> {
                    // 投票到服务器
                    Map<String, Object> params = new HashMap<>();
                    params.put("voteId", item.voteId);
                    params.put("userId", currentUserId);
                    params.put("optionIds", Arrays.asList(item.optionIds.get(index)));
                    
                    teamApi.castVote(params).enqueue(new Callback<ApiResponse<Void>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(CollaborationActivity.this, "投票成功", Toast.LENGTH_SHORT).show();
                                loadVotes();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                            // 离线模式
                            item.votes.set(index, item.votes.get(index) + 1);
                            notifyItemChanged(position);
                            Toast.makeText(CollaborationActivity.this, "投票成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                holder.llOptions.addView(optionView);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            LinearLayout llOptions;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                llOptions = itemView.findViewById(R.id.llOptions);
            }
        }
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
        private List<ChatMessage> items;
        private String currentUserName;

        ChatAdapter(List<ChatMessage> items, String currentUserName) {
            this.items = items;
            this.currentUserName = currentUserName;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ChatMessage item = items.get(position);
            
            if (item.isMe) {
                holder.layoutMe.setVisibility(View.VISIBLE);
                holder.layoutOther.setVisibility(View.GONE);
                holder.tvMessageMe.setText(item.message);
                holder.tvNameMe.setText(item.sender);
                holder.tvAvatarMe.setText(item.sender.substring(0, 1));
                
                GradientDrawable bgMe = (GradientDrawable) holder.tvAvatarMe.getBackground();
                bgMe.setColor(item.avatarColor);
            } else {
                holder.layoutMe.setVisibility(View.GONE);
                holder.layoutOther.setVisibility(View.VISIBLE);
                holder.tvMessageOther.setText(item.message);
                holder.tvNameOther.setText(item.sender);
                holder.tvAvatarOther.setText(item.sender.substring(0, 1));
                
                GradientDrawable bgOther = (GradientDrawable) holder.tvAvatarOther.getBackground();
                bgOther.setColor(item.avatarColor);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout layoutMe, layoutOther;
            TextView tvMessageMe, tvMessageOther;
            TextView tvNameMe, tvNameOther;
            TextView tvAvatarMe, tvAvatarOther;

            ViewHolder(View itemView) {
                super(itemView);
                layoutMe = itemView.findViewById(R.id.layoutMe);
                layoutOther = itemView.findViewById(R.id.layoutOther);
                tvMessageMe = itemView.findViewById(R.id.tvMessageMe);
                tvMessageOther = itemView.findViewById(R.id.tvMessageOther);
                tvNameMe = itemView.findViewById(R.id.tvNameMe);
                tvNameOther = itemView.findViewById(R.id.tvNameOther);
                tvAvatarMe = itemView.findViewById(R.id.tvAvatarMe);
                tvAvatarOther = itemView.findViewById(R.id.tvAvatarOther);
            }
        }
    }

    class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {
        private List<ActivityLogItem> items;

        ActivityLogAdapter(List<ActivityLogItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_activity_log, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ActivityLogItem item = items.get(position);
            holder.tvUserName.setText(item.userName);
            holder.tvAction.setText(item.action);
            holder.tvTime.setText(item.time);
            holder.tvAvatar.setText(item.userName.substring(0, 1));
            
            GradientDrawable bg = (GradientDrawable) holder.tvAvatar.getBackground();
            bg.setColor(item.avatarColor);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvUserName, tvAction, tvTime, tvAvatar;

            ViewHolder(View itemView) {
                super(itemView);
                tvUserName = itemView.findViewById(R.id.tvUserName);
                tvAction = itemView.findViewById(R.id.tvAction);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvAvatar = itemView.findViewById(R.id.tvAvatar);
            }
        }
    }

    // ========== 工具方法 ==========

    private void generateShareUrl() {
        String roomId = currentTeamInviteCode != null ? currentTeamInviteCode : UUID.randomUUID().toString().substring(0, 8);
        shareUrl = "https://travelplanner.com/share/" + roomId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshRunnable != null) {
            handler.removeCallbacks(refreshRunnable);
        }
    }

    private void copyShareLinkToClipboard() {
        if (shareUrl == null) {
            generateShareUrl();
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("TravelPlanner Share", shareUrl);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "已复制分享链接到剪贴板", Toast.LENGTH_SHORT).show();
    }

    private void showQRCodeDialog() {
        if (shareUrl == null) {
            generateShareUrl();
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_qr_code, null);
        ImageView ivQRCode = dialogView.findViewById(R.id.ivQRCode);
        TextView tvShareUrl = dialogView.findViewById(R.id.tvShareUrl);
        Button btnCopyLink = dialogView.findViewById(R.id.btnCopyLink);

        tvShareUrl.setText(shareUrl);
        Bitmap qrBitmap = generateQRCode(shareUrl);
        if (qrBitmap != null) {
            ivQRCode.setImageBitmap(qrBitmap);
        }

        btnCopyLink.setOnClickListener(v -> {
            copyShareLinkToClipboard();
        });

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("关闭", null)
                .show();
    }

    private Bitmap generateQRCode(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 2);

            int size = 512;
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints);

            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    pixels[y * size + x] = bitMatrix.get(x, y)
                            ? getResources().getColor(android.R.color.black)
                            : getResources().getColor(android.R.color.white);
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "生成二维码失败", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
