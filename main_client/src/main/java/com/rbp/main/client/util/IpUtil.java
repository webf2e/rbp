package com.rbp.main.client.util;

import java.util.List;

public class IpUtil {
    public static String getIp() {
        try{
            List<String> contents = ShellUtil.run("ifconfig");
            for(String content : contents){
                if(content.contains("broadcast")){
                    return content.split("\\s+")[1];
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "can_not_get_ip";
    }
}

