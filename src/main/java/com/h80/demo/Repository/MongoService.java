package com.h80.demo.Repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.h80.demo.Document.Servers;

@Repository
public class MongoService {
    private final MongoTemplate mongoTemplate;

    public MongoService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    public void SetMail(String id, String email) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("email", email);
        mongoTemplate.updateFirst(query, update, Servers.class); 
    }
}
