package com.travelplanner.ui.restaurant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.travelplanner.R;
import com.travelplanner.data.remote.AMapConfig;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.AMapApi;
import com.travelplanner.model.amap.AMapPoiSearchResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class RestaurantSearchActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantSearch";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int REQUEST_CODE_SELECT_CITY = 2001;
    private static final int DEFAULT_RADIUS = 3000;
    private static final String RESTAURANT_TYPE_CODE = "050000";

    private Handler handler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();
    private AMapApi aMapApi;
    private FusedLocationProviderClient fusedLocationClient;

    private Toolbar toolbar;
    private Button btnUseCurrentLocation;
    private EditText etSearchLocation;
    private EditText etKeyword;
    private Button btnSearch;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvError;
    private RecyclerView recyclerViewRestaurants;
    private RestaurantAdapter adapter;

    private String currentLatitude;
    private String currentLongitude;
    private String searchCity;
    private String searchDistrict;
    private String rawJsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);

        aMapApi = ApiClient.getAMapApi();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupToolbar();
        setupViews();
        setupRecyclerView();
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
        btnUseCurrentLocation = findViewById(R.id.btnUseCurrentLocation);
        etSearchLocation = findViewById(R.id.etSearchLocation);
        etKeyword = findViewById(R.id.etKeyword);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvError = findViewById(R.id.tvError);
        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);

        btnUseCurrentLocation.setOnClickListener(v -> getCurrentLocation());
        etSearchLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, CitySelectActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_CITY);
        });
        etSearchLocation.setFocusable(false);
        etSearchLocation.setCursorVisible(false);
        btnSearch.setOnClickListener(v -> performSearch());
    }

    private void setupRecyclerView() {
        adapter = new RestaurantAdapter();
        recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRestaurants.setAdapter(adapter);

        adapter.setOnRestaurantClickListener((restaurant, position) -> {
            Intent intent = new Intent(this, RestaurantDetailActivity.class);
            intent.putExtra(RestaurantDetailActivity.EXTRA_RESTAURANT_JSON, rawJsonResponse);
            intent.putExtra(RestaurantDetailActivity.EXTRA_RESTAURANT_INDEX, position);

            if (rawJsonResponse != null) {
                try {
                    com.google.gson.JsonObject fullObj = com.google.gson.JsonParser.parseString(rawJsonResponse)
                            .getAsJsonObject();
                    com.google.gson.JsonArray poisArray = fullObj.getAsJsonArray("pois");
                    if (poisArray != null && position < poisArray.size()) {
                        String singlePoiJson = poisArray.get(position).toString();
                        intent.putExtra("extra_single_poi_json", singlePoiJson);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "提取单个POI JSON失败", e);
                }
            }

            startActivity(intent);
        });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLatitude = String.valueOf(location.getLatitude());
                            currentLongitude = String.valueOf(location.getLongitude());
                            etSearchLocation.setText("当前位置已获取");
                            Toast.makeText(this, "已获取当前位置", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "当前位置: " + currentLatitude + ", " + currentLongitude);
                        } else {
                            Toast.makeText(this, "无法获取当前位置，请手动输入", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "获取位置失败", e);
                        Toast.makeText(this, "获取位置失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (SecurityException e) {
            Log.e(TAG, "位置权限错误", e);
            Toast.makeText(this, "位置权限错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "需要位置权限才能使用当前位置", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void performSearch() {
        String locationText = etSearchLocation.getText().toString().trim();
        String keyword = etKeyword.getText().toString().trim();

        if (currentLatitude == null || currentLongitude == null) {
            if (locationText.isEmpty()) {
                Toast.makeText(this, "请使用当前位置或输入搜索位置", Toast.LENGTH_SHORT).show();
                return;
            }
            geocodeLocationAndSearch(locationText, keyword);
        } else {
            searchNearbyRestaurants(keyword);
        }
    }

    private void geocodeLocationAndSearch(String location, String keyword) {
        showLoading(true);
        executor.execute(() -> {
            try {
                // 如果选择了区县，使用"城市+区县"进行地理编码以获得更精确的结果
                String fullLocation = location;
                if (searchDistrict != null && !searchDistrict.isEmpty()) {
                    fullLocation = location + searchDistrict;
                }

                Call<com.travelplanner.model.amap.AMapGeocodeResponse> geocodingCall = aMapApi
                        .geocode(AMapConfig.API_KEY, fullLocation, "");
                Response<com.travelplanner.model.amap.AMapGeocodeResponse> geocodingResponse = geocodingCall.execute();

                if (geocodingResponse.isSuccessful() && geocodingResponse.body() != null) {
                    com.travelplanner.model.amap.AMapGeocodeResponse response = geocodingResponse.body();
                    if (response.getGeocodes() != null && !response.getGeocodes().isEmpty()) {
                        com.travelplanner.model.amap.AMapGeocodeResponse.Geocode geocode = response.getGeocodes()
                                .get(0);
                        String locationStr = geocode.getLocation();
                        if (locationStr != null && locationStr.contains(",")) {
                            String[] parts = locationStr.split(",");
                            currentLongitude = parts[0];
                            currentLatitude = parts[1];
                            searchCity = geocode.getCity();
                            searchNearbyRestaurants(keyword);
                            return;
                        }
                    }
                }

                handler.post(() -> {
                    showLoading(false);
                    showError("无法解析位置，请检查地址是否正确");
                });

            } catch (Exception e) {
                Log.e(TAG, "地理编码失败", e);
                handler.post(() -> {
                    showLoading(false);
                    showError("搜索失败: " + e.getMessage());
                });
            }
        });
    }

    private void searchNearbyRestaurants(String keyword) {
        showLoading(true);
        executor.execute(() -> {
            try {
                String location = currentLongitude + "," + currentLatitude;
                String searchKeyword = keyword.isEmpty() ? "餐厅" : keyword;

                Log.d(TAG, "搜索附近餐厅: location=" + location + ", keyword=" + searchKeyword);

                String url = AMapConfig.BASE_URL + "place/around" +
                        "?key=" + AMapConfig.API_KEY +
                        "&keywords=" + java.net.URLEncoder.encode(searchKeyword, "UTF-8") +
                        "&location=" + location +
                        "&radius=" + DEFAULT_RADIUS +
                        "&types=" + RESTAURANT_TYPE_CODE +
                        "&offset=20&page=1&extensions=all";

                Log.d(TAG, "请求URL: " + url);

                okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .build();

                okhttp3.Response okResponse = client.newCall(request).execute();
                rawJsonResponse = null;

                if (okResponse.body() != null) {
                    rawJsonResponse = okResponse.body().string();
                    Log.d(TAG, "原始响应数据: " + rawJsonResponse);
                }

                Log.d(TAG, "搜索响应码: " + okResponse.code());

                AMapPoiSearchResponse response = null;
                if (rawJsonResponse != null) {
                    response = new com.google.gson.Gson().fromJson(rawJsonResponse, AMapPoiSearchResponse.class);
                }

                if (response != null) {
                    Log.d(TAG, "搜索状态: " + response.getStatus() + ", info: " + response.getInfo());

                    final String errorInfo = response.getInfo();
                    final List<AMapPoiSearchResponse.Poi> finalPois = response.getPois();

                    if (response.isSuccess()) {
                        if (finalPois != null && !finalPois.isEmpty()) {
                            AMapPoiSearchResponse.Poi firstPoi = finalPois.get(0);
                            Log.d(TAG, "第一个餐厅: " + firstPoi.getName());
                            Log.d(TAG, "Poi.rating: " + firstPoi.getRating());
                            Log.d(TAG, "Poi.cost: " + firstPoi.getCost());
                            Log.d(TAG,
                                    "Photos: " + (firstPoi.getPhotos() != null ? firstPoi.getPhotos().size() : "null"));
                            if (firstPoi.getPhotos() != null && !firstPoi.getPhotos().isEmpty()) {
                                for (int i = 0; i < firstPoi.getPhotos().size(); i++) {
                                    com.travelplanner.model.amap.Photo photo = firstPoi.getPhotos().get(i);
                                    Log.d(TAG, "Photo " + i + " - title: " + photo.getTitle() + ", url: "
                                            + photo.getUrl());
                                }
                            }
                            Log.d(TAG, "Biz_ext: " + (firstPoi.getBiz_ext() != null ? "存在" : "null"));
                            if (firstPoi.getBiz_ext() != null) {
                                Log.d(TAG, "Biz_ext.rating: " + firstPoi.getBiz_ext().getRating());
                                Log.d(TAG, "Biz_ext.cost: " + firstPoi.getBiz_ext().getCost());
                                Log.d(TAG, "Biz_ext.opentime2: " + firstPoi.getBiz_ext().getOpentime2());
                            }
                            Log.d(TAG, "Type: " + firstPoi.getType());
                        }

                        handler.post(() -> {
                            showLoading(false);
                            if (finalPois != null && !finalPois.isEmpty()) {
                                adapter.setData(finalPois);
                                showRestaurants();
                                Log.d(TAG, "找到 " + finalPois.size() + " 个餐厅");
                            } else {
                                showEmpty();
                            }
                        });
                    } else {
                        handler.post(() -> {
                            showLoading(false);
                            showError("搜索失败: " + errorInfo);
                        });
                    }
                } else {
                    handler.post(() -> {
                        showLoading(false);
                        showError("搜索请求失败");
                    });
                }

            } catch (Exception e) {
                Log.e(TAG, "搜索餐厅失败", e);
                handler.post(() -> {
                    showLoading(false);
                    showError("搜索失败: " + e.getMessage());
                });
            }
        });

    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            recyclerViewRestaurants.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
            tvError.setVisibility(View.GONE);
        }
    }

    private void showRestaurants() {
        recyclerViewRestaurants.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showEmpty() {
        recyclerViewRestaurants.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
        tvEmpty.setText("未找到相关餐厅，请尝试其他关键词");
        tvError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        recyclerViewRestaurants.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_CITY && resultCode == RESULT_OK && data != null) {
            String cityName = data.getStringExtra(CitySelectActivity.EXTRA_SELECTED_CITY_NAME);
            String district = data.getStringExtra(CitySelectActivity.EXTRA_SELECTED_DISTRICT);
            if (cityName != null) {
                if (district != null && !district.isEmpty()) {
                    etSearchLocation.setText(cityName + " " + district);
                    searchDistrict = district;
                } else {
                    etSearchLocation.setText(cityName);
                    searchDistrict = null;
                }
                currentLatitude = null;
                currentLongitude = null;
            }
        }
    }
}
