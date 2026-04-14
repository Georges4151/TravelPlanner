package com.travelplanner.ui.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripReminderActivity extends AppCompatActivity {

    private RecyclerView rvReminders;
    private Button btnAddReminder;
    private ReminderAdapter adapter;
    private List<ReminderItem> reminderList = new ArrayList<>();
    private AlarmManager alarmManager;
    private int nextId = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, TripReminderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_reminder);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        setupToolbar();
        initViews();
        setupRecyclerView();
        setupListeners();
        loadSampleReminders();
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
        rvReminders = findViewById(R.id.rvReminders);
        btnAddReminder = findViewById(R.id.btnAddReminder);
    }

    private void setupRecyclerView() {
        adapter = new ReminderAdapter(reminderList);
        rvReminders.setLayoutManager(new LinearLayoutManager(this));
        rvReminders.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddReminder.setOnClickListener(v -> showAddReminderDialog());
    }

    private void loadSampleReminders() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, 5);
        reminderList.add(new ReminderItem(
                nextId++,
                "测试提醒",
                "5分钟后测试通知功能",
                sdf.format(calendar.getTime()),
                "测试",
                "应用内通知",
                true));
        scheduleReminder(reminderList.get(0));

        calendar.add(Calendar.HOUR, 2);
        reminderList.add(new ReminderItem(
                nextId++,
                "出发提醒",
                "前往首都机场T3航站楼",
                sdf.format(calendar.getTime()),
                "飞机",
                "应用内通知",
                true));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        reminderList.add(new ReminderItem(
                nextId++,
                "景点开放提醒",
                "故宫博物院 8:00 开放，请提前15分钟到达",
                sdf.format(calendar.getTime()),
                "景点",
                "应用内通知",
                true));

        calendar.add(Calendar.HOUR, 16);
        reminderList.add(new ReminderItem(
                nextId++,
                "预约倒计时提醒",
                "国家博物馆预约还有2小时",
                sdf.format(calendar.getTime()),
                "预约",
                "短信",
                false));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        reminderList.add(new ReminderItem(
                nextId++,
                "交通换乘提醒",
                "30分钟后地铁2号线发车，建议提前10分钟到站",
                sdf.format(calendar.getTime()),
                "换乘",
                "应用内通知",
                true));

        adapter.notifyDataSetChanged();
    }

    private void showAddReminderDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_reminder, null);
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etTime = dialogView.findViewById(R.id.etTime);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);
        Spinner spinnerMethod = dialogView.findViewById(R.id.spinnerMethod);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[] { "出发提醒", "景点开放", "预约提醒", "交通换乘" });
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[] { "应用内通知", "短信" });
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMethod.setAdapter(methodAdapter);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        etTime.setText(sdf.format(new Date()));

        new AlertDialog.Builder(this)
                .setTitle("添加提醒")
                .setView(dialogView)
                .setPositiveButton("添加", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String description = etDescription.getText().toString().trim();
                    String time = etTime.getText().toString().trim();
                    String type = (String) spinnerType.getSelectedItem();
                    String method = (String) spinnerMethod.getSelectedItem();

                    if (!title.isEmpty() && !time.isEmpty()) {
                        ReminderItem item = new ReminderItem(nextId++, title, description, time, type, method, true);
                        reminderList.add(0, item);
                        adapter.notifyItemInserted(0);

                        if ("应用内通知".equals(method) && item.enabled) {
                            scheduleReminder(item);
                        }

                        Toast.makeText(this, "提醒已添加", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void scheduleReminder(ReminderItem item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
                return;
            }
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date = sdf.parse(item.time);
            if (date == null)
                return;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Intent intent = new Intent(this, ReminderReceiver.class);
            intent.setAction("com.travelplanner.REMINDER");
            intent.putExtra(ReminderReceiver.EXTRA_TITLE, item.title);
            intent.putExtra(ReminderReceiver.EXTRA_DESCRIPTION, item.description);
            intent.putExtra(ReminderReceiver.EXTRA_ID, item.id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    item.id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelReminder(ReminderItem item) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.setAction("com.travelplanner.REMINDER");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                item.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(item.id);
        }
    }

    static class ReminderItem {
        int id;
        String title;
        String description;
        String time;
        String type;
        String method;
        boolean enabled;

        ReminderItem(int id, String title, String description, String time, String type, String method,
                boolean enabled) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.time = time;
            this.type = type;
            this.method = method;
            this.enabled = enabled;
        }
    }

    class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
        private List<ReminderItem> items;

        ReminderAdapter(List<ReminderItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reminder, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ReminderItem item = items.get(position);
            holder.tvTitle.setText(item.title);
            holder.tvDescription.setText(item.description);
            holder.tvTime.setText(item.time);
            holder.tvType.setText(item.type);
            holder.tvMethod.setText(item.method);
            holder.cbEnabled.setChecked(item.enabled);

            holder.cbEnabled.setOnCheckedChangeListener(null);
            holder.cbEnabled.setChecked(item.enabled);
            holder.cbEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.enabled = isChecked;
                if ("应用内通知".equals(item.method)) {
                    if (isChecked) {
                        scheduleReminder(item);
                        Toast.makeText(TripReminderActivity.this, "提醒已开启", Toast.LENGTH_SHORT).show();
                    } else {
                        cancelReminder(item);
                        Toast.makeText(TripReminderActivity.this, "提醒已关闭", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.itemView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(TripReminderActivity.this)
                        .setTitle("删除提醒")
                        .setMessage("确定要删除这个提醒吗？")
                        .setPositiveButton("删除", (dialog, which) -> {
                            ReminderItem removedItem = items.remove(position);
                            notifyItemRemoved(position);
                            if ("应用内通知".equals(removedItem.method)) {
                                cancelReminder(removedItem);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDescription, tvTime, tvType, tvMethod;
            CheckBox cbEnabled;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvDescription = itemView.findViewById(R.id.tvDescription);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvType = itemView.findViewById(R.id.tvType);
                tvMethod = itemView.findViewById(R.id.tvMethod);
                cbEnabled = itemView.findViewById(R.id.cbEnabled);
            }
        }
    }
}
