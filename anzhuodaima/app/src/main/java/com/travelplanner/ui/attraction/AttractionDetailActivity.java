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
import com.travelplanner.data.remote.api.FavoriteApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.FavoriteRequest;
import com.travelplanner.model.amap.AMapPoiSearchResponse;
import com.travelplanner.model.amap.Photo;
import com.travelplanner.ui.hotel.HotelPhotoAdapter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AttractionDetailActivity extends AppCompatActivity {

    private static final String TAG = "AttractionDetailActivity";
    public static final String EXTRA_ATTRACTION_JSON = "extra_attraction_json";
    public static final String EXTRA_ATTRACTION_INDEX = "extra_attraction_index";

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

    private HotelPhotoAdapter photoAdapter;
    private AMapPoiSearchResponse.Poi attraction;

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
        photoAdapter = new HotelPhotoAdapter();
        viewPagerPhotos.setAdapter(photoAdapter);
    }

    private void loadAttractionData() {
        String fullJson = getIntent().getStringExtra(EXTRA_ATTRACTION_JSON);
        int index = getIntent().getIntExtra(EXTRA_ATTRACTION_INDEX, -1);
        String singlePoiJson = getIntent().getStringExtra("extra_single_poi_json");
        this.originalSinglePoiJson = singlePoiJson;

        Log.d(TAG, "接收到的完整JSON: " + fullJson);
        Log.d(TAG, "接收到的索引: " + index);
        Log.d(TAG, "接收到的单个POI JSON: " + singlePoiJson);

        try {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                    .create();

            if (singlePoiJson != null && !singlePoiJson.isEmpty()) {
                Log.d(TAG, "直接解析单个POI JSON");
                String poiJsonWithWrapper = "{\"pois\":[" + singlePoiJson + "]}";
                AMapPoiSearchResponse response = gson.fromJson(poiJsonWithWrapper, AMapPoiSearchResponse.class);
                if (response != null && response.getPois() != null && !response.getPois().isEmpty()) {
                    attraction = response.getPois().get(0);
                }
            } else if (fullJson != null && index >= 0) {
                Log.d(TAG, "从完整JSON中提取单个POI");
                AMapPoiSearchResponse response = gson.fromJson(fullJson, AMapPoiSearchResponse.class);
                if (response != null && response.getPois() != null) {
                    attraction = response.getPois().get(index);
                }
            } else {
                attraction = TravelPlannerApp.getInstance().getCurrentHotel();
            }

            if (attraction != null) {
                Log.d(TAG, "解析后的景点对象: " + attraction);
                Log.d(TAG, "景点名称: " + attraction.getName());
                if (attraction.getPhotos() != null) {
                    Log.d(TAG, "Photos数量: " + attraction.getPhotos().size());
                    for (int i = 0; i < attraction.getPhotos().size(); i++) {
                        Photo photo = attraction.getPhotos().get(i);
                        Log.d(TAG, "Photo " + i + " - url: " + photo.getUrl());
                    }
                }
                displayAttractionInfo();
                displayPhotos();
                checkFavoriteState();
            }
        } catch (Exception e) {
            Log.e(TAG, "解析JSON失败", e);
            Toast.makeText(this, "解析景点信息失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayAttractionInfo() {
        tvAttractionName.setText(attraction.getName() != null ? attraction.getName() : "未知景点");

        String ratingText = null;
        String costText = null;

        if (attraction.getRating() != null && !attraction.getRating().isEmpty()) {
            ratingText = attraction.getRating() + "分";
        } else if (attraction.getBiz_ext() != null && attraction.getBiz_ext().getRating() != null
                && !attraction.getBiz_ext().getRating().isEmpty()) {
            ratingText = attraction.getBiz_ext().getRating() + "分";
        }

        if (attraction.getCost() != null && !attraction.getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(attraction.getCost());
                costText = String.format("门票：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "门票：" + attraction.getCost();
            }
        } else if (attraction.getBiz_ext() != null && attraction.getBiz_ext().getCost() != null
                && !attraction.getBiz_ext().getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(attraction.getBiz_ext().getCost());
                costText = String.format("门票：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "门票：" + attraction.getBiz_ext().getCost();
            }
        }

        tvRating.setText(ratingText != null ? ratingText : "暂无评分");
        tvPrice.setText(costText != null ? costText : "门票：免费");

        tvAddress.setText(attraction.getAddress() != null ? attraction.getAddress() : "地址信息未知");

        String openingHours = null;
        if (attraction.getBiz_ext() != null) {
            if (attraction.getBiz_ext().getOpen_time() != null && !attraction.getBiz_ext().getOpen_time().isEmpty()) {
                openingHours = attraction.getBiz_ext().getOpen_time();
            } else if (attraction.getBiz_ext().getOpentime2() != null
                    && !attraction.getBiz_ext().getOpentime2().isEmpty()) {
                openingHours = attraction.getBiz_ext().getOpentime2();
            }
        }
        tvOpeningHours.setText(openingHours != null ? openingHours : "开放时间：暂无信息");

        tvTicketInfo.setText("成人票：请咨询景区\n儿童票：请咨询景区\n老人票：请咨询景区");
        tvBookingInfo.setText("预约方式：请通过官方渠道预约\n预约平台：景区官网/微信公众号");
        tvPlayDuration.setText("建议游玩时长：2-3小时\n最佳游览路线：请咨询景区");
        tvFacilities.setText("卫生间：景区内设有多个卫生间\n母婴室：游客中心旁设有母婴室\n停车场：景区门口设有停车场\n便利店：景区内设有便利店");
    }

    private void displayPhotos() {
        List<Photo> photos = attraction.getPhotos();
        Log.d(TAG, "displayPhotos: photos = " + (photos != null ? photos.size() : "null"));
        if (photos != null && !photos.isEmpty()) {
            for (int i = 0; i < photos.size(); i++) {
                Log.d(TAG, "Photo " + i + ": title=" + photos.get(i).getTitle() + ", url=" + photos.get(i).getUrl());
            }
            photoAdapter.setData(photos);
        } else {
            Log.d(TAG, "没有照片数据");
        }
    }

    private void checkFavoriteState() {
        if (attraction == null)
            return;
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            updateFavoriteIcon(false);
            return;
        }
        final String poiId = attraction.getId();
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
        if (attraction == null)
            return;
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final String poiId = attraction.getId();

        // 先检查当前收藏状态
        FavoriteApi api = ApiClient.getFavoriteApi();
        api.checkFavorite(userId, poiId, "attraction").enqueue(new retrofit2.Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse<Boolean>> call, retrofit2.Response<ApiResponse<Boolean>> response) {
                boolean currentlyFavorite = response.isSuccessful() && response.body() != null
                        && response.body().isSuccess() && Boolean.TRUE.equals(response.body().getData());

                if (currentlyFavorite) {
                    // 取消收藏
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
                    // 添加收藏
                    FavoriteRequest request = new FavoriteRequest();
                    request.setUserId(userId);
                    request.setPoiId(poiId);
                    request.setType("attraction");
                    request.setName(attraction.getName());
                    request.setAddress(attraction.getAddress());

                    if (attraction.getPhotos() != null && !attraction.getPhotos().isEmpty()) {
                        Photo photo = attraction.getPhotos().get(0);
                        request.setImageUrl(photo.getUrl());
                    }

                    String ratingValue = null;
                    if (attraction.getRating() != null && !attraction.getRating().isEmpty()) {
                        ratingValue = attraction.getRating();
                    } else if (attraction.getBiz_ext() != null && attraction.getBiz_ext().getRating() != null
                            && !attraction.getBiz_ext().getRating().isEmpty()) {
                        ratingValue = attraction.getBiz_ext().getRating();
                    }
                    request.setRating(ratingValue);

                    String costValue = null;
                    if (attraction.getCost() != null && !attraction.getCost().isEmpty()) {
                        costValue = attraction.getCost();
                    } else if (attraction.getBiz_ext() != null && attraction.getBiz_ext().getCost() != null
                            && !attraction.getBiz_ext().getCost().isEmpty()) {
                        costValue = attraction.getBiz_ext().getCost();
                    }
                    request.setPrice(costValue);

                    request.setPhone(attraction.getFirstTel());
                    request.setLocation(attraction.getLocation());

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

    private void updateFavoriteIcon(boolean isFavorite) {
        if (isFavorite) {
            ivFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            ivFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }
}
