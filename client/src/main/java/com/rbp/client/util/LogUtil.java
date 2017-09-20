package com.rbp.client.util;

import java.text.SimpleDateFormat;

/**
 * 日志输出类
 */
public class LogUtil {

    /**
     * 输出日志内容
     * @param content
     */
    public static void log(String content){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(System.currentTimeMillis()) + " -- " + content);
    }
}
