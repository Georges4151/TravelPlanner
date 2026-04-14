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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransportPlanActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private TicketAdapter ticketAdapter;
    private DrivingRouteAdapter drivingRouteAdapter;

    private TextView tvTitle;
    private TextView tvItineraryInfo;
    private Button btnSearch;
    private LinearLayout llResultContainer;
    private TextView tvDistance;
    private TextView tvCost;
    private View dividerTickets;
    private TextView tvTicketsTitle;
    private RecyclerView rvTickets;
    private View dividerDriving;
    private TextView tvDrivingTitle;
    private RecyclerView rvDrivingRoute;
    private ProgressBar progressBar;
    private TextView tvError;

    private Handler handler = new Handler(Looper.getMainLooper());
    private List<TicketInfo> currentTickets = new ArrayList<>();

    private Itinerary itinerary;
    private List<String> selectedModes;
    private String customOrigin;
    private String customDestination;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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
        }
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvItineraryInfo = findViewById(R.id.tvItineraryInfo);
        btnSearch = findViewById(R.id.btnSearch);
        llResultContainer = findViewById(R.id.llRouteInfo);
        tvDistance = findViewById(R.id.tvDistance);
        tvCost = findViewById(R.id.tvCost);
        dividerTickets = findViewById(R.id.dividerTickets);
        tvTicketsTitle = findViewById(R.id.tvTicketsTitle);
        rvTickets = findViewById(R.id.rvTickets);
        dividerDriving = findViewById(R.id.dividerDriving);
        tvDrivingTitle = findViewById(R.id.tvDrivingTitle);
        rvDrivingRoute = findViewById(R.id.rvDrivingRoute);
        progressBar = findViewById(R.id.progressBar);
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

        tvError.setVisibility(View.GONE);
        llResultContainer.setVisibility(View.GONE);
        rvTickets.setVisibility(View.GONE);
        dividerTickets.setVisibility(View.GONE);
        tvTicketsTitle.setVisibility(View.GONE);
        rvDrivingRoute.setVisibility(View.GONE);
        dividerDriving.setVisibility(View.GONE);
        tvDrivingTitle.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        StringBuilder prompt = new StringBuilder();
        prompt.append("请作为专业的交通规划助手，为以下完整行程提供路线规划方案。\n\n");

        prompt.append("【行程基本信息】\n");
        prompt.append("行程标题: ").append(itinerary.getTitle()).append("\n");
        prompt.append("总天数: ").append(itinerary.getDays()).append("天\n\n");

        prompt.append("【用户选择的出行方式】\n");
        for (String mode : selectedModes) {
            prompt.append("- ").append(getTransportModeName(mode)).append("\n");
        }
        prompt.append("\n");

        prompt.append("【完整行程路线（途经点）】\n");
        prompt.append("注意：以下是需要途经的所有景点，请按顺序规划路线\n");
        List<Itinerary.DayPlan> dayPlans = itinerary.getDayPlans();
        if (dayPlans != null && !dayPlans.isEmpty()) {
            for (Itinerary.DayPlan dayPlan : dayPlans) {
                prompt.append("\n第").append(dayPlan.getDayNumber()).append("天");
                if (dayPlan.getDate() != null && !dayPlan.getDate().isEmpty()) {
                    prompt.append(" (").append(dayPlan.getDate()).append(")");
                }
                prompt.append(":\n");

                if (dayPlan.getWeather() != null && !dayPlan.getWeather().isEmpty()) {
                    prompt.append("  天气: ").append(dayPlan.getWeather()).append("\n");
                }
                if (dayPlan.getTips() != null && !dayPlan.getTips().isEmpty()) {
                    prompt.append("  小贴士: ").append(dayPlan.getTips()).append("\n");
                }

                List<Itinerary.SpotPlan> spots = dayPlan.getSpots();
                if (spots != null && !spots.isEmpty()) {
                    for (Itinerary.SpotPlan spot : spots) {
                        if (spot.getAttractionName() != null && !spot.getAttractionName().isEmpty()) {
                            prompt.append("- ").append(spot.getAttractionName());
                            if (spot.getArrivalTime() != null && !spot.getArrivalTime().isEmpty()) {
                                prompt.append(" (到达: ").append(spot.getArrivalTime());
                                if (spot.getLeaveTime() != null && !spot.getLeaveTime().isEmpty()) {
                                    prompt.append(", 离开: ").append(spot.getLeaveTime());
                                }
                                prompt.append(")");
                            }
                            if (spot.getStayDuration() != null && spot.getStayDuration() > 0) {
                                prompt.append(" [停留: ").append(spot.getStayDuration()).append("分钟]");
                            }
                            if (spot.getTicketPrice() != null) {
                                prompt.append(" [门票: ¥").append(spot.getTicketPrice()).append("]");
                            }
                            if (spot.getCrowdLevel() != null && !spot.getCrowdLevel().isEmpty()) {
                                prompt.append(" [客流: ").append(spot.getCrowdLevel()).append("]");
                            }
                            if (spot.getBestVisitTime() != null && !spot.getBestVisitTime().isEmpty()) {
                                prompt.append(" [最佳时间: ").append(spot.getBestVisitTime()).append("]");
                            }
                            if (spot.getTransportToNext() != null && !spot.getTransportToNext().isEmpty()) {
                                prompt.append(" [下一站交通: ").append(spot.getTransportToNext()).append("]");
                            }
                            if (spot.getNotes() != null && !spot.getNotes().isEmpty()) {
                                prompt.append(" [备注: ").append(spot.getNotes()).append("]");
                            }
                            prompt.append("\n");
                        }
                    }
                }
            }
        }
        prompt.append("\n");

        prompt.append("【总体出行信息】\n");
        prompt.append("总出发地: ").append(customOrigin).append("\n");
        prompt.append("总目的地: ").append(customDestination).append("\n\n");

        prompt.append("【任务要求】\n");
        prompt.append("请规划从出发地出发，途经上述所有景点，最后到达目的地的完整路线。请按以下格式返回JSON：\n\n");
        prompt.append("{\n");
        prompt.append("  \"totalDistance\": \"全程大致距离，单位：公里\",\n");
        prompt.append("  \"totalTollFee\": \"全程高速过路费总计，数字类型（仅驾车时需要）\",\n");
        prompt.append("  \"drivingSegments\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"origin\": \"起点\",\n");
        prompt.append("      \"destination\": \"终点\",\n");
        prompt.append("      \"distance\": \"距离，如：50公里\",\n");
        prompt.append("      \"duration\": \"预计时长，如：1小时20分钟\",\n");
        prompt.append("      \"roadType\": \"道路类型，如：高速、国道、省道\",\n");
        prompt.append("      \"instruction\": \"行驶说明，如：从起点出发沿XX路向东行驶\",\n");
        prompt.append("      \"tollFee\": \"该段高速过路费，数字类型\"\n");
        prompt.append("    }\n");
        prompt.append("  ],\n");
        prompt.append("  \"dayPlans\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"dayNumber\": 1,\n");
        prompt.append("      \"date\": \"日期（可选）\",\n");
        prompt.append("      \"origin\": \"当天出发地\",\n");
        prompt.append("      \"destination\": \"当天目的地\",\n");
        prompt.append("      \"recommendations\": [\n");
        prompt.append("        {\n");
        prompt.append("          \"mode\": \"出行方式（train/highspeed/flight/drive/transit/walk/ride）\",\n");
        prompt.append("          \"modeName\": \"出行方式名称（火车/高铁/飞机/驾车/公交/步行/骑行）\",\n");
        prompt.append("          \"duration\": \"预计时长，如：2小时30分钟\",\n");
        prompt.append("          \"cost\": \"预计费用，数字类型\",\n");
        prompt.append("          \"tollFee\": \"高速过路费（仅驾车时需要），数字类型\",\n");
        prompt.append("          \"tickets\": [\n");
        prompt.append("            {\n");
        prompt.append("              \"tripNumber\": \"班次号（如G1234、CA1234）\",\n");
        prompt.append("              \"departureTime\": \"出发时间（HH:mm格式）\",\n");
        prompt.append("              \"arrivalTime\": \"到达时间（HH:mm格式）\",\n");
        prompt.append("              \"duration\": \"时长（如2h30m）\",\n");
        prompt.append("              \"seatType\": \"座位类型（如二等座、经济舱）\",\n");
        prompt.append("              \"price\": \"票价，数字类型\",\n");
        prompt.append("              \"origin\": \"出发站点\",\n");
        prompt.append("              \"destination\": \"到达站点\"\n");
        prompt.append("            }\n");
        prompt.append("          ]\n");
        prompt.append("        }\n");
        prompt.append("      ]\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}\n\n");

        prompt.append("【注意事项】\n");
        prompt.append("1. 只规划用户选择的出行方式\n");
        prompt.append("2. 必须经过上述所有景点\n");
        prompt.append("3. 对于火车、高铁、飞机，请提供3-5个接近出行日期的班次\n");
        prompt.append("4. 班次出发时间应该在出行当天的合理时间段内\n");
        prompt.append("5. 请使用真实的班次号格式\n");
        prompt.append("6. 如果是驾车出行，必须在drivingSegments中提供分段的路线规划，每段包含起点、终点、距离、时长、道路类型、行驶说明和过路费\n");
        prompt.append("7. 请估算全程的高速过路费总和，填入totalTollFee字段\n");
        prompt.append("8. 请返回纯JSON格式，不要包含任何其他文字说明\n");

        Log.d("TransportPlan", "行程是否有dayPlans: " + (itinerary.getDayPlans() != null));
        if (itinerary.getDayPlans() != null) {
            Log.d("TransportPlan", "dayPlans数量: " + itinerary.getDayPlans().size());
            for (int i = 0; i < itinerary.getDayPlans().size(); i++) {
                Itinerary.DayPlan dp = itinerary.getDayPlans().get(i);
                Log.d("TransportPlan",
                        "第" + (i + 1) + "天spots数量: " + (dp.getSpots() != null ? dp.getSpots().size() : 0));
            }
        }
        Log.d("TransportPlan", "发送给AI的Prompt: " + prompt.toString());

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("model", "deepseek-chat");

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt.toString());

        requestJson.add("messages", gson.toJsonTree(Arrays.asList(message)));
        requestJson.addProperty("temperature", 0.7);
        requestJson.addProperty("max_tokens", 4000);

        RequestBody body = RequestBody.create(requestJson.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.deepseek.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer sk-bae04f8b40de465290471e774fda4da9")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, java.io.IOException e) {
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    showError("网络请求失败，请稍后重试\n" + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws java.io.IOException {
                String responseBody = response.body() != null ? response.body().string() : "";
                Log.d("TransportPlan", "DeepSeek API响应状态码: " + response.code());
                Log.d("TransportPlan", "DeepSeek API完整响应: " + responseBody);

                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);

                    if (!response.isSuccessful()) {
                        showError("API请求失败，状态码: " + response.code() + "\n\n响应: " + responseBody);
                        return;
                    }

                    try {
                        com.google.gson.Gson lenientGson = new com.google.gson.GsonBuilder()
                                .setLenient()
                                .create();

                        com.google.gson.JsonElement jsonElement = lenientGson.fromJson(responseBody,
                                com.google.gson.JsonElement.class);

                        if (!jsonElement.isJsonObject()) {
                            showError("API响应格式错误，不是JSON对象\n\n原始响应: " + responseBody);
                            return;
                        }

                        JsonObject jsonResponse = jsonElement.getAsJsonObject();

                        Log.d("TransportPlan", "解析后的JSON响应: " + jsonResponse);

                        if (jsonResponse.has("choices") && jsonResponse.get("choices").isJsonArray()
                                && jsonResponse.getAsJsonArray("choices").size() > 0) {

                            com.google.gson.JsonElement choiceElement = jsonResponse.getAsJsonArray("choices").get(0);
                            if (!choiceElement.isJsonObject()) {
                                showError("choices[0]不是对象\n\n原始响应: " + responseBody);
                                return;
                            }

                            JsonObject choice = choiceElement.getAsJsonObject();

                            if (!choice.has("message") || !choice.get("message").isJsonObject()) {
                                showError("message字段不存在或不是对象\n\n原始响应: " + responseBody);
                                return;
                            }

                            JsonObject message = choice.getAsJsonObject("message");

                            if (!message.has("content") || !message.get("content").isJsonPrimitive()) {
                                showError("content字段不存在或不是字符串\n\n原始响应: " + responseBody);
                                return;
                            }

                            String content = message.get("content").getAsString();
                            Log.d("TransportPlan", "AI内容: " + content);

                            parseAndShowAIResult(content);
                        } else {
                            showError("AI响应格式错误，请稍后重试\n\n原始响应: " + responseBody);
                        }
                    } catch (Exception e) {
                        Log.e("TransportPlan", "解析失败", e);
                        showError("解析AI响应失败，请稍后重试\n" + e.getMessage() + "\n\n原始响应: " + responseBody);
                    }
                });
            }
        });
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

            llResultContainer.setVisibility(View.VISIBLE);

            String totalDistance = "未知";
            if (resultJson.has("totalDistance")) {
                com.google.gson.JsonElement distElement = resultJson.get("totalDistance");
                if (distElement.isJsonPrimitive()) {
                    totalDistance = distElement.getAsString();
                }
            }
            tvDistance.setText("全程距离：" + totalDistance);

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
                tvCost.setVisibility(View.VISIBLE);
                tvCost.setText("全程过路费：¥" + totalTollFee.toString());
            } else {
                tvCost.setVisibility(View.GONE);
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
