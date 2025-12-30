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

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Service
public class RandomRequestScheduler {
    private final WebClient webclient;
    private final TaskScheduler taskScheduler;
    private final MongoRepo repo;
    private final Random random;
    private final EmailManager emailManager; 
    private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();
    private static final ScheduledFuture<?> DUMMY_FUTURE
            = new ScheduledFuture<>() {
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public Object get() {
            return null;
        }

        @Override
        public Object get(long timeout, java.util.concurrent.TimeUnit unit) {
            return null;
        }

        @Override
        public long getDelay(java.util.concurrent.TimeUnit unit) {
            return 0;
        }

        @Override
        public int compareTo(java.util.concurrent.Delayed o) {
            return 0;
        }
    };


    public RandomRequestScheduler(EmailManager emailManager, Random random, MongoRepo repo, TaskScheduler taskScheduler, WebClient webclient) {
        this.emailManager = emailManager;
        this.random = random;
        this.repo = repo;
        this.taskScheduler = taskScheduler;
        this.webclient = webclient;
    }

    

    @PostConstruct
    public void init() {
        // repo.findAll()
        //         .stream()
        //         .forEach(task -> start(task.getUrl()));
        start("");
    }
    
    public void start(String url) {
        if (tasks.containsKey(url))
            return;
        tasks.put(url, DUMMY_FUTURE); 
        scheduleNext(url);
    }
    
    public void scheduleNext(String url) {
        if(!tasks.containsKey(url))
            return ; 
        long deley = random.nextInt(5 * 60 * 1000);
        ScheduledFuture<?> future = taskScheduler.schedule( ()-> sendRequest(url), Instant.now().plusMillis(deley)) ; 
        tasks.put(url, future) ; 
    }

    public void sendRequest(String url) {
        if (!tasks.containsKey(url))
            return;
        webclient.get()
                .uri(url)
                .exchangeToMono(response -> {
                    if(!(response.statusCode().value()==200))
                        emailManager.ServerIsDown(url);
                    if(response.statusCode().value()==200)
                        emailManager.ServerIsUp(url);
                    return Mono.empty();  
                })
                .subscribe();
        scheduleNext(url);
    }
    
    public void stop(String url) {
        System.out.println("inside stop func");
        ScheduledFuture<?> future = tasks.remove(url);
        if (future != null) {
            System.out.println("now i did put the false in cancel");
            future.cancel(false);
        }
    }
}
