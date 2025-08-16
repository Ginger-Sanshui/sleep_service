# Sleep Service：智能睡眠辅助系统完整架构文档

<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue.svg" alt="MySQL">
  <img src="https://img.shields.io/badge/Etcd-3.5.9-purple.svg" alt="Etcd">
  <img src="https://img.shields.io/badge/K8s-1.24+-orange.svg" alt="Kubernetes">
  <img src="https://img.shields.io/badge/ESP8266-ESP32-green.svg" alt="IoT Devices">
  <img src="https://img.shields.io/badge/JDK-17-red.svg" alt="JDK">
</div>

---

## 📋 项目概述

**Sleep Service** 是一款基于物联网与分布式技术的智能睡眠辅助系统，通过实时采集环境与生理数据，结合智能决策引擎为用户提供个性化睡眠优化方案。系统采用微服务架构设计，支持多设备接入、分布式数据存储与容器化部署，为用户打造科学、便捷的睡眠管理生态。

**核心价值**：
- 实时监测睡眠环境关键指标（温度、湿度、噪音）
- 结合生理数据（心率）提供精准睡眠建议
- 集成舒缓音乐库辅助睡眠
- 支持多终端访问与容器化部署

---

## 🏗️ 系统架构

### 整体架构图
<div align="center">
<img src="https://picsum.photos/id/0/1200/800" alt="Sleep Service 系统架构图" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
<p style="margin-top: 8px; color: #666; font-size: 14px;">图1：Sleep Service 五层架构全景图</p>
</div>
### 技术栈详情

| 模块 | 技术选型 | 版本 | 核心作用 |
|------|---------|------|---------|
| 后端框架 | Spring Boot | 3.4.4 | 提供RESTful API与服务集成 |
| 数据持久化 | MySQL | 8.0.33 | 存储历史传感器数据与用户配置 |
| 分布式存储 | Etcd | 3.5.9 | 实时数据缓存与服务注册发现 |
| 容器编排 | Kubernetes | 1.24+ | 服务部署、扩缩容与高可用管理 |
| 可视化工具 | Kuboard | v3 | K8s集群可视化管理界面 |
| 物联网设备 | ESP8266/ESP32 | - | 环境与生理数据采集终端 |
| 构建工具 | Maven | 3.9+ | 项目构建与依赖管理 |
| 开发环境 | JDK | 17 | Java开发工具包 |

---

## 🚀 核心功能模块

### 1. 多维度数据采集模块

#### 功能描述
实时接收来自物联网设备的环境与生理数据，支持多设备类型与数据格式。

#### 技术实现
- **设备适配**：
  - ESP8266：采集温度、湿度、噪音数据
  - ESP32：采集心率数据（支持数据自动校准）

- **核心接口**：
  ```
  POST /addSensor       # 接收ESP8266环境数据
  POST /addSensor32     # 接收ESP32心率数据
  ```

- **数据处理代码**：
  ```java
  @PostMapping("/addSensor32")
  public ResponseEntity<String> addSensor32(@RequestBody Sensor32Request request) {
      try {
          // 心率数据校准（原始值/10000）
          int calibratedRate = Integer.parseInt(request.getRate()) / 10000;
          
          // 同步存储至Etcd与MySQL
          etcdService.putValue("heart_rate", String.valueOf(calibratedRate));
          sensorService.saveHeartRate(calibratedRate);
          
          return ResponseEntity.ok("心率数据已接收");
      } catch (Exception e) {
          return ResponseEntity.status(500).body("数据处理失败: " + e.getMessage());
      }
  }
  ```

### 2. 智能睡眠策略推荐模块

#### 功能描述
基于多维度数据（温度、湿度、心率、噪音）动态生成个性化睡眠建议。

#### 策略生成逻辑
```java
public class StrategyService {
    /**
     * 生成综合睡眠策略
     */
    public String generateStrategy(int temperature, int humidity, int heartRate, int noiseLevel) {
        StringBuilder strategy = new StringBuilder();
        
        // 温度策略
        if (temperature < 15) {
            strategy.append("环境温度较低，建议使用电热毯预热床铺并穿着保暖睡衣；");
        } else if (temperature > 26) {
            strategy.append("环境温度较高，建议使用风扇或空调将温度调节至22-24℃；");
        }
        
        // 湿度策略
        if (humidity < 30) {
            strategy.append("空气干燥，建议使用加湿器将湿度维持在40-60%；");
        } else if (humidity > 70) {
            strategy.append("空气潮湿，建议开启除湿功能并减少卧室水源；");
        }
        
        // 心率策略
        if (heartRate > 90) {
            strategy.append("心率偏高，建议睡前进行10分钟深呼吸练习；");
        }
        
        // 噪音策略
        if (noiseLevel > 50) {
            strategy.append("环境噪音较大，建议使用白噪音掩盖干扰或佩戴降噪耳塞；");
        }
        
        return strategy.length() > 0 ? strategy.toString() : "当前环境适合睡眠，建议保持规律作息。";
    }
}
```

#### 接口调用
```
GET /getStrategy    # 获取睡眠策略（需携带认证令牌）
```

### 3. 舒缓音乐资源管理模块

#### 功能描述
提供精选助眠音乐列表，支持歌曲与对应歌手信息查询。

#### 音乐资源列表
| 歌曲名 | 歌手 | 音乐类型 | 时长 |
|-------|------|---------|------|
| 疑心病 | 徐良 | 抒情 | 3:45 |
| 心似烟火 | 陈壹千 | 治愈 | 4:12 |
| 夏天的风 | 温岚 | 舒缓 | 4:28 |
| 海底 | 一支榴莲 | 平静 | 3:56 |
| 东京不太热 | 封茗囧菌 | 轻柔 | 3:30 |
| 大雾 | 张一乔 | 空灵 | 4:05 |
| 阿拉斯加海湾 | 蓝心羽 | 治愈 | 3:42 |

#### 核心接口
```
GET /getMusicList      # 获取歌曲列表
GET /getMusicListTwo   # 获取对应歌手列表
```

#### 实现代码
```java
@Service
public class MusicService {
    
    /**
     * 从资源文件读取歌曲列表
     */
    public List<String> getMusicList() {
        try {
            Resource resource = new ClassPathResource("listName.txt");
            return Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取音乐列表失败", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * 从资源文件读取歌手列表
     */
    public List<String> getSingerList() {
        try {
            Resource resource = new ClassPathResource("Listming.txt");
            return Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取歌手列表失败", e);
            return Collections.emptyList();
        }
    }
}
```

### 4. 安全认证模块

#### 功能描述
基于令牌的接口访问控制，保护敏感数据与操作接口。

#### 认证流程
1. 获取令牌：通过`/Token`接口验证用户信息
2. 使用令牌：在HTTP请求头中携带`Author-token`参数
3. 令牌验证：拦截器验证令牌有效性

#### 核心代码
```java
/**
 * 令牌认证拦截器
 */
@Component
public class TokenAuthInterceptor implements HandlerInterceptor {
    private static final String VALID_TOKEN = "86146e3c320c71484befa19486d76981";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        // 开放接口无需认证
        String path = request.getRequestURI();
        if ("/Token".equals(path) || "/addSensor".equals(path)) {
            return true;
        }
        
        // 验证令牌
        String token = request.getHeader("Author-token");
        if (token == null || !VALID_TOKEN.equals(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("认证失败：无效或过期的令牌");
            return false;
        }
        
        return true;
    }
}
```

#### 接口调用
```
POST /Token    # 获取访问令牌
请求体: {"user":"X","name":"SDD"}
响应: 86146e3c320c71484befa19486d76981
```

---

## 💾 数据模型设计

### 核心实体类

#### 1. 传感器数据实体（Sensor.java）
```java
@Entity
@Table(name = "sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;               // 主键ID
    
    private String temperature;       // 温度（℃）
    
    private String humidity;          // 湿度（%）
    
    private Integer noise;            // 噪音（dB）
    
    private Integer heartRate;        // 心率（次/分钟）
    
    private LocalDateTime collectTime;// 采集时间
    
    @Version
    private Long version;             // 乐观锁版本号，用于并发控制
}
```

#### 2. 数据访问层（SensorRepository.java）
```java
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    // 查询最近的传感器数据
    Optional<Sensor> findTopByOrderByCollectTimeDesc();
    
    // 按时间范围查询数据
    List<Sensor> findByCollectTimeBetween(LocalDateTime start, LocalDateTime end);
}
```

---

## 📊 接口文档

| 接口地址 | 请求方法 | 功能描述 | 请求参数 | 认证要求 |
|---------|---------|---------|---------|---------|
| `/Token` | POST | 获取访问令牌 | `{"user":"X","name":"SDD"}` | 无 |
| `/addSensor` | POST | 接收ESP8266数据 | `{"temperature":"24","humidity":"55","noise":"32"}` | 无 |
| `/addSensor32` | POST | 接收ESP32心率数据 | `{"rate":"750000"}` | 无 | 因发送端未换算 实际值为75
| `/getStrategy` | GET | 获取睡眠策略 | - | 需要 |
| `/getMusicList` | GET | 获取歌曲列表 | - | 需要 |
| `/getMusicListTwo` | GET | 获取歌手列表 | - | 需要 |
| `/getLatestSensorData` | GET | 获取最新传感器数据 | - | 需要 |
| `/getEtcdIP` | GET | 获取Etcd集群地址 | - | 需要 |

---

## 🛠️ 部署指南

### 1. 基于VM镜像的K8s集群快速部署

#### 前置条件
- 虚拟机软件：VMware Workstation 16+ 或 VirtualBox 6+
- 硬件资源：至少8GB内存，20GB空闲磁盘空间
- 网络环境：支持桥接网络的环境

#### 部署步骤

1. **获取VM镜像**
   ```
   下载地址：https://example.com/sleep-service/etcd-cluster-vm
   镜像包含：3节点K8s集群（1主2从）、预配置的Etcd集群、Docker环境
   ```

2. **导入虚拟机**
   - 解压下载的镜像文件（`k8s-etcd-cluster-v1.24.7.zip`）
   - 在VMware中选择"打开虚拟机"，导入`.vmx`文件
   - 重复操作导入3台节点虚拟机（master/worker1/worker2）

3. **启动集群**
   - 按顺序启动master节点，再启动worker节点
   - 默认登录信息：用户名`k8s-admin`，密码`k8s@123`
   - 集群自动初始化，约2-3分钟完成

4. **验证集群状态**
   ```bash
   # 登录master节点
   ssh k8s-admin@192.168.58.128
   
   # 检查节点状态
   kubectl get nodes
   # 预期输出3个节点均为Ready状态
   
   # 检查Etcd集群健康状态
   etcdctl --endpoints=http://192.168.58.128:2379,http://192.168.58.129:2379,http://192.168.58.130:2379 endpoint health
   ```

### 2. Kuboard可视化界面部署

#### 部署步骤
1. 在master节点执行安装命令：
   ```bash
   kubectl apply -f https://addons.kuboard.cn/kuboard/kuboard-v3.yaml
   ```

2. 确认部署状态：
   ```bash
   kubectl get pods -n kuboard
   # 等待kuboard-v3-xxxx pod状态变为Running
   ```

3. 访问Kuboard：
   - 浏览器打开：`http://192.168.58.128:30000`
   - 初始登录信息：用户名`admin`，密码`Kuboard123`

4. 导入集群：
   - 点击"添加集群"，输入集群名称`sleep-service-cluster`
   - 自动加载kubeconfig配置，点击"导入"完成配置

#### Kuboard核心功能
- 集群概览：节点资源使用率、Pod分布、健康状态监控
- 工作负载管理：部署、伸缩、升级Sleep Service应用
- 日志中心：实时查看应用运行日志与接口调用记录
- 资源监控：CPU、内存、网络IO等性能指标可视化

### 3. Sleep Service应用部署

#### 部署步骤
1. 克隆代码仓库：
   ```bash
   git clone https://github.com/Ginger-Sanshui/sleep_service.git
   cd sleep_service/demo
   ```

2. 构建Docker镜像：
   ```bash
   # 构建应用镜像
   docker build -t sleep-service:v1.0 .
   
   # 推送镜像到集群内部仓库（或使用私有仓库）
   docker tag sleep-service:v1.0 192.168.58.128:5000/sleep-service:v1.0
   docker push 192.168.58.128:5000/sleep-service:v1.0
   ```

3. 部署到K8s集群：
   ```bash
   # 创建部署配置
   kubectl apply -f k8s/deployment.yaml
   
   # 创建服务暴露
   kubectl apply -f k8s/service.yaml
   
   # 查看部署状态
   kubectl get pods
   ```

4. 验证服务：
   ```bash
   # 检查服务暴露端口
   kubectl get svc sleep-service
   
   # 测试接口访问
   curl http://192.168.58.128:30084/getMusicList
   ```

### 4. 手机端APP配置

#### APP功能说明
- 实时数据展示：温度、湿度、噪音、心率数据曲线
- 睡眠策略接收：展示系统生成的睡眠建议
- 音乐控制：播放助眠音乐列表
- 设备管理：查看传感器连接状态

#### 配置步骤
1. 下载APP：
   - Android：[SleepService_v1.0.apk](https://example.com/sleep-service/app/android)
   - iOS：通过TestFlight测试链接安装

2. 网络配置：
   - 打开APP，进入"设置"→"服务器配置"
   - 输入API地址：`http://192.168.58.128:30084`（根据实际部署调整）

3. 登录认证：
   - 输入默认账号：user=X，name=SDD
   - 点击"登录"，APP自动获取并保存令牌

4. 功能使用：
   - 首页：查看实时数据与睡眠策略
   - 音乐页：浏览并播放助眠音乐
   - 数据页：查看历史数据曲线

---

## 🔍 运维与监控

### 日常运维命令
```bash
# 查看应用日志
kubectl logs -f deployment/sleep-service

# 重启应用
kubectl rollout restart deployment sleep-service

# 扩展副本数
kubectl scale deployment sleep-service --replicas=3

# 查看Etcd存储数据
etcdctl get / --prefix --keys-only

# 查看数据库连接
kubectl exec -it <mysql-pod> -- mysql -u root -pSensor@123 -e "show processlist"
```

### 常见问题排查
1. **Kuboard访问失败**
   ```bash
   # 检查服务是否正常
   kubectl get svc -n kuboard
   
   # 检查端口映射
   netstat -tpln | grep 30000
   ```

2. **传感器数据不上报**
   ```bash
   # 检查服务端口是否可达
   telnet 192.168.58.128 30084
   
   # 查看接口调用日志
   kubectl logs -f deployment/sleep-service | grep addSensor
   ```

3. **Etcd集群异常**
   ```bash
   # 检查集群成员状态
   etcdctl member list
   
   # 查看集群健康状态
   etcdctl endpoint health --cluster
   ```

---

## 📜 许可证

本项目采用 Apache 2.0 许可证开源，详情参见 LICENSE 文件。

---

<div align="center">
  <p>✨ 让科技守护你的优质睡眠 ✨</p>
  <p>项目地址：<a href="https://github.com/Ginger-Sanshui/sleep_service">https://github.com/Ginger-Sanshui/sleep_service</a></p>
</div>




