<p align="center">
  <img src="./static/logo.png" width="280" alt="Epitome Logo">
</p>

<h1 align="center">Epitome</h1>

<p align="center">
  <strong>开创AI与社会科学融合的实验平台</strong>
</p>

<p align="center">
  <a href="./README.md">🌍 English</a> •
  <a href="./README_ZH.md">🇨🇳 中文</a> •
  <a href="https://arxiv.org/abs/2507.01061">📄 arXiv</a> •
  <a href="https://www.epitome-ai.com/">🌐 官网</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/JDK-1.8+-orange.svg" alt="JDK">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.3-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-8.x-blue.svg" alt="MySQL">
  <img src="https://img.shields.io/badge/Redis-Latest-red.svg" alt="Redis">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
</p>

---

## 📖 目录

- [项目简介](#-项目简介)
- [核心特性](#-核心特性)
- [技术架构](#-技术架构)
- [环境要求](#-环境要求)
- [快速开始](#-快速开始)
- [项目结构](#-项目结构)
- [配置指南](#-配置指南)
- [其他](#-其他)
- [开源许可](#-开源许可)
- [联系我们](#-联系我们)
- [引用](#-引用)

---

## 🎯 项目简介

**Epitome** 是全球首个专注于人工智能与社会科学深度融合的实验开放平台。平台通过七大核心模块，从**实验干预-实验环境-实验参与者**全流程智能化帮助研究者轻松设计并运行复杂的人机交互实验场景，实现对人工智能社会影响的系统性研究和综合解决方案的探索，推进人工智能更加和谐地融入人类社会。

### 🎓 适用场景

- 社会科学实验研究
- 人机交互行为分析
- AI社会影响评估
- 群体动力学模拟
- 跨学科实验设计

---

## ✨ 核心特性

### 🎭 创新性交互场景

#### 角色扮演机器人
- 🤖 模拟法官、历史人物等多样化社会角色
- 🌐 实现跨时空、跨身份的对话实验
- 📊 为抽象社会问题研究提供标准化且可重复的交互元素

#### 人智混合聊天室
- 👥 支持"人-人-机-机"多元互动模式
- 🔄 模拟复杂的社会网络和群体互动情境
- 🔬 为群体行为和社会动力学研究开创新可能

#### 社会模拟仿真
- 💼 通过智能体Agent模拟人类经济交易、社交冲突等行为
- ⏱️ 提供大规模、长时间跨度的社会情境仿真
- 💰 实现低成本的政策效果预测和社会规范演化研究


### 🔄 全流程闭环支持

- **🎯 实验管理**：从设计到执行的完整生命周期管理
- **📦 素材管理**：多模态实验材料的集中式管理
- **📋 问卷管理**：灵活的问卷设计与数据收集
- **⚙️ 工作流管理**：模块化组件的可视化编排
- **📈 数据分析**：实时数据采集与导出功能


### 🔓 开放性研究设计

#### 多样化素材支持
- 📝 文本、图片、视频
- 📄 PDF、PPT等多种格式
- 🤖 多模态大模型支撑实验材料准备

#### 开放架构设计
- 🔧 工作流自定义设计
- 🧩 Agent系统灵活配置
- 🎨 适配多学科复杂实验范式


### 🎮 可控性实验推进

- **🔍 实验规模可拓展**：突破传统线下实验的场地和人员限制
- **⚡ 精确流程控制**：预设交互流程，平衡控制与现实模拟
- **♻️ 可重复性强化**：标准化实验过程，提高可重复性和可验证性
- **📊 数据实时采集**：全程记录实验数据，支持多维度分析

---

## 🏗️ 技术架构

### 核心技术栈

| 类别 | 技术选型 | 版本 |
|------|---------|------|
| **核心框架** | Spring Boot | 2.7.3 |
| **持久层** | MyBatis-Plus | - |
| **数据库** | MySQL | 8.x |
| **缓存** | Redis (Jedis) | - |
| **安全框架** | Apache Shiro | - |
| **会话共享** | Shiro-Redis | - |
| **API文档** | Knife4j (Swagger) | - |
| **Excel处理** | EasyExcel, EasyPoi | - |
| **文件存储** | MinIO | - |
| **实时通信** | WebSocket | - |
| **工具库** | Hutool, FastJSON, Druid | - |

---

## 📋 环境要求

### 必需环境

- ☕ **JDK**: 1.8+
- 📦 **Maven**: 3.x
- 🗄️ **MySQL**: 8.x
- 🔴 **Redis**: 5.x+

---

## 🚀 快速开始

### 步骤 1: 克隆与构建项目

```bash
# 克隆仓库
git clone https://github.com/your-org/epitome.git

# 进入项目目录
cd epitome

# 安装依赖并打包（含本地 lib 依赖）
mvn clean install -DskipTests
```

### 步骤 2: 修改配置文件

主配置`src/main/resources/application.yml`：

激活的 Profile 在 `application.yml` 中通过 `spring.profiles.active` 指定（如 `testLocal`）
```yaml
spring:
  profiles:
    active: testLocal  # 选择激活的环境配置
```

在对应 Profile 的环境配置文件（如 `application-testLocal.yml`）中配置：
  - **MySQL**：`spring.datasource.url / username / password`
  - **Redis**：`spring.redis.host / port` 等 

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

> 💡 **提示**: 将 `{your_mysql_ip}` 等占位符替换为实际值

### 步骤 3: 运行项目

使用 Maven 运行

```bash
# 使用 Maven 运行（会使用当前激活的 profile）
mvn spring-boot:run

# 或先打包再运行
mvn clean package -DskipTests
java -jar target/ailab-1.0.0.jar
```

默认端口见各 profile 配置（如本地常用 8085）。

### 步骤 4: 验证启动

访问 Knife4j 文档验证服务是否正常启动（以实际 context-path 为准）：

- 📚 **API文档示例**: `http://localhost:8085/doc.html`

---

## 📁 项目结构

```
src/
└── main/
    ├── java/com/nbtech/ailab/
    │   ├── AiLabApplication.java           # 🚀 启动类
    │   ├── biz/                             # 💼 业务层
    │   │   ├── controller/                  # 🎮 控制器
    │   │   ├── dao/                         # 🗄️ 数据访问层
    │   │   ├── dto/                         # 📦 数据传输对象
    │   │   ├── entity/                      # 📋 实体类
    │   │   └── service/                     # ⚙️ 服务层
    │   ├── common/                          # 🔧 公共模块
    │   ├── config/                          # ⚙️ 配置类
    │   ├── facade/                          # 🎭 门面/聚合服务
    │   ├── security/                        # 🔒 安全配置
    │   ├── util/                            # 🛠️ 工具类
    │   ├── vo/                              # 👁️ 视图对象
    │   └── websocket/                       # 🔌 WebSocket
    └── resources/                           # SQL 脚本与变更记录
        └── mapper/                          # MyBatis映射文件
```

## ⚙️ 配置指南

- **多环境**：通过 `spring.profiles.active` 切换（如 `testLocal`、`testDev`、`formalDev`）。  
- **文件上传**：在 `application.yml` 中配置了 `spring.servlet.multipart`（如 500MB 限制）。  
- **数据库**：使用 MyBatis-Plus，逻辑删除字段为 `is_deleted`。  
---

## 🐳 其他
- 本地依赖：`lib/common-0.0.1-release.jar`，需存在且已安装或通过 `system` 作用域引入。  
- 部署：项目内提供 `Dockerfile`，可按需构建镜像部署。

---

## 📄 开源许可

本项目采用 [MIT](https://opensource.org/licenses/MIT) 许可证。

```
MIT License

Copyright (c) 2025 Epitome Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

---

## 📞 联系我们

如果您对本平台感兴趣，或有社会实验需求，欢迎通过以下方式与我们联系：

### 📧 联系邮箱
**epitome_AI4SS@163.com**

### 💬 社交媒体

<table>
  <tr>
    <td align="center">
      <strong>微信公众号</strong><br>
      <img src="./static/WeChatOfficialAccount.jpg" width="150"><br>
      <sub>Epitome-AI</sub>
    </td>
    <td align="center">
      <strong>Bilibili</strong><br>
      <img src="./static/Bilibili.jpg" width="150"><br>
      <sub>关注我们的视频动态</sub>
    </td>
    <td align="center">
      <strong>小红书</strong><br>
      <img src="./static/RedNotes.jpg" width="150"><br>
      <sub>查看最新分享</sub>
    </td>
  </tr>
</table>

### 🌐 在线资源

- 🏠 **官方网站**: [https://www.epitome-ai.com/](https://www.epitome-ai.com/)
- 📄 **研究论文**: [arXiv:2507.01061](https://arxiv.org/abs/2507.01061)
- 💻 **GitHub**: [https://github.com/your-org/epitome](https://github.com/epitome-AISS/epitome)

---

## 📖 引用

如果您在研究中使用了 Epitome 平台，请引用我们的论文：

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
