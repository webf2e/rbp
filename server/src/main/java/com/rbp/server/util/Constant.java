package com.rbp.server.util;

import org.apache.curator.framework.CuratorFramework;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuwenbin on 2017/9/5.
 */
public class Constant {
    public static HashMap<String,Integer> fanStatMap = new HashMap<>();
    public static HashMap<String,String> fanConfigMap = new HashMap<>();

    public static Map<String,Long> serverTimeMap = new ConcurrentHashMap<>();

    public static CuratorFramework curatorFramework = null;

    public static boolean startFan = false;
}
