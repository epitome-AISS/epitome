<p align="center">
  <img src="./static/logo.png" width="280" alt="Epitome Logo">
</p>

<h1 align="center">Epitome</h1>

<p align="center">
  <strong>Pioneering an Experimental Platform for AI-Social Science Integration</strong>
</p>

<p align="center">
  <a href="./README.md">🌍 English</a> •
  <a href="./README_ZH.md">🇨🇳 中文</a> •
  <a href="https://arxiv.org/abs/2507.01061">📄 arXiv</a> •
  <a href="https://www.epitome-ai.com/">🌐 Website</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/JDK-1.8+-orange.svg" alt="JDK">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.3-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-8.x-blue.svg" alt="MySQL">
  <img src="https://img.shields.io/badge/Redis-Latest-red.svg" alt="Redis">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
</p>

---

## 📖 Table of Contents

- [Introduction](#-introduction)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Requirements](#-requirements)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [Configuration Guide](#-configuration-guide)
- [Other](#-other)
- [License](#-license)
- [Contact Us](#-contact-us)
- [Citation](#-citation)

---

## 🎯 Introduction

**Epitome** is the world's first experimental open platform dedicated to the deep integration of artificial intelligence and social sciences. Through seven major modules, the platform provides intelligent assistance across the entire process—ranging from **experimental intervention, experimental environment, to experimental participants**—helping researchers easily design and conduct complex human-computer interaction experimental scenarios. This enables systematic research on AI's social impact and exploration of comprehensive solutions, advancing the harmonious integration of artificial intelligence into human society.

For more information, please refer to [the introduction video](https://youtu.be/i7WFY5xNJ_c).

### 🎓 Application Scenarios

- Social science experimental research
- Human-computer interaction behavior analysis
- AI social impact assessment
- Group dynamics simulation
- Interdisciplinary experimental design

---

## ✨ Key Features

### 🎭 Innovative Interactive Scenarios

#### Role-Playing Robots
- 🤖 Simulate diverse social roles such as judges and historical figures
- 🌐 Enable cross-temporal and cross-identity dialogue experiments
- 📊 Provide standardized and replicable interactive elements for abstract social problem research

#### Human-AI Hybrid Chat Rooms
- 👥 Support multi-party interaction patterns of "human-human-AI-AI"
- 🔄 Simulate complex social networks and group interaction contexts
- 🔬 Open new possibilities for group behavior and social dynamics research

#### Social Simulation
- 💼 Simulate human economic transactions, social conflicts, and other behaviors through intelligent agents
- ⏱️ Provide large-scale, long-term social scenario simulations
- 💰 Enable low-cost policy effect prediction and social norm evolution research


### 🔄 Complete-Flow Closed-Loop Support

- **🎯 Experiment Management**: Complete lifecycle management from design to execution
- **📦 Material Management**: Centralized management of multimodal experimental materials
- **📋 Questionnaire Management**: Flexible questionnaire design and data collection
- **⚙️ Workflow Management**: Visual orchestration of modular components
- **📈 Data Analysis**: Real-time data collection and export functionality


### 🔓 Open Research Design

#### Diverse Material Support
- 📝 Text, images, videos
- 📄 PDFs, PPTs, and various other formats
- 🤖 Multimodal large models supporting experimental material preparation

#### Open Architecture Design
- 🔧 Customized workflow design
- 🧩 Flexible agent system configuration
- 🎨 Adaptable to complex experimental paradigms across multiple disciplines


### 🎮 Controllable Experimental Advancement

- **🔍 Scalable Experimental Scale**: Break through the venue and personnel limitations of traditional offline experiments
- **⚡ Precise Process Control**: Preset interaction processes that balance experimental control and realistic simulation
- **♻️ Enhanced Reproducibility**: Standardized experimental processes improve reproducibility and verifiability
- **📊 Real-time Data Collection**: Record experimental data throughout, supporting multi-dimensional analysis

---

## 🏗️ Tech Stack

### Core Technologies

| Category | Technology | Version |
|----------|------------|---------|
| **Framework** | Spring Boot | 2.7.3 |
| **Persistence** | MyBatis-Plus | - |
| **Database** | MySQL | 8.x |
| **Cache** | Redis (Jedis) | - |
| **Security** | Apache Shiro | - |
| **Session Sharing** | Shiro-Redis | - |
| **API Docs** | Knife4j (Swagger) | - |
| **Export** | EasyExcel, EasyPoi | - |
| **File Storage** | MinIO | - |
| **Real-time Communication** | WebSocket | - |
| **Utilities** | Hutool, FastJSON, Druid | - |

---

## 📋 Requirements

### Essential Environment

- ☕ **JDK**: 1.8+
- 📦 **Maven**: 3.x
- 🗄️ **MySQL**: 8.x
- 🔴 **Redis**: 5.x+

---

## 🚀 Quick Start

### Step 1: Clone and Build

```bash
# Clone the repository
git clone https://github.com/epitome-AISS/epitome.git

# Enter project directory
cd epitome

# Install dependencies and package (including local lib)
mvn clean install -DskipTests
```

### Step 2: Modify Configuration Files

Main configuration file `src/main/resources/application.yml`:

The active profile is set via `spring.profiles.active` in `application.yml` (e.g., `testLocal`)
```yaml
spring:
  profiles:
    active: testLocal  # Select the active environment configuration
```

Configure in the corresponding profile file (e.g., `application-testLocal.yml`):
  - **MySQL**: `spring.datasource.url / username / password`
  - **Redis**: `spring.redis.host / port`, etc.

```yaml
spring:
  datasource:
    url: jdbc:mysql://{your_mysql_ip}:3306/ailab?useSSL=false&characterEncoding=utf8
    username: {your_mysql_username}
    password: {your_mysql_password}
    
  redis:
    host: {your_redis_ip}
    port: 6379
    password: {your_redis_password}
```

> 💡 **Tip**: Replace placeholders like `{your_mysql_ip}` with actual values

### Step 3: Run the Project

Run with Maven

```bash
# Run with Maven (uses the active profile)
mvn spring-boot:run

# Or package first, then run
mvn clean package -DskipTests
java -jar target/ailab-1.0.0.jar
```

Default port is defined in each profile config (e.g., 8085 for local).

### Step 4: Verify Startup

Access Knife4j documentation to verify the service is running (adjust context-path if needed):

- 📚 **API Documentation Example**: `http://localhost:8085/doc.html`

---

## 📁 Project Structure

```
src/
└── main/
    ├── java/com/nbtech/ailab/
    │   ├── AiLabApplication.java           # 🚀 Application entry
    │   ├── biz/                             # 💼 Business layer
    │   │   ├── controller/                  # 🎮 Controllers
    │   │   ├── dao/                         # 🗄️ Data access layer
    │   │   ├── dto/                         # 📦 Data transfer objects
    │   │   ├── entity/                      # 📋 Entities
    │   │   └── service/                     # ⚙️ Service layer
    │   ├── common/                          # 🔧 Common modules
    │   ├── config/                          # ⚙️ Configuration classes
    │   ├── facade/                          # 🎭 Facade/aggregation services
    │   ├── security/                        # 🔒 Security configuration
    │   ├── util/                            # 🛠️ Utilities
    │   ├── vo/                              # 👁️ View objects
    │   └── websocket/                       # 🔌 WebSocket
    └── resources/                           # SQL scripts and change logs
        └── mapper/                          # MyBatis mapper files
```

## ⚙️ Configuration Guide

- **Multi-environment**: Switch via `spring.profiles.active` (e.g., `testLocal`, `testDev`, `formalDev`)
- **File Upload**: Configured in `application.yml` under `spring.servlet.multipart` (e.g., 500MB limit)
- **Database**: MyBatis-Plus with logical delete field `is_deleted`.  

---

## 🐳 Other

- **Local Dependency**: `lib/common-0.0.1-release.jar` must exist and be installed or referenced via `system` scope
- **Deployment**: A `Dockerfile` is provided for building and deploying the image as needed

---

## 📄 License

This project is licensed under the [MIT](https://opensource.org/licenses/MIT) License.

```
MIT License

Copyright (c) 2025 Epitome Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

---

## 📞 Contact Us

If you are interested in this platform or have social experiment needs, please feel free to contact us through the following methods:

### 📧 Contact Email
**epitome_AI4SS@163.com**

### 💬 Social Media

<table>
  <tr>
    <td align="center">
      <strong>WeChat Official Account</strong><br>
      <img src="./static/WeChatOfficialAccount.jpg" width="150"><br>
      <sub>Epitome-AI</sub>
    </td>
    <td align="center">
      <strong>Bilibili</strong><br>
      <img src="./static/Bilibili.jpg" width="150"><br>
      <sub>Follow our video updates</sub>
    </td>
    <td align="center">
      <strong>RedNote</strong><br>
      <img src="./static/RedNotes.jpg" width="150"><br>
      <sub>Check our latest shares</sub>
    </td>
  </tr>
</table>

### 🌐 Online Resources

- 🏠 **Official Website**: [https://www.epitome-ai.com/](https://www.epitome-ai.com/)
- 📄 **Research Paper**: [arXiv:2507.01061](https://arxiv.org/abs/2507.01061)
- 💻 **GitHub**: [https://github.com/epitome-AISS/epitome](https://github.com/epitome-AISS/epitome)

---

## 📖 Citation

If you use the Epitome platform in your research, please cite our paper:

```bibtex
@misc{qu2025epitomepioneeringexperimentalplatform,
  title={Epitome: Pioneering an Experimental Platform for AI-Social Science Integration}, 
  author={Jingjing Qu and Kejia Hu and Jun Zhu and Yulei Ye and Wenhao Li and Teng Wang and Zhiyun Chen and Chaochao Lu and Aimin Zhou and Xiangfeng Wang and Xia Hu and James Evans},
  year={2025},
  eprint={2507.01061},
  archivePrefix={arXiv},
  primaryClass={cs.CY},
  url={https://arxiv.org/abs/2507.01061}
}
```

---

<p align="center">
  <sub>Built with ❤️ by Epitome Team</sub>
</p>

<p align="center">
  <sub>© 2025 Epitome. All rights reserved.</sub>
</p>
