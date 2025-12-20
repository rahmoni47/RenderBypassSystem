package com.h80.demo.Service;

import java.net.URI;

import org.springframework.stereotype.Service;

import com.h80.demo.Document.Servers;
import com.h80.demo.Exception.ApiException;
import com.h80.demo.Model.Response;
import com.h80.demo.Repository.MongoRepo;

@Service
public class ManageRequest {

    private final MongoRepo repo;
    private final RandomRequestScheduler requestScheduler;

    public ManageRequest(MongoRepo repo, RandomRequestScheduler requestScheduler) {
        this.repo = repo;
        this.requestScheduler = requestScheduler;
    }

    public Response CreateRequest(String url, String email) {
        String domaine = CheckFormatAndGetDomaine(url);
        if (domaine == null) {
            throw new ApiException("the link format is rong please check your url");
        }
        boolean existsByDomain = repo.existsByDomain(domaine);
        if (existsByDomain) {
            throw new ApiException("There is already a requestScheduler for this website if you want to edit it use the Id") ; 
        }
        Servers server = Servers.builder()
                .url(url)
                .email(email)
                .domain(domaine)
                .build();
        server = repo.save(server);
        requestScheduler.start(url);
        return Response.builder()
                .status("Created")
                .message("New request Scheduler was Created Successfully with id :" + server.getId())
                .build();
    }

    public Response DeleteRequest(String id) {
        Servers server = repo.findById(id)
                .orElseThrow(() -> new ApiException("There is no request Scheduler with this id :" + id));
        repo.deleteById(id);
        requestScheduler.stop(id);
        return Response.builder()
                .status("Deleted")
                .message("request Scheduler with id :" + id + "is deleted Successfully")
                .build();
    }

    public String CheckFormatAndGetDomaine(String url) {
        try {
            URI uri = new URI(url);

            String host = uri.getHost();

            if (host == null) {
                uri = new URI("http://" + url);
                host = uri.getHost();
            }

            return host;
        } catch (Exception e) {
            return null;
        }
    }
}
