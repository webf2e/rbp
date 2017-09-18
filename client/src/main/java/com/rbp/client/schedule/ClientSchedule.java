package com.rbp.client.schedule;

import com.rbp.client.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liuwenbin on 2017/8/29.
 */
@Component
public class ClientSchedule {

    @Value("${spring.rbp.name}")
    private String rbpName;

    @Value("${spring.proj.path}")
    private String projectPath;

    @Value("${spring.rbp.interface.host}")
    private String interfaceHost;

    @Value("${spring.rbp.bak.savePath}")
    private String bakSavePath;

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    /**
     * 获取系统状态
     */
    @Scheduled(cron = "0 0/1 * * * ?}")
    public void getServerInfo(){
        try{
            LogUtil.log("开始获取系统状态");
            List<String> lines = ShellUtil.run("sh "+projectPath+"bin/pistat_java.sh");
            String result = lines.get(0);
            //String result = "62.3|1.8|62.3|949.0|551.0|76.0|329.0|30G|4.5G|16%";
            result = result.replaceAll("%","");
            //获取loadaverage
            try{
                String uptime = ShellUtil.run("uptime").get(0);
                uptime = uptime.split("average:")[1].trim();
                LogUtil.log(uptime);
                result += "|" + uptime;
            }catch(Exception e){
                e.printStackTrace();
            }
            //获取网络状态
            try{
                List<String> shells = ShellUtil.run("ifstat -T 1 1");
                String net = shells.get(2);
                LogUtil.log(net);
                String[] netInfos = net.split("\\s+");
                result += "|" + netInfos[4] + "|" + netInfos[5];
            }catch(Exception e){
                e.printStackTrace();
            }
            LogUtil.log(result);
            String url = interfaceHost + "rbp/uploadStat?sn=" + rbpName + "&s=" + URLEncoder.encode(result);
            LogUtil.log(url);
            String webResult = WebUtil.get(url);
            LogUtil.log(webResult);
            LogUtil.log("获取系统状态结束");
            //根据结果判断是否要打开风扇
            if(webResult.contains("Success: ")){
                webResult = webResult.replaceAll("Success: ","");
                if(webResult.contains("success")){
                    return;
                }
                String[] everyPans = webResult.split(";");
                String state = null;
                for(String pan : everyPans){
                    String[] stat = pan.split(":");
                    if(Integer.parseInt(stat[1]) > 0){
                        state = "start";
                    }else{
                        state = "stop";
                    }
                    String panUrl = "http://" + stat[0] + ":8082/fan/operate?state="+state;
                    LogUtil.log(panUrl);
                    webResult = WebUtil.get(panUrl);
                    LogUtil.log("fan_result: " + webResult);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取ip,如果修改发送给服务器
     */
    @Scheduled(cron = "0 0/10 * * * ?}")
    public void getIp(){
        try{
            String currentIp = IpUtil.getIp();
            LogUtil.log("Constant ip : " + Constant.ip);
            LogUtil.log("current ip : " + currentIp);
            if(!currentIp.equals(Constant.ip)){
                //发送给服务器端
                Constant.ip = currentIp;
                String url = interfaceHost + "rbp/ip?sn=" + rbpName + "&ip=" + Constant.ip;
                WebUtil.get(url);
            }
            //获取upTime
            LogUtil.log("开始获取服务器的uptime");
            try{
                String upTime = ShellUtil.run("uptime").get(0);
                upTime = upTime.split("up ")[1];
                String[] ups = upTime.split(",");
                upTime = ups[0].trim();
                if(!ups[1].contains("user")){
                    upTime += " " + ups[1].trim();
                }
                //发送到服务器端
                LogUtil.log(upTime);
                String url = interfaceHost + "rbp/uptime?sn=" + rbpName + "&uptime=" + URLEncoder.encode(upTime);
                LogUtil.log(url);
                WebUtil.get(url);
            }catch(Exception e){
                e.printStackTrace();
            }
            LogUtil.log("获取服务器的uptime结束");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 备份SD
     */
    @Scheduled(cron = "0 0 0 * * ?}")
    public void backUpSDCard(){
        //备份SD
        Date date = new Date();
        //判断今天是否需要执行备份
        int serverIndex = Integer.parseInt(rbpName.replaceAll("ras",""));
        LogUtil.log("date.getDay():"+date.getDay());
        if(date.getDay() != serverIndex){
            return;
        }
        //先判断 das 路径是否被挂载
        if(!NasUtil.isExistNasPath()){
            NasUtil.makeNasLink();
        }
        //获取文件名称
        String ofName = rbpName + "_" + format.format(date)+".img.gz";
        String shell = "sh /root/java_proj/bin/backup.sh " + bakSavePath + ofName;
        LogUtil.log(shell);
        long startTime = System.currentTimeMillis();
        try{
            List<String> strs = ShellUtil.run(shell);
            for(String str : strs){
                LogUtil.log(str);
            }
        }catch(Exception e){
            LogUtil.log(rbpName + "备份失败:"+e.getMessage());
        }
        LogUtil.log("备份耗时: " + (System.currentTimeMillis() - startTime));
    }

}
