package com.h80.demo.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.h80.demo.Exception.ApiException;
import com.h80.demo.Model.CreateRequestDTO;
import com.h80.demo.Model.EditRequestDTO;
import com.h80.demo.Model.Response;
import com.h80.demo.Repository.MongoService;
import com.h80.demo.Service.ManageRequest;

import jakarta.validation.Valid;

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
    public Response createrequestScheduler(@Valid @RequestBody CreateRequestDTO dto) {
        return manager.CreateRequest(dto.getUrl(), dto.getEmail());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response EditServerInfo(@PathVariable String id, @Valid @RequestBody EditRequestDTO dto) {
        try {
            mongoService.SetMail(id, dto.getEmail());
            return Response.builder()
                            .status("Updated")
                            .message("email updated Successfully")
                    .build();
        } catch (Exception e) {
            throw new ApiException("There is no project with this id please check again");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleterequestScheduler(@PathVariable String id){
        manager.DeleteRequest(id);
        return Response.builder()
                        .status("Deleted")
                        .message("The monitor for id : "+id+"is Deleted Successfully")
                .build();  
    }
    // this endpoint is only for keeping this server online
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getEndpoit() {
        return Map.of("message", "nothing");
    }
}
