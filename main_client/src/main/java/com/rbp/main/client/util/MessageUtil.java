package com.rbp.main.client.util;

import com.rbp.main.client.bean.Message;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;

/**
 * Created by liuwenbin on 2017/9/17.
 */
public class MessageUtil {

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void send(Message message){
        //发送文本消息
        if(message.getType().equals("message")){
            try{
                QQUtil.sendMessage(URLDecoder.decode(message.getContent(),"UTF-8")+"|消息时间: " + dateTimeFormat.format(message.getTimestramp()));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //发送图片消息
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
        //请求url
        if(message.getType().equals("url")){
            try{
                WebUtil.get(URLDecoder.decode(message.getContent(),"UTF-8"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
