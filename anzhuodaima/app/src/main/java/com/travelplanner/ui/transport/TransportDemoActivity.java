package com.travelplanner.ui.transport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Itinerary;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class TransportDemoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_ORIGIN = 2001;
    private static final int REQUEST_CODE_SELECT_DESTINATION = 2002;

    private Handler handler = new Handler(Looper.getMainLooper());
    private List<Itinerary> itineraryList = new ArrayList<>();
    private Spinner spinnerItinerary;
    private EditText etOrigin, etDestination;
    private RadioGroup rgTransportCategory;
    private RadioButton rbPublicTransport, rbSelfDriving;
    private LinearLayout llPublicTransportOptions, llSelfDrivingOptions;
    private CheckBox cbTrain, cbHighspeed, cbFlight, cbTransit, cbDrive, cbWalk, cbRide;
    private Button btnViewTransport;
    private Itinerary selectedItinerary;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_demo);

        spinnerItinerary = findViewById(R.id.spinnerItinerary);
        etOrigin = findViewById(R.id.etOrigin);
        etDestination = findViewById(R.id.etDestination);

        // 交通方式分类单选组
        rgTransportCategory = findViewById(R.id.rgTransportCategory);
        rbPublicTransport = findViewById(R.id.rbPublicTransport);
        rbSelfDriving = findViewById(R.id.rbSelfDriving);
        llPublicTransportOptions = findViewById(R.id.llPublicTransportOptions);
        llSelfDrivingOptions = findViewById(R.id.llSelfDrivingOptions);

        // 公共交通选项
        cbTrain = findViewById(R.id.cbTrain);
        cbHighspeed = findViewById(R.id.cbHighspeed);
        cbFlight = findViewById(R.id.cbFlight);
        cbTransit = findViewById(R.id.cbTransit);

        // 自驾/慢行选项
        cbDrive = findViewById(R.id.cbDrive);
        cbWalk = findViewById(R.id.cbWalk);
        cbRide = findViewById(R.id.cbRide);

        btnViewTransport = findViewById(R.id.btnViewTransport);
        btnViewTransport.setOnClickListener(v -> planRoute());
        btnViewTransport.setEnabled(false);

        // 设置交通方式分类的监听器
        rgTransportCategory.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPublicTransport) {
                llPublicTransportOptions.setVisibility(View.VISIBLE);
                llSelfDrivingOptions.setVisibility(View.GONE);
                // 清空自驾选项
                cbDrive.setChecked(false);
                cbWalk.setChecked(false);
                cbRide.setChecked(false);
            } else if (checkedId == R.id.rbSelfDriving) {
                llPublicTransportOptions.setVisibility(View.GONE);
                llSelfDrivingOptions.setVisibility(View.VISIBLE);
                // 清空公共交通选项
                cbTrain.setChecked(false);
                cbHighspeed.setChecked(false);
                cbFlight.setChecked(false);
                cbTransit.setChecked(false);
            }
        });

        etOrigin.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.travelplanner.ui.restaurant.CitySelectActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_ORIGIN);
        });
        etOrigin.setFocusable(false);
        etOrigin.setCursorVisible(false);

        etDestination.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.travelplanner.ui.restaurant.CitySelectActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_DESTINATION);
        });
        etDestination.setFocusable(false);
        etDestination.setCursorVisible(false);

        loadItineraries();
    }

    private void loadItineraries() {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        Log.d("TransportDemo", "loadItineraries - userId: " + userId);

        if (userId == 0) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TransportDemo", "开始从后端加载行程");
        HttpUtil.get("itinerary/user/" + userId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                Log.e("TransportDemo", "网络请求失败", e);
                handler.post(() -> {
                    Toast.makeText(TransportDemoActivity.this, "网络错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                Log.d("TransportDemo", "后端响应: " + body);

                try {
                    ApiResponse<List<Itinerary>> apiResponse = HttpUtil.fromJson(body,
                            new TypeToken<ApiResponse<List<Itinerary>>>() {
                            }.getType());

                    handler.post(() -> {
                        if (apiResponse.isSuccess() && apiResponse.getData() != null
                                && !apiResponse.getData().isEmpty()) {
                            Log.d("TransportDemo", "成功加载 " + apiResponse.getData().size() + " 个行程");
                            itineraryList.clear();
                            itineraryList.addAll(apiResponse.getData());
                            setupItinerarySpinner();
                        } else {
                            Toast.makeText(TransportDemoActivity.this, "没有可用的行程，请先创建行程", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("TransportDemo", "解析响应失败", e);
                    handler.post(() -> {
                        Toast.makeText(TransportDemoActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void setupItinerarySpinner() {
        Log.d("TransportDemo", "设置行程Spinner，行程数量: " + itineraryList.size());

        List<String> itineraryNames = new ArrayList<>();
        for (Itinerary itinerary : itineraryList) {
            String itineraryInfo = itinerary.getTitle();
            if (itinerary.getDestination() != null && !itinerary.getDestination().isEmpty()) {
                itineraryInfo += " - " + itinerary.getDestination();
            }
            itineraryNames.add(itineraryInfo);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(TransportDemoActivity.this,
                android.R.layout.simple_spinner_item, itineraryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItinerary.setAdapter(adapter);

        spinnerItinerary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TransportDemo", "选择了行程: " + position);
                if (position >= 0 && position < itineraryList.size()) {
                    Itinerary basicItinerary = itineraryList.get(position);
                    loadItineraryDetail(basicItinerary.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TransportDemo", "未选择行程");
                selectedItinerary = null;
                btnViewTransport.setEnabled(false);
            }
        });

        if (!itineraryList.isEmpty()) {
            Log.d("TransportDemo", "默认加载第一个行程");
            loadItineraryDetail(itineraryList.get(0).getId());
        }
    }

    private void loadItineraryDetail(Long itineraryId) {
        Log.d("TransportDemo", "加载行程详情: " + itineraryId);
        btnViewTransport.setEnabled(false);
        Toast.makeText(this, "正在加载行程详情...", Toast.LENGTH_SHORT).show();

        HttpUtil.get("itinerary/" + itineraryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                Log.e("TransportDemo", "加载行程详情失败", e);
                handler.post(() -> {
                    Toast.makeText(TransportDemoActivity.this, "加载行程详情失败: " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    btnViewTransport.setEnabled(false);
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                Log.d("TransportDemo", "行程详情响应: " + body);

                try {
                    ApiResponse<Itinerary> apiResponse = HttpUtil.fromJson(body,
                            new TypeToken<ApiResponse<Itinerary>>() {
                            }.getType());

                    handler.post(() -> {
                        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                            selectedItinerary = apiResponse.getData();
                            Log.d("TransportDemo", "成功加载行程详情，dayPlans数量: " +
                                    (selectedItinerary.getDayPlans() != null ? selectedItinerary.getDayPlans().size()
                                            : 0));
                            btnViewTransport.setEnabled(true);
                            Toast.makeText(TransportDemoActivity.this, "行程详情加载成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransportDemoActivity.this, "加载行程详情失败", Toast.LENGTH_SHORT).show();
                            btnViewTransport.setEnabled(false);
                        }
                    });
                } catch (Exception e) {
                    Log.e("TransportDemo", "解析行程详情失败", e);
                    handler.post(() -> {
                        Toast.makeText(TransportDemoActivity.this, "解析行程详情失败", Toast.LENGTH_SHORT).show();
                        btnViewTransport.setEnabled(false);
                    });
                }
            }
        });
    }

    private void planRoute() {
        if (selectedItinerary == null) {
            Toast.makeText(this, "请先选择行程", Toast.LENGTH_SHORT).show();
            return;
        }

        String origin = etOrigin.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();

        if (origin.isEmpty()) {
            Toast.makeText(this, "请输入出发地", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            Toast.makeText(this, "请输入目的地", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> selectedModes = new ArrayList<>();

        // 根据选中的分类收集出行方式
        if (rbPublicTransport.isChecked()) {
            // 公共交通
            if (cbTrain.isChecked())
                selectedModes.add("train");
            if (cbHighspeed.isChecked())
                selectedModes.add("highspeed");
            if (cbFlight.isChecked())
                selectedModes.add("flight");
            if (cbTransit.isChecked())
                selectedModes.add("transit");
        } else if (rbSelfDriving.isChecked()) {
            // 自驾/慢行
            if (cbDrive.isChecked())
                selectedModes.add("drive");
            if (cbWalk.isChecked())
                selectedModes.add("walk");
            if (cbRide.isChecked())
                selectedModes.add("ride");
        }

        if (selectedModes.isEmpty()) {
            String hint = rbPublicTransport.isChecked() ? "请至少选择一种公共交通方式" : "请至少选择一种自驾/慢行方式";
            Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TransportDemo", "选中的出行方式分类: " + (rbPublicTransport.isChecked() ? "公共交通" : "自驾/慢行"));
        Log.d("TransportDemo", "选中的出行方式: " + selectedModes);
        Log.d("TransportDemo", "选中的行程: " + selectedItinerary.getTitle());
        Log.d("TransportDemo", "出发地: " + origin);
        Log.d("TransportDemo", "目的地: " + destination);

        Intent intent = new Intent(this, TransportPlanActivity.class);

        String itineraryJson = gson.toJson(selectedItinerary);
        intent.putExtra("itineraryJson", itineraryJson);

        String modesJson = gson.toJson(selectedModes);
        intent.putExtra("selectedModes", modesJson);

        intent.putExtra("customOrigin", origin);
        intent.putExtra("customDestination", destination);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String cityName = data.getStringExtra(com.travelplanner.ui.restaurant.CitySelectActivity.EXTRA_SELECTED_CITY_NAME);
            if (cityName != null) {
                if (requestCode == REQUEST_CODE_SELECT_ORIGIN) {
                    etOrigin.setText(cityName);
                } else if (requestCode == REQUEST_CODE_SELECT_DESTINATION) {
                    etDestination.setText(cityName);
                }
            }
        }
    }
}
