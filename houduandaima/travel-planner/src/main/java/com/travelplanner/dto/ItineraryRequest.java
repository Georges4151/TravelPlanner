package com.travelplanner.dto;

import lombok.Data;
import java.util.List;

@Data
public class ItineraryRequest   //是一个数据传输对象（DTO - Data Transfer Object），用于封装前端发送给后端的行程生成请求参数。
{
    private Long userId;  //用户id
    private String origin;//出发地
    private String destination;//目的地
    private Integer days;//行程天数
    private Integer peopleCount;// 人数
    private String budget;// 预算
    private String travelStyle;// 旅行风格
    private List<String> tabooNeeds;// 旅行风格
    private String startDate;// 开始日期
}
