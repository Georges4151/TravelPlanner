package com.travelplanner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelplanner.dto.WeatherInfo;
import com.travelplanner.dto.CrowdInfo;
import com.travelplanner.dto.HolidayInfo;
import com.travelplanner.dto.DistanceInfo;
import com.travelplanner.dto.AttractionSearchResult;
import com.travelplanner.dto.CrowdForecastResponse;
import com.travelplanner.dto.TrafficInfo;
import com.travelplanner.entity.Attraction;
import com.travelplanner.mapper.AttractionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ExternalApiService {

    @Autowired
    private AttractionMapper attractionMapper;

    @Value("${travel.qweather.api-key:}")
    private String qweatherApiKey;

    @Value("${travel.qweather.api-url:https://devapi.qweather.com/v7}")
    private String qweatherApiUrl;

    @Value("${travel.amap.api-key:}")
    private String amapApiKey;

    @Value("${travel.amap.api-url:https://restapi.amap.com/v3}")
    private String amapApiUrl;

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Random random = new Random();
    private Map<String, String> cityLocationCache = new HashMap<>();
    private Map<String, List<AttractionSearchResult>> searchCache = new HashMap<>();
    private Map<String, String> attractionCityMap = new HashMap<String, String>() {{
        put("京娘湖", "邯郸");
        put("娲皇宫", "邯郸");
        put("广府古城", "邯郸");
        put("响堂山石窟", "邯郸");
        put("太行五指山", "邯郸");
        put("朝阳沟", "邯郸");
        put("七步沟", "邯郸");
        put("太行山", "邯郸");
        put("长寿村", "邯郸");
        put("东山文化博艺园", "邯郸");
    }};

    private Map<String, String> cityAdcodeMap = new HashMap<String, String>() {{
        put("北京", "110000");
        put("上海", "310000");
        put("广州", "440100");
        put("深圳", "440300");
        put("杭州", "330100");
        put("成都", "510100");
        put("重庆", "500100");
        put("西安", "610100");
        put("南京", "320100");
        put("苏州", "320500");
        put("武汉", "420100");
        put("长沙", "430100");
        put("张家界", "430800");
        put("三亚", "460200");
        put("厦门", "350200");
        put("青岛", "370200");
        put("大连", "210200");
        put("天津", "120000");
        put("昆明", "530100");
        put("大理", "532900");
        put("丽江", "530700");
        put("桂林", "450300");
        put("黄山", "341000");
        put("九寨沟", "513225");
        put("凤凰", "433123");
        put("西塘", "330421");
        put("乌镇", "330483");
        put("邯郸", "130400");
        put("石家庄", "130100");
        put("保定", "130600");
        put("承德", "130800");
        put("秦皇岛", "130300");
        put("唐山", "130200");
        put("廊坊", "131000");
        put("沧州", "130900");
        put("邢台", "130500");
        put("衡水", "131100");
        put("张家口", "130700");
        put("太原", "140100");
        put("大同", "140200");
        put("平遥", "140729");
        put("晋中", "140700");
        put("临汾", "141000");
        put("运城", "140800");
        put("长治", "140400");
        put("晋城", "140500");
        put("呼和浩特", "150100");
        put("包头", "150200");
        put("鄂尔多斯", "150600");
        put("赤峰", "150400");
        put("呼伦贝尔", "150700");
        put("沈阳", "210100");
        put("大连", "210200");
        put("鞍山", "210300");
        put("抚顺", "210400");
        put("本溪", "210500");
        put("丹东", "210600");
        put("锦州", "210700");
        put("营口", "210800");
        put("长春", "220100");
        put("吉林", "220200");
        put("延吉", "222401");
        put("长白山", "222404");
        put("哈尔滨", "230100");
        put("齐齐哈尔", "230200");
        put("牡丹江", "231000");
        put("大庆", "230600");
        put("伊春", "230700");
        put("漠河", "232721");
        put("无锡", "320200");
        put("常州", "320400");
        put("扬州", "321000");
        put("镇江", "321100");
        put("南通", "320600");
        put("徐州", "320300");
        put("连云港", "320700");
        put("淮安", "320800");
        put("盐城", "320900");
        put("泰州", "321200");
        put("宿迁", "321300");
        put("合肥", "340100");
        put("芜湖", "340200");
        put("蚌埠", "340300");
        put("安庆", "340800");
        put("阜阳", "341200");
        put("亳州", "341600");
        put("池州", "341700");
        put("宣城", "341800");
        put("福州", "350100");
        put("泉州", "350500");
        put("漳州", "350600");
        put("龙岩", "350800");
        put("三明", "350400");
        put("莆田", "350300");
        put("南平", "350700");
        put("宁德", "350900");
        put("南昌", "360100");
        put("九江", "360400");
        put("景德镇", "360200");
        put("萍乡", "360300");
        put("新余", "360500");
        put("鹰潭", "360600");
        put("赣州", "360700");
        put("吉安", "360800");
        put("宜春", "360900");
        put("上饶", "361100");
        put("抚州", "361000");
        put("济南", "370100");
        put("烟台", "370600");
        put("威海", "371000");
        put("潍坊", "370700");
        put("淄博", "370300");
        put("枣庄", "370400");
        put("东营", "370500");
        put("济宁", "370800");
        put("泰安", "370900");
        put("日照", "371100");
        put("临沂", "371300");
        put("德州", "371400");
        put("聊城", "371500");
        put("滨州", "371600");
        put("菏泽", "371700");
        put("郑州", "410100");
        put("洛阳", "410300");
        put("开封", "410200");
        put("安阳", "410500");
        put("南阳", "411300");
        put("新乡", "410700");
        put("焦作", "410800");
        put("许昌", "411000");
        put("漯河", "411100");
        put("商丘", "411400");
        put("信阳", "411500");
        put("周口", "411600");
        put("驻马店", "411700");
        put("平顶山", "410400");
        put("鹤壁", "410600");
        put("濮阳", "410900");
        put("三门峡", "411200");
        put("珠海", "440400");
        put("汕头", "440500");
        put("佛山", "440600");
        put("东莞", "441900");
        put("惠州", "441300");
        put("中山", "442000");
        put("江门", "440700");
        put("湛江", "440800");
        put("茂名", "440900");
        put("肇庆", "441200");
        put("清远", "441800");
        put("韶关", "440200");
        put("梅州", "441400");
        put("河源", "441600");
        put("阳江", "441700");
        put("云浮", "445300");
        put("潮州", "445100");
        put("揭阳", "445200");
        put("南宁", "450100");
        put("北海", "450500");
        put("柳州", "450200");
        put("梧州", "450400");
        put("防城港", "450600");
        put("钦州", "450700");
        put("贵港", "450800");
        put("玉林", "450900");
        put("百色", "451000");
        put("贺州", "451100");
        put("河池", "451200");
        put("来宾", "451300");
        put("崇左", "451400");
        put("海口", "460100");
        put("万宁", "469006");
        put("文昌", "469005");
        put("琼海", "469002");
        put("五指山", "469001");
        put("儋州", "460400");
        put("贵阳", "520100");
        put("遵义", "520300");
        put("赤水", "520381");
        put("六盘水", "520200");
        put("安顺", "520400");
        put("毕节", "520500");
        put("铜仁", "520600");
        put("黔东南", "522600");
        put("黔南", "522700");
        put("黔西南", "522300");
        put("兰州", "620100");
        put("敦煌", "620982");
        put("嘉峪关", "620200");
        put("金昌", "620300");
        put("白银", "620400");
        put("天水", "620500");
        put("武威", "620600");
        put("张掖", "620700");
        put("平凉", "620800");
        put("酒泉", "620900");
        put("庆阳", "621000");
        put("定西", "621100");
        put("陇南", "621200");
        put("甘南", "623000");
        put("临夏", "622900");
        put("西宁", "630100");
        put("海东", "630200");
        put("海北", "632200");
        put("黄南", "632300");
        put("海南", "632500");
        put("果洛", "632600");
        put("玉树", "632700");
        put("海西", "632800");
        put("银川", "640100");
        put("石嘴山", "640200");
        put("吴忠", "640300");
        put("固原", "640400");
        put("中卫", "640500");
        put("乌鲁木齐", "650100");
        put("克拉玛依", "650200");
        put("吐鲁番", "650400");
        put("哈密", "650500");
        put("昌吉", "652300");
        put("博尔塔拉", "652700");
        put("巴音郭楞", "652800");
        put("阿克苏", "652900");
        put("克孜勒苏", "653000");
        put("喀什", "653100");
        put("和田", "653200");
        put("伊犁", "654000");
        put("塔城", "654200");
        put("阿勒泰", "654300");
        put("拉萨", "540100");
        put("日喀则", "540200");
        put("昌都", "540300");
        put("林芝", "540400");
        put("山南", "540500");
        put("那曲", "540600");
        put("阿里", "542500");
        put("绵阳", "510700");
        put("乐山", "511100");
        put("宜宾", "511500");
        put("南充", "511300");
        put("泸州", "510500");
        put("德阳", "510600");
        put("广元", "510800");
        put("遂宁", "510900");
        put("内江", "511000");
        put("自贡", "510300");
        put("攀枝花", "510400");
        put("广安", "511600");
        put("达州", "511700");
        put("眉山", "511400");
        put("雅安", "511800");
        put("巴中", "511900");
        put("资阳", "512000");
        put("阿坝", "513200");
        put("甘孜", "513300");
        put("凉山", "513400");
    }};

    public WeatherInfo getWeather(String city, String date) {
        if (qweatherApiKey != null && !qweatherApiKey.isEmpty()) {
            try {
                return getQWeatherInfo(city, date);
            } catch (Exception e) {
                System.err.println("和风天气API调用失败: " + e.getMessage());
            }
        }
        return getMockWeather(city, date);
    }

    private String getCityLocation(String city) {
        if (cityLocationCache.containsKey(city)) {
            return cityLocationCache.get(city);
        }

        try {
            String url = "https://geoapi.qweather.com/v2/city/lookup?location=" +
                    URLEncoder.encode(city, "UTF-8") + "&key=" + qweatherApiKey;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if ("200".equals(root.path("code").asText())) {
                JsonNode location = root.path("location").get(0);
                String locationId = location.path("id").asText();
                cityLocationCache.put(city, locationId);
                return locationId;
            }
        } catch (Exception e) {
            System.err.println("获取城市Location ID失败: " + e.getMessage());
        }
        return null;
    }

    private WeatherInfo getQWeatherInfo(String city, String date) {
        String locationId = getCityLocation(city);
        if (locationId == null) {
            return getMockWeather(city, date);
        }

        try {
            String url = qweatherApiUrl + "/weather/now?location=" + locationId + "&key=" + qweatherApiKey;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if ("200".equals(root.path("code").asText())) {
                JsonNode now = root.path("now");
                WeatherInfo info = new WeatherInfo();
                info.setCity(city);
                info.setDate(date != null ? date : LocalDate.now().format(DateTimeFormatter.ISO_DATE));

                String weatherText = now.path("text").asText();
                info.setWeather(weatherText);

                String temp = now.path("temp").asText();
                String feelsLike = now.path("feelsLike").asText();
                info.setTemperature(temp + "°C (体感 " + feelsLike + "°C)");

                info.setHumidity(now.path("humidity").asText() + "%");
                info.setWind(now.path("windDir").asText() + " " + now.path("windScale").asText() + "级");

                if (weatherText.contains("雨") || weatherText.contains("雪")) {
                    info.setTips("今日" + weatherText + "，建议携带雨具，优先选择室内景点");
                } else if (weatherText.contains("晴")) {
                    info.setTips("天气晴好，适合户外活动，注意防晒");
                } else if (weatherText.contains("阴") || weatherText.contains("云")) {
                    info.setTips("天气适宜，适合出行游览");
                } else {
                    info.setTips("天气一般，建议关注天气变化");
                }

                return info;
            }
        } catch (Exception e) {
            System.err.println("获取天气信息失败: " + e.getMessage());
        }

        return getMockWeather(city, date);
    }

    private WeatherInfo getMockWeather(String city, String date) {
        WeatherInfo info = new WeatherInfo();
        info.setCity(city);
        info.setDate(date != null ? date : LocalDate.now().format(DateTimeFormatter.ISO_DATE));

        String[] weathers = {"晴", "多云", "阴", "小雨", "大雨"};
        String weather = weathers[random.nextInt(weathers.length)];
        info.setWeather(weather);

        int temp = 15 + random.nextInt(20);
        info.setTemperature(temp + "°C ~ " + (temp + 5) + "°C");
        info.setHumidity((40 + random.nextInt(40)) + "%");
        info.setWind("东南风 " + (1 + random.nextInt(4)) + "级");

        if ("小雨".equals(weather) || "大雨".equals(weather)) {
            info.setTips("今日有雨，建议携带雨具，优先选择室内景点");
        } else if ("晴".equals(weather)) {
            info.setTips("天气晴好，适合户外活动，注意防晒");
        } else {
            info.setTips("天气适宜，适合出行游览");
        }

        return info;
    }

    public List<WeatherInfo> getWeatherForecast(String city, int days) {
        if (qweatherApiKey != null && !qweatherApiKey.isEmpty()) {
            try {
                return getQWeatherForecast(city, days);
            } catch (Exception e) {
                System.err.println("和风天气预报API调用失败: " + e.getMessage());
            }
        }

        List<WeatherInfo> forecast = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < days; i++) {
            LocalDate date = today.plusDays(i);
            WeatherInfo info = getMockWeather(city, date.format(DateTimeFormatter.ISO_DATE));
            forecast.add(info);
        }
        return forecast;
    }

    private List<WeatherInfo> getQWeatherForecast(String city, int days) {
        String locationId = getCityLocation(city);
        List<WeatherInfo> forecast = new ArrayList<>();

        if (locationId == null) {
            return forecast;
        }

        try {
            String url = qweatherApiUrl + "/weather/3d?location=" + locationId + "&key=" + qweatherApiKey;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if ("200".equals(root.path("code").asText())) {
                JsonNode daily = root.path("daily");
                int count = Math.min(days, daily.size());

                for (int i = 0; i < count; i++) {
                    JsonNode day = daily.get(i);
                    WeatherInfo info = new WeatherInfo();
                    info.setCity(city);
                    info.setDate(day.path("fxDate").asText());
                    info.setWeather(day.path("textDay").asText());
                    info.setTemperature(day.path("tempMin").asText() + "°C ~ " + day.path("tempMax").asText() + "°C");
                    info.setHumidity(day.path("humidity").asText() + "%");
                    info.setWind(day.path("windDirDay").asText() + " " + day.path("windScaleDay").asText() + "级");

                    String weatherText = day.path("textDay").asText();
                    if (weatherText.contains("雨") || weatherText.contains("雪")) {
                        info.setTips("预计" + weatherText + "，建议携带雨具");
                    } else if (weatherText.contains("晴")) {
                        info.setTips("天气晴好，适合户外活动");
                    } else {
                        info.setTips("天气适宜，适合出行");
                    }

                    forecast.add(info);
                }
            }
        } catch (Exception e) {
            System.err.println("获取天气预报失败: " + e.getMessage());
        }

        return forecast;
    }

    public CrowdInfo getCrowdInfo(Long attractionId) {
        Attraction attraction = attractionMapper.findById(attractionId);
        if (attraction == null) {
            return null;
        }

        CrowdInfo info = new CrowdInfo();
        info.setAttractionId(attractionId);
        info.setAttractionName(attraction.getName());

        String[] levels = {"空闲", "中等", "拥挤"};
        int index = random.nextInt(100);
        String level;
        if (index < 30) {
            level = "空闲";
        } else if (index < 70) {
            level = "中等";
        } else {
            level = "拥挤";
        }

        info.setCurrentLevel(level);
        info.setCurrentIndex(index);

        if ("空闲".equals(level)) {
            info.setForecast("预计全天人流较少，适合游览");
            info.setBestVisitTime("全天皆可");
            info.setAvoidTime("无");
        } else if ("中等".equals(level)) {
            info.setForecast("预计人流适中，建议错峰游览");
            info.setBestVisitTime("09:00-11:00 或 14:00-16:00");
            info.setAvoidTime("11:00-13:00 午间高峰");
        } else {
            info.setForecast("预计人流较多，建议早到或傍晚游览");
            info.setBestVisitTime("07:30-09:00 或 16:00-18:00");
            info.setAvoidTime("10:00-15:00 高峰时段");
        }

        return info;
    }

    public List<CrowdInfo> getCityCrowdInfo(String city) {
        List<Attraction> attractions = attractionMapper.findByCity(city);
        List<CrowdInfo> crowdList = new ArrayList<>();

        if (attractions == null || attractions.isEmpty()) {
            return crowdList;
        }

        for (Attraction attraction : attractions) {
            CrowdInfo info = getCrowdInfo(attraction.getId());
            if (info != null) {
                crowdList.add(info);
            }
        }

        return crowdList;
    }

    public HolidayInfo getHolidayInfo(String date) {
        HolidayInfo info = new HolidayInfo();
        info.setDate(date);

        try {
            String url = "https://timor.tech/api/holiday/info/" + date;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (root.path("code").asInt() == 0) {
                JsonNode holiday = root.path("holiday");
                if (!holiday.isNull() && !holiday.isMissingNode()) {
                    info.setIsHoliday(true);
                    info.setHolidayName(holiday.path("name").asText());
                    info.setWage(holiday.path("wage").asInt());
                    info.setIsWorkday(false);
                } else {
                    info.setIsHoliday(false);
                    info.setIsWorkday(true);
                }
            } else {
                info.setIsHoliday(false);
                info.setIsWorkday(true);
            }
        } catch (Exception e) {
            System.err.println("获取节假日信息失败: " + e.getMessage());
            info.setIsHoliday(false);
            info.setIsWorkday(true);
        }

        return info;
    }

    public DistanceInfo getDistance(String origin, String destination, String mode) {
        DistanceInfo info = new DistanceInfo();
        info.setOrigin(origin);
        info.setDestination(destination);
        info.setTransportMode(mode != null ? mode : "driving");

        if (amapApiKey == null || amapApiKey.isEmpty()) {
            return getMockDistance(origin, destination, mode);
        }

        try {
            String url = amapApiUrl + "/distance?key=" + amapApiKey +
                    "&origins=" + URLEncoder.encode(origin, "UTF-8") +
                    "&destination=" + URLEncoder.encode(destination, "UTF-8") +
                    "&type=1";

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if ("1".equals(root.path("status").asText()) &&
                "10000".equals(root.path("infocode").asText())) {

                JsonNode results = root.path("results");
                if (!results.isMissingNode() && results.isArray() && results.size() > 0) {
                    JsonNode result = results.get(0);
                    JsonNode distance = result.path("distance");
                    JsonNode duration = result.path("duration");

                    if (!distance.isMissingNode() && !distance.isNull()) {
                        info.setDistance(distance.asDouble() / 1000.0);
                    }
                    if (!duration.isMissingNode() && !duration.isNull()) {
                        info.setDuration(duration.asInt() / 60);
                    }
                    info.setRoute("建议路线已生成");
                }
            }
        } catch (Exception e) {
            System.err.println("获取距离信息失败: " + e.getMessage());
            return getMockDistance(origin, destination, mode);
        }

        if (info.getDistance() == null) {
            return getMockDistance(origin, destination, mode);
        }

        return info;
    }

    private DistanceInfo getMockDistance(String origin, String destination, String mode) {
        DistanceInfo info = new DistanceInfo();
        info.setOrigin(origin);
        info.setDestination(destination);
        info.setTransportMode(mode != null ? mode : "driving");

        int baseDistance = random.nextInt(20) + 5;
        int baseDuration = random.nextInt(30) + 10;

        if ("walking".equals(mode)) {
            baseDuration = baseDuration * 3;
        } else if ("transit".equals(mode)) {
            baseDuration = baseDuration * 2;
        }

        info.setDistance((double) baseDistance);
        info.setDuration(baseDuration);
        info.setRoute("建议路线已生成");

        return info;
    }

    public List<AttractionSearchResult> searchAttractions(String city, String keyword, String type) {
        List<AttractionSearchResult> results = new ArrayList<>();

        if (amapApiKey == null || amapApiKey.isEmpty()) {
            System.err.println("高德地图API密钥未配置");
            return results;
        }

        try {
            String keywords = keyword != null && !keyword.isEmpty() ? keyword : "景点";

            String searchCity = city;
            String originalKeyword = keywords;
            if (searchCity == null || searchCity.isEmpty()) {
                if (attractionCityMap.containsKey(keywords)) {
                    searchCity = attractionCityMap.get(keywords);
                    System.out.println("景点 '" + keywords + "' 映射到城市: " + searchCity);
                } else {
                    String[] cityKeywords = {"北京", "上海", "广州", "深圳", "杭州", "成都", "重庆", "西安", "南京", "苏州",
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
                    for (String cityKeyword : cityKeywords) {
                        if (keywords.equals(cityKeyword)) {
                            searchCity = null;
                            originalKeyword = keywords;
                            System.out.println("搜索关键词是城市名称，不限制城市: " + originalKeyword);
                            break;
                        } else if (keywords.contains(cityKeyword)) {
                            searchCity = cityKeyword;
                            originalKeyword = keywords.replace(cityKeyword, "").trim();
                            if (originalKeyword.isEmpty()) {
                                originalKeyword = "风景区";
                            }
                            break;
                        }
                    }
                }
            }

            String cityCode = null;
            if (searchCity != null && !searchCity.isEmpty()) {
                cityCode = cityAdcodeMap.get(searchCity);
            }

            if (searchCity == null || searchCity.isEmpty()) {
                StringBuilder urlBuilder = new StringBuilder(amapApiUrl + "/place/text?key=" + amapApiKey +
                        "&keywords=" + URLEncoder.encode(originalKeyword, "UTF-8"));
                urlBuilder.append("&types=" + URLEncoder.encode("110000|110100|110200|110201|110202|110203|110204|110205|110206|110900", "UTF-8"));
                urlBuilder.append("&output=json&offset=0&page_size=50");

                String url = urlBuilder.toString();
                System.out.println("高德地图API请求URL(全国搜索): " + url);
                String response = restTemplate.getForObject(url, String.class);
                System.out.println("高德地图API响应: " + response);

                results = parseAmapResponse(response, null);
                System.out.println("全国搜索结果数量: " + results.size());
            } else if (cityCode != null && !cityCode.isEmpty()) {
                StringBuilder urlBuilder = new StringBuilder(amapApiUrl + "/place/text?key=" + amapApiKey +
                        "&keywords=" + URLEncoder.encode(originalKeyword, "UTF-8"));
                urlBuilder.append("&city=").append(URLEncoder.encode(cityCode, "UTF-8"));
                urlBuilder.append("&types=" + URLEncoder.encode("110000|110100|110200|110201|110202|110203|110204|110205|110206|110900", "UTF-8"));
                urlBuilder.append("&output=json&offset=0&page_size=50");

                String url = urlBuilder.toString();
                System.out.println("高德地图API请求URL(城市代码): " + url);
                String response = restTemplate.getForObject(url, String.class);
                System.out.println("高德地图API响应: " + response);

                results = parseAmapResponse(response, searchCity);
                System.out.println("使用城市代码搜索结果数量: " + results.size());
            }

            if (results.isEmpty() && searchCity != null && !searchCity.isEmpty()) {
                StringBuilder urlBuilder = new StringBuilder(amapApiUrl + "/place/text?key=" + amapApiKey +
                        "&keywords=" + URLEncoder.encode(originalKeyword, "UTF-8"));
                urlBuilder.append("&city=").append(URLEncoder.encode(searchCity, "UTF-8"));
                urlBuilder.append("&types=" + URLEncoder.encode("110000|110100|110200|110201|110202|110203|110204|110205|110206|110900", "UTF-8"));
                urlBuilder.append("&output=json&offset=0&page_size=50");

                String url = urlBuilder.toString();
                System.out.println("高德地图API请求URL(城市名称): " + url);
                String response = restTemplate.getForObject(url, String.class);
                System.out.println("高德地图API响应: " + response);

                results = parseAmapResponse(response, searchCity);
                System.out.println("使用城市名称搜索结果数量: " + results.size());
            }

            return results;
        } catch (Exception e) {
            System.err.println("搜索景点失败: " + e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private List<AttractionSearchResult> parseAmapResponse(String response, String searchCity) {
        List<AttractionSearchResult> results = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            String status = root.path("status").asText();
            String infocode = root.path("infocode").asText();

            if ("1".equals(status) && "10000".equals(infocode)) {
                JsonNode pois = root.path("pois");
                if (pois.isArray()) {
                    List<AttractionSearchResult> allResults = new ArrayList<>();
                    for (JsonNode poi : pois) {
                        String cityName = poi.path("cityname").asText();
                        if (cityName == null || cityName.isEmpty()) {
                            cityName = searchCity != null ? searchCity : "";
                        }

                        if (searchCity != null && !searchCity.isEmpty() && cityName != null && !cityName.isEmpty()) {
                            if (!cityName.contains(searchCity) && !searchCity.contains(cityName)) {
                                System.out.println("过滤城市不匹配POI: " + poi.path("name").asText() + " city=" + cityName + " (期望: " + searchCity + ")");
                                continue;
                            }
                        }

                        String type = poi.path("type").asText();
                        String typecode = poi.path("typecode").asText();

                        boolean isAttraction = isAttractionType(type, typecode);
                        if (!isAttraction) {
                            System.out.println("过滤非景点POI: " + poi.path("name").asText() + " type=" + type);
                            continue;
                        }

                        AttractionSearchResult result = AttractionSearchResult.builder()
                                .id(poi.path("id").asText())
                                .name(poi.path("name").asText())
                                .city(cityName)
                                .address(poi.path("address").asText())
                                .location(poi.path("location").asText())
                                .tel(poi.path("tel").asText())
                                .type(type)
                                .businessArea(poi.path("business_area").asText())
                                .typecode(typecode)
                                .build();
                        allResults.add(result);
                    }

                    if (!allResults.isEmpty()) {
                        results.addAll(allResults);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("解析高德地图响应失败: " + e.getMessage());
        }
        return results;
    }

    private boolean isAttractionType(String type, String typecode) {
        if (type == null || type.isEmpty()) {
            System.out.println("POI类型为空，默认保留");
            return true;
        }

        String lowerType = type.toLowerCase();

        if (typecode != null && typecode.length() >= 2) {
            String prefix = typecode.substring(0, 2);
            if ("11".equals(prefix) || "14".equals(prefix) || "13".equals(prefix)) {
                System.out.println("POI typecode匹配景点: " + typecode + " -> 保留");
                return true;
            }
            if ("19".equals(prefix)) {
                if (typecode.startsWith("1901") || typecode.startsWith("1902") || typecode.startsWith("1903")) {
                    System.out.println("POI typecode匹配景点: " + typecode + " -> 保留");
                    return true;
                }
            }
        }

        if (type.contains("风景名胜") || type.contains("公园") || type.contains("广场") ||
            type.contains("博物馆") || type.contains("纪念馆") || type.contains("故居") ||
            type.contains("寺庙") || type.contains("教堂") || type.contains("湖泊") ||
            type.contains("山") || type.contains("海滩") || type.contains("景点") ||
            type.contains("景区") || type.contains("古镇") || type.contains("古城") ||
            type.contains("遗址") || type.contains("陵墓") || type.contains("园林") ||
            type.contains("自然") || type.contains("湿地") || type.contains("森林") ||
            type.contains("图书馆") || type.contains("文化馆") || type.contains("美术馆") ||
            type.contains("科技馆") || type.contains("展览馆") || type.contains("体育馆")) {
            System.out.println("POI类型匹配景点关键词: " + type + " -> 保留");
            return true;
        }

        if (lowerType.contains("餐饮") || lowerType.contains("购物") || lowerType.contains("商店") ||
            lowerType.contains("酒店") || lowerType.contains("写字楼") || lowerType.contains("商务") ||
            lowerType.contains("住宅") || lowerType.contains("银行") || lowerType.contains("医院") ||
            lowerType.contains("学校") || lowerType.contains("公司") || lowerType.contains("企业") ||
            lowerType.contains("地铁") || lowerType.contains("公交") || lowerType.contains("车站") ||
            lowerType.contains("造型") || lowerType.contains("理发") || lowerType.contains("美容") ||
            lowerType.contains("咖啡") || lowerType.contains("茶饮") || lowerType.contains("餐厅") ||
            lowerType.contains("酒馆") || lowerType.contains("酒吧") || lowerType.contains("娱乐") ||
            lowerType.contains("ktv") || lowerType.contains("网吧") || lowerType.contains("健身") ||
            lowerType.contains("生活服务") || lowerType.contains("汽车销售") || lowerType.contains("汽车") ||
            lowerType.contains("公寓") || lowerType.contains("产业园") || lowerType.contains("楼宇") ||
            lowerType.contains("停车场") || lowerType.contains("影剧院") || lowerType.contains("电影院") ||
            lowerType.contains("影城") || lowerType.contains("门牌") || lowerType.contains("地址信息") ||
            lowerType.contains("住宿") || lowerType.contains("宾馆") || lowerType.contains("民宿")) {
            System.out.println("POI类型匹配非景点关键词: " + type + " -> 过滤");
            return false;
        }

        System.out.println("POI类型未匹配: " + type + " -> 默认保留");
        return true;
    }

    public List<AttractionSearchResult> searchAttractionsFlexible(String city, String keyword) {
        List<AttractionSearchResult> results = new ArrayList<>();

        if (amapApiKey == null || amapApiKey.isEmpty()) {
            System.err.println("高德地图API密钥未配置");
            return results;
        }

        try {
            String keywords = keyword != null && !keyword.isEmpty() ? keyword : "景点";
            String searchCity = city;

            // 暂时禁用缓存，因为缓存key只用关键词导致搜索结果不准确
            // if (searchCache.containsKey(keywords)) {
            //     System.out.println("使用缓存: " + keywords);
            //     return searchCache.get(keywords);
            // }

            Thread.sleep(1000);

            String[] cityCodes = getCityCodes(searchCity);

            for (String cityCode : cityCodes) {
                StringBuilder urlBuilder = new StringBuilder(amapApiUrl + "/place/text?key=" + amapApiKey +
                        "&keywords=" + URLEncoder.encode(keywords, "UTF-8"));
                if (cityCode != null && !cityCode.isEmpty()) {
                    urlBuilder.append("&city=").append(URLEncoder.encode(cityCode, "UTF-8"));
                }
                urlBuilder.append("&output=json&offset=0&page_size=20");

                String url = urlBuilder.toString();
                System.out.println("高德灵活搜索API请求URL: " + url);
                String response = restTemplate.getForObject(new java.net.URI(url), String.class);
                System.out.println("高德灵活搜索API响应: " + response);

                results = parseAmapResponse(response, searchCity);
                if (results != null && !results.isEmpty()) {
                    System.out.println("高德灵活搜索结果数量: " + results.size());
                    break;
                }
            }

            if (results.isEmpty()) {
                StringBuilder urlBuilder = new StringBuilder(amapApiUrl + "/place/text?key=" + amapApiKey +
                        "&keywords=" + URLEncoder.encode(keywords, "UTF-8") +
                        "&output=json&offset=0&page_size=20");

                String url = urlBuilder.toString();
                System.out.println("高德全国搜索API请求URL: " + url);
                String response = restTemplate.getForObject(new java.net.URI(url), String.class);
                System.out.println("高德全国搜索API响应: " + response);

                results = parseAmapResponse(response, null);
                System.out.println("高德全国搜索结果数量: " + results.size());
            }

            if (!results.isEmpty()) {
                // searchCache.put(cacheKey, results);
                // System.out.println("缓存搜索结果: " + cacheKey + " = " + results.size() + "条");
            }

            return results;
        } catch (Exception e) {
            System.err.println("灵活搜索景点失败: " + e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private String[] getCityCodes(String city) {
        if (city == null || city.isEmpty()) {
            return new String[]{};
        }

        Map<String, String> cityCodeMap = new HashMap<>();
        cityCodeMap.put("北京", "010");
        cityCodeMap.put("上海", "021");
        cityCodeMap.put("广州", "020");
        cityCodeMap.put("深圳", "0755");
        cityCodeMap.put("杭州", "0571");
        cityCodeMap.put("成都", "028");
        cityCodeMap.put("重庆", "023");
        cityCodeMap.put("西安", "029");
        cityCodeMap.put("南京", "025");
        cityCodeMap.put("苏州", "0512");
        cityCodeMap.put("武汉", "027");
        cityCodeMap.put("长沙", "0731");
        cityCodeMap.put("天津", "022");
        cityCodeMap.put("青岛", "0532");
        cityCodeMap.put("大连", "0411");
        cityCodeMap.put("沈阳", "024");
        cityCodeMap.put("厦门", "0592");
        cityCodeMap.put("昆明", "0871");
        cityCodeMap.put("丽江", "0888");

        String code = cityCodeMap.get(city);
        if (code != null) {
            return new String[]{code, city};
        }
        return new String[]{city};
    }

    private AttractionSearchResult selectBestMatch(List<AttractionSearchResult> results, String keyword) {
        if (results == null || results.isEmpty()) {
            return null;
        }

        for (AttractionSearchResult result : results) {
            if (result.getName() != null && result.getName().contains(keyword)) {
                System.out.println("选择最佳匹配(包含关键词): " + result.getName());
                return result;
            }
        }

        String cleanedKeyword = keyword.replaceAll("[（()）【】\\[\\]「」『』〖〗《》〈〉''\"\"''<>]", "");
        if (!cleanedKeyword.equals(keyword)) {
            for (AttractionSearchResult result : results) {
                if (result.getName() != null && result.getName().contains(cleanedKeyword)) {
                    System.out.println("选择最佳匹配(包含清理后关键词): " + result.getName());
                    return result;
                }
            }
        }

        String[] keywordParts = cleanedKeyword.split("[\\s\\-—_]");
        for (String part : keywordParts) {
            if (part.length() >= 2) {
                for (AttractionSearchResult result : results) {
                    if (result.getName() != null && result.getName().contains(part)) {
                        System.out.println("选择最佳匹配(包含关键词部分 '" + part + "'): " + result.getName());
                        return result;
                    }
                }
            }
        }

        for (int len = Math.min(4, cleanedKeyword.length()); len >= 2; len--) {
            String prefix = cleanedKeyword.substring(0, len);
            for (AttractionSearchResult result : results) {
                if (result.getName() != null && result.getName().contains(prefix)) {
                    System.out.println("选择最佳匹配(包含关键词前缀 '" + prefix + "'): " + result.getName());
                    return result;
                }
            }
        }

        System.out.println("选择最佳匹配(第一个结果): " + results.get(0).getName());
        return results.get(0);
    }

    private List<AttractionSearchResult> searchWithRetry(String attractionName, String city) {
        System.out.println("========== searchWithRetry START ==========");
        System.out.println("尝试多种搜索策略 for: " + attractionName + ", city: " + city);

        String normalizedName = attractionName
                .replace("（", "(")
                .replace("）", ")")
                .replace("【", "[")
                .replace("】", "]");
        System.out.println("规范化景点名称: " + attractionName + " -> " + normalizedName);

        Map<String, String> specialAttractions = new HashMap<>();
        specialAttractions.put("西湖", "杭州西湖风景名胜区-白堤");
        specialAttractions.put("西湖(北山街-孤山段)", "杭州西湖风景名胜区-白堤");
        specialAttractions.put("茅家埠", "茅家埠");
        specialAttractions.put("浴鹄湾", "浴鹄湾");
        specialAttractions.put("浙江省博物馆(武林馆区)", "浙江省博物馆(之江馆区)");
        specialAttractions.put("杭州图书馆(市民中心馆)", "杭州图书馆");

        System.out.println("specialAttractions.containsKey(normalizedName): " + specialAttractions.containsKey(normalizedName));
        System.out.println("normalizedName: [" + normalizedName + "]");

        if (specialAttractions.containsKey(normalizedName)) {
            String mappedName = specialAttractions.get(normalizedName);
            System.out.println("使用特殊景点映射: " + normalizedName + " -> " + mappedName);
            String cleanedMappedName = mappedName.replaceAll("[（()）【】\\[\\]「」『』〖〗《》〈〉''\"\"''<>]", "");
            System.out.println("清理映射名称: " + mappedName + " -> " + cleanedMappedName);
            List<AttractionSearchResult> results = searchAttractionsFlexible(city, cleanedMappedName);
            System.out.println("特殊景点映射搜索结果: " + (results != null ? results.size() : "null"));
            if (results != null && !results.isEmpty()) {
                for (AttractionSearchResult result : results) {
                    System.out.println("检查结果: " + result.getName() + " vs " + mappedName);
                    if (result.getName() != null && (result.getName().equals(mappedName) || result.getName().equals(cleanedMappedName))) {
                        System.out.println("特殊景点映射精确匹配: " + mappedName);
                        List<AttractionSearchResult> exactMatch = new ArrayList<>();
                        exactMatch.add(result);
                        return exactMatch;
                    }
                }
                System.out.println("特殊景点映射搜索成功(无精确匹配): " + mappedName);
                return results;
            }
        }

        String cleanedName = attractionName.replaceAll("[（()）【】\\[\\]「」『』〖〗《》〈〉‘’\"\"''<>]", "");
        if (!cleanedName.equals(attractionName)) {
            System.out.println("清理特殊字符: " + attractionName + " -> " + cleanedName);
            List<AttractionSearchResult> results = searchAttractionsFlexible(city, cleanedName);
            if (results != null && !results.isEmpty()) {
                AttractionSearchResult bestMatch = selectBestMatch(results, cleanedName);
                if (bestMatch != null) {
                    System.out.println("清理特殊字符搜索成功(最佳匹配): " + bestMatch.getName());
                    List<AttractionSearchResult> matchedResults = new ArrayList<>();
                    matchedResults.add(bestMatch);
                    return matchedResults;
                }
            }
        }

        String[] suffixToRemove = {"博物馆", "景区", "公园", "广场", "大厦", "中心", "酒店", "餐厅",
                                   "夜景游船", "游船", "创意园", "创意园区", "展览馆", "纪念馆",
                                   "故居", "遗址", "古镇", "老街", "北山街-孤山段", "北山街", "孤山段"};

        for (String suffix : suffixToRemove) {
            if (attractionName.contains(suffix)) {
                String simplified = attractionName.replace(suffix, "").trim();
                if (simplified.length() >= 2) {
                    System.out.println("尝试简化搜索: " + simplified);
                    List<AttractionSearchResult> results = searchAttractionsFlexible(city, simplified);
                    if (results != null && !results.isEmpty()) {
                        System.out.println("简化搜索成功: " + simplified);
                        return results;
                    }
                }
            }
        }

        if (city != null && !city.isEmpty()) {
            String withCityPrefix = city + attractionName;
            System.out.println("尝试带传入城市前缀搜索: " + withCityPrefix);
            List<AttractionSearchResult> results = searchAttractionsFlexible(city, withCityPrefix);
            if (results != null && !results.isEmpty()) {
                for (AttractionSearchResult result : results) {
                    if (result.getName() != null && result.getName().contains(attractionName)) {
                        System.out.println("带传入城市前缀搜索成功(包含关键词): " + result.getName());
                        List<AttractionSearchResult> matchedResults = new ArrayList<>();
                        matchedResults.add(result);
                        return matchedResults;
                    }
                }
                System.out.println("带传入城市前缀搜索成功(无匹配): " + withCityPrefix);
                return results;
            }
        }

        String[] keywords = {"景点", "旅游", "观光"};
        for (String keyword : keywords) {
            String combined = cleanedName + " " + keyword;
            System.out.println("尝试关键词组合搜索: " + combined);
            List<AttractionSearchResult> results = searchAttractionsFlexible(city, combined);
            if (results != null && !results.isEmpty()) {
                for (AttractionSearchResult result : results) {
                    if (result.getName() != null && result.getName().contains(attractionName)) {
                        System.out.println("关键词组合搜索成功(包含关键词): " + result.getName());
                        List<AttractionSearchResult> matchedResults = new ArrayList<>();
                        matchedResults.add(result);
                        return matchedResults;
                    }
                }
                System.out.println("关键词组合搜索成功(无匹配): " + combined);
                return results;
            }
        }

        System.out.println("所有重试策略都失败");
        return null;
    }

    public TrafficInfo getTrafficStatus(Double longitude, Double latitude) {
        if (amapApiKey == null || amapApiKey.isEmpty()) {
            return TrafficInfo.builder()
                    .congestionIndex(0.0)
                    .status("未知")
                    .speed(0.0)
                    .build();
        }

        try {
            double delta = 0.01;
            String rectangle = String.format("%.6f,%.6f;%.6f,%.6f",
                    longitude - delta, latitude - delta,
                    longitude + delta, latitude + delta);

            String url = amapApiUrl + "/traffic/status/rectangle?key=" + amapApiKey +
                    "&rectangle=" + rectangle + "&level=5&extensions=all";

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if ("1".equals(root.path("status").asText()) &&
                "10000".equals(root.path("infocode").asText())) {

                JsonNode trafficInfo = root.path("trafficinfo");
                JsonNode evaluation = trafficInfo.path("evaluation");

                double congestionIndex = evaluation.path("congestion_index").asDouble();
                String status = evaluation.path("status").asText();
                double speed = evaluation.path("speed").asDouble();

                return TrafficInfo.builder()
                        .congestionIndex(congestionIndex)
                        .status(status)
                        .speed(speed)
                        .build();
            }
        } catch (Exception e) {
            System.err.println("获取交通态势失败: " + e.getMessage());
        }

        return TrafficInfo.builder()
                .congestionIndex(0.0)
                .status("未知")
                .speed(0.0)
                .build();
    }

    public CrowdForecastResponse getCrowdForecast(Long attractionId) {
       //从数据库取景点信息（包含经纬度）
        Attraction attraction = attractionMapper.selectById(attractionId);
        if (attraction == null) {
            return null;
        }

        boolean isHot = attraction.getRating() != null && attraction.getRating() >= 4.5 ||
                       attraction.getCommentCount() != null && attraction.getCommentCount() > 1000;

        List<CrowdForecastResponse.DailyForecast> forecasts = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            //查节假日
            HolidayInfo holiday = getHolidayInfo(date.toString());
            boolean isHoliday = holiday.getIsHoliday() != null && holiday.getIsHoliday();
            boolean isWorkday = holiday.getIsWorkday() != null && holiday.getIsWorkday();

            boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
         // ③ 用城市名调天气API
            WeatherInfo weather = getWeather(attraction.getCity(), date.toString());
            boolean isRainy = weather.getText() != null && weather.getText().contains("雨");
            boolean isSnowy = weather.getText() != null && weather.getText().contains("雪");
            boolean isSunny = weather.getText() != null && weather.getText().contains("晴");
            boolean isFoggy = weather.getText() != null && weather.getText().contains("雾");
            boolean isHazy = weather.getText() != null && weather.getText().contains("霾");
            // ② 用经纬度调高德交通API
            TrafficInfo traffic = getTrafficStatus(
                    attraction.getLongitude() != null ? attraction.getLongitude().doubleValue() : 0.0,
                    attraction.getLatitude() != null ? attraction.getLatitude().doubleValue() : 0.0);
            boolean isCongested = "拥堵".equals(traffic.getStatus()) || "严重拥堵".equals(traffic.getStatus());

            double congestionIndex = traffic.getCongestionIndex() != null ? traffic.getCongestionIndex() : 0.0;

            double baseCrowd = calculateBaseCrowd(attraction, date);
//7因子加权算法
            double holidayFactor = calculateHolidayFactor(isHoliday, isWorkday, holiday.getHolidayName());
            double weekendFactor = calculateWeekendFactor(isWeekend, isWorkday);
            double weatherFactor = calculateWeatherFactor(isRainy, isSnowy, isSunny, isFoggy, isHazy, attraction);
            double hotFactor = calculateHotFactor(attraction);
            double trafficFactor = calculateTrafficFactor(congestionIndex);
            double seasonFactor = calculateSeasonFactor(date);
            double timeFactor = calculateTimeFactor(date);

            double weightedCrowd = baseCrowd * holidayFactor * weekendFactor *
                                   weatherFactor * hotFactor * trafficFactor *
                                   seasonFactor * timeFactor;

            weightedCrowd = Math.min(100.0, Math.max(0.0, weightedCrowd));

            String crowdLevel = calculateCrowdLevel(weightedCrowd);

            String recommendation = generateRecommendation(crowdLevel, isHoliday, isRainy, isCongested, weather.getText());

            List<String> peakHours = generatePeakHours(crowdLevel, isHoliday, isWeekend);
            String bestTime = generateBestTime(crowdLevel, isHoliday, isWeekend);

            CrowdForecastResponse.DailyForecast forecast = CrowdForecastResponse.DailyForecast.builder()
                    .date(date.toString())
                    .dayOfWeek(date.getDayOfWeek().toString())
                    .isHoliday(isHoliday)
                    .holidayName(holiday.getHolidayName())
                    .weather(weather.getText())
                    .trafficStatus(traffic.getStatus())
                    .baseCrowd(baseCrowd)
                    .weightedCrowd((double) Math.round(weightedCrowd * 100.0) / 100.0)
                    .crowdLevel(crowdLevel)
                    .peakHours(peakHours)
                    .bestTime(bestTime)
                    .recommendation(recommendation)
                    .factors(CrowdForecastResponse.Factors.builder()
                            .holiday(holidayFactor)
                            .weekend(weekendFactor)
                            .weather(weatherFactor)
                            .hot(hotFactor)
                            .traffic(trafficFactor)
                            .build())
                    .build();

            forecasts.add(forecast);
        }

        return CrowdForecastResponse.builder()
                .attractionId(attractionId)
                .attractionName(attraction.getName())
                .coordinates(attraction.getLongitude() + "," + attraction.getLatitude())
                .forecasts(forecasts)
                .build();
    }

    public CrowdForecastResponse getCrowdForecastByName(String attractionName, String city) {
        try {
            System.out.println("========== getCrowdForecastByName START ==========");
            System.out.println("attractionName: [" + attractionName + "], city: [" + city + "]");

            List<AttractionSearchResult> searchResults = searchWithRetry(attractionName, city);
            System.out.println("searchWithRetry returned: " + (searchResults != null ? searchResults.size() + " results" : "null"));

            if (searchResults == null || searchResults.isEmpty()) {
                System.out.println("searchWithRetry failed, trying searchAttractionsFlexible with city");
                searchResults = searchAttractionsFlexible(city, attractionName);
            }

            if (searchResults == null || searchResults.isEmpty()) {
                System.out.println("searchAttractionsFlexible with city failed, trying without city");
                searchResults = searchAttractionsFlexible("", attractionName);
            }

            if (searchResults == null || searchResults.isEmpty()) {
                System.err.println("无法找到景点: " + attractionName);
                return null;
            }

            AttractionSearchResult amapResult = searchResults.get(0);
            System.out.println("Selected attraction: " + amapResult.getName() + " at " + amapResult.getLocation());

            Attraction attraction = new Attraction();
            attraction.setId(0L);
            attraction.setName(amapResult.getName());
            attraction.setCity(amapResult.getCity());
            attraction.setAddress(amapResult.getAddress());

            if (amapResult.getLocation() != null && amapResult.getLocation().contains(",")) {
                String[] parts = amapResult.getLocation().split(",");
                attraction.setLongitude(new java.math.BigDecimal(parts[0]));
                attraction.setLatitude(new java.math.BigDecimal(parts[1]));
            }

            List<CrowdForecastResponse.DailyForecast> forecasts = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                LocalDate date = LocalDate.now().plusDays(i);

                HolidayInfo holiday = getHolidayInfo(date.toString());
                boolean isHoliday = holiday.getIsHoliday() != null && holiday.getIsHoliday();
                boolean isWorkday = holiday.getIsWorkday() != null && holiday.getIsWorkday();

                boolean isWeekend = date.getDayOfWeek().getValue() >= 6;

                String weatherCity = amapResult.getCity() != null ? amapResult.getCity() : city;
                WeatherInfo weather = getWeather(weatherCity, date.toString());
                boolean isRainy = weather.getText() != null && weather.getText().contains("雨");
                boolean isSnowy = weather.getText() != null && weather.getText().contains("雪");
                boolean isSunny = weather.getText() != null && weather.getText().contains("晴");
                boolean isFoggy = weather.getText() != null && weather.getText().contains("雾");
                boolean isHazy = weather.getText() != null && weather.getText().contains("霾");

                double lon = attraction.getLongitude() != null ? attraction.getLongitude().doubleValue() : 0.0;
                double lat = attraction.getLatitude() != null ? attraction.getLatitude().doubleValue() : 0.0;
                TrafficInfo traffic = getTrafficStatus(lon, lat);
                boolean isCongested = "拥堵".equals(traffic.getStatus()) || "严重拥堵".equals(traffic.getStatus());

                double congestionIndex = traffic.getCongestionIndex() != null ? traffic.getCongestionIndex() : 0.0;

                double baseCrowd = 40.0 + random.nextInt(20);

                double holidayFactor = calculateHolidayFactor(isHoliday, isWorkday, holiday.getHolidayName());
                double weekendFactor = calculateWeekendFactor(isWeekend, isWorkday);
                double weatherFactor = calculateWeatherFactor(isRainy, isSnowy, isSunny, isFoggy, isHazy, attraction);
                double hotFactor = 1.0;
                double trafficFactor = calculateTrafficFactor(congestionIndex);
                double seasonFactor = calculateSeasonFactor(date);
                double timeFactor = calculateTimeFactor(date);

                double weightedCrowd = baseCrowd * holidayFactor * weekendFactor *
                                       weatherFactor * hotFactor * trafficFactor *
                                       seasonFactor * timeFactor;

                weightedCrowd = Math.min(100.0, Math.max(0.0, weightedCrowd));

                String crowdLevel = calculateCrowdLevel(weightedCrowd);
                String recommendation = generateRecommendation(crowdLevel, isHoliday, isRainy, isCongested, weather.getText());
                List<String> peakHours = generatePeakHours(crowdLevel, isHoliday, isWeekend);
                String bestTime = generateBestTime(crowdLevel, isHoliday, isWeekend);

                CrowdForecastResponse.DailyForecast forecast = CrowdForecastResponse.DailyForecast.builder()
                        .date(date.toString())
                        .dayOfWeek(date.getDayOfWeek().toString())
                        .isHoliday(isHoliday)
                        .holidayName(holiday.getHolidayName())
                        .weather(weather.getText())
                        .trafficStatus(traffic.getStatus())
                        .baseCrowd(baseCrowd)
                        .weightedCrowd((double) Math.round(weightedCrowd * 100.0) / 100.0)
                        .crowdLevel(crowdLevel)
                        .peakHours(peakHours)
                        .bestTime(bestTime)
                        .recommendation(recommendation)
                        .factors(CrowdForecastResponse.Factors.builder()
                                .holiday(holidayFactor)
                                .weekend(weekendFactor)
                                .weather(weatherFactor)
                                .hot(hotFactor)
                                .traffic(trafficFactor)
                                .build())
                        .build();

                forecasts.add(forecast);
            }

            return CrowdForecastResponse.builder()
                    .attractionId(0L)
                    .attractionName(attraction.getName())
                    .coordinates(attraction.getLongitude() + "," + attraction.getLatitude())
                    .forecasts(forecasts)
                    .build();

        } catch (Exception e) {
            System.err.println("按名称获取客流预测失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private double calculateBaseCrowd(Attraction attraction, LocalDate date) {
        double baseCrowd = 40.0;

        if (attraction.getRating() != null) {
            baseCrowd += (attraction.getRating() - 3.0) * 10.0;
        }

        if (attraction.getCommentCount() != null) {
            baseCrowd += Math.min(20.0, attraction.getCommentCount() / 100.0);
        }

        if (attraction.getTicketPrice() != null && attraction.getTicketPrice().doubleValue() == 0) {
            baseCrowd += 15.0;
        }

        return baseCrowd;
    }

    private double calculateHolidayFactor(boolean isHoliday, boolean isWorkday, String holidayName) {
        if (!isHoliday) {
            return 1.0;
        }

        if (holidayName != null) {
            if (holidayName.contains("春节") || holidayName.contains("国庆")) {
                return 2.0;
            } else if (holidayName.contains("元旦") || holidayName.contains("五一")) {
                return 1.8;
            } else if (holidayName.contains("清明") || holidayName.contains("端午") || holidayName.contains("中秋")) {
                return 1.6;
            }
        }

        return 1.5;
    }

    private double calculateWeekendFactor(boolean isWeekend, boolean isWorkday) {
        if (isWorkday) {
            return 1.0;
        }

        if (isWeekend) {
            return 1.4;
        }

        return 1.0;
    }

    private double calculateWeatherFactor(boolean isRainy, boolean isSnowy, boolean isSunny,
                                         boolean isFoggy, boolean isHazy, Attraction attraction) {
        boolean isIndoor = attraction.getDescription() != null &&
                         (attraction.getDescription().contains("室内") ||
                          attraction.getDescription().contains("博物馆") ||
                          attraction.getDescription().contains("展馆"));

        if (isIndoor) {
            if (isRainy || isSnowy) {
                return 1.2;
            }
            return 1.0;
        }

        if (isRainy) {
            return 0.5;
        }

        if (isSnowy) {
            return 0.4;
        }

        if (isFoggy || isHazy) {
            return 0.7;
        }

        if (isSunny) {
            return 1.15;
        }

        return 1.0;
    }

    private double calculateHotFactor(Attraction attraction) {
        double rating = attraction.getRating() != null ? attraction.getRating() : 0.0;
        int commentCount = attraction.getCommentCount() != null ? attraction.getCommentCount() : 0;

        if (rating >= 4.8 && commentCount > 5000) {
            return 1.5;
        } else if (rating >= 4.5 && commentCount > 2000) {
            return 1.3;
        } else if (rating >= 4.3) {
            return 1.15;
        }

        return 1.0;
    }

    private double calculateTrafficFactor(double congestionIndex) {
        if (congestionIndex >= 8.0) {
            return 1.6;
        } else if (congestionIndex >= 6.0) {
            return 1.4;
        } else if (congestionIndex >= 4.0) {
            return 1.2;
        } else if (congestionIndex >= 2.0) {
            return 1.05;
        }

        return 1.0;
    }

    private double calculateSeasonFactor(LocalDate date) {
        int month = date.getMonthValue();

        if (month >= 3 && month <= 5) {
            return 1.25;
        } else if (month >= 9 && month <= 11) {
            return 1.2;
        } else if (month >= 6 && month <= 8) {
            return 1.15;
        } else {
            return 0.85;
        }
    }

    private double calculateTimeFactor(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();

        if (dayOfWeek == 5) {
            return 1.2;
        } else if (dayOfWeek == 1) {
            return 0.9;
        }

        return 1.0;
    }

    private String calculateCrowdLevel(double weightedCrowd) {
        if (weightedCrowd < 25) {
            return "低";
        } else if (weightedCrowd < 55) {
            return "中";
        } else if (weightedCrowd < 80) {
            return "较高";
        } else {
            return "高";
        }
    }

    private String generateRecommendation(String crowdLevel, boolean isHoliday, boolean isRainy, boolean isCongested, String weather) {
        if ("高".equals(crowdLevel)) {
            if (isHoliday && isRainy && isCongested) {
                return "节假日+雨天+拥堵，建议7:00前入园或改期";
            } else if (isHoliday) {
                return "节假日客流高峰，建议7:30前入园避开高峰";
            } else if (isCongested) {
                return "道路拥堵，建议提前出发或选择公共交通";
            } else {
                return "客流较大，建议7:30前入园";
            }
        } else if ("较高".equals(crowdLevel)) {
            if (isHoliday) {
                return "节假日客流较高，建议8:00前入园";
            } else if (isRainy) {
                return "雨天客流较高，建议上午游览";
            } else {
                return "客流较高，建议上午游览";
            }
        } else if ("中".equals(crowdLevel)) {
            if (isRainy) {
                return "雨天客流适中，建议上午游览";
            } else {
                return "客流适中，建议上午游览";
            }
        } else {
            return "预计人流较少，适合游览";
        }
    }

    private List<String> generatePeakHours(String crowdLevel, boolean isHoliday, boolean isWeekend) {
        List<String> peakHours = new ArrayList<>();

        if ("高".equals(crowdLevel)) {
            if (isHoliday || isWeekend) {
                peakHours.add("07:30-12:00");
                peakHours.add("13:30-17:30");
            } else {
                peakHours.add("08:00-12:00");
                peakHours.add("13:00-17:00");
            }
        } else if ("较高".equals(crowdLevel)) {
            if (isHoliday || isWeekend) {
                peakHours.add("09:00-12:00");
                peakHours.add("14:00-17:00");
            } else {
                peakHours.add("10:00-12:00");
                peakHours.add("14:00-16:30");
            }
        } else if ("中".equals(crowdLevel)) {
            peakHours.add("10:00-12:00");
            peakHours.add("14:00-16:00");
        }

        return peakHours;
    }

    private String generateBestTime(String crowdLevel, boolean isHoliday, boolean isWeekend) {
        if ("高".equals(crowdLevel)) {
            if (isHoliday || isWeekend) {
                return "06:30-07:30";
            } else {
                return "07:00-08:00";
            }
        } else if ("较高".equals(crowdLevel)) {
            if (isHoliday || isWeekend) {
                return "07:30-08:30";
            } else {
                return "08:30-09:30";
            }
        } else if ("中".equals(crowdLevel)) {
            return "09:00-10:00";
        } else {
            return "全天皆可";
        }
    }
}
