package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.TemplateDTO;
import com.travelplanner.entity.Itinerary;
import com.travelplanner.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping(value = "/hot", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<List<TemplateDTO>> getHotTemplates(@RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.success(templateService.getHotTemplates(limit));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<List<TemplateDTO>> getTemplates(
            @RequestParam(required = false) String style,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Integer days) {
        return ApiResponse.success(templateService.getTemplates(style, destination, days));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ApiResponse<Itinerary> getTemplateById(@PathVariable Long id) {
        Itinerary template = templateService.getTemplateById(id);
        if (template == null) {
            return ApiResponse.error(404, "模板不存在");
        }
        return ApiResponse.success(template);
    }
}
