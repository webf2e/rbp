package com.rbp.demo.service;

import com.rbp.demo.util.ShellUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by liuwenbin on 2017/9/3.
 */
@Service
public class GPIOService {

    @PostConstruct
    public void init() throws Exception{
        try{
            ShellUtil.run("sh /root/gpio_test/bin/pan.sh start 16 18");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy(){
        try{
            ShellUtil.run("sh /root/gpio_test/bin/pan.sh stop 16 18");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
