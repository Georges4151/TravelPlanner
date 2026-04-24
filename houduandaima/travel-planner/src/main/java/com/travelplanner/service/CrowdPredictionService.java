package com.travelplanner.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CrowdPredictionService {
    //将DateTimeFormatter格式化为年-月-日的形式
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //DATE_FMT用于解析和格式化日期字符串为yyyy-MM-dd的形式
    //创建一个HOLIDAY_DATES静态节假日，后续用的话然后通过DATE_FMT来解析这些日期
    private static final Map<String, String> HOLIDAY_DATES = new HashMap<>();
    static {
        HOLIDAY_DATES.put("2026-01-01", "元旦");
        HOLIDAY_DATES.put("2026-01-02", "元旦");
        HOLIDAY_DATES.put("2026-01-03", "元旦");
        HOLIDAY_DATES.put("2026-02-14", "春节");
        HOLIDAY_DATES.put("2026-02-15", "春节");
        HOLIDAY_DATES.put("2026-02-16", "春节");
        HOLIDAY_DATES.put("2026-02-17", "春节");
        HOLIDAY_DATES.put("2026-02-18", "春节");
        HOLIDAY_DATES.put("2026-02-19", "春节");
        HOLIDAY_DATES.put("2026-02-20", "春节");
        HOLIDAY_DATES.put("2026-04-04", "清明");
        HOLIDAY_DATES.put("2026-04-05", "清明");
        HOLIDAY_DATES.put("2026-04-06", "清明");
        HOLIDAY_DATES.put("2026-05-01", "劳动节");
        HOLIDAY_DATES.put("2026-05-02", "劳动节");
        HOLIDAY_DATES.put("2026-05-03", "劳动节");
        HOLIDAY_DATES.put("2026-05-04", "劳动节");
        HOLIDAY_DATES.put("2026-05-05", "劳动节");
        HOLIDAY_DATES.put("2026-06-19", "端午");
        HOLIDAY_DATES.put("2026-06-20", "端午");
        HOLIDAY_DATES.put("2026-06-21", "端午");
        HOLIDAY_DATES.put("2026-09-25", "中秋");
        HOLIDAY_DATES.put("2026-09-26", "中秋");
        HOLIDAY_DATES.put("2026-09-27", "中秋");
        HOLIDAY_DATES.put("2026-10-01", "国庆");
        HOLIDAY_DATES.put("2026-10-02", "国庆");
        HOLIDAY_DATES.put("2026-10-03", "国庆");
        HOLIDAY_DATES.put("2026-10-04", "国庆");
        HOLIDAY_DATES.put("2026-10-05", "国庆");
        HOLIDAY_DATES.put("2026-10-06", "国庆");
        HOLIDAY_DATES.put("2026-10-07", "国庆");
    }

    //用于预测指定日期的客流量
    public CrowdLevel predict(LocalDate date) {
        double holidayFactor = getHolidayFactor(date);
        double dayOfWeekFactor = getDayOfWeekFactor(date);
        double seasonFactor = getSeasonFactor(date);
        double weatherFactor = getWeatherFactor(date);

        double score = holidayFactor * 0.40 + dayOfWeekFactor * 0.20 + seasonFactor * 0.15 + weatherFactor * 0.25;
        return classify(score);
    }
  //CrowdLevel重载方法提供了更方便的接口，是用于后端自动获取错峰的推测信息，然后为前端返回错峰建议,(应用示例在ltineraryService的1188行)
    public CrowdLevel predict(String dateStr, String destination) {
        LocalDate date = parseDate(dateStr);
        if (date == null) return CrowdLevel.UNKNOWN;
        return predict(date, destination);
    }
    //对应上面的方便接口
    public CrowdLevel predict(LocalDate date, String destination) {
        double holidayFactor = getHolidayFactor(date);        //节假日因素
        double dayOfWeekFactor = getDayOfWeekFactor(date);    //星期几因素
        double seasonFactor = getSeasonFactor(date);    //季节因素
        double weatherFactor = getWeatherFactor(date);  //天气因素

        double score = holidayFactor * 0.40 + dayOfWeekFactor * 0.20 + seasonFactor * 0.15 + weatherFactor * 0.25;
        return classify(score);
    }

    //预测一个日期范围内的平均客流等级,startDate开始时间,endDate是结束日期
    public CrowdLevel predictRange(LocalDate startDate, LocalDate endDate) {
        double totalScore = 0;
        int days = 0;
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            totalScore += calculateScore(current);
            current = current.plusDays(1);
            days++;
        }
        double avgScore = totalScore / days;
        return classify(avgScore);
    }

    // predictRange 的重载方法，主要用于提供更方便的接口，允许用户直接传入字符串格式的日期范围来预测客流等级。startDateStr开始字符日期,endDateStr结束字符日期
    public CrowdLevel predictRange(String startDateStr, String endDateStr) {
        LocalDate start = parseDate(startDateStr);
        LocalDate end = parseDate(endDateStr);
        if (start == null || end == null) return CrowdLevel.UNKNOWN;
        return predictRange(start, end);
    }
//用于详细预测客流量的部分代码，是用户点击错峰推测后调用的主代码
    public CrowdDetail getDetail(LocalDate date) {
        double holidayFactor = getHolidayFactor(date);
        double dayOfWeekFactor = getDayOfWeekFactor(date);
        double seasonFactor = getSeasonFactor(date);
        double weatherFactor = getWeatherFactor(date);

        double score = holidayFactor * 0.40 + dayOfWeekFactor * 0.20 + seasonFactor * 0.15 + weatherFactor * 0.25;

        CrowdDetail detail = new CrowdDetail();
        detail.setDate(date.toString());
        detail.setHolidayFactor(holidayFactor);
        detail.setDayOfWeekFactor(dayOfWeekFactor);
        detail.setSeasonFactor(seasonFactor);
        detail.setWeatherFactor(weatherFactor);
        detail.setScore(score);
        detail.setLevel(classify(score));
        detail.setHolidayName(getHolidayName(date));
        detail.setDayOfWeek(date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.CHINA));
        return detail;
    }
//封装一个权重方法,后面方便调用当然也可以不调用
    private double calculateScore(LocalDate date) {
        return getHolidayFactor(date) * 0.40 + getDayOfWeekFactor(date) * 0.20 +
               getSeasonFactor(date) * 0.15 + getWeatherFactor(date) * 0.25;
    }
//节假日因素 (holidayFactor)
//获取方法：getHolidayFactor
//计算逻辑：
//节假日当天：1.8（最高客流）
//节假日前后一天：1.5（次高客流）
//普通日：1.0（基准客流）
//权重：40%
    private double getHolidayFactor(LocalDate date) {
        String dateStr = date.toString();
        if (HOLIDAY_DATES.containsKey(dateStr)) return 1.8;
        for (Map.Entry<String, String> entry : HOLIDAY_DATES.entrySet()) {
            LocalDate holidayDate = LocalDate.parse(entry.getKey());
            if (date.plusDays(1).equals(holidayDate) || date.minusDays(1).equals(holidayDate)) return 1.5;
        }
        return 1.0;
    }

    // 星期几因素 (dayOfWeekFactor)
    //获取方法：getDayOfWeekFactor
    //计算逻辑：
    //周六：1.3
    //周日：1.2
    //周五：1.1
    //周四：0.85
    //周三：0.8
    //周二：0.75
    //周一：0.7
    //权重：20%
    private double getDayOfWeekFactor(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        switch (dow) {
            case MONDAY: return 0.7;
            case TUESDAY: return 0.75;
            case WEDNESDAY: return 0.8;
            case THURSDAY: return 0.85;
            case FRIDAY: return 1.1;
            case SATURDAY: return 1.3;
            case SUNDAY: return 1.2;
            default: return 1.0;
        }
    }

//季节因素 (seasonFactor)
//获取方法：getSeasonFactor
//计算逻辑：
//旺季（3-5月、7-8月、10月）：1.3
//平季（6月、9月、11月）：1.0
//淡季（12-2月）：0.7
//权重：15%
    private double getSeasonFactor(LocalDate date) {
        int month = date.getMonthValue();
        if ((month >= 3 && month <= 5) || month == 7 || month == 8 || month == 10) return 1.3;
        if (month == 6 || month == 9 || month == 11) return 1.0;
        return 0.7;
    }
//天气因素 (weatherFactor)
//获取方法：getWeatherFactor
//计算逻辑：
//春季（3-5月）：1.1
//秋季（9-11月）：1.05
//夏季（6-8月）：0.85
//冬季（12-2月）：0.8
//权重：25%
    private double getWeatherFactor(LocalDate date) {
        int month = date.getMonthValue();
        if (month >= 6 && month <= 8) return 0.85;
        if (month == 12 || month == 1 || month == 2) return 0.8;
        if (month >= 3 && month <= 5) return 1.1;
        if (month >= 9 && month <= 11) return 1.05;
        return 1.0;
    }
 //classify 方法是用来将客流综合评分转换为客流等级的，是客流预测算法的等级分类函数。
    private CrowdLevel classify(double score) {
        if (score >= 1.3) return CrowdLevel.HIGH;
        else if (score >= 0.9) return CrowdLevel.MEDIUM;
        else return CrowdLevel.LOW;
    }
//parseDate 方法是用来将字符串格式的日期解析为 LocalDate 对象的，它提供了容错处理，提高了日期解析的成功率。
    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FMT);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateStr);
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }
//getHolidayName 方法用于返回节假日名称
    private String getHolidayName(LocalDate date) {
        return HOLIDAY_DATES.getOrDefault(date.toString(), null);
    }

    //CrowdLevel 枚举
    //功能：定义客流等级的枚举值
    public enum CrowdLevel {
        HIGH("高"),
        MEDIUM("中"),
        LOW("低"),
        UNKNOWN("未知");

        private final String label;
        CrowdLevel(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    //CrowdDetail 静态类
    //功能：存储详细的客流预测信息
    //包含字段：
    //date：日期（字符串格式）
    //dayOfWeek：星期几（中文显示）
    //holidayName：节假日名称
    //holidayFactor：节假日因素影响值
    //dayOfWeekFactor：星期几因素影响值
    //seasonFactor：季节因素影响值
    //weatherFactor：天气因素影响值
    //score：综合评分
    //level：客流等级
    //用途：提供详细的客流分析信息，用于生成更准确的错峰推荐和前端展示
    //在
    public static class CrowdDetail {
        private String date;
        private String dayOfWeek;
        private String holidayName;
        private double holidayFactor;
        private double dayOfWeekFactor;
        private double seasonFactor;
        private double weatherFactor;
        private double score;
        private CrowdLevel level;

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public String getHolidayName() { return holidayName; }
        public void setHolidayName(String holidayName) { this.holidayName = holidayName; }
        public double getHolidayFactor() { return holidayFactor; }
        public void setHolidayFactor(double holidayFactor) { this.holidayFactor = holidayFactor; }
        public double getDayOfWeekFactor() { return dayOfWeekFactor; }
        public void setDayOfWeekFactor(double dayOfWeekFactor) { this.dayOfWeekFactor = dayOfWeekFactor; }
        public double getSeasonFactor() { return seasonFactor; }
        public void setSeasonFactor(double seasonFactor) { this.seasonFactor = seasonFactor; }
        public double getWeatherFactor() { return weatherFactor; }
        public void setWeatherFactor(double weatherFactor) { this.weatherFactor = weatherFactor; }
        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
        public CrowdLevel getLevel() { return level; }
        public void setLevel(CrowdLevel level) { this.level = level; }
    }
}
