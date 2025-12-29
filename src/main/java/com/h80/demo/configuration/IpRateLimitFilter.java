package com.h80.demo.configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;


@Component
public class IpRateLimitFilter implements WebFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket newBucket() {
        Refill refill = Refill.greedy(30, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(30, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String ip = extractClientIp(exchange);
        Bucket bucket = buckets.computeIfAbsent(ip, k -> newBucket());

        if (bucket.tryConsume(1)) {
            return chain.filter(exchange);
        }

        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }

    private String extractClientIp(ServerWebExchange exchange) {
        String forwarded = exchange.getRequest()
                .getHeaders()
                .getFirst("X-Forwarded-For");

        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }

        return exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress();
    }
}
