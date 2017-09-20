package com.rbp.main.client.schedule;

import org.springframework.stereotype.Component;

/**
 * Created by liuwenbin on 2017/8/29.
 */

/**
 * 主客户端的定时任务
 */
@Component
public class ClientSchedule {

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
