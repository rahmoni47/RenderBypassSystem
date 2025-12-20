package com.h80.demo.Service;

import org.springframework.stereotype.Service;

import com.h80.demo.Repository.MongoRepo;

@Service
public class EmailManager {

    private final MongoRepo repo;

    public EmailManager(MongoRepo repo) {
        this.repo = repo;
    }
    
}
