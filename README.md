# Travel Planner Android

## 技术栈
- Android SDK 34
- Java 17
- OkHttp3
- Gson
- Glide

## 项目结构
```
app/
├── src/main/java/com/travelplanner/
│   ├── model/          # 数据模型
│   ├── util/           # 工具类
│   └── ui/             # UI层
│       ├── home/       # 首页
│       ├── itinerary/  # 行程
│       ├── template/   # 模板
│       ├── attraction/  # 景点
│       ├── budget/     # 预算
│       ├── checklist/  # 清单
│       ├── info/       # 本地信息
│       └── user/       # 用户
└── src/main/res/       # 资源文件
```

## 运行
1. 确保后端服务已启动 (http://localhost:8080)
2. 使用Android Studio打开项目
3. 选择模拟器或连接设备运行