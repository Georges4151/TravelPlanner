CREATE TABLE IF NOT EXISTS `reminder` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `itinerary_id` BIGINT NOT NULL,
    `title` VARCHAR(100) NOT NULL,
    `content` TEXT,
    `remind_time` DATETIME NOT NULL,
    `type` VARCHAR(20) DEFAULT 'general',
    `status` INT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_itinerary_id` (`itinerary_id`),
    INDEX `idx_remind_time` (`remind_time`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
