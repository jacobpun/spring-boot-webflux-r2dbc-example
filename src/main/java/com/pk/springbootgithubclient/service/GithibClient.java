package com.pk.springbootgithubclient.service;

import com.pk.springbootgithubclient.config.GithubProperties;
import com.pk.springbootgithubclient.model.RepositoryEvent;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class GithibClient {
    private static final String EVENTS_URL = "https://api.github.com/repos/{owner}/{repo}/issues/events";
    private final WebClient webClient;

    public GithibClient(GithubProperties gp) {
        this.webClient = WebClient
                            .builder()
                            .filter(ExchangeFilterFunctions.basicAuthentication(gp.getUsername(), gp.getPassword()))
                            .build();
    }

    public Mono<RepositoryEvent[]> fetchEvents(String owner, String repo) {
        return this.webClient
                    .get()
                    .uri(EVENTS_URL, owner, repo)
                    .retrieve()
                    .bodyToMono(RepositoryEvent[].class);
    }
}