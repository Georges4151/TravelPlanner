USE travel_planner;

ALTER TABLE `itinerary` 
ADD COLUMN `generate_status` VARCHAR(20) DEFAULT NULL COMMENT '生成状态: generating, completed, failed',
ADD COLUMN `generate_message` VARCHAR(255) DEFAULT NULL COMMENT '生成进度消息';

UPDATE `itinerary` SET `generate_status` = 'completed', `generate_message` = '行程生成成功' WHERE `generate_status` IS NULL;
