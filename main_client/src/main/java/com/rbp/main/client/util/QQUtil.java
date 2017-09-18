package com.rbp.main.client.util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;

/**
 * Created by liuwenbin on 2017/9/11.
 */
public class QQUtil {

    public static void sendMessage(String message) throws Exception{
        ShellUtil.run("java RobotUtil \"" + message + "\"");
    }

    public static void sendImgMessage(String imgPath) throws Exception{
        ShellUtil.run("java RobotImgUtil \"" + imgPath + "\"");
    }
}
