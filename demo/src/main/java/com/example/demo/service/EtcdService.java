package com.example.demo.service;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.watch.WatchEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EtcdService {

    @Lazy
    @Autowired
    private Client etcdClient;

    /*
    * 获取etcd存入的数值
    * */
    public String getValue(String key) throws Exception {
        //实例化连接，调用连接实现获取键值，格式为utf-8
        KV kvClient = etcdClient.getKVClient();
        ByteSequence keyBs = ByteSequence.from(key, StandardCharsets.UTF_8);
        GetResponse response = kvClient.get(keyBs).get();
        if (response.getKvs().isEmpty()) {
            return null;
        }
        return response.getKvs().get(0).getValue().toString(StandardCharsets.UTF_8);
    }

    /*
     * 上传数据至etcd进行存储
     * */
    public void putValue(String key, String value) throws Exception {
        KV kvClient = etcdClient.getKVClient();
        ByteSequence keyBs = ByteSequence.from(key, StandardCharsets.UTF_8);
        ByteSequence valueBs = ByteSequence.from(value, StandardCharsets.UTF_8);
        kvClient.put(keyBs, valueBs).get();
    }

    /*
    * 通过watchClient监听键值的更改
    * 通过该链接可以将更改的数据值
    * 打印至控制台
    * */
    public void watchKey(String key) {
        Watch watchClient = etcdClient.getWatchClient();
        ByteSequence keyBs = ByteSequence.from(key, StandardCharsets.UTF_8);
        watchClient.watch(keyBs, response -> {
            for (WatchEvent event : response.getEvents()) {
                System.out.println("Type: " + event.getEventType());
                System.out.println("Key: " + event.getKeyValue().getKey().toString(StandardCharsets.UTF_8));
                System.out.println("Value: " + event.getKeyValue().getValue().toString(StandardCharsets.UTF_8));
            }
        });
    }
}
