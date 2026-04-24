package com.travelplanner.ui.checklist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.adapter.ChecklistAdapter;
import com.travelplanner.data.local.AppDatabase;
import com.travelplanner.data.local.dao.ChecklistDao;
import com.travelplanner.data.local.dao.ItineraryDao;
import com.travelplanner.data.local.entity.ChecklistItemEntity;
import com.travelplanner.data.local.entity.ItineraryEntity;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Checklist;
import com.travelplanner.model.Itinerary;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChecklistActivity extends AppCompatActivity {
    private static final String EXTRA_ITINERARY_ID = "itinerary_id";
    private static final String EXTRA_ITINERARY = "itinerary";
    private static final String EXTRA_VIEW_MODE = "view_mode";

    private ChecklistAdapter adapter;

    private TextView tvTotalCount;
    private TextView tvCheckedCount;
    private RecyclerView rvChecklist;
    private Button btnAddItem;
    private Button btnGenerateSmart;
    private Button btnSaveChecklist;
    private ProgressBar progressBar;
    private ChipGroup chipGroupCategories;

    private Long itineraryId;
    private Itinerary itinerary;
    private Long selectedItineraryIdForSave;
    private List<Checklist> allItems = new ArrayList<>();
    private String currentCategory = "全部";
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isViewMode = false;

    private AppDatabase db;
    private ChecklistDao checklistDao;
    private ItineraryDao itineraryDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final String[] CATEGORIES = { "全部", "衣物", "洗漱用品", "电子设备", "证件文件", "其他" };
    private static final String[] TRIP_TYPES = { "休闲", "商务", "徒步", "亲子", "海滩", "滑雪" };

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Gson gson = new Gson();

    public static void start(Context context, Long itineraryId) {
        Intent intent = new Intent(context, ChecklistActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID, itineraryId);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ChecklistActivity.class);
        context.startActivity(intent);
    }

    public static void startWithItinerary(Context context, Itinerary itinerary) {
        Intent intent = new Intent(context, ChecklistActivity.class);
        intent.putExtra(EXTRA_ITINERARY, gson.toJson(itinerary));
        context.startActivity(intent);
    }

    public static void startViewMode(Context context, Long itineraryId) {
        Intent intent = new Intent(context, ChecklistActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID, itineraryId);
        intent.putExtra(EXTRA_VIEW_MODE, true);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        db = AppDatabase.getInstance(this);
        checklistDao = db.checklistDao();
        itineraryDao = db.itineraryDao();

        itineraryId = getIntent().getLongExtra(EXTRA_ITINERARY_ID, 0);
        isViewMode = getIntent().getBooleanExtra(EXTRA_VIEW_MODE, false);
        String itineraryJson = getIntent().getStringExtra(EXTRA_ITINERARY);
        if (itineraryJson != null) {
            itinerary = gson.fromJson(itineraryJson, Itinerary.class);
            if (itinerary != null) {
                itineraryId = itinerary.getId();
            }
        }

        adapter = new ChecklistAdapter();

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupChipGroup();
        setupListeners();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    private void initViews() {
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvCheckedCount = findViewById(R.id.tvCheckedCount);
        rvChecklist = findViewById(R.id.rvChecklist);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnGenerateSmart = findViewById(R.id.btnGenerateSmart);
        progressBar = findViewById(R.id.progressBar);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        btnSaveChecklist = findViewById(R.id.btnSaveChecklist);

        if (isViewMode) {
            btnAddItem.setVisibility(View.GONE);
            btnGenerateSmart.setVisibility(View.GONE);
            if (btnSaveChecklist != null) {
                btnSaveChecklist.setVisibility(View.GONE);
            }
        }
    }

    private void setupChipGroup() {
        chipGroupCategories.removeAllViews();
        for (String category : CATEGORIES) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            chip.setChecked(category.equals("全部"));
            chip.setOnClickListener(v -> {
                currentCategory = category;
                filterItems();
            });
            chipGroupCategories.addView(chip);
        }
    }

    private void filterItems() {
        if (currentCategory.equals("全部")) {
            adapter.setItems(allItems);
        } else {
            List<Checklist> filtered = new ArrayList<>();
            for (Checklist item : allItems) {
                if (item.getCategory() != null && item.getCategory().equals(currentCategory)) {
                    filtered.add(item);
                }
            }
            adapter.setItems(filtered);
        }
        updateCount();
    }

    private void updateCount() {
        List<Checklist> items = currentCategory.equals("全部") ? allItems : adapter.getItems();
        if (items == null)
            return;

        int total = items.size();
        int checked = 0;
        for (Checklist item : items) {
            if (item.getIsChecked() != null && item.getIsChecked()) {
                checked++;
            }
        }

        tvTotalCount.setText("共 " + total + " 项");
        tvCheckedCount.setText("已准备 " + checked + " 项");
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (isViewMode) {
                toolbar.setTitle("查看出行清单");
            }
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        rvChecklist.setLayoutManager(new LinearLayoutManager(this));
        rvChecklist.setAdapter(adapter);
    }

    private void setupListeners() {
        adapter.setOnItemCheckListener((id, checked) -> {
            for (Checklist item : allItems) {
                if (item.getId() != null && item.getId().equals(id)) {
                    item.setIsChecked(checked);
                    if (!isViewMode && itineraryId != null && itineraryId > 0) {
                        updateItemInDatabase(item);
                    }
                    break;
                }
            }
            updateCount();
        });

        adapter.setOnItemDeleteListener(id -> {
            if (isViewMode)
                return;

            new AlertDialog.Builder(this)
                    .setTitle("删除确认")
                    .setMessage("确定要删除这个物品吗？")
                    .setPositiveButton("删除", (dialog, which) -> {
                        for (int i = 0; i < allItems.size(); i++) {
                            if (allItems.get(i).getId() != null && allItems.get(i).getId().equals(id)) {
                                Checklist removedItem = allItems.remove(i);
                                if (itineraryId != null && itineraryId > 0 && removedItem != null) {
                                    deleteItemFromDatabase(removedItem.getId());
                                }
                                filterItems();
                                break;
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        btnAddItem.setOnClickListener(v -> showAddItemDialog());
        btnGenerateSmart.setOnClickListener(v -> showSmartGenerateDialog());

        if (btnSaveChecklist != null) {
            btnSaveChecklist.setOnClickListener(v -> {
                if (selectedItineraryIdForSave != null && selectedItineraryIdForSave > 0) {
                    itineraryId = selectedItineraryIdForSave;
                    saveAllToDatabase(selectedItineraryIdForSave);
                } else {
                    showSelectItineraryAndSaveDialog();
                }
            });
        }
    }

    private void loadData() {
        if (itineraryId != null && itineraryId > 0) {
            executor.execute(() -> {
                List<ChecklistItemEntity> entities = checklistDao.getByItineraryId(itineraryId);
                handler.post(() -> {
                    allItems.clear();
                    if (entities != null && !entities.isEmpty()) {
                        for (ChecklistItemEntity entity : entities) {
                            Checklist item = convertEntityToModel(entity);
                            allItems.add(item);
                        }
                    }
                    filterItems();

                    if (entities != null && !entities.isEmpty()) {
                        Toast.makeText(this, "已加载行程的行李清单（" + entities.size() + "项）", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            allItems = new ArrayList<>();
            filterItems();
        }
    }

    private void updateItemInDatabase(Checklist item) {
        executor.execute(() -> {
            try {
                ChecklistItemEntity entity = convertModelToEntity(item);
                checklistDao.update(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void deleteItemFromDatabase(Long itemId) {
        executor.execute(() -> {
            try {
                ChecklistItemEntity entity = checklistDao.getById(itemId);
                if (entity != null) {
                    checklistDao.delete(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void saveAllToDatabase(Long targetItineraryId) {
        executor.execute(() -> {
            try {
                if (targetItineraryId == null || targetItineraryId <= 0)
                    return;

                List<ChecklistItemEntity> existingItems = checklistDao.getByItineraryId(targetItineraryId);

                List<String> existingNames = new ArrayList<>();
                for (ChecklistItemEntity existing : existingItems) {
                    if (existing.getItemName() != null) {
                        existingNames.add(existing.getItemName());
                    }
                }

                List<ChecklistItemEntity> entities = new ArrayList<>();
                int sortOrder = existingItems.size();
                int addedCount = 0;

                for (Checklist item : allItems) {
                    if (item.getItemName() != null && !existingNames.contains(item.getItemName())) {
                        ChecklistItemEntity entity = convertModelToEntity(item);
                        entity.setId(null);
                        entity.setItineraryId(targetItineraryId);
                        entity.setSortOrder(sortOrder++);
                        entities.add(entity);
                        addedCount++;
                    }
                }

                if (!entities.isEmpty()) {
                    checklistDao.insertAll(entities);
                }

                final int finalAddedCount = addedCount;
                final int totalCount = existingItems.size() + addedCount;
                handler.post(() -> {
                    if (finalAddedCount > 0) {
                        Toast.makeText(this, "已添加" + finalAddedCount + "项新物品，该行程共" + totalCount + "项物品",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "所有物品已存在，无需重复添加", Toast.LENGTH_SHORT).show();
                    }

                    if (!isViewMode) {
                        allItems.clear();
                        itineraryId = null;
                        selectedItineraryIdForSave = null;
                        filterItems();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> {
                    Toast.makeText(this, "保存失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private ChecklistItemEntity convertModelToEntity(Checklist model) {
        ChecklistItemEntity entity = new ChecklistItemEntity();
        entity.setId(model.getId());
        entity.setItineraryId(model.getItineraryId());
        entity.setItemName(model.getItemName());
        entity.setCategory(model.getCategory());
        entity.setIsChecked(model.getIsChecked());
        entity.setNotes(model.getNotes());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        return entity;
    }

    private Checklist convertEntityToModel(ChecklistItemEntity entity) {
        Checklist model = new Checklist();
        model.setId(entity.getId());
        model.setItineraryId(entity.getItineraryId());
        model.setItemName(entity.getItemName());
        model.setCategory(entity.getCategory());
        model.setIsChecked(entity.getIsChecked());
        model.setNotes(entity.getNotes());
        return model;
    }

    private void showSelectItineraryAndSaveDialog() {
        AlertDialog loadingDialog = new AlertDialog.Builder(this)
                .setTitle("加载中")
                .setMessage("正在获取行程列表...")
                .setCancelable(false)
                .create();
        loadingDialog.show();

        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            loadingDialog.dismiss();
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtil.get("itinerary/user/" + userId, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, java.io.IOException e) {
                handler.post(() -> {
                    loadingDialog.dismiss();

                    executor.execute(() -> {
                        List<ItineraryEntity> localItineraries = itineraryDao.getAll();
                        handler.post(() -> {
                            if (localItineraries == null || localItineraries.isEmpty()) {
                                Toast.makeText(ChecklistActivity.this, "暂无行程，请先创建行程", Toast.LENGTH_SHORT).show();
                            } else {
                                showItinerarySelectionDialog(localItineraries);
                            }
                        });
                    });
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws java.io.IOException {
                String body = response.body().string();
                handler.post(() -> {
                    loadingDialog.dismiss();

                    try {
                        ApiResponse<List<Itinerary>> apiResponse = HttpUtil.fromJson(body,
                                new TypeToken<ApiResponse<List<Itinerary>>>() {
                                }.getType());

                        if (apiResponse.isSuccess() && apiResponse.getData() != null
                                && !apiResponse.getData().isEmpty()) {
                            List<Itinerary> serverItineraries = apiResponse.getData();

                            executor.execute(() -> {
                                List<ItineraryEntity> entities = new ArrayList<>();
                                for (Itinerary itinerary : serverItineraries) {
                                    ItineraryEntity entity = convertItineraryToEntity(itinerary);
                                    entities.add(entity);
                                }
                                itineraryDao.insertAll(entities);

                                handler.post(() -> showItinerarySelectionDialog(entities));
                            });
                        } else {
                            executor.execute(() -> {
                                List<ItineraryEntity> localItineraries = itineraryDao.getAll();
                                handler.post(() -> {
                                    if (localItineraries == null || localItineraries.isEmpty()) {
                                        Toast.makeText(ChecklistActivity.this, "暂无行程，请先创建行程", Toast.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        showItinerarySelectionDialog(localItineraries);
                                    }
                                });
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ChecklistActivity.this, "解析行程数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showItinerarySelectionDialog(List<ItineraryEntity> itineraries) {
        String[] itineraryNames = new String[itineraries.size()];
        for (int i = 0; i < itineraries.size(); i++) {
            String title = itineraries.get(i).getTitle();
            String destination = itineraries.get(i).getDestination();
            itineraryNames[i] = (title != null ? title : "未命名行程") +
                    (destination != null && !destination.isEmpty() ? " - " + destination : "");
        }

        new AlertDialog.Builder(this)
                .setTitle("选择要绑定的行程")
                .setItems(itineraryNames, (dialog, which) -> {
                    Long selectedItineraryId = itineraries.get(which).getId();
                    itineraryId = selectedItineraryId;
                    saveAllToDatabase(selectedItineraryId);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private ItineraryEntity convertItineraryToEntity(Itinerary itinerary) {
        ItineraryEntity entity = new ItineraryEntity();
        entity.setId(itinerary.getId());
        entity.setTitle(itinerary.getTitle());
        entity.setDestination(itinerary.getDestination());
        entity.setStartDate(itinerary.getStartDate());
        entity.setEndDate(itinerary.getEndDate());
        entity.setDays(itinerary.getDays());
        entity.setPersonCount(itinerary.getPeopleCount());
        entity.setStyle(itinerary.getTravelStyle());

        try {
            if (itinerary.getStatus() != null) {
                entity.setStatus(Integer.parseInt(itinerary.getStatus()));
            } else {
                entity.setStatus(0);
            }
        } catch (NumberFormatException e) {
            entity.setStatus(0);
        }

        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        return entity;
    }

    private void showSmartGenerateDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_smart_generate, null);
        Spinner spinnerItinerary = dialogView.findViewById(R.id.spinnerItinerary);
        Spinner spinnerTripType = dialogView.findViewById(R.id.spinnerTripType);
        Spinner spinnerTemperature = dialogView.findViewById(R.id.spinnerTemperature);
        Spinner spinnerRain = dialogView.findViewById(R.id.spinnerRain);
        TextView tvWeatherInfo = dialogView.findViewById(R.id.tvWeatherInfo);
        Button btnUseItineraryWeather = dialogView.findViewById(R.id.btnUseItineraryWeather);
        ProgressBar progressBarWeather = dialogView.findViewById(R.id.progressBarWeather);

        List<ItineraryEntity> itineraryList = new ArrayList<>();
        ArrayAdapter<String> itineraryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        itineraryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItinerary.setAdapter(itineraryAdapter);

        ArrayAdapter<String> tripTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, TRIP_TYPES);
        tripTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTripType.setAdapter(tripTypeAdapter);

        ArrayAdapter<String> tempAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[] { "寒冷(<10°C)", "凉爽(10-20°C)", "温暖(20-28°C)", "炎热(>28°C)" });
        tempAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemperature.setAdapter(tempAdapter);

        ArrayAdapter<String> rainAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[] { "晴天", "多云", "小雨", "大雨" });
        rainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRain.setAdapter(rainAdapter);

        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId != 0) {
            HttpUtil.get("itinerary/user/" + userId, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, java.io.IOException e) {
                    handler.post(() -> {
                        itineraryAdapter.clear();
                        itineraryAdapter.add("无可用行程");
                        itineraryAdapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws java.io.IOException {
                    String body = response.body().string();
                    handler.post(() -> {
                        try {
                            ApiResponse<List<Itinerary>> apiResponse = HttpUtil.fromJson(body,
                                    new TypeToken<ApiResponse<List<Itinerary>>>() {
                                    }.getType());

                            if (apiResponse.isSuccess() && apiResponse.getData() != null
                                    && !apiResponse.getData().isEmpty()) {
                                itineraryList.clear();
                                itineraryAdapter.clear();
                                for (Itinerary it : apiResponse.getData()) {
                                    ItineraryEntity entity = convertItineraryToEntity(it);
                                    itineraryList.add(entity);
                                    String title = entity.getTitle() != null ? entity.getTitle() : "未命名行程";
                                    String dest = entity.getDestination() != null ? entity.getDestination() : "";
                                    itineraryAdapter.add(title + (dest.isEmpty() ? "" : " - " + dest));
                                }
                                itineraryAdapter.notifyDataSetChanged();

                                executor.execute(() -> {
                                    try {
                                        itineraryDao.insertAll(itineraryList);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            } else {
                                itineraryAdapter.clear();
                                itineraryAdapter.add("无可用行程");
                                itineraryAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            itineraryAdapter.clear();
                            itineraryAdapter.add("无可用行程");
                            itineraryAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        } else {
            itineraryAdapter.add("请先登录");
            itineraryAdapter.notifyDataSetChanged();
        }

        final ItineraryEntity[] selectedItinerary = { null };
        spinnerItinerary.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < itineraryList.size()) {
                    selectedItinerary[0] = itineraryList.get(position);

                    if (selectedItinerary[0].getDestination() != null
                            && !selectedItinerary[0].getDestination().isEmpty()) {
                        progressBarWeather.setVisibility(View.VISIBLE);
                        tvWeatherInfo.setVisibility(View.GONE);

                        String city = selectedItinerary[0].getDestination();
                        String startDate = selectedItinerary[0].getStartDate();
                        String endDate = selectedItinerary[0].getEndDate();

                        com.travelplanner.service.AMapService.getInstance().getWeatherForecast(
                                city, startDate, endDate,
                                new com.travelplanner.service.AMapService.WeatherCallback() {
                                    @Override
                                    public void onSuccess(double avgTemp, String weatherCondition,
                                            List<com.travelplanner.model.amap.AMapWeatherResponse.Cast> forecasts) {
                                        progressBarWeather.setVisibility(View.GONE);
                                        setTemperatureFromAvg(spinnerTemperature, avgTemp);
                                        setWeatherFromCondition(spinnerRain, weatherCondition);

                                        StringBuilder weatherInfo = new StringBuilder();
                                        weatherInfo.append("平均温度: ").append(String.format("%.1f", avgTemp))
                                                .append("°C\n");
                                        weatherInfo.append("天气状况: ").append(weatherCondition);
                                        tvWeatherInfo.setText(weatherInfo.toString());
                                        tvWeatherInfo.setVisibility(View.VISIBLE);

                                        if (selectedItinerary[0].getStyle() != null) {
                                            String style = selectedItinerary[0].getStyle();
                                            for (int i = 0; i < TRIP_TYPES.length; i++) {
                                                if (TRIP_TYPES[i].equals(style)) {
                                                    spinnerTripType.setSelection(i);
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(String message) {
                                        progressBarWeather.setVisibility(View.GONE);
                                        Toast.makeText(ChecklistActivity.this, "获取天气失败: " + message, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    }
                } else {
                    selectedItinerary[0] = null;
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedItinerary[0] = null;
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("AI智能生成清单")
                .setView(dialogView)
                .setPositiveButton("AI生成", null)
                .setNegativeButton("取消", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String tripType = (String) spinnerTripType.getSelectedItem();
                String temp = (String) spinnerTemperature.getSelectedItem();
                String rain = (String) spinnerRain.getSelectedItem();

                ItineraryEntity selectedIt = selectedItinerary[0];
                if (selectedIt != null) {
                    itinerary = new Itinerary();
                    itinerary.setId(selectedIt.getId());
                    itinerary.setTitle(selectedIt.getTitle());
                    itinerary.setDestination(selectedIt.getDestination());
                    itinerary.setStartDate(selectedIt.getStartDate());
                    itinerary.setEndDate(selectedIt.getEndDate());
                    itinerary.setDays(selectedIt.getDays());
                    itinerary.setPeopleCount(selectedIt.getPersonCount());
                    itinerary.setTravelStyle(selectedIt.getStyle());
                }

                if (!allItems.isEmpty()) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("生成新清单将清空当前清单，是否继续？")
                            .setPositiveButton("继续", (dialog1, which) -> {
                                dialog.dismiss();
                                generateWithAI(tripType, temp, rain);
                            })
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    dialog.dismiss();
                    generateWithAI(tripType, temp, rain);
                }
            });
        });
        dialog.show();
    }

    private void setTemperatureFromAvg(Spinner spinner, double avgTemp) {
        String[] tempOptions = { "寒冷(<10°C)", "凉爽(10-20°C)", "温暖(20-28°C)", "炎热(>28°C)" };
        int selection = 2;

        if (avgTemp < 10) {
            selection = 0;
        } else if (avgTemp < 20) {
            selection = 1;
        } else if (avgTemp < 28) {
            selection = 2;
        } else {
            selection = 3;
        }

        spinner.setSelection(selection);
    }

    private void setWeatherFromCondition(Spinner spinner, String weatherCondition) {
        String[] rainOptions = { "晴天", "多云", "小雨", "大雨" };
        int selection = 0;

        if (weatherCondition != null) {
            if (weatherCondition.contains("雨")) {
                if (weatherCondition.contains("大") || weatherCondition.contains("暴")) {
                    selection = 3;
                } else {
                    selection = 2;
                }
            } else if (weatherCondition.contains("云") || weatherCondition.contains("阴")) {
                selection = 1;
            } else {
                selection = 0;
            }
        }

        spinner.setSelection(selection);
    }

    private void autoFillFromItinerary(Spinner spinnerTemperature, Spinner spinnerRain) {
        if (itinerary == null || itinerary.getDayPlans() == null || itinerary.getDayPlans().isEmpty()) {
            return;
        }

        String weather = itinerary.getDayPlans().get(0).getWeather();
        if (weather == null) {
            return;
        }

        String tempSelection = "温暖(20-28°C)";
        String rainSelection = "晴天";

        if (weather.contains("雪") || weather.contains("冷")) {
            tempSelection = "寒冷(<10°C)";
        } else if (weather.contains("凉") || weather.contains("阴")) {
            tempSelection = "凉爽(10-20°C)";
        } else if (weather.contains("热") || weather.contains("晴")) {
            tempSelection = "炎热(>28°C)";
        }

        if (weather.contains("雨")) {
            rainSelection = weather.contains("大") ? "大雨" : "小雨";
        } else if (weather.contains("云")) {
            rainSelection = "多云";
        }

        String[] tempOptions = { "寒冷(<10°C)", "凉爽(10-20°C)", "温暖(20-28°C)", "炎热(>28°C)" };
        for (int i = 0; i < tempOptions.length; i++) {
            if (tempOptions[i].equals(tempSelection)) {
                spinnerTemperature.setSelection(i);
                break;
            }
        }

        String[] rainOptions = { "晴天", "多云", "小雨", "大雨" };
        for (int i = 0; i < rainOptions.length; i++) {
            if (rainOptions[i].equals(rainSelection)) {
                spinnerRain.setSelection(i);
                break;
            }
        }
    }

    private void generateWithAI(String tripType, String temp, String rain) {
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "正在调用DeepSeek AI生成清单...", Toast.LENGTH_SHORT).show();

        StringBuilder prompt = new StringBuilder();
        prompt.append("请为以下行程生成个性化的出行行李清单，以JSON格式返回，格式为：{\"items\": [{\"name\": \"物品名称\", \"category\": \"分类\"}]}\n\n");

        if (itinerary != null) {
            prompt.append("【行程信息】\n");
            prompt.append("行程标题: ").append(itinerary.getTitle() != null ? itinerary.getTitle() : "未命名行程").append("\n");
            prompt.append("目的地: ").append(itinerary.getDestination() != null ? itinerary.getDestination() : "未知")
                    .append("\n");
            prompt.append("行程天数: ").append(itinerary.getDays() != null ? itinerary.getDays() : 1).append("天\n");
            prompt.append("出行人数: ").append(itinerary.getPeopleCount() != null ? itinerary.getPeopleCount() : 1)
                    .append("人\n");
            prompt.append("旅行风格: ").append(itinerary.getTravelStyle() != null ? itinerary.getTravelStyle() : tripType)
                    .append("\n\n");

            if (itinerary.getDayPlans() != null && !itinerary.getDayPlans().isEmpty()) {
                prompt.append("【每日行程与天气】\n");
                for (Itinerary.DayPlan dayPlan : itinerary.getDayPlans()) {
                    prompt.append("第").append(dayPlan.getDayNumber()).append("天: ");
                    if (dayPlan.getSpots() != null && !dayPlan.getSpots().isEmpty()) {
                        prompt.append("景点: ");
                        for (Itinerary.SpotPlan spot : dayPlan.getSpots()) {
                            if (spot.getAttractionName() != null) {
                                prompt.append(spot.getAttractionName()).append("、");
                            }
                        }
                        if (prompt.length() > 0 && prompt.charAt(prompt.length() - 1) == '、') {
                            prompt.deleteCharAt(prompt.length() - 1);
                        }
                    }
                    if (dayPlan.getWeather() != null) {
                        prompt.append(" | 天气: ").append(dayPlan.getWeather());
                    }
                    prompt.append("\n");
                }
                prompt.append("\n");
            }
        }

        prompt.append("【用户选择】\n");
        prompt.append("出行类型: ").append(tripType).append("\n");
        prompt.append("温度范围: ").append(temp).append("\n");
        prompt.append("天气状况: ").append(rain).append("\n\n");

        prompt.append("请提供详细的行李清单，分类包括：衣物、洗漱用品、电子设备、证件文件、其他。");

        try {
            JsonObject requestJson = new JsonObject();
            requestJson.addProperty("model", "deepseek-chat");

            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", prompt.toString());

            requestJson.add("messages", gson.toJsonTree(Arrays.asList(message)));
            requestJson.addProperty("temperature", 0.7);
            requestJson.addProperty("max_tokens", 2000);

            RequestBody body = RequestBody.create(requestJson.toString(), JSON);
            Request request = new Request.Builder()
                    .url("https://api.deepseek.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer sk-bae04f8b40de465290471e774fda4da9")
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, java.io.IOException e) {
                    handler.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChecklistActivity.this, "网络请求失败，使用本地规则生成", Toast.LENGTH_LONG).show();
                        generateLocalList(tripType, temp, rain);
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws java.io.IOException {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    android.util.Log.d("ChecklistActivity", "AI Response: " + responseBody);
                    handler.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        boolean success = false;
                        try {
                            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                            if (jsonResponse.has("choices") && jsonResponse.getAsJsonArray("choices").size() > 0) {
                                String content = jsonResponse.getAsJsonArray("choices")
                                        .get(0).getAsJsonObject()
                                        .getAsJsonObject("message")
                                        .get("content").getAsString();
                                android.util.Log.d("ChecklistActivity", "AI Content: " + content);

                                int jsonStart = content.indexOf("{");
                                int jsonEnd = content.lastIndexOf("}");
                                if (jsonStart >= 0 && jsonEnd > jsonStart) {
                                    String jsonStr = content.substring(jsonStart, jsonEnd + 1);
                                    JsonObject itemsJson = gson.fromJson(jsonStr, JsonObject.class);
                                    if (itemsJson.has("items")) {
                                        List<Checklist> newItems = new ArrayList<>();
                                        JsonArray itemsArray = itemsJson.getAsJsonArray("items");
                                        for (int i = 0; i < itemsArray.size(); i++) {
                                            JsonObject itemJson = itemsArray.get(i).getAsJsonObject();
                                            Checklist checklist = new Checklist();
                                            checklist.setId((long) (System.currentTimeMillis() + i));
                                            checklist.setItineraryId(itineraryId);
                                            checklist.setItemName(itemJson.get("name").getAsString());
                                            checklist.setCategory(itemJson.get("category").getAsString());
                                            checklist.setIsChecked(false);
                                            newItems.add(checklist);
                                        }
                                        allItems.clear();
                                        allItems.addAll(newItems);
                                        filterItems();
                                        Toast.makeText(ChecklistActivity.this, "AI已生成 " + newItems.size() + " 项物品",
                                                Toast.LENGTH_SHORT).show();
                                        success = true;

                                        if (itinerary != null && itinerary.getId() != null) {
                                            selectedItineraryIdForSave = itinerary.getId();
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            android.util.Log.e("ChecklistActivity", "AI Parse Error", e);
                            e.printStackTrace();
                        }

                        if (!success) {
                            Toast.makeText(ChecklistActivity.this, "AI返回格式异常，使用本地规则生成", Toast.LENGTH_SHORT).show();
                            generateLocalList(tripType, temp, rain);
                        }
                    });
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "请求异常，使用本地规则生成", Toast.LENGTH_SHORT).show();
            generateLocalList(tripType, temp, rain);
        }
    }

    private void generateLocalList(String tripType, String temp, String rain) {
        List<Checklist> newItems = new ArrayList<>();

        List<String> clothes = new ArrayList<>();
        List<String> toiletries = Arrays.asList("牙刷", "牙膏", "毛巾", "洗面奶", "护肤品", "防晒霜");
        List<String> electronics = Arrays.asList("手机", "充电器", "充电宝", "耳机");
        List<String> documents = Arrays.asList("身份证", "银行卡", "现金");
        List<String> others = new ArrayList<>();

        if (temp.contains("寒冷")) {
            clothes.addAll(Arrays.asList("羽绒服", "毛衣", "秋裤", "厚外套", "围巾", "手套", "帽子"));
        } else if (temp.contains("凉爽")) {
            clothes.addAll(Arrays.asList("外套", "长袖T恤", "长裤"));
        } else if (temp.contains("温暖")) {
            clothes.addAll(Arrays.asList("短袖T恤", "衬衫", "薄外套", "长裤/短裤"));
        } else {
            clothes.addAll(Arrays.asList("短袖T恤", "短裤", "裙子", "遮阳帽", "太阳镜"));
        }

        if (rain.contains("小雨") || rain.contains("大雨")) {
            others.addAll(Arrays.asList("雨伞", "雨衣", "防水鞋"));
        }

        if (tripType.equals("商务")) {
            clothes.addAll(Arrays.asList("西装", "衬衫", "领带", "皮鞋"));
            electronics.add("笔记本电脑");
        } else if (tripType.equals("徒步")) {
            clothes.addAll(Arrays.asList("登山鞋", "速干衣", "登山杖", "护膝"));
            others.addAll(Arrays.asList("背包", "水壶", "手电筒", "创可贴"));
        } else if (tripType.equals("亲子")) {
            others.addAll(Arrays.asList("儿童推车", "儿童玩具", "零食", "奶粉", "尿不湿"));
        } else if (tripType.equals("海滩")) {
            clothes.addAll(Arrays.asList("泳衣", "泳裤", "沙滩裤", "拖鞋"));
            others.addAll(Arrays.asList("浴巾", "沙滩垫", "潜水镜", "防晒霜"));
        } else if (tripType.equals("滑雪")) {
            clothes.addAll(Arrays.asList("滑雪服", "滑雪裤", "滑雪镜", "头盔", "手套"));
            others.add("防滑链");
        }

        long id = System.currentTimeMillis();
        for (String item : clothes) {
            Checklist checklist = new Checklist();
            checklist.setId(id++);
            checklist.setItineraryId(itineraryId);
            checklist.setItemName(item);
            checklist.setCategory("衣物");
            checklist.setIsChecked(false);
            newItems.add(checklist);
        }

        for (String item : toiletries) {
            Checklist checklist = new Checklist();
            checklist.setId(id++);
            checklist.setItineraryId(itineraryId);
            checklist.setItemName(item);
            checklist.setCategory("洗漱用品");
            checklist.setIsChecked(false);
            newItems.add(checklist);
        }

        for (String item : electronics) {
            Checklist checklist = new Checklist();
            checklist.setId(id++);
            checklist.setItineraryId(itineraryId);
            checklist.setItemName(item);
            checklist.setCategory("电子设备");
            checklist.setIsChecked(false);
            newItems.add(checklist);
        }

        for (String item : documents) {
            Checklist checklist = new Checklist();
            checklist.setId(id++);
            checklist.setItineraryId(itineraryId);
            checklist.setItemName(item);
            checklist.setCategory("证件文件");
            checklist.setIsChecked(false);
            newItems.add(checklist);
        }

        for (String item : others) {
            Checklist checklist = new Checklist();
            checklist.setId(id++);
            checklist.setItineraryId(itineraryId);
            checklist.setItemName(item);
            checklist.setCategory("其他");
            checklist.setIsChecked(false);
            newItems.add(checklist);
        }

        allItems.clear();
        allItems.addAll(newItems);
        filterItems();

        Toast.makeText(this, "已生成 " + newItems.size() + " 项物品", Toast.LENGTH_SHORT).show();

        if (itinerary != null && itinerary.getId() != null) {
            selectedItineraryIdForSave = itinerary.getId();
        }
    }

    private void showAddItemDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_checklist_item, null);
        TextInputEditText etItemName = dialogView.findViewById(R.id.etItemName);
        TextInputEditText etCategory = dialogView.findViewById(R.id.etCategory);

        new AlertDialog.Builder(this)
                .setTitle("添加物品")
                .setView(dialogView)
                .setPositiveButton("添加", (dialog, which) -> {
                    String itemName = etItemName.getText() != null ? etItemName.getText().toString().trim() : "";
                    String category = etCategory.getText() != null ? etCategory.getText().toString().trim() : "其他";

                    if (TextUtils.isEmpty(itemName)) {
                        return;
                    }

                    Checklist item = new Checklist();
                    item.setId(System.currentTimeMillis());
                    item.setItineraryId(itineraryId);
                    item.setItemName(itemName);
                    item.setCategory(category);
                    item.setIsChecked(false);

                    allItems.add(item);
                    filterItems();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
