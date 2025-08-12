package com.example.demo.config;

import io.etcd.jetcd.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EtcdConfig {

    /*
    * 配置etcd
    * 获取集群接口网址，进行连接
    * */
    @Value("${etcd.endpoints}")
    private String[] etcdEndpoints;

    @Bean
    public Client etcdClient() {
        return Client.builder()
                .endpoints(etcdEndpoints)
                .build();
    }
}