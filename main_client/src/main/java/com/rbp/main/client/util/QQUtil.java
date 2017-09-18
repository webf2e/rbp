package com.rbp.main.client.util;

import java.util.List;

/**
 * Created by liuwenbin on 2017/9/11.
 */
public class QQUtil {

    public static void sendMessage(String message) throws Exception{
        List<String> lines = ShellUtil.run("java RobotUtil \"" + message + "\"");
        for(String line : lines){
            System.out.println(line);
        }
    }

    public static void sendImgMessage(String imgPath) throws Exception{
        List<String> lines = ShellUtil.run("java RobotImgUtil \"" + imgPath + "\"");
        for(String line : lines){
            System.out.println(line);
        }
    }
}
