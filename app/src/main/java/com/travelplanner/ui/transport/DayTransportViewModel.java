package com.travelplanner.ui.transport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.travelplanner.data.local.dao.AttractionDao;
import com.travelplanner.data.local.dao.AttractionDayDao;
import com.travelplanner.data.local.dao.ItineraryDao;
import com.travelplanner.data.local.dao.ItineraryDayDao;
import com.travelplanner.data.local.dao.TransportPlanDao;
import com.travelplanner.data.local.entity.ItineraryDayEntity;
import com.travelplanner.data.local.entity.ItineraryEntity;
import com.travelplanner.data.local.entity.TransportPlanEntity;
import com.travelplanner.data.remote.api.AMapApi;
import com.travelplanner.model.TransportOptions;
import com.travelplanner.service.TransportPlanService;

import java.util.List;

/**
 * 每日交通规划ViewModel类
 * 负责处理行程中每日的交通安排相关业务逻辑
 * 与TransportPlanService交互，生成和管理交通规划
 */
public class DayTransportViewModel extends ViewModel {
    // 数据访问对象，用于访问本地数据库
    private final ItineraryDao itineraryDao;            // 行程数据访问
    private final ItineraryDayDao itineraryDayDao;        // 行程日数据访问
    private final AttractionDayDao attractionDayDao;      // 景点日数据访问
    private final AttractionDao attractionDao;            // 景点数据访问
    private final TransportPlanDao transportPlanDao;      // 交通规划数据访问
    private final AMapApi aMapApi;                        // 高德地图API接口

    // 交通规划服务，用于生成和管理交通规划
    private TransportPlanService transportPlanService;

    // LiveData对象，用于观察数据变化
    private final MutableLiveData<TransportOptions> transportOptions = new MutableLiveData<>(); // 交通选项数据
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();              // 错误信息
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);            // 加载状态

    /**
     * 构造函数
     * 初始化所有依赖的DAO和服务
     *
     * @param itineraryDao      行程数据访问对象
     * @param itineraryDayDao   行程日数据访问对象
     * @param attractionDayDao  景点日数据访问对象
     * @param attractionDao     景点数据访问对象
     * @param transportPlanDao  交通规划数据访问对象
     * @param aMapApi           高德地图API接口
     */
    public DayTransportViewModel(ItineraryDao itineraryDao, ItineraryDayDao itineraryDayDao,
            AttractionDayDao attractionDayDao, AttractionDao attractionDao,
            TransportPlanDao transportPlanDao, AMapApi aMapApi) {
        this.itineraryDao = itineraryDao;
        this.itineraryDayDao = itineraryDayDao;
        this.attractionDayDao = attractionDayDao;
        this.attractionDao = attractionDao;
        this.transportPlanDao = transportPlanDao;
        this.aMapApi = aMapApi;

        // 初始化交通规划服务
        this.transportPlanService = TransportPlanService.getInstance(aMapApi, itineraryDao, itineraryDayDao,
                attractionDayDao, attractionDao, transportPlanDao);
    }

    /**
     * 获取交通选项数据
     * 用于观察交通规划的结果
     *
     * @return 交通选项的LiveData对象
     */
    public LiveData<TransportOptions> getTransportOptions() {
        return transportOptions;
    }

    /**
     * 获取错误信息
     * 用于观察操作过程中发生的错误
     *
     * @return 错误信息的LiveData对象
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * 获取加载状态
     * 用于观察操作的加载状态
     *
     * @return 加载状态的LiveData对象
     */
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    /**
     * 根据ID获取行程日数据
     *
     * @param dayId 行程日ID
     * @return 行程日实体的LiveData对象
     */
    public LiveData<ItineraryDayEntity> getDayById(Long dayId) {
        return itineraryDayDao.getById(dayId);
    }

    /**
     * 根据ID获取行程数据
     *
     * @param itineraryId 行程ID
     * @return 行程实体的LiveData对象
     */
    public LiveData<ItineraryEntity> getItineraryById(Long itineraryId) {
        MutableLiveData<ItineraryEntity> liveData = new MutableLiveData<>();
        liveData.setValue(itineraryDao.getById(itineraryId));
        return liveData;
    }

    /**
     * 根据行程日ID获取交通规划列表
     *
     * @param dayId 行程日ID
     * @return 交通规划实体列表的LiveData对象
     */
    public LiveData<List<TransportPlanEntity>> getTransportPlansByDay(Long dayId) {
        return transportPlanDao.getByDayId(dayId);
    }

    /**
     * 生成每日交通规划
     * 调用TransportPlanService生成交通规划，并处理回调结果
     *
     * @param dayId 行程日ID
     * @param city 城市名称
     */
    public void generateTransportPlans(Long dayId, String city) {
        // 设置加载状态为true
        isLoading.setValue(true);
        // 清空错误信息
        errorMessage.setValue(null);

        // 调用服务生成交通规划
        transportPlanService.generateDayTransportPlans(dayId, city, new TransportPlanService.TransportPlanCallback() {
            @Override
            public void onSuccess(TransportOptions options) {
                // 生成成功，更新加载状态和交通选项数据
                isLoading.postValue(false);
                transportOptions.postValue(options);
            }

            @Override
            public void onError(String error) {
                // 生成失败，更新加载状态和错误信息
                isLoading.postValue(false);
                errorMessage.postValue(error);
            }
        });
    }

    /**
     * 清除错误信息
     * 用于重置错误状态
     */
    public void clearError() {
        errorMessage.setValue(null);
    }
}