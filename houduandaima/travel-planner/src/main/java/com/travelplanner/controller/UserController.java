package com.travelplanner.controller;

import com.travelplanner.dto.*;
import com.travelplanner.entity.User;
import com.travelplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request.getUsername(), request.getPassword(),
                    request.getPhone(), request.getEmail());
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("username", user.getUsername());
            return ApiResponse.success(data);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("username", user.getUsername());
            data.put("memberLevel", user.getMemberLevel());
            return ApiResponse.success(data);
        } catch (Exception e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(null);
            return ApiResponse.success(user);
        }
        return ApiResponse.error(404, "用户不存在");
    }
}
