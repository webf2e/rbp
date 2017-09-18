package com.rbp.main.client.init;

import com.rbp.main.client.service.WatcherService;
import com.rbp.main.client.service.ZookeeperService;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by liuwenbin on 2017/8/29.
 */

@Service
public class InitService {

    @Autowired
    private ZookeeperService zookeeperService;

    @Autowired
    private WatcherService watcherService;

    @Value("${spring.zookeeper.url}")
    private String zookeeperUrl;

    @Value("${spring.zookeeper.rpb.node}")
    private String baseNode;

    @PostConstruct
    public void init() {
        try{
            zookeeperService.getConnnection(zookeeperUrl);
            Stat stat = zookeeperService.getZnodeStat(baseNode);
            if(null == stat){
                zookeeperService.createNewZnode(baseNode,"","PERSISTENT");
            }
            watcherService.watchChild(baseNode);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
