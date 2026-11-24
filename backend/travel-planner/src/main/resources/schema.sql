-- 团队相关表初始化脚本
-- 团队表
CREATE TABLE IF NOT EXISTS `team` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `invite_code` VARCHAR(20) NOT NULL UNIQUE,
    `creator_id` BIGINT NOT NULL,
    `itinerary_id` BIGINT,
    `description` VARCHAR(255),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_invite_code` (`invite_code`),
    INDEX `idx_creator_id` (`creator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 团队成员表
CREATE TABLE IF NOT EXISTS `team_member` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `team_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `role` VARCHAR(20) DEFAULT 'editor',
    `nickname` VARCHAR(50),
    `avatar` VARCHAR(255),
    `join_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_team_user` (`team_id`, `user_id`),
    INDEX `idx_team_id` (`team_id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 团队待办事项表（核心表）
CREATE TABLE IF NOT EXISTS `team_todo` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `team_id` BIGINT NOT NULL,
    `title` VARCHAR(200) NOT NULL,
    `content` TEXT,
    `priority` VARCHAR(20) DEFAULT 'medium',
    `assignee_id` BIGINT,
    `status` VARCHAR(20) DEFAULT 'pending',
    `creator_id` BIGINT NOT NULL,
    `due_date` DATETIME,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_team_id` (`team_id`),
    INDEX `idx_assignee_id` (`assignee_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 团队投票表
CREATE TABLE IF NOT EXISTS `team_vote` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `team_id` BIGINT NOT NULL,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `multiple_choice` TINYINT(1) DEFAULT 0,
    `end_time` DATETIME,
    `creator_id` BIGINT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'active',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_team_id` (`team_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 投票选项表
CREATE TABLE IF NOT EXISTS `team_vote_option` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `vote_id` BIGINT NOT NULL,
    `option_text` VARCHAR(200) NOT NULL,
    `vote_count` INT DEFAULT 0,
    INDEX `idx_vote_id` (`vote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户投票记录表
CREATE TABLE IF NOT EXISTS `team_vote_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `vote_id` BIGINT NOT NULL,
    `option_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_vote_option_user` (`vote_id`, `option_id`, `user_id`),
    INDEX `idx_vote_id` (`vote_id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 团队聊天消息表
CREATE TABLE IF NOT EXISTS `team_message` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `team_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL,
    `message_type` VARCHAR(20) DEFAULT 'text',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_team_id` (`team_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 团队活动动态表
CREATE TABLE IF NOT EXISTS `team_activity` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `team_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `action_type` VARCHAR(50) NOT NULL,
    `target_type` VARCHAR(50),
    `target_id` BIGINT,
    `content` VARCHAR(500),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_team_id` (`team_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
