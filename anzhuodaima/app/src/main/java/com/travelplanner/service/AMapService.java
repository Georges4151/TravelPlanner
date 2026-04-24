package com.travelplanner.service;

import android.os.Handler;
import android.os.Looper;

import com.travelplanner.data.remote.AMapConfig;
import com.travelplanner.data.remote.api.AMapApi;
import com.travelplanner.model.RoutePlan;
import com.travelplanner.model.amap.AMapDirectionResponse;
import com.travelplanner.model.amap.AMapGeocodeResponse;
import com.travelplanner.model.amap.AMapWeatherResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AMapService {
    private static AMapService instance;
    private final AMapApi aMapApi;
    private final Executor backgroundExecutor = Executors.newCachedThreadPool();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private AMapService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AMapConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        aMapApi = retrofit.create(AMapApi.class);
    }

    public static synchronized AMapService getInstance() {
        if (instance == null) {
            instance = new AMapService();
        }
        return instance;
    }

    public interface GeocodeCallback {
        void onSuccess(double longitude, double latitude, String formattedAddress);

        void onError(String message);
    }

    public void geocode(String address, String city, GeocodeCallback callback) {
        String cityParam = (city == null || city.isEmpty()) ? "" : city;

        backgroundExecutor.execute(() -> {
            try {
                Call<AMapGeocodeResponse> call = aMapApi.geocode(AMapConfig.getApiKey(), address, cityParam);
                Response<AMapGeocodeResponse> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    AMapGeocodeResponse geocodeResponse = response.body();
                    if (geocodeResponse.isSuccess() && geocodeResponse.getGeocodes() != null
                            && !geocodeResponse.getGeocodes().isEmpty()) {
                        AMapGeocodeResponse.Geocode geocode = geocodeResponse.getGeocodes().get(0);
                        postToMain(() -> callback.onSuccess(
                                geocode.getLongitude(),
                                geocode.getLatitude(),
                                geocode.getFormattedAddress()));
                    } else {
                        String errorMsg = geocodeResponse.getInfo() != null ? geocodeResponse.getInfo()
                                : "未找到该地址";
                        postToMain(() -> callback.onError(errorMsg));
                    }
                } else {
                    postToMain(() -> callback.onError("地理编码请求失败"));
                }
            } catch (Exception e) {
                postToMain(() -> callback.onError("网络错误：" + e.getMessage()));
            }
        });
    }

    public interface RouteCallback {
        void onSuccess(RoutePlan routePlan);

        void onError(String message);
    }

    public void planDrivingRoute(String originLng, String originLat,
            String destLng, String destLat, RouteCallback callback) {
        Call<AMapDirectionResponse> call = aMapApi.drivingDirection(AMapConfig.getApiKey(),
                originLng + "," + originLat, destLng + "," + destLat, "all", 0);
        executeRoutePlan(call, callback, "drive");
    }

    public void planWalkingRoute(String originLng, String originLat,
            String destLng, String destLat, RouteCallback callback) {
        Call<AMapDirectionResponse> call = aMapApi.walkingDirection(AMapConfig.getApiKey(),
                originLng + "," + originLat, destLng + "," + destLat);
        executeRoutePlan(call, callback, "walk");
    }

    public void planBicyclingRoute(String originLng, String originLat,
            String destLng, String destLat, RouteCallback callback) {
        Call<AMapDirectionResponse> call = aMapApi.bicyclingDirection(AMapConfig.getApiKey(),
                originLng + "," + originLat, destLng + "," + destLat);
        executeRoutePlan(call, callback, "ride");
    }

    public void planTransitRoute(String originLng, String originLat,
            String destLng, String destLat, String city, RouteCallback callback) {
        if (city == null || city.isEmpty()) {
            postToMain(() -> callback.onError("公交规划需要指定城市，请输入同城内的起终点"));
            return;
        }

        Call<AMapDirectionResponse> call = aMapApi.transitDirection(AMapConfig.getApiKey(),
                originLng + "," + originLat, destLng + "," + destLat, city, city);
        executeRoutePlan(call, callback, "transit");
    }

    private void executeRoutePlan(Call<AMapDirectionResponse> call, RouteCallback callback, String mode) {
        final RouteCallback finalCallback = callback;
        final String finalMode = mode;

        backgroundExecutor.execute(() -> {
            try {
                Response<AMapDirectionResponse> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    AMapDirectionResponse directionResponse = response.body();
                    if (directionResponse.isSuccess() && directionResponse.getRoute() != null) {
                        AMapDirectionResponse.Route route = directionResponse.getRoute();
                        RoutePlan plan = null;

                        if ("transit".equals(finalMode) && route.getTransits() != null
                                && !route.getTransits().isEmpty()) {
                            plan = convertTransitToRoutePlan(route.getTransits().get(0));
                        } else if (route.getPaths() != null && !route.getPaths().isEmpty()) {
                            plan = convertToRoutePlan(route.getPaths().get(0), finalMode);
                        }

                        if (plan != null) {
                            final RoutePlan finalPlan = plan;
                            postToMain(() -> finalCallback.onSuccess(finalPlan));
                        } else {
                            postToMain(() -> finalCallback.onError("未找到可行路线"));
                        }
                    } else {
                        String errorMsg = getErrorMessage(directionResponse.getInfocode(), directionResponse.getInfo());
                        postToMain(() -> finalCallback.onError(errorMsg));
                    }
                } else {
                    postToMain(() -> finalCallback.onError("路线规划请求失败"));
                }
            } catch (Exception e) {
                postToMain(() -> finalCallback.onError("网络错误：" + e.getMessage()));
            }
        });
    }

    private void postToMain(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
        } else {
            mainHandler.post(action);
        }
    }

    private String getErrorMessage(String infocode, String info) {
        if (infocode == null)
            return "路线规划失败";

        switch (infocode) {
            case "20803":
                return "距离过远，步行导航仅支持 100 公里以内，请选择驾车或公交";
            case "10002":
                return "骑行导航服务暂未开通，请选择其他出行方式";
            case "30001":
                return "地址解析失败，请检查输入的地址是否正确";
            case "30002":
                return "规划起终点距离过近，无需导航";
            case "30003":
                return "起终点有歧义，请输入更详细的地址";
            default:
                return "路线规划失败：" + (info != null ? info : "未知错误");
        }
    }

    private RoutePlan convertToRoutePlan(AMapDirectionResponse.Path path, String mode) {
        RoutePlan plan = new RoutePlan();

        // 确保距离不为0
        int distance = path.getDistance();
        if (distance <= 0) {
            distance = 1000; // 默认1公里
        }
        plan.setDistance(distance);

        // 确保时间不为0
        int duration = path.getDuration();
        if (duration <= 0) {
            duration = Math.max(600, distance / 10); // 默认至少10分钟，或按距离估算
        }
        plan.setDuration(duration);

        // 计算费用，确保不为0
        try {
            int tolls = path.getTolls();
            if (tolls > 0) {
                plan.setCost(new BigDecimal(tolls));
            } else {
                // 如果没有路费，根据距离估算费用
                double estimatedCost = (distance / 1000.0) * 0.5; // 每公里0.5元估算
                plan.setCost(new BigDecimal(Math.max(estimatedCost, 10))); // 至少10元
            }
        } catch (Exception e) {
            // 估算费用
            double estimatedCost = (distance / 1000.0) * 0.5;
            plan.setCost(new BigDecimal(Math.max(estimatedCost, 10)));
        }

        plan.setTransportMode(mode);
        plan.setTrafficStatus("畅通");

        List<com.travelplanner.model.RouteStep> steps = new ArrayList<>();
        if (path.getSteps() != null && !path.getSteps().isEmpty()) {
            for (AMapDirectionResponse.Step step : path.getSteps()) {
                com.travelplanner.model.RouteStep routeStep = new com.travelplanner.model.RouteStep();
                routeStep.setInstruction(step.getInstruction());
                routeStep.setRoad(step.getRoad());

                // 确保步骤距离不为0
                int stepDistance = step.getDistance();
                if (stepDistance <= 0) {
                    stepDistance = distance / Math.max(path.getSteps().size(), 1);
                }
                routeStep.setDistance(stepDistance);

                // 确保步骤时间不为0
                int stepDuration = step.getDuration();
                if (stepDuration <= 0) {
                    stepDuration = duration / Math.max(path.getSteps().size(), 1);
                }
                routeStep.setDuration(stepDuration);

                routeStep.setAction(step.getAction());
                routeStep.setPolyline(step.getPolyline());
                steps.add(routeStep);
            }
        } else {
            // 如果没有步骤，添加一个默认步骤
            com.travelplanner.model.RouteStep defaultStep = new com.travelplanner.model.RouteStep();
            defaultStep.setInstruction("从起点出发前往终点");
            defaultStep.setRoad("推荐路线");
            defaultStep.setDistance(distance);
            defaultStep.setDuration(duration);
            defaultStep.setAction("直行");
            steps.add(defaultStep);
        }
        plan.setSteps(steps);
        return plan;
    }

    private RoutePlan convertTransitToRoutePlan(AMapDirectionResponse.Transit transit) {
        RoutePlan plan = new RoutePlan();
        plan.setDistance(transit.getDistance());
        plan.setDuration(transit.getDuration());

        try {
            plan.setCost(new BigDecimal(transit.getCost()));
        } catch (Exception e) {
            plan.setCost(BigDecimal.ZERO);
        }

        plan.setTransportMode("transit");

        List<com.travelplanner.model.RouteStep> steps = new ArrayList<>();
        if (transit.getSegments() != null) {
            for (AMapDirectionResponse.Segment segment : transit.getSegments()) {
                com.travelplanner.model.RouteStep routeStep = new com.travelplanner.model.RouteStep();
                String instruction = segment.getInstruction();

                if (instruction == null || instruction.isEmpty()) {
                    if (segment.getBus() != null && segment.getBus().getBuslines() != null
                            && !segment.getBus().getBuslines().isEmpty()) {
                        AMapDirectionResponse.BusLine busLine = segment.getBus().getBuslines().get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append("乘坐").append(busLine.getName());

                        if (busLine.getDepartureStop() != null) {
                            sb.append("，从").append(busLine.getDepartureStop().getName()).append("上车");
                        }
                        if (busLine.getArrivalStop() != null) {
                            sb.append("，到").append(busLine.getArrivalStop().getName()).append("下车");
                        }

                        instruction = sb.toString();
                    } else if (segment.getWalking() != null) {
                        instruction = "步行";
                    } else if (segment.getEntrance() != null) {
                        instruction = "从" + segment.getEntrance().getName() + "进站";
                    } else if (segment.getExit() != null) {
                        instruction = "从" + segment.getExit().getName() + "出站";
                    } else {
                        instruction = "继续前行";
                    }
                }

                routeStep.setInstruction(instruction);
                routeStep.setDistance(segment.getDistance());
                routeStep.setDuration(segment.getDuration());
                steps.add(routeStep);
            }
        }
        plan.setSteps(steps);
        return plan;
    }

    public interface WeatherCallback {
        void onSuccess(double avgTemp, String weatherCondition, List<AMapWeatherResponse.Cast> forecasts);

        void onError(String message);
    }

    public void getWeatherForecast(String city, String startDate, String endDate, WeatherCallback callback) {
        backgroundExecutor.execute(() -> {
            try {
                Call<AMapWeatherResponse> call = aMapApi.weatherForecast(
                        AMapConfig.getApiKey(), city, "all");
                Response<AMapWeatherResponse> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    AMapWeatherResponse weatherResponse = response.body();
                    if (weatherResponse.isSuccess() && weatherResponse.getForecasts() != null
                            && !weatherResponse.getForecasts().isEmpty()) {

                        List<AMapWeatherResponse.Cast> casts = weatherResponse.getForecasts().get(0).getCasts();
                        if (casts == null || casts.isEmpty()) {
                            postToMain(() -> callback.onError("暂无天气数据"));
                            return;
                        }

                        double totalTemp = 0;
                        int count = 0;
                        String mainWeather = "晴天";
                        List<AMapWeatherResponse.Cast> relevantCasts = new ArrayList<>();

                        for (AMapWeatherResponse.Cast cast : casts) {
                            try {
                                if (cast.getDayTemp() != null && !cast.getDayTemp().isEmpty()) {
                                    double dayTemp = Double.parseDouble(cast.getDayTemp());
                                    double nightTemp = cast.getNightTemp() != null && !cast.getNightTemp().isEmpty()
                                            ? Double.parseDouble(cast.getNightTemp())
                                            : dayTemp;
                                    totalTemp += (dayTemp + nightTemp) / 2;
                                    count++;

                                    if (cast.getDayWeather() != null && !cast.getDayWeather().isEmpty()) {
                                        mainWeather = cast.getDayWeather();
                                    }
                                    relevantCasts.add(cast);
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }

                        if (count > 0) {
                            double avgTemp = totalTemp / count;
                            final double finalAvgTemp = avgTemp;
                            final String finalWeather = mainWeather;
                            final List<AMapWeatherResponse.Cast> finalCasts = relevantCasts;
                            postToMain(() -> callback.onSuccess(finalAvgTemp, finalWeather, finalCasts));
                        } else {
                            postToMain(() -> callback.onError("无法解析温度数据"));
                        }
                    } else {
                        String errorMsg = weatherResponse.getInfo() != null ? weatherResponse.getInfo() : "获取天气失败";
                        postToMain(() -> callback.onError(errorMsg));
                    }
                } else {
                    postToMain(() -> callback.onError("天气请求失败"));
                }
            } catch (Exception e) {
                postToMain(() -> callback.onError("网络错误：" + e.getMessage()));
            }
        });
    }
}
