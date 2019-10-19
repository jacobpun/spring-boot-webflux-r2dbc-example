package com.pk.springbootgithubclient.management;

import java.time.Duration;

import com.pk.springbootgithubclient.service.GithubClient;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GithubHealthIndicator implements HealthIndicator {
    private final GithubClient gitClient;

    @Override
    public Health health() {
        try {
            this.gitClient.fetchEvents("spring-projects", "spring-boot").block(Duration.ofMillis(3000));
            return Health.up().build();
        } catch(Exception e) {
            return Health.down(e).build();
        }
    }
}