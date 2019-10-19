package com.pk.springbootgithubclient.management;

import java.time.Duration;

import com.pk.springbootgithubclient.repository.GithubProjectRepository;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final GithubProjectRepository repo;

    @Override
    public Health health() {
        try {
            this.repo.isDbUp().block(Duration.ofMillis(500));
            return Health.up().build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}