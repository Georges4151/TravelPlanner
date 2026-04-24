-- 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `poi_id` VARCHAR(100) NOT NULL,
    `type` VARCHAR(20) NOT NULL COMMENT '收藏类型: attraction, restaurant, hotel',
    `name` VARCHAR(255),
    `address` VARCHAR(500),
    `image_url` VARCHAR(500),
    `rating` VARCHAR(20),
    `price` VARCHAR(50),
    `phone` VARCHAR(50),
    `location` VARCHAR(100),
    `poi_json` TEXT COMMENT 'POI完整JSON数据',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_user_type` (`user_id`, `type`),
    INDEX `idx_user_poi_type` (`user_id`, `poi_id`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';
