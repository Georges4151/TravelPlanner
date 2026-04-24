package com.travelplanner.controller;

import com.travelplanner.dto.AIRequest;
import com.travelplanner.dto.ApiResponse;
import com.travelplanner.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate")
    public ApiResponse<String> generateItinerary(@RequestBody AIRequest request) {
        try {
            String result = aiService.generateItineraryWithAI(request.getPrompt());
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(500, "AI服务暂时不可用");
        }
    }

    @PostMapping("/chat")
    public ApiResponse<String> chat(@RequestBody AIRequest request) {
        try {
            String result = aiService.chat(request.getPrompt());
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(500, "AI服务暂时不可用");
        }
    }
}
