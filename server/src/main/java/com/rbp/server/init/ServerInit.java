package com.rbp.server.init;

import com.rbp.server.mongo.service.EmailService;
import com.rbp.server.service.ZookeeperService;
import com.rbp.server.util.Constant;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by liuwenbin on 2017/9/5.
 */
@Service
public class ServerInit {

    @Value("${spring.fan.config}")
    private String configStr;

    @Value("${spring.zookeeper.url}")
    private String zookeeperUrl;

    @Value("${spring.zookeeper.rpb.node}")
    private String baseNode;

    @Autowired
    private ZookeeperService zookeeperService;

    @PostConstruct
    public void init(){
        String[] configs = configStr.split(";");
        for(String config : configs){
            String[] conf = config.split(":");
            Constant.fanConfigMap.put(conf[0],conf[1]);
        }

        try{
            zookeeperService.getConnnection(zookeeperUrl);
            Stat stat = zookeeperService.getZnodeStat(baseNode);
            if(null == stat){
                zookeeperService.createNewZnode(baseNode,"","PERSISTENT");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
