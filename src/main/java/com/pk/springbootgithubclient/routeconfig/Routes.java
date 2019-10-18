package com.pk.springbootgithubclient.routeconfig;

import com.pk.springbootgithubclient.model.RepositoryEvent;
import com.pk.springbootgithubclient.repository.GithubProjectRepository;
import com.pk.springbootgithubclient.service.GithibClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

import java.util.Arrays;

import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
@RequiredArgsConstructor
public class Routes {
    private final GithubProjectRepository repo;
    private final GithibClient client;

    @Bean
    public RouterFunction<ServerResponse> routeConfig() {
        return route()
            .GET("/events/{repoName}", this::fetchEvents)
            .build();
    }

    private Mono<ServerResponse> fetchEvents(ServerRequest req) {
        var gitRepoName = req.pathVariable("repoName");
        return repo.findByRepoName(gitRepoName)
                    .flatMap(proj -> client.fetchEvents(proj.getOrgName(), proj.getRepoName()))
                    .map(Arrays::asList)
                    .flatMap(events -> ok().body(Flux.fromIterable(events), RepositoryEvent.class))
                    .switchIfEmpty(notFound().build());
    }
}