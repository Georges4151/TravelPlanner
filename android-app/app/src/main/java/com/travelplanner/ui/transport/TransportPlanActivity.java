package com.travelplanner.ui.transport;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.adapter.DrivingRouteAdapter;
import com.travelplanner.adapter.TicketAdapter;
import com.travelplanner.model.DrivingRouteSegment;
import com.travelplanner.model.Itinerary;
import com.travelplanner.model.TicketInfo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TransportPlanActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private TicketAdapter ticketAdapter;
    private DrivingRouteAdapter drivingRouteAdapter;

    private TextView tvTitle;
    private TextView tvItineraryInfo;
    private Button btnSearch;
    // 【替换】智能出行建议卡片
    private LinearLayout llSmartTipsCard;
    private LinearLayout llSmartTipsList;
    private View dividerTickets;
    private TextView tvTicketsTitle;
    private RecyclerView rvTickets;
    private View dividerDriving;
    private TextView tvDrivingTitle;
    private RecyclerView rvDrivingRoute;
    private ProgressBar progressBar;
    private TextView tvLoadingStatus;
    private LinearLayout llLoadingContainer;
    private TextView tvError;

    private List<TicketInfo> currentTickets = new ArrayList<>();

    private Itinerary itinerary;
    private List<String> selectedModes;
    private String customOrigin;
    private String customDestination;

    private TransportPlanManager planManager;

    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_plan);

        ticketAdapter = new TicketAdapter();
        drivingRouteAdapter = new DrivingRouteAdapter();

        initViews();
        setupToolbar();
        setupRecyclerView();
        requestLocationPermission();

        loadIntentData();
    }

    private void loadIntentData() {
        if (getIntent() != null) {
            String itineraryJson = getIntent().getStringExtra("itineraryJson");
            String modesJson = getIntent().getStringExtra("selectedModes");
            customOrigin = getIntent().getStringExtra("customOrigin");
            customDestination = getIntent().getStringExtra("customDestination");

            if (itineraryJson != null && !itineraryJson.isEmpty()) {
                try {
                    itinerary = gson.fromJson(itineraryJson, Itinerary.class);
                    Log.d("TransportPlan", "加载的行程: " + itinerary.getTitle());
                } catch (Exception e) {
                    Log.e("TransportPlan", "解析行程失败", e);
                }
            }

            if (modesJson != null && !modesJson.isEmpty()) {
                try {
                    selectedModes = gson.fromJson(modesJson, new TypeToken<List<String>>() {
                    }.getType());
                    Log.d("TransportPlan", "选中的出行方式: " + selectedModes);
                } catch (Exception e) {
                    Log.e("TransportPlan", "解析出行方式失败", e);
                }
            }

            if (itinerary != null) {
                updateTitle();
            }

            if (itinerary != null && selectedModes != null && !selectedModes.isEmpty()) {
                btnSearch.setEnabled(true);
                btnSearch.postDelayed(() -> searchRouteWithAI(), 500);
            }
        }
    }

    private void updateTitle() {
        if (itinerary != null) {
            String title = itinerary.getTitle();
            if (itinerary.getDestination() != null && !itinerary.getDestination().isEmpty()) {
                title += " - " + itinerary.getDestination();
            }
            tvTitle.setText(title);

            StringBuilder info = new StringBuilder();
            info.append("总天数: ").append(itinerary.getDays()).append("天\n");

            info.append("用户设置起点: ").append(customOrigin != null ? customOrigin : "未设置").append("\n");
            info.append("用户设置终点: ").append(customDestination != null ? customDestination : "未设置").append("\n");

            if (selectedModes != null && !selectedModes.isEmpty()) {
                info.append("出行方式: ");
                for (int i = 0; i < selectedModes.size(); i++) {
                    if (i > 0)
                        info.append("、");
                    info.append(getTransportModeName(selectedModes.get(i)));
                }
            }

            tvItineraryInfo.setText(info.toString());
            
            // 【新增】生成智能出行建议
            generateSmartTips();
        }
    }
    
    /**
     * 【新增】生成智能出行建议卡片
     */
    private void generateSmartTips() {
        if (itinerary == null || llSmartTipsList == null) return;
        
        llSmartTipsList.removeAllViews();
        
        List<String> tips = new ArrayList<>();
        
        // 根据起止地点推荐交通方式
        String origin = customOrigin != null ? customOrigin : "";
        String dest = itinerary.getDestination() != null ? itinerary.getDestination() : "";
        
        // 推荐交通方式
        tips.add(getTransportRecommendation(origin, dest));
        
        // 天数相关建议
        int days = itinerary.getDays() != null ? itinerary.getDays() : 1;
        if (days >= 3) {
            tips.add("📅 " + days + "天行程建议提前预订机票/酒店，可节省20%-40%");
        } else if (days >= 2) {
            tips.add("🎫 短途旅行建议购买往返联程票，更经济");
        }
        
        // 预算估算
        String budgetTip = getBudgetEstimate(origin, dest, days);
        if (!budgetTip.isEmpty()) {
            tips.add("💰 预算约 " + budgetTip);
        }
        
        // 出行时间提示
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        if (month >= 5 && month <= 10) {
            tips.add("☀️ 当前为旅游旺季，建议提前规划避开高峰时段");
        } else {
            tips.add("❄️ 当前为淡季出游，性价比更高");
        }
        
        // 渲染建议项
        for (String tip : tips) {
            TextView tipView = new TextView(this);
            tipView.setText(tip);
            tipView.setTextSize(13f);
            tipView.setTextColor(0xFF37474F);
            tipView.setPadding(0, 6, 0, 6);
            llSmartTipsList.addView(tipView);
        }
    }
    
    /**
     * 【新增】根据起止点推荐交通方式
     */
    private String getTransportRecommendation(String origin, String destination) {
        if (origin == null || destination == null) {
            return "🚀 建议使用AI规划获取最优路线方案";
        }
        
        // 判断是否跨城
        boolean isCrossCity = !origin.equals(destination) && 
                            (origin.length() > 2 && destination.length() > 2);
        
        if (isCrossCity) {
            // 检查是否包含常见城市对
            String[][] commonRoutes = {{"北京", "上海"}, {"北京", "杭州"}, {"北京", "广州"},
                                       {"上海", "北京"}, {"上海", "杭州"}, {"广州", "深圳"}};
            
            for (String[] route : commonRoutes) {
                if ((origin.contains(route[0]) && destination.contains(route[1])) ||
                    (origin.contains(route[1]) && destination.contains(route[0]))) {
                    return "✈️ " + origin + "→" + destination + " 推荐高铁/飞机(" + estimateTravelTime(route[0], route[1]) + ")";
                }
            }
            
            return "🚄 " + origin + "→" + destination + " 跨城出行推荐高铁或自驾";
        }
        
        return "🚌 市内出行推荐地铁/公交+打车组合";
    }
    
    /**
     * 【新增】估算旅行时间（简化版）
     */
    private String estimateTravelTime(String city1, String city2) {
        // 简化的距离估算
        String[] longDistances = {"北京", "广州", "上海", "成都"};
        boolean isLongRoute = false;
        for (String city : longDistances) {
            if (city1.contains(city) || city2.contains(city)) {
                isLongRoute = true;
                break;
            }
        }
        
        return isLongRoute ? "约4.5h高铁" : "约2-3h";
    }
    
    /**
     * 【新增】预算估算
     */
    private String getBudgetEstimate(String origin, String destination, int days) {
        try {
            // 简单的预算估算逻辑
            boolean isCrossCity = !origin.equals(destination);
            int baseCost = isCrossCity ? 1500 : 300;  // 跨城基础费用更高
            int dailyCost = 500;  // 每日交通预估
            
            int totalEstimate = baseCost + (dailyCost * days);
            
            if (totalEstimate >= 10000) {
                return "¥" + (totalEstimate / 1000) + "k - ¥" + (totalEstimate / 800 + 1) + "k";
            } else if (totalEstimate >= 5000) {
                return "¥" + (totalEstimate / 1000) + "k - ¥" + (totalEstimate / 500) + "k";
            } else {
                return "¥" + (totalEstimate - 200) + " - ¥" + (totalEstimate + 500);
            }
        } catch (Exception e) {
            return "根据实际路线计算";
        }
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvItineraryInfo = findViewById(R.id.tvItineraryInfo);
        btnSearch = findViewById(R.id.btnSearch);
        
        // 【替换】智能出行建议卡片
        llSmartTipsCard = findViewById(R.id.llSmartTipsCard);
        llSmartTipsList = findViewById(R.id.llSmartTipsList);
        
        dividerTickets = findViewById(R.id.dividerTickets);
        tvTicketsTitle = findViewById(R.id.tvTicketsTitle);
        rvTickets = findViewById(R.id.rvTickets);
        dividerDriving = findViewById(R.id.dividerDriving);
        tvDrivingTitle = findViewById(R.id.tvDrivingTitle);
        rvDrivingRoute = findViewById(R.id.rvDrivingRoute);
        progressBar = findViewById(R.id.progressBar);
        tvLoadingStatus = findViewById(R.id.tvLoadingStatus);
        llLoadingContainer = findViewById(R.id.llLoadingContainer);
        tvError = findViewById(R.id.tvError);

        btnSearch.setOnClickListener(v -> searchRouteWithAI());
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

    private void setupRecyclerView() {
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        rvTickets.setAdapter(ticketAdapter);

        rvDrivingRoute.setLayoutManager(new LinearLayoutManager(this));
        rvDrivingRoute.setAdapter(drivingRouteAdapter);

        ticketAdapter.setOnTicketClickListener(ticket -> {
            Toast.makeText(this, "已选择: " + ticket.getTripNumber(), Toast.LENGTH_SHORT).show();
        });
    }

    private void searchRouteWithAI() {
        if (itinerary == null) {
            Toast.makeText(this, "行程数据为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedModes == null || selectedModes.isEmpty()) {
            Toast.makeText(this, "请选择出行方式", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customOrigin == null || customOrigin.isEmpty()) {
            Toast.makeText(this, "请设置出发地", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customDestination == null || customDestination.isEmpty()) {
            Toast.makeText(this, "请设置目的地", Toast.LENGTH_SHORT).show();
            return;
        }

        // 显示加载状态
        tvError.setVisibility(View.GONE);
        llSmartTipsCard.setVisibility(View.GONE);  // 【替换】隐藏建议卡片，显示结果
        rvTickets.setVisibility(View.GONE);
        dividerTickets.setVisibility(View.GONE);
        tvTicketsTitle.setVisibility(View.GONE);
        rvDrivingRoute.setVisibility(View.GONE);
        dividerDriving.setVisibility(View.GONE);
        tvDrivingTitle.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        llLoadingContainer.setVisibility(View.VISIBLE);
        tvLoadingStatus.setText("正在启动规划...");

        // 创建规划管理器
        planManager = new TransportPlanManager();
        planManager.setCallback(new TransportPlanManager.PlanningCallback() {
            @Override
            public void onProgress(int currentDay, int totalDays, int currentMode, int totalModes) {
                String status = "规划中: 第" + currentDay + "/" + totalDays + "天";
                if (currentMode > 0 && totalModes > 0) {
                    status += " (方式" + currentMode + "/" + totalModes + ")";
                }
                tvLoadingStatus.setText(status);
            }

            @Override
            public void onDayComplete(int dayNumber) {
                Toast.makeText(TransportPlanActivity.this,
                        "第" + dayNumber + "天规划完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(List<TicketInfo> tickets, List<DrivingRouteSegment> drivingSegments,
                                  String totalDistance, BigDecimal totalTollFee) {
                progressBar.setVisibility(View.GONE);
                llLoadingContainer.setVisibility(View.GONE);

                currentTickets.clear();

                // 合并所有车次（已按天数排序）
                if (tickets != null && !tickets.isEmpty()) {
                    currentTickets.addAll(tickets);
                    showTickets(currentTickets);
                }

                // 显示驾车路线（已按天数排序）
                if (drivingSegments != null && !drivingSegments.isEmpty()) {
                    showDrivingRoute(drivingSegments);
                }

                // 显示汇总信息 - 【已移至规划结果区域】
                // 汇总信息现在显示在 Toast 提示和结果列表中

                // 构建汇总信息
                StringBuilder summary = new StringBuilder();
                if (totalDistance != null && !totalDistance.isEmpty()) {
                    summary.append("全程距离：").append(totalDistance);
                }
                if (totalTollFee != null && totalTollFee.compareTo(BigDecimal.ZERO) > 0) {
                    if (summary.length() > 0) summary.append(" | ");
                    summary.append("过路费：¥").append(totalTollFee.toString());
                }

                // 计算公共交通总花费
                BigDecimal transitCost = BigDecimal.ZERO;
                if (tickets != null && !tickets.isEmpty()) {
                    for (TicketInfo ticket : tickets) {
                        if (ticket.getPrice() != null) {
                            transitCost = transitCost.add(ticket.getPrice());
                        }
                    }
                    if (transitCost.compareTo(BigDecimal.ZERO) > 0) {
                        if (summary.length() > 0) summary.append(" | ");
                        summary.append("交通费：¥").append(transitCost.toString());
                    }
                }

                // 【修改】通过 Toast 和日志显示汇总信息
                if (summary.length() > 0) {
                    Log.d("TransportPlan", "规划完成: " + summary.toString());
                }

                if (currentTickets.isEmpty() && (drivingSegments == null || drivingSegments.isEmpty())) {
                    showError("未找到匹配的班次信息，请尝试其他出行方式");
                } else {
                    String resultMsg = "规划完成! ";
                    if (!currentTickets.isEmpty()) {
                        resultMsg += currentTickets.size() + "个班次";
                    }
                    if (drivingSegments != null && !drivingSegments.isEmpty()) {
                        if (!currentTickets.isEmpty()) resultMsg += ", ";
                        resultMsg += drivingSegments.size() + "段驾车路线";
                    }
                    Toast.makeText(TransportPlanActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                llLoadingContainer.setVisibility(View.GONE);
                showError(error);
            }
        });

        // 启动分天分方式规划
        planManager.startPlanning(itinerary, selectedModes, customOrigin, customDestination);
    }

    private String getOriginFromItinerary() {
        if (itinerary == null)
            return "未知";

        List<Itinerary.DayPlan> dayPlans = itinerary.getDayPlans();
        if (dayPlans != null && !dayPlans.isEmpty()) {
            Itinerary.DayPlan firstDay = dayPlans.get(0);
            List<Itinerary.SpotPlan> spots = firstDay.getSpots();
            if (spots != null && !spots.isEmpty()) {
                Itinerary.SpotPlan firstSpot = spots.get(0);
                if (firstSpot.getAttractionName() != null && !firstSpot.getAttractionName().isEmpty()) {
                    return firstSpot.getAttractionName();
                }
            }
        }

        return "出发地";
    }

    private String getDestinationFromItinerary() {
        if (itinerary == null)
            return "未知";

        List<Itinerary.DayPlan> dayPlans = itinerary.getDayPlans();
        if (dayPlans != null && !dayPlans.isEmpty()) {
            Itinerary.DayPlan lastDay = dayPlans.get(dayPlans.size() - 1);
            List<Itinerary.SpotPlan> spots = lastDay.getSpots();
            if (spots != null && !spots.isEmpty()) {
                Itinerary.SpotPlan lastSpot = spots.get(spots.size() - 1);
                if (lastSpot.getAttractionName() != null && !lastSpot.getAttractionName().isEmpty()) {
                    return lastSpot.getAttractionName();
                }
            }
        }

        return itinerary.getDestination() != null ? itinerary.getDestination() : "目的地";
    }

    private void parseAndShowAIResult(String aiResponse) {
        Log.d("TransportPlan", "AI原始响应: " + aiResponse);

        try {
            String jsonStr = extractJsonFromResponse(aiResponse);
            Log.d("TransportPlan", "提取的JSON: " + jsonStr);

            com.google.gson.JsonElement jsonElement = gson.fromJson(jsonStr, com.google.gson.JsonElement.class);

            if (!jsonElement.isJsonObject()) {
                showError("AI返回的不是JSON对象\n\n原始响应: " + aiResponse);
                return;
            }

            JsonObject resultJson = jsonElement.getAsJsonObject();

            // 【已移除】旧的路线汇总显示区域
            // 汇总信息现在通过日志和 Toast 展示

            String totalDistance = "未知";
            if (resultJson.has("totalDistance")) {
                com.google.gson.JsonElement distElement = resultJson.get("totalDistance");
                if (distElement.isJsonPrimitive()) {
                    totalDistance = distElement.getAsString();
                }
            }
            Log.d("TransportPlan", "全程距离: " + totalDistance);

            BigDecimal totalTollFee = null;
            if (resultJson.has("totalTollFee")) {
                try {
                    com.google.gson.JsonElement tollElement = resultJson.get("totalTollFee");
                    if (tollElement.isJsonPrimitive()) {
                        totalTollFee = BigDecimal.valueOf(tollElement.getAsDouble());
                    }
                } catch (Exception e) {
                    totalTollFee = null;
                }
            }

            if (totalTollFee != null && totalTollFee.compareTo(BigDecimal.ZERO) > 0) {
                Log.d("TransportPlan", "全程过路费: ¥" + totalTollFee.toString());
            }

            if (resultJson.has("drivingSegments") && resultJson.get("drivingSegments").isJsonArray()) {
                com.google.gson.JsonArray segmentsArray = resultJson.getAsJsonArray("drivingSegments");
                List<DrivingRouteSegment> drivingSegments = new ArrayList<>();

                for (int i = 0; i < segmentsArray.size(); i++) {
                    com.google.gson.JsonElement segElement = segmentsArray.get(i);
                    if (!segElement.isJsonObject()) {
                        continue;
                    }

                    JsonObject segJson = segElement.getAsJsonObject();
                    DrivingRouteSegment segment = new DrivingRouteSegment();

                    if (segJson.has("origin")) {
                        com.google.gson.JsonElement origElement = segJson.get("origin");
                        if (origElement.isJsonPrimitive()) {
                            segment.setOrigin(origElement.getAsString());
                        }
                    }

                    if (segJson.has("destination")) {
                        com.google.gson.JsonElement destElement = segJson.get("destination");
                        if (destElement.isJsonPrimitive()) {
                            segment.setDestination(destElement.getAsString());
                        }
                    }

                    if (segJson.has("distance")) {
                        com.google.gson.JsonElement distElement = segJson.get("distance");
                        if (distElement.isJsonPrimitive()) {
                            segment.setDistance(distElement.getAsString());
                        }
                    }

                    if (segJson.has("duration")) {
                        com.google.gson.JsonElement durElement = segJson.get("duration");
                        if (durElement.isJsonPrimitive()) {
                            segment.setDuration(durElement.getAsString());
                        }
                    }

                    if (segJson.has("roadType")) {
                        com.google.gson.JsonElement roadElement = segJson.get("roadType");
                        if (roadElement.isJsonPrimitive()) {
                            segment.setRoadType(roadElement.getAsString());
                        }
                    }

                    if (segJson.has("instruction")) {
                        com.google.gson.JsonElement instElement = segJson.get("instruction");
                        if (instElement.isJsonPrimitive()) {
                            segment.setInstruction(instElement.getAsString());
                        }
                    }

                    if (segJson.has("tollFee")) {
                        try {
                            com.google.gson.JsonElement tollElement = segJson.get("tollFee");
                            if (tollElement.isJsonPrimitive()) {
                                segment.setTollFee(BigDecimal.valueOf(tollElement.getAsDouble()));
                            }
                        } catch (Exception e) {
                            segment.setTollFee(BigDecimal.ZERO);
                        }
                    }

                    drivingSegments.add(segment);
                }

                if (!drivingSegments.isEmpty()) {
                    showDrivingRoute(drivingSegments);
                }
            }

            if (resultJson.has("dayPlans") && resultJson.get("dayPlans").isJsonArray()) {
                currentTickets.clear();

                com.google.gson.JsonArray dayPlansArray = resultJson.getAsJsonArray("dayPlans");
                for (int i = 0; i < dayPlansArray.size(); i++) {
                    com.google.gson.JsonElement dayElement = dayPlansArray.get(i);
                    if (!dayElement.isJsonObject()) {
                        continue;
                    }

                    JsonObject dayPlanJson = dayElement.getAsJsonObject();

                    int dayNumber = 0;
                    if (dayPlanJson.has("dayNumber")) {
                        com.google.gson.JsonElement dayNumElement = dayPlanJson.get("dayNumber");
                        if (dayNumElement.isJsonPrimitive()) {
                            dayNumber = dayNumElement.getAsInt();
                        }
                    }

                    if (dayPlanJson.has("recommendations") && dayPlanJson.get("recommendations").isJsonArray()) {
                        com.google.gson.JsonArray recsArray = dayPlanJson.getAsJsonArray("recommendations");
                        for (int j = 0; j < recsArray.size(); j++) {
                            com.google.gson.JsonElement recElement = recsArray.get(j);
                            if (!recElement.isJsonObject()) {
                                continue;
                            }

                            JsonObject rec = recElement.getAsJsonObject();

                            if (rec.has("tickets") && rec.get("tickets").isJsonArray()) {
                                com.google.gson.JsonArray ticketsArray = rec.getAsJsonArray("tickets");
                                for (int k = 0; k < ticketsArray.size(); k++) {
                                    com.google.gson.JsonElement ticketElement = ticketsArray.get(k);
                                    if (!ticketElement.isJsonObject()) {
                                        continue;
                                    }

                                    JsonObject ticketJson = ticketElement.getAsJsonObject();

                                    TicketInfo ticket = new TicketInfo();

                                    String modeName = "";
                                    if (rec.has("modeName")) {
                                        com.google.gson.JsonElement modeElement = rec.get("modeName");
                                        if (modeElement.isJsonPrimitive()) {
                                            modeName = modeElement.getAsString();
                                        }
                                    }
                                    ticket.setTransportMode(modeName + (dayNumber > 0 ? " (第" + dayNumber + "天)" : ""));

                                    String tripNumber = "";
                                    if (ticketJson.has("tripNumber")) {
                                        com.google.gson.JsonElement tripElement = ticketJson.get("tripNumber");
                                        if (tripElement.isJsonPrimitive()) {
                                            tripNumber = tripElement.getAsString();
                                        }
                                    }
                                    ticket.setTripNumber(tripNumber);

                                    String depTime = "";
                                    if (ticketJson.has("departureTime")) {
                                        com.google.gson.JsonElement depElement = ticketJson.get("departureTime");
                                        if (depElement.isJsonPrimitive()) {
                                            depTime = depElement.getAsString();
                                        }
                                    }
                                    ticket.setDepartureTime(depTime);

                                    String arrTime = "";
                                    if (ticketJson.has("arrivalTime")) {
                                        com.google.gson.JsonElement arrElement = ticketJson.get("arrivalTime");
                                        if (arrElement.isJsonPrimitive()) {
                                            arrTime = arrElement.getAsString();
                                        }
                                    }
                                    ticket.setArrivalTime(arrTime);

                                    String duration = "";
                                    if (ticketJson.has("duration")) {
                                        com.google.gson.JsonElement durElement = ticketJson.get("duration");
                                        if (durElement.isJsonPrimitive()) {
                                            duration = durElement.getAsString();
                                        }
                                    }
                                    ticket.setDuration(duration);

                                    String seatType = "";
                                    if (ticketJson.has("seatType")) {
                                        com.google.gson.JsonElement seatElement = ticketJson.get("seatType");
                                        if (seatElement.isJsonPrimitive()) {
                                            seatType = seatElement.getAsString();
                                        }
                                    }
                                    ticket.setSeatType(seatType);

                                    String origin = "";
                                    if (ticketJson.has("origin")) {
                                        com.google.gson.JsonElement origElement = ticketJson.get("origin");
                                        if (origElement.isJsonPrimitive()) {
                                            origin = origElement.getAsString();
                                        }
                                    }
                                    ticket.setOrigin(origin);

                                    String destination = "";
                                    if (ticketJson.has("destination")) {
                                        com.google.gson.JsonElement destElement = ticketJson.get("destination");
                                        if (destElement.isJsonPrimitive()) {
                                            destination = destElement.getAsString();
                                        }
                                    }
                                    ticket.setDestination(destination);

                                    if (ticketJson.has("price")) {
                                        try {
                                            com.google.gson.JsonElement priceElement = ticketJson.get("price");
                                            if (priceElement.isJsonPrimitive()) {
                                                ticket.setPrice(BigDecimal.valueOf(priceElement.getAsDouble()));
                                            } else {
                                                ticket.setPrice(BigDecimal.ZERO);
                                            }
                                        } catch (Exception e) {
                                            ticket.setPrice(BigDecimal.ZERO);
                                        }
                                    }

                                    currentTickets.add(ticket);
                                }
                            }
                        }
                    }
                }

                if (!currentTickets.isEmpty()) {
                    showTickets(currentTickets);
                }
            }

            Toast.makeText(this, "路线规划成功", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("TransportPlan", "解析失败", e);
            showError("解析结果失败: " + e.getMessage() + "\n\n原始响应: " + aiResponse);
        }
    }

    private String extractJsonFromResponse(String response) {
        if (response == null || response.isEmpty()) {
            return "{}";
        }

        String cleaned = response.trim();

        int startIndex = cleaned.indexOf("{");
        int endIndex = cleaned.lastIndexOf("}");

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            String json = cleaned.substring(startIndex, endIndex + 1);
            Log.d("TransportPlan", "提取的JSON: " + json);
            return json;
        }

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }

        cleaned = cleaned.trim();

        startIndex = cleaned.indexOf("{");
        endIndex = cleaned.lastIndexOf("}");

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            String json = cleaned.substring(startIndex, endIndex + 1);
            Log.d("TransportPlan", "清理后的JSON: " + json);
            return json;
        }

        return "{}";
    }

    private String getTransportModeName(String mode) {
        switch (mode) {
            case "train":
                return "火车";
            case "highspeed":
                return "高铁";
            case "flight":
                return "飞机";
            case "drive":
                return "驾车";
            case "transit":
                return "公交";
            case "walk":
                return "步行";
            case "ride":
                return "骑行";
            default:
                return "未知";
        }
    }

    private void showTickets(List<TicketInfo> tickets) {
        if (tickets != null && !tickets.isEmpty()) {
            dividerTickets.setVisibility(View.VISIBLE);
            tvTicketsTitle.setVisibility(View.VISIBLE);
            rvTickets.setVisibility(View.VISIBLE);
            ticketAdapter.setTickets(tickets);
            Toast.makeText(this, "找到 " + tickets.size() + " 个班次", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDrivingRoute(List<DrivingRouteSegment> segments) {
        if (segments != null && !segments.isEmpty()) {
            dividerDriving.setVisibility(View.VISIBLE);
            tvDrivingTitle.setVisibility(View.VISIBLE);
            rvDrivingRoute.setVisibility(View.VISIBLE);
            drivingRouteAdapter.setSegments(segments);
            Toast.makeText(this, "驾车路线共 " + segments.size() + " 段", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(String error) {
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "位置权限被拒绝，请手动输入地址", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
