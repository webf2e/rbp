package com.rbp.server.mongo.service;

import com.rbp.server.mongo.bean.DailyMessage;
import com.rbp.server.mongo.bean.FanStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by liuwenbin on 2017/9/17.
 */
@Service
public class DailyMessageService {

    @Autowired
    private MongoTemplate mongoTemplate;


    public void insert(DailyMessage dailyMessage){
        mongoTemplate.save(dailyMessage);
    }
}
