package com.travelplanner.service;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.travelplanner.data.local.dao.AttractionDao;
import com.travelplanner.data.local.dao.AttractionDayDao;
import com.travelplanner.data.local.dao.ItineraryDao;
import com.travelplanner.data.local.dao.ItineraryDayDao;
import com.travelplanner.data.local.dao.TransportPlanDao;
import com.travelplanner.data.local.entity.AttractionDayEntity;
import com.travelplanner.data.local.entity.AttractionEntity;
import com.travelplanner.data.local.entity.ItineraryDayEntity;
import com.travelplanner.data.local.entity.ItineraryEntity;
import com.travelplanner.data.local.entity.TransportPlanEntity;
import com.travelplanner.data.remote.AMapConfig;
import com.travelplanner.data.remote.api.AMapApi;
import com.travelplanner.model.RoutePlan;
import com.travelplanner.model.RouteStep;
import com.travelplanner.model.TransportOptions;
import com.travelplanner.model.amap.AMapDirectionResponse;
import com.travelplanner.model.amap.AMapGeocodeResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportPlanService {
    private static TransportPlanService instance;

    private final AMapApi aMapApi;
    private final ItineraryDao itineraryDao;
    private final ItineraryDayDao itineraryDayDao;
    private final AttractionDayDao attractionDayDao;
    private final AttractionDao attractionDao;
    private final TransportPlanDao transportPlanDao;
    private final Executor executor;
    private final Handler mainHandler;

    private TransportPlanService(AMapApi aMapApi, ItineraryDao itineraryDao,
            ItineraryDayDao itineraryDayDao, AttractionDayDao attractionDayDao,
            AttractionDao attractionDao, TransportPlanDao transportPlanDao) {
        this.aMapApi = aMapApi;
        this.itineraryDao = itineraryDao;
        this.itineraryDayDao = itineraryDayDao;
        this.attractionDayDao = attractionDayDao;
        this.attractionDao = attractionDao;
        this.transportPlanDao = transportPlanDao;
        this.executor = Executors.newFixedThreadPool(4);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized TransportPlanService getInstance(AMapApi aMapApi, ItineraryDao itineraryDao,
            ItineraryDayDao itineraryDayDao, AttractionDayDao attractionDayDao,
            AttractionDao attractionDao, TransportPlanDao transportPlanDao) {
        if (instance == null) {
            instance = new TransportPlanService(aMapApi, itineraryDao, itineraryDayDao,
                    attractionDayDao, attractionDao, transportPlanDao);
        }
        return instance;
    }

    public interface TransportPlanCallback {
        void onSuccess(TransportOptions options);

        void onError(String error);
    }

    public void generateDayTransportPlans(Long dayId, String city, TransportPlanCallback callback) {
        executor.execute(() -> {
            try {
                ItineraryDayEntity day = itineraryDayDao.getByIdSync(dayId);
                if (day == null) {
                    mainHandler.post(() -> callback.onError("行程天不存在"));
                    return;
                }

                ItineraryEntity itinerary = itineraryDao.getByIdSync(day.getItineraryId());
                if (itinerary == null) {
                    mainHandler.post(() -> callback.onError("行程不存在"));
                    return;
                }

                List<AttractionDayEntity> attractionDays = attractionDayDao.getByDayIdSync(dayId);
                if (attractionDays.isEmpty()) {
                    mainHandler.post(() -> callback.onError("该天没有景点"));
                    return;
                }

                String fromAddress = itinerary.getDepartureAddress();
                String fromLat = itinerary.getDepartureLatitude() != null
                        ? String.valueOf(itinerary.getDepartureLatitude())
                        : null;
                String fromLng = itinerary.getDepartureLongitude() != null
                        ? String.valueOf(itinerary.getDepartureLongitude())
                        : null;

                if (fromAddress == null || fromLat == null || fromLng == null) {
                    mainHandler.post(() -> callback.onError("请先设置出发地址"));
                    return;
                }

                AttractionEntity firstAttraction = attractionDao.getById(attractionDays.get(0).getAttractionId());
                if (firstAttraction == null) {
                    mainHandler.post(() -> callback.onError("景点信息不存在"));
                    return;
                }

                String toAddress = firstAttraction.getAddress();
                String toLat = firstAttraction.getLatitude() != null ? String.valueOf(firstAttraction.getLatitude())
                        : null;
                String toLng = firstAttraction.getLongitude() != null ? String.valueOf(firstAttraction.getLongitude())
                        : null;

                if (toLat == null || toLng == null) {
                    mainHandler.post(() -> callback.onError("景点位置信息不完整"));
                    return;
                }

                String origin = fromLat + "," + fromLng;
                String destination = toLat + "," + toLng;

                generateAllTransportPlans(origin, destination, fromAddress, toAddress,
                        dayId, itinerary.getId(), city, callback);

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError("生成交通规划失败: " + e.getMessage()));
            }
        });
    }

    private void generateAllTransportPlans(String origin, String destination, String fromAddress,
            String toAddress, Long dayId, Long itineraryId,
            String city, TransportPlanCallback callback) {
        final List<RoutePlan> drivingPlans = new ArrayList<>();
        final List<RoutePlan> transitPlans = new ArrayList<>();
        final List<RoutePlan> walkingPlans = new ArrayList<>();
        final List<RoutePlan> cyclingPlans = new ArrayList<>();
        final int[] completedCount = { 0 };
        final int totalTasks = 4;

        final Runnable notifyComplete = () -> {
            TransportOptions options = new TransportOptions();
            options.setFromLocation(fromAddress);
            options.setToLocation(toAddress);
            options.setDrivingPlans(drivingPlans);
            options.setTransitPlans(transitPlans);
            options.setWalkingPlans(walkingPlans);
            options.setCyclingPlans(cyclingPlans);

            mainHandler.post(() -> callback.onSuccess(options));
        };

        generateDrivingPlan(origin, destination, fromAddress, toAddress, dayId, itineraryId, city,
                new RoutePlanCallback() {
                    @Override
                    public void onSuccess(RoutePlan plan) {
                        if (plan != null) {
                            drivingPlans.add(plan);
                        }
                        checkCompletion();
                    }

                    @Override
                    public void onError(String error) {
                        checkCompletion();
                    }

                    private void checkCompletion() {
                        completedCount[0]++;
                        if (completedCount[0] >= totalTasks) {
                            notifyComplete.run();
                        }
                    }
                });

        generateTransitPlan(origin, destination, fromAddress, toAddress, dayId, itineraryId, city,
                new RoutePlanCallback() {
                    @Override
                    public void onSuccess(RoutePlan plan) {
                        if (plan != null) {
                            transitPlans.add(plan);
                        }
                        checkCompletion();
                    }

                    @Override
                    public void onError(String error) {
                        checkCompletion();
                    }

                    private void checkCompletion() {
                        completedCount[0]++;
                        if (completedCount[0] >= totalTasks) {
                            notifyComplete.run();
                        }
                    }
                });

        generateWalkingPlan(origin, destination, fromAddress, toAddress, dayId, itineraryId,
                new RoutePlanCallback() {
                    @Override
                    public void onSuccess(RoutePlan plan) {
                        if (plan != null) {
                            walkingPlans.add(plan);
                        }
                        checkCompletion();
                    }

                    @Override
                    public void onError(String error) {
                        checkCompletion();
                    }

                    private void checkCompletion() {
                        completedCount[0]++;
                        if (completedCount[0] >= totalTasks) {
                            notifyComplete.run();
                        }
                    }
                });

        generateBicyclingPlan(origin, destination, fromAddress, toAddress, dayId, itineraryId,
                new RoutePlanCallback() {
                    @Override
                    public void onSuccess(RoutePlan plan) {
                        if (plan != null) {
                            cyclingPlans.add(plan);
                        }
                        checkCompletion();
                    }

                    @Override
                    public void onError(String error) {
                        checkCompletion();
                    }

                    private void checkCompletion() {
                        completedCount[0]++;
                        if (completedCount[0] >= totalTasks) {
                            notifyComplete.run();
                        }
                    }
                });
    }

    public interface RoutePlanCallback {
        void onSuccess(RoutePlan plan);

        void onError(String error);
    }

    private void generateDrivingPlan(String origin, String destination, String fromAddress, String toAddress,
            Long dayId, Long itineraryId, String city, RoutePlanCallback callback) {
        aMapApi.drivingDirection(AMapConfig.getApiKey(), origin, destination, "all", 0)
                .enqueue(new Callback<AMapDirectionResponse>() {
                    @Override
                    public void onResponse(Call<AMapDirectionResponse> call, Response<AMapDirectionResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            AMapDirectionResponse.Route route = response.body().getRoute();
                            if (route != null && route.getPaths() != null && !route.getPaths().isEmpty()) {
                                AMapDirectionResponse.Path path = route.getPaths().get(0);
                                RoutePlan plan = convertToRoutePlan(path, fromAddress, toAddress, "drive", dayId,
                                        itineraryId, city);
                                callback.onSuccess(plan);
                            } else {
                                callback.onError("没有找到驾车路线");
                            }
                        } else {
                            callback.onError("驾车路线规划失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<AMapDirectionResponse> call, Throwable t) {
                        callback.onError("网络请求失败: " + t.getMessage());
                    }
                });
    }

    private void generateTransitPlan(String origin, String destination, String fromAddress, String toAddress,
            Long dayId, Long itineraryId, String city, RoutePlanCallback callback) {
        aMapApi.transitDirection(AMapConfig.getApiKey(), origin, destination, city, city)
                .enqueue(new Callback<AMapDirectionResponse>() {
                    @Override
                    public void onResponse(Call<AMapDirectionResponse> call, Response<AMapDirectionResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            AMapDirectionResponse.Route route = response.body().getRoute();
                            if (route != null && route.getTransits() != null && !route.getTransits().isEmpty()) {
                                AMapDirectionResponse.Transit transit = route.getTransits().get(0);
                                RoutePlan plan = convertTransitToRoutePlan(transit, fromAddress, toAddress, "transit",
                                        dayId, itineraryId, city);
                                callback.onSuccess(plan);
                            } else {
                                callback.onError("没有找到公交路线");
                            }
                        } else {
                            callback.onError("公交路线规划失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<AMapDirectionResponse> call, Throwable t) {
                        callback.onError("网络请求失败: " + t.getMessage());
                    }
                });
    }

    private void generateWalkingPlan(String origin, String destination, String fromAddress, String toAddress,
            Long dayId, Long itineraryId, RoutePlanCallback callback) {
        aMapApi.walkingDirection(AMapConfig.getApiKey(), origin, destination)
                .enqueue(new Callback<AMapDirectionResponse>() {
                    @Override
                    public void onResponse(Call<AMapDirectionResponse> call, Response<AMapDirectionResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            AMapDirectionResponse.Route route = response.body().getRoute();
                            if (route != null && route.getPaths() != null && !route.getPaths().isEmpty()) {
                                AMapDirectionResponse.Path path = route.getPaths().get(0);
                                RoutePlan plan = convertToRoutePlan(path, fromAddress, toAddress, "walking", dayId,
                                        itineraryId, null);
                                callback.onSuccess(plan);
                            } else {
                                callback.onError("没有找到步行路线");
                            }
                        } else {
                            callback.onError("步行路线规划失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<AMapDirectionResponse> call, Throwable t) {
                        callback.onError("网络请求失败: " + t.getMessage());
                    }
                });
    }

    private void generateBicyclingPlan(String origin, String destination, String fromAddress, String toAddress,
            Long dayId, Long itineraryId, RoutePlanCallback callback) {
        aMapApi.bicyclingDirection(AMapConfig.getApiKey(), origin, destination)
                .enqueue(new Callback<AMapDirectionResponse>() {
                    @Override
                    public void onResponse(Call<AMapDirectionResponse> call, Response<AMapDirectionResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            AMapDirectionResponse.Route route = response.body().getRoute();
                            if (route != null && route.getPaths() != null && !route.getPaths().isEmpty()) {
                                AMapDirectionResponse.Path path = route.getPaths().get(0);
                                RoutePlan plan = convertToRoutePlan(path, fromAddress, toAddress, "cycling", dayId,
                                        itineraryId, null);
                                callback.onSuccess(plan);
                            } else {
                                callback.onError("没有找到骑行路线");
                            }
                        } else {
                            callback.onError("骑行路线规划失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<AMapDirectionResponse> call, Throwable t) {
                        callback.onError("网络请求失败: " + t.getMessage());
                    }
                });
    }

    private RoutePlan convertToRoutePlan(AMapDirectionResponse.Path path, String fromAddress, String toAddress,
            String transportMode, Long dayId, Long itineraryId, String city) {
        RoutePlan plan = new RoutePlan();
        plan.setOrigin(fromAddress);
        plan.setDestination(toAddress);
        plan.setTransportMode(transportMode);
        plan.setDistance(path.getDistance());
        plan.setDuration(path.getDuration());

        double distanceKm = path.getDistance() / 1000.0;
        long durationMinutes = path.getDuration() / 60;

        BigDecimal cost = calculateCost(transportMode, distanceKm, durationMinutes, city);
        plan.setCost(cost);

        if (path.getSteps() != null && !path.getSteps().isEmpty()) {
            List<RouteStep> steps = new ArrayList<>();
            for (AMapDirectionResponse.Step step : path.getSteps()) {
                RouteStep routeStep = new RouteStep();
                routeStep.setInstruction(step.getInstruction());
                routeStep.setRoad(step.getRoad());
                routeStep.setDistance(step.getDistance());
                routeStep.setDuration(step.getDuration());
                routeStep.setAction(step.getAction());
                steps.add(routeStep);
            }
            plan.setSteps(steps);
        }

        saveTransportPlan(plan, dayId, itineraryId, fromAddress, toAddress, transportMode);

        return plan;
    }

    private RoutePlan convertTransitToRoutePlan(AMapDirectionResponse.Transit transit, String fromAddress,
            String toAddress,
            String transportMode, Long dayId, Long itineraryId, String city) {
        RoutePlan plan = new RoutePlan();
        plan.setOrigin(fromAddress);
        plan.setDestination(toAddress);
        plan.setTransportMode(transportMode);
        plan.setDistance(transit.getDistance());
        plan.setDuration(transit.getDuration());

        double distanceKm = transit.getDistance() / 1000.0;
        long durationMinutes = transit.getDuration() / 60;
        int transferCount = transit.getTransitNum();

        BigDecimal cost = CostCalculator.calculateTransitCost(distanceKm, city, transferCount);
        plan.setCost(cost);

        if (transit.getSegments() != null && !transit.getSegments().isEmpty()) {
            List<RouteStep> steps = new ArrayList<>();
            for (AMapDirectionResponse.Segment segment : transit.getSegments()) {
                RouteStep routeStep = new RouteStep();
                routeStep.setInstruction(segment.getInstruction());
                routeStep.setDistance(segment.getDistance());
                routeStep.setDuration(segment.getDuration());
                steps.add(routeStep);
            }
            plan.setSteps(steps);
        }

        saveTransportPlan(plan, dayId, itineraryId, fromAddress, toAddress, transportMode);

        return plan;
    }

    private BigDecimal calculateCost(String transportMode, double distanceKm, long durationMinutes, String city) {
        switch (transportMode) {
            case "drive":
                return CostCalculator.calculateDrivingCost(distanceKm, city);
            case "transit":
                return CostCalculator.calculateTransitCost(distanceKm, city, 0);
            case "walking":
                return CostCalculator.calculateWalkingCost();
            case "cycling":
                return CostCalculator.calculateCyclingCost(durationMinutes, city);
            default:
                return BigDecimal.ZERO;
        }
    }

    private void saveTransportPlan(RoutePlan plan, Long dayId, Long itineraryId, String fromAddress,
            String toAddress, String transportMode) {
        try {
            TransportPlanEntity entity = new TransportPlanEntity();
            entity.setPlanId(UUID.randomUUID().toString());
            entity.setItineraryId(itineraryId);
            entity.setDayId(dayId);
            entity.setFromAddress(fromAddress);
            entity.setToAddress(toAddress);
            entity.setTransportMode(transportMode);
            entity.setDistance(plan.getDistance().doubleValue());
            entity.setDuration(plan.getDuration().longValue());
            entity.setCost(plan.getCost());
            entity.setCreateTime(System.currentTimeMillis());
            entity.setUpdateTime(System.currentTimeMillis());

            transportPlanDao.insert(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}