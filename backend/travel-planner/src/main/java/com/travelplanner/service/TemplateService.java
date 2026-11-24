package com.travelplanner.service;

import com.travelplanner.dto.TemplateDTO;
import com.travelplanner.entity.Template;
import com.travelplanner.mapper.TemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    public List<TemplateDTO> getTemplates(String style, String destination, Integer days) {
        List<Template> templates;
        
        if (style != null && !style.isEmpty()) {
            templates = templateMapper.findByStyle(style);
        } else if (destination != null && !destination.isEmpty()) {
            templates = templateMapper.findByDestination(destination);
        } else {
            templates = templateMapper.findHotTemplates(100);
        }

        if (days != null) {
            templates = templates.stream()
                    .filter(t -> t.getDays().equals(days))
                    .collect(Collectors.toList());
        }

        return templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TemplateDTO> getHotTemplates(Integer limit) {
        List<Template> templates = templateMapper.findHotTemplates(limit != null ? limit : 10);
        return templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public com.travelplanner.entity.Itinerary getTemplateById(Long id) {
        Template template = templateMapper.findById(id);
        if (template == null) {
            return null;
        }
        
        com.travelplanner.entity.Itinerary itinerary = new com.travelplanner.entity.Itinerary();
        itinerary.setId(template.getId());
        itinerary.setTitle(template.getTitle());
        itinerary.setDestination(template.getDestination());
        itinerary.setDays(template.getDays());
        itinerary.setPeopleCount(2);
        itinerary.setBudget(template.getBudget());
        itinerary.setTravelStyle(template.getStyle());
        itinerary.setStatus("template");
        
        return itinerary;
    }

    private TemplateDTO convertToDTO(Template template) {
        TemplateDTO dto = new TemplateDTO();
        dto.setId(template.getId());
        dto.setTitle(template.getTitle());
        dto.setDestination(template.getDestination());
        dto.setDays(template.getDays());
        dto.setStyle(template.getStyle());
        dto.setCrowd(template.getCrowd());
        dto.setDescription(template.getDescription());
        dto.setCoverImage(template.getCoverImage());
        dto.setAuthorName(template.getAuthorName());
        dto.setUsageCount(template.getUsageCount());
        dto.setViewCount(template.getUsageCount() * 4);
        dto.setBudget(template.getBudget());
        dto.setIsOfficial(template.getIsOfficial());
        dto.setIsHot(template.getIsHot());
        return dto;
    }
}
