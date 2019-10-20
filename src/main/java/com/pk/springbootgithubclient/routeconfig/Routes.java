package com.pk.springbootgithubclient.routeconfig;

import com.pk.springbootgithubclient.model.GithubProject;
import com.pk.springbootgithubclient.model.RepositoryEvent;
import com.pk.springbootgithubclient.repository.GithubProjectRepository;
import com.pk.springbootgithubclient.service.GithubClient;

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
    private final GithubClient client;
    
    @Bean
    public RouterFunction<ServerResponse> routeConfig() {
        return route()
            .GET("/repos", this::fetchRepos)
            .GET("/events/{repoName}", this::fetchEvents)
            .GET("/admin/projects", this::fetchProjects)
            .build();
    }

    private Mono<ServerResponse> fetchRepos(ServerRequest req) {
        var repoNameFlux = repo.findAll().map(GithubProject::getRepoName);
        return ok().body(repoNameFlux, String.class);
    }

    private Mono<ServerResponse> fetchEvents(ServerRequest req) {
        var gitRepoName = req.pathVariable("repoName");        
        return repo.findByRepoName(gitRepoName)
                    .flatMap(proj -> client.fetchEvents(proj.getOrgName(), proj.getRepoName()))
                    .map(Arrays::asList)
                    .flatMap(events -> ok().body(Flux.fromIterable(events), RepositoryEvent.class))
                    .switchIfEmpty(notFound().build());
    }

    private Mono<ServerResponse> fetchProjects(ServerRequest req) {
        return ok().body(this.repo.findAll(), GithubProject.class);
    }
}