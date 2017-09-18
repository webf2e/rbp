package com.rbp.server.controller;

import com.rbp.server.mongo.bean.Message;
import com.rbp.server.mongo.service.MessageService;
import com.rbp.server.util.Constant;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * Created by liuwenbin on 2017/9/11.
 */
@RequestMapping(value = "/message")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/get")
    private List<Message> getAll(HttpServletRequest request){
        List<Message> messages = messageService.getAll();
        Collections.sort(messages);
        return messages;
    }
}
