package com.h80.demo.Service;

import org.springframework.stereotype.Service;

@Service
public class CleanUpService {

    // private final MongoRepo repo;
    // private final RandomRequestScheduler scheduler;
    // private final EmailManager emailManager ; 

    // public CleanUpService(EmailManager emailManager, MongoRepo repo, RandomRequestScheduler scheduler) {
    //     this.emailManager = emailManager;
    //     this.repo = repo;
    //     this.scheduler = scheduler;
    // }

    // @Scheduled(cron = "0 0 3 * * *")
    // public void deleteDeadServers() {
    //     Instant threshold = Instant.now().minus(7, ChronoUnit.DAYS);

    //     List<Servers> deadServers
    //             = repo.findByStatusFalseAndDownSinceBefore(threshold);
    //     List<String> deadDomains = deadServers.stream()
    //                                         .map(Servers::getDomain)
    //             .collect(Collectors.toList());
    //     emailManager.cleanDownList(deadDomains); 
    //     deadServers.forEach(server -> {
    //         scheduler.stop(server.getId()); 
    //         repo.delete(server);
    //     });
    // }
}
