package com.rbp.main.client.util;

import com.google.gson.Gson;
import com.rbp.main.client.bean.Message;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;

/**
 * Created by liuwenbin on 2017/9/17.
 */
public class MessageUtil {

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void send(Message message){
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
}
