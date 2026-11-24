# 智旅星程 · 错峰旅行智能规划工具

一个面向自由行用户的旅行规划工具：Android 端负责交互，Spring Boot 后端负责数据与 AI 能力。它解决的是"行程规划靠人肉翻攻略、热门景点全是人、预算和清单散落在备忘录里"的问题，用户输入目的地、天数、预算和偏好后，几分钟内就能拿到一份可执行的逐日行程，并给出错峰出行建议。项目同时支持模板复用、预算记账、行前清单、行程提醒和多人协作，覆盖一次旅行从"想去"到"出发"的完整流程。

---

## 一、功能清单

**账号与个人中心**
- 注册 / 登录，密码、手机号、邮箱基础校验
- 个人偏好维护：饮食偏好、住宿偏好、交通偏好、同行人偏好、目的地偏好
- 个人资料、收藏夹（景点 / 行程分类管理）

**景点与客流**
- 按城市、省份查询景点，关键词搜索，热门景点排行
- 景点详情：开放时间、票价、建议游玩时长、评分、图片
- 客流预测：单日预测、区间预测、按景点查询，输出拥挤等级、高峰时段、最佳游玩时间
- 错峰推荐：结合节假日、星期、季节、天气、交通给出日期建议

**AI 行程规划**
- 异步生成行程：提交需求后立即返回行程 ID，客户端轮询生成状态，避免长请求超时
- 生成参数：出发地、目的地、天数、人数、预算、旅行风格、忌口/禁忌需求、出发日期
- 支持重新生成、按单景点生成行程、对已有行程的某一天做局部调整
- 行程详情以"天 + 景点时间线"结构返回，含交通衔接与时间安排

**模板**
- 热门模板、模板列表、模板详情
- 一键从模板创建自己的行程，模板使用次数统计

**预算**
- 创建行程预算，记录每笔消费（类别、金额、备注）
- 预算与实际支出对比、分类占比统计

**行前清单**
- 按行程自动生成默认清单
- 清单项增删改、勾选"已打包 / 已购买"
- 完成度统计，系统清单模板与用户自定义模板

**提醒**
- 行程提醒的增删改查、批量创建、状态切换
- 即将到期的提醒查询，供客户端做本地通知

**团队协作（多人出行）**
- 创建团队、邀请码加入、成员角色管理
- 团队待办、成员投票（多选项投票、投票记录、截止关闭）
- 团队留言板与操作动态流

**本地信息**
- 酒店、餐厅（含按菜系筛选）、交通方案查询

**Android 客户端**
- 登录 / 注册、首页、行程、模板、我的五大入口
- 行程创建向导、行程详情、预算、清单、收藏、提醒、协作
- 景点 / 酒店 / 餐厅搜索与详情、城市选择、交通方案页
- 二维码分享（ZXing）、定位（高德）、图片加载（Glide）

---

## 二、技术栈

| 模块 | 技术选型 |
|------|----------|
| 客户端 | Java 17、Android SDK 34（minSdk 24）、AppCompat + Material 3、RecyclerView、ViewPager2、SwipeRefreshLayout |
| 客户端网络 | Retrofit 2.9 + Gson、OkHttp 4.12 + Logging Interceptor |
| 客户端本地 | Room 2.6.1（本地缓存）、SharedPreferences（登录态）、Glide 4.16（图片）、ZXing（二维码）、Play Services Location |
| 后端框架 | Spring Boot 3.2.0、Spring MVC、Bean Validation |
| 后端持久层 | MyBatis 3（注解为主 + XML）、MySQL Connector/J、HikariCP |
| 数据库 | MySQL 8.0，utf8mb4，36 张业务表 |
| 工具库 | Lombok、Hutool、Fastjson2 |
| 第三方服务 | DeepSeek Chat API（行程生成/对话）、和风天气 API（天气）、高德开放平台（POI、距离、地理编码） |
| 构建 / 运行 | Maven 3.9、Gradle 8.5（AGP）、JDK 17、Android Studio / 模拟器 |

> 说明：`spring-boot-starter-data-redis` 目前只作为依赖引入，代码中尚未落地缓存逻辑，部署时不需要启动 Redis。

---

## 三、项目亮点（个人贡献）

- **独立完成 115 个后端 Java 类的分层设计**：12 个 Controller、15 个 Service、28 个 Mapper、28 个 Entity、30 个 DTO，接口按"用户 / 景点 / 行程 / 模板 / 预算 / 清单 / 提醒 / 团队 / 外部数据 / AI"十个领域拆分，统一使用 `ApiResponse{code,message,data}` 作为返回结构，客户端只需写一套解析逻辑。
- **设计并实现了客流预测加权算法**：把节假日（权重 40%）、星期几（20%）、季节（15%）、天气与交通等因素归一化后加权求和，映射为"舒适 / 适中 / 拥挤 / 爆满"等级，并进一步输出高峰时段和"建议改到哪天出发"的错峰建议；算法同时支持单日、区间和按景点名称查询三种入口。
- **把 AI 生成做成可用的工程方案而不是一次 HTTP 调用**：Prompt 中约束模型必须输出固定字段的 JSON，返回后用 JSON 解析器做结构校验，失败自动带着错误信息重试；再用异步任务 + `GET /api/itinerary/status/{id}` 轮询的方式，把耗时几十秒的生成过程从请求线程里拆出去，避免客户端超时和 ANR。
- **处理了多接口的中文编码问题**：早期接口返回中文乱码，最终在所有返回中文的接口上显式声明 `produces = application/json;charset=UTF-8`，并在 `application.yml` 中统一配置 `server.servlet.encoding` 强制请求/响应使用 UTF-8。
- **完成 Android 端 24 个 Activity、5 个 Fragment 的页面与交互实现**：包括行程创建向导的状态回填、行程详情的时间线渲染、清单勾选后的本地即时刷新、预算分类统计图表、团队协作的轮询刷新。
- **设计 36 张表的关系模型**：把"行程—天—景点"拆成三层结构，预算与消费分离，团队协作拆成成员/待办/投票/投票记录/留言/动态六张表，用户偏好按维度独立建表，便于后续扩展维度而不改主表。
- **补齐了跨端联调的工程细节**：客户端统一 `BASE_URL`，模拟器走 `10.0.2.2`；后端异步任务的行程状态字段单独维护；数据库初始化提供 `create_db.sql`、`init.sql` 以及团队/清单/收藏/提醒的分模块建表脚本，换机器时不用手工补表。

---

## 四、目录结构说明

```
competition 3/
├── README.md                      # 项目说明（本文件）
├── 项目代码识别报告.md              # 代码结构与模块识别报告
│
├── android-app/                   # Android 客户端
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/travelplanner/
│   │   │   │   ├── TravelPlannerApp.java   # Application，全局 BASE_URL
│   │   │   │   ├── ui/                     # 24 个 Activity + 5 个 Fragment
│   │   │   │   │   ├── home/ itinerary/ template/ attraction/
│   │   │   │   │   ├── budget/ checklist/ favorite/ reminder/
│   │   │   │   │   └── hotel/ restaurant/ transport/ user/ base/
│   │   │   │   ├── model/                  # 客户端数据模型
│   │   │   │   ├── data/remote/            # Retrofit ApiClient
│   │   │   │   ├── service/                # 地图 / 定位相关服务
│   │   │   │   └── util/                   # HttpUtil 等工具类
│   │   │   ├── res/                        # 布局、drawable、values
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   ├── gradle/ gradlew.bat settings.gradle build.gradle
│   ├── local.properties            # 本机 SDK 路径（不提交）
│   └── docs/                       # 开发过程文档
│
└── backend/                       # Spring Boot 后端
    └── travel-planner/
        ├── src/main/java/com/travelplanner/
        │   ├── TravelPlannerApplication.java
        │   ├── config/             # Web 配置（跨域等）
        │   ├── controller/         # 12 个 Controller
        │   ├── service/            # 15 个 Service（含 AI、客流预测）
        │   ├── mapper/             # 28 个 MyBatis Mapper
        │   ├── entity/             # 28 个实体
        │   └── dto/                # 30 个 DTO
        ├── src/main/resources/
        │   ├── application.yml     # 端口、数据源、三方 API Key
        │   ├── schema.sql          # 启动时自动执行的建表脚本
        │   └── mapper/             # XML 映射（少量复杂 SQL）
        ├── database/               # 初始化脚本：init.sql、团队/清单/收藏/提醒
        ├── pom.xml
        └── target/                 # 编译产物
```

---

## 五、运行步骤

### 1. 环境准备

| 依赖 | 版本要求 |
|------|----------|
| JDK | 17 及以上（本项目在 JDK 17 / 19 下验证通过） |
| MySQL | 8.0，服务已启动 |
| Maven | 3.6+ |
| Android | Android Studio + SDK 34 + 一个模拟器（AVD）或真机 |

### 2. 初始化数据库

```bash
mysql -u root -p < backend/travel-planner/database/init.sql
```

脚本会创建 `travel_planner` 库（utf8mb4）并建好基础表。团队、清单、收藏、提醒相关的表在 `database/` 目录下另有分模块脚本，`init.sql` 已包含主要建表语句，若表中缺少对应结构再单独执行即可。

### 3. 配置数据库连接

编辑 `backend/travel-planner/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/travel_planner?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 你的密码
```

同样的文件里配置三方服务 Key（不使用相关功能可以忽略）：

```yaml
travel:
  deepseek:
    api-key: sk-xxx        # AI 行程生成
  qweather:
    api-key: xxx           # 天气
  amap:
    api-key: xxx           # 高德 POI / 距离
```

### 4. 启动后端

```bash
cd backend/travel-planner
mvn spring-boot:run
```

启动成功后监听 `http://localhost:8080`。自检：

```bash
curl http://localhost:8080/api/attraction/top
```

返回 `code: 200` 和景点数组即表示数据库连接、Mapper 扫描、接口注册都正常。

### 5. 运行 Android 客户端

1. 确认 `android-app/local.properties` 指向本机 SDK：

   ```properties
   sdk.dir=D\:\\Androidsdk
   ```

2. 用 Android Studio 打开 `android-app` 目录，等待 Gradle Sync 完成。
3. 启动模拟器（或连接真机），点击 Run。
4. 也可以直接命令行构建安装：

   ```bash
   cd android-app
   gradlew.bat assembleDebug
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

5. 客户端默认请求地址在 `android-app/app/src/main/java/com/travelplanner/TravelPlannerApp.java`：

   ```java
   public static final String BASE_URL = "http://10.0.2.2:8080/api/";
   ```

   `10.0.2.2` 是 Android 模拟器访问宿主机 localhost 的固定地址。**真机调试时改成电脑的局域网 IP**（如 `http://192.168.1.10:8080/api/`），并确保手机和电脑在同一网段、电脑防火墙放行 8080。

6. 首次进入用注册页新建账号，或使用数据库中已有账号登录。

---

## 六、接口文档

### 6.1 通用约定

- 基础路径：`http://localhost:8080/api`
- 请求体：`Content-Type: application/json`
- 统一响应结构：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 200 成功；400 参数/业务错误；401 未授权；404 不存在；500 服务异常 |
| message | string | 成功为 `success`，失败为错误描述 |
| data | object/array | 业务数据，失败时通常为空 |

### 6.2 用户模块

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| POST | `/user/register` | body: `username, password, phone, email` | 注册，返回 `id, username` |
| POST | `/user/login` | body: `username, password` | 登录，返回 `id, username, memberLevel` |
| GET | `/user/{id}` | path: 用户 ID | 用户信息（密码置空返回） |

**登录示例**

```http
POST /api/user/login
Content-Type: application/json

{ "username": "demo", "password": "123456" }
```

```json
{
  "code": 200,
  "message": "success",
  "data": { "id": 8, "username": "demo", "memberLevel": 1 }
}
```

### 6.3 景点与客流

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/attraction/city/{city}` | path: 城市 | 城市景点列表 |
| GET | `/attraction/province/{province}` | path: 省份 | 省份景点列表 |
| GET | `/attraction/search` | query: `keyword`、可选 `city` | 关键词搜索 |
| GET | `/attraction/top` | query: `limit`（默认 10） | 热门景点 |
| GET | `/attraction/{id}` | path: 景点 ID | 景点详情 |
| GET | `/attraction/{id}/crowd` | path: id，query: `days`（默认 3） | 简版客流预测 |
| GET | `/external/crowd/forecast/{attractionId}` | path: id | 详细客流预测（含天气、因素权重） |
| GET | `/external/weather` | query: `city`、可选 `date` | 实时/指定日期天气 |
| GET | `/external/weather/forecast` | query: `city, days` | 天气预报 |
| GET | `/external/holiday` | query: `date` | 节假日判断 |

**客流预测示例**

```http
GET /api/external/crowd/forecast/1
```

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "attractionId": 1,
    "attractionName": "西湖风景区",
    "coordinates": "120.15,30.25",
    "forecasts": [
      {
        "date": "2026-05-02",
        "dayOfWeek": "星期六",
        "isHoliday": true,
        "holidayName": "劳动节",
        "weather": "晴",
        "trafficStatus": "畅通",
        "baseCrowd": 60.0,
        "weightedCrowd": 88.5,
        "crowdLevel": "爆满",
        "peakHours": ["10:00-12:00", "14:00-16:00"],
        "bestTime": "07:00-09:00",
        "recommendation": "建议避开假期首日，改期至 5 月 6 日后出行",
        "factors": { "holiday": 1.0, "weekend": 0.8, "weather": 0.6, "hot": 0.9, "traffic": 0.5 }
      }
    ]
  }
}
```

### 6.4 行程模块

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| POST | `/itinerary/generate-async` | body 见下 | 异步生成，立即返回行程 ID |
| GET | `/itinerary/status/{id}` | path: 行程 ID | 查询生成状态 |
| POST | `/itinerary/generate` | body 同上 | 同步生成（耗时较长） |
| GET | `/itinerary/{id}` | path: 行程 ID | 行程详情（按天+景点） |
| GET | `/itinerary/user/{userId}` | path: 用户 ID | 用户行程列表 |
| POST | `/itinerary/template/{templateId}` | query: `userId` | 从模板创建行程 |
| POST | `/itinerary/regenerate/{id}` | path: 行程 ID | 重新生成 |
| POST | `/itinerary/{id}/adjust` | path + body: 调整项 | 局部调整某一天 |
| POST | `/itinerary/generate-from-attraction` | body: 景点与日期 | 按单个景点生成行程 |
| DELETE | `/itinerary/{id}` | path: 行程 ID | 删除行程 |
| GET | `/itinerary/offpeak/recommend` | query: 目的地等 | 错峰出行推荐 |

**生成请求体**

```json
{
  "userId": 8,
  "origin": "上海",
  "destination": "杭州",
  "days": 3,
  "peopleCount": 2,
  "budget": "3000",
  "travelStyle": "休闲",
  "tabooNeeds": ["不吃辣"],
  "startDate": "2026-05-01"
}
```

**异步生成响应**

```json
{ "code": 200, "message": "success", "data": 1024 }
```

客户端拿到 `1024` 后轮询 `GET /api/itinerary/status/1024`，当返回的行程状态表示生成完成时再请求详情。

### 6.5 模板 / 预算 / 清单

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/template/hot` | - | 热门模板 |
| GET | `/template` | query: 分页/目的地 | 模板列表 |
| GET | `/template/{id}` | path: 模板 ID | 模板详情 |
| GET | `/budget/{itineraryId}` | path: 行程 ID | 预算详情 |
| POST | `/budget/create` | body: 行程 ID、总预算 | 创建预算 |
| POST | `/budget/expense` | body: 类别、金额、备注 | 记录消费 |
| GET | `/budget/expense/{itineraryId}` | path: 行程 ID | 消费明细 |
| GET | `/budget/comparison/{itineraryId}` | path: 行程 ID | 预算与实际对比 |
| POST | `/checklist/generate` | body: 行程 ID | 生成默认清单 |
| GET | `/checklist/{itineraryId}` | path: 行程 ID | 清单列表 |
| GET | `/checklist/stats/{itineraryId}` | path: 行程 ID | 完成度统计 |
| PUT | `/checklist/item/{itemId}/pack` | path: 清单项 ID | 勾选已打包 |
| PUT | `/checklist/item/{itemId}/purchase` | path: 清单项 ID | 勾选已购买 |
| GET | `/checklist/templates/system` | - | 系统清单模板 |

### 6.6 收藏 / 提醒 / 团队 / AI / 本地信息

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/favorite/list`、`/favorite/list/{type}` | 收藏列表（按类型） |
| POST | `/favorite/add`、DELETE `/favorite/remove` | 添加 / 取消收藏 |
| GET | `/favorite/check` | 是否已收藏 |
| GET | `/reminder/itinerary/{itineraryId}` | 行程提醒列表 |
| POST | `/reminder`、PUT `/reminder/{id}`、DELETE `/reminder/{id}` | 提醒增删改 |
| PUT | `/reminder/{id}/status` | 切换提醒状态 |
| POST | `/reminder/batch` | 批量创建提醒 |
| POST | `/team/create`、`/team/join` | 创建 / 加入团队（邀请码） |
| GET | `/team/{teamId}`、`/team/{teamId}/members` | 团队详情 / 成员 |
| GET/POST/PUT/DELETE | `/team/{teamId}/todos`、`/team/todo/*` | 团队待办 |
| POST | `/team/vote/create`、`/team/vote/cast` | 发起投票 / 投票 |
| GET/POST | `/team/{teamId}/messages`、`/team/message/send` | 团队留言 |
| GET | `/team/{teamId}/activities` | 团队动态 |
| POST | `/ai/generate` | AI 生成行程文本 |
| POST | `/ai/chat` | AI 旅行对话 |
| GET | `/info/hotel/{city}`、`/info/restaurant/{city}` | 酒店 / 餐厅 |
| GET | `/info/transport`、`/info/transport/search` | 交通方案 |

---

## 七、常见问题（FAQ）

**1. 启动后端报 `Port 8080 was already in use`**
8080 被其他进程占用（本机常见于另一个 Spring Boot 项目）。用 `netstat -ano | findstr :8080` 找到 PID 后结束进程；或临时改端口 `server.port`。注意改端口后 Android 端的 `BASE_URL` 要同步修改。

**2. 报 `release version 17 not supported`**
Maven 用的还是 JDK 8/11。把 `JAVA_HOME` 指向 JDK 17+ 再执行：

```powershell
$env:JAVA_HOME="E:\Program Files\Java\jdk-17"
mvn -f backend/travel-planner/pom.xml spring-boot:run
```

**3. 接口返回的中文是乱码（问号 / 方框）**
两类原因：一是接口没有声明响应编码，已在所有返回中文的接口上加了 `produces = application/json;charset=UTF-8`；二是 `application.yml` 缺少强制编码配置，补上：

```yaml
server:
  servlet:
    encoding:
      charset: UTF-8
      force: true
```

**4. 数据库连不上：`Public Key Retrieval is not allowed` / 时区报错**
连接串里必须有 `allowPublicKeyRetrieval=true`、`serverTimezone=Asia/Shanghai`、`useSSL=false`，同时确认 MySQL 服务已启动、`travel_planner` 库已通过 `init.sql` 创建。

**5. App 所有接口都请求失败，日志里是连接超时**
先确认后端在本机 8080 可用；模拟器必须用 `10.0.2.2` 而不是 `localhost`（模拟器里的 localhost 是模拟器自己）。真机则要改成电脑的局域网 IP，并检查防火墙。

**6. AI 行程生成偶尔失败或返回的不是 JSON**
DeepSeek 返回内容存在波动，代码里会先校验 JSON 结构，不合法时带着错误信息重试，多次失败才返回 500。检查 `travel.deepseek.api-key` 是否有效、账户是否有额度；网络不通时接口会返回明确的错误信息而不是卡死。

**7. 行程一直显示"生成中"**
异步生成依赖 `GET /itinerary/status/{id}` 的状态字段。如果改过表结构，确认 `itinerary` 表中的生成状态字段存在；`schema.sql` 使用 `continue-on-error: true`，缺字段不会中断启动，但运行时会异常，需要手动补列。

**8. Gradle 提示文件被占用 / 无法重命名项目目录**
Gradle 守护进程会锁住 `.gradle` 目录。先执行 `gradlew.bat --stop` 或结束 `GradleDaemon` 进程，再操作目录；必要时删除 `.gradle` 让其重新生成。

**9. 重装 App 后又要重新登录，之前的数据呢**
登录态存在 SharedPreferences，数据在后端 MySQL。`adb install -r` 覆盖安装会保留数据；只有卸载或清除应用数据才会丢登录态，重新登录即可看到历史行程。

---

**许可**：本项目为竞赛/学习用途，代码与数据仅供交流使用。
