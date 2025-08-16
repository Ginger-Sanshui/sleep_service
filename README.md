智能睡眠辅助系统 Sleep Service 完整简介
Sleep Service 是一款基于 Spring Boot 3.4.4 构建的智能睡眠辅助系统，深度整合 物联网设备数据采集、分布式存储 与 智能决策引擎，为用户提供个性化睡眠优化方案。系统通过 ESP8266/ESP32 设备实时采集环境温湿度、噪音及心率数据，结合 MySQL 8.0 持久化存储与 Etcd 集群 高可用存储，确保数据安全与实时性。
核心功能与技术亮点：
多设备数据采集
通过 /addSensor 和 /addSensor32 接口分别接收 ESP8266（温湿度、噪音）与 ESP32（心率）数据，支持 JSON 格式传输 与 数据校准处理（如心率数据除以 10000 校准）。
分布式存储架构
采用 双写机制：传感器数据同时写入 MySQL（带 @Retryable 事务重试 机制）和 Etcd 集群（支持 服务注册与租约续约），通过 Kubernetes 编排 实现 Etcd 集群的高可用部署（StatefulSet 管理）。
智能睡眠策略推荐
基于 温度、湿度、心率、噪音 四维度数据，通过 Strategy 类 动态生成组合建议（如温度低于 10℃ 推荐饮用温热饮品，湿度高于 60% 建议开启除湿机）。
音乐资源管理
内置舒缓音乐列表（如《海底》《阿拉斯加海湾》等），通过 /getMusicList（歌曲名）和 /getMusicListTwo（对应歌手）接口提供资源查询，支持前后端分离架构调用。
安全认证机制
基于 令牌验证 保护敏感接口，通过 /Token 接口获取凭证（默认令牌：86146e3c320c71484befa19486d76981），未授权请求将被 拦截器 拒绝访问。
容器化部署支持
提供完整 Kubernetes 配置，包括 Deployment（双副本高可用）、NodePort 服务暴露、ConfigMap 环境配置及 NetworkPolicy 网络隔离，支持边缘设备跨网络接入。
技术栈概览：
后端框架：Spring Boot 3.4.4、Spring Data JPA
数据库：MySQL 8.0（持久化）、Etcd（分布式存储）
容器编排：Kubernetes 1.24+（StatefulSet/Deployment/Service）
物联网设备：ESP8266/ESP32（数据采集终端）
安全机制：令牌认证、接口拦截器
构建工具：Maven 3.9+、JDK 17+

通过 简洁的 API 设计 与 可靠的分布式架构，Sleep Service 可无缝对接各类物联网设备与前端应用，为用户打造科学、个性化的睡眠管理体验。
