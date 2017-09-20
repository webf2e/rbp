package com.rbp.client.controller;

import com.rbp.client.util.ShellUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuwenbin on 2017/9/5.
 */

/**
 * 风扇控制的controller
 */
@RestController
@RequestMapping("/fan")
public class FanController {

    @RequestMapping("/operate")
    public String operate(HttpServletRequest request){
        String state = request.getParameter("state");
        try{
            ShellUtil.run("sh /root/java_proj/bin/fan.sh " + state + " 18 16");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "OK";
    }
}
