package com.rbp.main.client.schedule;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rbp.main.client.bean.Message;
import com.rbp.main.client.util.Constant;
import com.rbp.main.client.util.DownLoadImg;
import com.rbp.main.client.util.QQUtil;
import com.rbp.main.client.util.WebUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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

    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 判断有无需要执行的事件
     */
    /*@Scheduled(cron = "0/1 * * * * ?}")
    public void getServerInfo(){
        try {
            String content = WebUtil.get("http://47.90.32.49:8083/message/get");
            content = content.replaceAll("Success: ","").trim();
            if("null".equals(content) || "".equals(content)){
                return;
            }
            List<Message> messages = new Gson().fromJson(content, new TypeToken<List<Message>>() {}.getType());
            for (Message message : messages){
                if(message.getType().equals("message")){
                    try{
                        QQUtil.sendMessage(URLDecoder.decode(message.getContent(),"UTF-8")+"|消息时间: " + dateTimeFormat.format(message.getTimestramp()));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                if(message.getType().equals("image")){
                    try{
                        //先下载图片
                        String imgUrl = message.getContent();
                        //下载
                        String filePath = Constant.imgFilePath+imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                        DownLoadImg.down(imgUrl,filePath);
                        QQUtil.sendImgMessage(filePath);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
