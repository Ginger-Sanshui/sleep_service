package com.example.demo.controller;


import com.example.demo.entity.Sensor;
import com.example.demo.entity.SensorRequest;
import com.example.demo.entity.TokenRequest;
import com.example.demo.service.EtcdService;
import com.example.demo.service.FileService;
import com.example.demo.service.ServiceDiscovery;
import com.example.demo.strategy.Strategy;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class controller {

    FileService FileService = new FileService();
    @Autowired
    private EtcdService etcdService;

    @Autowired
    private MysqlController mysqlController;

    @Lazy
    @Autowired
    private ServiceDiscovery serviceDiscovery;

    //获取音乐列表
    @GetMapping("/getMusicText")
    public String hello(String name) {
        if (name == null) {
            return "null";
        }
        return switch (name) {
            case "OnePage" -> "白山茶";
            case "OnePageTwo" -> "陈雪凝";
            case "TextZ1" -> "坏女孩";
            case "TextZ2" -> "徐良";
            case "TextX1" -> "与我无关";
            case "TextX2" -> "阿冗";
            default -> "error";
        };
    }

    //手机端同步资源表
    @GetMapping("/getMusicList")
    public List<String> getMusicList() {
        List<String> strings = new ArrayList<>();
        strings = FileService.readFileToList("classpath:listName.txt");
        return strings;
    }

    //手机端同步资源表
    @GetMapping("/getMusicListTwo")
    public List<String> getMusicListTwo() {
        List<String> strings = new ArrayList<>();
        strings = FileService.readFileToList("classpath:listMing.txt");
        return strings;
    }


    Long version = 1L;
    //存储esp8266传输内容至etcd集群
    @PostMapping(value = "/addSensor",
            produces = "application/json;charset=UTF-8",
            consumes = MediaType.APPLICATION_JSON_VALUE)  // 明确指定接收 JSON
    public void putSensor(@RequestBody SensorRequest request) throws Exception {
        String temperature = request.getTemperature();
        String humidity = request.getHumidity();
        String noise = request.getNoise();
        String clock = request.getClock();
        etcdService.putValue("temperature",temperature);
        etcdService.putValue("humidity",humidity);
        etcdService.putValue("noise",noise);
        etcdService.putValue("clock",clock);
        Sensor sensor = new Sensor();
        sensor.setTemperature(temperature);
        sensor.setHumidity(humidity);
        sensor.setNoise(Integer.parseInt(noise));
        mysqlController.addSensor(sensor);
    }
    int ida = 1;
    //存储esp8266传输内容至etcd集群
    @PostMapping(value = "/addSensor32",
            produces = "application/json;charset=UTF-8",
            consumes = MediaType.APPLICATION_JSON_VALUE)  // 明确指定接收 JSON
    public void putSensor32(@RequestBody SensorRequest request) throws Exception {
        String rate = request.getRate();
        int rates = Integer.parseInt(rate)/10000;
        etcdService.putValue("rate", String.valueOf(rates));
        Sensor sensor = new Sensor();
        sensor.setRate(Integer.parseInt(rate));
        mysqlController.updateSensor(ida,sensor);
        ida++;
    }

    //获取etcd集群中的值
    @GetMapping("/getSensor")
    public String getSensor(@RequestParam String key) throws Exception {
        return etcdService.getValue(key);
    }

    //获取etcd集群中的值
    @GetMapping("/getEtcdIP")
    public List<String> getEtcdIP(){
        System.out.println(serviceDiscovery.getInstances());
        return serviceDiscovery.getInstances();
    }

    /*
    * 获取令牌
    * */
    @PostMapping("/Token")
    public String login(@RequestBody TokenRequest request) {
        //验证用户名密码
        if (!"X".equals(request.getUser()) || !"SDD".equals(request.getName())) {
            return "令牌获取失败";
        }
        return "86146e3c320c71484befa19486d76981";
    }

    /*
    * 根据数据分析
    * 给出相对应的策略
    * */
    @GetMapping("/getStrategy")
    public String getStrategy() throws Exception {
        Strategy strategy = new Strategy();
        int temp = Integer.parseInt(etcdService.getValue("temperature"));
        int humidity = Integer.parseInt(etcdService.getValue("humidity"));
        int rate = Integer.parseInt(etcdService.getValue("rate"));
        int noise = Integer.parseInt(etcdService.getValue("noise"));
        return strategy.strategy(temp,humidity,rate,noise);
    }


}
