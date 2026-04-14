package com.travelplanner.ui.transport;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.data.remote.AMapConfig;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.AMapApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Itinerary;
import com.travelplanner.model.RoutePlan;
import com.travelplanner.model.TransportOptions;
import com.travelplanner.model.amap.AMapDirectionResponse;
import com.travelplanner.model.amap.AMapGeocodeResponse;
import com.travelplanner.util.HttpUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class TransportOptionsActivity extends AppCompatActivity {

    private Long itineraryId;
    private Integer dayNumber;
    private String city;
    private Itinerary itinerary;
    private Itinerary.DayPlan selectedDayPlan;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();
    private AMapApi aMapApi;

    private String userInputOrigin;
    private String userInputOriginLat;
    private String userInputOriginLng;

    private ProgressBar progressBar;
    private TextView tvError;
    private RecyclerView recyclerViewRoutes;
    private RouteSegmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_options);

        itineraryId = getIntent().getLongExtra("itineraryId", 0L);
        dayNumber = getIntent().getIntExtra("dayNumber", 0);
        city = getIntent().getStringExtra("city");
        userInputOrigin = getIntent().getStringExtra("origin");
        userInputOriginLat = getIntent().getStringExtra("originLat");
        userInputOriginLng = getIntent().getStringExtra("originLng");

        if (itineraryId == 0L || dayNumber == 0 || city == null) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        aMapApi = ApiClient.getAMapApi();

        setupToolbar();
        setupViews();
        loadItinerary();
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
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        recyclerViewRoutes = findViewById(R.id.recyclerViewRoutes);

        recyclerViewRoutes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RouteSegmentAdapter();
        recyclerViewRoutes.setAdapter(adapter);
    }

    private void loadItinerary() {
        showLoading(true);

        HttpUtil.get("itinerary/" + itineraryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    showLoading(false);
                    showError("网络错误: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<Itinerary> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Itinerary>>() {
                        }.getType());

                handler.post(() -> {
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        itinerary = apiResponse.getData();
                        findDayPlanAndGenerateTransport();
                    } else {
                        showLoading(false);
                        showError("加载行程失败");
                    }
                });
            }
        });
    }

    private void findDayPlanAndGenerateTransport() {
        if (itinerary == null || itinerary.getDayPlans() == null) {
            showError("行程数据不完整");
            return;
        }

        for (Itinerary.DayPlan dayPlan : itinerary.getDayPlans()) {
            if (dayPlan.getDayNumber().equals(dayNumber)) {
                selectedDayPlan = dayPlan;
                break;
            }
        }

        if (selectedDayPlan == null) {
            showError("找不到第" + dayNumber + "天的行程");
            return;
        }

        if (selectedDayPlan.getSpots() == null || selectedDayPlan.getSpots().isEmpty()) {
            showError("第" + dayNumber + "天没有景点安排");
            return;
        }

        generateTransportPlans();
    }

    /**
     * 智能地理编码方法，尝试多种查询策略
     */
    private String[] smartGeocode(String locationName, String cityHint) throws Exception {
        Log.d("TransportOptions", "智能地理编码: " + locationName + ", 城市提示: " + cityHint);

        // 策略1：直接使用景点名称，不带城市限制
        Log.d("TransportOptions", "策略1: 不带城市限制");
        String[] result = tryGeocode(locationName, "");
        if (result != null) {
            return result;
        }

        // 策略2：景点名称 + 城市
        if (cityHint != null && !cityHint.isEmpty()) {
            Log.d("TransportOptions", "策略2: 景点名称 + 城市");
            result = tryGeocode(locationName + " " + cityHint, cityHint);
            if (result != null) {
                return result;
            }
        }

        // 策略3：仅使用景点名称（可能前面的策略都太严格）
        Log.d("TransportOptions", "策略3: 仅使用景点名称重试");
        result = tryGeocode(locationName, "");
        if (result != null) {
            return result;
        }

        // 策略4：尝试更宽松的查询（去掉可能的修饰词）
        Log.d("TransportOptions", "策略4: 简化景点名称");
        String simpleName = simplifyLocationName(locationName);
        if (!simpleName.equals(locationName)) {
            result = tryGeocode(simpleName, "");
            if (result != null) {
                return result;
            }
        }

        Log.e("TransportOptions", "所有地理编码策略都失败: " + locationName);
        return null;
    }

    /**
     * 尝试一次地理编码查询
     */
    private String[] tryGeocode(String address, String city) throws Exception {
        try {
            Call<AMapGeocodeResponse> geocodingCall = aMapApi.geocode(AMapConfig.API_KEY, address, city);
            Response<AMapGeocodeResponse> geocodingResponse = geocodingCall.execute();

            if (geocodingResponse.isSuccessful() && geocodingResponse.body() != null) {
                AMapGeocodeResponse response = geocodingResponse.body();
                if (response.getGeocodes() != null && !response.getGeocodes().isEmpty()) {
                    // 获取第一个结果
                    AMapGeocodeResponse.Geocode geocode = response.getGeocodes().get(0);
                    String location = geocode.getLocation();
                    String formattedAddress = geocode.getFormattedAddress();

                    Log.d("TransportOptions", "地理编码成功 - 地址: " + formattedAddress + ", 位置: " + location);

                    if (location != null && location.contains(",")) {
                        String[] parts = location.split(",");
                        return new String[] { parts[0], parts[1] }; // [lng, lat]
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TransportOptions", "地理编码查询异常: " + address, e);
        }
        return null;
    }

    /**
     * 简化地点名称，去掉可能的修饰词
     */
    private String simplifyLocationName(String name) {
        if (name == null) {
            return "";
        }

        // 去掉常见的修饰词
        String simplified = name
                .replace("景区", "")
                .replace("公园", "")
                .replace("博物馆", "")
                .replace("纪念馆", "")
                .replace("古镇", "")
                .replace("古城", "")
                .replace("风景区", "")
                .replace("旅游区", "")
                .replace("度假村", "")
                .trim();

        // 如果简化后为空，返回原名
        return simplified.isEmpty() ? name : simplified;
    }

    private void generateTransportPlans() {
        executor.execute(() -> {
            try {
                List<Itinerary.SpotPlan> spots = selectedDayPlan.getSpots();
                if (spots == null || spots.isEmpty()) {
                    handler.post(() -> {
                        showLoading(false);
                        showError("当天没有景点安排");
                    });
                    return;
                }

                List<TransportOptions> allTransportOptions = new ArrayList<>();
                String currentOrigin = null;
                String currentOriginLat = null;
                String currentOriginLng = null;

                if (dayNumber == 1) {
                    if (userInputOrigin != null && userInputOriginLat != null && userInputOriginLng != null) {
                        currentOrigin = userInputOrigin;
                        currentOriginLat = userInputOriginLat;
                        currentOriginLng = userInputOriginLng;
                        Log.d("TransportOptions", "第一天使用用户输入的起点: " + currentOrigin);
                    } else {
                        currentOrigin = "北京市朝阳区建国路88号";
                        currentOriginLat = "39.9042";
                        currentOriginLng = "116.4074";
                        Log.d("TransportOptions", "第一天使用默认起点: " + currentOrigin);
                    }
                } else {
                    Itinerary.DayPlan previousDayPlan = null;
                    for (Itinerary.DayPlan dayPlan : itinerary.getDayPlans()) {
                        if (dayPlan.getDayNumber().equals(dayNumber - 1)) {
                            previousDayPlan = dayPlan;
                            break;
                        }
                    }

                    if (previousDayPlan != null && previousDayPlan.getSpots() != null
                            && !previousDayPlan.getSpots().isEmpty()) {
                        Itinerary.SpotPlan lastSpot = previousDayPlan.getSpots()
                                .get(previousDayPlan.getSpots().size() - 1);
                        currentOrigin = lastSpot.getAttractionName();
                        Log.d("TransportOptions", "非第一天，使用前一天最后一个景点作为起点: " + currentOrigin);

                        // 使用智能地理编码
                        String[] originCoords = smartGeocode(currentOrigin, city);
                        if (originCoords != null) {
                            currentOriginLng = originCoords[0];
                            currentOriginLat = originCoords[1];
                            Log.d("TransportOptions", "前一天最后景点位置: " + currentOriginLng + "," + currentOriginLat);
                        }

                        if (currentOriginLat == null || currentOriginLng == null) {
                            final String finalOriginName = currentOrigin;
                            handler.post(() -> {
                                showLoading(false);
                                showError("无法获取前一天最后景点位置: " + finalOriginName);
                            });
                            return;
                        }
                    } else {
                        currentOrigin = "北京市朝阳区建国路88号";
                        currentOriginLat = "39.9042";
                        currentOriginLng = "116.4074";
                        Log.d("TransportOptions", "非第一天但前一天无景点，使用默认起点: " + currentOrigin);
                    }
                }

                for (int i = 0; i < spots.size(); i++) {
                    Itinerary.SpotPlan spot = spots.get(i);
                    String destination = spot.getAttractionName();
                    String destLat = null;
                    String destLng = null;

                    Log.d("TransportOptions",
                            "开始获取景点位置 (" + (i + 1) + "/" + spots.size() + "): " + destination);

                    // 添加延迟，避免QPS超限
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    // 使用智能地理编码
                    String[] destCoords = smartGeocode(destination, city);
                    if (destCoords != null) {
                        destLng = destCoords[0];
                        destLat = destCoords[1];
                    }

                    if (destLat == null || destLng == null) {
                        final String finalDestName = destination;
                        handler.post(() -> {
                            showLoading(false);
                            showError("无法获取景点位置信息: " + finalDestName + "\n\n请检查景点名称是否正确，或尝试手动输入位置。");
                        });
                        return;
                    }

                    TransportOptions options = new TransportOptions();
                    options.setFromLocation(currentOrigin);
                    options.setToLocation(destination);

                    String finalOrigin = currentOriginLng + "," + currentOriginLat;
                    String finalDestination = destLng + "," + destLat;

                    // 添加延迟
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    // 调用高德路线API获取真实数据
                    generateTransportModeFromAMap(options, currentOrigin, destination, finalOrigin, finalDestination,
                            "drive");

                    // 添加延迟
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    generateTransportModeFromAMap(options, currentOrigin, destination, finalOrigin, finalDestination,
                            "transit");

                    // 添加延迟
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    generateTransportModeFromAMap(options, currentOrigin, destination, finalOrigin, finalDestination,
                            "walking");

                    // 添加延迟
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    generateTransportModeFromAMap(options, currentOrigin, destination, finalOrigin, finalDestination,
                            "cycling");

                    allTransportOptions.add(options);

                    currentOrigin = destination;
                    currentOriginLat = destLat;
                    currentOriginLng = destLng;
                }

                handler.post(() -> {
                    showLoading(false);
                    if (!allTransportOptions.isEmpty()) {
                        adapter.setData(allTransportOptions);
                        recyclerViewRoutes.setVisibility(View.VISIBLE);
                    }
                });

            } catch (Exception e) {
                handler.post(() -> {
                    showLoading(false);
                    showError("生成交通规划失败: " + e.getMessage());
                });
            }
        });
    }

    /**
     * 从高德API获取真实路线数据
     */
    private void generateTransportModeFromAMap(TransportOptions options, String origin, String destination,
            String originLngLat, String destLngLat, String transportMode) {

        try {
            RoutePlan plan = new RoutePlan();
            plan.setOrigin(origin);
            plan.setDestination(destination);
            plan.setTransportMode(transportMode);

            Response<AMapDirectionResponse> response = null;

            switch (transportMode) {
                case "drive":
                    Call<AMapDirectionResponse> drivingCall = aMapApi.drivingDirection(
                            AMapConfig.API_KEY, originLngLat, destLngLat, "all", 0);
                    response = drivingCall.execute();
                    break;
                case "transit":
                    Call<AMapDirectionResponse> transitCall = aMapApi.transitDirection(
                            AMapConfig.API_KEY, originLngLat, destLngLat, "", "");
                    response = transitCall.execute();
                    break;
                case "walking":
                    Call<AMapDirectionResponse> walkingCall = aMapApi.walkingDirection(
                            AMapConfig.API_KEY, originLngLat, destLngLat);
                    response = walkingCall.execute();
                    break;
                case "cycling":
                    Call<AMapDirectionResponse> cyclingCall = aMapApi.bicyclingDirection(
                            AMapConfig.API_KEY, originLngLat, destLngLat);
                    response = cyclingCall.execute();
                    break;
            }

            if (response != null && response.isSuccessful() && response.body() != null) {
                AMapDirectionResponse directionResponse = response.body();
                if (directionResponse.getRoute() != null) {

                    if ("transit".equals(transportMode) && directionResponse.getRoute().getTransits() != null
                            && !directionResponse.getRoute().getTransits().isEmpty()) {
                        AMapDirectionResponse.Transit transit = directionResponse.getRoute().getTransits().get(0);
                        plan.setDistance(transit.getDistance());
                        plan.setDuration(transit.getDuration());
                        if (transit.getCost() > 0) {
                            plan.setCost(BigDecimal.valueOf(transit.getCost()));
                        } else {
                            plan.setCost(BigDecimal.valueOf(4));
                        }
                    } else if (directionResponse.getRoute().getPaths() != null
                            && !directionResponse.getRoute().getPaths().isEmpty()) {
                        AMapDirectionResponse.Path path = directionResponse.getRoute().getPaths().get(0);
                        plan.setDistance(path.getDistance());
                        plan.setDuration(path.getDuration());

                        if ("drive".equals(transportMode)) {
                            double distanceKm = path.getDistance() / 1000.0;
                            double fuelCost = distanceKm * 0.8;
                            double tollCost = path.getTolls() > 0 ? path.getTolls()
                                    : (distanceKm > 20 ? distanceKm * 0.5 : 0);
                            plan.setCost(BigDecimal.valueOf(Math.max(fuelCost + tollCost, 10)));
                        } else if ("cycling".equals(transportMode)) {
                            double distanceKm = path.getDistance() / 1000.0;
                            plan.setCost(BigDecimal.valueOf(Math.max(2, distanceKm * 0.5)));
                        }
                    }

                    // 确保数据不为0
                    Integer currentDistance = plan.getDistance();
                    Integer currentDuration = plan.getDuration();

                    if (currentDistance == null || currentDistance <= 0 || currentDuration == null
                            || currentDuration <= 0) {
                        // 使用简单估算作为备用
                        double originLatDouble = Double.parseDouble(originLngLat.split(",")[1]);
                        double originLngDouble = Double.parseDouble(originLngLat.split(",")[0]);
                        double destLatDouble = Double.parseDouble(destLngLat.split(",")[1]);
                        double destLngDouble = Double.parseDouble(destLngLat.split(",")[0]);
                        int haversineDistance = calculateHaversineDistance(
                                originLatDouble, originLngDouble, destLatDouble, destLngDouble);
                        int estimatedDistance = (int) (haversineDistance * 1.3);
                        plan.setDistance(estimatedDistance);
                        int estimatedDuration = estimateDurationByDistance(estimatedDistance, transportMode);
                        plan.setDuration(estimatedDuration);
                        double cost = estimateCostByDistance(estimatedDistance, transportMode);
                        if (cost > 0) {
                            plan.setCost(BigDecimal.valueOf(cost));
                        }
                    }
                }
            } else {
                // API失败时使用估算
                double originLatDouble = Double.parseDouble(originLngLat.split(",")[1]);
                double originLngDouble = Double.parseDouble(originLngLat.split(",")[0]);
                double destLatDouble = Double.parseDouble(destLngLat.split(",")[1]);
                double destLngDouble = Double.parseDouble(destLngLat.split(",")[0]);
                int haversineDistance = calculateHaversineDistance(
                        originLatDouble, originLngDouble, destLatDouble, destLngDouble);
                int estimatedDistance = (int) (haversineDistance * 1.3);
                plan.setDistance(estimatedDistance);
                int estimatedDuration = estimateDurationByDistance(estimatedDistance, transportMode);
                plan.setDuration(estimatedDuration);
                double cost = estimateCostByDistance(estimatedDistance, transportMode);
                if (cost > 0) {
                    plan.setCost(BigDecimal.valueOf(cost));
                }
            }

            List<RoutePlan> plans = new ArrayList<>();
            plans.add(plan);

            switch (transportMode) {
                case "drive":
                    options.setDrivingPlans(plans);
                    break;
                case "transit":
                    options.setTransitPlans(plans);
                    break;
                case "walking":
                    options.setWalkingPlans(plans);
                    break;
                case "cycling":
                    options.setCyclingPlans(plans);
                    break;
            }

            Log.d("TransportOptions",
                    transportMode + " - 距离: " + (plan.getDistance() != null ? plan.getDistance() : "null") +
                            "米, 时间: " + (plan.getDuration() != null ? plan.getDuration() : "null") + "秒");

        } catch (Exception e) {
            Log.e("TransportOptions", "获取" + transportMode + "路线失败", e);

            // 出错时使用估算
            try {
                double originLatDouble = Double.parseDouble(originLngLat.split(",")[1]);
                double originLngDouble = Double.parseDouble(originLngLat.split(",")[0]);
                double destLatDouble = Double.parseDouble(destLngLat.split(",")[1]);
                double destLngDouble = Double.parseDouble(destLngLat.split(",")[0]);
                int haversineDistance = calculateHaversineDistance(
                        originLatDouble, originLngDouble, destLatDouble, destLngDouble);

                RoutePlan plan = new RoutePlan();
                plan.setOrigin(origin);
                plan.setDestination(destination);
                plan.setTransportMode(transportMode);
                int estimatedDistance = (int) (haversineDistance * 1.3);
                plan.setDistance(estimatedDistance);
                int estimatedDuration = estimateDurationByDistance(estimatedDistance, transportMode);
                plan.setDuration(estimatedDuration);
                double cost = estimateCostByDistance(estimatedDistance, transportMode);
                if (cost > 0) {
                    plan.setCost(BigDecimal.valueOf(cost));
                }

                List<RoutePlan> plans = new ArrayList<>();
                plans.add(plan);

                switch (transportMode) {
                    case "drive":
                        options.setDrivingPlans(plans);
                        break;
                    case "transit":
                        options.setTransitPlans(plans);
                        break;
                    case "walking":
                        options.setWalkingPlans(plans);
                        break;
                    case "cycling":
                        options.setCyclingPlans(plans);
                        break;
                }
            } catch (Exception ex) {
                Log.e("TransportOptions", "估算路线也失败", ex);
            }
        }
    }

    private void showLoading(Boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (loading) {
            recyclerViewRoutes.setVisibility(View.GONE);
            tvError.setVisibility(View.GONE);
        }
    }

    private void showError(String error) {
        if (error != null && !error.isEmpty()) {
            tvError.setText(error);
            tvError.setVisibility(View.VISIBLE);
            recyclerViewRoutes.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.GONE);
        }
    }

    /**
     * 使用Haversine公式计算两点之间的直线距离（米）
     */
    private int calculateHaversineDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceKm = earthRadius * c;
        return (int) (distanceKm * 1000);
    }

    /**
     * 根据距离和出行方式估算时间（秒）
     */
    private int estimateDurationByDistance(int distanceMeters, String transportMode) {
        double speedKmPerHour;

        switch (transportMode) {
            case "drive":
                speedKmPerHour = 40.0;
                break;
            case "transit":
                speedKmPerHour = 25.0;
                break;
            case "walking":
                speedKmPerHour = 5.0;
                break;
            case "cycling":
                speedKmPerHour = 15.0;
                break;
            default:
                speedKmPerHour = 30.0;
        }

        double distanceKm = distanceMeters / 1000.0;
        double hours = distanceKm / speedKmPerHour;
        return (int) (hours * 3600);
    }

    /**
     * 根据距离和出行方式估算费用（元）
     */
    private double estimateCostByDistance(int distanceMeters, String transportMode) {
        double distanceKm = distanceMeters / 1000.0;

        switch (transportMode) {
            case "drive":
                double fuelCost = distanceKm * 0.8;
                double tollCost = distanceKm > 20 ? distanceKm * 0.5 : 0;
                return Math.max(fuelCost + tollCost, 10);
            case "transit":
                return Math.max(4, distanceKm * 1.5);
            case "walking":
                return 0;
            case "cycling":
                return Math.max(2, distanceKm * 0.5);
            default:
                return Math.max(10, distanceKm * 1);
        }
    }
}
