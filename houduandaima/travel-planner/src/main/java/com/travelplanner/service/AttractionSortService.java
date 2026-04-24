package com.travelplanner.service;

import com.travelplanner.entity.Attraction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class AttractionSortService {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    // 综合排序：距离(40%) + 开放时间(25%) + 游玩时长(20%) + 客流(15%)
    public void sortAttractions(List<Attraction> attractions, String travelStyle, String destination, LocalDate startDate) {
        if (attractions == null || attractions.size() <= 1) {
            return;
        }

        AttractionSortContext context = new AttractionSortContext(travelStyle, destination, startDate);
        attractions.sort(new AttractionComparator(context));
    }

    // 按旅行风格排序（保留原有逻辑但增强）
    public void sortAttractionsByStyle(List<Attraction> attractions, String travelStyle) {
        if (attractions == null || attractions.size() <= 1) {
            return;
        }

        switch (travelStyle) {
            case "错峰优先":
                attractions.sort(Comparator
                        .comparing((Attraction a) -> getCrowdScore(a.getCrowdLevel()))
                        .thenComparing(a -> a.getRating() != null ? -a.getRating() : 0));
                break;
            case "暴走":
                attractions.sort(Comparator
                        .comparing((Attraction a) -> a.getPlayDuration() != null ? -a.getPlayDuration() : 0)
                        .thenComparing(a -> a.getRating() != null ? -a.getRating() : 0));
                break;
            case "亲子":
                attractions.sort(Comparator
                        .comparing((Attraction a) -> a.getPlayDuration() != null ? a.getPlayDuration() : 0)
                        .thenComparing(a -> a.getRating() != null ? -a.getRating() : 0));
                break;
            case "休闲":
                attractions.sort(Comparator
                        .comparing((Attraction a) -> getOpenTimeScore(a.getOpenTime()))
                        .thenComparing(a -> a.getRating() != null ? -a.getRating() : 0));
                break;
            default:
                attractions.sort(Comparator.comparing((Attraction a) -> a.getRating() != null ? -a.getRating() : 0));
                break;
        }
    }

    // 综合评分
    private double calculateComprehensiveScore(Attraction attraction, AttractionSortContext context) {
        double distanceScore = getDistanceScore(attraction, context);
        double openTimeScore = getOpenTimeScore(attraction.getOpenTime());
        double durationScore = getDurationScore(attraction.getPlayDuration());
        double crowdScore = getCrowdScore(attraction.getCrowdLevel());

        return distanceScore * 0.40
             + openTimeScore * 0.25
             + durationScore * 0.20
             + crowdScore * 0.15;
    }

    // 距离得分（越近越高，归一化到 0-1）
    private double getDistanceScore(Attraction attraction, AttractionSortContext context) {
        if (attraction.getLongitude() == null || attraction.getLatitude() == null) {
            return 0.5;
        }

        // 计算与目的地中心的距离（简化为经纬度差）
        double distance = calculateDistance(
                attraction.getLongitude().doubleValue(),
                attraction.getLatitude().doubleValue(),
                context.getDestinationLongitude(),
                context.getDestinationLatitude()
        );

        // 距离越近得分越高，5公里内满分，20公里外0分
        if (distance <= 5) return 1.0;
        if (distance >= 20) return 0.0;
        return 1.0 - (distance - 5) / 15.0;
    }

    // 开放时间得分（开门越早、关门越晚得分越高）
    private double getOpenTimeScore(String openTime) {
        if (openTime == null || openTime.isEmpty()) {
            return 0.5;
        }

        try {
            int openHour = extractOpenHour(openTime);
            int closeHour = extractCloseHour(openTime);

            // 开门早（7-9点）得分高
            double openScore = 0.0;
            if (openHour >= 7 && openHour <= 9) {
                openScore = 1.0 - (openHour - 7) / 2.0;
            } else if (openHour < 7) {
                openScore = 1.0;
            } else {
                openScore = Math.max(0, 1.0 - (openHour - 9) / 5.0);
            }

            // 关门晚（18点后）得分高
            double closeScore = 0.0;
            if (closeHour >= 18) {
                closeScore = 1.0;
            } else if (closeHour >= 14) {
                closeScore = (closeHour - 14) / 4.0;
            } else {
                closeScore = 0.0;
            }

            return (openScore + closeScore) / 2.0;
        } catch (Exception e) {
            return 0.5;
        }
    }

    // 游玩时长得分（适中最佳，太短或太长都不好）
    private double getDurationScore(Integer duration) {
        if (duration == null) {
            return 0.5;
        }

        // 1-3 小时最佳
        if (duration >= 60 && duration <= 180) {
            return 1.0;
        } else if (duration < 60) {
            return duration / 60.0;
        } else {
            return Math.max(0.3, 1.0 - (duration - 180) / 240.0);
        }
    }

    // 客流得分（越低越好）
    private double getCrowdScore(String crowdLevel) {
        if ("空闲".equals(crowdLevel)) return 1.0;
        if ("中等".equals(crowdLevel)) return 0.6;
        if ("拥挤".equals(crowdLevel)) return 0.2;
        return 0.5;
    }

    // 计算两点间距离（公里）
    private double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c; // 地球半径 6371 公里
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

    private int extractCloseHour(String openTime) {
        if (openTime == null || openTime.isEmpty()) {
            return 17;
        }
        try {
            if (openTime.contains("-")) {
                String[] parts = openTime.split("-");
                if (parts.length > 1) {
                    String endTime = parts[1].trim();
                    if (endTime.contains(":")) {
                        String[] timeParts = endTime.split(":");
                        if (timeParts.length > 0) {
                            return Integer.parseInt(timeParts[0].trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return 17;
    }

    // 排序上下文
    private static class AttractionSortContext {
        private final String travelStyle;
        private final String destination;
        private final LocalDate startDate;
        private final double destinationLongitude;
        private final double destinationLatitude;

        public AttractionSortContext(String travelStyle, String destination, LocalDate startDate) {
            this.travelStyle = travelStyle;
            this.destination = destination;
            this.startDate = startDate;
            // 简化：使用目的地中心坐标
            this.destinationLongitude = getDestinationLongitude(destination);
            this.destinationLatitude = getDestinationLatitude(destination);
        }

        public String getTravelStyle() { return travelStyle; }
        public String getDestination() { return destination; }
        public LocalDate getStartDate() { return startDate; }
        public double getDestinationLongitude() { return destinationLongitude; }
        public double getDestinationLatitude() { return destinationLatitude; }

        private double getDestinationLongitude(String destination) {
            switch (destination) {
                case "杭州": return 120.15;
                case "成都": return 104.07;
                case "北京": return 116.40;
                case "上海": return 121.47;
                case "西安": return 108.95;
                case "南京": return 118.78;
                case "重庆": return 106.55;
                case "广州": return 113.26;
                case "深圳": return 114.06;
                case "厦门": return 118.09;
                default: return 120.0;
            }
        }

        private double getDestinationLatitude(String destination) {
            switch (destination) {
                case "杭州": return 30.25;
                case "成都": return 30.57;
                case "北京": return 39.90;
                case "上海": return 31.23;
                case "西安": return 34.27;
                case "南京": return 32.06;
                case "重庆": return 29.56;
                case "广州": return 23.13;
                case "深圳": return 22.54;
                case "厦门": return 24.48;
                default: return 30.0;
            }
        }
    }

    // 综合比较器
    private class AttractionComparator implements Comparator<Attraction> {
        private final AttractionSortContext context;

        public AttractionComparator(AttractionSortContext context) {
            this.context = context;
        }

        @Override
        public int compare(Attraction a, Attraction b) {
            double scoreA = calculateComprehensiveScore(a, context);
            double scoreB = calculateComprehensiveScore(b, context);
            return Double.compare(scoreB, scoreA);
        }
    }
}
