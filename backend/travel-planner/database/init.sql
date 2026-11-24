CREATE DATABASE IF NOT EXISTS travel_planner DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE travel_planner;

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(20),
    `email` VARCHAR(100),
    `avatar` VARCHAR(255),
    `member_level` INT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_username` (`username`),
    INDEX `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `attraction` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `city` VARCHAR(50) NOT NULL,
    `province` VARCHAR(50) NOT NULL,
    `description` TEXT,
    `address` VARCHAR(255),
    `latitude` DECIMAL(10,6),
    `longitude` DECIMAL(10,6),
    `open_time` VARCHAR(100),
    `ticket_price` DECIMAL(10,2) DEFAULT 0,
    `booking_method` VARCHAR(100),
    `play_duration` INT,
    `crowd_level` VARCHAR(20) DEFAULT 'medium',
    `traffic_info` TEXT,
    `facilities` VARCHAR(255),
    `image_url` VARCHAR(255),
    `rating` DECIMAL(2,1) DEFAULT 5.0,
    `comment_count` INT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_city` (`city`),
    INDEX `idx_province` (`province`),
    INDEX `idx_rating` (`rating` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `itinerary` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `title` VARCHAR(100) NOT NULL,
    `destination` VARCHAR(50) NOT NULL,
    `days` INT NOT NULL,
    `people_count` INT DEFAULT 1,
    `budget` DECIMAL(10,2),
    `travel_style` VARCHAR(50),
    `status` VARCHAR(20) DEFAULT 'draft',
    `start_date` DATE,
    `end_date` DATE,
    `source` VARCHAR(20) DEFAULT 'ai',
    `template_id` BIGINT,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_destination` (`destination`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `itinerary_day` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `itinerary_id` BIGINT NOT NULL,
    `day_number` INT NOT NULL,
    `date` VARCHAR(20),
    `weather` VARCHAR(50),
    `tips` TEXT,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_itinerary_id` (`itinerary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `itinerary_spot` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `itinerary_day_id` BIGINT NOT NULL,
    `attraction_id` BIGINT,
    `attraction_name` VARCHAR(100) NOT NULL,
    `order_num` INT NOT NULL,
    `arrival_time` VARCHAR(20),
    `leave_time` VARCHAR(20),
    `stay_duration` INT,
    `transport_to_next` VARCHAR(100),
    `notes` TEXT,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_itinerary_day_id` (`itinerary_day_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `template` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(100) NOT NULL,
    `destination` VARCHAR(50) NOT NULL,
    `days` INT NOT NULL,
    `style` VARCHAR(50),
    `crowd` VARCHAR(20),
    `description` TEXT,
    `cover_image` VARCHAR(255),
    `user_id` BIGINT,
    `author_name` VARCHAR(50),
    `usage_count` INT DEFAULT 0,
    `budget` DECIMAL(10,2),
    `is_official` TINYINT(1) DEFAULT 0,
    `is_hot` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_destination` (`destination`),
    INDEX `idx_usage_count` (`usage_count` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `template_day` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `template_id` BIGINT NOT NULL,
    `day_number` INT NOT NULL,
    `date` VARCHAR(20),
    `weather` VARCHAR(50),
    `tips` TEXT,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `template_spot` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `template_day_id` BIGINT NOT NULL,
    `attraction_name` VARCHAR(100) NOT NULL,
    `order_num` INT NOT NULL,
    `arrival_time` VARCHAR(10),
    `leave_time` VARCHAR(10),
    `stay_duration` INT,
    `transport_to_next` VARCHAR(100),
    `notes` TEXT,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_template_day_id` (`template_day_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `budget` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `itinerary_id` BIGINT NOT NULL,
    `total_budget` DECIMAL(10,2) NOT NULL,
    `transport_budget` DECIMAL(10,2),
    `hotel_budget` DECIMAL(10,2),
    `food_budget` DECIMAL(10,2),
    `ticket_budget` DECIMAL(10,2),
    `other_budget` DECIMAL(10,2),
    `actual_transport` DECIMAL(10,2),
    `actual_hotel` DECIMAL(10,2),
    `actual_food` DECIMAL(10,2),
    `actual_ticket` DECIMAL(10,2),
    `actual_other` DECIMAL(10,2),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_itinerary_id` (`itinerary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `expense` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `itinerary_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `category` VARCHAR(20) NOT NULL,
    `item_name` VARCHAR(100) NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL,
    `pay_method` VARCHAR(20),
    `notes` TEXT,
    `expense_time` DATETIME,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_itinerary_id` (`itinerary_id`),
    INDEX `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `checklist` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `itinerary_id` BIGINT NOT NULL,
    `item_name` VARCHAR(100) NOT NULL,
    `category` VARCHAR(50) DEFAULT 'essential',
    `is_checked` TINYINT(1) DEFAULT 0,
    `notes` VARCHAR(255),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_itinerary_id` (`itinerary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `collaborator` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `itinerary_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `role` VARCHAR(20) DEFAULT 'editor',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_itinerary_user` (`itinerary_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `hotel` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `city` VARCHAR(50) NOT NULL,
    `address` VARCHAR(255),
    `price` DECIMAL(10,2),
    `room_type` VARCHAR(50),
    `rating` DECIMAL(2,1) DEFAULT 5.0,
    `image_url` VARCHAR(255),
    `facilities` VARCHAR(255),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `restaurant` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `city` VARCHAR(50) NOT NULL,
    `address` VARCHAR(255),
    `avg_price` DECIMAL(10,2),
    `rating` DECIMAL(2,1) DEFAULT 5.0,
    `cuisine_type` VARCHAR(50),
    `image_url` VARCHAR(255),
    `business_hours` VARCHAR(100),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_city` (`city`),
    INDEX `idx_cuisine_type` (`cuisine_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `transport` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(20) NOT NULL,
    `departure` VARCHAR(50) NOT NULL,
    `arrival` VARCHAR(50) NOT NULL,
    `departure_time` VARCHAR(20),
    `arrival_time` VARCHAR(20),
    `duration` INT,
    `price` VARCHAR(20),
    `provider` VARCHAR(50),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_departure` (`departure`),
    INDEX `idx_arrival` (`arrival`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `crowd_data` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `attraction_id` BIGINT NOT NULL,
    `date` VARCHAR(20) NOT NULL,
    `hour` VARCHAR(10) NOT NULL,
    `crowd_level` VARCHAR(20) DEFAULT 'medium',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_attraction_date` (`attraction_id`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (username, password, phone, email) VALUES
('testuser', '123456', '13800138000', 'test@example.com'),
('admin', 'admin123', '13900139000', 'admin@example.com');

INSERT INTO `attraction` (name, city, province, description, open_time, ticket_price, play_duration, crowd_level, rating) VALUES
('西湖风景区', '杭州', '浙江', '杭州标志性景点，中国十大风景名胜之一', '全天', 0, 180, '拥挤', 4.8),
('灵隐寺', '杭州', '浙江', '中国佛教著名寺院，历史悠久', '07:00-18:00', 75, 120, '中等', 4.7),
('宋城', '杭州', '浙江', '大型宋代文化主题公园', '10:00-21:00', 300, 240, '中等', 4.5),
('乌镇', '嘉兴', '浙江', '江南水乡古镇，保存完好的明清建筑', '09:00-22:00', 150, 360, '拥挤', 4.6),
('千岛湖', '杭州', '浙江', '千岛之湖，湖光山色美不胜收', '08:00-17:00', 130, 300, '空闲', 4.7);

INSERT INTO `template` (title, destination, days, style, crowd, description, author_name, usage_count, budget, is_official, is_hot) VALUES
('北京4日文化之旅', '北京', 4, '文化', '拥挤', '探访故宫长城，感受首都历史文化', '官方', 1568, 4500, 1, 1),
('上海3日都市游', '上海', 3, '都市', '拥挤', '外滩陆家嘴，现代都市魅力之旅', '官方', 2134, 4000, 1, 1),
('成都4日美食游', '成都', 4, '美食', '中等', '川菜火锅熊猫基地，天府之国美食之旅', '官方', 1876, 3500, 1, 1),
('西安3日古都游', '西安', 3, '文化', '中等', '兵马俑大雁塔，千年古都历史探索', '官方', 1432, 3000, 1, 1),
('丽江大理5日深度游', '丽江', 5, '休闲', '空闲', '古城玉龙雪山，彩云之南的浪漫之旅', '官方', 987, 4500, 1, 1),
('厦门4日海滨度假', '厦门', 4, '海滨', '空闲', '鼓浪屿环岛路，南国海滨风光', '官方', 756, 3800, 1, 1),
('广州3日美食游', '广州', 3, '美食', '中等', '早茶粤菜小蛮腰，舌尖上的广州', '官方', 1123, 3200, 1, 1),
('苏州杭州3日江南游', '苏州', 3, '休闲', '空闲', '园林水乡古镇，诗意江南', '官方', 1654, 2800, 1, 1),
('青岛3日海滨之旅', '青岛', 3, '海滨', '空闲', '栈桥崂山啤酒街，黄海之滨的惬意', '官方', 645, 2800, 1, 0),
('重庆4日山城游', '重庆', 4, '美食', '拥挤', '火锅洪崖洞轻轨穿楼，魔幻山城', '官方', 2087, 3200, 1, 1),
('杭州3日错峰游', '杭州', 3, '休闲', '空闲', '错峰游览西湖，探索隐藏的宝藏景点', '官方', 1256, 3000, 1, 1),
('浙江5日家庭游', '杭州', 5, '亲子', '中等', '完美的家庭旅行，轻松的行程安排', '官方', 896, 5000, 1, 1),
('杭州周末2日游', '杭州', 2, '休闲', '空闲', '短途周末旅行，错峰出行', '旅行博主', 2341, 1500, 0, 1);
