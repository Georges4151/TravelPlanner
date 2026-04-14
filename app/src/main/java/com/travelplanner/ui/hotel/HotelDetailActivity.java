package com.travelplanner.ui.hotel;

import android.content.Intent;
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
import com.travelplanner.model.amap.AMapPoiSearchResponse;
import com.travelplanner.model.amap.Photo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HotelDetailActivity extends AppCompatActivity {

    private static final String TAG = "HotelDetailActivity";
    public static final String EXTRA_HOTEL_JSON = "extra_hotel_json";
    public static final String EXTRA_HOTEL_INDEX = "extra_hotel_index";

    private Toolbar toolbar;
    private ViewPager2 viewPagerPhotos;
    private TextView tvHotelName;
    private TextView tvRating;
    private TextView tvPrice;
    private TextView tvAddress;
    private TextView tvPhone;
    private Button btnBook;
    private ImageView ivFavorite;

    private HotelPhotoAdapter photoAdapter;
    private AMapPoiSearchResponse.Poi hotel;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();
    private String originalSinglePoiJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        setupToolbar();
        setupViews();
        setupViewPager();
        loadHotelData();
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
        tvHotelName = findViewById(R.id.tvHotelName);
        tvRating = findViewById(R.id.tvRating);
        tvPrice = findViewById(R.id.tvPrice);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        btnBook = findViewById(R.id.btnBook);
        ivFavorite = findViewById(R.id.ivFavorite);

        btnBook.setOnClickListener(v -> {
            Toast.makeText(this, "预订功能开发中", Toast.LENGTH_SHORT).show();
        });

        ivFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void setupViewPager() {
        photoAdapter = new HotelPhotoAdapter();
        viewPagerPhotos.setAdapter(photoAdapter);
    }

    private void loadHotelData() {
        String fullJson = getIntent().getStringExtra(EXTRA_HOTEL_JSON);
        int index = getIntent().getIntExtra(EXTRA_HOTEL_INDEX, -1);
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
                    hotel = response.getPois().get(0);
                }
            } else if (fullJson != null && index >= 0) {
                Log.d(TAG, "从完整JSON中提取单个POI");
                AMapPoiSearchResponse response = gson.fromJson(fullJson, AMapPoiSearchResponse.class);
                if (response != null && response.getPois() != null) {
                    hotel = response.getPois().get(index);
                }
            } else {
                hotel = TravelPlannerApp.getInstance().getCurrentHotel();
            }

            if (hotel != null) {
                Log.d(TAG, "解析后的酒店对象: " + hotel);
                Log.d(TAG, "酒店名称: " + hotel.getName());
                if (hotel.getPhotos() != null) {
                    Log.d(TAG, "Photos数量: " + hotel.getPhotos().size());
                    for (int i = 0; i < hotel.getPhotos().size(); i++) {
                        Photo photo = hotel.getPhotos().get(i);
                        Log.d(TAG, "Photo " + i + " - url: " + photo.getUrl());
                    }
                }
                displayHotelInfo();
                displayPhotos();
                checkFavoriteState();
            }
        } catch (Exception e) {
            Log.e(TAG, "解析JSON失败", e);
            Toast.makeText(this, "解析酒店信息失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayHotelInfo() {
        tvHotelName.setText(hotel.getName() != null ? hotel.getName() : "未知酒店");

        String ratingText = null;
        String costText = null;

        if (hotel.getRating() != null && !hotel.getRating().isEmpty()) {
            ratingText = hotel.getRating() + "分";
        } else if (hotel.getBiz_ext() != null && hotel.getBiz_ext().getRating() != null
                && !hotel.getBiz_ext().getRating().isEmpty()) {
            ratingText = hotel.getBiz_ext().getRating() + "分";
        }

        if (hotel.getCost() != null && !hotel.getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(hotel.getCost());
                costText = String.format("人均：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "人均：" + hotel.getCost();
            }
        } else if (hotel.getBiz_ext() != null && hotel.getBiz_ext().getCost() != null
                && !hotel.getBiz_ext().getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(hotel.getBiz_ext().getCost());
                costText = String.format("人均：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "人均：" + hotel.getBiz_ext().getCost();
            }
        }

        tvRating.setText(ratingText != null ? ratingText : "暂无评分");
        tvPrice.setText(costText != null ? costText : "人均：暂无");

        tvAddress.setText(hotel.getAddress() != null ? hotel.getAddress() : "地址信息未知");

        if (hotel.getFirstTel() != null && !hotel.getFirstTel().isEmpty()) {
            tvPhone.setText(hotel.getFirstTel());
        } else {
            tvPhone.setText("暂无电话");
        }
    }

    private void displayPhotos() {
        List<Photo> photos = hotel.getPhotos();
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
        if (hotel == null)
            return;
        final String poiId = hotel.getId();
        executor.execute(() -> {
            try {
                com.travelplanner.data.local.AppDatabase db = com.travelplanner.data.local.AppDatabase
                        .getInstance(this);
                boolean isFav = db.favoriteDao().isFavorite(poiId, "hotel");
                handler.post(() -> {
                    updateFavoriteIcon(isFav);
                });
            } catch (Exception e) {
                Log.e(TAG, "检查收藏状态失败", e);
            }
        });
    }

    private void toggleFavorite() {
        if (hotel == null)
            return;
        final String poiId = hotel.getId();
        executor.execute(() -> {
            try {
                com.travelplanner.data.local.AppDatabase db = com.travelplanner.data.local.AppDatabase
                        .getInstance(this);
                boolean isFav = db.favoriteDao().isFavorite(poiId, "hotel");

                if (isFav) {
                    db.favoriteDao().deleteByPoiId(poiId, "hotel");
                    handler.post(() -> {
                        updateFavoriteIcon(false);
                        Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    com.travelplanner.data.local.entity.FavoriteEntity favorite = new com.travelplanner.data.local.entity.FavoriteEntity();
                    favorite.setPoiId(poiId);
                    favorite.setType("hotel");
                    favorite.setName(hotel.getName());
                    favorite.setAddress(hotel.getAddress());

                    if (hotel.getPhotos() != null && !hotel.getPhotos().isEmpty()) {
                        Photo photo = hotel.getPhotos().get(0);
                        favorite.setImageUrl(photo.getUrl());
                    }

                    String ratingValue = null;
                    if (hotel.getRating() != null && !hotel.getRating().isEmpty()) {
                        ratingValue = hotel.getRating();
                    } else if (hotel.getBiz_ext() != null && hotel.getBiz_ext().getRating() != null
                            && !hotel.getBiz_ext().getRating().isEmpty()) {
                        ratingValue = hotel.getBiz_ext().getRating();
                    }
                    favorite.setRating(ratingValue);

                    String costValue = null;
                    if (hotel.getCost() != null && !hotel.getCost().isEmpty()) {
                        costValue = hotel.getCost();
                    } else if (hotel.getBiz_ext() != null && hotel.getBiz_ext().getCost() != null
                            && !hotel.getBiz_ext().getCost().isEmpty()) {
                        costValue = hotel.getBiz_ext().getCost();
                    }
                    favorite.setPrice(costValue);

                    favorite.setPhone(hotel.getFirstTel());
                    favorite.setLocation(hotel.getLocation());
                    favorite.setCreatedAt(System.currentTimeMillis());

                    String poiJsonToSave;
                    if (originalSinglePoiJson != null && !originalSinglePoiJson.isEmpty()) {
                        poiJsonToSave = originalSinglePoiJson;
                        Log.d(TAG, "使用原始单个POI JSON保存");
                    } else {
                        String fullJsonFromIntent = getIntent().getStringExtra(EXTRA_HOTEL_JSON);
                        int idxFromIntent = getIntent().getIntExtra(EXTRA_HOTEL_INDEX, -1);
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
