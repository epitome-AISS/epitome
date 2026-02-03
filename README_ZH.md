# Ai Lab

实验管理平台后端服务，提供实验计划、实验组、问卷、模型对话、数据采集与导出等能力。

## 技术栈

| 类别     | 技术 |
|----------|------|
| 框架     | Spring Boot 2.7.3 |
| 持久层   | MyBatis-Plus、MySQL 8.x |
| 缓存     | Redis（Jedis） |
| 安全     | Apache Shiro、Shiro-Redis |
| 接口文档 | Knife4j（Swagger） |
| 导出     | EasyExcel、EasyPoi |
| 其他     | WebSocket、MinIO、Hutool、FastJSON、Druid |

- **JDK**：1.8  
- **构建**：Maven  

## 环境要求

- JDK 8+
- Maven 3.x
- MySQL 8.x
- Redis

## 快速开始

### 1. 克隆与构建

```bash
# 克隆项目后进入目录
cd ailab

# 安装依赖并打包（含本地 lib 依赖）
mvn clean install -DskipTests
```

### 2. 配置

- 主配置：`src/main/resources/application.yml`  
- 激活的 Profile 在 `application.yml` 中通过 `spring.profiles.active` 指定（如 `testLocal`）。  
- 在对应 Profile 的配置文件（如 `application-testLocal.yml`）中配置：
  - **MySQL**：`spring.datasource.url / username / password`
  - **Redis**：`spring.redis.host / port` 等  

具体占位符以各 profile 配置文件为准（如 `{your_mysql_ip}`、`{your_redis_ip}` 等），请替换为实际环境信息。

### 3. 运行

```bash
# 使用 Maven 运行（会使用当前激活的 profile）
mvn spring-boot:run

# 或先打包再运行
mvn clean package -DskipTests
java -jar target/ailab-1.0.0.jar
```

默认端口见各 profile 配置（如本地常用 8085）。

### 4. 接口文档

启动后访问 Knife4j 文档（以实际 context-path 为准）：

- 例如：`http://localhost:8085/doc.html`

## 项目结构（简要）

```
src/main/java/com/nbtech/ailab/
├── AiLabApplication.java          # 启动类
├── biz/                            # 业务层
│   ├── controller/                 # 控制器
│   ├── dao/                        # 数据访问
│   ├── dto/                        # 数据传输对象
│   ├── entity/                     # 实体
│   └── service/                    # 服务接口与实现
├── common/                         # 枚举、常量等
├── config/                         # 配置类
├── facade/                         # 门面/聚合服务
├── security/                       # Shiro 等安全配置
├── util/                           # 工具类
├── vo/                             # 视图对象
└── websocket/                      # WebSocket 相关
```

- SQL 脚本与变更记录：`src/main/resources/sql/`  
- MyBatis 映射：`src/main/resources/mapper/`  

## 配置说明

- **多环境**：通过 `spring.profiles.active` 切换（如 `testLocal`、`testDev`、`formalDev`）。  
- **文件上传**：在 `application.yml` 中配置了 `spring.servlet.multipart`（如 500MB 限制）。  
- **数据库**：使用 MyBatis-Plus，逻辑删除字段为 `is_deleted`。  

## 其他

- 本地依赖：`lib/common-0.0.1-release.jar`，需存在且已安装或通过 `system` 作用域引入。  
- 部署：项目内提供 `Dockerfile`，可按需构建镜像部署。  
