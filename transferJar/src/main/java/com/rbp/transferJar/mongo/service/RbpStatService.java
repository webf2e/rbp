package com.rbp.transferJar.mongo.service;

import com.rbp.transferJar.mongo.bean.RbpStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuwenbin on 2017/8/31.
 */
@Service
public class RbpStatService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(RbpStat rbpStat){
        mongoTemplate.save(rbpStat);
    }

    public List<String> getServerNameList(){
        return mongoTemplate.getCollection("rbpStat").distinct("serverName");
    }

    public void delByTime(long timestramp){
        Query query = new Query();
        Criteria criteria = Criteria.where("timestramp").lt(timestramp);
        query.addCriteria(criteria);
        mongoTemplate.remove(query,RbpStat.class);
    }

    public RbpStat getLast(String serverName){
        Query query = new Query();
        Criteria criteria = Criteria.where("serverName").is(serverName);
        query.addCriteria(criteria);
        query.with(new Sort(Sort.Direction.DESC, "timestramp"));
        query.limit(1);
        query.skip(0);
        return mongoTemplate.findOne(query,RbpStat.class);
    }

    public List<RbpStat> getLastStat(String serverName,int count){
        Query query = new Query();
        Criteria criteria = Criteria.where("serverName").is(serverName);
        query.addCriteria(criteria);
        query.with(new Sort(Sort.Direction.DESC, "timestramp"));
        query.limit(count);
        query.skip(0);
        return mongoTemplate.find(query,RbpStat.class);
    }
}
