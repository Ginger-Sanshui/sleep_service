package com.example.demo.controller;

import com.example.demo.entity.Sensor;
import com.example.demo.mapper.SensorMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;




@Service
public class MysqlController {

    @Autowired
    private SensorMapper sensorRepository;


    //插入数据
    @Transactional
    @Retryable(value = {org.hibernate.StaleObjectStateException.class}, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public Sensor addSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

      //查询数据
//    public List<Sensor> getAllSensors() {
//        return sensorRepository.findAll();
//    }
//
//    //删除数据
//    public void deleteSensor(Integer id) {
//        sensorRepository.deleteById(id);
//    }
    /*
    * 更新数据上传心率值
    * */
    @Update("UPDATE sensor SET rate = #{rate} WHERE id = #{id}")
    @Transactional
    public Sensor updateSensor(Integer id,Sensor sensor) {
        Sensor sensors = sensorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found: " + id));
        sensors.setRate(sensor.getRate());
        return sensorRepository.save(sensors);
    }
}