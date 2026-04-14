package com.travelplanner.ui.itinerary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.dto.ItineraryAdjustRequest;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.BudgetInfo;
import com.travelplanner.model.CrowdForecastResponse;
import com.travelplanner.model.Itinerary;
import com.travelplanner.ui.budget.BudgetActivity;
import com.travelplanner.ui.checklist.ChecklistActivity;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItineraryDetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvDestination, tvDays, tvBudget, tvStatus;
    private RecyclerView rvDayPlans;
    private View btnBudget, btnChecklist, btnRegenerate, btnAdjust;
    private ImageButton btnDelete;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Long itineraryId;
    private Itinerary itinerary;
    private List<Itinerary.DayPlan> dayPlans = new ArrayList<>();
    private DayPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_detail);

        itineraryId = getIntent().getLongExtra("itinerary_id", 0);

        initViews();
        loadItinerary();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvDestination = findViewById(R.id.tv_destination);
        tvDays = findViewById(R.id.tv_days);
        tvBudget = findViewById(R.id.tv_budget);
        tvStatus = findViewById(R.id.tv_status);
        rvDayPlans = findViewById(R.id.rv_day_plans);
        btnBudget = findViewById(R.id.btn_budget);
        btnChecklist = findViewById(R.id.btn_checklist);
        btnDelete = findViewById(R.id.btn_delete);
        btnRegenerate = findViewById(R.id.btn_regenerate);
        btnAdjust = findViewById(R.id.btn_adjust);

        adapter = new DayPlanAdapter(dayPlans);
        rvDayPlans.setLayoutManager(new LinearLayoutManager(this));
        rvDayPlans.setAdapter(adapter);

        btnBudget.setOnClickListener(v -> {
            Intent intent = new Intent(this, BudgetActivity.class);
            intent.putExtra("itinerary_id", itineraryId);
            startActivity(intent);
        });

        btnChecklist.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChecklistActivity.class);
            intent.putExtra("itinerary_id", itineraryId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> showDeleteConfirmDialog());
        
        btnRegenerate.setOnClickListener(v -> showRegenerateConfirmDialog());
        
        btnAdjust.setOnClickListener(v -> showAdjustDialog());
    }

    private void loadItinerary() {
        HttpUtil.get("itinerary/" + itineraryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> Toast.makeText(ItineraryDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<Itinerary> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Itinerary>>(){}.getType());

                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    handler.post(() -> {
                        itinerary = apiResponse.getData();
                        updateUI();
                    });
                } else {
                    handler.post(() -> Toast.makeText(ItineraryDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void updateUI() {
        if (itinerary == null) return;

        tvTitle.setText(itinerary.getTitle());
        tvDestination.setText(itinerary.getDestination());
        tvDays.setText(itinerary.getDays() + "天");

        if (itinerary.getBudget() != null) {
            tvBudget.setText("预算: ¥" + itinerary.getBudget());
        }

        String status = itinerary.getStatus();
        if (status != null) {
            switch (status) {
                case "draft":
                    tvStatus.setText("草稿");
                    tvStatus.setBackgroundColor(getResources().getColor(R.color.text_hint));
                    break;
                case "planning":
                    tvStatus.setText("规划中");
                    tvStatus.setBackgroundColor(getResources().getColor(R.color.accent));
                    break;
                case "confirmed":
                    tvStatus.setText("已确认");
                    tvStatus.setBackgroundColor(getResources().getColor(R.color.primary));
                    break;
                case "completed":
                    tvStatus.setText("已完成");
                    tvStatus.setBackgroundColor(getResources().getColor(R.color.success));
                    break;
                case "cancelled":
                    tvStatus.setText("已取消");
                    tvStatus.setBackgroundColor(getResources().getColor(R.color.error));
                    break;
                default:
                    tvStatus.setText(status);
                    tvStatus.setBackgroundColor(getResources().getColor(R.color.text_hint));
                    break;
            }
        }

        if (itinerary.getDayPlans() != null) {
            dayPlans.clear();
            dayPlans.addAll(itinerary.getDayPlans());
            adapter.notifyDataSetChanged();
        }
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("删除行程")
                .setMessage("确定要删除这个行程吗？此操作无法撤销。")
                .setPositiveButton("删除", (dialog, which) -> deleteItinerary())
                .setNegativeButton("取消", null)
                .show();
    }

    private void deleteItinerary() {
        HttpUtil.delete("itinerary/" + itineraryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> Toast.makeText(ItineraryDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                if (response.isSuccessful()) {
                    handler.post(() -> {
                        Toast.makeText(ItineraryDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    handler.post(() -> Toast.makeText(ItineraryDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void showRegenerateConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("重新生成行程")
                .setMessage("确定要重新生成行程吗？当前日程安排将被替换。")
                .setPositiveButton("重新生成", (dialog, which) -> regenerateItinerary())
                .setNegativeButton("取消", null)
                .show();
    }

    private void regenerateItinerary() {
        if (itinerary == null) return;
        
        btnRegenerate.setEnabled(false);
        
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setTitle("正在重新生成");
        progressDialog.setMessage("AI正在生成行程，请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        
        String json = "{\"userId\":" + TravelPlannerApp.getInstance().getUserId() +
                ",\"destination\":\"" + itinerary.getDestination() + "\"" +
                ",\"days\":" + itinerary.getDays() +
                ",\"peopleCount\":" + itinerary.getPeopleCount() +
                ",\"budget\":\"" + itinerary.getBudget() + "\"" +
                ",\"travelStyle\":\"" + (itinerary.getTravelStyle() != null ? itinerary.getTravelStyle() : "休闲") + "\"}";

        HttpUtil.post("itinerary/regenerate/" + itineraryId, json, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    progressDialog.dismiss();
                    btnRegenerate.setEnabled(true);
                    Toast.makeText(ItineraryDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<Itinerary> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Itinerary>>(){}.getType());

                handler.post(() -> {
                    progressDialog.dismiss();
                    btnRegenerate.setEnabled(true);
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Toast.makeText(ItineraryDetailActivity.this, "重新生成成功！", Toast.LENGTH_SHORT).show();
                        loadItinerary();
                    } else {
                        Toast.makeText(ItineraryDetailActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showAdjustDialog() {
        String[] options = {"删除景点", "添加景点", "调整时间"};
        new AlertDialog.Builder(this)
                .setTitle("微调行程")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            showRemoveSpotDialog();
                            break;
                        case 1:
                            showAddSpotDialog();
                            break;
                        case 2:
                            showAdjustTimeDialog();
                            break;
                    }
                })
                .show();
    }

    private void showRemoveSpotDialog() {
        if (dayPlans == null || dayPlans.isEmpty()) {
            Toast.makeText(this, "暂无景点可删除", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> spotNames = new ArrayList<>();
        List<Itinerary.SpotPlan> allSpots = new ArrayList<>();
        
        for (Itinerary.DayPlan dayPlan : dayPlans) {
            if (dayPlan.getSpots() != null) {
                for (Itinerary.SpotPlan spot : dayPlan.getSpots()) {
                    spotNames.add("第" + dayPlan.getDayNumber() + "天 - " + spot.getAttractionName());
                    allSpots.add(spot);
                }
            }
        }

        if (spotNames.isEmpty()) {
            Toast.makeText(this, "暂无景点可删除", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] items = spotNames.toArray(new String[0]);
        boolean[] checked = new boolean[items.length];

        new AlertDialog.Builder(this)
                .setTitle("选择要删除的景点")
                .setMultiChoiceItems(items, checked, (dialog, which, isChecked) -> {
                    checked[which] = isChecked;
                })
                .setPositiveButton("删除", (dialog, which) -> {
                    List<Long> spotIdsToRemove = new ArrayList<>();
                    List<String> spotNamesToRemove = new ArrayList<>();
                    for (int i = 0; i < checked.length; i++) {
                        if (checked[i]) {
                            Itinerary.SpotPlan spot = allSpots.get(i);
                            if (spot.getAttractionId() != null && spot.getAttractionId() > 0) {
                                spotIdsToRemove.add(spot.getAttractionId());
                            } else {
                                spotNamesToRemove.add(spot.getAttractionName());
                            }
                        }
                    }

                    if (!spotIdsToRemove.isEmpty() || !spotNamesToRemove.isEmpty()) {
                        Toast.makeText(this, "正在删除...", Toast.LENGTH_SHORT).show();
                        adjustItinerary(spotIdsToRemove, spotNamesToRemove, null, null);
                    } else {
                        Toast.makeText(this, "请选择要删除的景点", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showAddSpotDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("添加景点");
        
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_spot, null);
        android.widget.EditText etSpotName = dialogView.findViewById(R.id.et_spot_name);
        android.widget.Spinner spinnerDay = dialogView.findViewById(R.id.spinner_day);
        
        List<String> days = new ArrayList<>();
        for (int i = 1; i <= itinerary.getDays(); i++) {
            days.add("第" + i + "天");
        }
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapter);
        
        builder.setView(dialogView);
        builder.setPositiveButton("添加", (dialog, which) -> {
            String spotName = etSpotName.getText().toString().trim();
            int dayNumber = spinnerDay.getSelectedItemPosition() + 1;
            
            if (spotName.isEmpty()) {
                Toast.makeText(this, "请输入景点名称", Toast.LENGTH_SHORT).show();
                return;
            }
            
            addSpotToItinerary(spotName, dayNumber);
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showAdjustTimeDialog() {
        if (dayPlans == null || dayPlans.isEmpty()) {
            Toast.makeText(this, "暂无景点可调整", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> spotNames = new ArrayList<>();
        List<Itinerary.SpotPlan> allSpots = new ArrayList<>();
        List<Integer> dayNumbers = new ArrayList<>();
        
        for (Itinerary.DayPlan dayPlan : dayPlans) {
            if (dayPlan.getSpots() != null) {
                for (Itinerary.SpotPlan spot : dayPlan.getSpots()) {
                    spotNames.add("第" + dayPlan.getDayNumber() + "天 - " + spot.getAttractionName());
                    allSpots.add(spot);
                    dayNumbers.add(dayPlan.getDayNumber());
                }
            }
        }

        if (spotNames.isEmpty()) {
            Toast.makeText(this, "暂无景点可调整", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] items = spotNames.toArray(new String[0]);
        
        new AlertDialog.Builder(this)
                .setTitle("选择要调整时间的景点")
                .setItems(items, (dialog, which) -> {
                    Itinerary.SpotPlan selectedSpot = allSpots.get(which);
                    int dayNumber = dayNumbers.get(which);
                    showTimeAdjustDialog(selectedSpot, dayNumber);
                })
                .show();
    }

    private void showTimeAdjustDialog(Itinerary.SpotPlan spot, int dayNumber) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("调整 " + spot.getAttractionName() + " 的时间");
        
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_adjust_time, null);
        android.widget.EditText etArrivalTime = dialogView.findViewById(R.id.et_arrival_time);
        android.widget.EditText etLeaveTime = dialogView.findViewById(R.id.et_leave_time);
        android.widget.EditText etDuration = dialogView.findViewById(R.id.et_duration);
        
        etArrivalTime.setText(spot.getArrivalTime());
        etLeaveTime.setText(spot.getLeaveTime());
        etDuration.setText(String.valueOf(spot.getStayDuration()));
        
        builder.setView(dialogView);
        builder.setPositiveButton("保存", (dialog, which) -> {
            String arrivalTime = etArrivalTime.getText().toString().trim();
            String leaveTime = etLeaveTime.getText().toString().trim();
            String durationStr = etDuration.getText().toString().trim();
            
            if (arrivalTime.isEmpty() || leaveTime.isEmpty() || durationStr.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int duration = Integer.parseInt(durationStr);
            adjustSpotTime(spot.getAttractionName(), dayNumber, arrivalTime, leaveTime, duration);
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void adjustItinerary(List<Long> removeSpots, List<String> removeSpotNames, List<ItineraryAdjustRequest.AttractionToAdd> addSpots,
                                  Map<String, Map<String, ItineraryAdjustRequest.SpotAdjustment>> adjustments) {
        ItineraryAdjustRequest request = new ItineraryAdjustRequest();
        request.setRemoveSpots(removeSpots);
        request.setRemoveSpotNames(removeSpotNames);
        request.setAddSpots(addSpots);
        request.setAdjustments(adjustments);

        String json = HttpUtil.toJson(request);
        Log.d("ItineraryDetail", "发送删除请求: " + json);

        HttpUtil.post("itinerary/" + itineraryId + "/adjust", json, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                Log.e("ItineraryDetail", "删除请求失败", e);
                handler.post(() -> Toast.makeText(ItineraryDetailActivity.this, "调整失败: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                Log.d("ItineraryDetail", "删除响应: " + body);
                ApiResponse<Void> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Void>>(){}.getType());

                handler.post(() -> {
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(ItineraryDetailActivity.this, "调整成功", Toast.LENGTH_SHORT).show();
                        loadItinerary();
                    } else {
                        Toast.makeText(ItineraryDetailActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addSpotToItinerary(String spotName, int dayNumber) {
        List<ItineraryAdjustRequest.AttractionToAdd> addSpots = new ArrayList<>();
        ItineraryAdjustRequest.AttractionToAdd addSpot = new ItineraryAdjustRequest.AttractionToAdd();
        addSpot.setAttractionName(spotName);
        addSpot.setDayNumber(dayNumber);
        addSpot.setOrderNum(0);
        addSpot.setArrivalTime("09:00");
        addSpot.setLeaveTime("12:00");
        addSpot.setStayDuration(180);
        addSpots.add(addSpot);
        
        adjustItinerary(null, null, addSpots, null);
    }

    private void adjustSpotTime(String attractionName, int dayNumber, String arrivalTime, String leaveTime, int duration) {
        Map<String, Map<String, ItineraryAdjustRequest.SpotAdjustment>> adjustments = new java.util.HashMap<>();
        Map<String, ItineraryAdjustRequest.SpotAdjustment> dayAdjustments = new java.util.HashMap<>();

        ItineraryAdjustRequest.SpotAdjustment adjustment = new ItineraryAdjustRequest.SpotAdjustment();
        adjustment.setArrivalTime(arrivalTime);
        adjustment.setLeaveTime(leaveTime);
        adjustment.setStayDuration(duration);

        dayAdjustments.put(attractionName, adjustment);
        adjustments.put(String.valueOf(dayNumber), dayAdjustments);

        adjustItinerary(null, null, null, adjustments);
    }

    private void showCrowdForecastDialog(Itinerary.DayPlan dayPlan) {
        if (dayPlan.getSpots() == null || dayPlan.getSpots().isEmpty()) {
            Toast.makeText(this, "当天暂无景点安排", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("第" + dayPlan.getDayNumber() + "天 - 错峰推测");

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(40, 30, 40, 20);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(progressParams);
        container.addView(progressBar);

        TextView loadingText = new TextView(this);
        loadingText.setText("正在推测客流量...");
        loadingText.setGravity(Gravity.CENTER);
        loadingText.setPadding(0, 20, 0, 0);
        container.addView(loadingText);

        builder.setView(container);
        AlertDialog dialog = builder.create();
        dialog.show();

        LinearLayout resultContainer = new LinearLayout(this);
        resultContainer.setOrientation(LinearLayout.VERTICAL);
        resultContainer.setPadding(40, 20, 40, 20);

        int[] completedCount = {0};
        int totalSpots = dayPlan.getSpots().size();

        for (Itinerary.SpotPlan spot : dayPlan.getSpots()) {
            fetchCrowdForecast(spot.getAttractionId(), spot.getAttractionName(), itinerary.getDestination(), resultContainer, () -> {
                completedCount[0]++;
                if (completedCount[0] == totalSpots) {
                    dialog.dismiss();
                    
                    AlertDialog.Builder resultBuilder = new AlertDialog.Builder(this);
                    resultBuilder.setTitle("第" + dayPlan.getDayNumber() + "天 - 错峰推测结果");
                    resultBuilder.setView(resultContainer);
                    resultBuilder.setPositiveButton("知道了", null);
                    resultBuilder.show();
                }
            });
        }
    }

    private void fetchCrowdForecast(Long attractionId, String attractionName, String city, LinearLayout container, Runnable onComplete) {
        if (attractionId == null || attractionId == 0) {
            android.util.Log.d("CrowdForecast", "attractionId is null, searching by name: " + attractionName + ", city: " + city);
            searchAttractionAndFetchCrowd(attractionName, city, container, onComplete);
            return;
        }
        
        android.util.Log.d("CrowdForecast", "Fetching crowd for attractionId: " + attractionId + ", name: " + attractionName);
        
        HttpUtil.get("external/crowd/forecast/" + attractionId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                android.util.Log.e("CrowdForecast", "API call failed: " + e.getMessage());
                handler.post(() -> {
                    addCrowdItem(container, attractionName, "未知", Color.GRAY, "无法获取客流信息: " + e.getMessage());
                    onComplete.run();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                android.util.Log.d("CrowdForecast", "Response code: " + response.code() + ", for attractionId: " + attractionId);
                String body = response.body().string();
                android.util.Log.d("CrowdForecast", "Response body: " + body);
                
                ApiResponse<CrowdForecastResponse> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<CrowdForecastResponse>>(){}.getType());

                handler.post(() -> {
                    if (apiResponse == null) {
                        android.util.Log.e("CrowdForecast", "apiResponse is null");
                        addCrowdItem(container, attractionName, "未知", Color.GRAY, "数据解析失败");
                        onComplete.run();
                        return;
                    }
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        CrowdForecastResponse forecast = apiResponse.getData();
                        if (forecast.getForecasts() != null && !forecast.getForecasts().isEmpty()) {
                            CrowdForecastResponse.DailyForecast todayForecast = forecast.getForecasts().get(0);
                            String level = todayForecast.getCrowdLevel();
                            String recommendation = todayForecast.getRecommendation();
                            String bestTime = todayForecast.getBestTime();
                            
                            int color;
                            String levelText;
                            if ("低".equals(level)) {
                                color = Color.parseColor("#4CAF50");
                                levelText = "空闲";
                            } else if ("中".equals(level)) {
                                color = Color.parseColor("#FF9800");
                                levelText = "较多";
                            } else if ("较高".equals(level)) {
                                color = Color.parseColor("#FFEB3B");
                                levelText = "超多";
                            } else {
                                color = Color.parseColor("#F44336");
                                levelText = "爆满";
                            }
                            
                            String info = recommendation;
                            if (bestTime != null && !bestTime.isEmpty()) {
                                info += "\n最佳游览: " + bestTime;
                            }
                            
                            addCrowdItem(container, attractionName, levelText, color, info);
                        } else {
                            addCrowdItem(container, attractionName, "未知", Color.GRAY, "暂无客流数据");
                        }
                    } else {
                        android.util.Log.e("CrowdForecast", "API error: " + apiResponse.getMessage());
                        addCrowdItem(container, attractionName, "未知", Color.GRAY, "获取失败: " + apiResponse.getMessage());
                    }
                    onComplete.run();
                });
            }
        });
    }
    
    private void searchAttractionAndFetchCrowd(String attractionName, String city, LinearLayout container, Runnable onComplete) {
        String encodedName = "";
        String encodedCity = "";
        try {
            encodedName = java.net.URLEncoder.encode(attractionName, "UTF-8");
            encodedCity = city != null ? java.net.URLEncoder.encode(city, "UTF-8") : "";
        } catch (Exception e) {
            encodedName = attractionName;
            encodedCity = city != null ? city : "";
        }
        
        HttpUtil.get("external/crowd/forecast/name?attractionName=" + encodedName + "&city=" + encodedCity, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                android.util.Log.e("CrowdForecast", "Forecast by name API failed: " + e.getMessage());
                handler.post(() -> {
                    addCrowdItem(container, attractionName, "未知", Color.GRAY, "获取预测失败");
                    onComplete.run();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                android.util.Log.d("CrowdForecast", "Forecast by name response: " + body);
                
                try {
                    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ApiResponse<com.travelplanner.model.CrowdForecastResponse>>(){}.getType();
                    ApiResponse<com.travelplanner.model.CrowdForecastResponse> apiResponse = HttpUtil.fromJson(body, type);
                    
                    if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                        android.util.Log.d("CrowdForecast", "API success, parsing forecast data");
                        com.travelplanner.model.CrowdForecastResponse forecast = apiResponse.getData();
                        android.util.Log.d("CrowdForecast", "Forecast attractionName: " + forecast.getAttractionName());
                        if (forecast.getForecasts() != null && !forecast.getForecasts().isEmpty()) {
                            com.travelplanner.model.CrowdForecastResponse.DailyForecast todayForecast = forecast.getForecasts().get(0);
                            String level = todayForecast.getCrowdLevel();
                            android.util.Log.d("CrowdForecast", "Crowd level raw: " + level);
                            
                            int color;
                            String levelText;
                            if ("低".equals(level)) {
                                color = Color.parseColor("#4CAF50");
                                levelText = "空闲";
                            } else if ("中".equals(level)) {
                                color = Color.parseColor("#FF9800");
                                levelText = "较多";
                            } else if ("较高".equals(level)) {
                                color = Color.parseColor("#FFEB3B");
                                levelText = "超多";
                            } else {
                                color = Color.parseColor("#F44336");
                                levelText = "爆满";
                            }
                            
                            String info = "最佳时段: " + (todayForecast.getBestTime() != null ? todayForecast.getBestTime() : "未知");
                            handler.post(() -> {
                                addCrowdItem(container, attractionName, levelText, color, info);
                                onComplete.run();
                            });
                        } else {
                            handler.post(() -> {
                                addCrowdItem(container, attractionName, "未知", Color.GRAY, "无预测数据");
                                onComplete.run();
                            });
                        }
                    } else {
                        String errorMsg = apiResponse != null ? apiResponse.getMessage() : "景点未收录";
                        android.util.Log.d("CrowdForecast", "API returned error: " + errorMsg);
                        handler.post(() -> {
                            addCrowdItem(container, attractionName, "未知", Color.GRAY, errorMsg);
                            onComplete.run();
                        });
                    }
                } catch (Exception e) {
                    android.util.Log.e("CrowdForecast", "Parse forecast response failed: " + e.getMessage());
                    handler.post(() -> {
                        addCrowdItem(container, attractionName, "未知", Color.GRAY, "解析失败");
                        onComplete.run();
                    });
                }
            }
        });
    }

    private void addCrowdItem(LinearLayout container, String name, String level, int color, String info) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(0, 16, 0, 16);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemLayout.setLayoutParams(itemParams);

        TextView statusBadge = new TextView(this);
        statusBadge.setText(level);
        statusBadge.setTextColor(Color.WHITE);
        statusBadge.setBackgroundColor(color);
        statusBadge.setPadding(20, 10, 20, 10);
        statusBadge.setTextSize(12);
        statusBadge.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        badgeParams.setMargins(0, 0, 20, 0);
        statusBadge.setLayoutParams(badgeParams);
        itemLayout.addView(statusBadge);

        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textContainer.setLayoutParams(textParams);

        TextView nameText = new TextView(this);
        nameText.setText(name);
        nameText.setTextSize(15);
        nameText.setTextColor(getResources().getColor(R.color.text_primary));
        nameText.setTypeface(null, android.graphics.Typeface.BOLD);
        textContainer.addView(nameText);

        TextView infoText = new TextView(this);
        infoText.setText(info);
        infoText.setTextSize(12);
        infoText.setTextColor(getResources().getColor(R.color.text_secondary));
        infoText.setPadding(0, 6, 0, 0);
        textContainer.addView(infoText);

        itemLayout.addView(textContainer);
        container.addView(itemLayout);

        View divider = new View(this);
        divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        divider.setLayoutParams(dividerParams);
        container.addView(divider);
    }

    class DayPlanAdapter extends RecyclerView.Adapter<DayPlanAdapter.ViewHolder> {
        private List<Itinerary.DayPlan> list;

        DayPlanAdapter(List<Itinerary.DayPlan> list) { this.list = list; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_plan, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Itinerary.DayPlan dayPlan = list.get(position);
            holder.tvDayNumber.setText("第" + dayPlan.getDayNumber() + "天");
            holder.tvDate.setText(dayPlan.getDate());
            holder.tvWeather.setText(dayPlan.getWeather());
            holder.tvTips.setText(dayPlan.getTips());

            StringBuilder spots = new StringBuilder();
            if (dayPlan.getSpots() != null) {
                for (Itinerary.SpotPlan spot : dayPlan.getSpots()) {
                    spots.append(spot.getArrivalTime()).append(" ").append(spot.getAttractionName()).append("\n");
                }
            }
            holder.tvSpots.setText(spots.toString());
            
            holder.btnCrowdForecast.setOnClickListener(v -> {
                showCrowdForecastDialog(dayPlan);
            });
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDayNumber, tvDate, tvWeather, tvTips, tvSpots;
            com.google.android.material.button.MaterialButton btnCrowdForecast;
            ViewHolder(View itemView) {
                super(itemView);
                tvDayNumber = itemView.findViewById(R.id.tv_day_number);
                tvDate = itemView.findViewById(R.id.tv_date);
                tvWeather = itemView.findViewById(R.id.tv_weather);
                tvTips = itemView.findViewById(R.id.tv_tips);
                tvSpots = itemView.findViewById(R.id.tv_spots);
                btnCrowdForecast = itemView.findViewById(R.id.btn_crowd_forecast);
            }
        }
    }
}
