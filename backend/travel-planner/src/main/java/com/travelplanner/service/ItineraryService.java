package com.travelplanner.service;

import com.travelplanner.dto.*;
import com.travelplanner.dto.AttractionSearchResult;
import com.travelplanner.dto.OffpeakRecommendationResponse;
import com.travelplanner.entity.*;
import com.travelplanner.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryMapper itineraryMapper;

    @Autowired
    private ItineraryDayMapper itineraryDayMapper;

    @Autowired
    private ItinerarySpotMapper itinerarySpotMapper;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private AttractionMapper attractionMapper;

    @Autowired
    private ExternalApiService externalApiService;

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private AIService aiService;

    @Autowired
    private CrowdPredictionService crowdPredictionService;

    @Autowired
    private AttractionSortService attractionSortService;

    @Autowired
    private ItineraryTimelineService timelineService;

    @Autowired
    private TemplateDayMapper templateDayMapper;

    @Autowired
    private TemplateSpotMapper templateSpotMapper;
  //AL异步生成
    public Long startGenerateItinerary(ItineraryRequest request)  //定义开始生成行程的方法，接受请求参数，返回行程id
    {
        Itinerary itinerary = new Itinerary();
        itinerary.setUserId(request.getUserId());
        itinerary.setDestination(request.getDestination());
        itinerary.setDays(request.getDays());
        itinerary.setPeopleCount(request.getPeopleCount());
        itinerary.setBudget(new BigDecimal(request.getBudget()));
        itinerary.setTravelStyle(request.getTravelStyle());
        itinerary.setStatus("draft");
        itinerary.setSource("ai");
        itinerary.setGenerateStatus("generating");
        itinerary.setGenerateMessage("正在初始化...");

        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            LocalDate startDate = LocalDate.parse(request.getStartDate());
            itinerary.setStartDate(startDate.atStartOfDay());
            itinerary.setEndDate(startDate.plusDays(request.getDays() - 1).atStartOfDay());
        }

        String title = request.getDays() + "天" + request.getDestination() + "错峰之旅";
        itinerary.setTitle(title);

        itineraryMapper.insert(itinerary);

        final Long itineraryId = itinerary.getId();
        final BigDecimal totalBudget = new BigDecimal(request.getBudget());
        Budget budget = new Budget();
        budget.setItineraryId(itineraryId);
        budget.setTotalBudget(totalBudget);
        budget.setTransportBudget(totalBudget.multiply(new BigDecimal("0.3")));
        budget.setHotelBudget(totalBudget.multiply(new BigDecimal("0.35")));
        budget.setFoodBudget(totalBudget.multiply(new BigDecimal("0.2")));
        budget.setTicketBudget(totalBudget.multiply(new BigDecimal("0.1")));
        budget.setOtherBudget(totalBudget.multiply(new BigDecimal("0.05")));
        budgetMapper.insert(budget);

        final Long finalItineraryId = itineraryId;
        final ItineraryRequest finalRequest = request;

        new Thread(() -> {
            try {
                updateGenerateStatus(itineraryId, "generating", "正在调用AI生成行程...");

                String prompt = buildAIPrompt(finalRequest);
                System.out.println("=== AI Prompt ===");
                System.out.println(prompt);

                updateGenerateStatus(itineraryId, "generating", "AI正在分析您的需求...");

                String aiResponse = aiService.generateItineraryWithAI(prompt);
                System.out.println("=== AI Response ===");
                System.out.println(aiResponse);

                if (aiResponse == null || aiResponse.isEmpty() || aiResponse.contains("error")) {
                    System.err.println("AI响应异常: " + aiResponse);
                    updateGenerateStatus(itineraryId, "failed", "AI服务暂时不可用");
                    return;
                }

                updateGenerateStatus(itineraryId, "generating", "正在解析行程数据...");

                List<DayPlan> aiDayPlans = parseAIResponse(aiResponse, finalRequest.getDays());

                updateGenerateStatus(itineraryId, "generating", "正在保存行程详情...");

                for (DayPlan dayPlan : aiDayPlans) {
                    ItineraryDay itineraryDay = new ItineraryDay();
                    itineraryDay.setItineraryId(itineraryId);
                    itineraryDay.setDayNumber(dayPlan.getDayNumber());
                    itineraryDay.setDate(dayPlan.getDate());
                    itineraryDay.setWeather(dayPlan.getWeather());
                    itineraryDay.setTips(dayPlan.getTips());
                    itineraryDayMapper.insert(itineraryDay);

                    for (SpotPlan spot : dayPlan.getSpots()) {
                        ItinerarySpot itinerarySpot = new ItinerarySpot();
                        itinerarySpot.setItineraryDayId(itineraryDay.getId());
                        itinerarySpot.setAttractionName(spot.getName());
                        itinerarySpot.setOrderNum(dayPlan.getSpots().indexOf(spot) + 1);
                        itinerarySpot.setArrivalTime(spot.getVisitTime());
                        itinerarySpot.setStayDuration(spot.getDuration());
                        itinerarySpot.setTransportToNext(spot.getTransportMode());
                        itinerarySpot.setNotes(spot.getNotes());
                        itinerarySpotMapper.insert(itinerarySpot);
                    }
                }

                updateGenerateStatus(itineraryId, "completed", "行程生成成功");

            } catch (Exception e) {
                System.err.println("生成行程失败: " + e.getMessage());
                e.printStackTrace();
                updateGenerateStatus(itineraryId, "failed", "生成失败: " + e.getMessage());
            }
        }).start();

        return itineraryId;
    }

    private void updateGenerateStatus(Long itineraryId, String status, String message) {
        Itinerary itinerary = itineraryMapper.findById(itineraryId);
        if (itinerary != null) {
            itinerary.setGenerateStatus(status);
            itinerary.setGenerateMessage(message);
            itineraryMapper.update(itinerary);
        }
    }
    //al同步生成
    public ItineraryResponse generateItinerary(ItineraryRequest request) {
        Itinerary itinerary = new Itinerary();
        itinerary.setUserId(request.getUserId());
        itinerary.setDestination(request.getDestination());
        itinerary.setDays(request.getDays());
        itinerary.setPeopleCount(request.getPeopleCount());
        itinerary.setBudget(new BigDecimal(request.getBudget()));
        itinerary.setTravelStyle(request.getTravelStyle());
        itinerary.setStatus("draft");
        itinerary.setSource("ai");

        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            LocalDate startDate = LocalDate.parse(request.getStartDate());
            itinerary.setStartDate(startDate.atStartOfDay());
            itinerary.setEndDate(startDate.plusDays(request.getDays() - 1).atStartOfDay());
        }

        String title = request.getDays() + "天" + request.getDestination() + "错峰之旅";
        itinerary.setTitle(title);

        itineraryMapper.insert(itinerary);

        BigDecimal totalBudget = new BigDecimal(request.getBudget());
        Budget budget = new Budget();
        budget.setItineraryId(itinerary.getId());
        budget.setTotalBudget(totalBudget);
        budget.setTransportBudget(totalBudget.multiply(new BigDecimal("0.3")));
        budget.setHotelBudget(totalBudget.multiply(new BigDecimal("0.35")));
        budget.setFoodBudget(totalBudget.multiply(new BigDecimal("0.2")));
        budget.setTicketBudget(totalBudget.multiply(new BigDecimal("0.1")));
        budget.setOtherBudget(totalBudget.multiply(new BigDecimal("0.05")));
        budgetMapper.insert(budget);

        List<DayPlan> dayPlans = new ArrayList<>();

        String prompt = buildAIPrompt(request);
        System.out.println("=== AI Prompt ===");
        System.out.println(prompt);

        String aiResponse = aiService.generateItineraryWithAI(prompt);
        System.out.println("=== AI Response ===");
        System.out.println(aiResponse);
        System.out.println("=== AI Response Length ===");
        System.out.println(aiResponse != null ? aiResponse.length() : "null");

        if (aiResponse == null || aiResponse.isEmpty() || aiResponse.contains("error")) {
            System.err.println("AI响应异常: " + aiResponse);
            throw new RuntimeException("AI服务暂时不可用，请稍后重试");
        }

        List<DayPlan> aiDayPlans = parseAIResponse(aiResponse, request.getDays());

        for (DayPlan dayPlan : aiDayPlans) {
            ItineraryDay itineraryDay = new ItineraryDay();
            itineraryDay.setItineraryId(itinerary.getId());
            itineraryDay.setDayNumber(dayPlan.getDayNumber());
            itineraryDay.setDate(dayPlan.getDate());
            itineraryDay.setWeather(dayPlan.getWeather());
            itineraryDay.setTips(dayPlan.getTips());
            itineraryDayMapper.insert(itineraryDay);

            for (SpotPlan spot : dayPlan.getSpots()) {
                ItinerarySpot itinerarySpot = new ItinerarySpot();
                itinerarySpot.setItineraryDayId(itineraryDay.getId());
                itinerarySpot.setAttractionName(spot.getName());
                itinerarySpot.setOrderNum(dayPlan.getSpots().indexOf(spot) + 1);
                itinerarySpot.setArrivalTime(spot.getVisitTime());
                itinerarySpot.setStayDuration(spot.getDuration());
                itinerarySpot.setTransportToNext(spot.getTransportMode());
                itinerarySpot.setNotes(spot.getNotes());
                itinerarySpotMapper.insert(itinerarySpot);
            }

            dayPlans.add(dayPlan);
        }

        ItineraryResponse response = new ItineraryResponse();
        response.setId(itinerary.getId());
        response.setTitle(itinerary.getTitle());
        response.setDestination(itinerary.getDestination());
        response.setDays(itinerary.getDays());
        response.setPeopleCount(itinerary.getPeopleCount());
        response.setBudget(itinerary.getBudget());
        response.setTravelStyle(itinerary.getTravelStyle());
        response.setStatus(itinerary.getStatus());
        response.setDayPlans(dayPlans);

        return response;
    }

    private String buildAIPrompt(ItineraryRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请为我生成一个").append(request.getDays()).append("天的").append(request.getDestination()).append("错峰旅行行程。\n\n");
        prompt.append("需求详情：\n");
        prompt.append("- 目的地：").append(request.getDestination()).append("\n");
        prompt.append("- 行程天数：").append(request.getDays()).append("天\n");
        prompt.append("- 人数：").append(request.getPeopleCount()).append("人\n");
        prompt.append("- 预算：").append(request.getBudget()).append("元\n");
        prompt.append("- 旅行风格：").append(request.getTravelStyle()).append("\n");

        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            prompt.append("- 出发日期：").append(request.getStartDate()).append("\n");
        }

        if (request.getTabooNeeds() != null && !request.getTabooNeeds().isEmpty()) {
            prompt.append("- 禁忌需求：").append(String.join("、", request.getTabooNeeds())).append("\n");
        }

        prompt.append("\n请按照以下JSON格式返回行程：\n");
        prompt.append("{\n");
        prompt.append("  \"days\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"day\": 1,\n");
        prompt.append("      \"date\": \"2024-01-01\",\n");
        prompt.append("      \"weather\": \"晴天\",\n");
        prompt.append("      \"tips\": \"错峰出行建议\",\n");
        prompt.append("      \"spots\": [\n");
        prompt.append("        {\n");
        prompt.append("          \"name\": \"景点名称\",\n");
        prompt.append("          \"type\": \"景点类型\",\n");
        prompt.append("          \"visitTime\": \"09:00\",\n");
        prompt.append("          \"duration\": 120,\n");
        prompt.append("          \"transportMode\": \"步行\",\n");
        prompt.append("          \"ticketPrice\": 0,\n");
        prompt.append("          \"notes\": \"备注信息\"\n");
        prompt.append("        }\n");
        prompt.append("      ]\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}\n\n");
        prompt.append("请确保：\n");
        prompt.append("1. 每天安排3-4个景点\n");
        prompt.append("2. 景点要符合错峰出行的特点，避开人流高峰\n");
        prompt.append("3. 景点要是").append(request.getDestination()).append("当地的真实景点\n");
        prompt.append("4. 考虑天气因素，雨天推荐室内景点\n");
        prompt.append("5. 合理安排游览时间，避免过于紧张\n");

        return prompt.toString();
    }

    private List<DayPlan> parseAIResponse(String aiResponse, int days) {
        List<DayPlan> dayPlans = new ArrayList<>();

        try {
            String jsonStr = aiResponse.trim();

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
            if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                jsonStr = jsonStr.substring(jsonStart, jsonEnd + 1);
            }

            com.alibaba.fastjson2.JSONObject json = com.alibaba.fastjson2.JSON.parseObject(jsonStr);
            com.alibaba.fastjson2.JSONArray daysArray = json.getJSONArray("days");

            if (daysArray != null) {
                for (int i = 0; i < daysArray.size(); i++) {
                    com.alibaba.fastjson2.JSONObject dayJson = daysArray.getJSONObject(i);
                    DayPlan dayPlan = new DayPlan();
                    dayPlan.setDayNumber(dayJson.getIntValue("day"));
                    dayPlan.setDate(dayJson.getString("date"));
                    dayPlan.setWeather(dayJson.getString("weather"));
                    dayPlan.setTips(dayJson.getString("tips"));

                    List<SpotPlan> spots = new ArrayList<>();
                    com.alibaba.fastjson2.JSONArray spotsArray = dayJson.getJSONArray("spots");

                    if (spotsArray != null) {
                        for (int j = 0; j < spotsArray.size(); j++) {
                            com.alibaba.fastjson2.JSONObject spotJson = spotsArray.getJSONObject(j);
                            SpotPlan spot = new SpotPlan();
                            spot.setName(spotJson.getString("name"));
                            spot.setType(spotJson.getString("type"));
                            spot.setVisitTime(spotJson.getString("visitTime"));
                            spot.setDuration(spotJson.getIntValue("duration"));
                            spot.setTransportMode(spotJson.getString("transportMode"));
                            spot.setTicketPrice(spotJson.getBigDecimal("ticketPrice"));
                            spot.setNotes(spotJson.getString("notes"));
                            spots.add(spot);
                        }
                    }

                    dayPlan.setSpots(spots);
                    dayPlans.add(dayPlan);
                }
            }
        } catch (Exception e) {
            System.err.println("解析AI响应失败: " + e.getMessage());
            e.printStackTrace();

            for (int i = 1; i <= days; i++) {
                DayPlan dayPlan = new DayPlan();
                dayPlan.setDayNumber(i);
                dayPlan.setDate(LocalDate.now().plusDays(i - 1).format(DateTimeFormatter.ISO_DATE));
                dayPlan.setWeather("晴天");
                dayPlan.setTips("AI生成失败，请手动调整行程");
                dayPlan.setSpots(new ArrayList<>());
                dayPlans.add(dayPlan);
            }
        }

        return dayPlans;
    }

    public Itinerary getItineraryByIdSimple(Long id) {
        return itineraryMapper.findById(id);
    }

    public ItineraryResponse getItineraryById(Long id) {
       //查第1张表：itineraries（行程主表）
        Itinerary itinerary = itineraryMapper.findById(id);
        if (itinerary == null) {
            throw new RuntimeException("行程不存在");
        }
   //拿到：标题、目的地、天数、预算、风格、状态...
        ItineraryResponse response = new ItineraryResponse();
        response.setId(itinerary.getId());
        response.setTitle(itinerary.getTitle());
        response.setDestination(itinerary.getDestination());
        response.setDays(itinerary.getDays());
        response.setPeopleCount(itinerary.getPeopleCount());
        response.setBudget(itinerary.getBudget());
        response.setTravelStyle(itinerary.getTravelStyle());
        response.setStatus(itinerary.getStatus());

        // ② 查第2张表：itinerary_days（每日计划表）
        List<ItineraryDay> days = itineraryDayMapper.findByItineraryId(id);
        List<DayPlan> dayPlans = new ArrayList<>();
        // 拿到：第1天、第2天、第3天的 日期/天气/提示
        for (ItineraryDay day : days) {
            DayPlan dayPlan = new DayPlan();
            dayPlan.setDayNumber(day.getDayNumber());
            dayPlan.setDate(day.getDate());
            dayPlan.setWeather(day.getWeather());
            dayPlan.setTips(day.getTips());
// 查第3张表：itinerary_spots（景点表）
            List<ItinerarySpot> spots = itinerarySpotMapper.findByItineraryDayId(day.getId());
            List<SpotPlan> spotPlans = new ArrayList<>();
// 拿到：景点名、到达时间、停留时长、交通方式、备注
            for (ItinerarySpot spot : spots) {
                SpotPlan spotPlan = new SpotPlan();
                spotPlan.setAttractionId(spot.getAttractionId());
                spotPlan.setAttractionName(spot.getAttractionName());
                spotPlan.setArrivalTime(spot.getArrivalTime());
                spotPlan.setLeaveTime(spot.getLeaveTime());
                spotPlan.setStayDuration(spot.getStayDuration());
                spotPlan.setTransportToNext(spot.getTransportToNext());
                spotPlan.setNotes(spot.getNotes());
                spotPlans.add(spotPlan);
            }

            dayPlan.setSpots(spotPlans);
            dayPlans.add(dayPlan);
        }

        response.setDayPlans(dayPlans);
        //查第4张表：budgets（预算表）
        Budget budget = budgetMapper.findByItineraryId(id);
        // 拿到：交通30%、住宿35%、餐饮20%、门票10%、其他5%
        if (budget != null) {
            BudgetInfo budgetInfo = new BudgetInfo();
            budgetInfo.setTotalBudget(budget.getTotalBudget());
            budgetInfo.setTransportBudget(budget.getTransportBudget());
            budgetInfo.setHotelBudget(budget.getHotelBudget());
            budgetInfo.setFoodBudget(budget.getFoodBudget());
            budgetInfo.setTicketBudget(budget.getTicketBudget());
            budgetInfo.setOtherBudget(budget.getOtherBudget());
            response.setBudgetInfo(budgetInfo);
        }

        return response;//返回排版信息
    }

    public List<Itinerary> getUserItineraries(Long userId) {
        return itineraryMapper.findByUserId(userId);
    }

    public ItineraryResponse createFromTemplate(Long templateId, Long userId) {
        Template template = templateMapper.findById(templateId);
        if (template == null) {
            throw new RuntimeException("模板不存在");
        }

        templateMapper.incrementUsageCount(templateId);

        Itinerary itinerary = new Itinerary();
        itinerary.setUserId(userId);
        itinerary.setDestination(template.getDestination());
        itinerary.setDays(template.getDays());
        itinerary.setPeopleCount(1);
        itinerary.setBudget(template.getBudget() != null ? template.getBudget() : new BigDecimal("5000"));
        itinerary.setTravelStyle(template.getStyle());
        itinerary.setStatus("draft");
        itinerary.setSource("template");
        itinerary.setTitle(template.getTitle());

        itineraryMapper.insert(itinerary);

        BigDecimal totalBudget = template.getBudget() != null ? template.getBudget() : new BigDecimal("5000");
        Budget budget = new Budget();
        budget.setItineraryId(itinerary.getId());
        budget.setTotalBudget(totalBudget);
        budget.setTransportBudget(totalBudget.multiply(new BigDecimal("0.3")));
        budget.setHotelBudget(totalBudget.multiply(new BigDecimal("0.35")));
        budget.setFoodBudget(totalBudget.multiply(new BigDecimal("0.2")));
        budget.setTicketBudget(totalBudget.multiply(new BigDecimal("0.1")));
        budget.setOtherBudget(totalBudget.multiply(new BigDecimal("0.05")));
        budgetMapper.insert(budget);

        List<TemplateDay> templateDays = templateDayMapper.findByTemplateId(templateId);
        List<DayPlan> dayPlans = new ArrayList<>();

        for (TemplateDay templateDay : templateDays) {
            ItineraryDay itineraryDay = new ItineraryDay();
            itineraryDay.setItineraryId(itinerary.getId());
            itineraryDay.setDayNumber(templateDay.getDayNumber());
            itineraryDay.setDate(templateDay.getDate());
            itineraryDay.setWeather(templateDay.getWeather());
            itineraryDay.setTips(templateDay.getTips());
            itineraryDayMapper.insert(itineraryDay);

            List<TemplateSpot> templateSpots = templateSpotMapper.findByTemplateDayId(templateDay.getId());
            List<SpotPlan> spots = new ArrayList<>();

            for (TemplateSpot templateSpot : templateSpots) {
                ItinerarySpot itinerarySpot = new ItinerarySpot();
                itinerarySpot.setItineraryDayId(itineraryDay.getId());
                itinerarySpot.setAttractionName(templateSpot.getAttractionName());
                itinerarySpot.setOrderNum(templateSpot.getOrderNum());
                itinerarySpot.setArrivalTime(templateSpot.getArrivalTime());
                itinerarySpot.setLeaveTime(templateSpot.getLeaveTime());
                itinerarySpot.setStayDuration(templateSpot.getStayDuration());
                itinerarySpot.setTransportToNext(templateSpot.getTransportToNext());
                itinerarySpot.setNotes(templateSpot.getNotes());
                itinerarySpotMapper.insert(itinerarySpot);

                SpotPlan spotPlan = new SpotPlan();
                spotPlan.setAttractionName(templateSpot.getAttractionName());
                spotPlan.setArrivalTime(templateSpot.getArrivalTime());
                spotPlan.setLeaveTime(templateSpot.getLeaveTime());
                spotPlan.setStayDuration(templateSpot.getStayDuration());
                spotPlan.setTransportToNext(templateSpot.getTransportToNext());
                spotPlan.setNotes(templateSpot.getNotes());
                spots.add(spotPlan);
            }

            DayPlan dayPlan = new DayPlan();
            dayPlan.setDayNumber(templateDay.getDayNumber());
            dayPlan.setDate(templateDay.getDate());
            dayPlan.setWeather(templateDay.getWeather());
            dayPlan.setTips(templateDay.getTips());
            dayPlan.setSpots(spots);
            dayPlans.add(dayPlan);
        }

        ItineraryResponse response = new ItineraryResponse();
        response.setId(itinerary.getId());
        response.setTitle(itinerary.getTitle());
        response.setDestination(itinerary.getDestination());
        response.setDays(itinerary.getDays());
        response.setPeopleCount(itinerary.getPeopleCount());
        response.setBudget(itinerary.getBudget());
        response.setTravelStyle(itinerary.getTravelStyle());
        response.setStatus(itinerary.getStatus());
        response.setDayPlans(dayPlans);

        return response;
    }

    public void deleteItinerary(Long id) {
            List<ItineraryDay> days = itineraryDayMapper.findByItineraryId(id);
            for (ItineraryDay day : days) {
                itinerarySpotMapper.deleteByItineraryDayId(day.getId());
            }
            itineraryDayMapper.deleteByItineraryId(id);
            budgetMapper.deleteByItineraryId(id);
            itineraryMapper.deleteById(id);
        }

        public void deleteItineraryDays(Long id) {
            List<ItineraryDay> days = itineraryDayMapper.findByItineraryId(id);
            for (ItineraryDay day : days) {
                itinerarySpotMapper.deleteByItineraryDayId(day.getId());
            }
            itineraryDayMapper.deleteByItineraryId(id);
        }

        public ItineraryResponse regenerateItinerary(Long itineraryId, ItineraryRequest request) {
            Itinerary itinerary = itineraryMapper.findById(itineraryId);
            if (itinerary == null) {
                throw new RuntimeException("行程不存在");
            }

            budgetMapper.deleteByItineraryId(itineraryId);

            BigDecimal totalBudget = itinerary.getBudget();
            if (totalBudget == null) {
                totalBudget = new BigDecimal("5000");
            }

            Budget budget = new Budget();
            budget.setItineraryId(itineraryId);
            budget.setTotalBudget(totalBudget);
            budget.setTransportBudget(totalBudget.multiply(new BigDecimal("0.3")));
            budget.setHotelBudget(totalBudget.multiply(new BigDecimal("0.35")));
            budget.setFoodBudget(totalBudget.multiply(new BigDecimal("0.2")));
            budget.setTicketBudget(totalBudget.multiply(new BigDecimal("0.1")));
            budget.setOtherBudget(totalBudget.multiply(new BigDecimal("0.05")));
            budgetMapper.insert(budget);

            List<DayPlan> dayPlans = new ArrayList<>();

            String prompt = buildAIPrompt(request);
            System.out.println("=== 重新生成 AI Prompt ===");
            System.out.println(prompt);

            String aiResponse = aiService.generateItineraryWithAI(prompt);
            System.out.println("=== 重新生成 AI Response ===");
            System.out.println(aiResponse);

            if (aiResponse == null || aiResponse.isEmpty() || aiResponse.contains("error")) {
                System.err.println("AI响应异常: " + aiResponse);
                throw new RuntimeException("AI服务暂时不可用，请稍后重试");
            }

            List<DayPlan> aiDayPlans = parseAIResponse(aiResponse, request.getDays());

            for (DayPlan dayPlan : aiDayPlans) {
                ItineraryDay itineraryDay = new ItineraryDay();
                itineraryDay.setItineraryId(itinerary.getId());
                itineraryDay.setDayNumber(dayPlan.getDayNumber());
                itineraryDay.setDate(dayPlan.getDate());
                itineraryDay.setWeather(dayPlan.getWeather());
                itineraryDay.setTips(dayPlan.getTips());
                itineraryDayMapper.insert(itineraryDay);

                for (SpotPlan spot : dayPlan.getSpots()) {
                    ItinerarySpot itinerarySpot = new ItinerarySpot();
                    itinerarySpot.setItineraryDayId(itineraryDay.getId());
                    itinerarySpot.setAttractionName(spot.getName());
                    itinerarySpot.setOrderNum(dayPlan.getSpots().indexOf(spot) + 1);
                    itinerarySpot.setArrivalTime(spot.getVisitTime());
                    itinerarySpot.setStayDuration(spot.getDuration());
                    itinerarySpot.setTransportToNext(spot.getTransportMode());
                    itinerarySpot.setNotes(spot.getNotes());
                    itinerarySpotMapper.insert(itinerarySpot);
                }

                dayPlans.add(dayPlan);
            }

            ItineraryResponse response = new ItineraryResponse();
            response.setId(itinerary.getId());
            response.setTitle(itinerary.getTitle());
            response.setDestination(itinerary.getDestination());
            response.setDays(itinerary.getDays());
            response.setPeopleCount(itinerary.getPeopleCount());
            response.setBudget(itinerary.getBudget());
            response.setTravelStyle(itinerary.getTravelStyle());
            response.setStatus(itinerary.getStatus());
            response.setDayPlans(dayPlans);

            return response;
        }

    private List<Attraction> getAttractionsFromApi(String city) {
        try {
            List<AttractionSearchResult> searchResults = externalApiService.searchAttractions(city, "风景区", null);

            if (searchResults == null || searchResults.isEmpty()) {
                return null;
            }

            List<Attraction> attractions = new ArrayList<>();
            long id = 1000L;

            for (AttractionSearchResult result : searchResults) {
                Attraction attraction = new Attraction();
                attraction.setId(id++);
                attraction.setName(result.getName());
                attraction.setCity(result.getCity());
                attraction.setProvince("");
                attraction.setDescription(result.getType());
                attraction.setOpenTime("全天开放");
                attraction.setTicketPrice(new BigDecimal("0"));
                attraction.setPlayDuration(120);
                attraction.setCrowdLevel("中等");
                attraction.setRating(4.5);

                if (result.getLocation() != null && !result.getLocation().isEmpty()) {
                    String[] coords = result.getLocation().split(",");
                    if (coords.length == 2) {
                        attraction.setLongitude(new BigDecimal(coords[0]));
                        attraction.setLatitude(new BigDecimal(coords[1]));
                    }
                }

                attractions.add(attraction);
            }

            return attractions;
        } catch (Exception e) {
            System.err.println("从API获取景点失败: " + e.getMessage());
            return null;
        }
    }

    private List<Attraction> getMockAttractions(String destination) {
        List<Attraction> mockAttractions = new ArrayList<>();

        Attraction a1 = new Attraction();
        a1.setId(1L);
        a1.setName("西湖风景区");
        a1.setCity(destination);
        a1.setProvince("浙江");
        a1.setDescription("杭州标志性景点");
        a1.setOpenTime("全天开放");
        a1.setTicketPrice(new BigDecimal("0"));
        a1.setPlayDuration(180);
        a1.setCrowdLevel("拥挤");
        a1.setRating(4.8);
        a1.setLongitude(new BigDecimal("120.148916"));
        a1.setLatitude(new BigDecimal("30.259244"));
        mockAttractions.add(a1);

        Attraction a2 = new Attraction();
        a2.setId(2L);
        a2.setName("灵隐寺");
        a2.setCity(destination);
        a2.setProvince("浙江");
        a2.setDescription("千年古刹");
        a2.setOpenTime("07:00-18:00");
        a2.setTicketPrice(new BigDecimal("75"));
        a2.setPlayDuration(120);
        a2.setCrowdLevel("中等");
        a2.setRating(4.7);
        a2.setLongitude(new BigDecimal("120.101476"));
        a2.setLatitude(new BigDecimal("30.241558"));
        mockAttractions.add(a2);

        Attraction a3 = new Attraction();
        a3.setId(3L);
        a3.setName("宋城");
        a3.setCity(destination);
        a3.setProvince("浙江");
        a3.setDescription("大型宋代主题公园");
        a3.setOpenTime("10:00-21:00");
        a3.setTicketPrice(new BigDecimal("300"));
        a3.setPlayDuration(240);
        a3.setCrowdLevel("中等");
        a3.setRating(4.5);
        a3.setLongitude(new BigDecimal("120.069719"));
        a3.setLatitude(new BigDecimal("30.218839"));
        mockAttractions.add(a3);

        Attraction a4 = new Attraction();
        a4.setId(4L);
        a4.setName("杭州博物馆");
        a4.setCity(destination);
        a4.setProvince("浙江");
        a4.setDescription("室内博物馆，雨天推荐");
        a4.setOpenTime("09:00-17:00");
        a4.setTicketPrice(new BigDecimal("0"));
        a4.setPlayDuration(120);
        a4.setCrowdLevel("空闲");
        a4.setRating(4.3);
        a4.setLongitude(new BigDecimal("120.169920"));
        a4.setLatitude(new BigDecimal("30.267560"));
        mockAttractions.add(a4);

        Attraction a5 = new Attraction();
        a5.setId(5L);
        a5.setName("中国丝绸博物馆");
        a5.setCity(destination);
        a5.setProvince("浙江");
        a5.setDescription("室内展馆，雨天推荐");
        a5.setOpenTime("09:00-17:00");
        a5.setTicketPrice(new BigDecimal("0"));
        a5.setPlayDuration(90);
        a5.setCrowdLevel("空闲");
        a5.setRating(4.2);
        a5.setLongitude(new BigDecimal("120.139620"));
        a5.setLatitude(new BigDecimal("30.254440"));
        mockAttractions.add(a5);

        return mockAttractions;
    }

    private List<Attraction> filterAndSortAttractions(List<Attraction> attractions, ItineraryRequest request) {
        System.out.println("=== filterAndSortAttractions 方法被调用 ===");
        System.out.println("request对象: " + request);
        List<String> tabooNeeds = request.getTabooNeeds();
        System.out.println("tabooNeeds: " + tabooNeeds);
        System.out.println("tabooNeeds是否为null: " + (tabooNeeds == null));
        if (tabooNeeds != null) {
            System.out.println("tabooNeeds是否为空: " + tabooNeeds.isEmpty());
        }

        if (tabooNeeds != null && !tabooNeeds.isEmpty()) {
            System.out.println("=== 开始过滤景点，禁忌需求: " + tabooNeeds + " ===");
            System.out.println("过滤前景点数量: " + attractions.size());
            for (Attraction a : attractions) {
                System.out.println("景点: " + a.getName() + ", 客流: " + a.getCrowdLevel());
            }

            attractions = attractions.stream()
                    .filter(a -> {
                        for (String taboo : tabooNeeds) {
                            System.out.println("检查禁忌: '" + taboo + "', 包含'拥挤': " + taboo.contains("拥挤"));
                            System.out.println("景点客流: '" + a.getCrowdLevel() + "', 等于'拥挤': " + "拥挤".equals(a.getCrowdLevel()));
                            if (taboo.contains("拥挤") && "拥挤".equals(a.getCrowdLevel())) {
                                System.out.println("过滤掉拥挤景点: " + a.getName());
                                return false;
                            }
                            System.out.println("检查景点名称: '" + a.getName() + "', 禁忌包含它: " + taboo.contains(a.getName()));
                            if (taboo.contains(a.getName())) {
                                System.out.println("过滤掉禁忌景点: " + a.getName());
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(java.util.stream.Collectors.toList());

            System.out.println("过滤后景点数量: " + attractions.size());
            for (Attraction a : attractions) {
                System.out.println("景点: " + a.getName() + ", 客流: " + a.getCrowdLevel());
            }
        } else {
            System.out.println("=== 没有禁忌需求，跳过过滤 ===");
        }

        // 使用新的多维度综合排序算法
        LocalDate startDate = null;
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            try {
                startDate = LocalDate.parse(request.getStartDate());
            } catch (Exception e) {
                startDate = LocalDate.now();
            }
        } else {
            startDate = LocalDate.now();
        }

        attractionSortService.sortAttractions(attractions, request.getTravelStyle(), request.getDestination(), startDate);

        return attractions;
    }

    private List<Attraction> sortAttractionsByDistance(List<Attraction> attractions, String destination) {
        if (attractions == null || attractions.size() <= 1) {
            return attractions;
        }

        try {
            List<Attraction> sortedAttractions = new ArrayList<>(attractions);
            sortedAttractions.sort((a, b) -> {
                try {
                    String coordA = String.format("%f,%f",
                        a.getLongitude() != null ? a.getLongitude().doubleValue() : 0.0,
                        a.getLatitude() != null ? a.getLatitude().doubleValue() : 0.0);
                    String coordB = String.format("%f,%f",
                        b.getLongitude() != null ? b.getLongitude().doubleValue() : 0.0,
                        b.getLatitude() != null ? b.getLatitude().doubleValue() : 0.0);

                    DistanceInfo distA = externalApiService.getDistance(destination, coordA, "driving");
                    DistanceInfo distB = externalApiService.getDistance(destination, coordB, "driving");

                    if (distA != null && distB != null) {
                        return Double.compare(distA.getDistance(), distB.getDistance());
                    }
                } catch (Exception e) {
                }
                return 0;
            });
            return sortedAttractions;
        } catch (Exception e) {
            return attractions;
        }
    }

    private List<Attraction> sortAttractionsByOpenTime(List<Attraction> attractions) {
        if (attractions == null || attractions.size() <= 1) {
            return attractions;
        }

        try {
            List<Attraction> sortedAttractions = new ArrayList<>(attractions);
            sortedAttractions.sort((a, b) -> {
                String openTimeA = a.getOpenTime() != null ? a.getOpenTime() : "";
                String openTimeB = b.getOpenTime() != null ? b.getOpenTime() : "";

                int openHourA = extractOpenHour(openTimeA);
                int openHourB = extractOpenHour(openTimeB);

                return Integer.compare(openHourA, openHourB);
            });
            return sortedAttractions;
        } catch (Exception e) {
            return attractions;
        }
    }

    private int extractOpenHour(String openTime) {
        if (openTime == null || openTime.isEmpty()) {
            return 9;
        }

        try {
            if (openTime.contains("-")) {
                String[] parts = openTime.split("-");
                if (parts.length > 0) {
                    String startTime = parts[0].trim();
                    if (startTime.contains(":")) {
                        String[] timeParts = startTime.split(":");
                        if (timeParts.length > 0) {
                            return Integer.parseInt(timeParts[0].trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        return 9;
    }

    private List<Attraction> filterIndoorAttractions(List<Attraction> attractions) {
        return attractions.stream()
                .filter(a -> {
                    String name = a.getName() != null ? a.getName() : "";
                    String desc = a.getDescription() != null ? a.getDescription() : "";

                    return name.contains("博物馆") || name.contains("展馆") ||
                           name.contains("艺术馆") || name.contains("展览") ||
                           desc.contains("室内") || desc.contains("博物馆") ||
                           desc.contains("展馆");
                })
                .collect(java.util.stream.Collectors.toList());
    }

    private int getCrowdOrder(String crowdLevel) {
        if ("空闲".equals(crowdLevel)) return 1;
        if ("中等".equals(crowdLevel)) return 2;
        if ("拥挤".equals(crowdLevel)) return 3;
        return 2;
    }

    private int getSpotsPerDay(String travelStyle) {
        if ("暴走".equals(travelStyle)) return 5;
        if ("休闲".equals(travelStyle)) return 2;
        if ("亲子".equals(travelStyle)) return 2;
        return 3;
    }

    private String[] generateTimeSlots(int spotsPerDay, String travelStyle) {
        String[] slots = new String[spotsPerDay * 2];
        int startHour = "暴走".equals(travelStyle) ? 7 : 9;
        int currentHour = startHour;

        for (int i = 0; i < spotsPerDay; i++) {
            slots[i * 2] = String.format("%02d:00", currentHour);
            currentHour += "暴走".equals(travelStyle) ? 2 : 3;
            slots[i * 2 + 1] = String.format("%02d:00", currentHour);
            currentHour += 1;
        }
        return slots;
    }

    private String getWeatherInfo(String date, String destination) {
        try {
            WeatherInfo weatherInfo = externalApiService.getWeather(destination, date);
            if (weatherInfo != null) {
                return weatherInfo.getWeather();
            }
        } catch (Exception e) {
        }
        return "晴";
    }

    private String generateDayTips(int dayNumber, String travelStyle, String weather) {
        if ("雨天".equals(weather)) {
            return "今日有雨，建议携带雨具，优先选择室内景点";
        }
        if ("错峰优先".equals(travelStyle)) {
            return "建议早起出发，避开人流高峰时段，享受更好的游览体验";
        }
        if ("亲子".equals(travelStyle)) {
            return "注意儿童体力，适时休息，备好零食和水";
        }
        if ("暴走".equals(travelStyle)) {
            return "行程较紧凑，注意体力分配，穿舒适的鞋子";
        }
        return "祝您旅途愉快，注意安全";
    }

    private String getBestVisitTime(Attraction attraction) {
        if (attraction.getOpenTime() != null && attraction.getOpenTime().contains("07")) {
            return "07:30-09:00（早间错峰时段）";
        }
        if ("拥挤".equals(attraction.getCrowdLevel())) {
            return "建议工作日上午或下午4点后";
        }
        return "09:00-11:00 或 14:00-16:00";
    }

    private String generateSpotNotes(Attraction attraction, ItineraryRequest request) {
        StringBuilder notes = new StringBuilder();

        if ("错峰优先".equals(request.getTravelStyle())) {
            notes.append("错峰出行推荐");
            if ("拥挤".equals(attraction.getCrowdLevel())) {
                notes.append("，建议避开节假日");
            }
        } else if ("亲子".equals(request.getTravelStyle())) {
            notes.append("适合亲子游");
        } else {
            notes.append("热门景点");
        }

        if (attraction.getRating() != null && attraction.getRating() >= 4.5) {
            notes.append("，高评分景点");
        }

        return notes.toString();
    }

    private BudgetInfo calculateBudget(BigDecimal totalBudget, int days, int peopleCount, String travelStyle) {
        BudgetInfo budgetInfo = new BudgetInfo();

        // 使用精确计算替代固定比例
        double transportRatio = 0.25;
        double hotelRatio = 0.35;
        double foodRatio = 0.20;
        double ticketRatio = 0.12;
        double otherRatio = 0.08;

        if ("暴走".equals(travelStyle)) {
            transportRatio = 0.30;
            ticketRatio = 0.18;
            hotelRatio = 0.28;
        } else if ("亲子".equals(travelStyle)) {
            foodRatio = 0.25;
            hotelRatio = 0.38;
            ticketRatio = 0.10;
        }

        budgetInfo.setTransportBudget(totalBudget.multiply(new BigDecimal(transportRatio)));
        budgetInfo.setHotelBudget(totalBudget.multiply(new BigDecimal(hotelRatio)));
        budgetInfo.setFoodBudget(totalBudget.multiply(new BigDecimal(foodRatio)));
        budgetInfo.setTicketBudget(totalBudget.multiply(new BigDecimal(ticketRatio)));
        budgetInfo.setOtherBudget(totalBudget.multiply(new BigDecimal(otherRatio)));
        budgetInfo.setTotalBudget(totalBudget);
        budgetInfo.setEstimatedTotal(totalBudget.multiply(new BigDecimal("0.95")));

        return budgetInfo;
    }

    public boolean adjustItinerary(Long itineraryId, ItineraryAdjustRequest request) {
        try {
            Itinerary itinerary = itineraryMapper.selectById(itineraryId);
            if (itinerary == null) {
                return false;
            }

            List<ItineraryDay> days = itineraryDayMapper.selectByItineraryId(itineraryId);
            Map<Integer, ItineraryDay> dayMap = new HashMap<>();
            for (ItineraryDay day : days) {
                dayMap.put(day.getDayNumber(), day);
            }

            if (request.getRemoveSpots() != null && !request.getRemoveSpots().isEmpty()) {
                for (Long attractionId : request.getRemoveSpots()) {
                    if (attractionId == null) continue;
                    for (ItineraryDay day : days) {
                        List<ItinerarySpot> spots = itinerarySpotMapper.selectByItineraryDayId(day.getId());
                        for (ItinerarySpot spot : spots) {
                            if (attractionId.equals(spot.getAttractionId())) {
                                itinerarySpotMapper.deleteById(spot.getId());
                                break;
                            }
                        }
                    }
                }
            }

            if (request.getRemoveSpotNames() != null && !request.getRemoveSpotNames().isEmpty()) {
                for (String spotName : request.getRemoveSpotNames()) {
                    if (spotName == null || spotName.isEmpty()) continue;
                    for (ItineraryDay day : days) {
                        List<ItinerarySpot> spots = itinerarySpotMapper.selectByItineraryDayId(day.getId());
                        for (ItinerarySpot spot : spots) {
                            if (spotName.equals(spot.getAttractionName())) {
                                itinerarySpotMapper.deleteById(spot.getId());
                                break;
                            }
                        }
                    }
                }
            }

            if (request.getAddSpots() != null && !request.getAddSpots().isEmpty()) {
                for (ItineraryAdjustRequest.AttractionToAdd addSpot : request.getAddSpots()) {
                    ItineraryDay day = dayMap.get(addSpot.getDayNumber());
                    if (day == null) {
                        continue;
                    }

                    ItinerarySpot spot = new ItinerarySpot();
                    spot.setItineraryDayId(day.getId());
                    spot.setAttractionId(addSpot.getAttractionId());
                    spot.setAttractionName(addSpot.getAttractionName());
                    spot.setOrderNum(addSpot.getOrderNum());
                    spot.setArrivalTime(addSpot.getArrivalTime());
                    spot.setLeaveTime(addSpot.getLeaveTime());
                    spot.setStayDuration(addSpot.getStayDuration());
                    spot.setTransportToNext("驾车");
                    itinerarySpotMapper.insert(spot);
                }
            }

            if (request.getAdjustments() != null && !request.getAdjustments().isEmpty()) {
                System.out.println("=== 调整请求详情 ===");
                System.out.println("adjustments: " + request.getAdjustments());
                for (Map.Entry<String, Map<String, ItineraryAdjustRequest.SpotAdjustment>> dayEntry :
                        request.getAdjustments().entrySet()) {
                    System.out.println("  Day key: " + dayEntry.getKey() + ", value: " + dayEntry.getValue());
                    Integer dayNumber = Integer.parseInt(dayEntry.getKey().replace("day", ""));
                    ItineraryDay day = dayMap.get(dayNumber);
                    if (day == null) {
                        System.out.println("  找不到Day: " + dayNumber);
                        continue;
                    }

                    List<ItinerarySpot> spots = itinerarySpotMapper.selectByItineraryDayId(day.getId());
                    System.out.println("  Day " + dayNumber + " 有 " + spots.size() + " 个景点");
                    Map<String, ItineraryAdjustRequest.SpotAdjustment> adjustments = dayEntry.getValue();

                    for (ItinerarySpot spot : spots) {
                        String spotKey = String.valueOf(spot.getAttractionId());
                        String spotNameKey = spot.getAttractionName();
                        System.out.println("    景点: " + spot.getAttractionName() + ", attractionId=" + spotKey + ", 是否有adjustment: " + adjustments.containsKey(spotKey));
                        ItineraryAdjustRequest.SpotAdjustment adjustment =
                                adjustments.get(spotKey);
                        if (adjustment == null) {
                            adjustment = adjustments.get(spotNameKey);
                            System.out.println("    按名称匹配: " + spotNameKey + ", 是否有adjustment: " + (adjustment != null));
                        }
                        if (adjustment != null) {
                            System.out.println("    应用调整: arrivalTime=" + adjustment.getArrivalTime());
                            if (adjustment.getArrivalTime() != null) {
                                spot.setArrivalTime(adjustment.getArrivalTime());
                            }
                            if (adjustment.getLeaveTime() != null) {
                                spot.setLeaveTime(adjustment.getLeaveTime());
                            }
                            if (adjustment.getStayDuration() != null) {
                                spot.setStayDuration(adjustment.getStayDuration());
                            }
                            if (adjustment.getOrderNum() != null) {
                                spot.setOrderNum(adjustment.getOrderNum());
                            }
                            itinerarySpotMapper.updateById(spot);
                        }
                    }
                }
            }

            recalculateItinerary(itineraryId);

            return true;
        } catch (Exception e) {
            System.err.println("调整行程失败: " + e.getMessage());
            return false;
        }
    }

    private void recalculateItinerary(Long itineraryId) {
        Itinerary itinerary = itineraryMapper.selectById(itineraryId);
        List<ItineraryDay> days = itineraryDayMapper.selectByItineraryId(itineraryId);

        for (ItineraryDay day : days) {
            List<ItinerarySpot> spots = itinerarySpotMapper.selectByItineraryDayId(day.getId());

            if (spots.size() > 1) {
                for (int i = 0; i < spots.size() - 1; i++) {
                    ItinerarySpot current = spots.get(i);
                    ItinerarySpot next = spots.get(i + 1);

                    Attraction currentAttraction = attractionMapper.selectById(current.getAttractionId());
                    Attraction nextAttraction = attractionMapper.selectById(next.getAttractionId());

                    if (currentAttraction != null && nextAttraction != null) {
                        String origin = currentAttraction.getLongitude() + "," + currentAttraction.getLatitude();
                        String destination = nextAttraction.getLongitude() + "," + nextAttraction.getLatitude();

                        DistanceInfo distanceInfo = externalApiService.getDistance(origin, destination, "driving");

                        if (distanceInfo != null && distanceInfo.getDistance() != null) {
                            current.setTransportToNext(distanceInfo.getDistance() + "公里");
                        }

                        itinerarySpotMapper.updateById(current);
                    }
                }
            }
        }

        itinerary.setUpdateTime(java.time.LocalDateTime.now());
        itineraryMapper.updateById(itinerary);
    }
//根据CrowdPredictionService里面的CrowdLevel提供的接口来分析当日的客流量然后给出建议
    public OffpeakRecommendationResponse getOffpeakRecommendation(String destination, String startDate, int days) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = start.plusDays(days - 1);

        // 使用多维度客流预测算法
        CrowdPredictionService.CrowdLevel originalCrowdLevel = crowdPredictionService.predictRange(start, end);

        boolean isHoliday = originalCrowdLevel == CrowdPredictionService.CrowdLevel.HIGH;
        String holidayName = crowdPredictionService.getDetail(start).getHolidayName();

        OffpeakRecommendationResponse.OriginalDates originalDates = OffpeakRecommendationResponse.OriginalDates.builder()
                .startDate(startDate)
                .endDate(end.toString())
                .isHoliday(isHoliday)
                .holidayName(holidayName)
                .isWorkday(originalCrowdLevel == CrowdPredictionService.CrowdLevel.LOW)
                .build();

        List<OffpeakRecommendationResponse.OffpeakRecommendation> recommendations = new ArrayList<>();

        // 方案1：原日期客流信息
        OffpeakRecommendationResponse.OffpeakRecommendation originalRec =
                OffpeakRecommendationResponse.OffpeakRecommendation.builder()
                        .startDate(startDate)
                        .endDate(end.toString())
                        .reason(buildCrowdReason(start, end, destination))
                        .crowdLevel(originalCrowdLevel.getLabel())
                        .alternativeSpots(generateAlternativeSpots(destination, start, days))
                        .build();
        recommendations.add(originalRec);

        // 方案2：推荐最佳错峰日期
        LocalDate bestStart = findBestOffpeakDate(start, days);
        if (!bestStart.equals(start)) {
            LocalDate bestEnd = bestStart.plusDays(days - 1);
            CrowdPredictionService.CrowdLevel bestCrowdLevel = crowdPredictionService.predictRange(bestStart, bestEnd);

            OffpeakRecommendationResponse.OffpeakRecommendation bestRec =
                    OffpeakRecommendationResponse.OffpeakRecommendation.builder()
                            .startDate(bestStart.toString())
                            .endDate(bestEnd.toString())
                            .reason(buildBestOffpeakReason(bestStart, bestEnd, originalCrowdLevel, bestCrowdLevel))
                            .crowdLevel(bestCrowdLevel.getLabel())
                            .alternativeSpots(generateAlternativeSpots(destination, bestStart, days))
                            .build();
            recommendations.add(bestRec);
        }

        // 方案3：前后各找一个备选
        LocalDate beforeStart = start.minusDays(7);
        LocalDate beforeEnd = beforeStart.plusDays(days - 1);
        if (beforeStart.isAfter(LocalDate.now())) {
            CrowdPredictionService.CrowdLevel beforeCrowdLevel = crowdPredictionService.predictRange(beforeStart, beforeEnd);
            if (beforeCrowdLevel != originalCrowdLevel) {
                OffpeakRecommendationResponse.OffpeakRecommendation beforeRec =
                        OffpeakRecommendationResponse.OffpeakRecommendation.builder()
                                .startDate(beforeStart.toString())
                                .endDate(beforeEnd.toString())
                                .reason("提前一周出行，" + beforeCrowdLevel.getLabel() + "客流")
                                .crowdLevel(beforeCrowdLevel.getLabel())
                                .alternativeSpots(new ArrayList<>())
                                .build();
                recommendations.add(beforeRec);
            }
        }

        LocalDate afterStart = start.plusDays(7);
        LocalDate afterEnd = afterStart.plusDays(days - 1);
        CrowdPredictionService.CrowdLevel afterCrowdLevel = crowdPredictionService.predictRange(afterStart, afterEnd);
        if (afterCrowdLevel != originalCrowdLevel) {
            OffpeakRecommendationResponse.OffpeakRecommendation afterRec =
                    OffpeakRecommendationResponse.OffpeakRecommendation.builder()
                            .startDate(afterStart.toString())
                            .endDate(afterEnd.toString())
                            .reason("推迟一周出行，" + afterCrowdLevel.getLabel() + "客流")
                            .crowdLevel(afterCrowdLevel.getLabel())
                            .alternativeSpots(new ArrayList<>())
                            .build();
            recommendations.add(afterRec);
        }

        return OffpeakRecommendationResponse.builder()
                .originalDates(originalDates)
                .offpeakRecommendations(recommendations)
                .build();
    }

    private String buildCrowdReason(LocalDate start, LocalDate end, String destination) {
        CrowdPredictionService.CrowdLevel level = crowdPredictionService.predictRange(start, end);
        String holidayName = crowdPredictionService.getDetail(start).getHolidayName();

        if (holidayName != null) {
            return destination + "正值" + holidayName + "假期，客流" + level.getLabel();
        }

        DayOfWeek startDow = start.getDayOfWeek();
        DayOfWeek endDow = end.getDayOfWeek();
        if (startDow == DayOfWeek.SATURDAY || startDow == DayOfWeek.SUNDAY ||
            endDow == DayOfWeek.SATURDAY || endDow == DayOfWeek.SUNDAY) {
            return destination + "周末出行高峰，客流" + level.getLabel();
        }

        return destination + "工作日出行，客流" + level.getLabel();
    }

    private String buildBestOffpeakReason(LocalDate bestStart, LocalDate bestEnd,
                                          CrowdPredictionService.CrowdLevel originalLevel,
                                          CrowdPredictionService.CrowdLevel bestLevel) {
        String holidayName = crowdPredictionService.getDetail(bestStart).getHolidayName();
        if (holidayName != null) {
            return "节后错峰，客流从" + originalLevel.getLabel() + "降至" + bestLevel.getLabel();
        }

        DayOfWeek bestDow = bestStart.getDayOfWeek();
        if (bestDow == DayOfWeek.MONDAY || bestDow == DayOfWeek.TUESDAY ||
            bestDow == DayOfWeek.WEDNESDAY || bestDow == DayOfWeek.THURSDAY) {
            return "工作日错峰，客流从" + originalLevel.getLabel() + "降至" + bestLevel.getLabel();
        }

        return "调整出行日期，客流从" + originalLevel.getLabel() + "降至" + bestLevel.getLabel();
    }

    private LocalDate findBestOffpeakDate(LocalDate originalStart, int days) {
        // 向前后各搜索14天，找到客流最低的日期
        LocalDate bestStart = originalStart;
        double bestScore = Double.MAX_VALUE;

        for (int offset = -14; offset <= 14; offset++) {
            LocalDate candidate = originalStart.plusDays(offset);
            if (candidate.isBefore(LocalDate.now())) continue;

            CrowdPredictionService.CrowdDetail detail = crowdPredictionService.getDetail(candidate);
            if (detail.getScore() < bestScore) {
                bestScore = detail.getScore();
                bestStart = candidate;
            }
        }

        return bestStart;
    }

    private List<OffpeakRecommendationResponse.AlternativeSpot> generateAlternativeSpots(
            String destination, LocalDate startDate, int days) {
        List<OffpeakRecommendationResponse.AlternativeSpot> alternatives = new ArrayList<>();

        // 根据目的地动态生成替代景点
        Map<String, Map<String, String>> destinationAlternatives = new HashMap<>();
        destinationAlternatives.put("杭州", new HashMap<String, String>() {{
            put("西湖", "西溪湿地");
            put("灵隐寺", "法喜寺");
            put("宋城", "千岛湖");
            put("河坊街", "龙井村");
        }});
        destinationAlternatives.put("成都", new HashMap<String, String>() {{
            put("宽窄巷子", "平乐古镇");
            put("锦里", "洛带古镇");
            put("熊猫基地", "成都动物园");
            put("杜甫草堂", "浣花溪公园");
            put("武侯祠", "文殊院");
        }});
        destinationAlternatives.put("北京", new HashMap<String, String>() {{
            put("故宫", "首都博物馆");
            put("长城", "慕田峪长城");
            put("天坛", "地坛公园");
            put("颐和园", "圆明园");
        }});
        destinationAlternatives.put("上海", new HashMap<String, String>() {{
            put("外滩", "徐汇滨江");
            put("迪士尼", "海昌海洋公园");
            put("南京路", "田子坊");
            put("东方明珠", "上海科技馆");
        }});

        Map<String, String> alternativesMap = destinationAlternatives.getOrDefault(destination, new HashMap<>());
        for (Map.Entry<String, String> entry : alternativesMap.entrySet()) {
            OffpeakRecommendationResponse.AlternativeSpot alternative =
                    OffpeakRecommendationResponse.AlternativeSpot.builder()
                            .original(entry.getKey())
                            .alternative(entry.getValue())
                            .reason(entry.getKey() + "节假日常拥挤，" + entry.getValue() + "人少景美")
                            .build();
            alternatives.add(alternative);
        }

        return alternatives;
    }
}
