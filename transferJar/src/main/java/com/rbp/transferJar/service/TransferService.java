package com.rbp.transferJar.service;

import com.rbp.transferJar.mongo.bean.RbpInfo;
import com.rbp.transferJar.mongo.service.RbpInfoService;
import com.rbp.transferJar.util.RemoteShell;
import com.rbp.transferJar.util.ShellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwenbin on 2017/9/1.
 */
@Service
public class TransferService {

    @Autowired
    private RbpInfoService rbpInfoService;

    @PostConstruct
    public void init(){
        String jarFilePath = "/Users/liuwenbin/IdeaProjects/rbp/client/target/client.jar";
        String toFilePath = "/Volumes/raspberrypi/";
        File file = new File(toFilePath + "client.jar");
        if(file.exists()){
            file.delete();
        }
        //转移到目标文件
        try{
            ShellUtil.run("cp " + jarFilePath + " " + toFilePath);
        }catch(Exception e){
            e.printStackTrace();
        }
        List<RbpInfo> list = getIps();//rbpInfoService.getAll();
        for(RbpInfo rbpInfo : list){
            String serverName = rbpInfo.getServerName();
            String ip = rbpInfo.getIp();
            System.out.println(serverName + " : " + ip);
            if(!isExistNasPath(ip)){
                //创建 nas 目录
                System.out.println(serverName+" " + ip+"需要创建 nas 关联");
                makeNasLink(ip);
            }
            if(!isClientJarExist(ip)){
                System.out.println(serverName+" " + ip+"的client.jar不存在");
                continue;
            }
            //先删除项目文件
            deleteProjectJar(ip);
            deletelog(ip);
            //转移项目文件
            transferProjectJar(ip);
            //获取端口
            String pid = getPid(ip);
            if(null == pid){
                System.out.println(serverName+" " + ip+"无client运行");
            }else{
                System.out.println("before PID: " + pid);
                //关闭 client
                killClient(ip,pid);
                System.out.println("killed");
            }
            //启动 client
            startClient(ip);
            System.out.println("started");
            try{
                Thread.sleep(10000);
            }catch(Exception e){
                e.printStackTrace();
            }
            pid = getPid(ip);
            System.out.println("after PID: " + pid);
            System.out.println("==========================");
        }
        System.out.println("over");
    }

    private boolean isExistNasPath(String host){
        List<String> strings = RemoteShell.run(host,"root","123qwe",22,"df -h");
        for(String str : strings){
            if(str.contains("192.168.1.6")){
                return true;
            }
        }
        return false;
    }

    private void makeNasLink(String host){
        RemoteShell.run(host,"root","123qwe",22,"sudo mount -t cifs -o username=liuwbnas,password=54liuWENBIN. //192.168.1.6/home/raspberrypi /root/nas &");
    }

    private boolean isClientJarExist(String host){
        List<String> strings = RemoteShell.run(host,"root","123qwe",22,"ls /root/nas");
        for(String str : strings){
            if(str.contains("client.jar")){
                return true;
            }
        }
        return false;
    }

    private void deleteClientJar(String host){
        RemoteShell.run(host,"root","123qwe",22,"rm -rf /root/nas/client.jar");
    }


    private void deleteProjectJar(String host){
        RemoteShell.run(host,"root","123qwe",22,"rm -rf /root/java_proj/client.jar");
    }

    private void deletelog(String host){
        RemoteShell.run(host,"root","123qwe",22,"rm -rf /root/java_proj/*.log");
    }

    private void transferProjectJar(String host){
        RemoteShell.run(host,"root","123qwe",22,"cp /root/nas/client.jar /root/java_proj/client.jar");
    }

    private String getPid(String host){
        List<String> strings = RemoteShell.run(host,"root","123qwe",22,"ps -ef | grep client.jar");
        for (String str : strings){
            if(str.contains("java -jar")){
                return str.split("\\s+")[1];
            }
        }
        return null;
    }

    private void killClient(String host,String pid){
        RemoteShell.run(host,"root","123qwe",22,"kill -9 " + pid);
    }

    private void startClient(String host){
        new Thread(() -> {
            RemoteShell.run(host,"root","123qwe",22,"cd /root/java_proj;./run.sh");
        }).start();
    }

    private List<RbpInfo> getIps(){
        List<RbpInfo> rbpInfos = new ArrayList<>();
        RbpInfo rbpInfo1 = new RbpInfo();
        rbpInfo1.setServerName("ras1");
        rbpInfo1.setIp("192.168.1.11");
        rbpInfos.add(rbpInfo1);

        RbpInfo rbpInfo2 = new RbpInfo();
        rbpInfo2.setServerName("ras2");
        rbpInfo2.setIp("192.168.1.29");
        rbpInfos.add(rbpInfo2);

        RbpInfo rbpInfo3 = new RbpInfo();
        rbpInfo3.setServerName("ras3");
        rbpInfo3.setIp("192.168.1.30");
        rbpInfos.add(rbpInfo3);

        RbpInfo rbpInfo4 = new RbpInfo();
        rbpInfo4.setServerName("ras4");
        rbpInfo4.setIp("192.168.1.31");
        rbpInfos.add(rbpInfo4);

        RbpInfo rbpInfo5 = new RbpInfo();
        rbpInfo5.setServerName("ras5");
        rbpInfo5.setIp("192.168.1.21");
        rbpInfos.add(rbpInfo5);
        return rbpInfos;
    }
}