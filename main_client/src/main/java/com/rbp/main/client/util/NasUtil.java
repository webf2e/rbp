package com.rbp.main.client.util;

import java.util.List;

/**
 * Created by liuwenbin on 2017/9/2.
 */
public class NasUtil {

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

    public static void makeNasLink(){
        try{
            ShellUtil.run("sudo mount -t cifs -o username=liuwbnas,password=54liuWENBIN. //192.168.1.6/home/raspberrypi /root/nas &");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
