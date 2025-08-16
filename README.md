# Sleep Service: Complete Architecture Documentation for Intelligent Sleep Assistance System

<div align="center">
  <a href="https://github.com/Ginger-Sanshui/sleep_service/tree/china?tab=readme-ov-file" style="text-decoration: none; color: #333; font-weight: bold;">
    👉 切换到 中文版本 (Switch to Chinese Version)
  </a>
</div>


<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue.svg" alt="MySQL">
  <img src="https://img.shields.io/badge/Etcd-3.5.9-purple.svg" alt="Etcd">
  <img src="https://img.shields.io/badge/K8s-1.24+-orange.svg" alt="Kubernetes">
  <img src="https://img.shields.io/badge/ESP8266-ESP32-green.svg" alt="IoT Devices">
  <img src="https://img.shields.io/badge/JDK-17-red.svg" alt="JDK">
</div>

---

## 📋 Project Overview

**Sleep Service** is an intelligent sleep assistance system based on IoT and distributed technology. By collecting real-time environmental and physiological data, it provides personalized sleep optimization solutions through an intelligent decision engine. The system adopts a microservices architecture design, supporting multi-device connectivity, distributed data storage, and containerized deployment, creating a scientific and convenient sleep management ecosystem for users.

**Core Value**:
- Real-time monitoring of key sleep environment indicators (temperature, humidity, noise)
- Precise sleep recommendations based on physiological data (heart rate)
- Integrated soothing music library for sleep assistance
- Support for multi-terminal access and containerized deployment

---

## 🏗️ System Architecture
```
### Overall Architecture Diagram
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                   🔵 Perception Layer (IoT Devices)                         │
│                                                                                             │
│  ┌─────────────────────┐                      ┌─────────────────────┐                       │
│  │    🟢 ESP8266 Device│                      │     🟢 ESP32 Device │                        │
│  │ (Temp/Humid/Noise)  │                      │    (Heart Rate)     │                        │
│  │                     │                      │                     │                        │
│  │  ┌───────────────┐  │                      │  ┌───────────────┐  │                        │
│  │  │  🟡Data Collector│ │                      │  │ 🟡Heart Rate Calib.│ │                        │
│  │  └───────┬───────┘  │                      │  └───────┬───────┘  │                        │
│  │          │          │                      │          │          │                        │
│  └──────────┼──────────┘                      └──────────┼──────────┘                        │
└─────────────┼───────────────────────────────────────────┼─────────────────────────────────────┘
              │                                           │
              ▼                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                   🟣 Access Layer (API Gateway)                             │
│                                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────────────────┐     │
│  │                                🟤Unified API Gateway                               │     │
│  │  (Request Routing/Load Balancing/Token Validation)                                 │     │
│  │                                                                                    │     │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │  │ 🟠/addSensor│ │🟠/addSensor32 │🟠/getStrategy |🟠/getMusicList │   🟠/Token  │     │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘     │
│  └─────────────────────────────────────────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────────────────────────────────────┘
              │                                           │
              ▼                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                   🟡 Service Layer (K8s Cluster Deployment)                 │
│                                                                                             │
│  ┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐                  │
│  │    🟢Data Collection│  │    🟢Strategy Engine│  │    🟢Music Service   │                  │
│  │      Service        │  │      Service        │  │                     │                  │
│  │  ┌───────────────┐  │  │  ┌───────────────┐  │  │  ┌───────────────┐  │                  │
│  │  │ 🔵Data Validation│ │  │ 🔵Multi-dimension│ │  │ 🔵Music List Mgmt.│ │                  │
│  │  └───────┬───────┘  │  │  └───────┬───────┘  │  │  └───────┬───────┘  │                  │
│  │          │          │  │          │          │  │          │          │                  │
│  │  ┌───────▼───────┐  │  │  ┌───────▼───────┐  │  │  ┌───────▼───────┐  │                  │
│  │  │ 🔵Dual Write   │  │  │ 🔵Strategy Cache │  │  │  │ 🔵Resource Opt.│ │                  │
│  │  └───────┬───────┘  │  │  └───────┬───────┘  │  │  └───────────────┘  │                  │
│  │          │          │  │          │          │  │                     │                  │
│  └──────────┼──────────┘  └──────────┼──────────┘  └─────────────────────┘                  │
│             │                        │                                                     │
│             ▼                        ▼                                                     │
│  ┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐                  │
│  │    🔵Data Storage    │  │    🔵Service Discovery│ │    🔵Config Center    │                  │
│  │                     │  │                     │  │                     │                  │
│  │  ┌───────────────┐  │  │  ┌───────────────┐  │  │  ┌───────────────┐  │                  │
│  │  │ 🟣MySQL 8.0  │  │  │  │ 🟣Etcd Cluster│  │  │  │ 🟣Dynamic Config│ │                  │
│  │  │ (Historical)   │  │  │  │ (Service Reg.) │  │  │  │ (Param Mgmt.)  │  │                  │
│  │  └───────────────┘  │  │  └───────────────┘  │  │  └───────────────┘  │                  │
│  │                     │  │                     │  │                     │                  │
│  │  ┌───────────────┐  │  │  ┌───────────────┐  │  │                     │                  │
│  │  │ 🟣Etcd Cluster│  │  │  │ 🟣Lease Renewal│  │  │                    │                  │
│  │  │ (Real-time)    │  │  │  │ (HA Guarantee) │  │  │                     │                  │
│  │  └───────────────┘  │  │  └───────────────┘  │  │                     │                  │
│  └─────────────────────┘  └─────────────────────┘  └─────────────────────┘                  │
└─────────────────────────────────────────────────────────────────────────────────────────────┘
              │                                           │
              ▼                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                   🟤 Application Layer (Clients)                            │
│                                                                                             │
│  ┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐                  │
│  │    🟢Web Client     │  │    🟢Mobile App      │  │    🟢Admin Console   │                  │
│  │                     │  │                     │  │                     │                  │
│  │  ┌───────────────┐  │  │  ┌───────────────┐  │  │  ┌───────────────┐  │                  │
│  │  │ 🔵Data Vis.     │  │  │ 🔵Real-time Data│ │  │ │ 🔵Kuboard v3    │ │                  │
│  │  └───────────────┘  │  │  └───────────────┘  │  │  │ (Cluster Mgmt.)│  │                  │
│  │                     │  │                     │  │  └───────────────┘  │                  │
│  │  ┌───────────────┐  │  │  ┌───────────────┐  │  │                     │                  │
│  │  │ 🔵Strategy View│ │  │  │ 🔵Music Control │  │  ┌───────────────┐  │                  │
│  │  └───────────────┘  │  │  └───────────────┘  │  │  │ 🔵Log Monitoring│ │                  │
│  └─────────────────────┘  └─────────────────────┘  │  └───────────────┘  │                  │
│                                                    └─────────────────────┘                  │
└─────────────────────────────────────────────────────────────────────────────────────────────┘
```
```
Color Legend
Color       Meaning                         Application Scenario
🔵          Layer/Module ID                Perception Layer, Access Layer, Service Layer
🟢          Core Component/Device          ESP Devices, Service Instances, Client Apps
🟡          Functional Submodule           Data Collection Module, Heart Rate Calibration
🟤          Key Infrastructure             API Gateway, Admin Console

Formatting Guide
- Uniform width for all layer boxes ensuring horizontal alignment
- Consistent indentation for internal submodules maintaining vertical alignment
- Data flow arrows (│/└/┘/▼) vertically centered
- Same-size boxes for similar components (ESP devices, service instances)
- Key interfaces/functions annotated below corresponding modules
```
### Technology Stack Details

| Module | Technology | Version | Core Function |
|--------|------------|---------|---------------|
| Backend Framework | Spring Boot | 3.4.4 | Provides RESTful APIs & service integration |
| Data Persistence | MySQL | 8.0.33 | Stores historical sensor data & user configurations |
| Distributed Storage | Etcd | 3.5.9 | Real-time data caching & service discovery |
| Container Orchestration | Kubernetes | 1.24+ | Service deployment, scaling & HA management |
| Visualization Tool | Kuboard | v3 | K8s cluster visual management |
| IoT Devices | ESP8266/ESP32 | - | Environmental & physiological data collectors |
| Build Tool | Maven | 3.9+ | Project build & dependency management |
| Dev Environment | JDK | 17 | Java Development Kit |

---

## 🚀 Core Functional Modules

### 1. Multi-dimensional Data Collection

#### Functional Description
Real-time collection of environmental and physiological data from IoT devices, supporting multiple device types and data formats.

#### Technical Implementation
- **Device Adaptation**:
  - ESP8266: Collects temperature, humidity, noise data
  - ESP32: Collects heart rate data (with auto-calibration)

- **Core Interfaces**:
  ```
  POST /addSensor       # Receive ESP8266 environmental data
  POST /addSensor32     # Receive ESP32 heart rate data
  ```

- **Data Processing Code**:
  ```java
  @PostMapping("/addSensor32")
  public ResponseEntity<String> addSensor32(@RequestBody Sensor32Request request) {
      try {
          // Heart rate calibration (raw value/10000)
          int calibratedRate = Integer.parseInt(request.getRate()) / 10000;
          
          // Sync storage to Etcd & MySQL
          etcdService.putValue("heart_rate", String.valueOf(calibratedRate));
          sensorService.saveHeartRate(calibratedRate);
          
          return ResponseEntity.ok("Heart rate data received");
      } catch (Exception e) {
          return ResponseEntity.status(500).body("Data processing failed: " + e.getMessage());
      }
  }
  ```

### 2. Intelligent Sleep Strategy Engine

#### Functional Description
Generates personalized sleep recommendations based on multi-dimensional data (temperature, humidity, heart rate, noise).

#### Strategy Generation Logic
```java
public class StrategyService {
    /**
     * Generate comprehensive sleep strategy
     */
    public String generateStrategy(int temperature, int humidity, int heartRate, int noiseLevel) {
        StringBuilder strategy = new StringBuilder();
        
        // Temperature strategy
        if (temperature < 15) {
            strategy.append("Low temperature detected. Use electric blanket and wear warm sleepwear;");
        } else if (temperature > 26) {
            strategy.append("High temperature detected. Use fan/AC to maintain 22-24°C;");
        }
        
        // Humidity strategy
        if (humidity < 30) {
            strategy.append("Dry air detected. Use humidifier to maintain 40-60% humidity;");
        } else if (humidity > 70) {
            strategy.append("Humid air detected. Enable dehumidifier and reduce water sources;");
        }
        
        // Heart rate strategy
        if (heartRate > 90) {
            strategy.append("Elevated heart rate. Practice deep breathing for 10 minutes before sleep;");
        }
        
        // Noise strategy
        if (noiseLevel > 50) {
            strategy.append("High noise level. Use white noise or noise-canceling earplugs;");
        }
        
        return strategy.length() > 0 ? strategy.toString() : "Optimal sleep environment. Maintain regular schedule.";
    }
}
```

#### Interface
```
GET /getStrategy    # Get sleep strategy (requires auth token)
```

### 3. Soothing Music Resource Management

#### Functional Description
Provides curated sleep music library with song and artist information.

#### Music Resource List
| Song Title | Artist | Genre | Duration |
|------------|--------|-------|----------|
| 疑心病 (Suspicion) | 徐良 (Xu Liang) | Lyrical | 3:45 |
| 心似烟火 (Heart Like Fireworks) | 陈壹千 (Chen Yiqian) | Healing | 4:12 |
| 夏天的风 (Summer Wind) | 温岚 (Wen Lan) | Soothing | 4:28 |
| 海底 (Ocean Floor) | 一支榴莲 (Yi Zhi Liu Lian) | Calm | 3:56 |
| 东京不太热 (Tokyo Not So Hot) | 封茗囧菌 (Feng Ming Jiong Jun) | Gentle | 3:30 |
| 大雾 (Heavy Fog) | 张一乔 (Zhang Yiqiao) | Ethereal | 4:05 |
| 阿拉斯加海湾 (Gulf of Alaska) | 蓝心羽 (Lan Xin Yu) | Healing | 3:42 |

#### Core Interfaces
```
GET /getMusicList      # Get song list
GET /getMusicListTwo   # Get artist list
```

#### Implementation Code
```java
@Service
public class MusicService {
    
    /**
     * Read song list from resources
     */
    public List<String> getMusicList() {
        try {
            Resource resource = new ClassPathResource("listName.txt");
            return Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to read music list", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Read artist list from resources
     */
    public List<String> getSingerList() {
        try {
            Resource resource = new ClassPathResource("Listming.txt");
            return Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to read artist list", e);
            return Collections.emptyList();
        }
    }
}
```

### 4. Security Authentication Module

#### Functional Description
Token-based access control for protecting sensitive data and operations.

#### Authentication Flow
1. Obtain token: Via `/Token` interface with user credentials
2. Use token: Include `Author-token` in HTTP headers
3. Token validation: Interceptor verifies token validity

#### Core Code
```java
/**
 * Token Authentication Interceptor
 */
@Component
public class TokenAuthInterceptor implements HandlerInterceptor {
    private static final String VALID_TOKEN = "86146e3c320c71484befa19486d76981";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        // Open interfaces require no auth
        String path = request.getRequestURI();
        if ("/Token".equals(path) || "/addSensor".equals(path)) {
            return true;
        }
        
        // Validate token
        String token = request.getHeader("Author-token");
        if (token == null || !VALID_TOKEN.equals(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed: Invalid or expired token");
            return false;
        }
        
        return true;
    }
}
```

#### Interface
```
POST /Token    # Obtain access token
Request body: {"user":"X","name":"SDD"}
Response: 86146e3c320c71484befa19486d76981
```

---

## 💾 Data Model Design

### Core Entity Classes

#### 1. Sensor Data Entity (Sensor.java)
```java
@Entity
@Table(name = "sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;               // Primary Key
    
    private String temperature;       // Temperature (°C)
    
    private String humidity;          // Humidity (%)
    
    private Integer noise;            // Noise (dB)
    
    private Integer heartRate;        // Heart Rate (bpm)
    
    private LocalDateTime collectTime;// Collection Timestamp
    
    @Version
    private Long version;             // Optimistic Lock Version
}
```

#### 2. Data Access Layer (SensorRepository.java)
```java
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    // Query latest sensor data
    Optional<Sensor> findTopByOrderByCollectTimeDesc();
    
    // Query data by time range
    List<Sensor> findByCollectTimeBetween(LocalDateTime start, LocalDateTime end);
}
```

---

## 📊 Interface Documentation

| Endpoint | Method | Description | Parameters | Authentication |
|----------|--------|-------------|------------|----------------|
| `/Token` | POST | Obtain access token | `{"user":"X","name":"SDD"}` | None |
| `/addSensor` | POST | Receive ESP8266 data | `{"temperature":"24","humidity":"55","noise":"32"}` | None |
| `/addSensor32` | POST | Receive ESP32 heart rate | `{"rate":"750000"}` | None | (Raw value: 75 bpm)
| `/getStrategy` | GET | Get sleep strategy | - | Required |
| `/getMusicList` | GET | Get song list | - | Required |
| `/getMusicListTwo` | GET | Get artist list | - | Required |
| `/getLatestSensorData` | GET | Get latest sensor data | - | Required |
| `/getEtcdIP` | GET | Get Etcd cluster addresses | - | Required |

---

## 🛠️ Deployment Guide

### 1. K8s Cluster Deployment via VM Image

#### Prerequisites
- VM Software: VMware Workstation 16+ or VirtualBox 6+
- Hardware: Minimum 2GB RAM, 20GB free disk space
- Network: Bridged network environment

#### Deployment Steps

1. **Download VM Image**
   ```
   URL: https://example.com/sleep-service/etcd-cluster-vm
   Image Contains: 3-node K8s cluster (1 master, 2 workers), preconfigured Etcd cluster, Docker
   ```

2. **Import VMs**
   - Unzip downloaded file (`k8s-etcd-cluster-v1.24.7.zip`)
   - In VMware: "Open a Virtual Machine", import `.vmx` files
   - Repeat for 3 node VMs (master/worker1/worker2)

3. **Start Cluster**
   - Start master node first, then worker nodes
   - Default credentials: Username `k8s-admin`, Password `k8s@123`
   - Cluster auto-initializes (takes 2-3 minutes)

4. **Verify Cluster Status**
   ```bash
   # Login to master
   ssh k8s-admin@192.168.58.128
   
   # Check node status
   kubectl get nodes
   # Expected: 3 nodes in Ready state
   
   # Check Etcd health
   etcdctl --endpoints=http://192.168.58.128:2379,http://192.168.58.129:2379,http://192.168.58.130:2379 endpoint health
   ```

### 2. Kuboard Visual Interface Deployment

#### Deployment Steps
1. On master node:
   ```bash
   kubectl apply -f https://addons.kuboard.cn/kuboard/kuboard-v3.yaml
   ```

2. Verify deployment:
   ```bash
   kubectl get pods -n kuboard
   # Wait for kuboard-v3-xxxx pod to reach Running state
   ```

3. Access Kuboard:
   - Browser: `http://192.168.58.128:30000`
   - Default credentials: Username `admin`, Password `Kuboard123`

4. Import cluster:
   - Click "Add Cluster", name `sleep-service-cluster`
   - Auto-load kubeconfig, click "Import"

#### Kuboard Core Features
- Cluster Overview: Node resource usage, pod distribution, health monitoring
- Workload Management: Deploy, scale, upgrade Sleep Service
- Log Center: Real-time application logs & API call tracking
- Resource Monitoring: Visualized CPU, memory, network metrics

### 3. Sleep Service Application Deployment

#### Deployment Steps
1. Clone repository:
   ```bash
   git clone https://github.com/Ginger-Sanshui/sleep_service.git
   cd sleep_service/demo
   ```

2. Build Docker image:
   ```bash
   docker build -t sleep-service:v1.0 .
   docker tag sleep-service:v1.0 192.168.58.128:5000/sleep-service:v1.0
   docker push 192.168.58.128:5000/sleep-service:v1.0
   ```

3. Deploy to K8s:
   ```bash
   kubectl apply -f k8s/deployment.yaml
   kubectl apply -f k8s/service.yaml
   kubectl get pods
   ```

4. Verify service:
   ```bash
   kubectl get svc sleep-service
   curl http://192.168.58.128:30084/getMusicList
   ```

### 4. Mobile Application (Android)

#### Feature Overview
Primary user interface providing data visualization, strategy display, music control, and device management with offline caching.

#### Core Features
- **Real-time Dashboard**: Charts for temperature, humidity, noise, heart rate
- **Strategy Center**: Personalized recommendations with favorite function
- **Music Player**: Playback controls with sleep timer
- **Device Management**: Sensor status monitoring with alerts
- **Data Analytics**: Daily/weekly/monthly environment trend reports

#### Technical Implementation
- Architecture: MVVM with ViewModel+LiveData
- Networking: Retrofit+OkHttp for REST APIs with token management
- Local Storage: Room database, SharedPreferences
- Charts: MPAndroidChart for data visualization
- Background Services: Foreground service for music playback & data sync

#### Source Repository
```
https://github.com/Ginger-Sanshui/sleep_android
```

## 🔍 Operations & Monitoring

### Daily Operations Commands
```bash
# View application logs
kubectl logs -f deployment/sleep-service

# Restart application
kubectl rollout restart deployment sleep-service

# Scale replicas
kubectl scale deployment sleep-service --replicas=3

# Inspect Etcd data
etcdctl get / --prefix --keys-only

# Check DB connections
kubectl exec -it <mysql-pod> -- mysql -u root -pSensor@123 -e "show processlist"
```

### Troubleshooting Guide
1. **Kuboard Access Issues**
   ```bash
   kubectl get svc -n kuboard
   netstat -tpln | grep 30000
   ```

2. **Sensor Data Not Reporting**
   ```bash
   telnet 192.168.58.128 30084
   kubectl logs -f deployment/sleep-service | grep addSensor
   ```

3. **Etcd Cluster Issues**
   ```bash
   etcdctl member list
   etcdctl endpoint health --cluster
   ```

---

## 📜 License

Apache 2.0 License - See LICENSE file for details.

---

<div align="center">
  <p>✨ Let Technology Guard Your Quality Sleep ✨</p>
  <p>Project URL: <a href="https://github.com/Ginger-Sanshui/sleep_service">https://github.com/Ginger-Sanshui/sleep_service</a></p>
</div>




