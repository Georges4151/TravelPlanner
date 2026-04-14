package com.travelplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.User;
import com.travelplanner.util.HttpUtil;

import java.io.IOException;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (TravelPlannerApp.getInstance().isLoggedIn()) {
            goToMain();
            return;
        }

        initViews();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(v -> doLogin());
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        HttpUtil.post("user/login", json, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                handler.post(() -> Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String body = response.body().string();
                ApiResponse<Map<String, Object>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Map<String, Object>>>(){}.getType());

                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    Map<String, Object> data = apiResponse.getData();
                    Long userId = ((Number) data.get("id")).longValue();
                    String name = (String) data.get("username");
                    Integer level = data.get("memberLevel") != null ? ((Number) data.get("memberLevel")).intValue() : 0;

                    TravelPlannerApp.getInstance().saveUserId(userId);
                    TravelPlannerApp.getInstance().saveUsername(name);
                    TravelPlannerApp.getInstance().saveMemberLevel(level);

                    handler.post(() -> {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        goToMain();
                    });
                } else {
                    handler.post(() -> Toast.makeText(LoginActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
