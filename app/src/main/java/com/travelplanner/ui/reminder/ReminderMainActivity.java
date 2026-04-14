package com.travelplanner.ui.reminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.data.local.AppDatabase;
import com.travelplanner.data.local.dao.ChecklistDao;
import com.travelplanner.data.local.dao.ItineraryDao;
import com.travelplanner.data.local.entity.ChecklistItemEntity;
import com.travelplanner.data.local.entity.ItineraryEntity;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Itinerary;
import com.travelplanner.ui.checklist.ChecklistActivity;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderMainActivity extends AppCompatActivity {

    private LinearLayout btnChecklist;
    private LinearLayout btnTripReminder;
    private LinearLayout btnCollaboration;
    private LinearLayout btnSelectItinerary;
    private LinearLayout btnViewMyChecklist;
    private TextView tvSelectedItinerary;

    private List<Itinerary> itineraryList = new ArrayList<>();
    private Itinerary selectedItinerary;
    private Handler handler = new Handler(Looper.getMainLooper());

    private AppDatabase db;
    private ChecklistDao checklistDao;
    private ItineraryDao itineraryDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void start(Context context) {
        Intent intent = new Intent(context, ReminderMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_main);

        db = AppDatabase.getInstance(this);
        checklistDao = db.checklistDao();
        itineraryDao = db.itineraryDao();

        setupToolbar();
        setupViews();
        setupListeners();
        loadItineraries();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
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

    private void setupViews() {
        btnChecklist = findViewById(R.id.btnChecklist);
        btnTripReminder = findViewById(R.id.btnTripReminder);
        btnCollaboration = findViewById(R.id.btnCollaboration);
        btnSelectItinerary = findViewById(R.id.btnSelectItinerary);
        btnViewMyChecklist = findViewById(R.id.btnViewMyChecklist);
        tvSelectedItinerary = findViewById(R.id.tvSelectedItinerary);
    }

    private void setupListeners() {
        btnSelectItinerary.setOnClickListener(v -> showItinerarySelectDialog());

        btnChecklist.setOnClickListener(v -> {
            if (selectedItinerary != null) {
                ChecklistActivity.startWithItinerary(ReminderMainActivity.this, selectedItinerary);
            } else {
                ChecklistActivity.start(ReminderMainActivity.this);
            }
        });

        btnViewMyChecklist.setOnClickListener(v -> showMyChecklistDialog());

        btnTripReminder.setOnClickListener(v -> {
            TripReminderActivity.start(ReminderMainActivity.this);
        });

        btnCollaboration.setOnClickListener(v -> {
            CollaborationActivity.start(ReminderMainActivity.this);
        });
    }

    private void showMyChecklistDialog() {
        executor.execute(() -> {
            List<ItineraryEntity> itineraries = itineraryDao.getAll();

            if (itineraries == null || itineraries.isEmpty()) {
                handler.post(() -> {
                    Toast.makeText(this, "暂无行程，请先创建行程并保存清单", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            List<ItineraryEntity> itinerariesWithChecklist = new ArrayList<>();
            for (ItineraryEntity itinerary : itineraries) {
                int count = checklistDao.getCountByItineraryId(itinerary.getId());
                if (count > 0) {
                    itinerariesWithChecklist.add(itinerary);
                }
            }

            if (itinerariesWithChecklist.isEmpty()) {
                handler.post(() -> {
                    Toast.makeText(this, "暂无已保存的出行清单", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            String[] itineraryNames = new String[itinerariesWithChecklist.size()];
            String[] itineraryDetails = new String[itinerariesWithChecklist.size()];

            for (int i = 0; i < itinerariesWithChecklist.size(); i++) {
                ItineraryEntity entity = itinerariesWithChecklist.get(i);
                String title = entity.getTitle() != null ? entity.getTitle() : "未命名行程";
                String destination = entity.getDestination() != null ? entity.getDestination() : "";

                int totalCount = checklistDao.getCountByItineraryId(entity.getId());
                int checkedCount = checklistDao.getCheckedCount(entity.getId());

                itineraryNames[i] = title + (destination.isEmpty() ? "" : " - " + destination);
                itineraryDetails[i] = "共" + totalCount + "项，已准备" + checkedCount + "项";
            }

            handler.post(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择要查看的行程清单");

                View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_itinerary_checklist, null);
                RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewItineraries);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                ItineraryChecklistAdapter adapter = new ItineraryChecklistAdapter(
                        this,
                        itinerariesWithChecklist,
                        itineraryNames,
                        itineraryDetails,
                        checklistDao);
                recyclerView.setAdapter(adapter);

                builder.setView(dialogView);
                builder.setNegativeButton("关闭", null);
                builder.show();
            });
        });
    }

    private void loadItineraries() {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            return;
        }

        HttpUtil.get("itinerary/user/" + userId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    Toast.makeText(ReminderMainActivity.this, "加载行程失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<List<Itinerary>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<List<Itinerary>>>() {
                        }.getType());

                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    handler.post(() -> {
                        itineraryList.clear();
                        itineraryList.addAll(apiResponse.getData());
                    });
                }
            }
        });
    }

    private void showItinerarySelectDialog() {
        if (itineraryList.isEmpty()) {
            Toast.makeText(this, "暂无行程，请先创建行程", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择行程");

        String[] titles = new String[itineraryList.size()];
        for (int i = 0; i < itineraryList.size(); i++) {
            titles[i] = itineraryList.get(i).getTitle() + " - " + itineraryList.get(i).getDestination();
        }

        builder.setItems(titles, (dialog, which) -> {
            selectedItinerary = itineraryList.get(which);
            tvSelectedItinerary.setText("已选择: " + selectedItinerary.getTitle());
            tvSelectedItinerary.setVisibility(View.VISIBLE);
            Toast.makeText(this, "已选择行程: " + selectedItinerary.getTitle(), Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private static class ItineraryChecklistAdapter extends RecyclerView.Adapter<ItineraryChecklistAdapter.ViewHolder> {

        private final Context context;
        private final List<ItineraryEntity> itineraries;
        private final String[] names;
        private final String[] details;
        private final ChecklistDao checklistDao;

        ItineraryChecklistAdapter(Context context, List<ItineraryEntity> itineraries,
                String[] names, String[] details, ChecklistDao checklistDao) {
            this.context = context;
            this.itineraries = itineraries;
            this.names = names;
            this.details = details;
            this.checklistDao = checklistDao;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvTitle.setText(names[position]);
            holder.tvSubtitle.setText(details[position]);

            holder.itemView.setOnClickListener(v -> {
                Long itineraryId = itineraries.get(position).getId();
                ChecklistActivity.startViewMode(context, itineraryId);
            });
        }

        @Override
        public int getItemCount() {
            return itineraries.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvSubtitle;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(android.R.id.text1);
                tvSubtitle = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
