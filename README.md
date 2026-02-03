# Ai Lab

Backend service for the experiment management platform, providing experiment plans, groups, questionnaires, model dialogue, data collection, export, and related capabilities.

## Tech Stack

| Category | Technology |
|----------|------------|
| Framework | Spring Boot 2.7.3 |
| Persistence | MyBatis-Plus, MySQL 8.x |
| Cache | Redis (Jedis) |
| Security | Apache Shiro, Shiro-Redis |
| API Docs | Knife4j (Swagger) |
| Export | EasyExcel, EasyPoi |
| Others | WebSocket, MinIO, Hutool, FastJSON, Druid |

- **JDK**: 1.8  
- **Build**: Maven  

## Requirements

- JDK 8+
- Maven 3.x
- MySQL 8.x
- Redis

## Quick Start

### 1. Clone and Build

```bash
# Enter project directory after cloning
cd ailab

# Install dependencies and package (including local lib)
mvn clean install -DskipTests
```

### 2. Configuration

- Main config: `src/main/resources/application.yml`  
- Active profile is set via `spring.profiles.active` in `application.yml` (e.g. `testLocal`).  
- Configure in the corresponding profile file (e.g. `application-testLocal.yml`):
  - **MySQL**: `spring.datasource.url`, `username`, `password`
  - **Redis**: `spring.redis.host`, `port`, etc.  

Replace placeholders (e.g. `{your_mysql_ip}`, `{your_redis_ip}`) with your actual environment values.

### 3. Run

```bash
# Run with Maven (uses active profile)
mvn spring-boot:run

# Or package first, then run
mvn clean package -DskipTests
java -jar target/ailab-1.0.0.jar
```

Default port is defined in each profile config (e.g. 8085 for local).

### 4. API Documentation

After startup, access Knife4j docs (adjust context-path if needed):

- Example: `http://localhost:8085/doc.html`

## Project Structure (Overview)

```
src/main/java/com/nbtech/ailab/
├── AiLabApplication.java          # Application entry
├── biz/                            # Business layer
│   ├── controller/                 # Controllers
│   ├── dao/                        # Data access
│   ├── dto/                        # Data transfer objects
│   ├── entity/                     # Entities
│   └── service/                    # Service interfaces and implementations
├── common/                         # Enums, constants
├── config/                         # Configuration classes
├── facade/                         # Facade / aggregation services
├── security/                       # Shiro and security config
├── util/                           # Utilities
├── vo/                             # View objects
└── websocket/                      # WebSocket
```

- SQL scripts and migrations: `src/main/resources/sql/`  
- MyBatis mappers: `src/main/resources/mapper/`  

## Configuration

- **Multi-environment**: Switch via `spring.profiles.active` (e.g. `testLocal`, `testDev`, `formalDev`).  
- **File upload**: Configured in `application.yml` under `spring.servlet.multipart` (e.g. 500MB limit).  
- **Database**: MyBatis-Plus with logical delete field `is_deleted`.  

## Other

- **Local dependency**: `lib/common-0.0.1-release.jar` must exist and be installed or referenced via `system` scope.  
- **Deployment**: A `Dockerfile` is provided for building and deploying the image.  
