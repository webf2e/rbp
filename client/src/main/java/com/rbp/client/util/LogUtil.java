package com.rbp.client.util;

import java.text.SimpleDateFormat;

/**
 * Created by liuwenbin on 2017/9/7.
 */
public class LogUtil {

    public static void log(String content){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(System.currentTimeMillis()) + " -- " + content);
    }
}
