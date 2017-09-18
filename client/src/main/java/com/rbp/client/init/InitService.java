package com.rbp.client.init;

import com.rbp.client.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by liuwenbin on 2017/8/29.
 */

@Service
public class InitService {

    @Value("${spring.zookeeper.rpb.node}")
    private String rpbNode;

    @Value("${spring.rbp.name}")
    private String rpbName;

    @Value("${spring.rbp.interface.host}")
    private String interfaceHost;

    @Value("${spring.rbp.name}")
    private String rbpName;

    @PostConstruct
    public void init(){
        new Thread(() -> {
            //上传信息
            Constant.ip = IpUtil.getIp();
            String teamviewerId = getTeamviewerId();
            String url = interfaceHost + "rbp/info?sn=" + rbpName
                    + "&tmid=" + teamviewerId
                    + "&time=" + System.currentTimeMillis()
                    + "&si=" + Constant.serverId
                    + "&ip=" + Constant.ip;
            LogUtil.log(url);
            WebUtil.get(url);

        }).start();
    }

    private String getTeamviewerId(){
        String id = "";
        try{
            List<String> list = ShellUtil.run("teamviewer info");
            for(String str : list){
                if(str.contains("TeamViewer ID:")){
                    id = str.split("TeamViewer ID:")[1].replaceAll("\\[0m","").trim();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return id;
    }
}
