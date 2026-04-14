package com.travelplanner.ui.itinerary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Itinerary;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateItineraryActivity extends AppCompatActivity {
    private AutoCompleteTextView actvDestination, actvOrigin;
    private EditText etDays, etPeopleCount, etBudget, etStartDate, etRestrictions;
    private Spinner spTravelStyle;
    private Button btnGenerate;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ProgressDialog progressDialog;

    private static class CityInfo {
        String name;
        String pinyin;
        String py;

        CityInfo(String name, String pinyin) {
            this.name = name;
            this.pinyin = pinyin;
            this.py = pinyin.replaceAll("[^a-zA-Z]", "").toLowerCase();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private CityInfo[] cities = {
            new CityInfo("北京", "Beijing"),
            new CityInfo("上海", "Shanghai"),
            new CityInfo("广州", "Guangzhou"),
            new CityInfo("深圳", "Shenzhen"),
            new CityInfo("成都", "Chengdu"),
            new CityInfo("杭州", "Hangzhou"),
            new CityInfo("重庆", "Chongqing"),
            new CityInfo("武汉", "Wuhan"),
            new CityInfo("西安", "Xian"),
            new CityInfo("苏州", "Suzhou"),
            new CityInfo("天津", "Tianjin"),
            new CityInfo("南京", "Nanjing"),
            new CityInfo("长沙", "Changsha"),
            new CityInfo("郑州", "Zhengzhou"),
            new CityInfo("东莞", "Dongguan"),
            new CityInfo("青岛", "Qingdao"),
            new CityInfo("沈阳", "Shenyang"),
            new CityInfo("宁波", "Ningbo"),
            new CityInfo("昆明", "Kunming"),
            new CityInfo("大连", "Dalian"),
            new CityInfo("厦门", "Xiamen"),
            new CityInfo("合肥", "Hefei"),
            new CityInfo("佛山", "Foshan"),
            new CityInfo("福州", "Fuzhou"),
            new CityInfo("济南", "Jinan"),
            new CityInfo("温州", "Wenzhou"),
            new CityInfo("长春", "Changchun"),
            new CityInfo("哈尔滨", "Haerbin"),
            new CityInfo("石家庄", "Shijiazhuang"),
            new CityInfo("南宁", "Nanning"),
            new CityInfo("泉州", "Quanzhou"),
            new CityInfo("贵阳", "Guiyang"),
            new CityInfo("南昌", "Nanchang"),
            new CityInfo("海口", "Haikou"),
            new CityInfo("兰州", "Lanzhou"),
            new CityInfo("太原", "Taiyuan"),
            new CityInfo("徐州", "Xuzhou"),
            new CityInfo("惠州", "Huizhou"),
            new CityInfo("常州", "Changzhou"),
            new CityInfo("烟台", "Yantai"),
            new CityInfo("珠海", "Zhuhai"),
            new CityInfo("金华", "Jinhua"),
            new CityInfo("潍坊", "Weifang"),
            new CityInfo("临沂", "Linyi"),
            new CityInfo("唐山", "Tangshan"),
            new CityInfo("台州", "Taizhou"),
            new CityInfo("嘉兴", "Jiaxing"),
            new CityInfo("绍兴", "Shaoxing"),
            new CityInfo("保定", "Baoding"),
            new CityInfo("漳州", "Zhangzhou"),
            new CityInfo("淄博", "Zibo"),
            new CityInfo("盐城", "Yancheng"),
            new CityInfo("济宁", "Jining"),
            new CityInfo("泰州", "Taizhou"),
            new CityInfo("廊坊", "Langfang"),
            new CityInfo("衡阳", "Hengyang")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_itinerary);

        initViews();
    }

    private void initViews() {
        actvDestination = findViewById(R.id.actv_destination);
        actvOrigin = findViewById(R.id.actv_origin);
        etDays = findViewById(R.id.et_days);
        etPeopleCount = findViewById(R.id.et_people_count);
        etBudget = findViewById(R.id.et_budget);
        etStartDate = findViewById(R.id.et_start_date);
        etRestrictions = findViewById(R.id.et_restrictions);
        spTravelStyle = findViewById(R.id.sp_travel_style);
        btnGenerate = findViewById(R.id.btn_generate);

        List<CityInfo> cityList = new ArrayList<>(Arrays.asList(cities));
        ArrayAdapter<CityInfo> cityAdapter = new ArrayAdapter<CityInfo>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                cityList) {
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        List<CityInfo> filteredList = new ArrayList<>();

                        if (constraint == null || constraint.length() == 0) {
                            filteredList.addAll(cityList);
                        } else {
                            String filterPattern = constraint.toString().toLowerCase().trim();
                            for (CityInfo city : cities) {
                                if (city.name.contains(filterPattern) ||
                                        city.py.startsWith(filterPattern) ||
                                        city.pinyin.toLowerCase().startsWith(filterPattern)) {
                                    filteredList.add(city);
                                }
                            }
                        }

                        results.values = filteredList;
                        results.count = filteredList.size();
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        clear();
                        if (results.values != null) {
                            addAll((List<CityInfo>) results.values);
                        }
                        notifyDataSetChanged();
                    }
                };
            }
        };
        actvDestination.setAdapter(cityAdapter);
        actvOrigin.setAdapter(cityAdapter);

        String[] styles = { "休闲", "亲子", "探险", "文化", "美食", "错峰优先", "暴走" };
        spTravelStyle.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, styles));

        btnGenerate.setOnClickListener(v -> generateItinerary());
    }

    private void generateItinerary() {
        String origin = actvOrigin.getText().toString().trim();
        String destination = actvDestination.getText().toString().trim();
        String daysStr = etDays.getText().toString().trim();
        String peopleCountStr = etPeopleCount.getText().toString().trim();
        String budget = etBudget.getText().toString().trim();
        String travelStyle = spTravelStyle.getSelectedItem().toString();
        String startDate = etStartDate.getText().toString().trim();
        String restrictions = etRestrictions.getText().toString().trim();

        if (destination.isEmpty()) {
            Toast.makeText(this, "请输入目的地", Toast.LENGTH_SHORT).show();
            return;
        }
        if (daysStr.isEmpty()) {
            Toast.makeText(this, "请输入天数", Toast.LENGTH_SHORT).show();
            return;
        }

        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在初始化...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        btnGenerate.setEnabled(false);

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"userId\":").append(userId);
        jsonBuilder.append(",\"origin\":\"").append(origin).append("\"");
        jsonBuilder.append(",\"destination\":\"").append(destination).append("\"");
        jsonBuilder.append(",\"days\":").append(daysStr);
        jsonBuilder.append(",\"peopleCount\":").append(peopleCountStr.isEmpty() ? "1" : peopleCountStr);
        jsonBuilder.append(",\"budget\":\"").append(budget.isEmpty() ? "5000" : budget).append("\"");
        jsonBuilder.append(",\"travelStyle\":\"").append(travelStyle).append("\"");
        jsonBuilder.append(",\"startDate\":\"").append(startDate).append("\"");
        if (!restrictions.isEmpty()) {
            jsonBuilder.append(",\"tabooNeeds\":[\"").append(restrictions.replace(",", "\",\"")).append("\"]");
        }
        jsonBuilder.append("}");

        String json = jsonBuilder.toString();

        HttpUtil.post("itinerary/generate-async", json, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    btnGenerate.setEnabled(true);
                    Toast.makeText(CreateItineraryActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<Long> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Long>>() {
                        }.getType());

                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    Long itineraryId = apiResponse.getData();
                    handler.post(() -> startPollingStatus(itineraryId));
                } else {
                    handler.post(() -> {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        btnGenerate.setEnabled(true);
                        Toast.makeText(CreateItineraryActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    });
                }
            }
        });
    }

    private void startPollingStatus(Long itineraryId) {
        final int[] pollCount = { 0 };
        final int maxPolls = 180;
        final Runnable[] pollRunnableHolder = new Runnable[1];

        Runnable pollRunnable = new Runnable() {
            @Override
            public void run() {
                if (pollCount[0] >= maxPolls) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    btnGenerate.setEnabled(true);
                    Toast.makeText(CreateItineraryActivity.this, "生成超时，请重试", Toast.LENGTH_SHORT).show();
                    return;
                }

                pollCount[0]++;

                HttpUtil.get("itinerary/status/" + itineraryId, new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, java.io.IOException e) {
                        handler.postDelayed(pollRunnableHolder[0], 1000);
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                        String body = response.body().string();
                        ApiResponse<Itinerary> apiResponse = HttpUtil.fromJson(body,
                                new TypeToken<ApiResponse<Itinerary>>() {
                                }.getType());

                        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                            Itinerary itinerary = apiResponse.getData();
                            String status = itinerary.getGenerateStatus();
                            String message = itinerary.getGenerateMessage();

                            handler.post(() -> {
                                if ("generating".equals(status)) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.setMessage(message != null ? message : "AI正在生成行程...");
                                    }
                                    handler.postDelayed(pollRunnableHolder[0], 1000);
                                } else if ("completed".equals(status)) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(CreateItineraryActivity.this, "行程生成成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateItineraryActivity.this,
                                            ItineraryDetailActivity.class);
                                    intent.putExtra("itinerary_id", itineraryId);
                                    startActivity(intent);
                                    finish();
                                } else if ("failed".equals(status)) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    btnGenerate.setEnabled(true);
                                    Toast.makeText(CreateItineraryActivity.this,
                                            message != null ? message : "生成失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            handler.postDelayed(pollRunnableHolder[0], 1000);
                        }
                    }
                });
            }
        };

        pollRunnableHolder[0] = pollRunnable;
        handler.post(pollRunnable);
    }
}
