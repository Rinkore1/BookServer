# Book Recommendation System

这是一个基于 Spring Boot、MongoDB 和 Redis 的书籍推荐系统，支持用户注册、登录、书籍管理和推荐。系统包括熔断机制，以保证在高负载或服务故障情况下的系统稳定性。

## 项目结构

本项目使用分层架构，包含以下模块：

1. **用户模块**：提供用户注册、登录、Token 验证、注销等功能。
2. **书籍模块**：提供书籍的增删查改（CRUD）操作，使用 MongoDB 存储数据。
~~3. **AI 推荐模块**：可以集成 NLP 模型（如 GPT-3 API 或自定义模型），实现基于用户偏好的推荐功能（未在当前代码中实现）。~~
4. **熔断机制**：使用 Resilience4j 实现熔断器，确保系统在高负载或服务异常时稳定运行。

## 技术栈

- **Spring Boot**：核心框架，提供 REST API 支持。
- **Spring Data MongoDB**：处理书籍和用户信息的存储。
- **Spring Data Redis**：用于 Token 的缓存和会话管理。
- **Resilience4j**：提供熔断器和限流功能。
- **Gradle**：项目构建工具。
- **MongoDB**：非关系型数据库，用于存储书籍和用户信息。
- **Redis**：缓存数据库，用于存储和管理登录 Token。

## 项目配置

请确保 MongoDB 和 Redis 正在运行，并在 `application.yml` 中配置连接信息：

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/mydatabase
  redis:
    host: localhost
    port: 6379
    timeout: 6000ms

resilience4j:
  circuitbreaker:
    instances:
      bookServiceCircuitBreaker:
        failureRateThreshold: 50
        minimumNumberOfCalls: 10
        waitDurationInOpenState: 20s
        permittedNumberOfCallsInHalfOpenState: 5
        slidingWindowSize: 100
```

## 快速开始

1. 克隆项目

```bash

git clone https://github.com/Rinkore1/BookServer.git
cd BookServer
```
2. 构建项目

使用以下命令构建项目：

```bash

./gradlew build
```
3. 运行项目

确保 MongoDB 和 Redis 服务已启动，然后使用以下命令启动应用程序：

```bash

./gradlew bootRun
```
4. 测试 API 接口

可以使用 Postman 或 curl 测试 API：

    注册用户: `POST /api/user/register`
    用户登录: `POST /api/user/login`
    验证 Token: `GET /api/user/validate`
    书籍 CRUD:
        获取所有书籍: `GET /api/books`
        获取单本书籍: `GET /api/books/{id}`
        添加书籍: `POST /api/books`
        更新书籍: `PUT /api/books/{id}`
        删除书籍: `DELETE /api/books/{id}`
    书籍推荐:
        随机推荐: `GET /api/books/recommend/random`
        热门推荐: `GET  /api/books/recommend/top`
        用户偏好的推荐: `GET /api/books/recommend/user`
    书籍搜索: `GET /api/books/search`

> 注意: 填写 `Authorization` 头和其他参数

5. 熔断机制测试

熔断器用于在请求失败时保护系统。当故障率超过阈值时，熔断器会触发，停止请求并调用回退方法。在书籍模块的 getAllBooks() 和 getBookById() 方法上添加了熔断器。

    模拟故障：暂停 MongoDB 服务或在 getAllBooks() 中添加延迟。
    触发熔断器：当故障率达到配置的 50% 时，熔断器会自动切换到回退方法，并返回空结果。

6. CORS

如果有该需求，请修改 `application.properties` 中的 `cors`
默认允许所有方法和所有来源

## 目录结构

```plaintext

src
├── main
│   ├── java
│   │   └── com.example.demo
│   │       ├── controller  # 控制器层，处理请求并返回响应
│   │       ├── model       # 数据模型层，定义用户和书籍实体
│   │       ├── repository  # 数据库操作层，定义 CRUD 接口
│   │       └── service     # 业务逻辑层，处理核心逻辑
│   └── resources
│       ├── application.yml # 配置文件
└── test
    └── java
        └── com.bookserver.deamon # 单元测试
```
## 注意事项

    环境要求：本项目需要 Java 8+ 和 Gradle。
    数据库：确保 MongoDB 和 Redis 正确安装和配置。
    熔断器设置：Resilience4j 配置文件中的 failureRateThreshold、waitDurationInOpenState 等参数可以根据实际需求调整。

## 未来开发

- [] 集成 AI 模块：通过 NLP 模型实现推荐系统，提供个性化的书籍推荐。
- [] 完善日志管理：增加日志记录，方便故障排查。
- [] 增加单元测试和集成测试，确保系统稳定性。