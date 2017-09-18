package com.rbp.server.controller;

import com.rbp.server.mongo.bean.FanStat;
import com.rbp.server.mongo.bean.RbpInfo;
import com.rbp.server.mongo.bean.RbpStat;
import com.rbp.server.mongo.service.FanStatService;
import com.rbp.server.mongo.service.RbpInfoService;
import com.rbp.server.mongo.service.RbpStatService;
import com.rbp.server.util.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liuwenbin on 2017/8/31.
 */
@RestController
@RequestMapping(value = "/page")
public class PageController {

    @Autowired
    private RbpInfoService rbpInfoService;

    @Autowired
    private RbpStatService rbpStatService;

    @Autowired
    private FanStatService fanStatService;

    @Value("${spring.rbp.stat.count}")
    private int statCount;

    @RequestMapping(value = "/info")
    public List<RbpInfo> getInfo(){
        return rbpInfoService.getAll();
    }

    @RequestMapping(value = "/compareServers")
    public List<RbpStat> compareServers(){
        List<RbpInfo> rbpInfos = rbpInfoService.getAll();
        List<RbpStat> rbpStats = new ArrayList<>();
        for(RbpInfo rbpInfo : rbpInfos){
            try{
                rbpStats.add(rbpStatService.getLast(rbpInfo.getServerName()));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return rbpStats;
    }

    @RequestMapping(value = "/fan")
    public List<List<FanStat>> fanState(){
        String[] fansName = {"fan2","fan3","fan4","fan5"};
        List<List<FanStat>> fanStats = new ArrayList<>();
        for(String fanName : fansName){
            try{
                List<FanStat> fanStatList = fanStatService.getLastStat(fanName,statCount);
                Collections.sort(fanStatList);
                fanStats.add(fanStatList);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return fanStats;
    }

    @RequestMapping(value = "/stat")
    public List<RbpStat> getServerStat(HttpServletRequest request){
        String serverName = request.getParameter("sn");
        List<RbpStat> rbpStats = rbpStatService.getLastStat(serverName,statCount);
        Collections.sort(rbpStats);
        return rbpStats;
    }

    @RequestMapping(value = "/servers")
    public List<RbpInfo> servers(HttpServletRequest request){
        return rbpInfoService.getAll();
    }

    @RequestMapping(value = "/compare")
    public List<List<RbpStat>> compare(HttpServletRequest request){
        List<List<RbpStat>> rbpStats = new ArrayList<>();
        List<RbpInfo> rbpInfos = rbpInfoService.getAll();
        for (RbpInfo rbpInfo : rbpInfos){
            List<RbpStat> rbps = rbpStatService.getLastStat(rbpInfo.getServerName(),statCount);
            Collections.sort(rbps);
            rbpStats.add(rbps);
        }
        return rbpStats;
    }

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request){
        String code = request.getParameter("code");
        if(null == code || "".equals(code)){
            return "error";
        }
        try{
            Integer.parseInt(code);
        }catch(Exception e){
            return "error";
        }
        if(GoogleAuthenticator.isRightCode(code)){
            request.getSession().setAttribute("login","true");
            return "success";
        }
        return "error";
    }
}