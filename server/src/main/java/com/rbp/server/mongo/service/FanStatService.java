package com.rbp.server.mongo.service;

import com.rbp.server.mongo.bean.FanStat;
import com.rbp.server.mongo.bean.RbpStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuwenbin on 2017/9/6.
 */

@Service
public class FanStatService {

    @Autowired
    private MongoTemplate mongoTemplate;


    public void insert(FanStat fanStat){
        mongoTemplate.save(fanStat);
    }


    public void delByTime(long timestramp){
        Query query = new Query();
        Criteria criteria = Criteria.where("timestramp").lt(timestramp);
        query.addCriteria(criteria);
        mongoTemplate.remove(query,FanStat.class);
    }

    public List<FanStat> getLastStat(String fanName,int count){
        Query query = new Query();
        Criteria criteria = Criteria.where("fanName").is(fanName);
        query.addCriteria(criteria);
        query.with(new Sort(Sort.Direction.DESC, "timestramp"));
        query.limit(count);
        query.skip(0);
        return mongoTemplate.find(query,FanStat.class);
    }
}
