package com.travelplanner.ui.attraction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.AttractionApi;
import com.travelplanner.data.remote.api.FavoriteApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Attraction;
import com.travelplanner.model.FavoriteRequest;
import com.travelplanner.model.amap.AMapPoiSearchResponse;
import com.travelplanner.model.amap.Photo;
import com.travelplanner.ui.hotel.HotelPhotoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AttractionDetailActivity extends AppCompatActivity {

    private static final String TAG = "AttractionDetailActivity";
    public static final String EXTRA_ATTRACTION_JSON = "extra_attraction_json";
    public static final String EXTRA_ATTRACTION_INDEX = "extra_attraction_index";
    public static final String EXTRA_ATTRACTION_ID = "attraction_id";

    private Toolbar toolbar;
    private ViewPager2 viewPagerPhotos;
    private TextView tvAttractionName;
    private TextView tvRating;
    private TextView tvPrice;
    private TextView tvAddress;
    private TextView tvOpeningHours;
    private TextView tvTicketInfo;
    private TextView tvBookingInfo;
    private TextView tvPlayDuration;
    private TextView tvFacilities;
    private Button btnBooking;
    private ImageView ivFavorite;

    private AttractionPhotoAdapter attractionPhotoAdapter;
    private HotelPhotoAdapter aMapPhotoAdapter;

    // 两种数据源：自有 Attraction 模型 或 高德 POI 模型
    private Attraction attractionData;
    private AMapPoiSearchResponse.Poi aMapPoiData;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();
    private String originalSinglePoiJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);

        setupToolbar();
        setupViews();
        setupViewPager();
        loadAttractionData();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViews() {
        viewPagerPhotos = findViewById(R.id.viewPagerPhotos);
        tvAttractionName = findViewById(R.id.tvAttractionName);
        tvRating = findViewById(R.id.tvRating);
        tvPrice = findViewById(R.id.tvPrice);
        tvAddress = findViewById(R.id.tvAddress);
        tvOpeningHours = findViewById(R.id.tvOpeningHours);
        tvTicketInfo = findViewById(R.id.tvTicketInfo);
        tvBookingInfo = findViewById(R.id.tvBookingInfo);
        tvPlayDuration = findViewById(R.id.tvPlayDuration);
        tvFacilities = findViewById(R.id.tvFacilities);
        btnBooking = findViewById(R.id.btnBooking);
        ivFavorite = findViewById(R.id.ivFavorite);

        btnBooking.setOnClickListener(v -> {
            Toast.makeText(this, "预约功能开发中", Toast.LENGTH_SHORT).show();
        });

        ivFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void setupViewPager() {
        attractionPhotoAdapter = new AttractionPhotoAdapter();
        aMapPhotoAdapter = new HotelPhotoAdapter();
        viewPagerPhotos.setAdapter(attractionPhotoAdapter);
    }

    private void loadAttractionData() {
        // 优先处理 attraction_id（来自首页热门景点）
        long attractionId = getIntent().getLongExtra(EXTRA_ATTRACTION_ID, -1);
        if (attractionId > 0) {
            Log.d(TAG, "从后端加载景点数据，id=" + attractionId);
            loadAttractionFromApi(attractionId);
            return;
        }

        // 兼容旧逻辑：高德POI JSON
        String fullJson = getIntent().getStringExtra(EXTRA_ATTRACTION_JSON);
        int index = getIntent().getIntExtra(EXTRA_ATTRACTION_INDEX, -1);
        String singlePoiJson = getIntent().getStringExtra("extra_single_poi_json");
        this.originalSinglePoiJson = singlePoiJson;

        Log.d(TAG, "接收到的完整JSON: " + fullJson);
        Log.d(TAG, "接收到的索引: " + index);
        Log.d(TAG, "接收到的单个POI JSON: " + singlePoiJson);

        try {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().create();

            if (singlePoiJson != null && !singlePoiJson.isEmpty()) {
                Log.d(TAG, "直接解析单个POI JSON");
                String poiJsonWithWrapper = "{\"pois\":[" + singlePoiJson + "]}";
                AMapPoiSearchResponse response = gson.fromJson(poiJsonWithWrapper, AMapPoiSearchResponse.class);
                if (response != null && response.getPois() != null && !response.getPois().isEmpty()) {
                    aMapPoiData = response.getPois().get(0);
                }
            } else if (fullJson != null && index >= 0) {
                Log.d(TAG, "从完整JSON中提取单个POI");
                AMapPoiSearchResponse response = gson.fromJson(fullJson, AMapPoiSearchResponse.class);
                if (response != null && response.getPois() != null) {
                    aMapPoiData = response.getPois().get(index);
                }
            } else {
                aMapPoiData = TravelPlannerApp.getInstance().getCurrentHotel();
            }

            if (aMapPoiData != null) {
                displayAMapPoiInfo();
                displayAMapPhotos();
                checkFavoriteState();
            }
        } catch (Exception e) {
            Log.e(TAG, "解析JSON失败", e);
            Toast.makeText(this, "解析景点信息失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadAttractionFromApi(long id) {
        AttractionApi api = ApiClient.getAttractionApi();
        api.getAttractionDetail(id).enqueue(new retrofit2.Callback<ApiResponse<Attraction>>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse<Attraction>> call, retrofit2.Response<ApiResponse<Attraction>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    attractionData = response.body().getData();
                    if (attractionData != null) {
                        handler.post(() -> {
                            displayAttractionInfo();
                            displayAttractionPhotos();
                            checkFavoriteState();
                        });
                    } else {
                        handler.post(() -> {
                            Toast.makeText(AttractionDetailActivity.this, "景点数据为空", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                } else {
                    handler.post(() -> {
                        Toast.makeText(AttractionDetailActivity.this, "获取景点信息失败", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse<Attraction>> call, Throwable t) {
                Log.e(TAG, "获取景点详情失败", t);
                handler.post(() -> {
                    Toast.makeText(AttractionDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    /**
     * 使用 Attraction 模型显示景点信息（来自后端API）
     */
    private void displayAttractionInfo() {
        tvAttractionName.setText(attractionData.getName() != null ? attractionData.getName() : "未知景点");

        String ratingText = null;
        if (attractionData.getRating() != null) {
            ratingText = attractionData.getRating() + "分";
        }
        tvRating.setText(ratingText != null ? ratingText : "暂无评分");

        String costText = null;
        if (attractionData.getTicketPrice() != null) {
            costText = String.format("门票：¥%.0f", attractionData.getTicketPrice());
        }
        tvPrice.setText(costText != null ? costText : "门票：免费");

        tvAddress.setText(attractionData.getAddress() != null ? attractionData.getAddress() : "地址信息未知");

        String openingHours = attractionData.getOpenTime();
        tvOpeningHours.setText(openingHours != null && !openingHours.isEmpty() ? openingHours : "开放时间：暂无信息");

        tvTicketInfo.setText("成人票：请咨询景区\n儿童票：请咨询景区\n老人票：请咨询景区");
        tvBookingInfo.setText("预约方式：请通过官方渠道预约\n预约平台：景区官网/微信公众号");

        String playDurationText = "建议游玩时长：";
        if (attractionData.getPlayDuration() != null && attractionData.getPlayDuration() > 0) {
            playDurationText += attractionData.getPlayDuration() + "小时";
        } else {
            playDurationText += "2-3小时";
        }
        playDurationText += "\n最佳游览路线：请咨询景区";
        tvPlayDuration.setText(playDurationText);

        String facilitiesText = attractionData.getFacilities();
        if (facilitiesText != null && !facilitiesText.isEmpty()) {
            tvFacilities.setText(facilitiesText);
        } else {
            tvFacilities.setText("卫生间：景区内设有多个卫生间\n母婴室：游客中心旁设有母婴室\n停车场：景区门口设有停车场\n便利店：景区内设有便利店");
        }
    }

    /**
     * 使用 Attraction 模型显示图片（来自后端API）
     */
    private void displayAttractionPhotos() {
        List<String> imageList = attractionData.getImageList();
        String imageUrl = attractionData.getImageUrl();

        Log.d(TAG, "displayAttractionPhotos: imageList=" + (imageList != null ? imageList.size() : "null")
                + ", imageUrl=" + imageUrl);

        // 切换到 AttractionPhotoAdapter
        viewPagerPhotos.setAdapter(attractionPhotoAdapter);

        if (imageList != null && !imageList.isEmpty()) {
            attractionPhotoAdapter.setData(imageList);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            // 只有一张图片
            List<String> singleList = new ArrayList<>();
            singleList.add(imageUrl);
            attractionPhotoAdapter.setData(singleList);
        } else {
            Log.d(TAG, "没有照片数据");
        }
    }

    /**
     * 使用高德POI模型显示景点信息（兼容旧逻辑）
     */
    private void displayAMapPoiInfo() {
        tvAttractionName.setText(aMapPoiData.getName() != null ? aMapPoiData.getName() : "未知景点");

        String ratingText = null;
        String costText = null;

        if (aMapPoiData.getRating() != null && !aMapPoiData.getRating().isEmpty()) {
            ratingText = aMapPoiData.getRating() + "分";
        } else if (aMapPoiData.getBiz_ext() != null && aMapPoiData.getBiz_ext().getRating() != null
                && !aMapPoiData.getBiz_ext().getRating().isEmpty()) {
            ratingText = aMapPoiData.getBiz_ext().getRating() + "分";
        }

        if (aMapPoiData.getCost() != null && !aMapPoiData.getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(aMapPoiData.getCost());
                costText = String.format("门票：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "门票：" + aMapPoiData.getCost();
            }
        } else if (aMapPoiData.getBiz_ext() != null && aMapPoiData.getBiz_ext().getCost() != null
                && !aMapPoiData.getBiz_ext().getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(aMapPoiData.getBiz_ext().getCost());
                costText = String.format("门票：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "门票：" + aMapPoiData.getBiz_ext().getCost();
            }
        }

        tvRating.setText(ratingText != null ? ratingText : "暂无评分");
        tvPrice.setText(costText != null ? costText : "门票：免费");

        tvAddress.setText(aMapPoiData.getAddress() != null ? aMapPoiData.getAddress() : "地址信息未知");

        String openingHours = null;
        if (aMapPoiData.getBiz_ext() != null) {
            if (aMapPoiData.getBiz_ext().getOpen_time() != null && !aMapPoiData.getBiz_ext().getOpen_time().isEmpty()) {
                openingHours = aMapPoiData.getBiz_ext().getOpen_time();
            } else if (aMapPoiData.getBiz_ext().getOpentime2() != null
                    && !aMapPoiData.getBiz_ext().getOpentime2().isEmpty()) {
                openingHours = aMapPoiData.getBiz_ext().getOpentime2();
            }
        }
        tvOpeningHours.setText(openingHours != null ? openingHours : "开放时间：暂无信息");

        tvTicketInfo.setText("成人票：请咨询景区\n儿童票：请咨询景区\n老人票：请咨询景区");
        tvBookingInfo.setText("预约方式：请通过官方渠道预约\n预约平台：景区官网/微信公众号");
        tvPlayDuration.setText("建议游玩时长：2-3小时\n最佳游览路线：请咨询景区");
        tvFacilities.setText("卫生间：景区内设有多个卫生间\n母婴室：游客中心旁设有母婴室\n停车场：景区门口设有停车场\n便利店：景区内设有便利店");
    }

    /**
     * 使用高德POI模型显示图片（兼容旧逻辑）
     */
    private void displayAMapPhotos() {
        List<Photo> photos = aMapPoiData.getPhotos();
        Log.d(TAG, "displayAMapPhotos: photos = " + (photos != null ? photos.size() : "null"));

        // 切换到 HotelPhotoAdapter（高德Photo列表）
        viewPagerPhotos.setAdapter(aMapPhotoAdapter);

        if (photos != null && !photos.isEmpty()) {
            aMapPhotoAdapter.setData(photos);
        } else {
            Log.d(TAG, "没有照片数据");
        }
    }

    private void checkFavoriteState() {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            updateFavoriteIcon(false);
            return;
        }

        String poiId = getCurrentPoiId();
        if (poiId == null || poiId.isEmpty()) {
            updateFavoriteIcon(false);
            return;
        }

        FavoriteApi api = ApiClient.getFavoriteApi();
        api.checkFavorite(userId, poiId, "attraction").enqueue(new retrofit2.Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse<Boolean>> call, retrofit2.Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Boolean isFav = response.body().getData();
                    handler.post(() -> updateFavoriteIcon(isFav != null && isFav));
                } else {
                    handler.post(() -> updateFavoriteIcon(false));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse<Boolean>> call, Throwable t) {
                Log.e(TAG, "检查收藏状态失败", t);
                handler.post(() -> updateFavoriteIcon(false));
            }
        });
    }

    private void toggleFavorite() {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        String poiId = getCurrentPoiId();
        if (poiId == null || poiId.isEmpty()) {
            Toast.makeText(this, "无法操作收藏", Toast.LENGTH_SHORT).show();
            return;
        }

        FavoriteApi api = ApiClient.getFavoriteApi();
        api.checkFavorite(userId, poiId, "attraction").enqueue(new retrofit2.Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse<Boolean>> call, retrofit2.Response<ApiResponse<Boolean>> response) {
                boolean currentlyFavorite = response.isSuccessful() && response.body() != null
                        && response.body().isSuccess() && Boolean.TRUE.equals(response.body().getData());

                if (currentlyFavorite) {
                    api.removeFavorite(userId, poiId, "attraction").enqueue(new retrofit2.Callback<ApiResponse<Void>>() {
                        @Override
                        public void onResponse(retrofit2.Call<ApiResponse<Void>> call, retrofit2.Response<ApiResponse<Void>> response) {
                            handler.post(() -> {
                                updateFavoriteIcon(false);
                                Toast.makeText(AttractionDetailActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ApiResponse<Void>> call, Throwable t) {
                            handler.post(() -> {
                                Toast.makeText(AttractionDetailActivity.this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                } else {
                    FavoriteRequest request = new FavoriteRequest();
                    request.setUserId(userId);
                    request.setPoiId(poiId);
                    request.setType("attraction");

                    if (attractionData != null) {
                        request.setName(attractionData.getName());
                        request.setAddress(attractionData.getAddress());
                        if (attractionData.getImageUrl() != null && !attractionData.getImageUrl().isEmpty()) {
                            request.setImageUrl(attractionData.getImageUrl());
                        }
                        if (attractionData.getRating() != null) {
                            request.setRating(String.valueOf(attractionData.getRating()));
                        }
                        if (attractionData.getTicketPrice() != null) {
                            request.setPrice(String.valueOf(attractionData.getTicketPrice()));
                        }
                        request.setPhone(attractionData.getPhone());
                        if (attractionData.getLatitude() != null && attractionData.getLongitude() != null) {
                            request.setLocation(attractionData.getLongitude() + "," + attractionData.getLatitude());
                        }
                    } else if (aMapPoiData != null) {
                        request.setName(aMapPoiData.getName());
                        request.setAddress(aMapPoiData.getAddress());
                        if (aMapPoiData.getPhotos() != null && !aMapPoiData.getPhotos().isEmpty()) {
                            request.setImageUrl(aMapPoiData.getPhotos().get(0).getUrl());
                        }
                        String ratingValue = null;
                        if (aMapPoiData.getRating() != null && !aMapPoiData.getRating().isEmpty()) {
                            ratingValue = aMapPoiData.getRating();
                        } else if (aMapPoiData.getBiz_ext() != null && aMapPoiData.getBiz_ext().getRating() != null
                                && !aMapPoiData.getBiz_ext().getRating().isEmpty()) {
                            ratingValue = aMapPoiData.getBiz_ext().getRating();
                        }
                        request.setRating(ratingValue);
                        String costValue = null;
                        if (aMapPoiData.getCost() != null && !aMapPoiData.getCost().isEmpty()) {
                            costValue = aMapPoiData.getCost();
                        } else if (aMapPoiData.getBiz_ext() != null && aMapPoiData.getBiz_ext().getCost() != null
                                && !aMapPoiData.getBiz_ext().getCost().isEmpty()) {
                            costValue = aMapPoiData.getBiz_ext().getCost();
                        }
                        request.setPrice(costValue);
                        request.setPhone(aMapPoiData.getFirstTel());
                        request.setLocation(aMapPoiData.getLocation());

                        String poiJsonToSave = null;
                        if (originalSinglePoiJson != null && !originalSinglePoiJson.isEmpty()) {
                            poiJsonToSave = originalSinglePoiJson;
                        } else {
                            String fullJsonFromIntent = getIntent().getStringExtra(EXTRA_ATTRACTION_JSON);
                            int idxFromIntent = getIntent().getIntExtra(EXTRA_ATTRACTION_INDEX, -1);
                            if (fullJsonFromIntent != null && idxFromIntent >= 0) {
                                com.google.gson.JsonObject fullObj = com.google.gson.JsonParser
                                        .parseString(fullJsonFromIntent).getAsJsonObject();
                                com.google.gson.JsonArray poisArray = fullObj.getAsJsonArray("pois");
                                if (poisArray != null && idxFromIntent < poisArray.size()) {
                                    poiJsonToSave = poisArray.get(idxFromIntent).toString();
                                }
                            }
                        }
                        request.setPoiJson(poiJsonToSave);
                    }

                    api.addFavorite(request).enqueue(new retrofit2.Callback<ApiResponse<com.travelplanner.model.Favorite>>() {
                        @Override
                        public void onResponse(retrofit2.Call<ApiResponse<com.travelplanner.model.Favorite>> call,
                                               retrofit2.Response<ApiResponse<com.travelplanner.model.Favorite>> response) {
                            handler.post(() -> {
                                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                    updateFavoriteIcon(true);
                                    Toast.makeText(AttractionDetailActivity.this, "已添加到收藏", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AttractionDetailActivity.this, "添加收藏失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ApiResponse<com.travelplanner.model.Favorite>> call, Throwable t) {
                            handler.post(() -> {
                                Toast.makeText(AttractionDetailActivity.this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse<Boolean>> call, Throwable t) {
                handler.post(() -> {
                    Toast.makeText(AttractionDetailActivity.this, "检查收藏状态失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * 获取当前景点的POI ID（兼容两种数据源）
     */
    private String getCurrentPoiId() {
        if (attractionData != null) {
            return attractionData.getId() != null ? String.valueOf(attractionData.getId()) : null;
        } else if (aMapPoiData != null) {
            return aMapPoiData.getId();
        }
        return null;
    }

    private void updateFavoriteIcon(boolean isFavorite) {
        if (isFavorite) {
            ivFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            ivFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }
}
