package com.travelplanner.ui.reminder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.travelplanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CollaborationActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private View viewMembers, viewTodos, viewVotes;
    private RecyclerView rvMembers, rvTodos, rvVotes;
    private Button btnAddMember, btnAddTodo, btnAddVote;
    private Button btnShareLink, btnShareQR;

    private MemberAdapter memberAdapter;
    private TodoAdapter todoAdapter;
    private VoteAdapter voteAdapter;

    private List<MemberItem> memberList = new ArrayList<>();
    private List<TodoItem> todoList = new ArrayList<>();
    private List<VoteItem> voteList = new ArrayList<>();

    private String shareUrl;

    public static void start(Context context) {
        Intent intent = new Intent(context, CollaborationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        setupToolbar();
        initViews();
        setupTabs();
        setupRecyclerViews();
        setupListeners();
        loadSampleData();
        generateShareUrl();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewMembers = findViewById(R.id.viewMembers);
        viewTodos = findViewById(R.id.viewTodos);
        viewVotes = findViewById(R.id.viewVotes);
        rvMembers = findViewById(R.id.rvMembers);
        rvTodos = findViewById(R.id.rvTodos);
        rvVotes = findViewById(R.id.rvVotes);
        btnAddMember = findViewById(R.id.btnAddMember);
        btnAddTodo = findViewById(R.id.btnAddTodo);
        btnAddVote = findViewById(R.id.btnAddVote);
        btnShareLink = findViewById(R.id.btnShareLink);
        btnShareQR = findViewById(R.id.btnShareQR);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("成员"));
        tabLayout.addTab(tabLayout.newTab().setText("待办"));
        tabLayout.addTab(tabLayout.newTab().setText("投票"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewMembers.setVisibility(View.VISIBLE);
                        viewTodos.setVisibility(View.GONE);
                        viewVotes.setVisibility(View.GONE);
                        break;
                    case 1:
                        viewMembers.setVisibility(View.GONE);
                        viewTodos.setVisibility(View.VISIBLE);
                        viewVotes.setVisibility(View.GONE);
                        break;
                    case 2:
                        viewMembers.setVisibility(View.GONE);
                        viewTodos.setVisibility(View.GONE);
                        viewVotes.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
    }

    private void setupListeners() {
        btnAddMember.setOnClickListener(v -> showAddMemberDialog());
        btnAddTodo.setOnClickListener(v -> showAddTodoDialog());
        btnAddVote.setOnClickListener(v -> showAddVoteDialog());
        btnShareLink.setOnClickListener(v -> copyShareLinkToClipboard());
        btnShareQR.setOnClickListener(v -> showQRCodeDialog());
    }

    private void loadSampleData() {
        memberList.add(new MemberItem("张三", "创建者", true));
        memberList.add(new MemberItem("李四", "编辑者", false));
        memberList.add(new MemberItem("王五", "查看者", false));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        todoList.add(new TodoItem("预订酒店", "张三", sdf.format(new Date()), false));
        todoList.add(new TodoItem("购买门票", "李四", sdf.format(new Date()), true));
        todoList.add(new TodoItem("准备行李", "王五", sdf.format(new Date()), false));

        voteList.add(new VoteItem("今晚吃什么?",
                Arrays.asList("火锅", "烧烤", "日料"),
                Arrays.asList(5, 3, 2)));
        voteList.add(new VoteItem("明天去哪玩?",
                Arrays.asList("故宫", "长城", "颐和园"),
                Arrays.asList(4, 4, 2)));

        memberAdapter.notifyDataSetChanged();
        todoAdapter.notifyDataSetChanged();
        voteAdapter.notifyDataSetChanged();
    }

    private void showAddMemberDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_member, null);
        EditText etName = dialogView.findViewById(R.id.etName);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[] { "编辑者", "查看者" });
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        new AlertDialog.Builder(this)
                .setTitle("添加成员")
                .setView(dialogView)
                .setPositiveButton("添加", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String role = (String) spinnerRole.getSelectedItem();
                    if (!name.isEmpty()) {
                        memberList.add(new MemberItem(name, role, false));
                        memberAdapter.notifyItemInserted(memberList.size() - 1);
                        Toast.makeText(this, "成员已添加", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showAddTodoDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_todo, null);
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        Spinner spinnerAssignee = dialogView.findViewById(R.id.spinnerAssignee);

        List<String> names = new ArrayList<>();
        for (MemberItem member : memberList) {
            names.add(member.name);
        }
        ArrayAdapter<String> assigneeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        assigneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignee.setAdapter(assigneeAdapter);

        new AlertDialog.Builder(this)
                .setTitle("添加待办")
                .setView(dialogView)
                .setPositiveButton("添加", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String assignee = (String) spinnerAssignee.getSelectedItem();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    if (!title.isEmpty()) {
                        todoList.add(new TodoItem(title, assignee, sdf.format(new Date()), false));
                        todoAdapter.notifyItemInserted(todoList.size() - 1);
                        Toast.makeText(this, "待办已添加", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
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
                        String[] options = optionsStr.split(",");
                        List<String> optionList = new ArrayList<>();
                        List<Integer> voteListData = new ArrayList<>();
                        for (String option : options) {
                            optionList.add(option.trim());
                            voteListData.add(0);
                        }
                        voteList.add(new VoteItem(title, optionList, voteListData));
                        voteAdapter.notifyItemInserted(voteList.size() - 1);
                        Toast.makeText(this, "投票已创建", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    static class MemberItem {
        String name;
        String role;
        boolean isOwner;

        MemberItem(String name, String role, boolean isOwner) {
            this.name = name;
            this.role = role;
            this.isOwner = isOwner;
        }
    }

    static class TodoItem {
        String title;
        String assignee;
        String deadline;
        boolean completed;

        TodoItem(String title, String assignee, String deadline, boolean completed) {
            this.title = title;
            this.assignee = assignee;
            this.deadline = deadline;
            this.completed = completed;
        }
    }

    static class VoteItem {
        String title;
        List<String> options;
        List<Integer> votes;

        VoteItem(String title, List<String> options, List<Integer> votes) {
            this.title = title;
            this.options = options;
            this.votes = votes;
        }
    }

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
            if (item.isOwner) {
                holder.tvRole.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvRole;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvRole = itemView.findViewById(R.id.tvRole);
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
            holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.completed = isChecked;
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvAssignee, tvDeadline;
            CheckBox cbCompleted;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvAssignee = itemView.findViewById(R.id.tvAssignee);
                tvDeadline = itemView.findViewById(R.id.tvDeadline);
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
                    item.votes.set(index, item.votes.get(index) + 1);
                    notifyItemChanged(position);
                    Toast.makeText(CollaborationActivity.this, "投票成功", Toast.LENGTH_SHORT).show();
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

    private void generateShareUrl() {
        String roomId = UUID.randomUUID().toString().substring(0, 8);
        shareUrl = "https://travelplanner.com/share/" + roomId;
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
