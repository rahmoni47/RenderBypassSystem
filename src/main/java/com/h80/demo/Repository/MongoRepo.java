package com.h80.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.h80.demo.Document.Servers;

public interface  MongoRepo extends MongoRepository<Servers, String> {

}
