package com.rbp.server.controller;

import com.rbp.server.mongo.bean.FanStat;
import com.rbp.server.mongo.bean.RbpInfo;
import com.rbp.server.mongo.bean.RbpStat;
import com.rbp.server.mongo.service.*;
import com.rbp.server.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuwenbin on 2017/8/31.
 */
@RestController
@RequestMapping(value = "/rbp")
public class RbpController {

    @Autowired
    private RbpStatService rbpStatService;

    @Autowired
    private RbpInfoService rbpInfoService;

    @Autowired
    private FanStatService fanStatService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageService messageService;

    @Value("${spring.mail.to.user}")
    private String toUser;

    @Value("${spring.fan.open.temp}")
    private double fanTemp;

    @RequestMapping(value = "/uploadStat")
    public String uploadStat(HttpServletRequest request, HttpServletResponse response){
        try{
            Date date = new Date();
            date.setSeconds(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String serverName = request.getParameter("sn");
            String stat = request.getParameter("s");
            stat = URLDecoder.decode(stat);
            stat = stat.replaceAll("G","").replaceAll("%","");
            RbpStat rbpStat = new RbpStat();
            rbpStat.setTimestramp(date.getTime());
            rbpStat.setDateTime(format.format(date));
            rbpStat.setServerName(serverName);
            //设置状态
            String[] stats = stat.split("\\|");
            rbpStat.setCpuTemp(stats[0]);
            rbpStat.setCpuUse(stats[1]);
            rbpStat.setGpuTemp(stats[2]);
            rbpStat.setMemoryTotal(stats[3]);
            rbpStat.setMemoryUsed(stats[4]);
            rbpStat.setMemoryFree(stats[5]);
            rbpStat.setMemoryAvail(stats[6]);
            rbpStat.setDiskTotal(stats[7]);
            rbpStat.setDiskUsed(stats[8]);
            rbpStat.setDiskUsedPercent(stats[9]);
            try{
                //获取系统负载
                if(stats.length > 10){
                    String uptime = stats[10];
                    String[] ups = uptime.split(",");
                    rbpStat.setLoadAverage1(ups[0].trim());
                    rbpStat.setLoadAverage5(ups[1].trim());
                    rbpStat.setLoadAverage15(ups[2].trim());
                    //获取系统网络状态
                    rbpStat.setNetIn(stats[11]);
                    rbpStat.setNetOut(stats[12]);
                }
            }catch(Exception e){

            }
            //保存
            rbpStatService.insert(rbpStat);
            //添加系统更新时间
            Constant.serverTimeMap.put(serverName,date.getTime());
            //判断风扇状态
            if(Double.parseDouble(rbpStat.getCpuTemp()) > fanTemp){
                Constant.fanStatMap.put(serverName,1);
            }else{
                Constant.fanStatMap.put(serverName,0);
            }
            if(Constant.fanStatMap.size() == 5){
                //判断是否开启风扇
                Set<String> fanStats = Constant.fanStatMap.keySet();
                Map<String,Integer> fanToOpenCount = new HashMap<>();
                Map<String,Integer> fanToOpenTongjiCount = new HashMap<>();
                String fanIP = "";
                int value = 0;
                String fanRasName = null;
                String fanName = null;
                for(String fanStat : fanStats){
                    value = Constant.fanStatMap.get(fanStat);
                    fanRasName = Constant.fanConfigMap.get(fanStat);
                    fanName = fanRasName.replaceAll("ras","fan");
                    fanIP = rbpInfoService.getByServerName(fanRasName).getIp();
                    if(fanToOpenCount.containsKey(fanIP)){
                        fanToOpenCount.put(fanIP,fanToOpenCount.get(fanIP) + value);
                        fanToOpenTongjiCount.put(fanName,fanToOpenTongjiCount.get(fanName) + value);
                    }else{
                        fanToOpenCount.put(fanIP,value);
                        fanToOpenTongjiCount.put(fanName,value);
                    }
                }
                Constant.fanStatMap.clear();
                String result = "";
                Set<String> fanOpenSet = fanToOpenCount.keySet();
                for(String fanOpen : fanOpenSet){
                    result += fanOpen+":"+fanToOpenCount.get(fanOpen)+";";
                }
                //添加统计
                fanOpenSet = fanToOpenTongjiCount.keySet();
                FanStat fanStat = null;
                for(String fanOpen : fanOpenSet){
                    fanStat = new FanStat();
                    fanStat.setFanName(fanOpen);
                    fanStat.setState(fanToOpenTongjiCount.get(fanOpen));
                    fanStat.setTimestramp(date.getTime());
                    fanStat.setDateTime(format.format(date));
                    fanStatService.insert(fanStat);
                    if(fanStat.getState() > 0){
                        messageService.setMessage(fanOpen + "风扇自动开启");
                    }
                }
                return result;
            }
            return "success";
        }catch(Exception e){
            return e.getMessage();
        }

    }

    @RequestMapping(value = "/info")
    public String info(HttpServletRequest request,HttpServletResponse response){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String serverName = request.getParameter("sn");
            String tmid = request.getParameter("tmid");
            Long time = Long.parseLong(request.getParameter("time"));
            String ip = request.getParameter("ip");
            RbpInfo rbpInfo = rbpInfoService.getByServerName(serverName);
            if(null == rbpInfo){
                rbpInfo = new RbpInfo();
            }
            rbpInfo.setServerName(serverName);
            rbpInfo.setIp(ip);
            rbpInfo.setServerId(Integer.parseInt(serverName.replaceAll("ras","")));
            rbpInfo.setStartUpTime(format.format(time));
            rbpInfo.setTeamviewerId(tmid);
            rbpInfo.setUpdateTime(format.format(System.currentTimeMillis()));
            rbpInfoService.update(rbpInfo);
            return "success";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/ip")
    public String ip(HttpServletRequest request,HttpServletResponse response){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String serverName = request.getParameter("sn");
            String ip = request.getParameter("ip");
            RbpInfo rbpInfo = rbpInfoService.getByServerName(serverName);
            if(null == rbpInfo){
                return "no service named " + serverName;
            }
            String originIp = rbpInfo.getIp();
            if(originIp.equals(ip)){
                return "IP not changed in " + serverName;
            }
            rbpInfo.setIp(ip);
            rbpInfo.setUpdateTime(format.format(System.currentTimeMillis()));
            rbpInfoService.update(rbpInfo);
            //发送邮件
            String content = serverName + "的IP从 "+originIp+" 变更为 " + ip;
            emailService.send(toUser,"IP变更通知",content);
            messageService.setMessage(content);
            return "success";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/uptime")
    public String uptime(HttpServletRequest request,HttpServletResponse response){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String serverName = request.getParameter("sn");
            String uptime = request.getParameter("uptime");
            RbpInfo rbpInfo = rbpInfoService.getByServerName(serverName);
            if(null == rbpInfo){
                return "no service named " + serverName;
            }
            rbpInfo.setUptime(URLDecoder.decode(uptime));
            rbpInfo.setUptimeChangedTime(format.format(System.currentTimeMillis()));
            rbpInfoService.update(rbpInfo);
            return "success";
        }catch(Exception e){
            return e.getMessage();
        }
    }


    @RequestMapping(value = "/sendMessage")
    public String sendMessage(HttpServletRequest request,HttpServletResponse response){
        try{
            String message = request.getParameter("m");
            String type = request.getParameter("t");
            if(null == message){
                return  "message is null";
            }
            if(null == type || "".equals(type)){
                type = "message";
            }
            message = URLDecoder.decode(message);
            System.out.println(message);
            if(type.equals("message")){
                messageService.setMessage(message);
            }
            if(type.equals("image")){
                messageService.setImgMessage(message);
            }
            return  "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
