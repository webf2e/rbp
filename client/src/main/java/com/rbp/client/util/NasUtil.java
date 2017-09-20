package com.rbp.client.util;

import java.util.List;

/**
 * nas操作类
 */
public class NasUtil {

    /**
     * 判断nas路径是否存在
     * @return
     */
    public static boolean isExistNasPath(){
        try{
            List<String> strings = ShellUtil.run("df -h");
            for(String str : strings){
                if(str.contains("192.168.1.6")){
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建nas链接
     */
    public static void makeNasLink(){
        try{
            ShellUtil.run("sudo mount -t cifs -o username=liuwbnas,password=54liuWENBIN. //192.168.1.6/home/raspberrypi /root/nas &");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
