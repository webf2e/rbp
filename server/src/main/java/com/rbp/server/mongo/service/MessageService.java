package com.rbp.server.mongo.service;

import com.google.gson.Gson;
import com.rbp.server.mongo.bean.FanStat;
import com.rbp.server.mongo.bean.Message;
import com.rbp.server.service.ZookeeperService;
import com.rbp.server.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liuwenbin on 2017/9/6.
 */

@Service
public class MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ZookeeperService zookeeperService;

    @Value("${spring.zookeeper.rpb.node}")
    private String baseNode;


    public void insert(Message message){
        mongoTemplate.save(message);
    }

    public List<Message> getAll(){
        Query query = new Query();
        return mongoTemplate.find(query,Message.class);
    }

    public void setMessage(String message){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStramp = System.currentTimeMillis();
        Message msg = new Message();
        msg.setDateTime(format.format(timeStramp));
        msg.setTimestramp(timeStramp);
        try{
            msg.setContent(message);
        }catch(Exception e){
            e.printStackTrace();
        }
        msg.setType("message");
        insert(msg);
        String messageJson = null;
        try{
            messageJson = URLEncoder.encode(new Gson().toJson(msg),"UTF-8");
            //messageJson
            zookeeperService.createNewZnode(baseNode+"/message",messageJson,"PERSISTENT_SEQUENTIAL");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setImgMessage(String message){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStramp = System.currentTimeMillis();
        Message msg = new Message();
        msg.setDateTime(format.format(timeStramp));
        msg.setTimestramp(timeStramp);
        try{
            msg.setContent(message);
        }catch(Exception e){
            e.printStackTrace();
        }
        msg.setType("image");
        insert(msg);
    }

    public void delete(Message message){
        Query query = new Query();
        Criteria criteria = Criteria.where("timestramp").is(message.getTimestramp());
        query.addCriteria(criteria);
        mongoTemplate.remove(query,Message.class);
    }
}
