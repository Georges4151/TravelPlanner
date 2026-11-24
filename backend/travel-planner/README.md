# Travel Planner Backend

## 技术栈
- Spring Boot 3.2.0
- MyBatis 3.0.3
- Java 17
- MySQL 8.0

## 快速开始

### 1. 初始化数据库
```sql
mysql -u root -p < database/init.sql
```

### 2. 配置数据库连接
修改 `src/main/resources/application.yml` 中的数据库连接信息

### 3. 运行项目
```bash
mvn spring-boot:run
```

服务启动后运行在 http://localhost:8080

## API接口

### 用户模块
- POST /api/user/register - 用户注册
- POST /api/user/login - 用户登录
- GET /api/user/{id} - 获取用户信息

### 景点模块
- GET /api/attraction/city/{city} - 获取城市景点
- GET /api/attraction/top - 获取热门景点
- GET /api/attraction/{id} - 获取景点详情
- GET /api/attraction/{id}/crowd - 获取客流预测

### 行程模块
- POST /api/itinerary/generate - AI生成行程
- GET /api/itinerary/{id} - 获取行程详情
- GET /api/itinerary/user/{userId} - 获取用户行程列表
- POST /api/itinerary/template/{templateId} - 从模板创建行程

### 模板模块
- GET /api/template/hot - 获取热门模板
- GET /api/template/destination/{destination} - 按目的地获取模板

### 预算模块
- GET /api/budget/{itineraryId} - 获取预算
- POST /api/budget/create - 创建预算
- POST /api/budget/expense - 添加消费记录

### 清单模块
- GET /api/checklist/{itineraryId} - 获取清单
- POST /api/checklist/create - 创建默认清单

### AI模块
- POST /api/ai/generate - AI生成行程内容
- POST /api/ai/chat - AI对话

### 本地信息模块
- GET /api/info/hotel/{city} - 获取酒店信息
- GET /api/info/restaurant/{city} - 获取餐厅信息
- GET /api/info/transport - 获取交通信息

## DeepSeek API配置

在环境变量中设置:
```
DEEPSEEK_API_KEY=your-api-key
```
