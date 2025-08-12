package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;


/*
* 数据库格式
* */
@Entity
@Table(name = "sensor")
@Data
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String temperature;
    private String humidity;
    private int rate;
    private int noise;
    @Version
    private Long version;
}