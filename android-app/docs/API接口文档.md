# 错峰旅行规划助手 - API 接口文档

## 项目概述

**项目名称**：错峰旅行规划助手 (TravelPlanner)  
**版本**：1.0.0  
**开发语言**：Java (Android)  
**架构模式**：MVVM + Repository + Room数据库  
**网络框架**：Retrofit2 + OkHttp3  
**API基础URL**：`http://[服务器地址]:8080`  

---

## 目录

1. [通用说明](#通用说明)
2. [用户模块](#用户模块)
3. [行程模块](#行程模块)
4. [景点模块](#景点模块)
5. [酒店模块](#酒店模块)
6. [餐厅模块](#餐厅模块)
7. [交通模块](#交通模块)
8. [行李清单模块](#行李清单模块)
9. [提醒模块](#提醒模块)
10. [协作模块](#协作模块)
11. [数据模型](#数据模型)

---

## 通用说明

### 请求格式
- Content-Type: `application/json`
- 编码: `UTF-8`
- 认证方式: `Bearer Token`

### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 状态码说明
| code | 说明 |
|------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

---

## 用户模块

### 1. 用户登录
```
POST /api/auth/login
```

**请求参数：**
```json
{
  "username": "string",
  "password": "string"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": 1,
    "username": "user",
    "token": "eyJhbGciOiJIUzI1NiIs..."
  }
}
```

### 2. 用户注册
```
POST /api/auth/register
```

**请求参数：**
```json
{
  "username": "string",
  "password": "string",
  "email": "string",
  "phone": "string"
}
```

---

## 行程模块

### 1. 获取行程列表
```
GET /api/itineraries/user/{userId}
```

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应：**
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "title": "北京5日游",
      "destination": "北京",
      "startDate": "2026-04-10",
      "endDate": "2026-04-14",
      "personCount": 2,
      "style": "休闲"
    }
  ]
}
```

### 2. 创建行程
```
POST /api/itineraries
```

**请求参数：**
```json
{
  "title": "string",
  "destination": "string",
  "startDate": "date",
  "endDate": "date",
  "personCount": int,
  "style": "string",
  "budget": double
}
```

### 3. 获取行程详情
```
GET /api/itineraries/{id}
```

### 4. 更新行程
```
PUT /api/itineraries/{id}
```

### 5. 删除行程
```
DELETE /api/itineraries/{id}
```

---

## 景点模块

### 1. 获取景点详情
```
GET /api/attractions/{id}
```

**响应字段：**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 景点ID |
| name | String | 景点名称 |
| address | String | 地址 |
| rating | Double | 评分 |
| price | Double | 门票价格 |
| openTime | String | 开放时间 |
| description | String | 描述 |
| latitude | Double | 纬度 |
| longitude | Double | 经度 |
| images | List\<String\> | 图片列表 |

### 2. 按城市获取景点列表
```
GET /api/attractions/city/{city}
```

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| city | String | 是 | 城市名称 |

### 3. 搜索景点
```
GET /api/attractions/search?keyword={keyword}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 是 | 搜索关键词 |

### 4. 获取景点人流预测
```
GET /api/attractions/{id}/crowd-forecast?days={days}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| days | int | 是 | 预测天数（1-7） |

**响应：**
```json
{
  "code": 200,
  "data": [
    {
      "date": "2026-04-10",
      "crowdLevel": "中等",
      "crowdIndex": "65",
      "bestTime": "09:00-10:00"
    }
  ]
}
```

### 5. 获取附近景点
```
GET /api/attractions/nearby?lat={lat}&lng={lng}&radius={radius}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| lat | double | 是 | 纬度 |
| lng | double | 是 | 经度 |
| radius | int | 否 | 搜索半径（米），默认1000 |

---

## 酒店模块

### 1. 获取酒店详情
```
GET /api/hotels/{id}
```

**响应字段：**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 酒店ID |
| name | String | 酒店名称 |
| address | String | 地址 |
| starLevel | int | 星级 |
| rating | Double | 评分 |
| minPrice | Double | 最低价格 |
| facilities | String | 设施 |
| images | List\<String\> | 图片列表 |

### 2. 按城市获取酒店列表
```
GET /api/hotels/city/{city}
```

### 3. 搜索酒店（支持多条件）
```
GET /api/hotels/search
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 否 | 关键词 |
| minPrice | Double | 否 | 最低价格 |
| maxPrice | Double | 否 | 最高价格 |
| rating | Double | 否 | 最低评分 |
| roomType | String | 否 | 房间类型 |

### 4. 获取附近酒店
```
GET /api/hotels/nearby?lat={lat}&lng={lng}&radius={radius}
```

### 5. 获取可用房间
```
GET /api/hotels/{id}/rooms?checkIn={checkIn}&checkOut={checkOut}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| checkIn | String | 是 | 入住日期（yyyy-MM-dd） |
| checkOut | String | 是 | 退房日期（yyyy-MM-dd） |

**响应：**
```json
{
  "code": 200,
  "data": [
    {
      "roomType": "标准间",
      "price": 299.0,
      "availableCount": 15,
      "facilities": "WiFi,空调,电视"
    }
  ]
}
```

---

## 餐厅模块

### 1. 获取餐厅详情
```
GET /api/restaurants/{id}
```

**响应字段：**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 餐厅ID |
| name | String | 餐厅名称 |
| cuisineType | String | 菜系 |
| address | String | 地址 |
| rating | Double | 评分 |
| avgPrice | Double | 人均消费 |
| openTime | String | 营业时间 |

### 2. 按城市获取餐厅列表
```
GET /api/restaurants/city/{city}
```

### 3. 搜索餐厅（支持多条件）
```
GET /api/restaurants/search
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 否 | 关键词 |
| cuisineType | String | 否 | 菜系类型 |
| minPrice | Double | 否 | 最低价格 |
| maxPrice | Double | 否 | 最高价格 |
| rating | Double | 否 | 最低评分 |

### 4. 获取附近餐厅
```
GET /api/restaurants/nearby?lat={lat}&lng={lng}&radius={radius}
```

### 5. 获取排队状态
```
GET /api/restaurants/{id}/queue-status
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "status": "排队中",
    "waitTime": 30,
    "queueCount": 12
  }
}
```

---

## 交通模块

### 1. 搜索交通方式
```
GET /api/transport/search
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| departure | String | 是 | 出发地 |
| arrival | String | 是 | 目的地 |
| date | String | 是 | 出发日期（yyyy-MM-dd） |
| type | String | 是 | 交通类型（train/plane/bus/car） |

**响应：**
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "type": "高铁",
      "departureTime": "08:30",
      "arrivalTime": "13:45",
      "duration": "5小时15分",
      "price": 553.0,
      "company": "复兴号"
    }
  ]
}
```

### 2. 路线规划
```
GET /api/transport/route-plan
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| originLat | double | 是 | 起点纬度 |
| originLng | double | 是 | 起点经度 |
| destLat | double | 是 | 终点纬度 |
| destLng | double | 是 | 终点经度 |
| mode | String | 是 | 出行方式（driving/transit/walking/biking） |

### 3. 多模式路线推荐
```
GET /api/transport/multi-route
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| originLat | double | 是 | 起点纬度 |
| originLng | double | 是 | 起点经度 |
| destLat | double | 是 | 终点纬度 |
| destLng | double | 是 | 终点经度 |

### 4. 实时路况信息
```
GET /api/transport/real-time-traffic
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "status": "拥堵",
    "congestionLevel": 7,
    "estimatedTime": 45,
    "description": "当前路段拥堵，建议绕行"
  }
}
```

---

## 行李清单模块

### 1. 获取行程的行李清单
```
GET /api/checklists/itinerary/{itineraryId}
```

### 2. 添加清单项
```
POST /api/checklists
```

**请求体：**
```json
{
  "itemName": "string",
  "category": "string",
  "quantity": int,
  "checked": boolean,
  "itineraryId": long
}
```

### 3. 更新清单项
```
PUT /api/checklists/{id}
```

### 4. 删除清单项
```
DELETE /api/checklists/{id}
```

### 5. 切换勾选状态
```
PUT /api/checklists/{id}/toggle
```

### 6. 获取模板列表
```
GET /api/checklists/templates
```

### 7. 按目的地获取模板
```
GET /api/checklists/templates/{destination}
```

### 8. AI智能生成清单
```
POST /api/checklists/generate
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| itineraryId | Long | 是 | 行程ID |
| destination | String | 是 | 目的地 |
| season | String | 是 | 季节（春/夏/秋/冬） |
| tripType | String | 是 | 旅行类型（休闲/商务/徒步/亲子） |

### 9. 批量添加清单项
```
POST /api/checklists/batch
```

---

## 提醒模块

### 1. 获取提醒详情
```
GET /api/reminders/{id}
```

### 2. 获取行程的提醒列表
```
GET /api/reminders/itinerary/{itineraryId}
```

### 3. 获取即将到来的提醒
```
GET /api/reminders/upcoming?hours={hours}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| hours | int | 是 | 未来几小时内的提醒 |

### 4. 创建提醒
```
POST /api/reminders
```

**请求体：**
```json
{
  "title": "string",
  "description": "string",
  "remindTime": "datetime",
  "type": "string",
  "method": "string",
  "itineraryId": long
}
```

### 5. 更新提醒
```
PUT /api/reminders/{id}
```

### 6. 删除提醒
```
DELETE /api/reminders/{id}
```

### 7. 更新提醒状态
```
PUT /api/reminders/{id}/status?status={status}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | int | 是 | 状态（0=关闭，1=开启） |

### 8. 批量创建提醒
```
POST /api/reminders/batch
```

---

## 协作模块

### 1. 创建协作链接
```
POST /api/collaboration/create-link
```

**请求体：**
```json
{
  "itineraryId": long,
  "permission": "string"  // read/write
}
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "shareCode": "ABC123XYZ",
    "link": "http://app.com/share/ABC123XYZ",
    "qrcodeUrl": "/api/qrcode/ABC123XYZ"
  }
}
```

### 2. 通过链接加入协作
```
POST /api/collaboration/join
```

**请求体：**
```json
{
  "shareCode": "string"
}
```

### 3. 获取协作者列表
```
GET /api/collaboration/members/{itineraryId}
```

### 4. 移除协作者
```
DELETE /api/collaboration/member/{memberId}
```

---

## 数据模型

### Itinerary（行程实体）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| title | String | 行程标题 |
| destination | String | 目的地 |
| startDate | Date | 开始日期 |
| endDate | Date | 结束日期 |
| personCount | int | 人数 |
| style | String | 旅行风格 |
| budget | Double | 预算 |
| userId | Long | 所属用户ID |
| createdAt | Date | 创建时间 |
| updatedAt | Date | 更新时间 |

### ChecklistItemEntity（行李清单实体）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| itemName | String | 物品名称 |
| category | String | 分类 |
| quantity | int | 数量 |
| checked | boolean | 是否已勾选 |
| itineraryId | Long | 关联的行程ID |
| sortOrder | int | 排序顺序 |

### ReminderEntity（提醒实体）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| title | String | 提醒标题 |
| description | String | 提醒内容 |
| remindTime | DateTime | 提醒时间 |
| type | String | 提醒类型 |
| method | String | 提醒方式 |
| status | int | 状态（0/1） |
| itineraryId | Long | 关联的行程ID |

---

## 第三方服务集成

### 高德地图API
- **地理编码**：地址转坐标
- **POI搜索**：景点、酒店、餐厅搜索
- **天气查询**：目的地天气预报
- **路线规划**：驾车/公交/步行路线

### DeepSeek AI
- **智能行李清单生成**：根据行程和天气自动生成
- **错峰出行建议**：基于人流量预测

---

## 错误码说明

| 错误码 | 说明 | 解决方案 |
|--------|------|----------|
| 40001 | 参数缺失或格式错误 | 检查请求参数 |
| 40101 | Token无效或过期 | 重新登录 |
| 40301 | 无权限访问 | 检查用户权限 |
| 40401 | 资源不存在 | 检查资源ID |
| 50001 | 服务器内部错误 | 联系管理员 |

---

## 更新日志

### v1.0.0 (2026-04-04)
- ✅ 基础功能完成
- ✅ 用户系统
- ✅ 行程管理
- ✅ 景点/酒店/餐厅搜索
- ✅ 交通规划
- ✅ 智能行李清单
- ✅ 应用内通知提醒
- ✅ 多人协作功能
- ✅ 二维码分享

---

**文档版本**：v1.0.0  
**最后更新**：2026-04-04  
**维护团队**：TravelPlanner 开发团队
