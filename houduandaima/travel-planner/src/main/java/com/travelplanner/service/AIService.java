package com.travelplanner.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {
//al封装,apl配置
    @Value("${travel.deepseek.api-key}")
    private String apiKey;

    @Value("${travel.deepseek.api-url}")
    private String apiUrl;

    @Value("${travel.deepseek.model}")
    private String model;

    private static final int MAX_RETRIES = 3;

    public String generateItineraryWithAI(String prompt) {
        String enhancedPrompt = buildEnhancedPrompt(prompt);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            System.out.println("=== AI 生成尝试 " + attempt + "/" + MAX_RETRIES + " ===");

            String response = callAI(enhancedPrompt);

            if (response == null || response.isEmpty() || response.contains("error")) {
                System.err.println("AI 调用失败: " + response);
                continue;
            }

            if (isValidItineraryJSON(response)) {
                System.out.println("=== AI 生成成功 ===");
                return response;
            } else {
                System.err.println("AI 返回格式错误，尝试 " + attempt + " 失败");

                if (attempt < MAX_RETRIES) {
                    enhancedPrompt = buildRetryPrompt(prompt, response, attempt);
                }
            }
        }

        System.err.println("=== AI 生成失败，降级处理 ===");
        return "{\"error\": \"AI服务暂时不可用，请稍后重试\"}";
    }

    private String buildEnhancedPrompt(String userRequest) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("你是旅行规划助手，根据需求生成JSON格式行程。\n\n");

        prompt.append("【JSON格式要求】\n");
        prompt.append("1. 返回纯JSON，{开头 }结尾，无额外文字\n");
        prompt.append("2. 必须包含days数组，每天2-5个景点\n");
        prompt.append("3. 每个景点必须字段：name, visitTime(HH:mm), leaveTime(HH:mm), duration(分钟)\n");
        prompt.append("   transportMode(步行/公交/地铁/打车), distanceToNext, ticketPrice(数字)\n");
        prompt.append("   estimatedCost, crowdLevel(很少/较少/一般/较多), bestVisitTime, trafficTips, notes\n");
        prompt.append("4. 每天必须有mealRecommendations(breakfast/lunch/dinner各含name/price/distance/notes)\n");
        prompt.append("5. 每天必须有estimatedDailyBudget\n\n");

        prompt.append("【行程原则】\n");
        prompt.append("- 热门景点建议8:00前或16:00后入园\n");
        prompt.append("- 景区内餐饮偏贵，预算合理分配\n");
        prompt.append("- 雨天自动安排室内景点\n\n");

        prompt.append("【用户需求】\n");
        prompt.append(userRequest);
        prompt.append("\n\n只返回JSON：");

        return prompt.toString();
    }

    private String buildRetryPrompt(String userRequest, String previousResponse, int attempt) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("你是一个专业的旅行规划助手。\n\n");
        prompt.append("【重要】上次返回格式错误，请严格按照以下要求重新生成JSON：\n\n");
        prompt.append("【必须字段】\n");
        prompt.append("1. 必须返回纯JSON格式，以 { 开头，以 } 结尾，不包含任何 ``` 或 ```json 标记\n");
        prompt.append("2. JSON 必须包含 \"days\" 数组\n");
        prompt.append("3. 每天必须有 mealRecommendations 餐饮推荐\n");
        prompt.append("4. 每天必须有 estimatedDailyBudget 当日预算\n");
        prompt.append("5. 每个 spot 必须包含全部字段：\n");
        prompt.append("   - name, visitTime, leaveTime, duration, transportMode\n");
        prompt.append("   - distanceToNext, ticketPrice, estimatedCost, crowdLevel\n");
        prompt.append("   - bestVisitTime, trafficTips, notes\n\n");

        prompt.append("【上次错误响应（仅用于参考格式问题）】\n");
        int len = Math.min(300, previousResponse.length());
        prompt.append(previousResponse.substring(0, len));
        prompt.append("\n\n【用户需求】\n");
        prompt.append(userRequest);
        prompt.append("\n\n只返回JSON，不要有任何解释：");

        return prompt.toString();
    }
//APL请求构建与发送
    private String callAI(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);

        JSONArray messages = new JSONArray();
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", "你是一个专业的旅行规划助手。");
        messages.add(systemMsg);

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.add(userMsg);

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1800);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = JSON.toJSONString(requestBody).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject jsonResponse = JSON.parseObject(response.toString());
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    if (choices != null && !choices.isEmpty()) {
                        JSONObject firstChoice = choices.getJSONObject(0);
                        JSONObject message = firstChoice.getJSONObject("message");
                        return message.getString("content");
                    }
                }
            }

            return "{\"error\": \"API调用失败，状态码: " + responseCode + "\"}";

        } catch (Exception e) {
            return "{\"error\": \"AI服务暂时不可用: " + e.getMessage() + "\"}";
        }
    }

    private boolean isValidItineraryJSON(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        String jsonStr = response.trim();

        if (jsonStr.startsWith("```json")) {
            jsonStr = jsonStr.substring(7);
        } else if (jsonStr.startsWith("```")) {
            jsonStr = jsonStr.substring(3);
        }
        if (jsonStr.endsWith("```")) {
            jsonStr = jsonStr.substring(0, jsonStr.length() - 3);
        }
        jsonStr = jsonStr.trim();

        int jsonStart = jsonStr.indexOf("{");
        int jsonEnd = jsonStr.lastIndexOf("}");
        if (jsonStart == -1 || jsonEnd == -1 || jsonEnd <= jsonStart) {
            System.err.println("JSON 格式错误：未找到 { 和 }");
            return false;
        }
        jsonStr = jsonStr.substring(jsonStart, jsonEnd + 1);

        try {
            JSONObject json = JSON.parseObject(jsonStr);

            if (!json.containsKey("days")) {
                System.err.println("JSON 格式错误：缺少 days 字段");
                return false;
            }

            JSONArray days = json.getJSONArray("days");
            if (days == null || days.isEmpty()) {
                System.err.println("JSON 格式错误：days 为空");
                return false;
            }

            for (int i = 0; i < days.size(); i++) {
                JSONObject day = days.getJSONObject(i);
                JSONArray spots = day.getJSONArray("spots");
                if (spots == null) {
                    System.err.println("JSON 格式错误：第 " + (i + 1) + " 天缺少 spots");
                    return false;
                }

                for (int j = 0; j < spots.size(); j++) {
                    JSONObject spot = spots.getJSONObject(j);
                    if (!spot.containsKey("name") || !spot.containsKey("visitTime")) {
                        System.err.println("JSON 格式错误：第 " + (i + 1) + " 天第 " + (j + 1) + " 个景点缺少必要字段");
                        return false;
                    }
                }
            }

            System.out.println("JSON 格式校验通过");
            return true;

        } catch (Exception e) {
            System.err.println("JSON 解析错误：" + e.getMessage());
            return false;
        }
    }

    public String chat(String message) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);

        JSONArray messages = new JSONArray();
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", "你是一个智能旅行助手，可以回答用户关于旅行规划、景点推荐、预算计算等问题。");
        messages.add(systemMsg);

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", message);
        messages.add(userMsg);

        requestBody.put("messages", messages);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = JSON.toJSONString(requestBody).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject jsonResponse = JSON.parseObject(response.toString());
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    if (choices != null && !choices.isEmpty()) {
                        JSONObject firstChoice = choices.getJSONObject(0);
                        JSONObject resultMsg = firstChoice.getJSONObject("message");
                        return resultMsg.getString("content");
                    }
                }
            }

            return "抱歉，AI服务暂时不可用。";

        } catch (Exception e) {
            return "抱歉，AI服务暂时不可用: " + e.getMessage();
        }
    }
}
