package com.rbp.transferJar.mongo.service;


import com.rbp.transferJar.mongo.bean.RbpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuwenbin on 2017/8/31.
 */
@Service
public class RbpInfoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(RbpInfo rbpInfo){
        mongoTemplate.save(rbpInfo);
    }

    public RbpInfo getByServerName(String serverName){
        Query query = new Query();
        Criteria criteria = Criteria.where("serverName").is(serverName);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query,RbpInfo.class);
    }

    public void update(RbpInfo rbpInfo){
        mongoTemplate.save(rbpInfo);
    }

    public List<RbpInfo> getAll(){
        return mongoTemplate.findAll(RbpInfo.class);
    }
}
