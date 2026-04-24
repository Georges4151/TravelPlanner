package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.ItineraryRequest;
import com.travelplanner.dto.ItineraryResponse;
import com.travelplanner.dto.ItineraryAdjustRequest;
import com.travelplanner.dto.OffpeakRecommendationResponse;
import com.travelplanner.entity.Itinerary;
import com.travelplanner.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @PostMapping("/generate-async")  //对应前端的请求路径
    public ApiResponse<Long> generateItineraryAsync(@RequestBody ItineraryRequest request) {
        // @RequestBody注解自动将JSON转换为ItineraryRequest对象！
        try {
            Long itineraryId = itineraryService.startGenerateItinerary(request);
            return ApiResponse.success(itineraryId);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @GetMapping("/status/{id}")
    public ApiResponse<Itinerary> getGenerateStatus(@PathVariable Long id) {
        try {
            Itinerary itinerary = itineraryService.getItineraryByIdSimple(id);
            return ApiResponse.success(itinerary);
        } catch (Exception e) {
            return ApiResponse.error(404, e.getMessage());
        }
    }

    @PostMapping("/generate")
    public ApiResponse<ItineraryResponse> generateItinerary(@RequestBody ItineraryRequest request) {
        try {
            ItineraryResponse response = itineraryService.generateItinerary(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<ItineraryResponse> getItineraryById(@PathVariable Long id) {
        try {
            return ApiResponse.success(itineraryService.getItineraryById(id));
        } catch (Exception e) {
            return ApiResponse.error(404, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Itinerary>> getUserItineraries(@PathVariable Long userId) {
        return ApiResponse.success(itineraryService.getUserItineraries(userId));
    }

    @PostMapping("/template/{templateId}")
    public ApiResponse<ItineraryResponse> createFromTemplate(
            @PathVariable Long templateId,
            @RequestParam Long userId) {
        try {
            return ApiResponse.success(itineraryService.createFromTemplate(templateId, userId));
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteItinerary(@PathVariable Long id) {
        try {
            itineraryService.deleteItinerary(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/regenerate/{id}")
    public ApiResponse<ItineraryResponse> regenerateItinerary(
            @PathVariable Long id,
            @RequestBody ItineraryRequest request) {
        try {
            itineraryService.deleteItineraryDays(id);
            ItineraryResponse response = itineraryService.regenerateItinerary(id, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @PostMapping("/{id}/adjust")
    public ApiResponse<Void> adjustItinerary(
            @PathVariable Long id,
            @RequestBody ItineraryAdjustRequest request) {
        try {
            System.out.println("=== 收到调整请求 ===");
            System.out.println("itineraryId: " + id);
            System.out.println("request: " + request);
            boolean success = itineraryService.adjustItinerary(id, request);
            if (success) {
                return ApiResponse.success(null);
            } else {
                return ApiResponse.error(400, "调整行程失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "调整行程失败: " + e.getMessage());
        }
    }

    @GetMapping("/offpeak/recommend")
    public ApiResponse<OffpeakRecommendationResponse> getOffpeakRecommendation(
            @RequestParam String destination,
            @RequestParam String startDate,
            @RequestParam(defaultValue = "3") int days) {
        try {
            OffpeakRecommendationResponse response = itineraryService.getOffpeakRecommendation(destination, startDate, days);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取错峰推荐失败: " + e.getMessage());
        }
    }

    @PostMapping("/generate-from-attraction")
    public ApiResponse<ItineraryResponse> generateFromAttraction(
            @RequestParam Long userId,
            @RequestParam String attractionName,
            @RequestParam String attractionAddress,
            @RequestParam(required = false) String attractionCity,
            @RequestParam(defaultValue = "3") Integer days) {
        try {
            System.out.println("=== generateFromAttraction 被调用 ===");
            System.out.println("attractionName: " + attractionName);
            System.out.println("attractionAddress: " + attractionAddress);
            System.out.println("attractionCity: " + attractionCity);
            System.out.println("days: " + days);

            String city = null;

            if (attractionCity != null && !attractionCity.isEmpty()) {
                city = attractionCity.replace("市", "");
                System.out.println("使用传递的城市参数: " + city);
            } else {
                city = extractCityFromAddress(attractionAddress);
                System.out.println("从地址提取城市: " + city);
            }

            if (city == null || city.isEmpty()) {
                city = attractionName;
                System.out.println("城市为空，使用景点名称作为城市: " + city);
            }

            ItineraryRequest request = new ItineraryRequest();
            request.setUserId(userId);
            request.setDestination(city);
            request.setDays(days);
            request.setPeopleCount(1);
            request.setBudget("1000");
            request.setTravelStyle("休闲");

            System.out.println("最终destination: " + request.getDestination());

            ItineraryResponse response = itineraryService.generateItinerary(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "生成行程失败: " + e.getMessage());
        }
    }

    private String extractCityFromAddress(String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }

        String[] cities = {"北京", "上海", "广州", "深圳", "杭州", "成都", "重庆", "西安", "南京", "苏州",
                "武汉", "长沙", "张家界", "三亚", "厦门", "青岛", "大连", "天津", "昆明", "大理", "丽江",
                "桂林", "黄山", "九寨沟", "凤凰", "西塘", "乌镇", "邯郸", "石家庄", "保定", "承德", "秦皇岛",
                "唐山", "廊坊", "沧州", "邢台", "衡水", "张家口", "太原", "大同", "平遥", "晋中", "临汾", "运城",
                "长治", "晋城", "呼和浩特", "包头", "鄂尔多斯", "赤峰", "呼伦贝尔", "沈阳", "鞍山", "抚顺", "本溪",
                "丹东", "锦州", "营口", "长春", "吉林", "延吉", "长白山", "哈尔滨", "齐齐哈尔", "牡丹江", "大庆",
                "伊春", "漠河", "无锡", "常州", "扬州", "镇江", "南通", "徐州", "连云港", "淮安", "盐城", "泰州",
                "宿迁", "合肥", "芜湖", "蚌埠", "安庆", "阜阳", "亳州", "池州", "宣城", "福州", "泉州", "漳州",
                "龙岩", "三明", "莆田", "南平", "宁德", "南昌", "九江", "景德镇", "萍乡", "新余", "鹰潭", "赣州",
                "吉安", "宜春", "上饶", "抚州", "济南", "烟台", "威海", "潍坊", "淄博", "枣庄", "东营", "济宁",
                "泰安", "日照", "临沂", "德州", "聊城", "滨州", "菏泽", "郑州", "洛阳", "开封", "安阳", "南阳",
                "新乡", "焦作", "许昌", "漯河", "商丘", "信阳", "周口", "驻马店", "平顶山", "鹤壁", "濮阳", "三门峡",
                "珠海", "汕头", "佛山", "东莞", "惠州", "中山", "江门", "湛江", "茂名", "肇庆", "清远", "韶关",
                "梅州", "河源", "阳江", "云浮", "潮州", "揭阳", "南宁", "北海", "柳州", "梧州", "防城港", "钦州",
                "贵港", "玉林", "百色", "贺州", "河池", "来宾", "崇左", "海口", "万宁", "文昌", "琼海", "五指山",
                "儋州", "贵阳", "遵义", "赤水", "六盘水", "安顺", "毕节", "铜仁", "黔东南", "黔南", "黔西南",
                "兰州", "敦煌", "嘉峪关", "金昌", "白银", "天水", "武威", "张掖", "平凉", "酒泉", "庆阳", "定西",
                "陇南", "甘南", "临夏", "西宁", "海东", "海北", "黄南", "海南", "果洛", "玉树", "海西", "银川",
                "石嘴山", "吴忠", "固原", "中卫", "乌鲁木齐", "克拉玛依", "吐鲁番", "哈密", "昌吉", "博尔塔拉",
                "巴音郭楞", "阿克苏", "克孜勒苏", "喀什", "和田", "伊犁", "塔城", "阿勒泰", "拉萨", "日喀则",
                "昌都", "林芝", "山南", "那曲", "阿里", "绵阳", "乐山", "宜宾", "南充", "泸州", "德阳", "广元",
                "遂宁", "内江", "自贡", "攀枝花", "广安", "达州", "眉山", "雅安", "巴中", "资阳", "阿坝", "甘孜",
                "凉山"};

        for (String city : cities) {
            if (address.contains(city)) {
                return city;
            }
        }

        return null;
    }
}
