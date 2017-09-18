package com.rbp.server.schedule;

import com.google.gson.Gson;
import com.rbp.server.bean.IciDaily;
import com.rbp.server.bean.Weather;
import com.rbp.server.bean.WeatherBase;
import com.rbp.server.mongo.bean.DailyMessage;
import com.rbp.server.mongo.bean.Message;
import com.rbp.server.mongo.bean.RbpInfo;
import com.rbp.server.mongo.bean.WeatherMessage;
import com.rbp.server.mongo.service.*;
import com.rbp.server.util.Constant;
import com.rbp.server.util.EmailUtil;
import com.rbp.server.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by liuwenbin on 2017/8/31.
 */
@Component
public class ServerSchedule {

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

    @Autowired
    private DailyMessageService dailyMessageService;

    @Autowired
    private WeatherMessageService weatherMessageService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 删除多余的rbpStat
     */
    @Scheduled(cron = "0 0 0/3 * * ?}")
    public void getServerInfo(){
        long timestramp = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 2;
        System.out.println("toDel:" + timestramp);
        try{
            rbpStatService.delByTime(timestramp);
            fanStatService.delByTime(timestramp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 判断服务是否在线
     */
    @Scheduled(cron = "0 0/5 * * * ?}")
    public void getServerTime(){
        String emailContent = "";
        List<RbpInfo> rbpInfos = rbpInfoService.getAll();
        String serverName = null;
        long currentTime = System.currentTimeMillis();
        long serverTime = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String message = null;
        for(RbpInfo rbpInfo : rbpInfos){
            serverName = rbpInfo.getServerName();
            if(!Constant.serverTimeMap.containsKey(serverName)){
                emailContent += EmailUtil.redAndBlockHead() + serverName + EmailUtil.redAndBlockFoot() + "没有时间信息上传,请检查. <br>";
                messageService.setMessage(serverName + "没有时间信息上传,请检查");
                continue;
            }
            serverTime = Constant.serverTimeMap.get(serverName);
            System.out.println(serverName+":"+serverTime);
            if(currentTime - serverTime > 1000 * 60 * 5){
                emailContent += EmailUtil.redAndBlockHead() + serverName + EmailUtil.redAndBlockFoot() + "时间上报出现异常,最后上报时间为:"+dateFormat.format(serverTime)+". <br>";
                messageService.setMessage(serverName + "时间上报出现异常");
            }
        }
        if(!"".equals(emailContent)){
            emailService.sendHtmlMail(toUser,"【树莓派集群报警】时间上报异常报警",emailContent);
        }
    }


    /**
     * 发送消息
     */
    @Scheduled(cron = "0 0 22 * * ?}")
    public void sendDailyMessage(){
        try{
            String content = WebUtil.get("http://sentence.iciba.com/index.php?c=dailysentence&m=getTodaySentence&_=1505130508541");
            IciDaily iciDaily = new Gson().fromJson(content,IciDaily.class);
            String message = "每日一句| |";
            message += iciDaily.getContent();
            message += "| |";
            message += iciDaily.getNote();
            System.out.println(message);
            try{
                messageService.setImgMessage(iciDaily.getPicture());
            }catch(Exception e){
                e.printStackTrace();
            }
            messageService.setMessage(message);
            //保存到数据库中
            DailyMessage dailyMessage = new DailyMessage();
            dailyMessage.setContent(iciDaily.getContent());
            dailyMessage.setNote(iciDaily.getNote());
            dailyMessage.setPicture(iciDaily.getPicture());
            dailyMessage.setTimestramp(System.currentTimeMillis());
            dailyMessage.setDateTime(dateFormat.format(System.currentTimeMillis()));
            dailyMessageService.insert(dailyMessage);
        }catch(Exception e){
            messageService.setMessage("获取每日一句失败,"+e.getMessage());
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 7 * * ?}")
    public void sendWeatherMessage(){
        try{
            String content = WebUtil.get("http://www.nmc.cn/f/rest/real/54511");
            Weather weather = new Gson().fromJson(content,WeatherBase.class).getWeather();
            weather.setImg("http://image.nmc.cn/static2/site/nmc/themes/basic/weather/white/day/"+weather.getImg()+".png");
            String message = "天气| |"+weather.getInfo()+"|温度: " + weather.getTemperature()+"|湿度: "+weather.getHumidity()+"|";
            message += "降水: " + weather.getRain();
            System.out.println(message);
            try{
                messageService.setImgMessage(weather.getImg());
            }catch(Exception e){
                e.printStackTrace();
            }
            messageService.setMessage(message);
            //保存到数据库中
            WeatherMessage weatherMessage = new WeatherMessage();
            weatherMessage.setImg(weather.getImg());
            weatherMessage.setHumidity(weather.getHumidity());
            weatherMessage.setInfo(weather.getInfo());
            weatherMessage.setRain(weather.getRain());
            weatherMessage.setTemperature(weather.getTemperature());
            weatherMessage.setTimestramp(System.currentTimeMillis());
            weatherMessage.setDateTime(dateFormat.format(System.currentTimeMillis()));
            weatherMessageService.insert(weatherMessage);
        }catch(Exception e){
            messageService.setMessage("获取天气预报失败,"+e.getMessage());
            e.printStackTrace();
        }
    }
}

