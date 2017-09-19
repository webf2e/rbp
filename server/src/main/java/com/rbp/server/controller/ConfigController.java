package com.rbp.server.controller;

import com.rbp.server.mongo.bean.RbpInfo;
import com.rbp.server.mongo.service.MessageService;
import com.rbp.server.mongo.service.RbpInfoService;
import com.rbp.server.util.Constant;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by liuwenbin on 2017/9/10.
 */
@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private RbpInfoService rbpInfoService;

    @RequestMapping(value = "/allFans")
    public List<String> getAllFans(HttpServletRequest request){
        if(null == request.getSession().getAttribute("login")){
            return null;
        }
        List<String> fans = new ArrayList<>();
        Set<String> fanMapKey = Constant.fanConfigMap.keySet();
        String fanName = null;
        for(String ras : fanMapKey){
            fanName = Constant.fanConfigMap.get(ras);
            fanName = fanName.replaceAll("ras","fan");
            if(!fans.contains(fanName)){
                fans.add(fanName);
            }
        }
        Collections.sort(fans);
        return fans;
    }

    @RequestMapping(value = "/operateFans")
    public String operateFans(javax.servlet.http.HttpServletRequest request, HttpServletResponse response){
        if(null == request.getSession().getAttribute("login")){
            return null;
        }
        try{
            String fanName = request.getParameter("fanName");
            String state = request.getParameter("state");
            if("start".equals(state)){
                Constant.startFan = true;
            }else{
                Constant.startFan = false;
            }
            if("all".equals(fanName)){
                String[] names = {"ras2","ras3","ras4","ras5"};
                for(String name : names){
                    RbpInfo rbpInfo = rbpInfoService.getByServerName(name);
                    String url = "http://"+rbpInfo.getIp()+":8082/fan/operate?state="+state;
                    messageService.setUrlMessage(url);
                }
            }else{
                fanName = fanName.replaceAll("fan","ras");
                RbpInfo rbpInfo = rbpInfoService.getByServerName(fanName);
                if(null == rbpInfo){
                    return "no server found";
                }
                String url = "http://"+rbpInfo.getIp()+":8082/fan/operate?state="+state;
                messageService.setUrlMessage(url);
            }

            return  "OK";
        }catch(Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
