package com.h80.demo.Service;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.h80.demo.Repository.MongoRepo;

@Service
public class RandomRequestScheduler {
    private final WebClient webclient;
    private final TaskScheduler taskScheduler;
    private final MongoRepo repo;
    private final Random random;
    private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    public RandomRequestScheduler(WebClient webclient, TaskScheduler taskScheduler, MongoRepo repo, Random random) {
        this.webclient = webclient;
        this.taskScheduler = taskScheduler;
        this.repo = repo;
        this.random = random;
        repo.findAll()
            .stream()
            .forEach(task -> start(task.getId()));
    }
    
    public void start(String url) {
        if (tasks.containsKey(url))
            return;
        scheduleNext(url);
    }
    
    public void scheduleNext(String url) {
        long deley = random.nextInt(5 * 60 * 1000);
        ScheduledFuture future = taskScheduler.schedule( ()-> sendRequest(url), Instant.now().plusMillis(deley)) ; 
        tasks.put(url, future) ; 
    }

    public void sendRequest(String url) {
        if (!tasks.containsKey(url))
            return;
        webclient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
        scheduleNext(url);
    }
    
    public void stop(String url) {
        ScheduledFuture<?> future = tasks.remove(url);
        if (future != null) {
            future.cancel(false);
        }
    }
}
