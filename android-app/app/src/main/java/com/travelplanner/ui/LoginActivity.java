package com.travelplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
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
    private static final String TAG = "LoginActivity";
    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 开始创建 LoginActivity");

        // 设置窗口标志，确保内容不被状态栏遮挡
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
            Log.d(TAG, "onCreate: 窗口设置完成");
        } catch (Exception e) {
            Log.w(TAG, "onCreate: 窗口设置警告", e);
        }

        try {
            Log.d(TAG, "onCreate: 正在设置布局");
            setContentView(R.layout.activity_login);
            
            // 强制显示根布局
            View rootView = findViewById(android.R.id.content);
            if (rootView != null) {
                Log.d(TAG, "onCreate: 根视图获取成功");
            }
            
            Log.d(TAG, "onCreate: 布局设置成功");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: 设置布局失败", e);
            Toast.makeText(this, "页面加载错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            boolean loggedIn = TravelPlannerApp.getInstance().isLoggedIn();
            Long userId = TravelPlannerApp.getInstance().getUserId();
            Log.d(TAG, "onCreate: 检查登录状态, isLoggedIn=" + loggedIn + ", userId=" + userId);

            // 调试模式：强制清除登录状态以显示登录页面
            if (loggedIn) {
                Log.w(TAG, "onCreate: 检测到旧登录状态, 清除并重新登录");
                TravelPlannerApp.getInstance().logout();
                Log.d(TAG, "onCreate: 已清除登录状态, 显示登录页面");
            }

            initViews();
            Log.d(TAG, "onCreate: 视图初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: 初始化出错", e);
            Toast.makeText(this, "初始化错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        Log.d(TAG, "initViews: 开始初始化控件");
        try {
            etUsername = findViewById(R.id.et_username);
            etPassword = findViewById(R.id.et_password);
            btnLogin = findViewById(R.id.btn_login);
            btnRegister = findViewById(R.id.btn_register);

            Log.d(TAG, "initViews: 控件查找成功, 设置监听器");
            btnLogin.setOnClickListener(v -> doLogin());
            btnRegister.setOnClickListener(v -> {
                Log.d(TAG, "initViews: 点击注册按钮");
                startActivity(new Intent(this, RegisterActivity.class));
            });
            // 从注册页带回账号密码
            Intent intent = getIntent();
            if (intent != null) {
                String regUsername = intent.getStringExtra("username");
                String regPassword = intent.getStringExtra("password");
                if (regUsername != null) etUsername.setText(regUsername);
                if (regPassword != null) etPassword.setText(regPassword);
            }

            Log.d(TAG, "initViews: 初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "initViews: 初始化控件失败", e);
            Toast.makeText(this, "UI 初始化错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
                    String message = apiResponse.getMessage() != null ? apiResponse.getMessage() : "登录失败";

// 正确的 Toast 写法
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    handler.post(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void goToMain() {
        Log.d(TAG, "goToMain: 准备跳转到 MainActivity");
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Log.d(TAG, "goToMain: 跳转成功");
            finish();
        } catch (Exception e) {
            Log.e(TAG, "goToMain: 跳转失败", e);
            Toast.makeText(this, "跳转错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
