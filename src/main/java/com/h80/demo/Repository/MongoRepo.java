package com.h80.demo.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.h80.demo.Document.Servers;

public interface  MongoRepo extends MongoRepository<Servers, String> {
    boolean existsByDomain(String domain); 

    Optional<Servers> findByDomain(String domain);
    
    List<Servers> findByStatusFalseAndDownSinceBefore(Instant date); 
}
