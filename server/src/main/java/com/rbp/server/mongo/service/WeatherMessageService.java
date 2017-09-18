package com.rbp.server.mongo.service;

import com.rbp.server.mongo.bean.DailyMessage;
import com.rbp.server.mongo.bean.WeatherMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by liuwenbin on 2017/9/17.
 */

@Service
public class WeatherMessageService {

    @Autowired
    private MongoTemplate mongoTemplate;


    public void insert(WeatherMessage weatherMessage){
        mongoTemplate.save(weatherMessage);
    }
}
