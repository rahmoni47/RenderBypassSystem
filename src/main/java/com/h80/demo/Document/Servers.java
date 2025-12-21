package com.h80.demo.Document;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "servers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Servers {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String url;
    private String email;

    @Builder.Default
    private boolean status = true;

    private String domain;

    private Instant downSince; 
}
