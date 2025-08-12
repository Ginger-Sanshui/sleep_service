package com.example.demo.service;

import com.example.demo.entity.Sensor;
import com.example.demo.mapper.SensorMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SensorService {
    @Autowired
    private SensorMapper sensorRepository;

    @Retryable(
            value = { ObjectOptimisticLockingFailureException.class }, // 使用大括号包裹异常类型
            maxAttempts = 3,
            backoff = @Backoff(delay = 100))


            @Transactional
            public Sensor updateSensorWithRetry(Integer id, Sensor updatedSensor) {
            Sensor sensor = sensorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sensor not found: " + id));
            sensor.setRate(updatedSensor.getRate());
            return sensorRepository.save(sensor);
            }
}