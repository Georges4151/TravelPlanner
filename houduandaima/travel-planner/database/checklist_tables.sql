-- 出行清单功能数据库设计

USE travel_planner;

-- 1. 物品基础数据表（预设物品）
CREATE TABLE IF NOT EXISTS checklist_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '物品名称',
    category VARCHAR(50) NOT NULL COMMENT '分类：证件、财务、电子设备、衣物、洗漱、药品、特殊物品、应急物品',
    sub_category VARCHAR(50) COMMENT '子分类',
    icon VARCHAR(50) COMMENT '图标',
    is_essential BOOLEAN DEFAULT TRUE COMMENT '是否必需品',
    season VARCHAR(20) COMMENT '适用季节：spring,summer,autumn,winter,all',
    destination_type VARCHAR(50) COMMENT '目的地类型：coastal高原、山区、海边、城市、all',
    min_days INT DEFAULT 1 COMMENT '最少需要天数（用于计算数量）',
    max_days INT COMMENT '最多需要天数（用于计算数量）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='物品基础数据表';

-- 2. 用户行程清单表
CREATE TABLE IF NOT EXISTS travel_checklist (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    itinerary_id BIGINT NOT NULL COMMENT '行程ID',
    item_name VARCHAR(100) NOT NULL COMMENT '物品名称',
    category VARCHAR(50) NOT NULL COMMENT '分类',
    quantity INT DEFAULT 1 COMMENT '数量',
    is_packed BOOLEAN DEFAULT FALSE COMMENT '是否已打包',
    is_purchased BOOLEAN DEFAULT FALSE COMMENT '是否需要购买',
    notes VARCHAR(255) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_itinerary_id (itinerary_id),
    INDEX idx_category (category),
    INDEX idx_is_packed (is_packed)
) COMMENT='用户行程清单表';

-- 3. 清单模板表
CREATE TABLE IF NOT EXISTS checklist_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT '用户ID（NULL表示系统模板）',
    name VARCHAR(100) NOT NULL COMMENT '模板名称',
    description VARCHAR(255) COMMENT '模板描述',
    items JSON NOT NULL COMMENT '物品列表JSON：[{"name":"物品名","category":"分类","quantity":1}]',
    is_system BOOLEAN DEFAULT FALSE COMMENT '是否系统模板',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否为用户默认模板',
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_is_system (is_system)
) COMMENT='清单模板表';

-- 4. 用户清单模板关联表
CREATE TABLE IF NOT EXISTS user_template_usage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    last_used_time DATETIME,
    use_count INT DEFAULT 1,
    UNIQUE KEY uk_user_template (user_id, template_id)
) COMMENT='用户模板使用记录表';

-- 插入预设物品数据
INSERT INTO checklist_item (name, category, sub_category, icon, is_essential, season, destination_type, min_days, max_days) VALUES
-- 证件类
('身份证', '证件类', '身份证', 'id_card', TRUE, 'all', 'all', 1, 1),
('护照', '证件类', '护照', 'passport', FALSE, 'all', 'all', 1, 1),
('签证', '证件类', '签证', 'visa', FALSE, 'all', 'all', 1, 1),
('驾照', '证件类', '证件', 'license', FALSE, 'all', 'all', 1, 1),
('学生证/老年证', '证件类', '证件', 'card', FALSE, 'all', 'all', 1, 1),

-- 财务类
('现金', '财务类', '现金', 'cash', TRUE, 'all', 'all', 1, 1),
('信用卡', '财务类', '银行卡', 'credit_card', FALSE, 'all', 'all', 1, 1),
('银行卡', '财务类', '银行卡', 'bank_card', TRUE, 'all', 'all', 1, 1),
('零钱', '财务类', '现金', 'coin', TRUE, 'all', 'all', 1, 1),

-- 电子设备类
('手机', '电子设备', '通讯', 'phone', TRUE, 'all', 'all', 1, 1),
('充电宝', '电子设备', '充电', 'power_bank', TRUE, 'all', 'all', 1, 1),
('充电器', '电子设备', '充电', 'charger', TRUE, 'all', 'all', 1, 1),
('耳机', '电子设备', '娱乐', 'headphone', FALSE, 'all', 'all', 1, 1),
('相机', '电子设备', '拍摄', 'camera', FALSE, 'all', 'all', 1, 1),
('自拍杆', '电子设备', '拍摄', 'selfie_stick', FALSE, 'all', 'all', 1, 1),
('平板电脑', '电子设备', '娱乐', 'tablet', FALSE, 'all', 'all', 1, 1),
('转换插头', '电子设备', '充电', 'adapter', FALSE, 'all', 'all', 1, 1),

-- 衣物类
('换洗衣物', '衣物类', '上衣', 'clothes', TRUE, 'all', 'all', 3, 10),
('内衣裤', '衣物类', '内衣', 'underwear', TRUE, 'all', 'all', 3, 10),
('袜子', '衣物类', '袜子', 'socks', TRUE, 'all', 'all', 3, 10),
('睡衣', '衣物类', '睡衣', 'pajamas', TRUE, 'all', 'all', 1, 1),
('外套', '衣物类', '外套', 'jacket', TRUE, 'springautumn', 'all', 1, 1),
('羽绒服', '衣物类', '外套', 'down_jacket', TRUE, 'winter', 'all', 1, 1),
('T恤', '衣物类', '上衣', 'tshirt', TRUE, 'summer', 'all', 3, 10),
('短裤/裙子', '衣物类', '裤子', 'shorts', TRUE, 'summer', 'all', 2, 5),
('长裤', '衣物类', '裤子', 'pants', TRUE, 'all', 'all', 2, 5),
('泳衣/泳裤', '衣物类', '特殊服装', 'swimsuit', FALSE, 'summer', 'coastal', 1, 2),
('泳镜', '衣物类', '特殊服装', 'goggles', FALSE, 'summer', 'coastal', 1, 1),
('防晒衣', '衣物类', '外套', 'sunproof', FALSE, 'summer', 'coastal', 1, 1),
('帽子', '衣物类', '配饰', 'hat', TRUE, 'summer', 'all', 1, 2),
('墨镜', '衣物类', '配饰', 'sunglasses', FALSE, 'summer', 'coastal', 1, 1),
('围巾', '衣物类', '配饰', 'scarf', FALSE, 'winter', 'all', 1, 2),
('手套', '衣物类', '配饰', 'gloves', FALSE, 'winter', 'all', 1, 1),
('保暖内衣', '衣物类', '内衣', 'thermal', FALSE, 'winter', 'all', 2, 3),

-- 洗漱用品类
('牙刷', '洗漱用品', '口腔', 'toothbrush', TRUE, 'all', 'all', 1, 1),
('牙膏', '洗漱用品', '口腔', 'toothpaste', TRUE, 'all', 'all', 1, 1),
('洗发水', '洗漱用品', '洗浴', 'shampoo', FALSE, 'all', 'all', 1, 1),
('沐浴露', '洗漱用品', '洗浴', 'shower_gel', FALSE, 'all', 'all', 1, 1),
('洗面奶', '洗漱用品', '面部', 'face_wash', FALSE, 'all', 'all', 1, 1),
('毛巾', '洗漱用品', '洗浴', 'towel', TRUE, 'all', 'all', 1, 2),
('护肤品', '洗漱用品', '面部', 'skincare', TRUE, 'all', 'all', 1, 1),
('防晒霜', '洗漱用品', '防晒', 'sunscreen', TRUE, 'summer', 'all', 1, 2),
('剃须刀', '洗漱用品', '男士', 'shaver', FALSE, 'all', 'all', 1, 1),
('梳子', '洗漱用品', '理容', 'comb', FALSE, 'all', 'all', 1, 1),
('纸巾', '洗漱用品', '卫生', 'tissue', TRUE, 'all', 'all', 2, 5),
('湿巾', '洗漱用品', '卫生', 'wet_wipes', FALSE, 'all', 'all', 1, 3),
('洗衣液', '洗漱用品', '洗浴', 'laundry', FALSE, 'all', 'all', 1, 1),

-- 药品类
('感冒药', '药品类', '常用药', 'cold_medicine', TRUE, 'all', 'all', 1, 1),
('退烧药', '药品类', '常用药', 'fever_medicine', TRUE, 'all', 'all', 1, 1),
('创可贴', '药品类', '外用药', 'bandage', TRUE, 'all', 'all', 1, 2),
('晕车药', '药品类', '常用药', 'motion_sickness', FALSE, 'all', 'all', 1, 1),
('肠胃药', '药品类', '常用药', 'stomach_medicine', FALSE, 'all', 'all', 1, 1),
('抗过敏药', '药品类', '常用药', 'allergy_medicine', FALSE, 'all', 'all', 1, 1),
('眼药水', '药品类', '外用药', 'eye_drop', FALSE, 'all', 'all', 1, 1),
('润喉糖', '药品类', '常用药', 'throat_candy', FALSE, 'all', 'all', 1, 2),
('藿香正气水', '药品类', '常用药', 'hxc', FALSE, 'summer', 'all', 1, 1),
('红景天', '药品类', '特殊药', 'hongjing', FALSE, 'all', 'plateau', 1, 2),
('氧气袋', '药品类', '特殊药', 'oxygen', FALSE, 'all', 'plateau', 1, 1),
('驱蚊液', '药品类', '外用药', 'mosquito', FALSE, 'summer', 'all', 1, 1),

-- 特殊物品类
('雨伞', '特殊物品', '雨具', 'umbrella', TRUE, 'springautumn', 'all', 1, 1),
('雨衣', '特殊物品', '雨具', 'raincoat', FALSE, 'springautumn', 'all', 1, 1),
('登山杖', '特殊物品', '登山装备', 'trekking_pole', FALSE, 'all', 'mountain', 1, 1),
('登山包', '特殊物品', '登山装备', 'hiking_bag', FALSE, 'all', 'mountain', 1, 1),
('睡袋', '特殊物品', '露营装备', 'sleeping_bag', FALSE, 'all', 'mountain', 1, 1),
('帐篷', '特殊物品', '露营装备', 'tent', FALSE, 'all', 'mountain', 1, 1),
('野餐垫', '特殊物品', '户外用品', 'picnic_mat', FALSE, 'summer', 'all', 1, 1),
('保温杯', '特殊物品', '饮水', 'thermos', TRUE, 'all', 'all', 1, 1),
('水杯', '特殊物品', '饮水', 'water_cup', TRUE, 'all', 'all', 1, 1),
('零食', '特殊物品', '食物', 'snack', FALSE, 'all', 'all', 1, 3),
('口香糖', '特殊物品', '食物', 'gum', FALSE, 'all', 'all', 1, 2),
('便携凳子', '特殊物品', '户外用品', 'folding_stool', FALSE, 'all', 'all', 1, 1),

-- 应急物品类
('手电筒', '应急物品', '照明', 'flashlight', FALSE, 'all', 'all', 1, 1),
('多功能刀', '应急物品', '工具', 'multitool', FALSE, 'all', 'all', 1, 1),
('绳子', '应急物品', '工具', 'rope', FALSE, 'all', 'all', 1, 1),
('针线包', '应急物品', '工具', 'sewing_kit', FALSE, 'all', 'all', 1, 1),
('指南针', '应急物品', '导航', 'compass', FALSE, 'all', 'mountain', 1, 1),
('保鲜袋', '应急物品', '收纳', 'zip_bag', FALSE, 'all', 'all', 1, 3),
('垃圾袋', '应急物品', '收纳', 'garbage_bag', FALSE, 'all', 'all', 1, 3),
('锁具', '应急物品', '安全', 'lock', FALSE, 'all', 'all', 1, 1),
('行李箱', '应急物品', '收纳', 'suitcase', TRUE, 'all', 'all', 1, 2),
('背包', '应急物品', '收纳', 'backpack', TRUE, 'all', 'all', 1, 2);

-- 插入系统默认模板
INSERT INTO checklist_template (user_id, name, description, items, is_system, is_default) VALUES
(NULL, '通用出行清单', '适合大多数旅行的基础物品清单', '[{"name":"身份证","category":"证件类","quantity":1},{"name":"现金","category":"财务类","quantity":1},{"name":"手机","category":"电子设备","quantity":1},{"name":"充电宝","category":"电子设备","quantity":1},{"name":"换洗衣物","category":"衣物类","quantity":3},{"name":"内衣裤","category":"衣物类","quantity":3},{"name":"牙刷","category":"洗漱用品","quantity":1},{"name":"毛巾","category":"洗漱用品","quantity":1},{"name":"护肤品","category":"洗漱用品","quantity":1},{"name":"纸巾","category":"洗漱用品","quantity":2},{"name":"感冒药","category":"药品类","quantity":1},{"name":"创可贴","category":"药品类","quantity":1},{"name":"雨伞","category":"特殊物品","quantity":1},{"name":"保温杯","category":"特殊物品","quantity":1}]', TRUE, FALSE),

(NULL, '海边度假清单', '适合海边度假的物品清单', '[{"name":"身份证","category":"证件类","quantity":1},{"name":"现金","category":"财务类","quantity":1},{"name":"手机","category":"电子设备","quantity":1},{"name":"充电宝","category":"电子设备","quantity":1},{"name":"泳衣/泳裤","category":"衣物类","quantity":2},{"name":"防晒衣","category":"衣物类","quantity":1},{"name":"帽子","category":"衣物类","quantity":1},{"name":"墨镜","category":"衣物类","quantity":1},{"name":"短裤/裙子","category":"衣物类","quantity":3},{"name":"T恤","category":"衣物类","quantity":3},{"name":"牙刷","category":"洗漱用品","quantity":1},{"name":"防晒霜","category":"洗漱用品","quantity":2},{"name":"护肤品","category":"洗漱用品","quantity":1},{"name":"毛巾","category":"洗漱用品","quantity":1},{"name":"纸巾","category":"洗漱用品","quantity":2},{"name":"创可贴","category":"药品类","quantity":1},{"name":"晕车药","category":"药品类","quantity":1},{"name":"藿香正气水","category":"药品类","quantity":1},{"name":"雨衣","category":"特殊物品","quantity":1},{"name":"保温杯","category":"特殊物品","quantity":1}]', TRUE, FALSE),

(NULL, '登山徒步清单', '适合登山徒步的物品清单', '[{"name":"身份证","category":"证件类","quantity":1},{"name":"现金","category":"财务类","quantity":1},{"name":"手机","category":"电子设备","quantity":1},{"name":"充电宝","category":"电子设备","quantity":1},{"name":"充电器","category":"电子设备","quantity":1},{"name":"登山杖","category":"特殊物品","quantity":1},{"name":"登山包","category":"特殊物品","quantity":1},{"name":"保暖内衣","category":"衣物类","quantity":2},{"name":"外套","category":"衣物类","quantity":1},{"name":"长裤","category":"衣物类","quantity":2},{"name":"运动鞋","category":"衣物类","quantity":1},{"name":"袜子","category":"衣物类","quantity":3},{"name":"帽子","category":"衣物类","quantity":1},{"name":"手套","category":"衣物类","quantity":1},{"name":"围巾","category":"衣物类","quantity":1},{"name":"牙刷","category":"洗漱用品","quantity":1},{"name":"毛巾","category":"洗漱用品","quantity":1},{"name":"纸巾","category":"洗漱用品","quantity":2},{"name":"湿巾","category":"洗漱用品","quantity":1},{"name":"感冒药","category":"药品类","quantity":1},{"name":"创可贴","category":"药品类","quantity":2},{"name":"红景天","category":"药品类","quantity":1},{"name":"手电筒","category":"应急物品","quantity":1},{"name":"多功能刀","category":"应急物品","quantity":1},{"name":"指南针","category":"应急物品","quantity":1},{"name":"垃圾袋","category":"应急物品","quantity":2},{"name":"保温杯","category":"特殊物品","quantity":1},{"name":"零食","category":"特殊物品","quantity":3}]', TRUE, FALSE),

(NULL, '冬季滑雪清单', '适合冬季滑雪的物品清单', '[{"name":"身份证","category":"证件类","quantity":1},{"name":"现金","category":"财务类","quantity":1},{"name":"手机","category":"电子设备","quantity":1},{"name":"充电宝","category":"电子设备","quantity":1},{"name":"保暖内衣","category":"衣物类","quantity":3},{"name":"羽绒服","category":"衣物类","quantity":1},{"name":"长裤","category":"衣物类","quantity":2},{"name":"袜子","category":"衣物类","quantity":3},{"name":"围巾","category":"衣物类","quantity":1},{"name":"手套","category":"衣物类","quantity":1},{"name":"帽子","category":"衣物类","quantity":1},{"name":"护目镜","category":"衣物类","quantity":1},{"name":"保暖护膝","category":"衣物类","quantity":1},{"name":"牙刷","category":"洗漱用品","quantity":1},{"name":"护肤品","category":"洗漱用品","quantity":1},{"name":"纸巾","category":"洗漱用品","quantity":2},{"name":"感冒药","category":"药品类","quantity":1},{"name":"创可贴","category":"药品类","quantity":1},{"name":"暖宝宝","category":"特殊物品","quantity":5},{"name":"保温杯","category":"特殊物品","quantity":1}]', TRUE, FALSE);

-- 创建通用出行清单的视图（方便查询）
CREATE OR REPLACE VIEW v_checklist_summary AS
SELECT
    t.id,
    t.itinerary_id,
    t.category,
    COUNT(*) as total_items,
    SUM(t.quantity) as total_quantity,
    SUM(CASE WHEN t.is_packed = TRUE THEN 1 ELSE 0 END) as packed_items,
    SUM(CASE WHEN t.is_purchased = TRUE THEN 1 ELSE 0 END) as need_purchase_items
FROM travel_checklist t
GROUP BY t.itinerary_id, t.category;
