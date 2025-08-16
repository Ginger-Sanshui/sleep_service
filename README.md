# Sleep Service：智能睡眠辅助系统

<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue.svg" alt="MySQL">
  <img src="https://img.shields.io/badge/Etcd-3.5.9-purple.svg" alt="Etcd">
  <img src="https://img.shields.io/badge/K8s-1.24+-orange.svg" alt="Kubernetes">
  <img src="https://img.shields.io/badge/JDK-17-red.svg" alt="JDK">
</div>

## 🌟 系统简介

**Sleep Service** 是一款基于物联网与分布式技术的智能睡眠辅助系统，通过实时采集环境与生理数据，结合智能决策引擎为用户提供个性化睡眠优化方案。系统支持多设备接入、分布式数据存储与容器化部署，打造科学、便捷的睡眠管理生态。

## 🚀 核心功能

### 1. 多维度数据采集
- **物联网设备支持**：  
  适配 ESP8266（环境数据）与 ESP32（生理数据）
- **采集指标**：  
  温度、湿度、噪音、心率
- **数据接口**：  
  - `POST /addSensor`：接收 ESP8266 环境数据  
  - `POST /addSensor32`：接收 ESP32 心率数据（自动校准：原始值/10000）  


### 2. 智能睡眠策略推荐
基于实时数据生成组合建议：  
- **温度调节**：  
  低温建议饮用温热饮品，高温推荐水循环凉垫  
- **湿度控制**：  
  根据湿度范围提供加湿/除湿方案  
- **心率管理**：  
  异常心率时触发环境优化建议  
- **噪音处理**：  
  分级推荐白噪音或降噪耳塞  


### 3. 舒缓音乐资源库
内置精选助眠音乐列表：  

| 歌曲名         | 歌手       |
|----------------|------------|
| 疑心病         | 徐良       |
| 心似烟火       | 陈壹千     |
| 夏天的风       | 温岚       |
| 海底           | 一支榴莲   |
| 东京不太热     | 封茗囧菌   |
| 大雾           | 张一乔     |
| 阿拉斯加海湾   | 蓝心羽     |

- **接口**：  
  - `GET /getMusicList`：获取歌曲列表  
  - `GET /getMusicListTwo`：获取对应歌手列表  


### 4. 安全认证机制
- 基于令牌（Token）的接口访问控制  
- 获取令牌：`POST /Token`（默认账号：X，密码：SDD）  
- 令牌验证：保护 `/getStrategy` 等敏感接口  


## 🛠️ 技术架构

### 核心技术栈
- **后端框架**：  
  Spring Boot 3.4.4  
- **数据存储**：  
  - MySQL 8.0（持久化存储，支持事务重试）  
  - Etcd 集群（实时数据存储与服务发现）  
- **容器编排**：  
  Kubernetes 1.24+  
- **构建工具**：  
  Maven 3.9+  
- **代码质量**：  
  Qodana 静态分析  


### 分布式架构
- **双写存储机制**：  
  传感器数据同时写入 MySQL 与 Etcd  
- **服务注册发现**：  
  基于 Etcd 实现服务自动注册与租约管理  
- **高可用部署**：  
  K8s StatefulSet 管理 Etcd 集群，Deployment 部署应用（双副本）  


## 📊 数据模型

```java
@Entity
@Table(name = "sensor")
@Data
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;               // 主键ID
    private String temperature;       // 温度
    private String humidity;          // 湿度
    private int rate;                 // 心率
    private int noise;                // 噪音
    @Version
    private Long version;             // 乐观锁版本号
}
```


## 📝 接口文档

| 接口地址         | 方法   | 描述                  | 请求参数示例                          |
|------------------|--------|-----------------------|---------------------------------------|
| `/Token`         | POST   | 获取访问令牌          | `{"user":"X","name":"SDD"}`           |
| `/addSensor`     | POST   | 上报环境数据          | `{"temperature":"24","humidity":"55","noise":"32"}` |
| `/addSensor32`   | POST   | 上报心率数据          | `{"rate":"750000"}`（实际心率=750000/10000=75） |
| `/getStrategy`   | GET    | 获取睡眠策略          | 需携带 `Author-token` 头              |
| `/getMusicList`  | GET    | 获取歌曲列表          | -                                     |


## 🔧 部署指南

### 前置条件
- K8s 集群（v1.24+）  
- Helm 3.x  
- Etcd 集群（推荐使用 `bitnami/etcd` Helm Chart）  


### 快速部署
1. 创建配置文件 `configmap.yaml` 存储应用参数  
2. 部署应用：  
   ```bash
   kubectl apply -f configmap.yaml
   kubectl apply -f deployment.yaml
   kubectl expose deployment sleep-service --type=NodePort --port=8084
   ```  
3. 验证部署：  
   ```bash
   kubectl get pods -l app=sleep-service
   ```  


## 📄 许可证

本项目采用 Apache 2.0 许可证开源，详情参见 LICENSE 文件。

---

<div align="center">
  <p>✨ 让科技守护你的优质睡眠 ✨</p>
</div>
