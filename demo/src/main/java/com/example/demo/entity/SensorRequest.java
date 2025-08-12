package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorRequest {
    private String temperature;
    private String humidity;
    private String rate;
    private String noise;
    private String clock;
}