package com.travelplanner.ui.restaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.travelplanner.R;
import com.travelplanner.model.amap.AMapPoiSearchResponse;
import com.travelplanner.model.amap.Photo;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantDetail";
    public static final String EXTRA_RESTAURANT = "extra_restaurant";
    public static final String EXTRA_RESTAURANT_JSON = "extra_restaurant_json";
    public static final String EXTRA_RESTAURANT_INDEX = "extra_restaurant_index";

    private Toolbar toolbar;
    private ViewPager2 viewPagerPhotos;
    private LinearLayout llPhotoIndicator;
    private TextView tvRestaurantName;
    private TextView tvRating;
    private TextView tvAvgPrice;
    private TextView tvCuisineType;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvBusinessHours;
    private TextView tvDistance;
    private LinearLayout llDistance;
    private LinearLayout llQueueInfo;
    private TextView tvQueueStatus;
    private TextView tvWaitTime;
    private Button btnNavigate;
    private ImageView ivFavorite;

    private PhotoPagerAdapter photoPagerAdapter;
    private List<View> indicatorDots = new ArrayList<>();

    private Handler handler = new Handler(Looper.getMainLooper());
    private java.util.concurrent.Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();

    private AMapPoiSearchResponse.Poi restaurant;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        initViews();
        setupToolbar();
        loadRestaurantData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        viewPagerPhotos = findViewById(R.id.viewPagerPhotos);
        llPhotoIndicator = findViewById(R.id.llPhotoIndicator);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvRating = findViewById(R.id.tvRating);
        tvAvgPrice = findViewById(R.id.tvAvgPrice);
        tvCuisineType = findViewById(R.id.tvCuisineType);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvBusinessHours = findViewById(R.id.tvBusinessHours);
        tvDistance = findViewById(R.id.tvDistance);
        llDistance = findViewById(R.id.llDistance);
        llQueueInfo = findViewById(R.id.llQueueInfo);
        tvQueueStatus = findViewById(R.id.tvQueueStatus);
        tvWaitTime = findViewById(R.id.tvWaitTime);
        btnNavigate = findViewById(R.id.btnNavigate);
        ivFavorite = findViewById(R.id.ivFavorite);

        photoPagerAdapter = new PhotoPagerAdapter(this);
        viewPagerPhotos.setAdapter(photoPagerAdapter);

        viewPagerPhotos.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updatePhotoIndicator(position);
            }
        });

        tvPhone.setOnClickListener(v -> {
            if (restaurant.getFirstTel() != null && !restaurant.getFirstTel().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + restaurant.getFirstTel()));
                startActivity(intent);
            }
        });

        ivFavorite.setOnClickListener(v -> toggleFavorite());

        btnNavigate.setOnClickListener(v -> navigateToRestaurant());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private String originalSinglePoiJson;

    private void loadRestaurantData() {
        String fullJson = getIntent().getStringExtra(EXTRA_RESTAURANT_JSON);
        int index = getIntent().getIntExtra(EXTRA_RESTAURANT_INDEX, -1);
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
                com.google.gson.JsonElement jsonElement = com.google.gson.JsonParser.parseString(singlePoiJson);
                String poiJsonWithWrapper = "{\"pois\":[" + singlePoiJson + "]}";
                Log.d(TAG, "包装后的JSON: " + poiJsonWithWrapper);

                AMapPoiSearchResponse response = gson.fromJson(poiJsonWithWrapper, AMapPoiSearchResponse.class);
                if (response != null && response.getPois() != null && !response.getPois().isEmpty()) {
                    restaurant = response.getPois().get(0);
                }
            } else if (fullJson != null && index >= 0) {
                Log.d(TAG, "从完整JSON中提取单个POI");
                com.google.gson.JsonObject fullObj = com.google.gson.JsonParser.parseString(fullJson).getAsJsonObject();
                com.google.gson.JsonArray poisArray = fullObj.getAsJsonArray("pois");

                if (poisArray != null && index < poisArray.size()) {
                    com.google.gson.JsonElement poiElement = poisArray.get(index);
                    String poiJson = poiElement.toString();
                    Log.d(TAG, "提取的单个POI JSON: " + poiJson);
                    AMapPoiSearchResponse response = gson.fromJson(fullJson, AMapPoiSearchResponse.class);
                    if (response != null && response.getPois() != null) {
                        restaurant = response.getPois().get(index);
                    }
                }
            }

            if (restaurant != null) {
                Log.d(TAG, "解析后的餐厅对象: " + restaurant);
                Log.d(TAG, "餐厅名称: " + restaurant.getName());
                if (restaurant.getPhotos() != null) {
                    Log.d(TAG, "Photos数量: " + restaurant.getPhotos().size());
                    for (int i = 0; i < restaurant.getPhotos().size(); i++) {
                        com.travelplanner.model.amap.Photo photo = restaurant.getPhotos().get(i);
                        Log.d(TAG, "Photo " + i + " - url: " + photo.getUrl());
                    }
                }
                displayRestaurantInfo();
            } else {
                Toast.makeText(this, "餐厅数据无效", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "解析JSON失败", e);
            Toast.makeText(this, "解析餐厅信息失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayRestaurantInfo() {
        Log.d(TAG, "开始显示餐厅信息");
        tvRestaurantName.setText(restaurant.getName());
        checkFavoriteState();

        if (restaurant.getLocation() != null && restaurant.getLocation().contains(",")) {
            String[] parts = restaurant.getLocation().split(",");
            longitude = Double.parseDouble(parts[0]);
            latitude = Double.parseDouble(parts[1]);
            Log.d(TAG, "经纬度: " + latitude + ", " + longitude);
        }

        Log.d(TAG, "Photos是否为空: " + (restaurant.getPhotos() == null));
        if (restaurant.getPhotos() != null) {
            Log.d(TAG, "Photos数量: " + restaurant.getPhotos().size());
            if (!restaurant.getPhotos().isEmpty()) {
                for (int i = 0; i < restaurant.getPhotos().size(); i++) {
                    Photo photo = restaurant.getPhotos().get(i);
                    Log.d(TAG, "Photo " + i + " - title: " + photo.getTitle() + ", url: " + photo.getUrl());
                }
            }
        }

        if (restaurant.getPhotos() != null && !restaurant.getPhotos().isEmpty()) {
            photoPagerAdapter.setPhotos(restaurant.getPhotos());
            setupPhotoIndicators(restaurant.getPhotos().size());
        } else {
            Log.d(TAG, "使用占位图片");
            List<Photo> placeholderPhotos = new ArrayList<>();
            Photo placeholder = new Photo();
            placeholder.setTitle("餐厅图片");
            placeholder.setUrl("");
            placeholderPhotos.add(placeholder);
            photoPagerAdapter.setPhotos(placeholderPhotos);
        }

        Log.d(TAG, "Poi.rating: " + restaurant.getRating());
        Log.d(TAG, "Poi.cost: " + restaurant.getCost());
        Log.d(TAG, "Biz_ext: " + (restaurant.getBiz_ext() != null ? "存在" : "null"));

        String ratingValue = null;
        if (restaurant.getRating() != null && !restaurant.getRating().isEmpty()) {
            ratingValue = restaurant.getRating();
        } else if (restaurant.getBiz_ext() != null && restaurant.getBiz_ext().getRating() != null
                && !restaurant.getBiz_ext().getRating().isEmpty()) {
            ratingValue = restaurant.getBiz_ext().getRating();
        }
        if (ratingValue != null) {
            tvRating.setText(ratingValue + "分");
        } else {
            tvRating.setText("暂无评分");
        }
        Log.d(TAG, "最终显示评分: " + tvRating.getText());

        String costValue = null;
        if (restaurant.getCost() != null && !restaurant.getCost().isEmpty()) {
            costValue = restaurant.getCost();
        } else if (restaurant.getBiz_ext() != null && restaurant.getBiz_ext().getCost() != null
                && !restaurant.getBiz_ext().getCost().isEmpty()) {
            costValue = restaurant.getBiz_ext().getCost();
        }
        if (costValue != null) {
            try {
                double cost = Double.parseDouble(costValue);
                tvAvgPrice.setText(String.format("人均：¥%.0f", cost));
            } catch (NumberFormatException e) {
                tvAvgPrice.setText("人均：" + costValue);
            }
        } else {
            tvAvgPrice.setText("人均：¥0");
        }
        Log.d(TAG, "最终显示人均: " + tvAvgPrice.getText());

        String businessHoursValue = null;
        if (restaurant.getBiz_ext() != null) {
            if (restaurant.getBiz_ext().getOpentime2() != null && !restaurant.getBiz_ext().getOpentime2().isEmpty()) {
                businessHoursValue = restaurant.getBiz_ext().getOpentime2();
            } else if (restaurant.getBiz_ext().getOpen_time() != null
                    && !restaurant.getBiz_ext().getOpen_time().isEmpty()) {
                businessHoursValue = restaurant.getBiz_ext().getOpen_time();
            }
        }
        if (businessHoursValue != null) {
            tvBusinessHours.setText(businessHoursValue);
        } else {
            tvBusinessHours.setText("营业时间暂无");
        }
        Log.d(TAG, "最终显示营业时间: " + tvBusinessHours.getText());

        if (restaurant.getType() != null && !restaurant.getType().isEmpty()) {
            tvCuisineType.setText(restaurant.getType());
        } else {
            tvCuisineType.setText("菜系类型未知");
        }

        if (restaurant.getAddress() != null && !restaurant.getAddress().isEmpty()) {
            tvAddress.setText(restaurant.getAddress());
        } else {
            tvAddress.setText("地址信息未知");
        }

        if (restaurant.getFirstTel() != null && !restaurant.getFirstTel().isEmpty()) {
            tvPhone.setText("📞 " + restaurant.getFirstTel());
        } else {
            tvPhone.setText("电话暂无");
        }

        if (restaurant.getDistance() != null && !restaurant.getDistance().isEmpty()) {
            try {
                int distance = Integer.parseInt(restaurant.getDistance());
                if (distance < 1000) {
                    tvDistance.setText(distance + "米");
                } else {
                    tvDistance.setText(String.format("%.1f公里", distance / 1000.0));
                }
                llDistance.setVisibility(View.VISIBLE);
            } catch (NumberFormatException e) {
                tvDistance.setText(restaurant.getDistance());
                llDistance.setVisibility(View.VISIBLE);
            }
        } else {
            llDistance.setVisibility(View.GONE);
        }
    }

    private void setupPhotoIndicators(int count) {
        llPhotoIndicator.removeAllViews();
        indicatorDots.clear();

        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            params.setMargins(4, 0, 4, 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.indicator_dot_selector);
            dot.setSelected(i == 0);
            llPhotoIndicator.addView(dot);
            indicatorDots.add(dot);
        }

        if (count <= 1) {
            llPhotoIndicator.setVisibility(View.GONE);
        }
    }

    private void updatePhotoIndicator(int position) {
        for (int i = 0; i < indicatorDots.size(); i++) {
            indicatorDots.get(i).setSelected(i == position);
        }
    }

    private void navigateToRestaurant() {
        if (latitude != 0 && longitude != 0) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("androidamap://navi?sourceApplication=travelplanner&poiname=" +
                        restaurant.getName() + "&lat=" + latitude + "&lon=" + longitude + "&dev=1"));
                intent.setPackage("com.autonavi.minimap");
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://uri.amap.com/navigation?to=" +
                            longitude + "," + latitude + "," + restaurant.getName() +
                            "&mode=car&coordinate=gaode"));
                    startActivity(intent);
                } catch (Exception e2) {
                    Toast.makeText(this, "请先安装高德地图", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "位置信息无效", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkFavoriteState() {
        if (restaurant == null)
            return;
        final String poiId = restaurant.getId();
        executor.execute(() -> {
            try {
                com.travelplanner.data.local.AppDatabase db = com.travelplanner.data.local.AppDatabase
                        .getInstance(this);
                boolean isFav = db.favoriteDao().isFavorite(poiId, "restaurant");
                handler.post(() -> {
                    updateFavoriteIcon(isFav);
                });
            } catch (Exception e) {
                Log.e(TAG, "检查收藏状态失败", e);
            }
        });
    }

    private void toggleFavorite() {
        if (restaurant == null)
            return;
        final String poiId = restaurant.getId();
        executor.execute(() -> {
            try {
                com.travelplanner.data.local.AppDatabase db = com.travelplanner.data.local.AppDatabase
                        .getInstance(this);
                boolean isFav = db.favoriteDao().isFavorite(poiId, "restaurant");

                if (isFav) {
                    db.favoriteDao().deleteByPoiId(poiId, "restaurant");
                    handler.post(() -> {
                        updateFavoriteIcon(false);
                        Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    com.travelplanner.data.local.entity.FavoriteEntity favorite = new com.travelplanner.data.local.entity.FavoriteEntity();
                    favorite.setPoiId(poiId);
                    favorite.setType("restaurant");
                    favorite.setName(restaurant.getName());
                    favorite.setAddress(restaurant.getAddress());

                    if (restaurant.getPhotos() != null && !restaurant.getPhotos().isEmpty()) {
                        com.travelplanner.model.amap.Photo photo = restaurant.getPhotos().get(0);
                        favorite.setImageUrl(photo.getUrl());
                    }

                    String ratingValue = null;
                    if (restaurant.getRating() != null && !restaurant.getRating().isEmpty()) {
                        ratingValue = restaurant.getRating();
                    } else if (restaurant.getBiz_ext() != null && restaurant.getBiz_ext().getRating() != null
                            && !restaurant.getBiz_ext().getRating().isEmpty()) {
                        ratingValue = restaurant.getBiz_ext().getRating();
                    }
                    favorite.setRating(ratingValue);

                    String costValue = null;
                    if (restaurant.getCost() != null && !restaurant.getCost().isEmpty()) {
                        costValue = restaurant.getCost();
                    } else if (restaurant.getBiz_ext() != null && restaurant.getBiz_ext().getCost() != null
                            && !restaurant.getBiz_ext().getCost().isEmpty()) {
                        costValue = restaurant.getBiz_ext().getCost();
                    }
                    favorite.setPrice(costValue);

                    favorite.setPhone(restaurant.getFirstTel());
                    favorite.setLocation(restaurant.getLocation());
                    favorite.setCreatedAt(System.currentTimeMillis());

                    String poiJsonToSave;
                    if (originalSinglePoiJson != null && !originalSinglePoiJson.isEmpty()) {
                        poiJsonToSave = originalSinglePoiJson;
                        Log.d(TAG, "使用原始单个POI JSON保存");
                    } else {
                        String fullJsonFromIntent = getIntent().getStringExtra(EXTRA_RESTAURANT_JSON);
                        int idxFromIntent = getIntent().getIntExtra(EXTRA_RESTAURANT_INDEX, -1);
                        if (fullJsonFromIntent != null && idxFromIntent >= 0) {
                            com.google.gson.JsonObject fullObj = com.google.gson.JsonParser
                                    .parseString(fullJsonFromIntent).getAsJsonObject();
                            com.google.gson.JsonArray poisArray = fullObj.getAsJsonArray("pois");
                            if (poisArray != null && idxFromIntent < poisArray.size()) {
                                poiJsonToSave = poisArray.get(idxFromIntent).toString();
                                Log.d(TAG, "从完整JSON中提取单个POI JSON保存");
                            } else {
                                poiJsonToSave = null;
                            }
                        } else {
                            poiJsonToSave = null;
                        }
                    }

                    if (poiJsonToSave != null) {
                        Log.d(TAG, "保存的POI JSON: " + poiJsonToSave);
                        favorite.setPoiJson(poiJsonToSave);
                    }

                    db.favoriteDao().insert(favorite);
                    handler.post(() -> {
                        updateFavoriteIcon(true);
                        Toast.makeText(this, "已添加到收藏", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "切换收藏状态失败", e);
                handler.post(() -> {
                    Toast.makeText(this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
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
