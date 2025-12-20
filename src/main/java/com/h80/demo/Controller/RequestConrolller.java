package com.h80.demo.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.h80.demo.Model.Response;
import com.h80.demo.Service.ManageRequest;

@RestController
public class RequestConrolller {
    private final ManageRequest manager;

    public RequestConrolller(ManageRequest manager) {
        this.manager = manager;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createrequestScheduler(@RequestParam String url,@RequestParam String email) {
        return manager.CreateRequest(url, email); 
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Response deleterequestScheduler(@PathVariable String id){
        return manager.DeleteRequest(id) ; 
    }
}
