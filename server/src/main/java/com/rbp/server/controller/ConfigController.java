package com.rbp.server.controller;

import com.rbp.server.util.Constant;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
