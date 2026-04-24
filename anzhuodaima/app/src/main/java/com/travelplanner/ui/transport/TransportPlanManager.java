package com.travelplanner.ui.transport;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.travelplanner.model.DrivingRouteSegment;
import com.travelplanner.model.Itinerary;
import com.travelplanner.model.TicketInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 分天分方式异步交通规划管理器
 * 采用分治策略：每天每种方式单独请求，确保数据完整性
 */
public class TransportPlanManager {
    private static final String TAG = "TransportPlanManager";
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = "sk-bae04f8b40de465290471e774fda4da9";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)  // 合并请求需要更长读取时间
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Gson gson = new Gson();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<Call> pendingCalls = new ArrayList<>();

    // 结果收集
    private List<TicketInfo> allTickets = new ArrayList<>();
    private List<DrivingRouteSegment> allDrivingSegments = new ArrayList<>();
    private String totalDistance = "";
    private BigDecimal totalTollFee = BigDecimal.ZERO;
    private BigDecimal totalCost = BigDecimal.ZERO;

    // 进度追踪
    private int totalRequests = 0;
    private AtomicInteger completedRequests = new AtomicInteger(0);
    private int[] dayProgress; // 每个天数的进度
    private int totalDays = 0;
    private List<Itinerary.DayPlan> allDayPlans = null; // 【修复】保存所有天数数据
    private String globalCustomOrigin = null; // 用户输入的总起点
    private String globalCustomDestination = null; // 用户输入的总终点

    // 回调接口
    public interface PlanningCallback {
        void onProgress(int currentDay, int totalDays, int currentMode, int totalModes);
        void onDayComplete(int dayNumber);
        void onComplete(List<TicketInfo> tickets, List<DrivingRouteSegment> drivingSegments,
                        String totalDistance, BigDecimal totalTollFee);
        void onError(String error);
    }

    private PlanningCallback callback;

    public void setCallback(PlanningCallback callback) {
        this.callback = callback;
    }

    /**
     * 开始分天规划 - 【优化】每天合并一次AI请求，智能过滤不适用的方式
     */
    public void startPlanning(Itinerary itinerary, List<String> selectedModes,
                              String customOrigin, String customDestination) {
        if (itinerary == null || selectedModes == null || selectedModes.isEmpty()) {
            if (callback != null) {
                callback.onError("行程数据或出行方式为空");
            }
            return;
        }

        List<Itinerary.DayPlan> dayPlans = itinerary.getDayPlans();
        if (dayPlans == null || dayPlans.isEmpty()) {
            if (callback != null) {
                callback.onError("行程中没有天数计划");
            }
            return;
        }

        // 初始化
        totalDays = dayPlans.size();
        dayProgress = new int[totalDays];
        allTickets.clear();
        allDrivingSegments.clear();
        totalDistance = "";
        totalTollFee = BigDecimal.ZERO;
        totalCost = BigDecimal.ZERO;
        completedRequests.set(0);

        // 【修复】保存全局数据供动态起终点计算使用
        this.allDayPlans = dayPlans;
        this.globalCustomOrigin = customOrigin;
        this.globalCustomDestination = customDestination;

        // 【优化】智能过滤：为每天确定适用的交通方式，跳过不合理的请求
        int filteredRequests = 0;
        for (int dayIndex = 0; dayIndex < dayPlans.size(); dayIndex++) {
            Itinerary.DayPlan dayPlan = dayPlans.get(dayIndex);
            int dayNumber = dayPlan.getDayNumber();
            List<String> filteredModes = filterApplicableModes(dayPlan, dayNumber, selectedModes, customOrigin, customDestination);
            filteredRequests += filteredModes.size();
            
            if (filteredModes.isEmpty()) {
                Log.d(TAG, "第" + dayNumber + "天: 无适用的交通方式，跳过");
            } else {
                Log.d(TAG, "第" + dayNumber + "天: 适用方式 " + filteredModes + " (原始: " + selectedModes + ")");
            }
        }

        // 每天发1次合并请求
        totalRequests = totalDays;
        Log.d(TAG, "开始规划: " + totalDays + "天, 合并请求(原" + (totalDays * selectedModes.size()) + "次→" + totalRequests + "次)");

        // 为每天发送一次合并请求
        for (int dayIndex = 0; dayIndex < dayPlans.size(); dayIndex++) {
            Itinerary.DayPlan dayPlan = dayPlans.get(dayIndex);
            int dayNumber = dayPlan.getDayNumber();
            List<String> filteredModes = filterApplicableModes(dayPlan, dayNumber, selectedModes, customOrigin, customDestination);
            sendDayCombinedRequest(dayPlan, dayNumber, filteredModes, dayIndex, customOrigin, customDestination);
        }
    }

    /**
     * 【新增】智能过滤：判断某天哪些交通方式是适用的
     * 规则：
     * - 市内行程（起终点同城/同景区）：只保留 transit/taxi/walk/ride/drive
     * - 跨城行程：保留 train/highspeed/flight + 市内方式
     * - 短距离行程：去掉 flight
     */
    private List<String> filterApplicableModes(Itinerary.DayPlan dayPlan, int dayNumber,
                                                List<String> selectedModes, String customOrigin, String customDestination) {
        List<String> applicable = new ArrayList<>();
        
        // 计算当天实际起终点
        String actualOrigin = calculateActualOrigin(dayPlan, dayNumber, customOrigin);
        String actualDestination = calculateActualDestination(dayPlan, dayNumber, customDestination);
        
        // 判断是否为跨城行程
        boolean isCrossCity = isCrossCityTrip(actualOrigin, actualDestination, dayPlan);
        
        // 判断是否为短途行程（景点都在同城）
        boolean isIntraCity = isIntraCityTrip(dayPlan);
        
        for (String mode : selectedModes) {
            switch (mode) {
                case "flight":
                    // 飞机：只有跨城行程才适用
                    if (isCrossCity) {
                        applicable.add(mode);
                    } else {
                        Log.d(TAG, "第" + dayNumber + "天: 跳过flight(市内行程)");
                    }
                    break;
                    
                case "highspeed":
                case "train":
                    // 高铁/火车：只有跨城行程才适用
                    if (isCrossCity) {
                        applicable.add(mode);
                    } else {
                        Log.d(TAG, "第" + dayNumber + "天: 跳过" + mode + "(市内行程)");
                    }
                    break;
                    
                case "transit":
                case "taxi":
                case "walk":
                case "ride":
                case "drive":
                    // 市内方式：始终适用
                    applicable.add(mode);
                    break;
                    
                default:
                    applicable.add(mode);
                    break;
            }
        }
        
        return applicable;
    }

    /**
     * 【新增】判断是否为跨城行程
     * 只根据当天的实际起终点判断，不使用全局fallback
     */
    private boolean isCrossCityTrip(String origin, String destination, Itinerary.DayPlan dayPlan) {
        // 如果起终点包含不同城市名，则为跨城
        if (origin != null && destination != null) {
            String cityOrigin = extractCity(origin);
            String cityDest = extractCity(destination);
            if (!cityOrigin.isEmpty() && !cityDest.isEmpty() && !cityOrigin.equals(cityDest)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 【新增】判断是否为纯市内行程（所有景点在同一城市）
     */
    private boolean isIntraCityTrip(Itinerary.DayPlan dayPlan) {
        if (dayPlan == null || dayPlan.getSpots() == null || dayPlan.getSpots().isEmpty()) {
            return true;  // 无景点信息时默认为市内
        }
        
        // 简化判断：如果行程有明确的起终点，检查是否同城
        // 景点通常在同城市内
        String origin = calculateActualOrigin(dayPlan, dayPlan.getDayNumber(), globalCustomOrigin);
        String dest = calculateActualDestination(dayPlan, dayPlan.getDayNumber(), globalCustomDestination);
        
        if (origin != null && dest != null) {
            String originCity = extractCity(origin);
            String destCity = extractCity(dest);
            if (!originCity.isEmpty() && !destCity.isEmpty() && originCity.equals(destCity)) {
                return true;
            }
        }
        
        // 默认：有多个景点时视为市内行程（大部分情况）
        return dayPlan.getSpots().size() > 1;
    }

    /**
     * 【新增】为某天发送合并请求 - 一次AI调用返回所有适用方式的结果
     */
    private void sendDayCombinedRequest(Itinerary.DayPlan dayPlan, int dayNumber,
                                         List<String> modes, int dayIndex,
                                         String customOrigin, String customDestination) {
        if (modes == null || modes.isEmpty()) {
            // 无适用方式，直接标记完成
            handleRequestComplete(dayNumber, dayIndex, false);
            return;
        }

        String prompt = buildCombinedDayPrompt(dayPlan, dayNumber, modes, customOrigin, customDestination);

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("model", "deepseek-chat");

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);

        requestJson.add("messages", gson.toJsonTree(Arrays.asList(message)));
        requestJson.addProperty("temperature", 0.3);  // 降低温度，让输出更稳定
        requestJson.addProperty("max_tokens", 4000);   // 合并请求需要更多token

        RequestBody body = RequestBody.create(requestJson.toString(), JSON);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Call call = client.newCall(request);
        synchronized (pendingCalls) {
            pendingCalls.add(call);
        }

        final int finalDayIndex = dayIndex;
        final List<String> finalModes = new ArrayList<>(modes);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "合并请求失败: 第" + dayNumber + "天 - " + e.getMessage());
                handleRequestComplete(dayNumber, finalDayIndex, false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 【关键修复】整个方法用try-catch包裹，确保任何异常都触发完成回调
                try {
                    String responseBody = response.body() != null ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        Log.e(TAG, "API失败: 第" + dayNumber + "天, 状态码: " + response.code());
                        handleRequestComplete(dayNumber, finalDayIndex, false);
                        return;
                    }

                    String content = parseAIResponse(responseBody);
                    if (content != null && !content.isEmpty()) {
                        Log.d(TAG, "第" + dayNumber + "天 合并AI响应: " + 
                              (content.length() > 300 ? content.substring(0, 300) + "..." : content));
                        parseCombinedDayResult(content, dayNumber, finalModes);
                        handleRequestComplete(dayNumber, finalDayIndex, true);
                    } else {
                        Log.w(TAG, "第" + dayNumber + "天 合并AI响应为空");
                        handleRequestComplete(dayNumber, finalDayIndex, false);
                    }
                } catch (Exception e) {
                    // 捕获SocketTimeoutException等在onResponse内部抛出的异常
                    Log.e(TAG, "第" + dayNumber + "天 合并请求异常: " + e.getMessage(), e);
                    handleRequestComplete(dayNumber, finalDayIndex, false);
                }
            }
        });
    }

    /**
     * 【新增】构建合并Prompt - 一次请求包含所有适用方式
     */
    private String buildCombinedDayPrompt(Itinerary.DayPlan dayPlan, int dayNumber,
                                           List<String> modes, String customOrigin, String customDestination) {
        StringBuilder prompt = new StringBuilder();

        // 当天景点列表
        List<Itinerary.SpotPlan> spots = dayPlan.getSpots();
        StringBuilder attractionsBuilder = new StringBuilder();
        if (spots != null && !spots.isEmpty()) {
            for (int i = 0; i < spots.size(); i++) {
                Itinerary.SpotPlan spot = spots.get(i);
                if (spot.getAttractionName() != null) {
                    attractionsBuilder.append(spot.getAttractionName());
                    if (spot.getArrivalTime() != null) {
                        attractionsBuilder.append("@").append(spot.getArrivalTime());
                    }
                    if (i < spots.size() - 1) {
                        attractionsBuilder.append(" -> ");
                    }
                }
            }
        }
        String attractionsRoute = attractionsBuilder.toString();

        // 计算动态起终点
        String actualOrigin = calculateActualOrigin(dayPlan, dayNumber, customOrigin);
        String actualDestination = calculateActualDestination(dayPlan, dayNumber, customDestination);

        // 判断是否为多天行程的首日/末日
        int totalDays = (allDayPlans != null) ? allDayPlans.size() : 1;
        boolean isFirstDay = (dayNumber == 1);
        boolean isLastDay = (dayNumber == totalDays);
        
        prompt.append("请为以下行程同时规划多种交通方案，并按JSON格式返回。\n\n");
        prompt.append("【行程信息】\n");
        prompt.append("第").append(dayNumber).append("天（共").append(totalDays).append("天）\n");
        if (isFirstDay) prompt.append("⚠️ 这是出发日，需要规划从出发地到目的地的城际交通\n");
        if (isLastDay && !isFirstDay) prompt.append("⚠️ 这是返程日，需要规划从目的地返回出发地的城际交通\n");
        prompt.append("起点：").append(actualOrigin).append("\n");
        prompt.append("终点：").append(actualDestination).append("\n");
        prompt.append("景点路线：").append(attractionsRoute).append("\n\n");
        
        prompt.append("【需要规划的方式】\n");
        for (String mode : modes) {
            prompt.append("- ").append(getModeDisplayName(mode));
            // 对城际交通给出明确提示
            if ((mode.equals("train") || mode.equals("highspeed") || mode.equals("flight"))) {
                if (isFirstDay) {
                    prompt.append("（去程：从起点到目的地的班次）");
                } else if (isLastDay) {
                    prompt.append("（返程：从景点返回出发地的班次）");
                }
            }
            prompt.append("\n");
        }

        prompt.append("\n【返回格式要求】\n");
        prompt.append("请严格按以下JSON格式返回，每个方式一个key：\n");
        prompt.append("```json\n{\n");
        
        boolean first = true;
        for (String mode : modes) {
            if (!first) prompt.append(",\n");
            first = false;
            prompt.append("  \"").append(mode).append("\": ");
            
            switch (mode) {
                case "transit":
                    prompt.append("{\n    \"routeName\": \"地铁1号线转公交\",\n    \"departureTime\": \"06:30\",\n    \"arrivalTime\": \"22:00\",\n    \"duration\": \"约1小时20分\",\n    \"distance\": \"15km\",\n    \"price\": 5,\n    \"steps\": [\n      {\"type\": \"地铁\", \"line\": \"1号线\", \"from\": \"起点站\", \"to\": \"换乘站\", \"duration\": \"20分钟\"},\n      {\"type\": \"公交\", \"line\": \"游2路\", \"from\": \"换乘站\", \"to\": \"终点站\", \"duration\": \"30分钟\"}\n    ]\n  }");
                    break;
                case "taxi":
                    prompt.append("{\n    \"carType\": \"快车\",\n    \"estimatedPrice\": 35,\n    \"duration\": \"约30分钟\",\n    \"distance\": \"8km\",\n    \"tip\": \"高峰期建议提前叫车\"\n  }");
                    break;
                case "walk":
                    prompt.append("{\n    \"distance\": \"2.5km\",\n    \"duration\": \"约35分钟\",\n    \"caloriesBurned\": \"约120千卡\",\n    \"landmarks\": \"途经西湖沿湖步道\",\n    \"safetyTip\": \"注意人行道安全\"\n  }");
                    break;
                case "ride":
                    prompt.append("{\n    \"distance\": \"3km\",\n    \"duration\": \"约15分钟\",\n    \"bikeStations\": \"景区入口有共享单车停放点\",\n    \"tip\": \"骑行请注意交通安全\"\n  }");
                    break;
                case "drive":
                    prompt.append("{\n    \"drivingSegments\": [{\n      \"origin\": \"起点\", \"destination\": \"终点\",\n      \"distance\": \"10km\", \"duration\": \"20分钟\",\n      \"roadType\": \"城市道路\", \"tollFee\": 0,\n      \"instruction\": \"沿XX路行驶\"\n    }],\n    \"totalDistance\": \"10km\"\n  }");
                    break;
                case "train":
                    prompt.append("{\n    \"trainOptions\": [{\n      \"trainNumber\": \"G1234\",\n      \"departureTime\": \"08:00\", \"arrivalTime\": \"12:30\",\n      \"duration\": \"4小时30分\",\n      \"seatTypes\": [{\"type\": \"二等座\", \"price\": 553}]\n    }]\n  }");
                    break;
                case "highspeed":
                    prompt.append("{\n    \"trainOptions\": [{\n      \"trainNumber\": \"G1234\",\n      \"departureTime\": \"08:00\", \"arrivalTime\": \"10:30\",\n      \"duration\": \"2小时30分\",\n      \"seatTypes\": [{\"type\": \"二等座\", \"price\": 553}]\n    }]\n  }");
                    break;
                case "flight":
                    prompt.append("{\n    \"flightOptions\": [{\n      \"flightNumber\": \"CA1234\",\n      \"departureTime\": \"08:00\", \"arrivalTime\": \"10:30\",\n      \"duration\": \"2小时30分\",\n      \"price\": 890,\n      \"airline\": \"中国国航\"\n    }]\n  }");
                    break;
                default:
                    prompt.append("{}");
                    break;
            }
        }
        prompt.append("\n}\n```\n\n");
        
        prompt.append("重要：\n");
        prompt.append("1. 只返回JSON，不要其他文字说明\n");
        prompt.append("2. 火车/高铁/飞机等城际交通：出发日必须提供去程班次，返程日必须提供返程班次，市内游览日可返回空对象{}\n");
        prompt.append("3. 价格为人民币数字，不要带单位\n");
        prompt.append("4. 确保所有方式的数据都在一个JSON中\n");
        prompt.append("5. 火车/高铁必须返回至少2个班次选项供用户选择\n");

        return prompt.toString();
    }

    /**
     * 【新增】获取方式的中文名
     */
    private String getModeDisplayName(String mode) {
        switch (mode) {
            case "transit": return "公交/地铁";
            case "taxi": return "打车";
            case "walk": return "步行";
            case "ride": return "骑行";
            case "drive": return "自驾";
            case "train": return "火车";
            case "highspeed": return "高铁/动车";
            case "flight": return "飞机";
            default: return mode;
        }
    }

    /**
     * 【新增】解析合并请求结果
     */
    private void parseCombinedDayResult(String content, int dayNumber, List<String> modes) {
        String jsonStr = extractJson(content);
        if (jsonStr.isEmpty()) {
            Log.w(TAG, "第" + dayNumber + "天 合并结果: 未能提取到有效JSON");
            return;
        }

        try {
            JsonElement jsonElement = gson.fromJson(jsonStr, JsonElement.class);
            if (jsonElement == null || !jsonElement.isJsonObject()) {
                Log.w(TAG, "第" + dayNumber + "天 合并结果: JSON不是对象格式");
                return;
            }

            JsonObject result = jsonElement.getAsJsonObject();
            Log.d(TAG, "第" + dayNumber + "天 合并结果: 包含字段 " + result.keySet());

            // 逐个方式解析
            for (String mode : modes) {
                if (result.has(mode)) {
                    JsonElement modeElement = result.get(mode);
                    if (modeElement.isJsonObject()) {
                        JsonObject modeResult = modeElement.getAsJsonObject();
                        // 复用已有的解析逻辑
                        switch (mode) {
                            case "walk":
                                parseWalkingRoute(modeResult, dayNumber);
                                break;
                            case "ride":
                                parseRidingRoute(modeResult, dayNumber);
                                break;
                            case "transit":
                                parseTransitSimple(modeResult, dayNumber);
                                break;
                            case "taxi":
                                if (modeResult.has("carType")) {
                                    parseTaxiSimple(modeResult, dayNumber);
                                } else if (modeResult.has("taxiOptions")) {
                                    parseTaxiOptions(modeResult, dayNumber);
                                }
                                break;
                            case "train":
                            case "highspeed":
                                parseTrainOptions(modeResult, dayNumber, mode);
                                break;
                            case "flight":
                                parseFlightOptions(modeResult, dayNumber);
                                break;
                            case "drive":
                                parseDrivingSegments(modeResult, dayNumber);
                                break;
                        }
                    } else {
                        Log.d(TAG, "第" + dayNumber + "天 " + mode + ": 返回空对象，跳过");
                    }
                } else {
                    Log.d(TAG, "第" + dayNumber + "天 " + mode + ": 未在合并结果中找到");
                }
            }

            Log.d(TAG, "第" + dayNumber + "天 合并解析完成, 当前票务数量: " + allTickets.size());

        } catch (Exception e) {
            Log.e(TAG, "第" + dayNumber + "天 合并解析JSON失败: " + e.getMessage(), e);
        }
    }

    /**
     * 构建某一天某一种方式的Prompt
     * 重新设计：确保AI返回完整的班次信息
     * 修复：根据天数动态计算当天实际起点（第1天用用户输入，第N天用前一天最后景点）
     */
    private String buildDayModePrompt(Itinerary.DayPlan dayPlan, int dayNumber, String mode,
                                       String customOrigin, String customDestination) {
        StringBuilder prompt = new StringBuilder();

        // 当天景点列表
        List<Itinerary.SpotPlan> spots = dayPlan.getSpots();
        StringBuilder attractionsBuilder = new StringBuilder();
        if (spots != null && !spots.isEmpty()) {
            for (int i = 0; i < spots.size(); i++) {
                Itinerary.SpotPlan spot = spots.get(i);
                if (spot.getAttractionName() != null) {
                    attractionsBuilder.append(spot.getAttractionName());
                    if (spot.getArrivalTime() != null) {
                        attractionsBuilder.append("@").append(spot.getArrivalTime());
                    }
                    if (i < spots.size() - 1) {
                        attractionsBuilder.append(" -> ");
                    }
                }
            }
        }
        String attractionsRoute = attractionsBuilder.toString();

        // 【关键修复】根据天数动态计算当天的实际起点
        String actualOrigin = calculateActualOrigin(dayPlan, dayNumber, customOrigin);
        String actualDestination = calculateActualDestination(dayPlan, dayNumber, customDestination);

        // 根据出行方式构建不同的Prompt
        switch (mode) {
            case "transit":
                // 【重点】公共交通：明确要求生成班次信息
                prompt.append("请为以下行程规划公共交通路线：\n\n");
                prompt.append("【行程信息】\n");
                prompt.append("第").append(dayNumber).append("天\n");
                prompt.append("起点：").append(actualOrigin).append("\n");
                prompt.append("终点：").append(actualDestination).append("\n");
                prompt.append("途经景点：").append(attractionsRoute).append("\n\n");
                prompt.append("【重要】请生成真实的公交/地铁班次信息，包括：\n");
                prompt.append("- 具体的公交线路号（如：地铁2号线、公交45路）\n");
                prompt.append("- 首末班车时间\n");
                prompt.append("- 预计票价\n");
                prompt.append("- 换乘站点\n\n");
                prompt.append("【返回格式】（必须严格按此JSON格式返回，不要添加任何解释文字）\n");
                prompt.append("{\n");
                prompt.append("  \"routeName\": \"线路名称（如：地铁2号线）\",\n");
                prompt.append("  \"departureTime\": \"首班车时间（如：06:00）\",\n");
                prompt.append("  \"arrivalTime\": \"末班车时间（如：23:00）\",\n");
                prompt.append("  \"duration\": \"预计时长（如：45分钟）\",\n");
                prompt.append("  \"distance\": \"预计距离（如：15公里）\",\n");
                prompt.append("  \"cost\": \"预计票价（如：5元）\",\n");
                prompt.append("  \"transfers\": 换乘次数（数字）,\n");
                prompt.append("  \"steps\": [\n");
                prompt.append("    {\"line\": \"线路号\", \"from\": \"上车站\", \"to\": \"下车站\", \"direction\": \"方向\"},\n");
                prompt.append("    {\"line\": \"线路号\", \"from\": \"上车站\", \"to\": \"下车站\", \"direction\": \"方向\"}\n");
                prompt.append("  ],\n");
                prompt.append("  \"tips\": \"乘坐建议\"\n");
                prompt.append("}");
                break;

            case "taxi":
                // 打车：本市内的便捷方式
                prompt.append("请为以下行程规划打车路线：\n\n");
                prompt.append("【行程信息】\n");
                prompt.append("第").append(dayNumber).append("天\n");
                prompt.append("起点：").append(actualOrigin).append("\n");
                prompt.append("终点：").append(actualDestination).append("\n");
                prompt.append("途经景点：").append(attractionsRoute).append("\n\n");
                prompt.append("【返回格式】（必须严格按此JSON格式返回）\n");
                prompt.append("{\n");
                prompt.append("  \"carType\": \"车型（如：经济型/舒适型）\",\n");
                prompt.append("  \"estimatedTime\": \"预估时间（如：30分钟）\",\n");
                prompt.append("  \"estimatedDistance\": \"预估距离（如：12公里）\",\n");
                prompt.append("  \"estimatedCost\": 预估费用（数字，单位元）,\n");
                prompt.append("  \"waitTime\": \"预计等待时间（如：5分钟）\",\n");
                prompt.append("  \"pickupPoint\": \"上车点建议\",\n");
                prompt.append("  \"dropoffPoint\": \"下车点建议\",\n");
                prompt.append("  \"trafficCondition\": \"路况提示\",\n");
                prompt.append("  \"tips\": \"乘坐建议\"\n");
                prompt.append("}");
                break;

            case "walk":
                // 步行：适合短距离
                prompt.append("请为以下行程规划步行路线：\n\n");
                prompt.append("【行程信息】\n");
                prompt.append("第").append(dayNumber).append("天\n");
                prompt.append("起点：").append(actualOrigin).append("\n");
                prompt.append("终点：").append(actualDestination).append("\n");
                prompt.append("途经景点：").append(attractionsRoute).append("\n\n");
                prompt.append("【返回格式】（必须严格按此JSON格式返回）\n");
                prompt.append("{\n");
                prompt.append("  \"totalDistance\": \"总距离（如：800米）\",\n");
                prompt.append("  \"totalDuration\": \"总时长（如：10分钟）\",\n");
                prompt.append("  \"steps\": [\n");
                prompt.append("    {\"from\": \"出发点\", \"to\": \"目的地\", \"instruction\": \"行走指示\", \"distance\": \"距离\"}\n");
                prompt.append("  ],\n");
                prompt.append("  \"caloriesBurned\": \"消耗热量\",\n");
                prompt.append("  \"healthTip\": \"健康提示\"\n");
                prompt.append("}");
                break;

            case "ride":
                // 骑行：适合中短距离
                prompt.append("请为以下行程规划骑行路线：\n\n");
                prompt.append("【行程信息】\n");
                prompt.append("第").append(dayNumber).append("天\n");
                prompt.append("起点：").append(actualOrigin).append("\n");
                prompt.append("终点：").append(actualDestination).append("\n");
                prompt.append("途经景点：").append(attractionsRoute).append("\n\n");
                prompt.append("【返回格式】（必须严格按此JSON格式返回）\n");
                prompt.append("{\n");
                prompt.append("  \"totalDistance\": \"总距离（如：5公里）\",\n");
                prompt.append("  \"totalDuration\": \"总时长（如：25分钟）\",\n");
                prompt.append("  \"bikeStations\": [\"共享单车停放点1\", \"共享单车停放点2\"],\n");
                prompt.append("  \"steps\": [\n");
                prompt.append("    {\"from\": \"出发点\", \"to\": \"目的地\", \"instruction\": \"骑行指示\", \"terrain\": \"路况\"}\n");
                prompt.append("  ],\n");
                prompt.append("  \"safetyTip\": \"安全提示\"\n");
                prompt.append("}");
                break;

            case "train":
            case "highspeed":
                // 火车/高铁：适合跨城长途
                prompt.append("请为以下行程规划火车/高铁方案：\n\n");
                prompt.append("【行程信息】\n");
                int totalDaysSingle = (allDayPlans != null) ? allDayPlans.size() : 1;
                prompt.append("第").append(dayNumber).append("天（共").append(totalDaysSingle).append("天）\n");
                if (dayNumber == 1) {
                    prompt.append("⚠️ 这是出发日，请规划从起点到目的地的去程车次\n");
                } else if (dayNumber == totalDaysSingle) {
                    prompt.append("⚠️ 这是返程日，请规划从景点返回出发地的返程车次\n");
                }
                prompt.append("起点：").append(actualOrigin).append("\n");
                prompt.append("终点：").append(actualDestination).append("\n");
                prompt.append("途经景点：").append(attractionsRoute).append("\n\n");
                prompt.append("【重要】请生成真实的车次信息，包括多种票价选择，至少提供2个班次选项。\n");
                prompt.append("【返回格式】（必须严格按此JSON格式返回）\n");
                prompt.append("{\n");
                prompt.append("  \"trainOptions\": [\n");
                prompt.append("    {\n");
                prompt.append("      \"trainNumber\": \"车次号（如：G1234）\",\n");
                prompt.append("      \"departureStation\": \"出发站\",\n");
                prompt.append("      \"arrivalStation\": \"到达站\",\n");
                prompt.append("      \"departureTime\": \"出发时间（如：08:30）\",\n");
                prompt.append("      \"arrivalTime\": \"到达时间（如：12:45）\",\n");
                prompt.append("      \"duration\": \"历时（如：4小时15分钟）\",\n");
                prompt.append("      \"seats\": [\n");
                prompt.append("        {\"seatType\": \"座位类型\", \"price\": 票价（数字）},\n");
                prompt.append("        {\"seatType\": \"座位类型\", \"price\": 票价（数字）}\n");
                prompt.append("      ]\n");
                prompt.append("    }\n");
                prompt.append("  ]\n");
                prompt.append("}");
                break;

            case "flight":
                // 飞机：适合跨省/跨国长途
                prompt.append("请为以下行程规划航班方案：\n\n");
                prompt.append("【行程信息】\n");
                int totalDaysFlight = (allDayPlans != null) ? allDayPlans.size() : 1;
                prompt.append("第").append(dayNumber).append("天（共").append(totalDaysFlight).append("天）\n");
                if (dayNumber == 1) {
                    prompt.append("⚠️ 这是出发日，请规划从起点到目的地的去程航班\n");
                } else if (dayNumber == totalDaysFlight) {
                    prompt.append("⚠️ 这是返程日，请规划从目的地返回出发地的返程航班\n");
                }
                prompt.append("起点：").append(actualOrigin).append("\n");
                prompt.append("终点：").append(actualDestination).append("\n");
                prompt.append("途经景点：").append(attractionsRoute).append("\n\n");
                prompt.append("【返回格式】（必须严格按此JSON格式返回）\n");
                prompt.append("{\n");
                prompt.append("  \"flightOptions\": [\n");
                prompt.append("    {\n");
                prompt.append("      \"flightNumber\": \"航班号（如：CA1234）\",\n");
                prompt.append("      \"airline\": \"航空公司\",\n");
                prompt.append("      \"departureAirport\": \"出发机场（含航站楼）\",\n");
                prompt.append("      \"arrivalAirport\": \"到达机场（含航站楼）\",\n");
                prompt.append("      \"departureTime\": \"起飞时间\",\n");
                prompt.append("      \"arrivalTime\": \"到达时间\",\n");
                prompt.append("      \"duration\": \"飞行时长\",\n");
                prompt.append("      \"prices\": {\"economy\": 经济舱票价, \"business\": 商务舱票价}\n");
                prompt.append("    }\n");
                prompt.append("  ],\n");
                prompt.append("  \"tip\": \"乘机提示\"\n");
                prompt.append("}");
                break;

            case "drive":
                // 驾车
                prompt.append("请为以下行程规划驾车路线：\n\n");
                prompt.append("【行程信息】\n");
                prompt.append("第").append(dayNumber).append("天\n");
                prompt.append("起点：").append(actualOrigin).append("\n");
                prompt.append("终点：").append(actualDestination).append("\n");
                prompt.append("途经景点：").append(attractionsRoute).append("\n\n");
                prompt.append("【返回格式】（必须严格按此JSON格式返回）\n");
                prompt.append("{\n");
                prompt.append("  \"drivingSegments\": [\n");
                prompt.append("    {\"origin\": \"起点\", \"destination\": \"终点\", \"distance\": \"距离\", \"duration\": \"时间\", \"instruction\": \"行驶说明\"}\n");
                prompt.append("  ],\n");
                prompt.append("  \"totalDistance\": \"总距离\",\n");
                prompt.append("  \"totalCost\": 总花费（数字）,\n");
                prompt.append("  \"duration\": \"总时间\",\n");
                prompt.append("  \"parkingTips\": \"停车建议\"\n");
                prompt.append("}");
                break;

            default:
                // 默认公共交通
                prompt.append("交通规划-第").append(dayNumber).append("天\n");
                prompt.append("起点：").append(actualOrigin).append("\n");
                prompt.append("终点：").append(actualDestination).append("\n");
                prompt.append("途经：").append(attractionsRoute).append("\n\n");
                prompt.append("【返回格式】\n");
                prompt.append("{\n");
                prompt.append("  \"recommendations\": [\n");
                prompt.append("    {\"mode\": \"出行方式\", \"duration\": \"时长\", \"cost\": \"花费\", \"tickets\": [{\"tripNumber\": \"班次号\", \"departureTime\": \"出发时间\", \"arrivalTime\": \"到达时间\", \"origin\": \"出发站\", \"destination\": \"到达站\", \"price\": 票价}]}\n");
                prompt.append("  ]\n");
                prompt.append("}");
                break;
        }

        return prompt.toString();
    }

    /**
     * 【关键修复】根据天数动态计算当天实际起点
     * 第1天：使用用户输入的起点
     * 第N天(N>1)：使用前一天最后一个景点作为起点
     */
    private String calculateActualOrigin(Itinerary.DayPlan dayPlan, int dayNumber, String customOrigin) {
        // 第1天使用用户输入的起点
        if (dayNumber <= 1) {
            return customOrigin != null ? customOrigin : "用户出发地";
        }

        // 第N天(N>1)：查找前一天最后景点作为起点
        if (allDayPlans != null && !allDayPlans.isEmpty()) {
            for (Itinerary.DayPlan dp : allDayPlans) {
                if (dp.getDayNumber() != null && dp.getDayNumber() == (dayNumber - 1)) {
                    List<Itinerary.SpotPlan> prevSpots = dp.getSpots();
                    if (prevSpots != null && !prevSpots.isEmpty()) {
                        Itinerary.SpotPlan lastSpot = prevSpots.get(prevSpots.size() - 1);
                        if (lastSpot.getAttractionName() != null && !lastSpot.getAttractionName().isEmpty()) {
                            return lastSpot.getAttractionName() + " (前一天终点)";
                        }
                    }
                    break;
                }
            }
        }

        // 如果找不到前一天数据，降级使用用户总起点
        return customOrigin != null ? customOrigin : "用户出发地";
    }

    /**
     * 【关键修复】根据天数动态计算当天实际终点
     * 最后一天：使用用户输入的总终点（返程目的地）
     * 中间天：使用当天最后一个景点
     */
    private String calculateActualDestination(Itinerary.DayPlan dayPlan, int dayNumber, String customDestination) {
        List<Itinerary.SpotPlan> spots = dayPlan.getSpots();

        // 【修复】最后一天使用用户输入的总终点（返程目的地）
        boolean isLastDay = (allDayPlans != null && dayNumber == allDayPlans.size());
        if (isLastDay && customDestination != null && !customDestination.isEmpty()) {
            return customDestination;
        }

        // 非最后一天：当天最后一个景点作为终点
        if (spots != null && !spots.isEmpty()) {
            Itinerary.SpotPlan lastSpot = spots.get(spots.size() - 1);
            if (lastSpot.getAttractionName() != null && !lastSpot.getAttractionName().isEmpty()) {
                return lastSpot.getAttractionName();
            }
        }

        return customDestination != null ? customDestination : "目的地";
    }

    /**
     * 解析AI响应
     */
    private String parseAIResponse(String responseBody) {
        JsonElement jsonElement = gson.fromJson(responseBody, JsonElement.class);
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return null;
        }

        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        if (!jsonResponse.has("choices") || !jsonResponse.get("choices").isJsonArray()) {
            return null;
        }

        JsonArray choices = jsonResponse.getAsJsonArray("choices");
        if (choices.size() == 0) {
            return null;
        }

        JsonObject choice = choices.get(0).getAsJsonObject();
        if (!choice.has("message")) {
            return null;
        }

        return choice.getAsJsonObject("message").get("content").getAsString();
    }

    /**
     * 解析单日单方式结果
     */
    private void parseDayModeResult(String content, int dayNumber, String mode) {
        String jsonStr = extractJson(content);
        if (jsonStr.isEmpty()) {
            Log.w(TAG, "第" + dayNumber + "天 " + mode + ": 未能提取到有效JSON");
            return;
        }

        try {
            JsonElement jsonElement = gson.fromJson(jsonStr, JsonElement.class);
            if (jsonElement == null || !jsonElement.isJsonObject()) {
                Log.w(TAG, "第" + dayNumber + "天 " + mode + ": JSON不是对象格式");
                return;
            }

            JsonObject result = jsonElement.getAsJsonObject();
            Log.d(TAG, "第" + dayNumber + "天 " + mode + ": 解析JSON, 包含字段: " + result.keySet());

            // 根据出行方式解析不同的JSON结构
            switch (mode) {
                case "walk":
                    parseWalkingRoute(result, dayNumber);
                    break;
                case "ride":
                    parseRidingRoute(result, dayNumber);
                    break;
                case "transit":
                    // 公共交通：使用新的简化格式解析
                    parseTransitSimple(result, dayNumber);
                    break;
                case "taxi":
                    // taxi 模式
                    if (result.has("carType")) {
                        parseTaxiSimple(result, dayNumber);
                    } else if (result.has("taxiOptions")) {
                        parseTaxiOptions(result, dayNumber);
                    } else {
                        Log.w(TAG, "第" + dayNumber + "天 taxi: 未找到可解析的选项格式");
                    }
                    break;
                case "train":
                case "highspeed":
                    parseTrainOptions(result, dayNumber, mode);
                    break;
                case "flight":
                    parseFlightOptions(result, dayNumber);
                    break;
                case "drive":
                    parseDrivingSegments(result, dayNumber);
                    break;
                default:
                    parseDefaultRecommendations(result, dayNumber, mode);
                    break;
            }

            // 检查本次解析是否产生了有效数据
            Log.d(TAG, "第" + dayNumber + "天 " + mode + ": 解析完成, 当前票务数量: " + allTickets.size());

        } catch (Exception e) {
            Log.e(TAG, "解析JSON失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析简化格式的公共交通（新版）
     */
    private void parseTransitSimple(JsonObject result, int dayNumber) {
        // 新的简化格式：直接解析顶层字段
        if (result.has("routeName") || result.has("departureTime") || result.has("steps")) {
            TicketInfo ticket = new TicketInfo();
            ticket.setDayNumber(dayNumber);
            ticket.setMode("transit");
            ticket.setTransportMode("公交/地铁 (第" + dayNumber + "天)");

            // 线路名称（作为班次号）
            if (result.has("routeName")) {
                ticket.setTripNumber(result.get("routeName").getAsString());
            } else {
                ticket.setTripNumber("公交/地铁");
            }

            // 首班车时间
            if (result.has("departureTime")) {
                ticket.setOrigin("首班: " + result.get("departureTime").getAsString());
            }

            // 末班车时间
            if (result.has("arrivalTime")) {
                ticket.setDestination("末班: " + result.get("arrivalTime").getAsString());
            }

            // 时长
            if (result.has("duration")) {
                ticket.setDuration(result.get("duration").getAsString());
            }

            // 距离
            if (result.has("distance")) {
                ticket.setInstruction("距离: " + result.get("distance").getAsString());
            }

            // 票价
            if (result.has("cost")) {
                try {
                    ticket.setPrice(BigDecimal.valueOf(result.get("cost").getAsDouble()));
                } catch (Exception e) {
                    ticket.setPrice(BigDecimal.ZERO);
                }
            }

            // 换乘次数
            if (result.has("transfers")) {
                ticket.setSeatType("换乘" + result.get("transfers").getAsInt() + "次");
            }

            // 提示信息
            if (result.has("tips")) {
                ticket.setSafetyTip(result.get("tips").getAsString());
            }

            // 解析详细步骤
            if (result.has("steps") && result.get("steps").isJsonArray()) {
                JsonArray steps = result.getAsJsonArray("steps");
                StringBuilder stepInfo = new StringBuilder();
                for (JsonElement stepElement : steps) {
                    if (!stepElement.isJsonObject()) continue;
                    JsonObject step = stepElement.getAsJsonObject();
                    if (step.has("line")) {
                        if (stepInfo.length() > 0) stepInfo.append(" → ");
                        stepInfo.append(step.get("line").getAsString());
                    }
                }
                if (stepInfo.length() > 0) {
                    ticket.setLandmarks(stepInfo.toString());
                }
            }

            synchronized (allTickets) {
                allTickets.add(ticket);
            }
            Log.d(TAG, "第" + dayNumber + "天 transit: 添加公共交通票务 - " + ticket.getTripNumber());
        }
        
        // 兼容旧格式 transitOptions
        else if (result.has("transitOptions")) {
            parseTransitOptions(result, dayNumber);
        }
        
        // 兼容 recommendations 格式
        else if (result.has("recommendations")) {
            parseDefaultRecommendations(result, dayNumber, "transit");
        }
        
        else {
            Log.w(TAG, "第" + dayNumber + "天 transit: 未找到可解析的字段");
        }
    }

    /**
     * 解析简化格式的打车信息（新版）
     */
    private void parseTaxiSimple(JsonObject result, int dayNumber) {
        TicketInfo ticket = new TicketInfo();
        ticket.setDayNumber(dayNumber);
        ticket.setMode("taxi");
        ticket.setTransportMode("打车 (第" + dayNumber + "天)");

        // 车型
        if (result.has("carType")) {
            ticket.setSeatType(result.get("carType").getAsString());
        }
        
        // 预估时间
        if (result.has("estimatedTime")) {
            ticket.setDuration(result.get("estimatedTime").getAsString());
        }
        
        // 预估距离
        if (result.has("estimatedDistance")) {
            ticket.setDistance(result.get("estimatedDistance").getAsString());
        }
        
        // 预估费用
        if (result.has("estimatedCost")) {
            try {
                ticket.setPrice(BigDecimal.valueOf(result.get("estimatedCost").getAsDouble()));
            } catch (Exception e) {
                ticket.setPrice(BigDecimal.ZERO);
            }
        }
        
        // 等待时间
        if (result.has("waitTime")) {
            ticket.setInstruction("等待: " + result.get("waitTime").getAsString());
        }
        
        // 上车点
        if (result.has("pickupPoint")) {
            ticket.setOrigin(result.get("pickupPoint").getAsString());
        }
        
        // 下车点
        if (result.has("dropoffPoint")) {
            ticket.setDestination(result.get("dropoffPoint").getAsString());
        }
        
        // 提示
        if (result.has("tips")) {
            ticket.setSafetyTip(result.get("tips").getAsString());
        }

        synchronized (allTickets) {
            allTickets.add(ticket);
        }
        Log.d(TAG, "第" + dayNumber + "天 taxi: 添加打车信息 - " + ticket.getSeatType());
    }

    /**
     * 解析步行路线
     */
    private void parseWalkingRoute(JsonObject result, int dayNumber) {
        // 兼容两种格式：嵌套格式(walkingRoute包裹)和扁平格式(合并prompt直接在顶层)
        JsonObject walkData = result;
        if (result.has("walkingRoute") && result.get("walkingRoute").isJsonObject()) {
            walkData = result.getAsJsonObject("walkingRoute");
        }

        if (walkData.has("steps") && walkData.get("steps").isJsonArray()) {
            JsonArray steps = walkData.getAsJsonArray("steps");
            for (JsonElement stepElement : steps) {
                if (!stepElement.isJsonObject()) continue;
                JsonObject step = stepElement.getAsJsonObject();

                TicketInfo ticket = new TicketInfo();
                ticket.setDayNumber(dayNumber);
                ticket.setMode("walk");
                ticket.setTransportMode("步行 (第" + dayNumber + "天)");

                if (step.has("from")) ticket.setOrigin(step.get("from").getAsString());
                if (step.has("to")) ticket.setDestination(step.get("to").getAsString());
                if (step.has("distance")) ticket.setDistance(step.get("distance").getAsString());
                if (step.has("duration")) ticket.setDuration(step.get("duration").getAsString());
                if (step.has("instruction")) ticket.setInstruction(step.get("instruction").getAsString());
                if (step.has("landmarks")) ticket.setLandmarks(step.get("landmarks").getAsString());

                if (walkData.has("totalDistance")) {
                    ticket.setTripNumber(walkData.get("totalDistance").getAsString());
                }

                synchronized (allTickets) {
                    allTickets.add(ticket);
                }
            }
        }

        // 步行汇总信息 - 兼容两种格式
        if (walkData.has("caloriesBurned") || walkData.has("totalDistance") || walkData.has("totalDuration")) {
            TicketInfo summary = new TicketInfo();
            summary.setDayNumber(dayNumber);
            summary.setMode("walk");
            summary.setTransportMode("步行汇总");
            if (walkData.has("caloriesBurned")) {
                summary.setTripNumber("消耗热量: " + walkData.get("caloriesBurned").getAsString());
            }
            if (walkData.has("totalDuration")) {
                summary.setDuration(walkData.get("totalDuration").getAsString());
            }
            if (walkData.has("totalDistance")) {
                summary.setDistance(walkData.get("totalDistance").getAsString());
                if (!walkData.has("caloriesBurned")) {
                    summary.setTripNumber(walkData.get("totalDistance").getAsString());
                }
            }
            if (result.has("healthTip")) {
                summary.setHealthTip(result.get("healthTip").getAsString());
            } else if (walkData.has("healthTip")) {
                summary.setHealthTip(walkData.get("healthTip").getAsString());
            }
            synchronized (allTickets) {
                allTickets.add(summary);
            }
        }

        // 兼容合并prompt的扁平格式(无steps但有totalDistance/totalDuration)
        if (!walkData.has("steps") && (walkData.has("totalDistance") || walkData.has("distance"))) {
            TicketInfo ticket = new TicketInfo();
            ticket.setDayNumber(dayNumber);
            ticket.setMode("walk");
            ticket.setTransportMode("步行 (第" + dayNumber + "天)");

            if (walkData.has("totalDistance")) ticket.setDistance(walkData.get("totalDistance").getAsString());
            else if (walkData.has("distance")) ticket.setDistance(walkData.get("distance").getAsString());
            if (walkData.has("totalDuration")) ticket.setDuration(walkData.get("totalDuration").getAsString());
            else if (walkData.has("duration")) ticket.setDuration(walkData.get("duration").getAsString());
            if (walkData.has("caloriesBurned")) ticket.setInstruction("消耗热量: " + walkData.get("caloriesBurned").getAsString());
            if (walkData.has("landmarks")) ticket.setLandmarks(walkData.get("landmarks").getAsString());
            if (result.has("healthTip")) ticket.setHealthTip(result.get("healthTip").getAsString());

            synchronized (allTickets) {
                allTickets.add(ticket);
            }
            Log.d(TAG, "第" + dayNumber + "天 walk: 添加步行信息(扁平格式)");
        }
    }

    /**
     * 解析骑行路线
     */
    private void parseRidingRoute(JsonObject result, int dayNumber) {
        // 兼容两种格式：嵌套格式(ridingRoute包裹)和扁平格式(合并prompt直接在顶层)
        JsonObject rideData = result;
        if (result.has("ridingRoute") && result.get("ridingRoute").isJsonObject()) {
            rideData = result.getAsJsonObject("ridingRoute");
        }

        if (rideData.has("steps") && rideData.get("steps").isJsonArray()) {
            JsonArray steps = rideData.getAsJsonArray("steps");
            for (JsonElement stepElement : steps) {
                if (!stepElement.isJsonObject()) continue;
                JsonObject step = stepElement.getAsJsonObject();

                TicketInfo ticket = new TicketInfo();
                ticket.setDayNumber(dayNumber);
                ticket.setMode("ride");
                ticket.setTransportMode("骑行 (第" + dayNumber + "天)");

                if (step.has("from")) ticket.setOrigin(step.get("from").getAsString());
                if (step.has("to")) ticket.setDestination(step.get("to").getAsString());
                if (step.has("distance")) ticket.setDistance(step.get("distance").getAsString());
                if (step.has("duration")) ticket.setDuration(step.get("duration").getAsString());
                if (step.has("instruction")) ticket.setInstruction(step.get("instruction").getAsString());
                if (step.has("terrain")) ticket.setTerrain(step.get("terrain").getAsString());

                if (rideData.has("totalDistance")) {
                    ticket.setTripNumber(rideData.get("totalDistance").getAsString());
                }

                synchronized (allTickets) {
                    allTickets.add(ticket);
                }
            }
        }

        // 骑行汇总信息 - 兼容两种格式
        if (rideData.has("totalDuration") || rideData.has("totalDistance")) {
            TicketInfo summary = new TicketInfo();
            summary.setDayNumber(dayNumber);
            summary.setMode("ride");
            summary.setTransportMode("骑行汇总");
            if (rideData.has("totalDuration")) {
                summary.setDuration(rideData.get("totalDuration").getAsString());
            }
            if (rideData.has("totalDistance")) {
                summary.setDistance(rideData.get("totalDistance").getAsString());
                summary.setTripNumber(rideData.get("totalDistance").getAsString());
            }
            if (rideData.has("bikeStations")) {
                try {
                    JsonArray stations = rideData.getAsJsonArray("bikeStations");
                    StringBuilder stationsStr = new StringBuilder();
                    for (JsonElement s : stations) {
                        if (stationsStr.length() > 0) stationsStr.append(", ");
                        stationsStr.append(s.getAsString());
                    }
                    summary.setBikeStations(stationsStr.toString());
                } catch (Exception e) {
                    // bikeStations可能是字符串而非数组
                    summary.setBikeStations(rideData.get("bikeStations").getAsString());
                }
            }
            if (result.has("safetyTip")) {
                summary.setSafetyTip(result.get("safetyTip").getAsString());
            } else if (rideData.has("safetyTip")) {
                summary.setSafetyTip(rideData.get("safetyTip").getAsString());
            }
            synchronized (allTickets) {
                allTickets.add(summary);
            }
        }

        // 兼容合并prompt的扁平格式(无steps但有totalDistance/totalDuration)
        if (!rideData.has("steps") && (rideData.has("totalDistance") || rideData.has("distance"))) {
            TicketInfo ticket = new TicketInfo();
            ticket.setDayNumber(dayNumber);
            ticket.setMode("ride");
            ticket.setTransportMode("骑行 (第" + dayNumber + "天)");

            if (rideData.has("totalDistance")) ticket.setDistance(rideData.get("totalDistance").getAsString());
            else if (rideData.has("distance")) ticket.setDistance(rideData.get("distance").getAsString());
            if (rideData.has("totalDuration")) ticket.setDuration(rideData.get("totalDuration").getAsString());
            else if (rideData.has("duration")) ticket.setDuration(rideData.get("duration").getAsString());
            if (rideData.has("bikeStations")) {
                try {
                    JsonArray stations = rideData.getAsJsonArray("bikeStations");
                    StringBuilder stationsStr = new StringBuilder();
                    for (JsonElement s : stations) {
                        if (stationsStr.length() > 0) stationsStr.append(", ");
                        stationsStr.append(s.getAsString());
                    }
                    ticket.setBikeStations(stationsStr.toString());
                } catch (Exception e) {
                    ticket.setBikeStations(rideData.get("bikeStations").getAsString());
                }
            }
            if (result.has("safetyTip")) ticket.setSafetyTip(result.get("safetyTip").getAsString());
            else if (rideData.has("safetyTip")) ticket.setSafetyTip(rideData.get("safetyTip").getAsString());

            synchronized (allTickets) {
                allTickets.add(ticket);
            }
            Log.d(TAG, "第" + dayNumber + "天 ride: 添加骑行信息(扁平格式)");
        }
    }

    /**
     * 解析公共交通选项
     */
    private void parseTransitOptions(JsonObject result, int dayNumber) {
        if (result.has("transitOptions") && result.get("transitOptions").isJsonArray()) {
            JsonArray options = result.getAsJsonArray("transitOptions");
            for (JsonElement optElement : options) {
                if (!optElement.isJsonObject()) continue;
                JsonObject opt = optElement.getAsJsonObject();

                TicketInfo ticket = new TicketInfo();
                ticket.setDayNumber(dayNumber);
                ticket.setMode("transit");
                ticket.setTransportMode("公交/地铁 (第" + dayNumber + "天)");

                if (opt.has("routeName")) ticket.setTripNumber(opt.get("routeName").getAsString());
                if (opt.has("totalDuration")) ticket.setDuration(opt.get("totalDuration").getAsString());
                if (opt.has("totalDistance")) ticket.setDistance(opt.get("totalDistance").getAsString());
                if (opt.has("totalCost")) {
                    try {
                        ticket.setPrice(BigDecimal.valueOf(opt.get("totalCost").getAsDouble()));
                    } catch (Exception e) {
                        ticket.setPrice(BigDecimal.ZERO);
                    }
                }
                if (opt.has("transfers")) {
                    ticket.setSeatType("换乘" + opt.get("transfers").getAsInt() + "次");
                }
                if (opt.has("transferStations")) {
                    JsonArray transfers = opt.getAsJsonArray("transferStations");
                    StringBuilder transferStr = new StringBuilder();
                    for (JsonElement t : transfers) {
                        if (transferStr.length() > 0) transferStr.append(" → ");
                        transferStr.append(t.getAsString());
                    }
                    ticket.setInstruction("换乘站: " + transferStr.toString());
                }
                if (opt.has("firstBus")) ticket.setOrigin("首班车: " + opt.get("firstBus").getAsString());
                if (opt.has("lastBus")) ticket.setDestination("末班车: " + opt.get("lastBus").getAsString());
                if (opt.has("peakAdvice")) ticket.setHealthTip(opt.get("peakAdvice").getAsString());

                synchronized (allTickets) {
                    allTickets.add(ticket);
                }
            }
        }
    }

    /**
     * 解析打车选项（本市行程的公共交通自动转为打车）
     */
    private void parseTaxiOptions(JsonObject result, int dayNumber) {
        if (result.has("taxiOptions") && result.get("taxiOptions").isJsonArray()) {
            JsonArray options = result.getAsJsonArray("taxiOptions");
            for (JsonElement optElement : options) {
                if (!optElement.isJsonObject()) continue;
                JsonObject opt = optElement.getAsJsonObject();

                TicketInfo ticket = new TicketInfo();
                ticket.setDayNumber(dayNumber);
                ticket.setMode("taxi");
                ticket.setTransportMode("打车 (第" + dayNumber + "天)");

                // 车型
                if (opt.has("carType")) {
                    ticket.setSeatType(opt.get("carType").getAsString());
                }
                // 预估时间
                if (opt.has("estimatedTime")) {
                    ticket.setDuration(opt.get("estimatedTime").getAsString());
                }
                // 预估距离
                if (opt.has("estimatedDistance")) {
                    ticket.setDistance(opt.get("estimatedDistance").getAsString());
                }
                // 预估费用
                if (opt.has("estimatedCost")) {
                    try {
                        ticket.setPrice(BigDecimal.valueOf(opt.get("estimatedCost").getAsDouble()));
                    } catch (Exception e) {
                        ticket.setPrice(BigDecimal.ZERO);
                    }
                }
                // 等待时间
                if (opt.has("waitTime")) {
                    ticket.setInstruction("等待: " + opt.get("waitTime").getAsString());
                }
                // 上车点
                if (opt.has("pickupPoint")) {
                    ticket.setOrigin(opt.get("pickupPoint").getAsString());
                }
                // 下车点
                if (opt.has("dropoffPoint")) {
                    ticket.setDestination(opt.get("dropoffPoint").getAsString());
                }
                // 路况
                if (opt.has("trafficCondition")) {
                    ticket.setHealthTip("路况: " + opt.get("trafficCondition").getAsString());
                }
                // 建议
                if (opt.has("tips")) {
                    ticket.setSafetyTip(opt.get("tips").getAsString());
                }

                synchronized (allTickets) {
                    allTickets.add(ticket);
                }
            }

            // 如果有最便宜/最快方案标记
            if (result.has("cheapestOption")) {
                TicketInfo cheapest = new TicketInfo();
                cheapest.setDayNumber(dayNumber);
                cheapest.setMode("taxi");
                cheapest.setTransportMode("💰 最便宜方案");
                cheapest.setInstruction(result.get("cheapestOption").getAsString());
                synchronized (allTickets) {
                    allTickets.add(cheapest);
                }
            }
            if (result.has("fastestOption")) {
                TicketInfo fastest = new TicketInfo();
                fastest.setDayNumber(dayNumber);
                fastest.setMode("taxi");
                fastest.setTransportMode("⚡ 最快方案");
                fastest.setInstruction(result.get("fastestOption").getAsString());
                synchronized (allTickets) {
                    allTickets.add(fastest);
                }
            }
        }
    }

    /**
     * 解析火车/高铁选项
     */
    private void parseTrainOptions(JsonObject result, int dayNumber, String mode) {
        if (result.has("trainOptions") && result.get("trainOptions").isJsonArray()) {
            JsonArray options = result.getAsJsonArray("trainOptions");
            for (JsonElement optElement : options) {
                if (!optElement.isJsonObject()) continue;
                JsonObject opt = optElement.getAsJsonObject();

                // 兼容两种字段名：seatTypes(AI实际返回) 和 seats(旧格式)
                JsonArray seatArray = null;
                String seatNameField = "seatType";  // seats格式用seatType
                
                if (opt.has("seatTypes") && opt.get("seatTypes").isJsonArray()) {
                    seatArray = opt.getAsJsonArray("seatTypes");
                    seatNameField = "type";  // seatTypes格式用type
                } else if (opt.has("seats") && opt.get("seats").isJsonArray()) {
                    seatArray = opt.getAsJsonArray("seats");
                    seatNameField = "seatType";
                }

                if (seatArray != null && seatArray.size() > 0) {
                    // 每种座位类型创建一个票务信息
                    for (JsonElement seatElement : seatArray) {
                        if (!seatElement.isJsonObject()) continue;
                        JsonObject seat = seatElement.getAsJsonObject();

                        TicketInfo ticket = new TicketInfo();
                        ticket.setDayNumber(dayNumber);
                        ticket.setMode(mode);
                        ticket.setTransportMode(getModeName(mode) + " (第" + dayNumber + "天)");

                        if (opt.has("trainNumber")) ticket.setTripNumber(opt.get("trainNumber").getAsString());
                        if (opt.has("departureTime")) ticket.setDepartureTime(opt.get("departureTime").getAsString());
                        if (opt.has("arrivalTime")) ticket.setArrivalTime(opt.get("arrivalTime").getAsString());
                        if (opt.has("duration")) ticket.setDuration(opt.get("duration").getAsString());
                        if (opt.has("departureStation")) ticket.setOrigin(opt.get("departureStation").getAsString());
                        if (opt.has("arrivalStation")) ticket.setDestination(opt.get("arrivalStation").getAsString());
                        // 座位类型：兼容type和seatType两种字段
                        if (seat.has("type")) ticket.setSeatType(seat.get("type").getAsString());
                        else if (seat.has("seatType")) ticket.setSeatType(seat.get("seatType").getAsString());
                        if (seat.has("price")) {
                            try {
                                ticket.setPrice(BigDecimal.valueOf(seat.get("price").getAsDouble()));
                            } catch (Exception e) {
                                ticket.setPrice(BigDecimal.ZERO);
                            }
                        }

                        synchronized (allTickets) {
                            allTickets.add(ticket);
                        }
                    }
                } else {
                    // 无座位信息时，至少创建一条基础票务记录
                    TicketInfo ticket = new TicketInfo();
                    ticket.setDayNumber(dayNumber);
                    ticket.setMode(mode);
                    ticket.setTransportMode(getModeName(mode) + " (第" + dayNumber + "天)");

                    if (opt.has("trainNumber")) ticket.setTripNumber(opt.get("trainNumber").getAsString());
                    if (opt.has("departureTime")) ticket.setDepartureTime(opt.get("departureTime").getAsString());
                    if (opt.has("arrivalTime")) ticket.setArrivalTime(opt.get("arrivalTime").getAsString());
                    if (opt.has("duration")) ticket.setDuration(opt.get("duration").getAsString());
                    if (opt.has("departureStation")) ticket.setOrigin(opt.get("departureStation").getAsString());
                    if (opt.has("arrivalStation")) ticket.setDestination(opt.get("arrivalStation").getAsString());
                    if (opt.has("price")) {
                        try {
                            ticket.setPrice(BigDecimal.valueOf(opt.get("price").getAsDouble()));
                        } catch (Exception e) {
                            ticket.setPrice(BigDecimal.ZERO);
                        }
                    }

                    synchronized (allTickets) {
                        allTickets.add(ticket);
                    }
                }
            }
        }
    }

    /**
     * 解析航班选项
     */
    private void parseFlightOptions(JsonObject result, int dayNumber) {
        if (result.has("flightOptions") && result.get("flightOptions").isJsonArray()) {
            JsonArray options = result.getAsJsonArray("flightOptions");
            for (JsonElement optElement : options) {
                if (!optElement.isJsonObject()) continue;
                JsonObject opt = optElement.getAsJsonObject();

                // 经济舱作为主要选项
                TicketInfo ticket = new TicketInfo();
                ticket.setDayNumber(dayNumber);
                ticket.setMode("flight");
                ticket.setTransportMode("飞机 (第" + dayNumber + "天)");

                if (opt.has("flightNumber")) ticket.setTripNumber(opt.get("flightNumber").getAsString());
                if (opt.has("airline")) ticket.setInstruction(opt.get("airline").getAsString());
                if (opt.has("departureTime")) ticket.setDepartureTime(opt.get("departureTime").getAsString());
                if (opt.has("arrivalTime")) ticket.setArrivalTime(opt.get("arrivalTime").getAsString());
                if (opt.has("duration")) ticket.setDuration(opt.get("duration").getAsString());
                if (opt.has("departureAirport")) ticket.setOrigin(opt.get("departureAirport").getAsString());
                if (opt.has("arrivalAirport")) ticket.setDestination(opt.get("arrivalAirport").getAsString());
                if (opt.has("aircraft")) ticket.setSeatType(opt.get("aircraft").getAsString());
                if (opt.has("tip")) ticket.setSafetyTip(opt.get("tip").getAsString());

                // 票价优先使用经济舱
                if (opt.has("prices") && opt.get("prices").isJsonObject()) {
                    JsonObject prices = opt.getAsJsonObject("prices");
                    if (prices.has("economy")) {
                        try {
                            ticket.setPrice(BigDecimal.valueOf(prices.get("economy").getAsDouble()));
                        } catch (Exception e) {
                            ticket.setPrice(BigDecimal.ZERO);
                        }
                    }
                    // 添加其他舱位价格信息
                    StringBuilder priceInfo = new StringBuilder();
                    if (prices.has("economy")) priceInfo.append("经济舱:¥").append(prices.get("economy").getAsString());
                    if (prices.has("business")) priceInfo.append(" | 商务舱:¥").append(prices.get("business").getAsString());
                    if (prices.has("firstClass")) priceInfo.append(" | 头等舱:¥").append(prices.get("firstClass").getAsString());
                    ticket.setLandmarks(priceInfo.toString());
                }

                synchronized (allTickets) {
                    allTickets.add(ticket);
                }
            }
        }
    }

    /**
     * 解析驾车路线
     */
    private void parseDrivingSegments(JsonObject result, int dayNumber) {
        // 兼容两种字段名：drivingSegments(标准) 和 segments(AI可能返回)
        JsonArray segmentsArray = null;
        if (result.has("drivingSegments") && result.get("drivingSegments").isJsonArray()) {
            segmentsArray = result.getAsJsonArray("drivingSegments");
        } else if (result.has("segments") && result.get("segments").isJsonArray()) {
            segmentsArray = result.getAsJsonArray("segments");
        }

        if (segmentsArray != null) {
            for (JsonElement segElement : segmentsArray) {
                if (!segElement.isJsonObject()) continue;
                JsonObject seg = segElement.getAsJsonObject();

                DrivingRouteSegment segment = new DrivingRouteSegment();
                segment.setDayNumber(dayNumber);
                segment.setModeName(getModeName("drive"));

                if (seg.has("origin")) segment.setOrigin(seg.get("origin").getAsString());
                if (seg.has("destination")) segment.setDestination(seg.get("destination").getAsString());
                if (seg.has("distance")) segment.setDistance(seg.get("distance").getAsString());
                if (seg.has("duration")) segment.setDuration(seg.get("duration").getAsString());
                if (seg.has("roadType")) segment.setRoadType(seg.get("roadType").getAsString());
                if (seg.has("instruction")) segment.setInstruction(seg.get("instruction").getAsString());
                if (seg.has("tollFee")) {
                    try {
                        segment.setTollFee(BigDecimal.valueOf(seg.get("tollFee").getAsDouble()));
                    } catch (Exception e) {
                        segment.setTollFee(BigDecimal.ZERO);
                    }
                }

                synchronized (allDrivingSegments) {
                    allDrivingSegments.add(segment);
                }
            }
            Log.d(TAG, "第" + dayNumber + "天 drive: 解析到" + segmentsArray.size() + "段驾车路线");
        }

        // 兼容：AI可能返回单个驾车路线而非数组
        if (segmentsArray == null && (result.has("origin") || result.has("destination"))) {
            DrivingRouteSegment segment = new DrivingRouteSegment();
            segment.setDayNumber(dayNumber);
            segment.setModeName(getModeName("drive"));

            if (result.has("origin")) segment.setOrigin(result.get("origin").getAsString());
            if (result.has("destination")) segment.setDestination(result.get("destination").getAsString());
            if (result.has("distance")) segment.setDistance(result.get("distance").getAsString());
            if (result.has("duration")) segment.setDuration(result.get("duration").getAsString());
            if (result.has("roadType")) segment.setRoadType(result.get("roadType").getAsString());
            if (result.has("instruction")) segment.setInstruction(result.get("instruction").getAsString());
            if (result.has("tollFee")) {
                try {
                    segment.setTollFee(BigDecimal.valueOf(result.get("tollFee").getAsDouble()));
                } catch (Exception e) {
                    segment.setTollFee(BigDecimal.ZERO);
                }
            }

            synchronized (allDrivingSegments) {
                allDrivingSegments.add(segment);
            }
            Log.d(TAG, "第" + dayNumber + "天 drive: 解析到1段驾车路线(单个对象格式)");
        }

        // 汇总数据
        if (result.has("totalDistance") && totalDistance.isEmpty()) {
            totalDistance = result.get("totalDistance").getAsString();
        }
        if (result.has("totalCost")) {
            try {
                totalCost = totalCost.add(BigDecimal.valueOf(result.get("totalCost").getAsDouble()));
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * 解析默认推荐格式（兼容旧格式）
     */
    private void parseDefaultRecommendations(JsonObject result, int dayNumber, String mode) {
        if (result.has("recommendations") && result.get("recommendations").isJsonArray()) {
            JsonArray recommendations = result.getAsJsonArray("recommendations");
            for (JsonElement recElement : recommendations) {
                if (!recElement.isJsonObject()) continue;
                JsonObject rec = recElement.getAsJsonObject();

                String modeName = rec.has("modeName") ? rec.get("modeName").getAsString() : getModeName(mode);

                if (rec.has("tickets") && rec.get("tickets").isJsonArray()) {
                    JsonArray tickets = rec.getAsJsonArray("tickets");
                    for (JsonElement ticketElement : tickets) {
                        if (!ticketElement.isJsonObject()) continue;
                        JsonObject ticketJson = ticketElement.getAsJsonObject();

                        TicketInfo ticket = new TicketInfo();
                        ticket.setDayNumber(dayNumber);
                        ticket.setMode(mode);
                        ticket.setTransportMode(modeName + " (第" + dayNumber + "天)");

                        if (ticketJson.has("tripNumber")) ticket.setTripNumber(ticketJson.get("tripNumber").getAsString());
                        if (ticketJson.has("departureTime")) ticket.setDepartureTime(ticketJson.get("departureTime").getAsString());
                        if (ticketJson.has("arrivalTime")) ticket.setArrivalTime(ticketJson.get("arrivalTime").getAsString());
                        if (ticketJson.has("duration")) ticket.setDuration(ticketJson.get("duration").getAsString());
                        if (ticketJson.has("seatType")) ticket.setSeatType(ticketJson.get("seatType").getAsString());
                        if (ticketJson.has("origin")) ticket.setOrigin(ticketJson.get("origin").getAsString());
                        if (ticketJson.has("destination")) ticket.setDestination(ticketJson.get("destination").getAsString());
                        if (ticketJson.has("price")) {
                            try {
                                ticket.setPrice(BigDecimal.valueOf(ticketJson.get("price").getAsDouble()));
                            } catch (Exception e) {
                                ticket.setPrice(BigDecimal.ZERO);
                            }
                        }
                        if (ticketJson.has("cost")) {
                            try {
                                ticket.setPrice(BigDecimal.valueOf(ticketJson.get("cost").getAsDouble()));
                            } catch (Exception e) {
                                // ignore
                            }
                        }

                        synchronized (allTickets) {
                            allTickets.add(ticket);
                        }
                    }
                }
            }
        }
    }

    /**
     * 从AI响应中提取JSON
     */
    private String extractJson(String response) {
        if (response == null || response.isEmpty()) {
            return "";
        }

        String cleaned = response.trim();
        int start = cleaned.indexOf("{");
        int end = cleaned.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            return cleaned.substring(start, end + 1);
        }

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }

        start = cleaned.indexOf("{");
        end = cleaned.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            return cleaned.substring(start, end + 1);
        }

        return "";
    }

    /**
     * 处理单个请求完成
     */
    private void handleRequestComplete(int dayNumber, int dayIndex, boolean success) {
        synchronized (pendingCalls) {
            pendingCalls.removeIf(call -> !call.isExecuted() || call.isCanceled());
        }

        int completed = completedRequests.incrementAndGet();
        dayProgress[dayIndex]++;

        final int currentDay = dayNumber;

        Log.d(TAG, "请求完成: 第" + dayNumber + "天, 成功=" + success + 
              ", 进度=" + completed + "/" + totalRequests + 
              ", 当前票务数=" + allTickets.size());

        // 回调进度
        mainHandler.post(() -> {
            if (callback != null) {
                callback.onProgress(currentDay, totalDays, dayProgress[dayIndex], selectedModesCount(dayIndex));
            }
        });

        // 检查是否全部完成
        if (completed >= totalRequests) {
            mainHandler.post(() -> {
                Log.d(TAG, "所有请求完成! 总票务数: " + allTickets.size() + 
                      ", 驾车分段数: " + allDrivingSegments.size());
                
                // 按天数排序
                List<TicketInfo> sortedTickets = sortTicketsByDay(new ArrayList<>(allTickets));
                List<DrivingRouteSegment> sortedSegments = sortDrivingSegmentsByDay(new ArrayList<>(allDrivingSegments));

                if (callback != null) {
                    callback.onComplete(sortedTickets, sortedSegments, totalDistance, totalTollFee);
                }
            });
        }
    }

    /**
     * 按天数递增排序票务信息
     */
    private List<TicketInfo> sortTicketsByDay(List<TicketInfo> tickets) {
        Collections.sort(tickets, new Comparator<TicketInfo>() {
            @Override
            public int compare(TicketInfo t1, TicketInfo t2) {
                int dayCompare = Integer.compare(t1.getDayNumber(), t2.getDayNumber());
                if (dayCompare != 0) return dayCompare;
                // 同一天按出发时间排序
                String time1 = t1.getDepartureTime() != null ? t1.getDepartureTime() : "";
                String time2 = t2.getDepartureTime() != null ? t2.getDepartureTime() : "";
                return time1.compareTo(time2);
            }
        });
        return tickets;
    }

    /**
     * 按天数递增排序驾车路线
     */
    private List<DrivingRouteSegment> sortDrivingSegmentsByDay(List<DrivingRouteSegment> segments) {
        Collections.sort(segments, new Comparator<DrivingRouteSegment>() {
            @Override
            public int compare(DrivingRouteSegment s1, DrivingRouteSegment s2) {
                return Integer.compare(s1.getDayNumber(), s2.getDayNumber());
            }
        });
        return segments;
    }

    private int selectedModesCount(int dayIndex) {
        // 简化处理：假设所有天数的出行方式数量相同
        return totalRequests / totalDays;
    }

    /**
     * 取消所有请求
     */
    public void cancel() {
        synchronized (pendingCalls) {
            for (Call call : pendingCalls) {
                if (!call.isCanceled()) {
                    call.cancel();
                }
            }
            pendingCalls.clear();
        }
    }

    /**
     * 判断是否为本市行程（公共交通默认改打车）
     * 只有明确检测到城市名相同才判定为本市
     */
    private boolean isSameCityTrip(String origin, String destination) {
        if (origin == null || destination == null || origin.isEmpty() || destination.isEmpty()) {
            return false;
        }

        // 提取城市名：去除具体地址，保留城市级别名称
        String cityOrigin = extractCity(origin);
        String cityDest = extractCity(destination);

        // 只有当两个城市名都能明确提取到时，才进行比较
        // 如果提取后仍包含市/区/县等后缀，说明提取成功
        boolean originHasSuffix = cityOrigin.contains("市") || cityOrigin.contains("区") || 
                                   cityOrigin.contains("县") || cityOrigin.contains("省");
        boolean destHasSuffix = cityDest.contains("市") || cityDest.contains("区") || 
                                 cityDest.contains("县") || cityDest.contains("省");

        // 只有两边都能明确提取到城市名时才判定为本市
        if (originHasSuffix && destHasSuffix && !cityOrigin.isEmpty() && !cityDest.isEmpty()) {
            boolean isSame = cityOrigin.equals(cityDest);
            Log.d(TAG, "本市判断: " + origin + " -> " + destination + " = " + isSame + 
                  " (城市: " + cityOrigin + " vs " + cityDest + ")");
            return isSame;
        }

        // 如果城市名太短（少于2个字），不进行本市判断
        if (cityOrigin.length() < 2 || cityDest.length() < 2) {
            Log.d(TAG, "本市判断跳过: 地址过短无法判断");
            return false;
        }

        // 如果无法提取城市名，检查是否包含共同的城市关键词
        return containsCommonCityKeyword(origin, destination);
    }

    /**
     * 从地址中提取城市名
     */
    private String extractCity(String address) {
        if (address == null || address.isEmpty()) {
            return "";
        }

        // 常见城市后缀
        String[] suffixes = {"市", "区", "县", "省"};
        for (String suffix : suffixes) {
            int idx = address.indexOf(suffix);
            if (idx > 0) {
                return address.substring(0, idx + 1);
            }
        }

        // 如果没有常见后缀，返回原始地址（用于后续比对）
        return address;
    }

    /**
     * 检查两个地址是否包含共同的城市关键词
     */
    private boolean containsCommonCityKeyword(String origin, String destination) {
        // 常见城市列表
        String[] cities = {"北京", "上海", "广州", "深圳", "杭州", "南京", "武汉", "成都",
                          "重庆", "西安", "天津", "苏州", "郑州", "长沙", "沈阳", "青岛",
                          "济南", "大连", "哈尔滨", "长春", "昆明", "福州", "厦门", "南昌",
                          "合肥", "石家庄", "贵阳", "太原", "兰州", "乌鲁木齐", "呼和浩特"};

        for (String city : cities) {
            if (origin.contains(city) && destination.contains(city)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为公共交通方式（会被本市行程改为打车）
     */
    private boolean isPublicTransportMode(String mode) {
        return "transit".equals(mode) || "train".equals(mode) ||
               "highspeed".equals(mode) || "flight".equals(mode);
    }

    /**
     * 获取出行方式中文名
     */
    private String getModeName(String mode) {
        switch (mode) {
            case "train": return "火车";
            case "highspeed": return "高铁";
            case "flight": return "飞机";
            case "drive": return "驾车";
            case "transit": return "公交";
            case "taxi": return "打车";
            case "walk": return "步行";
            case "ride": return "骑行";
            default: return "未知";
        }
    }
}
