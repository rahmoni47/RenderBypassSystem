package com.h80.demo.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.h80.demo.Exception.ApiException;
import com.h80.demo.Model.Response;
import com.h80.demo.Repository.MongoService;
import com.h80.demo.Service.ManageRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
public class RequestConrolller {
    private final ManageRequest manager;
    private final MongoService mongoService;

    public RequestConrolller(ManageRequest manager, MongoService mongoService) {
        this.manager = manager;
        this.mongoService = mongoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createrequestScheduler(@RequestParam String url, @RequestParam @Email @NotBlank String email) {
        return manager.CreateRequest(url, email);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response EditServerInfo(@PathVariable String id, @RequestParam @Email @NotBlank String email) {
        try {
            mongoService.SetMail(id, email);
            return Response.builder()
                            .status("Updated")
                            .message("email updated Successfully")
                    .build();
        } catch (Exception e) {
            throw new ApiException("There is no project with this id please check again");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleterequestScheduler(@PathVariable String id){
        manager.DeleteRequest(id) ; 
    }
}
