package com.travelplanner.ui.info;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Hotel;
import com.travelplanner.model.Restaurant;
import com.travelplanner.model.Transport;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    private RecyclerView rvInfo;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        city = getIntent().getStringExtra("city");
        rvInfo = findViewById(R.id.rv_info);
        rvInfo.setLayoutManager(new LinearLayoutManager(this));

        loadInfo();
    }

    private void loadInfo() {
        List<Object> allInfo = new ArrayList<>();

        HttpUtil.get("info/hotel/" + city, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {}

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<List<Hotel>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<List<Hotel>>>(){}.getType());
                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    allInfo.addAll(apiResponse.getData());
                }
            }
        });

        HttpUtil.get("info/restaurant/" + city, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {}

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<List<Restaurant>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<List<Restaurant>>>(){}.getType());
                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    allInfo.addAll(apiResponse.getData());
                }
                handler.post(() -> Toast.makeText(InfoActivity.this, "加载了" + allInfo.size() + "条信息", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
