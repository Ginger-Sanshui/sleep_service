package com.example.demo.mapper;

import com.example.demo.entity.Sensor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

@Mapper
public interface SensorMapper extends JpaRepository<Sensor, Integer> {}

