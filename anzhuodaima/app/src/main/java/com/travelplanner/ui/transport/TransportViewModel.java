package com.travelplanner.ui.transport;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.travelplanner.model.RoutePlan;
import com.travelplanner.service.AMapService;

public class TransportViewModel extends ViewModel {
    private final AMapService aMapService;
    private final MutableLiveData<RoutePlan> routePlan = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public TransportViewModel() {
        aMapService = AMapService.getInstance();
    }

    public void planRouteWithAMap(String originAddress, String destinationAddress, String mode, String city) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        // 使用简单模式：直接用地址查询，不需要手动地理编码
        aMapService.geocode(originAddress, city, new AMapService.GeocodeCallback() {
            @Override
            public void onSuccess(double originLng, double originLat, String originFormatted) {
                // 解析出发地成功，继续解析目的地
                aMapService.geocode(destinationAddress, city, new AMapService.GeocodeCallback() {
                    @Override
                    public void onSuccess(double destLng, double destLat, String destFormatted) {
                        // 两个地址都解析成功，开始路线规划
                        requestRoutePlan(originLng, originLat, destLng, destLat, mode,
                                originFormatted, destFormatted);
                    }

                    @Override
                    public void onError(String message) {
                        handleError("目的地解析失败：" + message);
                    }
                });
            }

            @Override
            public void onError(String message) {
                handleError("出发地解析失败：" + message);
            }
        });
    }

    private void requestRoutePlan(double originLng, double originLat,
            double destLng, double destLat, String mode,
            String originFormatted, String destFormatted) {
        String originLngStr = String.valueOf(originLng);
        String originLatStr = String.valueOf(originLat);
        String destLngStr = String.valueOf(destLng);
        String destLatStr = String.valueOf(destLat);

        AMapService.RouteCallback callback = new AMapService.RouteCallback() {
            @Override
            public void onSuccess(RoutePlan plan) {
                plan.setOrigin(originFormatted);
                plan.setDestination(destFormatted);
                isLoading.postValue(false);
                routePlan.postValue(plan);
            }

            @Override
            public void onError(String message) {
                handleError("路线规划失败：" + message);
            }
        };

        switch (mode) {
            case "walk":
                aMapService.planWalkingRoute(originLngStr, originLatStr, destLngStr, destLatStr, callback);
                break;
            case "transit":
                // 公交模式需要城市参数
                aMapService.planTransitRoute(originLngStr, originLatStr, destLngStr, destLatStr,
                        "廊坊市", callback);
                break;
            case "ride":
                aMapService.planBicyclingRoute(originLngStr, originLatStr, destLngStr, destLatStr, callback);
                break;
            case "drive":
            default:
                aMapService.planDrivingRoute(originLngStr, originLatStr, destLngStr, destLatStr, callback);
                break;
        }
    }

    private void handleError(String error) {
        isLoading.postValue(false);
        errorMessage.postValue(error);
    }

    public LiveData<RoutePlan> getRoutePlan() {
        return routePlan;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void clearError() {
        errorMessage.setValue(null);
    }
}
