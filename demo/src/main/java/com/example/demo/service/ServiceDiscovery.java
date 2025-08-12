package com.example.demo.service;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.Lease;
import io.etcd.jetcd.options.PutOption;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ServiceDiscovery {


    @Lazy
    @Autowired
    private Client etcdClient;

    @Lazy
    @Autowired
    private EtcdService etcdService;



    private static List<String> Instances = new ArrayList<>();
    private Lease lease;
    private long leaseId;
    private String serviceKey;

    //instanceAddress = "192.168.58.128:8080"
    public void register(String etcd,String instanceAddress) throws Exception {
        //申请租约
        lease = etcdClient.getLeaseClient();
        leaseId = lease.grant(30).get().getID();

        /*
        * 注册服务实例（key格式：/etcd/{etcd1}/{instance-id}）
        * 注册信息至etcd通过etcd集群服务，获取getServiceInstance
        * 获取ip和port
        * */
        serviceKey = "/etcd/" + etcd +"/" + "17254";

        etcdClient.getKVClient().put(
                ByteSequence.from(serviceKey, StandardCharsets.UTF_8),
                ByteSequence.from(instanceAddress, StandardCharsets.UTF_8),
                PutOption.newBuilder().withLeaseId(leaseId).build()
        ).get();

        //启动续约线程（每25秒续约一次）
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                lease.keepAliveOnce(leaseId);
            } catch (Exception e) {
                // 处理续约失败删除注册
                unregister();
            }
        }, 25, 25, TimeUnit.SECONDS);
    }

    /*
    * 取消注册服务
    * 通过对已经过期的租约进行删除注册
    * 表示服务端已超载无法提供正常服务
    * */
    @PreDestroy
    public void unregister() {
        //服务关闭时删除Key
        etcdClient.getKVClient().delete(ByteSequence.from(serviceKey, StandardCharsets.UTF_8));
    }


    @PostConstruct
    public void init(){

        /* 2025-4-28
         * 初始化服务，etcd集群信息注册
         * 并获取etcd服务地址
         * */
        try {
            register("etcd1","192.168.58.128:2379");
            register("etcd2","192.168.58.129:2379");
            register("etcd3","192.168.58.130:2379");
            //初始获取所有etcd集群列表
            InitInstances();

            //监听服务变化
            watchService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


        /*
         * 获取可用的etcd接口
         * */
    public List<String> getInstances() {
        return Instances;
    }

    private void watchService() {
    }

        /* 2025-4-28
        * 通过对租约注册信息的循环便利
        * 实现服务发现功能
        * */
        private void InitInstances() {
            try {
                for (int a=1;a<4;a++){
                    String serviceValue= etcdService.getValue("/etcd/etcd"+a+"/17254");
                    System.out.println("发现etcd服务集群地址:"+serviceValue);
                    Instances.add(serviceValue);
                }
            } catch (Exception ignored) {

            }
        }

}

