package com.travelplanner.ui.budget;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.BudgetInfo;
import com.travelplanner.util.HttpUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BudgetActivity extends AppCompatActivity {
    private TextView tvTotalBudget, tvTransportBudget, tvHotelBudget, tvFoodBudget, tvTicketBudget, tvOtherBudget;
    private MaterialButton btnEditBudget;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Long itineraryId;
    private BudgetInfo currentBudget;
    private boolean isBudgetLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        itineraryId = getIntent().getLongExtra("itinerary_id", 0);

        initViews();
        loadBudget();
    }

    private void initViews() {
        tvTotalBudget = findViewById(R.id.tv_total_budget);
        tvTransportBudget = findViewById(R.id.tv_transport_budget);
        tvHotelBudget = findViewById(R.id.tv_hotel_budget);
        tvFoodBudget = findViewById(R.id.tv_food_budget);
        tvTicketBudget = findViewById(R.id.tv_ticket_budget);
        tvOtherBudget = findViewById(R.id.tv_other_budget);
        btnEditBudget = findViewById(R.id.btn_edit_budget);
        btnEditBudget.setEnabled(false);

        btnEditBudget.setOnClickListener(v -> showEditDialog());
    }

    private void loadBudget() {
        HttpUtil.get("budget/" + itineraryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    Toast.makeText(BudgetActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    btnEditBudget.setEnabled(true);
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                System.out.println("Budget API response: " + body);
                ApiResponse<BudgetInfo> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<BudgetInfo>>(){}.getType());

                handler.post(() -> {
                    System.out.println("Budget API success: " + apiResponse.isSuccess() + ", data: " + apiResponse.getData());
                    if (apiResponse.isSuccess()) {
                        isBudgetLoaded = true;
                        btnEditBudget.setEnabled(true);
                        if (apiResponse.getData() != null) {
                            currentBudget = apiResponse.getData();
                            updateUI(currentBudget);
                        } else {
                            currentBudget = new BudgetInfo();
                        }
                    } else {
                        String message = apiResponse.getMessage() != null ? apiResponse.getMessage() : "未知错误";
                        Toast.makeText(BudgetActivity.this, "加载失败: " + message, Toast.LENGTH_SHORT).show();
                        btnEditBudget.setEnabled(true);
                    }
                });
            }
        });
    }

    private void updateUI(BudgetInfo budget) {
        tvTotalBudget.setText("¥" + getBudgetValue(budget.getTotalBudget()));
        tvTransportBudget.setText("¥" + getBudgetValue(budget.getTransportBudget()));
        tvHotelBudget.setText("¥" + getBudgetValue(budget.getHotelBudget()));
        tvFoodBudget.setText("¥" + getBudgetValue(budget.getFoodBudget()));
        tvTicketBudget.setText("¥" + getBudgetValue(budget.getTicketBudget()));
        tvOtherBudget.setText("¥" + getBudgetValue(budget.getOtherBudget()));
    }

    private String getBudgetValue(BigDecimal value) {
        return value != null ? value.toString() : "0";
    }

    private void showEditDialog() {
        if (!isBudgetLoaded) {
            Toast.makeText(this, "预算数据加载中，请稍候", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_budget, null);
        EditText etTotalBudget = dialogView.findViewById(R.id.et_total_budget);
        EditText etTransportBudget = dialogView.findViewById(R.id.et_transport_budget);
        EditText etHotelBudget = dialogView.findViewById(R.id.et_hotel_budget);
        EditText etFoodBudget = dialogView.findViewById(R.id.et_food_budget);
        EditText etTicketBudget = dialogView.findViewById(R.id.et_ticket_budget);
        EditText etOtherBudget = dialogView.findViewById(R.id.et_other_budget);

        etTotalBudget.setText(getBudgetValue(currentBudget.getTotalBudget()));
        etTransportBudget.setText(getBudgetValue(currentBudget.getTransportBudget()));
        etHotelBudget.setText(getBudgetValue(currentBudget.getHotelBudget()));
        etFoodBudget.setText(getBudgetValue(currentBudget.getFoodBudget()));
        etTicketBudget.setText(getBudgetValue(currentBudget.getTicketBudget()));
        etOtherBudget.setText(getBudgetValue(currentBudget.getOtherBudget()));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String totalStr = etTotalBudget.getText().toString().trim();
            String transportStr = etTransportBudget.getText().toString().trim();
            String hotelStr = etHotelBudget.getText().toString().trim();
            String foodStr = etFoodBudget.getText().toString().trim();
            String ticketStr = etTicketBudget.getText().toString().trim();
            String otherStr = etOtherBudget.getText().toString().trim();

            if (totalStr.isEmpty() || transportStr.isEmpty() || hotelStr.isEmpty() ||
                    foodStr.isEmpty() || ticketStr.isEmpty() || otherStr.isEmpty()) {
                Toast.makeText(this, "请填写所有预算项", Toast.LENGTH_SHORT).show();
                return;
            }

            updateBudget(totalStr, transportStr, hotelStr, foodStr, ticketStr, otherStr);
            dialog.dismiss();
        });

        dialog.show();
        
        android.view.Window window = dialog.getWindow();
        if (window != null) {
            android.view.WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            layoutParams.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }
    }

    private void updateBudget(String totalBudget, String transportBudget, String hotelBudget,
                              String foodBudget, String ticketBudget, String otherBudget) {
        Map<String, String> params = new HashMap<>();
        params.put("itineraryId", String.valueOf(itineraryId));
        params.put("totalBudget", totalBudget);
        params.put("transportBudget", transportBudget);
        params.put("hotelBudget", hotelBudget);
        params.put("foodBudget", foodBudget);
        params.put("ticketBudget", ticketBudget);
        params.put("otherBudget", otherBudget);

        HttpUtil.postForm("budget/update", params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> Toast.makeText(BudgetActivity.this, "网络错误", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                ApiResponse<Void> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Void>>(){}.getType());

                handler.post(() -> {
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(BudgetActivity.this, "预算更新成功", Toast.LENGTH_SHORT).show();
                        loadBudget();
                    } else {
                        String message = apiResponse.getMessage() != null ? apiResponse.getMessage() : "未知错误";
                        Toast.makeText(BudgetActivity.this, "更新失败: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
